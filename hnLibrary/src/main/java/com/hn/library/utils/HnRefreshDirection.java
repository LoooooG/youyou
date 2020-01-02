package com.hn.library.utils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：刷新状态
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public enum HnRefreshDirection {
    TOP(0),
    BOTTOM(1),
    BOTH(2);

    private int mValue;

    HnRefreshDirection(int value) {
        this.mValue = value;
    }

    public static HnRefreshDirection getFromInt(int value) {
        for (HnRefreshDirection direction : HnRefreshDirection.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return BOTH;
    }
}
