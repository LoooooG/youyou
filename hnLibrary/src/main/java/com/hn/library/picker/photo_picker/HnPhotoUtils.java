package com.hn.library.picker.photo_picker;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;

import com.hn.library.R;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：框架库
 * 类描述：图片选择器工具类
 * 创建人：Kevin
 * 创建时间：2016/5/13 9:43
 * 修改人：Kevin
 * 修改时间：2016/5/13 9:43
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPhotoUtils {

    private static final String PHOTO_DATE_FORMAT = "'IMG'_yyyyMMdd_HHmmss";

    /**
     * 产生图片临时存取的Uri
     *
     * @param context
     * @return
     */
    public static Uri generateTempImageUri(Context context) {
        return FileProvider.getUriForFile(context, context.getResources().getString(R.string.file_authorities),
                new File(pathForTempPhoto(context, generateTempPhotoFileName())));
    }

    /**
     * 产生裁剪图片临时的存取Uri
     *
     * @param context
     * @return
     */
    public static Uri generateTempCroppedImageUri(Context context) {
        return FileProvider.getUriForFile(context, context.getResources().getString(R.string.file_authorities),
                new File(pathForTempPhoto(context, generateTempCroppedPhotoFileName())));
    }

    /**
     * 获取图片临时路径
     *
     * @param context
     * @param fileName
     * @return
     */
    private static String pathForTempPhoto(Context context, String fileName) {
        final File dir = context.getCacheDir();
        dir.mkdirs();
        final File f = new File(dir, fileName);
        return f.getAbsolutePath();
    }

    /**
     * 产生图片临时文件名
     *
     * @return
     */
    private static String generateTempPhotoFileName() {
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(PHOTO_DATE_FORMAT, Locale.US);
        return "ContactPhoto-" + dateFormat.format(date) + ".jpg";
    }

    /**
     * 产生裁剪后图片的临时文件名
     *
     * @return
     */
    private static String generateTempCroppedPhotoFileName() {
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(PHOTO_DATE_FORMAT, Locale.US);
        return "ContactPhoto-" + dateFormat.format(date) + "-cropped.jpg";
    }

    public static Bitmap getVideoBg(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bitmap = mmr.getFrameAtTime(0);
        return bitmap;
    }

    /**
     * 位图转文件
     */
    public static File bitmapToFile(Bitmap bm, String fileName) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + HnUtils.getPackageName() + "/images/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(path, fileName);
        long len = myCaptureFile.length();
        try {
            FileOutputStream fops = new FileOutputStream(myCaptureFile);
            BufferedOutputStream bos = new BufferedOutputStream(fops);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return myCaptureFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Given a uri pointing to a bitmap, reads it into a bitmap and returns it.
     *
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        byte[] data = compressBitmap(BitmapFactory.decodeStream(imageStream));
        return BitmapFactory.decodeByteArray(data, 0, data.length);

    }

    /**
     * 质量压缩
     * Creates a byte[] containing the JPEG-compressed bitmap, or null if
     * something goes wrong.
     */
    public static byte[] compressBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Given a uri pointing to a bitmap, reads it into a bitmap and returns it.
     *
     * @throws FileNotFoundException
     */
    public static Bitmap getScaleBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        byte[] data = compressBitmap(BitmapFactory.decodeStream(imageStream));
        return getScaleBitmap(data);
    }


    /**
     * 尺寸压缩
     *
     * @param
     * @return
     */
    public static Bitmap getScaleBitmap(byte[] data) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize

        options.inSampleSize = calculateInSampleSize(options, 300, 500);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);

    }


    /**
     * 计算图片的采样值
     *
     * @param
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / reqHeight);
            final int widthRatio = Math.round((float) width / reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 裁剪图片需要设置的的参数
     *
     * @param intent
     */
    public static void addCropExtras(Intent intent, int width, int height) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
    }

    /**
     * Adds common extras to gallery intents.
     *
     * @param intent   The intent to add extras to.
     * @param photoUri The uri of the file to save the image to.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void addPhotoPickerExtras(Intent intent, Uri photoUri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));
    }

    /**
     * Given an input photo stored in a uri, save it to a destination uri
     */
    public static boolean savePhotoFromUriToUri(Context context, Uri inputUri, Uri outputUri,
                                                boolean deleteAfterSave) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = context.getContentResolver().openAssetFileDescriptor(outputUri, "rw")
                    .createOutputStream();
            inputStream = context.getContentResolver().openInputStream(inputUri);

            final byte[] buffer = new byte[16 * 1024];
            int length;
            int totalLength = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                totalLength += length;
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            HnLogUtils.e("Failed to write photo: " + inputUri.toString() + " because: " + e);
            return false;
        }

        return true;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}


