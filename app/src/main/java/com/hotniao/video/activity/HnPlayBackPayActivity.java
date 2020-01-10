package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.biz.chat.HnFastChatBiz;
import com.hotniao.video.biz.user.userinfo.HnMineBiz;
import com.hotniao.video.model.HnGetChatAnchorInfoModel;
import com.hotniao.video.model.HnPlayBackModel;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.openqq.protocol.imsdk.msg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：yibaobao
 * 类描述：
 * 创建人：oyke
 * 创建时间：2019/2/21 11:31
 * 修改人：oyke
 * 修改时间：2019/2/21 11:31
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPlayBackPayActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.iv_bg)
    FrescoImageView ivBg;
    @BindView(R.id.mIvClose)
    ImageView mIvClose;
    @BindView(R.id.iv_avatar)
    FrescoImageView ivAvatar;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_vip_free)
    TextView tvVipFree;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;

    public static final int PAY = 0;
    public static final int VIP = 1;

    private int type;
    private String userId;
    private HnFastChatBiz mFastChatBiz;
    private HnMineBiz mineBiz;
    private String balance = "0.00";
    private String price;
    private String logId;
    private String imgUrl;

    public static void launcher(Activity activity, int type, HnPlayBackModel.DBean.VideosBean.ItemsBean playBackModel){
        Intent intent = new Intent(activity,HnPlayBackPayActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("userId",playBackModel.getUser_id());
        intent.putExtra("price",playBackModel.getPlayback_price());
        intent.putExtra("logId",playBackModel.getAnchor_live_log_id());
        intent.putExtra("imgUrl",playBackModel.getImage_url());
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.playback_pay_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        mFastChatBiz = new HnFastChatBiz(this);
        mFastChatBiz.setBaseRequestStateListener(this);
        mineBiz = new HnMineBiz(this);
        mineBiz.setBaseRequestStateListener(this);
        type = getIntent().getIntExtra("type",0);
        userId = getIntent().getStringExtra("userId");
        price = getIntent().getStringExtra("price");
        logId = getIntent().getStringExtra("logId");
        imgUrl = getIntent().getStringExtra("imgUrl");
        tvPrice.setText(HnUtils.setTwoPoint(price));
        tvCoin.setText(HnApplication.getmConfig().getCoin());
    }

    @Override
    public void getInitData() {
        mFastChatBiz.getChatUserInfo(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkType(type);
        mineBiz.getCoin();
    }

    private void checkType(int type){
        if(type == PAY){
            tvPayType.setText("付费直播");
            llPrice.setVisibility(View.VISIBLE);
            tvVipFree.setVisibility(View.GONE);
            tvBalance.setVisibility(View.VISIBLE);
            tvRecharge.setVisibility(View.VISIBLE);
            tvNext.setText("付费观看");
        }else{
            tvPayType.setText("VIP直播");
            llPrice.setVisibility(View.GONE);
            tvVipFree.setVisibility(View.VISIBLE);
            tvBalance.setVisibility(View.INVISIBLE);
            tvRecharge.setVisibility(View.INVISIBLE);
            tvNext.setText("我要开通会员");

            if("Y".equals(HnApplication.getmUserBean().getUser_is_member())){
                HnPlayBackVideoActivity.luncher(this, userId,logId, 1,imgUrl);
            }
        }
    }

    @OnClick({R.id.mIvClose,R.id.iv_avatar,R.id.tv_next,R.id.tv_recharge})
    public void click(View view){
        switch (view.getId()){
            case R.id.mIvClose:
                finish();
                break;
            case R.id.iv_avatar:
                HnUserHomeActivity.luncher(this, userId);
                break;
            case R.id.tv_next:
                if(type == PAY){
                    if(!TextUtils.isEmpty(price)){
                        if(Double.parseDouble(balance) < Double.parseDouble(price)){
                            CommDialog.newInstance(this).setContent("余额不足，请先充值").setRightText("去充值").setClickListen(new CommDialog.TwoSelDialog() {
                                @Override
                                public void leftClick() {

                                }

                                @Override
                                public void rightClick() {
                                    openActivity(HnMyRechargeActivity.class);
                                }
                            }).showDialog();
                        }else{
                            HnPlayBackVideoActivity.luncher(this, userId,logId, 1,imgUrl);
                            finish();
                        }
                    }else{
                        HnToastUtils.showToastShort("未获取到回放视频价格");
                        finish();
                    }
                }else{
                    openActivity(HnMyVipMemberActivity.class);
                }
                break;
            case R.id.tv_recharge:
                openActivity(HnMyRechargeActivity.class);
                break;
        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnFastChatBiz.ChatAnchorInfo.equals(type)) {
            HnGetChatAnchorInfoModel model = (HnGetChatAnchorInfoModel) obj;
            if (model != null && model.getD() != null) {
                ivBg.setController(FrescoConfig.getBlurController(model.getD().getUser_avatar()));
                ivAvatar.setController(FrescoConfig.getController(model.getD().getUser_avatar()));
            }
        }else if(HnMineBiz.GET_COIN.equals(type)){
            balance = String.valueOf(obj);
            tvBalance.setText("我的" + HnApplication.getmConfig().getCoin() + "余额：" + HnUtils.setTwoPoint(balance));
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {

    }

}
