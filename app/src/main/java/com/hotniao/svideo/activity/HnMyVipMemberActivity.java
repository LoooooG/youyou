package com.hotniao.svideo.activity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alipay.sdk.app.PayTask;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.biz.user.vip.HnVipBiz;
import com.hotniao.svideo.dialog.HnRechargeMethodDialog;
import com.hotniao.svideo.eventbus.HnWeiXinPayEvent;
import com.hotniao.svideo.model.HnAliPayModel;
import com.hotniao.svideo.model.HnVipDataModel;
import com.hotniao.svideo.model.HnWxPayModel;
import com.hotniao.svideo.model.PayResult;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;
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

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述:我的会员
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnMyVipMemberActivity")
public class HnMyVipMemberActivity extends BaseActivity implements BaseRequestStateListener {
    @BindView(R.id.activity_my_vip)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvImg;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mTvCoin)
    TextView mTvCoin;
    @BindView(R.id.tv_level)
    HnSkinTextView mTvLevel;
    @BindView(R.id.mTvLong)
    TextView mTvLong;
    @BindView(R.id.mTvRenew)
    TextView mTvRenew;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mRecyclerPrivilege)
    RecyclerView mRecyclerPrivilege;
    @BindView(R.id.mTvPrice)
    TextView mTvPrice;
    @BindView(R.id.mTvOpen)
    TextView mTvOpen;
    @BindView(R.id.mLlMeal)
    LinearLayout mLlMeal;
    @BindView(R.id.mRlBottom)
    RelativeLayout mRlBottom;

    //套餐   特权
    private CommRecyclerAdapter mMealAdapter;
    private CommRecyclerAdapter mPrivilegeAdapter;

    private List<HnVipDataModel.DBean.VipComboBean> mMealData = new ArrayList<>();
    private List<HnVipDataModel.DBean.VipPrivilegeBean> mPrivilegeData = new ArrayList<>();

    private String mName[] = {HnUiUtils.getString(R.string.vip_type),HnUiUtils.getString(R.string.free_chat)};

    private int mLogs[] = {R.drawable.vip_rights_2,R.drawable.vip_rights_1};


    //选择的套餐
    private int mSelectItem = -1;
    //Vip接口
    private HnVipBiz mHnVipBizp;
    //vip数据
    private HnVipDataModel.DBean mDbean;
    //特殊字体
    private Typeface mTypeFace;
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
            if (msg.what == 1) {
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
            } else if (msg.what == 2) {
                if (mHnVipBizp != null)
                    mHnVipBizp.requestToVipList(HnVipBiz.Vip_Data_Refresh);
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_vip_member;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        setTitle(R.string.vip_center);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mTypeFace = Typeface.createFromAsset(getAssets(), "fonts/I770-Sans.ttf");
        mHnVipBizp = new HnVipBiz(this);
        mHnVipBizp.setBaseRequestStateListener(this);
        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                mHnVipBizp.requestToVipList(HnVipBiz.Vip_Data);
            }
        });
