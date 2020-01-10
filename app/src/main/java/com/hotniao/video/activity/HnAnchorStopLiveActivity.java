package com.hotniao.video.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.model.HnStopLiveModel;
import com.hotniao.video.utils.HnAppConfigUtil;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveDateUtils;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：主播端结束直播
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnAnchorStopLiveActivity")
public class HnAnchorStopLiveActivity extends BaseActivity {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_live_time)
    TextView tvLiveTime;
    @BindView(R.id.tv_people_number)
    TextView tvPeopleNumber;
    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.tv_u_piao)
    TextView tvUPiao;
    @BindView(R.id.mTvFocus)
    TextView mTvFocus;
    @BindView(R.id.mTvCoin)
    TextView mTvCoin;
    @BindView(R.id.mTvShow)
    TextView mTvShow;
    @BindView(R.id.mTvBackMoney)
    TextView mTvBackMoney;
    @BindView(R.id.mEtBackMoney)
    EditText mEtBackMoney;
    @BindView(R.id.mTvCoinName)
    TextView mTvCoinName;
    @BindView(R.id.mLlBackMoney)
    LinearLayout mLlBackMoney;

    //0：免费，1：VIP，2：门票，3：计时
    private String mPayType;
    private String mLiveLogId;

    @OnClick(R.id.mTvGOHome)
    public void onClick() {
        setPlayBackPrice();

    }

    @OnClick(R.id.tv_release_playback)
    public void releasePlayback(){
        if(!TextUtils.isEmpty(mLiveLogId)){
            HnReleasePlayBackActivity.launcher(this,mLiveLogId);
        }
    }



    @Override
    public int getContentViewId() {
        return R.layout.activity_stop_live;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        try {
            if (HnApplication.getmConfig() == null) {
                HnAppConfigUtil.setDefult();
            }
            mTvCoin.setText(HnUiUtils.getString(R.string.get_virtual_coin) + HnApplication.getmConfig().getDot());
            mTvCoinName.setText(HnApplication.getmConfig().getCoin());
            mPayType = getIntent().getExtras().getString("payType");
        } catch (Exception e) {
        }
    }

    @Override
    public void getInitData() {
        requestToExitAnchotLive();
    }


    /**
     * 网络请求：主播退出直播间，结束直播
     */
    public void requestToExitAnchotLive() {
        HnHttpUtils.getRequest(HnUrl.Stop_Live, null, TAG, new HnResponseHandler<HnStopLiveModel>(this, HnStopLiveModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (model.getD() != null && model.getD() != null) {
                        updateUI(model.getD());
                    }
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    /**
     * 网络请求：主播退出直播间，结束直播
     */
    public void setPlayBackPrice() {
        if (mLlBackMoney == null || mLlBackMoney.getVisibility() != View.VISIBLE) {
            HnAppManager.getInstance().finishActivity();
            return;
        }
        if (TextUtils.isEmpty(mEtBackMoney.getText().toString().trim()) || TextUtils.isEmpty(mLiveLogId)) {
            HnAppManager.getInstance().finishActivity();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("anchor_live_log_id", mLiveLogId);
        params.put("price", mEtBackMoney.getText().toString().trim());
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_SET_BACK_PRICE, params, "VIDEO_APP_SET_BACK_PRICE", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    HnAppManager.getInstance().finishActivity();
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


    /**
     * 更新界面数据
     */
    private void updateUI(HnStopLiveModel.DBean data) {
        if (isFinishing()) return;
        if ("Y".equals(data.getIs_create_record())) {
            mTvShow.setVisibility(View.GONE);
//            if ("2".equals(mPayType) || "3".equals(mPayType)) {
//                mTvBackMoney.setVisibility(View.VISIBLE);
//                mLlBackMoney.setVisibility(View.VISIBLE);
//            } else {
//                mTvBackMoney.setVisibility(View.GONE);
//                mLlBackMoney.setVisibility(View.GONE);
//            }

        } else {
            mTvShow.setVisibility(View.VISIBLE);
        }
        mLiveLogId = data.getAnchor_live_log_id();
        //直播时长
        String live_time = data.getLive_time();
        if (!TextUtils.isEmpty(live_time)) {
            String time = HnLiveDateUtils.getLiveTime(Long.valueOf(live_time));
            tvLiveTime.setText(time);
        }
        //直播人数
        String live_people_num = data.getAnchor_live_onlines();
        tvPeopleNumber.setText(live_people_num + "");
        //收到赞
        tvZan.setText(data.getAnchor_live_like_total());
        //优票
        String dot = data.getUser_dot();
        tvUPiao.setText(HnUtils.setTwoPoints(dot));

        mTvFocus.setText(data.getAnchor_live_follow_total());


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
