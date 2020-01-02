package com.hn.library.http;

/**
 * 基本响应数据模型(对应接口数据模型)
 */
public class BaseResponseModel {

    public boolean result = true;// 状态信息
    private int    c =-1; //	返回状态码
    private String m=""; //	提示信息

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public BaseResponseModel setResult(boolean result) {
        this.result = result;
        return this;
    }


}