//        mTvCoin.setText(HnApplication.getmConfig().getCoin());
        mTvPrice.setTypeface(mTypeFace);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getInitData() {
        initMealAdapter();
        initPrivilegeAdapter();
        mHnVipBizp.requestToVipList(HnVipBiz.Vip_Data);
    }

    /**
     * 套餐
     */
    private void initMealAdapter() {
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setHasFixedSize(true);

        mMealAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                if (mSelectItem == position) {
                    holder.getView(R.id.mLlPer).setBackgroundResource(R.drawable.shap_orange_raudio_4);
                    holder.getView(R.id.mTvTime).setBackgroundResource(R.drawable.shap_bg_yellow_raudio_right4);
                    holder.getView(R.id.mTvPrice).setSelected(true);
                    holder.getView(R.id.mTvPrice1).setSelected(true);
                    holder.getView(R.id.mTvTime).setSelected(true);
                } else {

                    holder.getView(R.id.mLlPer).setBackgroundResource(R.drawable.shap_gray_raudio_4);
                    holder.getView(R.id.mTvTime).setBackgroundResource(R.drawable.shap_bg_raudio_right4);
                    holder.getView(R.id.mTvPrice).setSelected(false);
                    holder.getView(R.id.mTvPrice1).setSelected(false);
                    holder.getView(R.id.mTvTime).setSelected(false);
                }
                TextView mTvCoin = (TextView) holder.getView(R.id.mTvPrice);
                mTvCoin.setTypeface(mTypeFace);
                if (!TextUtils.isEmpty(mMealData.get(position).getCombo_fee())) {
                    String combo_fee = mMealData.get(position).getCombo_fee();
                    int i = (new Double(Double.parseDouble(combo_fee))).intValue();
                    mTvCoin.setText(HnUtils.setTwoPoint(combo_fee) + "");
                }

                ((TextView) holder.getView(R.id.mTvTime)).setText(mMealData.get(position).getCombo_month() + HnUiUtils.getString(R.string.one_month));
//                ((TextView) holder.getView(R.id.mTvPrice1)).setText(HnApplication.getmConfig().getCoin());
                holder.getView(R.id.mLlPer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectItem = position;
                        mMealAdapter.notifyDataSetChanged();
                        mTvPrice.setText(mMealData.get(position).getCombo_fee());
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_vip_member_meal;
            }

            @Override
             public int getItemCount() {
                return mMealData.size();
            }
        };

        mRecycler.setAdapter(mMealAdapter);
    }

    /**
     * 特权
     */
    private void initPrivilegeAdapter() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerPrivilege.setLayoutManager(manager);
        mRecyclerPrivilege.setHasFixedSize(true);
        mPrivilegeAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
