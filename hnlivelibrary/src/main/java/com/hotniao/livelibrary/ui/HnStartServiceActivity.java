package com.hotniao.livelibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.service.HnWebSocketService;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：直播
 * 类描述：该界面不做任何显示，只用于启动服务
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
//@Route(path = "/live/HnStartServiceActivity") // 必须标明注解
public class HnStartServiceActivity extends BaseActivity {
    private String TAG = "HnStartServiceActivity";


    /**
     * websocket服务
     */
    public static final String WEBSCOKET_SERVICE = "WEBSCOKET_SERVICE";
    /**
     * 标识符
     */
    private String type;


    @Override
    public int getContentViewId() {
        return R.layout.activity_hn_start_service;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        Intent intent = new Intent(this, HnWebSocketService.class);
        startService(intent);
        finish();
    }

    @Override
    public void getInitData() {

    }
}
