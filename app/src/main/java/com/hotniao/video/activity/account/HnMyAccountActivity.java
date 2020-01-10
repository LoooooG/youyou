package com.hotniao.video.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.activity.BounsAct;
import com.hotniao.video.activity.HnMyRechargeActivity;
import com.hotniao.video.utils.HnAppConfigUtil;
import com.hotniao.video.utils.HnUiUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述 我的账户
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMyAccountActivity extends BaseActivity {
    @BindView(R.id.mTvCion)
    TextView mTvCion;
    @BindView(R.id.mTvBalance)
    TextView mTvBalance;

    public static void luncher(Activity activity, String mCion, boolean isAnchor) {
        activity.startActivity(new Intent(activity, HnMyAccountActivity.class).putExtra("cion", mCion).putExtra("isAnchor", isAnchor));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_account;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        if (HnApplication.getmConfig() == null) {
            HnAppConfigUtil.getConfig();
            HnAppConfigUtil.setOnCateListener(new HnAppConfigUtil.OnConfigListener() {
                @Override
                public void onSuccess() {
                    mTvBalance.setText(HnUiUtils.getString(R.string.balance) + "(" + HnApplication.getmConfig().getCoin() + ")");
                }

                @Override
                public void onError(int errCode, String msg) {
                }
            });
        } else {
            mTvBalance.setText(HnUiUtils.getString(R.string.balance) + "(" + HnApplication.getmConfig().getCoin() + ")");
        }

    }

    @Override
    public void getInitData() {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/I770-Sans.ttf");
        mTvCion.setTypeface(typeFace);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvCion.setText(HnUtils.setTwoPoint(HnApplication.getmUserBean().getUser_coin()));
    }

    @OnClick({R.id.mIvBack, R.id.mTvBouns, R.id.mTvToRecharge, R.id.mTvEarnings, R.id.mTvExpense, R.id.mTvRecharge, R.id.mTvWithdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvBouns:
                Intent intent = new Intent(this, BounsAct.class);
                intent.putExtra("isAnchor", getIntent().getBooleanExtra("isAnchor", false));
                startActivity(intent);
                break;
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mTvToRecharge://去充值
                openActivity(HnMyRechargeActivity.class);
                break;
            case R.id.mTvEarnings://收益
                openActivity(HnUserBillEarningActivity.class);
                break;
            case R.id.mTvExpense://消费
                openActivity(HnUserBillExpenseActivity.class);
                break;
            case R.id.mTvRecharge://充值记录
                HnUserBillRechargeAndWithdrawActivity.luncher(HnMyAccountActivity.this, HnUserBillRechargeAndWithdrawActivity.RECHARGE);
                break;
            case R.id.mTvWithdraw://提现记录
                HnUserBillRechargeAndWithdrawActivity.luncher(HnMyAccountActivity.this, HnUserBillRechargeAndWithdrawActivity.WITHDRAW);
                break;
        }
    }
}
