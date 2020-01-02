package com.hotniao.live.muliao.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.hn.library.base.BaseActivity;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.global.HnConstants;
import com.hotniao.live.R;
import com.hotniao.live.eventbus.HnWeiXinPayEvent;
import com.hotniao.live.utils.HnUiUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：微信支付的回调
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_entey;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        //创建wxapi工程
        api = WXAPIFactory.createWXAPI(this, HnPrefUtils.getString(HnConstants.Pay.WEIXINPAY_APPID, ""));
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void getInitData() {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                EventBus.getDefault().post(new HnWeiXinPayEvent());
            } else if (baseResp.errCode == -2) {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_on_cancel));
                EventBus.getDefault().post(new HnWeiXinPayEvent().setResult(false));
            } else {
                HnToastUtils.showToastShort("支付失败");
                EventBus.getDefault().post(new HnWeiXinPayEvent().setResult(false));
            }
            finish();
        }
    }

    public void finishMe() {
        HnAppManager.getInstance().finishActivity(WXPayEntryActivity.class);
    }
}
