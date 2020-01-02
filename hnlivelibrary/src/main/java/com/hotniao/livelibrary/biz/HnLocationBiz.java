package com.hotniao.livelibrary.biz;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hn.library.utils.HnLogUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：xingguang_v1.0.0
 * 类描述：定位工具类
 * 创建人：Administrator
 * 创建时间：2017/8/22 17:19
 * 修改人：Administrator
 * 修改时间：2017/8/22 17:19
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLocationBiz {


    private SimpleDateFormat sdf = null;
    private AMapLocationClient locationClient;//定位客户端

    private OnLocationListener listneer;
    private String TAG = "LocationBiz";

    private HnLocationBiz() {

    }

    public static class LocationBizHolder {
        private static HnLocationBiz instance = new HnLocationBiz();
    }

    public static HnLocationBiz getInsrance() {
        return LocationBizHolder.instance;
    }


    public void startLocation(Context context) {
        initLocation(context);
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation(Context context) {
        HnLogUtils.i(TAG, "开始定位");
        if (locationClient == null) {
            //初始化client
            locationClient = new AMapLocationClient(context);
            //设置定位参数
            locationClient.setLocationOption(getDefaultOption());
            // 设置定位监听
            locationClient.setLocationListener(locationListener);
        }
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {

            if (null != loc) {
                if (loc.getErrorCode() == 0) {
                    //解析定位结果
                    String city = loc.getCity();
                    DecimalFormat df = new DecimalFormat("######0.000000");
                    double latitude = loc.getLatitude();
                    double longitude = (float) loc.getLongitude();
                    String latitudeResult = df.format(latitude);
                    String longitudeResult = df.format(longitude);
                    if (listneer != null) {
                        listneer.onLocationSuccess(loc.getProvince(), loc.getCity(), loc.getAddress(), latitudeResult, longitudeResult);
                    }
                    HnLogUtils.i(TAG, "定位到当前城市:" + city);

                } else {
                    HnLogUtils.i(TAG, "定位错误码：" + loc.getErrorCode() + "错误信息：" + loc.getErrorInfo());
                    if (listneer != null) {
                        listneer.onLocationFail(loc.getErrorInfo(), loc.getErrorCode());
                    }
                }
                stopLocation();
            }

        }
    };


    /**
     * 停止定位
     */
    private void stopLocation() {
        if (locationClient != null) {
            locationClient.stopLocation();
        }
        HnLogUtils.i(TAG, "停止定位");
    }

    public interface OnLocationListener {

        void onLocationSuccess(String province, String city, String address, String latitudeResult, String longitudeResult);

        void onLocationFail(String errorRease, int code);
    }

    public void setOnLocationListener(OnLocationListener listneer) {
        this.listneer = listneer;
    }

}
