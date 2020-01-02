package com.hn.library.http;

/**
 * 请求失败
 */
public interface OnRequestErrCallBack {

    void loginErr(int errCode, String msg);
    //void costErr(int errCode, String msg);
}
