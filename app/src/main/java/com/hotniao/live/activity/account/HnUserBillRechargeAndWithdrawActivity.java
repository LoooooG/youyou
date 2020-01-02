package com.hotniao.live.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hn.library.base.BaseActivity;
import com.hotniao.live.R;
import com.hotniao.live.fragment.billRecord.HnBillRechargeFragment;
import com.hotniao.live.fragment.billRecord.HnBillWithDrawFragment;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：充值提现记录
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserBillRechargeAndWithdrawActivity extends BaseActivity {
    public static final int RECHARGE=1;//充值
    public static final int WITHDRAW=2;//提现

    public static void luncher(Activity activity,int type){
        activity.startActivity(new Intent(activity,HnUserBillRechargeAndWithdrawActivity.class).putExtra("type",type));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        if(1==getIntent().getIntExtra("type",1)){
            setTitle(R.string.recharge_record);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mFrame, HnBillRechargeFragment.newInstance())
                    .commitAllowingStateLoss();
        }else {
            setTitle(R.string.withdraw_record);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mFrame, HnBillWithDrawFragment.newInstance())
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void getInitData() {

    }
}
