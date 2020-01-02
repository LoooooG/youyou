package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：vip到期弹出框
 * 创建人：mj
 * 创建时间：2017/9/21 11:04
 * 修改人：Administrator
 * 修改时间：2017/9/21 11:04
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVIPOutDialog extends DialogFragment  implements  View.OnClickListener {

    private Activity mActivity;

    private TextView btnOk;
    private TextView   btnCancle;
    private TextView tvHint,mTvHead;





    public static HnVIPOutDialog newInstance() {
        HnVIPOutDialog sDialog = new HnVIPOutDialog();
        Bundle b = new Bundle();
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
        View view = View.inflate(mActivity, R.layout.live_dialog_time_pay_look_vip_out, null);
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
        //初始化布局
        intView(view);
        return dialog;
    }

    /**
     * 初始化布局
     * @param view
     */
    private void intView(View view) {
        btnOk= (TextView) view.findViewById(R.id.bt_set);
        btnOk.setOnClickListener(this);
        btnCancle= (TextView) view.findViewById(R.id.bt_ok);
        btnCancle.setOnClickListener(this);
        tvHint= (TextView) view.findViewById(R.id.px_dialog_description);
        mTvHead= (TextView) view.findViewById(R.id.mTvHead);
        tvHint.setText(R.string.live_vip_out_is_gobuy);
        mTvHead.setText(R.string.buy_Vip);
        btnOk.setText(getResources().getString(R.string.live_pay_vip));
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_ok) {
            ARouter.getInstance().build("/app/HnMyVIPActivity").navigation();
        } else if (i == R.id.bt_set) {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Show_Buy,0));
            dismiss();
        }

    }

    @Subscribe
    public   void   onEventBusCallBack(HnLiveEvent  event){
        if(event!=null){
            if(HnLiveConstants.EventBus.Close_Dialog.equals(event)){
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
