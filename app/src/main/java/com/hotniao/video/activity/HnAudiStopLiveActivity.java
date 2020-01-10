package com.hotniao.video.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hn.library.global.HnUrl;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户端结束直播
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnAudiStopLiveActivity")
public class HnAudiStopLiveActivity extends BaseActivity {


    @BindView(R.id.iv_icon)
    FrescoImageView ivIcon;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_level)
    HnSkinTextView tvLevel;
    @BindView(R.id.tv_live_level)
    TextView tvLiveLevel;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;


    /**
     * 主播id
     */
    private String mAnchor_id;
    /**
     * 是否已关注
     */
    private boolean isFollow = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_audi_stop_live;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
    }

    @Override
    public void getInitData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAnchor_id = bundle.getString("data");
            if (!TextUtils.isEmpty(mAnchor_id)) {
                requestToGetUserInfo();
            }
        }
    }

    /**
     * 获取主播信息
     */
    private void requestToGetUserInfo() {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", "0");
        param.put("user_id", mAnchor_id);
        HnHttpUtils.postRequest(HnLiveUrl.Get_User_Info, param, TAG, new HnResponseHandler<HnUserInfoDetailModel>(this, HnUserInfoDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    updateUi(model.getD());
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
     * 更新ui
     *
     * @param anchor 主播信息
     */
    private void updateUi(HnUserInfoDetailBean anchor) {
        //主播相关信息
        if (anchor != null) {
            //主播头像
            String avator = anchor.getUser_avatar();
            ivIcon.setController(FrescoConfig.getController(avator));
            //主播name
            String name = anchor.getUser_nickname();
            tvNick.setText(name);
            //主播id  优号
            mAnchor_id = anchor.getUser_id();
            //是否已关注
            String isFollows = anchor.getIs_follow();
            if (TextUtils.isEmpty(isFollows) || "N".equals(isFollows)) {//未关注
                isFollow = false;
                tvStart.setText(getResources().getString(R.string.follow_anchor));
            } else {
                isFollow = true;
                tvStart.setText(getResources().getString(R.string.search_on_follow));
            }
            //性别
            String sex = anchor.getUser_sex();
            if ("1".equals(sex)) {//男
                ivSex.setImageResource(R.mipmap.man);
            } else {
                ivSex.setImageResource(R.mipmap.girl);
            }
            //用户等级
            String userLvel = anchor.getUser_level();
            HnLiveLevelUtil.setAudienceLevBg(tvLevel, userLvel, true);
            //主播等级
            String liveLevel = anchor.getAnchor_level();
            tvLiveLevel.setText(liveLevel);
            tvLiveLevel.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.tv_start, R.id.tv_go_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start://关注
                if (TextUtils.isEmpty(mAnchor_id)) return;
                requestToFollow();
                break;
            case R.id.tv_go_home://进入首页
                openActivity(HnMainActivity.class);
                HnAppManager.getInstance().finishActivity();
                break;
        }
    }

    /**
     * 网络请求:关注/取消关注
     */
    private void requestToFollow() {
        showDoing(getResources().getString(R.string.loading), null);
        if (isFollow) {
            HnUserControl.cancelFollow(mAnchor_id, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    done();
                    if (isFinishing()) return;
                    isFollow = false;
                    tvStart.setText(getResources().getString(R.string.follow_anchor));
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    done();
                    HnToastUtils.showToastShort(msg);
                }
            });
        } else {
            HnUserControl.addFollow(mAnchor_id, null,new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    done();
                    if (isFinishing()) return;
                    isFollow = true;
                    tvStart.setText(getResources().getString(R.string.search_on_follow));
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    done();
                    HnToastUtils.showToastShort(msg);
                }
            });
        }

    }
}
