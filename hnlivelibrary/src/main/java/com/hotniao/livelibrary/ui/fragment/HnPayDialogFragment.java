package com.hotniao.livelibrary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseFragment;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnPayDialogFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvHead, mTvContent, mTvCancle, mTvSure;

    /**
     * 每分钟需要支付的费用
     */
    private String mCoin;
    /**
     * 直播类型   0：免费，1：VIP，2：门票，3：计时
     */
    private String live_Type;

    public static HnPayDialogFragment newInstance(String coin, String live_Type) {
        HnPayDialogFragment fragment = new HnPayDialogFragment();
        Bundle b = new Bundle();
        b.putString("data", coin);
        b.putString("live_Type", live_Type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.live_dialog_time_pay_look_finish_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCoin = getArguments().getString("data");
        live_Type = getArguments().getString("live_Type");
        initView(view);
    }

    private void initView(View view) {
        mTvHead = (TextView) view.findViewById(R.id.mTvHead);
        mTvContent = (TextView) view.findViewById(R.id.px_dialog_description);
        mTvCancle = (TextView) view.findViewById(R.id.bt_set);
        mTvSure = (TextView) view.findViewById(R.id.bt_ok);

        mTvCancle.setOnClickListener(this);
        mTvSure.setOnClickListener(this);

        if ("3".equals(live_Type)) {//计时付费
            mTvHead.setText(R.string.timing_charge);
            mTvContent.setText(HnUiUtils.getString(R.string.live_continiue_look_deduct_minute) + mCoin + HnBaseApplication.getmConfig().getCoin()+HnUiUtils.getString(R.string.live_sliver_is_continiu));
            mTvSure.setText(mActivity.getResources().getString(R.string.live_countiune_look));
        }else if("2".equals(live_Type)){//门票付费
            mTvHead.setText(R.string.live_buy_tickets);
            mTvContent.setText(String.format(HnUiUtils.getString(R.string.live_buy_tickets_only),  mCoin + HnBaseApplication.getmConfig().getCoin()));
            mTvSure.setText(mActivity.getResources().getString(R.string.buy));
        }else if("1".equals(live_Type)){//vip付费
            mTvHead.setText(R.string.live_vip);
            mTvContent.setText(R.string.live_open_vip_can_live);
            mTvSure.setText(mActivity.getResources().getString(R.string.buy));
        }

    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_ok) {

            EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Countiune_Look, 0));

        } else if (i == R.id.bt_set) {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Show_Buy, 0));

        }

    }
}
