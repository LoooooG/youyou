package com.hotniao.live.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.account.HnUserBillEarningActivity;
import com.hotniao.live.adapter.HnMyAccountAdapter;
import com.hotniao.live.biz.user.account.HnMyAccountBiz;
import com.hotniao.live.dialog.HnRechargeMethodDialog;
import com.hotniao.live.eventbus.HnWeiXinPayEvent;
import com.hotniao.live.model.HnAliPayModel;
import com.hotniao.live.model.HnProfileMode;
import com.hotniao.live.model.HnWxPayModel;
import com.hotniao.live.model.PayResult;
import com.hotniao.live.model.bean.HnProfileBean;
import com.hotniao.live.utils.HnAppConfigUtil;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.event.UserCoinReduceEvent;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：个人中心--充值
 * 创建人：mj
 * 创建时间：
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnMyRechargeActivity")
public class HnMyRechargeActivity extends BaseActivity implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {
    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.tv_moeny)
    TextView tvMoeny;
    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    //我的账户业务逻辑类，我的账户相关业务
    private HnMyAccountBiz mHnMyAccountBiz;
    //充值列表适配器
    private HnMyAccountAdapter mApdater;
    public Bundle bundle;

    private List<HnProfileBean.RechargeComboBean> rechargeList = new ArrayList<>();


    @OnClick({R.id.tv_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
//                mHnMyAccountBiz.requestToFourRecharge(custom_max_price_number, custom_min_price_number, money, rechargeList);
            case R.id.tv_agreement://充值协议
                HnWebActivity.luncher(HnMyRechargeActivity.this, getString(R.string.user_exchange_pro), HnUrl.Web_Url + "recharge", HnWebActivity.RechergeAgree);
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.recharge);
        setShowSubTitle(false);
        mSubtitle.setText(R.string.recharge_record);
        mSubtitle.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putInt(HnConstants.Intent.DATA, 4);
                openActivity(HnUserBillEarningActivity.class, bundle);
            }
        });
        mHnMyAccountBiz = new HnMyAccountBiz(this);
        mHnMyAccountBiz.setBaseRequestStateListener(this);
        EventBus.getDefault().register(this);
        initAdpter();

        try {
            if (HnApplication.getmConfig() == null) {
                HnAppConfigUtil.getConfig();
                HnAppConfigUtil.setOnCateListener(new HnAppConfigUtil.OnConfigListener() {
                    @Override
                    public void onSuccess() {
                        mTvId.setText(HnUiUtils.getString(R.string.recharge_id) + "(" + HnApplication.getmConfig().getCoin() + ")");
                    }

                    @Override
                    public void onError(int errCode, String msg) {
                        mTvId.setText(HnUiUtils.getString(R.string.recharge_id) + "(" + HnApplication.getmConfig().getCoin() + ")");
                    }
                });
            } else {
                mTvId.setText(HnUiUtils.getString(R.string.recharge_id) + "(" + HnApplication.getmConfig().getCoin() + ")");
            }

            if (HnApplication.getmUserBean() == null)
                HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                    @Override
                    public void onSuccess(String uid, HnLoginModel model, String response) {
                        if (isFinishing() || mTvName == null) return;
                        mTvName.setText(HnApplication.getmUserBean().getUser_nickname());
                    }

                    @Override
                    public void onError(int errCode, String msg) {

                    }
                });
            else {
                mTvName.setText(HnApplication.getmUserBean().getUser_nickname());

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void getInitData() {
        if (mHnMyAccountBiz != null)
            mHnMyAccountBiz.requestToMyAccount();
    }


    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        getInitData();
    }

    /**
     * 更新界面ui
     *
     * @param model
     */
    private void updateUi(HnProfileBean model) {

        rechargeList = model.getRecharge_combo();

        /*优票余额*/
        if (!TextUtils.isEmpty(model.getUser().getUser_coin())) {
            String coin = model.getUser().getUser_coin();
            HnApplication.getmUserBean().setUser_coin(coin);
            tvMoeny.setText(HnUtils.setTwoPoint(coin));
        } else {
            tvMoeny.setText(HnUtils.setTwoPoint(HnApplication.getmUserBean().getUser_coin()));
        }
        //充值列表
        if (rechargeList != null && rechargeList.size() > 0) {
            //默认选中第一个
            rechargeList.get(0).setChoose(true);
            mApdater.setNewData(rechargeList);
        }


    }

    /**
     * 初始化适配器
     */
    public void initAdpter() {
        if (mApdater == null) {
            mApdater = new HnMyAccountAdapter(this);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            mRecyclerView.setAdapter(mApdater);
            mApdater.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    List<HnProfileBean.RechargeComboBean> rechargeList = adapter.getData();
                    for (int i = 0; i < rechargeList.size(); i++) {
                        if (position == i) {
                            rechargeList.get(i).setChoose(true);
                        } else {
                            rechargeList.get(i).setChoose(false);
                        }
                    }
                    mApdater.notifyDataSetChanged();
                    payDialog(position);
//                    mHnMyAccountBiz.rechargeAli(rechargeList.get(position).getRecharge_combo_id());

                }
            });

        } else {
            mApdater.notifyDataSetChanged();
        }
    }

    private void payDialog(int position) {
        HnRechargeMethodDialog.getInstance(rechargeList.get(position).getRecharge_combo_id()).setClickListen(new HnRechargeMethodDialog.SelDialogListener() {
            @Override
            public void sureClick(String id, String type) {
                if (isFinishing() || mHnMyAccountBiz == null) return;
                if ("aliPay".equals(type)) {
                    mHnMyAccountBiz.rechargeAli(id);
                } else if ("wxPay".equals(type)) {
                    mHnMyAccountBiz.rechargeWxi(id);
                }

            }
        }).show(getSupportFragmentManager(), "pay");
    }


    @Subscribe
    public void onEventBusCallBack(EventBusBean event) {
        if (event != null) {
            if (HnConstants.EventBus.Update_U_Piao.equals(event.getType())) {//更新u票余额
                String dot = (String) event.getObj();
                tvMoeny.setText(HnUtils.setTwoPoint(dot));
            }
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        HnLogUtils.i(TAG, "重新刷新界面数据");
        if (mHnMyAccountBiz != null)
            mHnMyAccountBiz.requestToMyAccount();
    }


    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if ("my_account".equals(type)) {//获取vip列表
            HnProfileMode model = (HnProfileMode) obj;
            if (model != null && model.getD() != null) {
                setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
                updateUi(model.getD());
            }
        } else if ("user_info".equals(type)) {

            EventBus.getDefault().post(new UserCoinReduceEvent(HnApplication.getmUserBean().getUser_coin()));
            tvMoeny.setText(HnUtils.setTwoPoint(HnApplication.getmUserBean().getUser_coin()));
        } else if (HnMyAccountBiz.AliPay.equals(type)) {
            final HnAliPayModel model = (HnAliPayModel) obj;
            if (model.getD() == null || TextUtils.isEmpty(model.getD().getData())) {
                HnToastUtils.showToastShort(model.getC() + ":" + model.getM());
                return;
            } else if (!TextUtils.isEmpty(model.getD().getData())) {
                rechargeAli(model.getD().getData());
            }
        } else if (HnMyAccountBiz.WxPay.equals(type)) {
            HnWxPayModel model = (HnWxPayModel) obj;
            if (model.getD() != null) {
                rechargeWx(model.getD());
            } else {
                HnToastUtils.showToastShort(model.getC() + ":" + model.getM());
                return;
            }
        }
    }


    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if ("my_account".equals(type)) {//获取vip列表
            if (REQUEST_NET_ERROR == code) {
                setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
            } else {
                setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
            }
        } else if (HnMyAccountBiz.AliPay.equals(type) || HnMyAccountBiz.WxPay.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    private void rechargeAli(final String order) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(HnMyRechargeActivity.this);
                Map<String, String> result = alipay.payV2(order, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void rechargeWx(HnWxPayModel.DBean payBean) {
        //appId
        String appid = payBean.getAppid();
        //随机字符串
        String noncestr = payBean.getNoncestr();
        //扩展字段
        String packageX = payBean.getPackageX();
        //微信返回的支付交易会话ID
        String partnerid = payBean.getPartnerid();
        String prepayid = payBean.getPrepayid();
        //时间戳
        String timestamp = payBean.getTimestamp() + "";
        //签名
        String sign = payBean.getSign();

        //微信参数
        final PayReq req = new PayReq();
        req.appId = appid;
        req.nonceStr = noncestr;
        req.packageValue = packageX;
        req.partnerId = partnerid;
        req.prepayId = prepayid;
        req.timeStamp = timestamp;
        req.sign = sign;
        IWXAPI mWxapi = WXAPIFactory.createWXAPI(com.hn.library.utils.HnUiUtils.getContext(), appid);
        mWxapi.registerApp(appid);
        //在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        mWxapi.sendReq(req);
    }


    //微信付款成功事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HnWeiXinPayEvent event) {
        if (event.result) {
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_succeed));
            if (isFinishing()) return;
            payOk();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            /*9000	订单支付成功
              8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
              4000	订单支付失败
              5000	重复请求
              6001	用户中途取消
              6002	网络连接出错
              6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
              其它	其它支付错误
            */
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultMemo = payResult.getMemo();// 同步返回需要验证的信息
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_succeed));
                    payOk();
                } else if ((TextUtils.equals(resultStatus, "8000"))) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_result_unknown));
                } else if ((TextUtils.equals(resultStatus, "4000"))) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_fail));
                } else if ((TextUtils.equals(resultStatus, "5000"))) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_repeat_request));
                } else if ((TextUtils.equals(resultStatus, "6001"))) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.refill_on_cancel));
                } else if ((TextUtils.equals(resultStatus, "6002"))) {
                    HnToastUtils.showToastShort("未知错误");
                } else {
                    HnToastUtils.showToastShort("网络异常");
                }
            }
        }
    };

    private void payOk() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDoing(getResources().getString(R.string.loading), null);
                if (mHnMyAccountBiz != null)
                    mHnMyAccountBiz.requestToUserInfo();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