//                ((FrescoImageView) holder.getView(R.id.mIvImg)).setImageURI(HnUrl.setImageUri(mPrivilegeData.get(position).getLogo()));
//                ((TextView) holder.getView(R.id.mTvName)).setText(mPrivilegeData.get(position).getName());
                (holder.getView(R.id.mIvImg)).setBackgroundResource(mLogs[position]);
                ((TextView) holder.getView(R.id.mTvName)).setText(mName[position]);
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_vip_member_privilege;
            }

            @Override
            public int getItemCount() {
                return mLogs.length;
            }
        };
        mRecyclerPrivilege.setAdapter(mPrivilegeAdapter);
    }

    @OnClick({R.id.mIvBack, R.id.mTvRenew, R.id.mTvOpen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mTvRenew://续费
                break;
            case R.id.mTvOpen://开通
                if (mDbean == null || mDbean.getUser() == null || mDbean.getVip_combo() == null)
                    return;
                if (-1 == mSelectItem) {
                    HnToastUtils.showToastShort(getString(R.string.please_vip_meal));
                    return;
                }
                CommDialog.newInstance(HnMyVipMemberActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        payDialog(mMealData.get(mSelectItem).getCombo_id(), mDbean.getOrder_id());
//                        mHnVipBizp.buyVipZFB(mMealData.get(mSelectItem).getCombo_id(), mDbean.getOrder_id());
                    }
                }).setTitle(getString(R.string.buy_Vip)).setContent(getString(R.string.can_sure_buy_vip))
                        .setRightText(getString(R.string.buy)).show();

                break;
        }
    }

    private void payDialog(final String comboId, final String orderId) {
        HnRechargeMethodDialog.getInstance(comboId).setClickListen(new HnRechargeMethodDialog.SelDialogListener() {
            @Override
            public void sureClick(String id, String type) {
                if (isFinishing() || mHnVipBizp == null) return;
                if ("aliPay".equals(type)) {
                    mHnVipBizp.buyVipZFB(comboId, orderId);
                } else if ("wxPay".equals(type)) {
                    mHnVipBizp.buyVipWX(comboId, orderId);
                }

            }
        }).show(getSupportFragmentManager(), "pay");
    }

    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.loading), null);
    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if (HnVipBiz.Vip_Data.equals(type)) {//获取vip列表
            setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            HnVipDataModel model = (HnVipDataModel) obj;
            if (model != null && model.getD() != null || model.getD().getUser() == null) {
                setShowBack(false);
                setShowTitleBar(false);
                updataUI(model.getD());
            } else {
                setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                setShowTitleBar(true);
                setShowBack(true);
                setTitle(R.string.vip_center);


            }
        } else if (HnVipBiz.Buy_Vip_ZFB.equals(type)) {

            final HnAliPayModel model = (HnAliPayModel) obj;
            if (model.getD() == null || TextUtils.isEmpty(model.getD().getData())) {
                HnToastUtils.showToastShort(model.getC() + ":" + model.getM());
                return;
            } else if (!TextUtils.isEmpty(model.getD().getData())) {
                rechargeAli(model.getD().getData());
            }


        } else if (HnVipBiz.Buy_Vip_WX.equals(type)) {
            HnWxPayModel model = (HnWxPayModel) obj;
            if (model.getD() != null) {
                rechargeWx(model.getD());
            } else {
                HnToastUtils.showToastShort(model.getC() + ":" + model.getM());
                return;
            }
        } else if (HnVipBiz.Vip_Data_Refresh.equals(type)) {
            HnVipDataModel model = (HnVipDataModel) obj;
            if (model != null && model.getD() != null || model.getD().getUser() == null) {
                updataUI(model.getD());

                CommDialog.newInstance(HnMyVipMemberActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                    }
                }).setTitle(getString(R.string.buy_Vip))
                        .setContent(getString(R.string.buy_vip_success_long_to) +
                                HnDateUtils.stampToDate(model.getD().getUser().getUser_member_expire_time()))
                        .show();
            }

        }
    }

    /**
     * 更新数据
     *
     * @param d
     */
    private void updataUI(HnVipDataModel.DBean d) {
        mDbean = d;
        if (d.getUser() != null) {
            mIvImg.setController(FrescoConfig.getController(d.getUser().getUser_avatar()));
            mTvName.setText(d.getUser().getUser_nickname());
            if ("N".equals(d.getUser().getUser_is_member())) {
                mTvLong.setText(R.string.not_open_vip);
                mTvOpen.setText(R.string.now_to_open);
            } else {
                mTvLong.setText(HnUiUtils.getString(R.string.app_vip_validity_to) + HnDateUtils.stampToDate(d.getUser().getUser_member_expire_time()));
                mTvOpen.setText(R.string.now_renew);
            }
            HnLiveLevelUtil.setAudienceLevBg(mTvLevel, d.getUser().getUser_level(), true);
        }

        if (d.getVip_combo() != null) {
            mMealData.clear();
            mMealData.addAll(d.getVip_combo());
            if (mMealData.size() > 0) {
                mSelectItem = 0;
                mTvPrice.setText(mMealData.get(0).getCombo_fee());
            }

            if (mMealAdapter != null) mMealAdapter.notifyDataSetChanged();
        }
        if (d.getVip_privilege() != null) {
            mPrivilegeData.clear();
            mPrivilegeData.addAll(d.getVip_privilege());
            if (mPrivilegeAdapter != null) mPrivilegeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if (HnVipBiz.Vip_Data.equals(type)) {//获取vip列表
            HnToastUtils.showToastShort(msg);
        } else if (HnVipBiz.Buy_Vip_WX.equals(type) || HnVipBiz.Buy_Vip_ZFB.equals(type)) {//购买vip
            if (code == HnServiceErrorUtil.USER_COIN_NOT_ENOUGH) {
                notEnough();
            } else {
                HnToastUtils.showToastShort(msg);
            }
        }
    }

    /**
     * 余额不足弹窗
     */
    private void notEnough() {
        CommDialog.newInstance(HnMyVipMemberActivity.this).setClickListen(new CommDialog.OneSelDialog() {
            @Override
            public void sureClick() {

            }
        }).setTitle(getString(R.string.buy_Vip))
                .setContent("支付失败，请重试")
                .setRightText(getString(R.string.i_know)).show();
    }

    private void rechargeAli(final String order) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(HnMyVipMemberActivity.this);
                Map<String, String> result = alipay.payV2(order, true);

                Message msg = new Message();
                msg.what = 1;
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

    private void payOk() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDoing(getResources().getString(R.string.loading), null);
                HnApplication.getmUserBean().setUser_is_member("Y");
                mHandler.sendEmptyMessageDelayed(2, 1000);
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Buy_VIP_Success, null));
            }
        });
    }
}
