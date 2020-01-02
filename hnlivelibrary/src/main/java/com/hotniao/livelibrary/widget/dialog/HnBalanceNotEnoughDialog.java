package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.HnBaseApplication;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户余额不足弹出框
 * 创建人：mj
 * 创建时间：2017/9/21 11:04
 * 修改人：Administrator
 * 修改时间：2017/9/21 11:04
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBalanceNotEnoughDialog extends AppCompatDialogFragment implements  View.OnClickListener {

    public static  final int FistPay=1;
    public static  final int AgainPayEnough=2;
    private Activity mActivity;

    private TextView btnOk;
    private TextView   btnCancle;
    private TextView tvHint;

    private int mType=1;



    public static HnBalanceNotEnoughDialog newInstance(int type) {
        HnBalanceNotEnoughDialog sDialog = new HnBalanceNotEnoughDialog();
        Bundle b = new Bundle();
        b.putInt("type",type);
        sDialog.setArguments(b);
        return sDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity =  getActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_balannce_not_engouth_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.live_PayTip);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window alertWindow = dialog.getWindow();
        if (alertWindow != null) {
            alertWindow.setGravity(Gravity.CENTER);
            alertWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        mType=getArguments().getInt("type",1);
        //初始化布局
        intView(view);
        return dialog;
    }

    /**
     * 初始化布局
     * @param view
     */
    private void intView(View view) {
        tvHint= (TextView) view.findViewById(R.id.px_dialog_description);
        btnOk= (TextView) view.findViewById(R.id.bt_set);
        btnOk.setOnClickListener(this);
        btnCancle= (TextView) view.findViewById(R.id.bt_ok);
        btnCancle.setOnClickListener(this);
        tvHint.setText(String.format(HnUiUtils.getString(R.string.live_balance_not_enough_1), HnBaseApplication.getmConfig().getCoin()));
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_ok) {
            ARouter.getInstance().build("/app/HnMyRechargeActivity").navigation();
            dismiss();
        } else if (i == R.id.bt_set) {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Show_Buy,AgainPayEnough==mType?"":"rechargeCancle"));
            dismiss();
        }

    }

    @Subscribe
    public   void   onEventBusCallBack(HnLiveEvent  event){
        if(event!=null){
            if(HnLiveConstants.EventBus.Close_Dialog.equals(event.getType())){
                dismiss();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
