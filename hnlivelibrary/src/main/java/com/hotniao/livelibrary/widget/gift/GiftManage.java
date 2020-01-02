package com.hotniao.livelibrary.widget.gift;

import android.content.Context;

import com.hotniao.livelibrary.R;

import java.util.HashMap;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：礼物管理
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class GiftManage {


    private static HashMap<String, Integer> mLeftGiftMap;

    /**
     * 用户配置礼物,如有新的礼物加入,add到集合即可(必须先初始化)
     *
     * @param context
     */
    public static void init(Context context) {
        mLeftGiftMap = new HashMap<>();
        mLeftGiftMap.put("123", R.drawable.default_live_head);
    }
}
