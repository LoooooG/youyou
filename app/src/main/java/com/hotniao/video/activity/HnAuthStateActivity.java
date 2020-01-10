package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.biz.live.anchorAuth.HnAnchorAuthenticationBiz;
import com.hotniao.video.model.HnAuthDetailModel;
import com.hotniao.video.utils.HnUiUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：实名认证状态
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAuthStateActivity extends BaseActivity implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {


    @BindView(R.id.iv_state)
    ImageView mIvState;
    @BindView(R.id.mIvState2)
    ImageView mIvState2;
    @BindView(R.id.mIvBack)
    ImageView mIvBack;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;
    @BindView(R.id.mTvState)
    TextView mTvState;
    @BindView(R.id.tv_reauth)
    TextView mTvReauth;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;

    //主播实名认证逻辑类，处理主播认证详情相关业务
    private HnAnchorAuthenticationBiz mHnAnchorAuthenticationBiz;



    public static void luncher(Activity activity){
        activity.startActivity(new Intent(activity,HnAuthStateActivity.class));
    }

    @OnClick({R.id.tv_reauth, R.id.mIvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reauth:
                openActivity(HnAuthenticationActivity.class);
                finish();
                break;
            case R.id.mIvBack:
                finish();
                break;
        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_auth_state;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.real_name_title);
        setShowBack(true);
        setShowTitleBar(false);
        mLoading.setStatus(HnLoadingLayout.Loading);
        mHnAnchorAuthenticationBiz = new HnAnchorAuthenticationBiz(this);
        mHnAnchorAuthenticationBiz.setLoginListener(this);

    }

    @Override
    public void getInitData() {
        mHnAnchorAuthenticationBiz.reuqestToAnchorAuthStatusInfo();
    }

    @Override
    public void requesting() {

    }

    /**
     * 请求成功
     *
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if(isFinishing())return;
        if ("AnchorAuthStatusInfo".equals(type)) {//获取主播认证状态
            setLoadViewState(HnLoadingLayout.Success, mLoading);
            HnAuthDetailModel.DBean data = (HnAuthDetailModel.DBean) obj;
            updateUi(data, response);
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if(isFinishing())return;
        if ("AnchorAuthStatusInfo".equals(type)) {//获取主播认证状态
            if (REQUEST_NET_ERROR == code) {
                setLoadViewState(HnLoadingLayout.No_Network, mLoading);
            } else {
                setLoadViewState(HnLoadingLayout.Error, mLoading);
            }
        }
    }

    @Override
    public void onReload(View v) {
        getInitData();
    }


    /**
     * 更新界面ui
     *
     * @param data     状态码
     * @param response 审核失败，拒绝理由
     */
    private void updateUi(HnAuthDetailModel.DBean data, String response) {
        String type = "0";


        if (data != null && "Y".equals(data.getIs_submit())) {
            if ("C".equals(data.getUser_certification_status())) {//用户实名认证状态，C：核对中(Cache)，Y：通过，N：不通过
                type = "1";
            } else if ("Y".equals(data.getUser_certification_status())) {
                type = "2";
            } else if ("N".equals(data.getUser_certification_status())) {
                type = "3";
            }
        }

        switch (type) {
            case "0"://未认证
                mIvState.setImageResource(R.drawable.under_review);
                mIvState.setVisibility(View.GONE);
                mIvBack.setVisibility(View.GONE);
                setShowTitleBar(true);
                setShowBack(true);
                mIvState2.setVisibility(View.VISIBLE);
                mTvState.setVisibility(View.GONE);

                mTvDetail.setVisibility(View.VISIBLE);
                mTvDetail.setText(HnUiUtils.getString(R.string.auth_no_detail));
                mTvDetail.setTextColor(getResources().getColor(R.color.comm_text_color_black_s));
                mTvReauth.setVisibility(View.VISIBLE);
                mTvReauth.setText(HnUiUtils.getString(R.string.auth_imm));
                break;
            case "1"://审核中
                mIvState.setImageResource(R.drawable.under_review);
                setShowTitleBar(false);
                setShowBack(false);
                mIvState2.setVisibility(View.GONE);
                mIvState.setVisibility(View.VISIBLE);
                mIvBack.setVisibility(View.VISIBLE);
                mTvState.setVisibility(View.VISIBLE);

                mTvState.setText(R.string.sublim_success);
                mTvDetail.setVisibility(View.VISIBLE);
                mTvDetail.setText(HnUiUtils.getString(R.string.auth_suc_tip));
                mTvDetail.setTextColor(getResources().getColor(R.color.comm_text_color_black_s));
                mTvReauth.setVisibility(View.GONE);

                break;
            case "2"://审核成功
                mIvState.setImageResource(R.drawable.real_name_authentication);
                setShowTitleBar(false);
                setShowBack(false);
                mIvState2.setVisibility(View.GONE);
                mIvState.setVisibility(View.VISIBLE);
                mIvBack.setVisibility(View.VISIBLE);
                mTvState.setVisibility(View.VISIBLE);

                mTvDetail.setVisibility(View.VISIBLE);
                mTvDetail.setText(HnUiUtils.getString(R.string.certified));
                mTvDetail.setTextColor(getResources().getColor(R.color.comm_text_color_black_s));
                mTvReauth.setVisibility(View.GONE);
                HnToastUtils.showToastShort(getString(R.string.real_name_success));
                finish();
                break;
            case "3"://审核失败
                mIvState.setImageResource(R.drawable.non_approval);
                setShowTitleBar(false);
                setShowBack(false);
                mIvState2.setVisibility(View.GONE);
                mIvState.setVisibility(View.VISIBLE);
                mIvBack.setVisibility(View.VISIBLE);
                mTvState.setVisibility(View.VISIBLE);

                mTvDetail.setVisibility(View.VISIBLE);
                mTvState.setText(R.string.no_pass_real_name);
                mTvDetail.setText(HnUiUtils.getString(R.string.auth_fai_detail) + data.getUser_certification_result());
                mTvDetail.setTextColor(getResources().getColor(R.color.comm_text_color_black_hs));
                mTvReauth.setVisibility(View.VISIBLE);
                mTvReauth.setText(R.string.auth_fai);
                break;
        }
    }


}
