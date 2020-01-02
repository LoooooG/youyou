package com.hotniao.live.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.account.HnUserBillEarningActivity;
import com.hotniao.live.dialog.HnShareDialog;
import com.hotniao.live.model.HnInviteFriendModel;
import com.hotniao.live.utils.HnUserUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请码页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnInviteFriendActivity extends BaseActivity {
    @BindView(R.id.mTvCode)
    TextView mTvCode;
    @BindView(R.id.mIvBanner)
    FrescoImageView mIvBanner;
    @BindView(R.id.mTvCion)
    TextView mTvCion;
    @BindView(R.id.mTvPeople)
    TextView mTvPeople;
    @BindView(R.id.mTvHint)
    TextView mTvHint;
    @BindView(R.id.mTvCoinName)
    TextView mTvCoinName;

    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;
    private HnInviteFriendModel.DBean mDbean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.invite_friend);
        setShowSubTitle(true);
        mShareAPI = UMShareAPI.get(this);
        mShareAction = new ShareAction(this);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/I770-Sans.ttf");
        mTvCion.setTypeface(typeFace);
        mTvPeople.setTypeface(typeFace);


        if(HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR,false)){
            mTvCoinName.setText("已获奖励("+ HnApplication.getmConfig().getDot()+")");
        }else {
            mTvCoinName.setText("已获奖励("+ HnApplication.getmConfig().getCoin()+")");
        }
    }

    @Override
    public void getInitData() {
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //将数据返给SDK处理
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            HnHttpUtils.getRequest(HnUrl.SHARE_ADD_EXPER, null, "share add exper", new HnResponseHandler<BaseResponseModel>(this,BaseResponseModel.class) {
                @Override
                public void hnSuccess(String response) {

                }

                @Override
                public void hnErr(int errCode, String msg) {

                }
            });
        }
    }


    @OnClick({R.id.mLlCoin, R.id.mLlPeople, R.id.mTvCopy, R.id.mTvInvite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mLlCoin:
                if(HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR,false)){
                    startActivity(new Intent(HnInviteFriendActivity.this, HnUserBillEarningActivity.class).putExtra(HnConstants.Intent.DATA, 1));
                }else {
                    startActivity(new Intent(HnInviteFriendActivity.this,BounsAct.class));
                }
                break;
            case R.id.mLlPeople:
                openActivity(HnMyInvitePeopleActivity.class);
                break;
            case R.id.mTvCopy:
                if (mDbean == null || mDbean.getUser() == null) return;
                HnUserUtil.copy(mDbean.getUser().getUser_invite_code(), HnInviteFriendActivity.this);
                break;
            case R.id.mTvInvite:
                if (mDbean == null || mDbean.getShare() == null) return;
                HnInviteFriendModel.DBean.ShareBean mShare = mDbean.getShare();
                HnShareDialog.newInstance(mShareAPI, mShareAction, mShare.getContent(), mShare.getLogo(), mShare.getUrl(), null).show(getFragmentManager(), "share");
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        HnHttpUtils.postRequest(HnUrl.USER_INVITE_DETAIL, null, HnUrl.USER_INVITE_DETAIL, new HnResponseHandler<HnInviteFriendModel>(HnInviteFriendModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getD() == null) return;
                mDbean = model.getD();
                setMessage();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    /**
     * 设置数据
     */
    private void setMessage() {
        if (mDbean == null) return;
        if (mDbean.getInvite() != null) {
            mIvBanner.setImageURI(HnUrl.setImageUri(mDbean.getInvite().getBanner_url()));
            mTvHint.setText(mDbean.getInvite().getTips());
        }
        if (mDbean.getUser() != null) {
            mTvCode.setText(mDbean.getUser().getUser_invite_code());
            mTvCion.setText(mDbean.getUser().getUser_invite_coin_total());
            mTvPeople.setText(mDbean.getUser().getUser_invite_total());
        }
    }
}
