package com.hotniao.svideo.biz.user.account;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.svideo.model.HnExchangeModel;
import com.loopj.android.http.RequestParams;

public class HnMyExchangeBiz {
    public static final String EXCHANGE="EXCHANGE";
    public static final String EXCHANGE_INFO="EXCHANGE_INFO";

    private String TAG = "HnMyExchangeBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnMyExchangeBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网路请求：获取我的兑换界面数据
     */
    public void requestToMyExchange() {
        HnHttpUtils.getRequest(HnUrl.INVITE_EXCHANGECOMBO, null, TAG, new HnResponseHandler<HnExchangeModel>(context, HnExchangeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        if (model.getD() != null) {
                            listener.requestSuccess(EXCHANGE_INFO, response, model);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(EXCHANGE_INFO, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(EXCHANGE_INFO, errCode, msg);
                }
            }
        });


    }


    /**
     * 网络请求:兑换
     *
     * @param id 套餐Id
     */
    public void doExchange(final int id) {
        RequestParams param = new RequestParams();
        param.put("id", id);
        if (listener != null) {
            listener.requesting();
        }
        HnHttpUtils.postRequest(HnUrl.INVITE_EXCHANGE, param, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(EXCHANGE, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(EXCHANGE, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(EXCHANGE, errCode, msg);
                }
            }
        });
    }

}
