package com.hotniao.livelibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yh.wang on 2016/11/28.
 */

public class HnLiveStreamUtils {

    public static final boolean IS_USING_STREAMING_JSON = false;

    // 区分主播和副主播
    public static final int RTC_ROLE_ANCHOR = 0x01;
    public static final int RTC_ROLE_VICE_ANCHOR = 0x02;

    // 用户 ID 可以使用业务上的用户 ID
    // 这里为了演示方便，直接使用了随机值
    public static final String DEFAULT_RTC_USER_ID = UUID.randomUUID().toString();

    // 业务服务器的地址，需要能提供推流地址、播放地址、RoomToken
    // 这里原本填写的是七牛的测试业务服务器，现在需要改写为客户自己的业务服务器
    // 例如：http://www.xxx.com/api/
    public static final String APP_SERVER_BASE = "http://192.168.1.45:9008/example/test.php";

    // 为了 Demo 的演示方便，建议服务器提供一个获取固定推流地址的链接
    // 传给服务器一个 “房间号” ，由服务器根据 “房间号” 返回一个固定的推流地址
    // 例如：http://www.xxx.com/api/stream/room001
    // 这个 “房间号” 必须是业务服务器事先手动为 “主播” 创建的 “连麦房间号”，不能随意设置
    public static String requestPublishAddress(String roomName) {
        if (IS_USING_STREAMING_JSON) {
            return requestStreamJson(roomName);
        } else {
            return requestStreamURL(roomName);
        }
    }

    // 直接使用 URL 地址进行推流
    private static String requestStreamURL(String roomName) {
        String url = APP_SERVER_BASE + "/stream/url/" + roomName;
        Map<String, String> params=new HashMap<>();
        params.put("stream",roomName);
        params.put("username","1");
        params.put("step","2");
        String json = doRequest("POST", APP_SERVER_BASE, params);
        try {
            JSONObject jsonObject=new JSONObject(json);
            String d = jsonObject.getString("d");
            JSONObject jsonObject1=new JSONObject(d);
            String token = jsonObject1.getString("pushlive");
            return token;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 使用 StreamJson 进行推流
    private static String requestStreamJson(String roomName) {
        String url = APP_SERVER_BASE + "/stream/json/" + roomName;
      /*  Map<String, String> params=new HashMap<>();
        params.put("stream","test001");
        params.put("username","1");
        params.put("step","1");*/
        return doRequest("POST", url,null);
    }

    // 为了 Demo 的演示方便，建议服务器提供一个获取固定播放地址的链接
    // 传给服务器一个 “房间号” ，由服务器根据 “房间号” 返回一个固定的播放地址，跟上面推流地址“匹配”
    // 例如：http://www.xxx.com/api/play/room001
    // 这个 “房间号” 必须是业务服务器事先手动为 “主播” 创建的 “连麦房间号”，不能随意设置
    public static String requestPlayURL(String roomName) {
        Map<String, String> params=new HashMap<>();
        params.put("stream",roomName);
        params.put("step","3");
//        return doRequest("GET", APP_SERVER_BASE,params);
        String json = doRequest("POST", APP_SERVER_BASE, params);
        try {
            JSONObject jsonObject=new JSONObject(json);
            String d = jsonObject.getString("d");
            JSONObject jsonObject1=new JSONObject(d);
            String token = jsonObject1.getString("live");
            return token;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 为了 Demo 的演示方便，建议服务器提供一个获取 "RoomToken" 的链接
    // 传给服务器 "用户名" 和 “房间号” ，由服务器根据 "用户名" “房间号” 生成一个 roomToken
    // 客户端再以这个 "用户名"、“房间号”、“roomToken” 去加入房间
    // 例如：http://www.xxx.com/api/room/room001/user/user001/token
    // 这个 “房间号” 必须是业务服务器事先手动为 “主播” 创建的 “连麦房间号”，不能随意设置
    public static String requestRoomToken(String roomName) {
        roomName="test001";
        //  String url = APP_SERVER_BASE + "/room/" + roomName + "/user/" + DEFAULT_RTC_USER_ID + "/token";
        Map<String, String> params=new HashMap<>();
        params.put("stream",roomName);
        params.put("username","1");
        params.put("step","1");
        String json = doRequest("POST", APP_SERVER_BASE, params);
        try {
            JSONObject jsonObject=new JSONObject(json);
            String d = jsonObject.getString("d");
            JSONObject jsonObject1=new JSONObject(d);
            String token = jsonObject1.getString("token");
            return token;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 发送 HTTP 请求获取相关的地址信息
    private static String doRequest(String method, String url,Map<String, String> params) {
//        if ("POST".equals(method)) {
//            return new String(HttpUtils.sendHttpPost(url, params));
//        } else if ("GET".equals(method)) {
//            return new String(HttpUtils.sendHttpGet(url, params).toString()) ;
//
//        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
