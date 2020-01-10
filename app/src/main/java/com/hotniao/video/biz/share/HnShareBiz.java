package com.hotniao.video.biz.share;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.model.HnConfigModel;
import com.hn.library.model.HnLoginModel;
import com.hotniao.video.HnApplication;
import com.hotniao.video.model.HnGeneralizeModel;
import com.hotniao.video.model.HnRewardLogModel;
import com.hotniao.video.model.HnShareRuleModel;
import com.hotniao.video.model.HnSignStateModel;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnNoReadMessageModel;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于首页界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/13 13:54
 * 修改人：Administrator
 * 修改时间：2017/9/13 13:54
 * 修改备注：
 * Version:  1.0.0
 */
public class HnShareBiz {

    public static final String SHARE_RULE = "share_rule";
    public static final String REWARD_LOG = "reward_log";
    public static final String GENERALIZE_INDEX = "generalize_index";

    private String TAG = "HnShareBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnShareBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 分享奖励规则
     */
    public void shareRule() {
        HnHttpUtils.postRequest(HnUrl.SHARE_RULE, null, HnUrl.SHARE_RULE, new HnResponseHandler<HnShareRuleModel>(HnShareRuleModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(SHARE_RULE, response, model.getD());
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(SHARE_RULE, model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(SHARE_RULE, errCode, msg);
                }
            }
        });
    }

    /**
     * 奖励记录
     * @param page
     * @param dateType
     */
    public void rewardLog(int page,String dateType) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");
        param.put("pagesize", 20 + "");
        param.put("date_type", dateType);
        HnHttpUtils.postRequest(HnUrl.REWARD_LOG, param, HnUrl.REWARD_LOG, new HnResponseHandler<HnRewardLogModel>(HnRewardLogModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(REWARD_LOG, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(REWARD_LOG, model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(REWARD_LOG, errCode, msg);
                }
            }
        });
    }

    /**
     * 我的推广页首页数据
     */
    public void generalizeIndex() {
        HnHttpUtils.postRequest(HnUrl.GENERALIZE_INDEX, null, HnUrl.GENERALIZE_INDEX, new HnResponseHandler<HnGeneralizeModel>(HnGeneralizeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(GENERALIZE_INDEX, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(GENERALIZE_INDEX, model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(GENERALIZE_INDEX, errCode, msg);
                }
            }
        });
    }
}
