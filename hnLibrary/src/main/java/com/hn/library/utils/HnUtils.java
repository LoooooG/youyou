package com.hn.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.hn.library.HnBaseApplication;
import com.hn.library.R;
import com.hn.library.global.NetConstant;
import com.hn.library.view.HNAlert;
import com.hn.library.view.HnDialog;
import com.hn.library.view.HnLoadingDialog;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.RoundingMode.FLOOR;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：工具类
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class HnUtils {

    private final static SimpleDateFormat FORMAT_Y_M_D = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * json解析器
     */
    public static Gson gson = new Gson();


    /**
     * 两个Double数相乘
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double mulDouble(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个Double数相加
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double addDouble(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相加
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double addDouble(double v1, double v2) {
        double c = v1 + v2;
        BigDecimal bd = new BigDecimal(c);
        double d = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * 获取优币
     *
     * @return
     */
    public static String getCoin() {
        return HnPrefUtils.getString(NetConstant.GlobalConfig.Coin, "");
    }

    /**
     * 获取优票
     *
     * @return
     */
    public static String getDot() {
        return HnPrefUtils.getString(NetConstant.GlobalConfig.Dot, "");
    }


    /**
     * 判断网络上是否有效
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * 获取当前APP的版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取当前APP的版本名称
     */
    public static String getVersionNameNotV(Context context) {
        String VersionName = "1.0.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VersionName;

    }

    /**
     * 获取当前APP的版本名称
     */
    public static String getVersionName(Context context) {
        String VersionName = "V1.0.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VersionName = "V" + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VersionName;

    }

    /**
     * 判断网络是否可用
     */
    public static boolean checkConnectionOk(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return HnBaseApplication.getContext().getPackageName();
    }

    /**
     * 获得状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 将13位时间戳装换为天
     *
     * @param time
     */
    public static String timestamp2Date(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = null;
        if (time.length() == 13) {
            format = sdf.format(new Date(toLong(time)));
        }
        return format;
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 使用ProgressBar实现的进度动画
     */
    public static HnLoadingDialog progressLoading(Context context, String msg, DialogInterface.OnCancelListener cancelListener) {
        HnLoadingDialog loading = new HnLoadingDialog(context, R.style.loading);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading_pb, null);
        if (!TextUtils.isEmpty(msg)) {
            TextView tvMsg = (TextView) view.findViewById(R.id.tv_load_msg);
            tvMsg.setText(msg);
        }
        loading.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        loading.setTag(msg);
        loading.setCancelable(true);// 点击返回键关闭
        loading.setCanceledOnTouchOutside(false);// 点击外部关闭
        loading.setOnCancelListener(cancelListener);
        return loading;
    }

    /**
     * 使用Drawable实现的进度动画
     */
    public static HnLoadingDialog drawableLoading(Context context, String tag, DialogInterface.OnCancelListener cancelListener) {
        AnimationDrawable aniDraw;
        HnLoadingDialog loading = new HnLoadingDialog(context, R.style.loading);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        ImageView loadView = (ImageView) view.findViewById(R.id.loadingImageView);
        aniDraw = (AnimationDrawable) loadView.getBackground();
        aniDraw.start();
        loading.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        loading.setTag(tag);
        loading.setCancelable(true);// 点击返回键关闭
        loading.setCanceledOnTouchOutside(false);// 点击外部关闭
        loading.setOnCancelListener(cancelListener);
        return loading;
    }

    /**
     * 默认只有[确定]和[取消]的交互对话框
     */
    public static AlertDialog alert(Activity context, String title, String description, DialogInterface.OnClickListener listener) {

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(description)
                .setTitle(title)
                .setPositiveButton("确认", listener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 默认只有[确定]和[取消]的交互对话框
     */
    public static HnDialog dialog(Context context, String title, String description, View.OnClickListener listener) {
        return HnDialog.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(false)
                .setView(R.layout.dialog)
                .setTitle(title)
                .setDescription(description)
                .addListener(listener);
    }

    /**
     * 可设定[确定]和[取消]内容及颜色的交互对话框
     */
    public static HnDialog dialog(Context context, String ok, String cancel, int okColorId, int cancelColorId, String title, String description, View.OnClickListener listener) {
        return HnDialog.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(false)
                .setView(R.layout.dialog)
                .setOKText(ok)
                .setCancelText(cancel)
                .okBtnTxtColor(okColorId)
                .cancelBtnTxtColor(cancelColorId)
                .addListener(listener)
                .setTitle(title)
                .setDescription(description);
    }


    /**
     * 手机号码规范验证
     */
    public static boolean isMobileNo(String phoneNum) {
        String telRegex = "[1][3578]\\d{9}";
        Pattern p = Pattern
                .compile(telRegex);
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    /**
     * MD5加密码处理
     */
    public static String Str2MD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 取得当前时间年月日格式的日期字串
     */
    public static String getCurrentDateYMD() {
        return dateFormatYMD(new Date());
    }


    /**
     * 取得某日期年月日格式的日期字串
     */
    public static String dateFormatYMD(Date date) {
        return FORMAT_Y_M_D.format(date);
    }


    /**
     * 将字符串转日期
     *
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }


    /**
     * 将日期转为字符串
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String DateToString(Date date) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


    /**
     * 通过生日计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }


    /**************************************************尺寸换算及取值**************************************/

    /**
     * 获得屏幕px单位高度
     */
    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 获得屏幕px单位宽度
     */
    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 获得屏幕dp单位高度
     */
    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    /**
     * 获得屏幕dp单位宽度
     */
    public static final int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }

    /**
     * dp转px值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px值
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * dp转px值
     */
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * sp转px值
     */
    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    /**************************************检测SD卡信息**********************************************************************************************/
    /**
     * 检测SD卡是否存在
     */
    public static boolean hasSD() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 检测SD卡空余空间是否足够
     */
    public static boolean enoughSD(int size) {
        return getSDFreeSize() >= size;
    }

    /**
     * 检测SD卡空余空间(MB)
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }


    /*****************************************************************
     * 缓存文件大小计算
     ************************************************************************/

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    /*****************************************文件、缓存、清理、删除******************************************************/

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 清除各功能界面信息缓存
     */
    public static void clearCache(Context context, String[] files) {
        for (int i = 0; i < files.length; i++) {
            cleanCustomCache(context.getCacheDir() + File.separator + files[i]);
        }
    }


    //异步请求线程池对象
    protected static AsyncHttpClient client = new AsyncHttpClient();


    /**
     * 设计连接和读取超时时间
     */
    static {
        client.setTimeout(25000);
        client.setResponseTimeout(20000);
    }


    /**
     * 时间戳转为日期格式
     *
     * @param time
     * @return
     */
    public static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        return sf.format(new Date(time * 1000));
    }


    /**
     * 时间戳转为日期格式
     *
     * @param time
     * @return
     */
    public static String getDateToString_2(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date(time));
    }

    public static String getDateToString_3(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sf.format(new Date(time));
    }

    public static String getDateToString_4(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sf.format(new Date(time * 1000));
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }

        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 提示警告弹框
     *
     * @param msg 提示消息
     */
    public static HNAlert alert(Context context, String msg) {
        return HNAlert.builder(context, R.style.PXAlert).setView(R.layout.hn_alert).setMsg(msg);
    }

    /**
     * 提示警告弹框,带监听
     *
     * @param msg 提示消息
     */
    public static HNAlert alert(Context context, String msg, View.OnClickListener listener) {
        return HNAlert.builder(context, R.style.PXAlert).setView(R.layout.hn_alert).setMsg(msg).addOnclickListener(listener);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     * @param context
     */
    public static void hideSoftInputFrom(EditText view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 隐藏键盘
     *
     * @param views
     * @param context
     */
    public static void hideSoftInputFrom(Context context, EditText... views) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (int i = 0; i < views.length; i++) {
            imm.hideSoftInputFromWindow(views[i].getWindowToken(), 0); //强制隐藏键盘
        }

    }

    /**
     * 手机号添加斜杠
     *
     * @param phone
     * @return
     */
    public static String addLineForPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return phone;
        if (isMobileNo(phone)) return phone;
        String firstStr = phone.substring(0, 3);
        String secondStr = phone.substring(3, 7);
        String endStr = phone.substring(7, phone.length());
        return firstStr + "-" + secondStr + "-" + endStr;
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    /**
     * 采用Arouter框架 启动liveLibrary中webscoket服务
     */
    public static void startWebSoccketService() {
        //webscket地址
        String webscketUrl = HnPrefUtils.getString(NetConstant.User.Webscket_Url, "");
        Bundle bundle = new Bundle();
        bundle.putString("websocket_url", webscketUrl);
        ARouter.getInstance().build("/live/HnStartServiceActivity").with(bundle).navigation();
    }

    /**
     * 获取手机唯一标识
     *
     * @return
     */
    public static String getUniqueid(Context context) {
        String uniqueid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return uniqueid;
    }


    /**
     * 保留两位小数  不转单位
     *
     * @param number
     * @return
     */
    public static String setTwoPoint(String number) {
        if (TextUtils.isEmpty(number)) return "0.00";
        try {
            double v = Double.parseDouble(number);
            if (0 == v) return "0.00";
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足1位,会以0补足.
            decimalFormat.setRoundingMode(FLOOR);
            String p = decimalFormat.format((v / 1.0f));//format 返回的是字符串
            return p;
        } catch (Exception e) {
        }
        return "0.00";

    }

    /**
     * 设置无小数点  超过一万有两位
     *
     * @param number
     */
    public static String setNoPoint(String number) {
        if (TextUtils.isEmpty(number)) return "0";
        try {
            double v = Double.parseDouble(number);
            if (v > 10000) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足1位,会以0补足.
                decimalFormat.setRoundingMode(FLOOR);
                String p = decimalFormat.format((v / 10000 * 1.0f)) + HnUiUtils.getString(R.string.ten_thousand);//format 返回的是字符串
                return p;
            } else {
                return (new Double(v)).intValue() + "";
            }
        } catch (Exception e) {
        }
        return "0";
    }

    /**
     * 设置两位数点   转单位
     *
     * @param number
     */
    public static String setTwoPoints(String number) {
        if (TextUtils.isEmpty(number)) return "0.00";
        try {
            double v = Double.parseDouble(number);
            if (v > 10000000) {
                return "999+" + HnUiUtils.getString(R.string.ten_thousand);
            } else if (v > 10000) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足1位,会以0补足.
                decimalFormat.setRoundingMode(FLOOR);
                String p = decimalFormat.format((v / 10000 * 1.0f)) + HnUiUtils.getString(R.string.ten_thousand);//format 返回的是字符串
                return p;
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足1位,会以0补足.
                decimalFormat.setRoundingMode(FLOOR);
                String p = decimalFormat.format((v / 1.0f));//format 返回的是字符串
                return p;
            }
        } catch (Exception e) {
        }
        return "0.00";
    }


    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public static void openActivity(Class<?> pClass, Bundle bundle, Context context) {
        Intent itent = new Intent();
        itent.setClass(context, pClass);
        if (bundle != null) {
            itent.putExtras(bundle);
        }
        itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(itent);
    }



}




