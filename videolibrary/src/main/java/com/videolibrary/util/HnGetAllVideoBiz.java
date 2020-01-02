package com.videolibrary.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tencent.liteav.basic.log.TXCLog;
import com.videolibrary.model.TCVideoFileInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnGetAllVideoBiz {

    private static HnGetAllVideoBiz sInstance;
    private final ContentResolver mContentResolver;
    private final Context mContext;

    public static HnGetAllVideoBiz getInstance(Context context) {
        if (sInstance == null)
            sInstance = new HnGetAllVideoBiz(context);
        return sInstance;
    }

    private HnGetAllVideoBiz(Context context) {
        mContext = context.getApplicationContext();
        mContentResolver = context.getApplicationContext().getContentResolver();
    }

    public ArrayList<TCVideoFileInfo> getAllVideo() {
        ArrayList<TCVideoFileInfo> videos = new ArrayList<TCVideoFileInfo>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        Cursor cursor = mContentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor == null) return videos;

        if (cursor.moveToFirst()) {
            do {
                TCVideoFileInfo fileItem = new TCVideoFileInfo();
                fileItem.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                File file = new File(fileItem.getFilePath());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                if (duration < 0)
                    duration = 0;
                fileItem.setDuration(duration);

                if (fileItem.getFileName() != null && fileItem.getFileName().endsWith(".mp4")) {
                    if (!isVideoDamaged(fileItem)) {
                        videos.add(fileItem);
                    }

                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }

    /**
     * 检测视频是否损坏
     *
     * @param info
     * @return
     */
    private boolean isVideoDamaged(TCVideoFileInfo info) {
        if (info.getDuration() == 0) {
            //数据库获取到的时间为0，使用Retriever再次确认是否损坏
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(info.getFilePath());
            } catch (Exception e) {
                return true;//无法正常打开，也是错误
            }
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (TextUtils.isEmpty(duration))
                return true;
            return Integer.valueOf(duration) == 0;
        }
        return false;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                    int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
