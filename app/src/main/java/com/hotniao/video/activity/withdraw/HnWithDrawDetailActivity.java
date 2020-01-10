package com.hotniao.video.activity.withdraw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnWithDrawDetailAdapter;
import com.hn.library.global.HnUrl;
import com.hotniao.video.model.HnWithDrawDetailModel;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：提现结果详情
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnWithDrawDetailActivity extends BaseActivity {

    public static final int Apply = 1;
    public static final int Detail = 2;

    @BindView(R.id.mTvBack)
    TextView mTvBack;

    @BindView(R.id.mListView)
    ListView mListView;

    private boolean isInvite = false;

    private HnWithDrawDetailAdapter mAdapter;
    private List<HnWithDrawDetailModel.DBean.InfoBean> mData = new ArrayList<>();

    /**
     * @param activity
     * @param id       提现id
     */
    public static void luncher(Activity activity, String id, int joinType) {
        activity.startActivity(new Intent(activity, HnWithDrawDetailActivity.class).putExtra("id", id).putExtra("joinType", joinType));

    }

    public static void luncher(Activity activity, String id, int joinType,boolean isInvite) {
        activity.startActivity(new Intent(activity, HnWithDrawDetailActivity.class).putExtra("id", id).putExtra("joinType", joinType).putExtra("isInvite",isInvite));

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_withdraw_detail;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
//        if (Detail == getIntent().getIntExtra("joinType", 2)) {
        mTvBack.setVisibility(View.GONE);
//        }

        setShowBack(true);
        setTitle(R.string.withdraw_result_detail);
        isInvite = getIntent().getBooleanExtra("isInvite",false);
    }

    @Override
    public void getInitData() {
        mAdapter = new HnWithDrawDetailAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        withDrow(getIntent().getStringExtra("id"));
    }


    @OnClick(R.id.mTvBack)
    public void onClick() {

        HnAppManager.getInstance().finishActivity(HnWithDrawDetailActivity.class);

    }

    /**
     * 提现详情
     *
     * @param id 提现ID
     */
    private void withDrow(String id) {
        RequestParams mParam = new RequestParams();
        mParam.put("withdraw_log_id", id);//用户名

        HnHttpUtils.postRequest(isInvite ? HnUrl.INVITE_WITHDRAW_DETAIL : HnUrl.USER_WITHDRAW_DETAIL, mParam, HnUrl.USER_WITHDRAW_DETAIL, new HnResponseHandler<HnWithDrawDetailModel>(HnWithDrawDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    if (model.getD() == null) return;
                    mData.clear();
                    mData.addAll(model.getD().getInfo());
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
