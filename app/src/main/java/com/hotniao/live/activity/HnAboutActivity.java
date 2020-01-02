package com.hotniao.live.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.HnAboutModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述： 关于我们
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAboutActivity extends BaseActivity {


    @BindView(R.id.iv_icon)
    FrescoImageView ivIcon;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;

    private CommRecyclerAdapter mAdapter;
    private List<HnAboutModel.DBean.AboutUsBean> mData = new ArrayList<>();


    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setShowBack(true);
        setTitle(R.string.str_about);
        String version = HnUtils.getVersionName(this);
        tvVersion.setText(version);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_about)).setText(mData.get(position).getTitle());
                holder.getView(R.id.tv_about).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HnWebActivity.luncher(HnAboutActivity.this,mData.get(position).getTitle(), HnUrl.USER_APP_ABOUTS_DETAIL+"?about_us_id="+mData.get(position).getId(),HnWebActivity.About);
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_about;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void getInitData() {
        getData();
    }

    private void getData() {
        HnHttpUtils.postRequest(HnUrl.USER_APP_ABOUTS, null, HnUrl.USER_APP_ABOUTS, new HnResponseHandler<HnAboutModel>(HnAboutModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getD().getAbout_us() != null) mData.clear();
                mData.addAll(model.getD().getAbout_us());
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


}
