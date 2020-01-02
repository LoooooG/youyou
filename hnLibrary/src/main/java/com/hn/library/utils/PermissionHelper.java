package com.hn.library.utils;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class PermissionHelper {


    public static final int Open_Location = 0;//开启定位权限
    public static final int Open_LocationSer = 1;//开启定位服务

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        try {
            LocationManager locationManager;
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps || network) {
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 检查权限列表
     *
     * @param context
     * @param op       这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString 如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了
     */
    public static int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString, context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }


    /**
     * 判断定位
     *
     * @return
     */
    public static int isLocationEnabled(Context context) {
        int mLoctType = -1;
        if (!PermissionHelper.isLocServiceEnable(context)) {//检测是否开启定位服务
            mLoctType = Open_LocationSer;
        } else {//检测用户是否将当前应用的定位权限拒绝
            int checkResult = PermissionHelper.checkOp(context, 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
            int checkResult2 = PermissionHelper.checkOp(context, 1, AppOpsManager.OPSTR_FINE_LOCATION);
            if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                mLoctType = Open_Location;
            }
        }
        return mLoctType;
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) return false;

            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) return false;

        }
        return true;
    }


    public static boolean isCameraUseable() {

        Camera mCamera = null;

        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5。MX5通过Camera.open()拿到的Camera对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            return false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return true;
    }


    public static boolean isAudioRecordable() {

        try {
            AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();

            int recordingState = record.getRecordingState();

            if (recordingState == AudioRecord.RECORDSTATE_STOPPED) {
                return false;
            }
            record.release();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private static List<String> permissions = new ArrayList<>();

    public static int getPerSize(Context context) {
        permissions.clear();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.CHANGE_CONFIGURATION);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.RECORD_AUDIO);
//        permissions.add(Manifest.permission.CALL_PHONE);
        int size = 0;
        for (int i = 0; i < permissions.size(); i++) {
            if (hasPermission(context, permissions.get(i))) {
                size++;
            }
        }
        permissions.clear();
        return size;
    }
}
