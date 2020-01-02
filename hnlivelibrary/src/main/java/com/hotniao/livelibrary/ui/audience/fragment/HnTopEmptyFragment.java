package com.hotniao.livelibrary.ui.audience.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hn.library.base.BaseFragment;
import com.hotniao.livelibrary.R;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间空
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTopEmptyFragment extends BaseFragment   implements View.OnClickListener{

    private  ImageView mRoomExit;

    @Override
    public int getContentViewId() {
        return R.layout.live_layout_empty_fragment;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoomExit= (ImageView) mRootView.findViewById(R.id.iv_exit);
        mRoomExit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_exit){
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }
}
