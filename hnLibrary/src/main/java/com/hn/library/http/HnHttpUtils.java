package com.hn.library.http;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.hn.library.HnBaseApplication;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;
import static com.hn.library.utils.HnUtils.checkConnectionOk;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：网络请工具类(该网络框架基于AsyncHttp)
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class HnHttpUtils {

    protected static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 设计连接和读取超时时间
     */
    static {
        client.setTimeout(20000);
        client.setResponseTimeout(10000);
    }

    /**
     * 取消某个已标志的请求
     */
    public static void cancelRequest(String tag) {
        client.cancelRequestsByTAG(tag, true);
    }


    /**
     * 请求网络数据
     *
     * @param api                 业务接口
     * @param param               请求参数
     * @param tag                 请求标识
     * @param httpResponseHandler 响应回调
     */
    public static void getRequest(String api, RequestParam param, String tag, HnResponseHandler httpResponseHandler) {

        if (!checkConnectionOk(HnBaseApplication.getContext())) {
            httpResponseHandler.hnErr(REQUEST_NET_ERROR, "连接失败，请检查网络是否正常" + "");
            return;
        }


        //头部添加token
        if (param == null) param = new RequestParam();
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            if (!HnUrl.LOGIN.equals(api) && !HnUrl.LOGIN_QQ.equals(api) && !HnUrl.LOGIN_WX.equals(api) && !HnUrl.LOGIN_SINA.equals(api) && !HnUrl.REGISTER_PHONE.equals(api)) {
                client.addHeader("access_token", token);
                param.put("access_token", token);
            }
        }
        param.put("app_os", "Android");
        param.put("app_version", getVersion());
        param.put("device_id", getUniqueid());

        String url = "";

        if (param != null && param.size() > 0) {
            if (api.toLowerCase().startsWith("http"))
                url = api + "?" + RequestParam.httpFormat(param);//拼接url和参数
            else
                url = NetConstant.SERVER + api + "?" + RequestParam.httpFormat(param);//拼接url和参数
        } else {
            if (api.toLowerCase().startsWith("http"))
                url = api;
            else
                url = NetConstant.SERVER + api;
        }

        HnLogUtils.i(tag, "请求:" + url);

        httpResponseHandler.setTag(tag);

        client.get(url, httpResponseHandler);
    }

    /**
     * post请求网络数据
     *
     * @param api                 业务接口
     * @param params              请求参数
     * @param tag                 请求标识
     * @param httpResponseHandler 响应回调
     */
    public static void postRequest(String api, RequestParams params, String tag, HnResponseHandler httpResponseHandler) {

        if (!checkConnectionOk(HnBaseApplication.getContext())) {
            httpResponseHandler.hnErr(REQUEST_NET_ERROR, "连接失败，请检查网络是否正常" + "");
            return;
        }

        //头部添加token
        if (params == null) params = new RequestParams();
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            if (!HnUrl.LOGIN.equals(api) && !HnUrl.LOGIN_QQ.equals(api) && !HnUrl.LOGIN_WX.equals(api) && !HnUrl.LOGIN_SINA.equals(api) && !HnUrl.REGISTER_PHONE.equals(api)) {
                client.addHeader("access_token", token);
                params.put("access_token", token);
            }
        }
        params.put("app_os", "Android");
        params.put("app_version", getVersion());
        params.put("device_id", getUniqueid());
        if (!api.startsWith("http")) {
            api = NetConstant.SERVER + api;
        }


        if (params != null) {
            HnLogUtils.i(tag, "请求参数:" + api + "?" + params.toString());
        }
        httpResponseHandler.setTag(tag);


        client.post(api, params, httpResponseHandler);
    }


    /**
     * 文件上传
     */
    public static void uploadFile(String path, AsyncHttpResponseHandler httpResponseHandler) {
        FileRequestParam param = new FileRequestParam();

        try {
            param.put("file", new File(path));
        } catch (FileNotFoundException e) {

        }
        //头部添加token
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("access_token", token);
        }
        client.post(NetConstant.FILE_UPLOAD_IMG_API, param, httpResponseHandler);
    }

    /**
     * 视频上传
     */
    public static void uploadVideo(String path, AsyncHttpResponseHandler httpResponseHandler) {
        FileRequestParam param = new FileRequestParam();

        try {
            param.put("file", new File(path));
        } catch (FileNotFoundException e) {

        }
        //头部添加token
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("access_token", token);
        }
        client.post(NetConstant.FILE_UPLOAD_API, param, httpResponseHandler);
    }


    /**
     * 文件下载
     *
     * @param isComplieUrl        是否完整地址
     * @param path
     * @param httpResponseHandler
     */
    public static void downloadFile(boolean isComplieUrl, String path, FileAsyncHttpResponseHandler httpResponseHandler) {
        String url = "";
        if (isComplieUrl) {
            url = path;
        } else {
            url = NetConstant.SERVER + path;
        }


        client.get(url, httpResponseHandler);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = HnBaseApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(HnBaseApplication.getContext().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取手机唯一标识
     *
     * @return
     */
    public static String getUniqueid() {
        String uniqueid = Settings.Secure.getString(HnBaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return uniqueid;
    }


}
