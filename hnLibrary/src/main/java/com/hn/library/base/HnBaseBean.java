package com.hn.library.base;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：HnMall
 * 类描述：
 * 创建人：kevinxie
 * 创建时间：2017/1/5 14:04
 * 修改人：
 * 修改时间：2017/1/5 14:04
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBaseBean<T> {
    /**
     * c : 1
     * m :
     * d : null
     * consume : 0.99992752075195
     */

    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "HnBaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
