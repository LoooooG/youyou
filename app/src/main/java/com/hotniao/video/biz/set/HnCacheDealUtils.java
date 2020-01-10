package com.hotniao.video.biz.set;

import android.content.Context;
import android.os.Environment;

import com.hotniao.video.utils.CacheManager;

import static com.tencent.open.utils.Global.getFilesDir;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用于对app的缓存进行计算，清除
 * 创建人：mj
 * 创建时间：2017/9/7 14:54
 * 修改人：Administrator
 * 修改时间：2017/9/7 14:54
 * 修改备注：
 * Version:  1.0.0
 */
public class HnCacheDealUtils {

    private Context context;
    public HnCacheDealUtils(Context context) {
        this.context=context;
    }

    /**
     * 计算缓存
     * 1.Http请求缓存 H
     * 2.ImageLoader图片加载缓存
     * 3.Fresco 图片缓存
     * 4.webView缓存
     * 5.其它下载缓存文件
     */
    public String getCacheSize() {
        long size = 0;
        String cacheDir =context. getCacheDir().getPath();
        try {
            long innerCacheSize = CacheManager.getFolderSize(context.getCacheDir());
            size += innerCacheSize;
            long innerFileCache = CacheManager.getFolderSize(getFilesDir());
            size += innerFileCache;
            if (1 == 2 && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                long extraCache = CacheManager.getFolderSize(context.getExternalCacheDir());
                size += extraCache;
                long extraDCIMSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_DCIM));
                size += extraDCIMSize;
                long extraMOVIESSize = CacheManager.getFolderSize( context.getExternalFilesDir(Environment.DIRECTORY_MOVIES));
                size += extraMOVIESSize;
                long extraALARMSSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_ALARMS));
                size += extraALARMSSize;
                long extraDOCSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                size += extraDOCSize;
                long extraDOWNSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                size += extraDOWNSize;
                long extraPICSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                size += extraPICSize;
                long extraMUSICSize = CacheManager.getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC));
                size += extraMUSICSize;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return CacheManager.getFormatSize(size);
    }

    /**
     * 清除缓存
     */
    public boolean clearCache() {
        if(context==null)  return true;
        CacheManager.deleteFolderFile(context.getCacheDir().getPath(), true);
        CacheManager.deleteFolderFile(context.getFilesDir().getPath(), true);
        CacheManager.deleteFolderFile(context.getExternalCacheDir().getPath(), true);
        return   true;
    }
}
