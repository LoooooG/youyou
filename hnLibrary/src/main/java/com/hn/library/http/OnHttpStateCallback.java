package com.hn.library.http;

/**
 * 执行请求回调对象
 * Created by WuJinZhou on 2015/8/8.
 */
public interface OnHttpStateCallback {

    void requestStart();

    void requestFinish();
}
