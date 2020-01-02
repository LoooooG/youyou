package com.hotniao.livelibrary.ui.gift;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseFragment;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseListener;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.giflist.HnDonwloadGiftStateListener;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnGivePresentModel;
import com.hotniao.livelibrary.model.HnUserCoinDotModel;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnRechargeSuccessModel;
import com.hotniao.livelibrary.model.event.UserCoinReduceEvent;
import com.jakewharton.rxbinding2.view.RxView;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间  --  礼物
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 *
 * @author Administrator
 */
public class GiftDialog extends DialogFragment implements View.OnClickListener, HnLiveBaseListener, HnDonwloadGiftStateListener {

    private static final int ACTION_DOWN = 1;
    private static final int ACTION_UP = 2;

    private static final String TAG = "GiftDialog";

    /**
     * 布局文件
     */
    private SlidingTabLayout mSlidTab;
    private ViewPager mVpGift;
    private TextView mTvRechargeNum;
    private TextView mBtSend;
    private TextView mTvCountDown;
    private LinearLayout mLlCountDown;
    private TextView mTvGoExcharge;
    private View mViewDis;

    /**
     * 上下文
     */
    private BaseActivity mActivity;
    /**
     * 用户id
     */
    private String mUid;
    /**
     * 用户u币数
     */
    private String mCoin;
    /**
     * 选中的礼物id
     */
    private String mGiftId;
    /**
     * 选中的礼物价格
     */
    private String mGiftConsume;


    private TimeCount mTimeCount;
    private boolean clickable = true;

    private GiftListBean mGift;
    private List<GiftListBean> mGifts = new ArrayList<>();
    private List<HnGiftListBean.GiftBean> mGiftListData = new ArrayList<>();
    private List<BaseFragment> mFragments = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();
    /**
     * 礼物列表操作类
     */
    private HnGiftBiz mHnGiftBiz;
    /**
     * 是否更新礼物列表，用于标识在发送礼物时，某个礼物下载了，在后台更新数据库礼物数据，但是当前礼物列表暂时无需更换，下次再获取最新的
     */
    private boolean isUpdateGiftList = true;
    private View view;
    /**
     * 送礼Id
     */
    private int mSendNum = 0;
    private String chatLog;

    public static GiftDialog newInstance(String coin, String uid) {
        return newInstance(coin,uid,"");
    }

