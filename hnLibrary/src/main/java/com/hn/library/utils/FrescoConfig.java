package com.hn.library.utils;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hn.library.HnBaseApplication;
import com.hn.library.global.NetConstant;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：图片初始化 缓存
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class FrescoConfig {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 12;//使用的缓存数量

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 20 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 40 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 30 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 40 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 80 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 60 * ByteConstants.MB;//默认图磁盘缓存的最大值

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "hnImageCache";//小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "hnImageCacheDe";//默认图所放路径的文件夹名

    private static ImagePipelineConfig sImagePipelineConfig;


    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = configureCaches(context);
        }
        return sImagePipelineConfig;
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static ImagePipelineConfig configureCaches(Context context) {

        ImagePipelineConfig.Builder configBuilder = null;
        if (PermissionHelper.hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                    FrescoConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                    Integer.MAX_VALUE,                     // Max entries in the cache
                    FrescoConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                    Integer.MAX_VALUE,                     // Max length of eviction queue
                    Integer.MAX_VALUE);                    // Max cache entry size

            //修改内存图片缓存数量
            Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
                @Override
                public MemoryCacheParams get() {
                    return bitmapCacheParams;
                }
            };
            //小图片的磁盘配置
            DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(HnFileUtils.getExternalCacheDir(context))
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)
                    .build();

            //默认图片的磁盘配置
            DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)
                    .build();
            //缓存图片配置
            configBuilder = ImagePipelineConfig.newBuilder(context)
                    .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                    .setMainDiskCacheConfig(diskCacheConfig)
                    .setDownsampleEnabled(true)
                    .setSmallImageDiskCacheConfig(diskSmallCacheConfig);
        } else {
            configBuilder = ImagePipelineConfig.newBuilder(context)
                    .setDownsampleEnabled(true);
        }

        return configBuilder.build();
    }

    /**
     * 加载大图
     *
     * @param url
     * @return
     */
    public static DraweeController getController(String url) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(NetConstant.setImageUri(url))
                .setResizeOptions(new ResizeOptions(HnDimenUtil.dp2px(HnBaseApplication.getContext(), 400),
                        HnDimenUtil.dp2px(HnBaseApplication.getContext(), 400)))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                .setUri(HnUrl.setImageUri(url))
                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .setOldController(draweeView.getController())
//                .setTapToRetryEnabled(true)//加载失败时是否允许重试
//                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();

        return draweeController;
    }

    /**
     * 加载小图
     *
     * @param url
     * @return
     */
    public static DraweeController getHeadController(String url) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(NetConstant.setImageUri(url))
                .setResizeOptions(new ResizeOptions(HnDimenUtil.dp2px(HnBaseApplication.getContext(), 100),
                        HnDimenUtil.dp2px(HnBaseApplication.getContext(), 100)))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                .setUri(HnUrl.setImageUri(url))
                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .setOldController(draweeView.getController())
//                .setTapToRetryEnabled(true)//加载失败时是否允许重试
//                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();

        return draweeController;
    }

    /**
     * 加载高斯模糊
     *
     * @param url
     * @return
     */
    public static AbstractDraweeController getBlurController(String url) {
        try {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(NetConstant.setImageUri(url))
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(6, 10))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            return controller;
        } catch (Exception e) {
        }

        return null;
    }
    /**
     * 加载高斯模糊
     *
     * @param id
     * @return
     */
    public static AbstractDraweeController getBlurController(int id) {
        try {
            ImageRequest request = ImageRequestBuilder.newBuilderWithResourceId(id)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(6, 10))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            return controller;
        } catch (Exception e) {
        }

        return null;
    }
}
