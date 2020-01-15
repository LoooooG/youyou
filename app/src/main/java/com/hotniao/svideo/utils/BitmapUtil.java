package com.hotniao.svideo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：图片操作
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class BitmapUtil {


    /**
     * 保存图片到系统相册目录
     *
     * @param bmp        位图对象
     * @param fileFolder 图片目录
     * @param filename   图片名称
     * @return 保存地址
     */
    public static String saveImageToSystemAlbum(Context context, Bitmap bmp, String fileFolder, String filename) {
        OutputStream stream = null;
        try {
            File saveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileFolder);
            if (!saveDirectory.exists())
                saveDirectory.mkdirs();
            File saveFile = new File(saveDirectory, filename);
            if (!saveFile.exists())
                saveFile.createNewFile();
            stream = new BufferedOutputStream(new FileOutputStream(saveFile.getAbsolutePath()));
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            int quality = 100;
            bmp.compress(format, quality, stream);

            // 发送系统广播更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(saveFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return saveFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveImageToSystemAlbum(Context context, File file, String fileFolder, String filename) {
        OutputStream stream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            File saveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileFolder);
            if (!saveDirectory.exists())
                saveDirectory.mkdirs();
            File saveFile = new File(saveDirectory, filename);
            if (!saveFile.exists())
                saveFile.createNewFile();

            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            stream = new BufferedOutputStream(new FileOutputStream(saveFile.getAbsolutePath()));

            byte[] read = new byte[1024];
            while (bufferedInputStream.read(read) != -1) {
                stream.write(read);
            }
            stream.flush();

            // 发送系统广播更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(saveFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return saveFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