    public static GiftDialog newInstance(String coin, String uid,String chatLog) {
        Bundle args = new Bundle();
        if(!TextUtils.isEmpty(chatLog)){
            args.putString("chat_log",chatLog);
        }
        args.putString("coin", coin);
        args.putString("uid", uid);
        GiftDialog sDialog = new GiftDialog();
        sDialog.setArguments(args);
        return sDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        mActivity = (BaseActivity) getActivity();
        mHnGiftBiz = new HnGiftBiz(mActivity, this, this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCoin = bundle.getString("coin", "");
            mUid = bundle.getString("uid", "");
            chatLog = bundle.getString("chat_log","");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        view = View.inflate(mActivity, R.layout.live_dialog_gift, null);
        //初始化布局
        initView(view);
        //获取礼物数据
        mHnGiftBiz.getGiftListData();
        getCoin();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    /**
     * 初始化布局
     *
     * @param view
     */
    private void initView(View view) {
        mSlidTab = (SlidingTabLayout) view.findViewById(R.id.mSlidTab);
        mVpGift = (ViewPager) view.findViewById(R.id.mViewPager);
        mViewDis = view.findViewById(R.id.mViewDis);
        mTvRechargeNum = (TextView) view.findViewById(R.id.tv_Recharge_num);
        mTvRechargeNum.setOnClickListener(this);
        mBtSend = (TextView) view.findViewById(R.id.bt_send_gift);
        mTvCountDown = (TextView) view.findViewById(R.id.tv_count_down);
        mTvGoExcharge = (TextView) view.findViewById(R.id.go_excharge);
        mTvGoExcharge.setOnClickListener(this);
        mLlCountDown = (LinearLayout) view.findViewById(R.id.fl_count_down);
        mLlCountDown.setOnClickListener(this);
        setCoin();
        mViewDis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });


        RxView.clicks(mBtSend)
                .throttleFirst(1200, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        if (HnNetUtil.NETWORK_NONE == HnNetUtil.getNetWorkState(mActivity)) {
                            HnToastUtils.showToastShort("网络已断开,请先链接网络");
                            return;
                        }
                        if (TextUtils.isEmpty(mGiftId)) {
                            HnToastUtils.showToastShort(mActivity.getResources().getString(R.string.live_gift_not_select));
                            return;
                        }
                        if (TextUtils.isEmpty(mGiftConsume)) return;
                        try {
                            if (mCoin == null) {
                                if (mGiftConsume != null && mCoin != null) {
                                    if (Double.parseDouble(mGiftConsume) > (int) (Double.parseDouble(mCoin))) {
                                        toRecharge();
                                        return;
                                    }
                                }
                            } else {
                                if (mGiftConsume != null && mCoin != null) {
                                    if (Integer.parseInt(mGiftConsume) > (int) (Double.parseDouble(mCoin))) {
                                        toRecharge();
                                        return;
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                        mSendNum = 1;

                        if (!TextUtils.isEmpty(mGift.getZipDownUrl())) {
                            sendGift();
                        } else {
                            if (mBtSend.getVisibility() == View.VISIBLE && mLlCountDown.getVisibility() == View.GONE) {
                                mBtSend.setVisibility(View.GONE);
                                mLlCountDown.setVisibility(View.VISIBLE);
                            }
                            computeCoin();
                            setCoin();
                            mTvCountDown.setText(mSendNum + "");
                            if (mTimeCount == null) {
                                mTimeCount = new TimeCount(1000, 1000);
                            }
                            mTimeCount.start();
                            clickable = false;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 网络请求：成功
     *
     * @param type     类型  标识符
     * @param response 返回的响应数据
     * @param obj      实体等数据   根据实际需求而定
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) {
            return;
        }
        //网络请求获取礼物列表
        if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnGiftListModel model = (HnGiftListModel) obj;
            if (model.getD() != null && model.getD().getGift() != null) {
                mHnGiftBiz.transformData(model.getD().getGift());
            }
            //从数据库中获取到数据
        } else if (HnGiftBiz.GET_GIFT_LIST_FROM_DB == type) {
            ArrayList<GiftListBean> giftList = (ArrayList<GiftListBean>) obj;
            if (giftList != null && giftList.size() > 0) {
                mGifts.clear();
                mGifts.addAll(giftList);
                if (isUpdateGiftList) {
                    mGiftListData.addAll(setData(giftList));
                    setViewPagetAdapter();
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_Gift_List, obj));
                }
            }
        }
    }

    /**
     * 改变数据格式
     *
     * @param giftList
     * @return
     */
    private List<HnGiftListBean.GiftBean> setData(ArrayList<GiftListBean> giftList) {
        List<HnGiftListBean.GiftBean> mTab = new ArrayList<>();
        for (int i = 0; i < giftList.size(); i++) {
            boolean hasTab = false;
            for (int j = 0; j < mTab.size(); j++) {
                if (giftList.get(i).getmTabId().equals(mTab.get(j).getId() + "")) {
                    hasTab = true;

                    HnGiftListBean.GiftBean.ItemsBean itemsBean = new HnGiftListBean.GiftBean.ItemsBean();
                    itemsBean.setName(giftList.get(i).getGiftName());
                    itemsBean.setAnimation(giftList.get(i).getZipDownUrl());
                    itemsBean.setCoin(giftList.get(i).getGiftCoin());
                    itemsBean.setDetail(giftList.get(i).getDetail());
                    itemsBean.setIcon(giftList.get(i).getStaticGiftUrl());
                    itemsBean.setIcon_gif(giftList.get(i).getDynamicGiftUrl());
                    itemsBean.setId(giftList.get(i).getGift_id());
                    itemsBean.setStatus(giftList.get(i).getState());
                    itemsBean.setSort(giftList.get(i).getSort() + "");
                    itemsBean.setCheck(false);
                    itemsBean.setLocalGifPath(giftList.get(i).getDynamicGiftLocalUrl());
                    mTab.get(j).getItems().add(itemsBean);

                }
            }
            if (!hasTab) {
                HnGiftListBean.GiftBean giftBean = new HnGiftListBean.GiftBean();
                giftBean.setId(Integer.parseInt(giftList.get(i).getmTabId()));
                giftBean.setName(giftList.get(i).getmTabName());

                List<HnGiftListBean.GiftBean.ItemsBean> itemsBeens = new ArrayList<>();
                HnGiftListBean.GiftBean.ItemsBean itemsBean = new HnGiftListBean.GiftBean.ItemsBean();
                itemsBean.setName(giftList.get(i).getGiftName());
                itemsBean.setAnimation(giftList.get(i).getZipDownUrl());
                itemsBean.setCoin(giftList.get(i).getGiftCoin());
                itemsBean.setDetail(giftList.get(i).getDetail());
                itemsBean.setIcon(giftList.get(i).getStaticGiftUrl());
                itemsBean.setIcon_gif(giftList.get(i).getDynamicGiftUrl());
                itemsBean.setId(giftList.get(i).getGift_id());
                itemsBean.setStatus(giftList.get(i).getState());
                itemsBean.setSort(giftList.get(i).getSort() + "");
                itemsBean.setCheck(false);
                itemsBean.setLocalGifPath(giftList.get(i).getDynamicGiftLocalUrl());
                itemsBeens.add(itemsBean);

                giftBean.setItems(itemsBeens);
                mTab.add(giftBean);
            }
        }


        dataSort(mTab);


        return mTab;
    }

    private void dataSort(List<HnGiftListBean.GiftBean> mTab) {
        for (int n = 0; n < mTab.size(); n++) {
            List<HnGiftListBean.GiftBean.ItemsBean> items = mTab.get(n).getItems();
            for (int i = 1; i < items.size(); i++) {

                for (int j = i; j > 0 && Integer.parseInt(items.get(j).getSort()) < Integer.parseInt(items.get(j - 1).getSort()); j--) {
                    HnGiftListBean.GiftBean.ItemsBean temp = items.get(j);
                    items.set(j, items.get(j - 1));
                    items.set(j - 1, temp);
                }
            }
        }

    }


    /**
     * 网络请求：失败
     *
     * @param type 类型 标识符
     * @param code 错误码
     * @param msg  错误信息
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) {
            return;
        }
        if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 大礼物下载成功
     *
     * @param data 礼物数据
     */
    @Override
    public void downloadGiftSuccess(boolean isShow, GiftListBean data, Object obj) {
        if (mActivity == null || data == null || mHnGiftBiz == null) {
            return;
        }
        mHnGiftBiz.chanageGiftData(data, mGiftListData);
    }

    /**
     * 大礼物下载失败
     *
     * @param code 错误码
     * @param msg  原因
     * @param data 礼物数据
     */
    @Override
    public void downloadGiftFail(int code, String msg, GiftListBean data) {
        if (mActivity == null) {
            return;
        }

    }


    /**
     * 设置viewpager适配器
     */
    public void setViewPagetAdapter() {

        mVpGift.setOffscreenPageLimit(mGiftListData.size());
        for (int i = 0; i < mGiftListData.size(); i++) {
            mTitle.add(mGiftListData.get(i).getName());
            mFragments.add(new HnGiftPagerFragment(setPaging(mGiftListData.get(i).getItems()), true));
        }
        FragmentPagerListAdapter fragmentPagerListAdapter = new FragmentPagerListAdapter(getChildFragmentManager(), mTitle, mFragments);
        mVpGift.setAdapter(fragmentPagerListAdapter);
        mSlidTab.setViewPager(mVpGift);
        mSlidTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVpGift.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.go_excharge) {//充值
            ARouter.getInstance().build("/app/HnMyRechargeActivity").navigation();
            if (GiftDialog.this.isAdded()) {
                GiftDialog.this.dismiss();
            }
        } else if (v.getId() == R.id.fl_count_down) {//定时连击
            if (HnNetUtil.NETWORK_NONE == HnNetUtil.getNetWorkState(mActivity)) {
                HnToastUtils.showToastShort("网络已断开,请先链接网络");
                return;
            }
            if (TextUtils.isEmpty(mGiftId)) {
                HnToastUtils.showToastShort(getResources().getString(R.string.live_gift_not_select));
                return;
            }
            if (TextUtils.isEmpty(mGiftConsume)) return;
            try {
                if (mCoin == null) {
                    if (mGiftConsume != null && mCoin != null) {
                        if (Double.parseDouble(mGiftConsume) > (int) (Double.parseDouble(mCoin))) {
                            toRecharge();
                            return;
                        }
                    }
                } else {
                    if (mGiftConsume != null && mCoin != null) {
                        if (Integer.parseInt(mGiftConsume) > (int) (Double.parseDouble(mCoin))) {
                            toRecharge();
                            return;
                        }
                    }
                }
            } catch (Exception e) {
            }

            mSendNum++;
            mTvCountDown.setText(mSendNum + "");

            computeCoin();
            setCoin();

            if (mTimeCount == null) {
                mTimeCount = new TimeCount(1000, 1000);
            }
            mTimeCount.start();
        }
    }


    /**
     * 去充值
     */
    private void toRecharge() {
        CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                ARouter.getInstance().build("/app/HnMyRechargeActivity").navigation();
                if (GiftDialog.this.isAdded()) {
                    GiftDialog.this.dismiss();
                }
            }
        }).setTitle(HnUiUtils.getString(R.string.live_letter_bal_not_enough))
                .setContent(String.format(HnUiUtils.getString(R.string.live_balance_not_enough), HnBaseApplication.getmConfig().getCoin()))
                .setRightText(HnUiUtils.getString(R.string.live_recharge)).show();


    }


    @Subscribe
    public void userCoinReduce(UserCoinReduceEvent event) {
        mCoin = event.getCoin();
        setCoin();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mHnGiftBiz != null) {
            mHnGiftBiz = null;
        }
        if (mTimeCount != null) {
            mTimeCount = null;
            clickable = true;
        }
    }

    /**
     * 请求赠送礼物接口
     */
    private void sendGift() {
        if (TextUtils.isEmpty(mUid)) {
            return;
        }
        if (TextUtils.isEmpty(mGiftId)) {
            HnToastUtils.showToastShort(mActivity.getResources().getString(R.string.live_gift_not_select));
            return;
        }

        if (TextUtils.isEmpty(mCoin)) {
            toRecharge();
            return;
        }
//        if (!TextUtils.isEmpty(mGiftConsume) && !TextUtils.isEmpty(mCoin)) {
//            if (Double.parseDouble(mGiftConsume) > (Double.parseDouble(mCoin))) {
//                toRecharge();
//                return;
//            }
//        }


        RequestParams param = new RequestParams();
        param.put("gift_number", mSendNum);
        param.put("anchor_user_id", mUid);
        param.put("live_gift_id", mGiftId);
        if(!TextUtils.isEmpty(chatLog)){
            param.put("chat_log", chatLog);
        }
        HnHttpUtils.postRequest(HnLiveUrl.SEND_GIFT, param, TAG, new HnResponseHandler<HnGivePresentModel>(mActivity, HnGivePresentModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                if (model.getC() == 0) {
                    if (model.getD() != null && model.getD().getUser() != null && !TextUtils.isEmpty(model.getD().getUser().getUser_coin())) {
                        mCoin = model.getD().getUser().getUser_coin();
                        setCoin();
                        HnBaseApplication.getmUserBean().setUser_coin(mCoin);
                        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_User_Coin, mCoin));
                        HnLogUtils.e("sendGife----111111---" + mCoin);
                    }
                } else {
                    computeCoinError();
                    setCoin();
                    HnLogUtils.e("sendGife----22222---" + mCoin);
                    HnToastUtils.showToastShort(model.getM());
                    //礼物列表中，有礼物下架，重新获取数据，更新本地数据
                    if (model.getC() == 203) {
                        isUpdateGiftList = false;
                        mHnGiftBiz.requestToGetGiftList();
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (errCode != 10000) {
                    HnToastUtils.showToastShort(msg);
                }
                if (mActivity == null) return;
                computeCoinError();
                setCoin();
                //礼物列表中，有礼物下架，重新获取数据，更新本地数据
                if (errCode == 203) {
                    isUpdateGiftList = false;
                    mHnGiftBiz.requestToGetGiftList();
                }
            }
        });
    }

    /**
     * 获取余额
     */
    private void getCoin() {
        HnHttpUtils.postRequest(HnLiveUrl.USER_BALANCE, null, HnLiveUrl.USER_BALANCE, new HnResponseHandler<HnUserCoinDotModel>(HnUserCoinDotModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity != null && model.getD().getUser() != null && !TextUtils.isEmpty(model.getD().getUser().getUser_coin())) {
                    mCoin = model.getD().getUser().getUser_coin();
                    setCoin();
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }

    /**
     * 计算送礼物余额
     */
    private void computeCoin() {
        try {
            double v = Double.parseDouble(mCoin) - Double.parseDouble(mGiftConsume);
            mCoin = v + "";
        } catch (Exception e) {
        }
    }

    /**
     * 计算送礼物失败余额
     */
    private void computeCoinError() {
        try {
            double v = Double.parseDouble(mCoin) + Double.parseDouble(mGiftConsume);
            mCoin = v + "";
        } catch (Exception e) {
        }
    }

    /**
     * 设置余额
     */
    private void setCoin() {
        if (mTvRechargeNum != null) {
            mTvRechargeNum.setText(HnUtils.setTwoPoint(mCoin));
        }
    }

    @Subscribe
    public void onEventBusCallBack(HnLiveEvent event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.Pay_Success.equals(event.getType())) {//用户充值金额
                HnRechargeSuccessModel model = (HnRechargeSuccessModel) event.getObj();
                if (model != null && model.getData() != null) {
                    String coin = model.getData().getUser_coin();
                    if (!TextUtils.isEmpty(coin)) {
                        mCoin = coin;
                        setCoin();
                        HnLogUtils.i(TAG, "礼物对话框接到用户充值信息:" + mCoin);
                    }
                }
            } else if (HnLiveConstants.EventBus.Download_Gift_Gif_Success.equals(event.getType())) {
                if (mHnGiftBiz != null) {
                    mHnGiftBiz.updataGiftGifData(event.getObj() + "", event.getOtherObj() + "");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    /**
     * 分页数据，每页8个
     */
    public List<List<HnGiftListBean.GiftBean.ItemsBean>> setPaging(List<HnGiftListBean.GiftBean.ItemsBean> mData) {

        if (mData == null || mData.size() <= 0) {
            return null;
        }

        int pageNum = mData.size() / 8;
        int remainder = mData.size() % 8;
        if (remainder != 0) {
            pageNum = pageNum + 1;
        }

        List<List<HnGiftListBean.GiftBean.ItemsBean>> pagers = new ArrayList<List<HnGiftListBean.GiftBean.ItemsBean>>();

        for (int i = 0; i < pageNum; i++) {
            pagers.add(i, new ArrayList<HnGiftListBean.GiftBean.ItemsBean>());
            pagers.get(i).addAll(mData.subList(i * 8, Math.min(i * 8 + 8, mData.size())));
        }

        if (pagers.size() == 0) {
            pagers.add(new ArrayList<HnGiftListBean.GiftBean.ItemsBean>());
        }

        return pagers;

    }


    //##########################################################################################

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGiftEventHandle(HnGiftEven event) {

        if (mTvCountDown != null) {
            if (mBtSend.getVisibility() == View.GONE) {
                mLlCountDown.setVisibility(View.VISIBLE);
                mBtSend.setVisibility(View.VISIBLE);
                mLlCountDown.setVisibility(View.GONE);
            }
            if (mTimeCount != null) {
                mTimeCount.cancel();
                mTimeCount = null;
            }
            if (mGift != null) {
                if (!TextUtils.isEmpty(mGift.getZipDownLocalUrl())) {

                } else if (mSendNum != 0) {
                    sendGift();
                }
            }
            mSendNum = 0;
        }
        for (int i = 0; i < mGifts.size(); i++) {
            if (event.getId().equals(mGifts.get(i).getGift_id())) {
                mGift = mGifts.get(i);
                break;
            }
        }
        mGiftId = event.getId();
        mGiftConsume = event.getMoney();

        try {
            if (mHnGiftBiz != null && mActivity != null)
                if (mHnGiftBiz.isNeedDownloadBigGift(mGift, mActivity))
                    HnToastUtils.showToastShort(getString(R.string.live_down_gift_res));
        } catch (Exception e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

    /**
     * 倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程
            if (mTvCountDown != null) {
                HnLogUtils.d(TAG, "timecount计时中：" + millisUntilFinished);
            }
        }

        @Override
        public void onFinish() {//计时完毕
            if (mTvCountDown != null) {
                if (mBtSend.getVisibility() == View.GONE && mLlCountDown.getVisibility() == View.VISIBLE) {
                    mBtSend.setVisibility(View.VISIBLE);
                    mLlCountDown.setVisibility(View.GONE);
                }
                if (mTimeCount != null) {
                    mTimeCount = null;
                }
                clickable = true;
                if (mSendNum != 0) {
                    sendGift();
                }
                mSendNum = 0;
            }
        }
    }

    class FragmentPagerListAdapter extends FragmentPagerAdapter {

        private List<String> strings;
        private List<BaseFragment> fragments;

        public FragmentPagerListAdapter(FragmentManager fm, List<String> strings, List<BaseFragment> fragments) {
            super(fm);
            this.strings = strings;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strings.get(position);
        }
    }

}
