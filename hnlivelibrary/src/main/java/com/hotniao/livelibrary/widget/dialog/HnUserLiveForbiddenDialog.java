package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnJPushMessageInfo;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户被禁播弹框
 * 创建人：mj
 * 创建时间：2017/10/9 12:39
 * 修改人：Administrator
 * 修改时间：2017/10/9 12:39
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserLiveForbiddenDialog extends DialogFragment {


    private TextView tvOk;
    private TextView tvHint;
    /**
     * 禁播时间   -1：永久禁播
     */
    private String time;

    private Activity mActivity;


    public static HnUserLiveForbiddenDialog getInstance(String time) {
        HnUserLiveForbiddenDialog sDialog = new HnUserLiveForbiddenDialog();
        Bundle b = new Bundle();
        b.putString("data", time);
        sDialog.setArguments(b);
        return sDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_user_live_forbidden_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.live_Dialog_Style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
        alertWindow.setAttributes(params);
        //初始化组件
        initView(view);
        initData();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }


    /*
    *  初始化视图
     */
    private void initView(View view) {
        tvHint = (TextView) view.findViewById(R.id.px_dialog_title);
        tvOk = (TextView) view.findViewById(R.id.px_dialog_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出时 将直播界面关掉
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, 0));
                ARouter.getInstance().build("/app/HnAnchorStopLiveActivity").navigation();
                dismiss();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (HnUserLiveForbiddenDialog.this.getDialog().isShowing()) {
                        //退出时 将直播界面关掉
                        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, 0));
                        ARouter.getInstance().build("/app/HnAnchorStopLiveActivity").navigation();
                        dismiss();
                    }
                } catch (Exception e) {
                }
            }
        }, 3000);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            time = bundle.getString("data");
            if (TextUtils.isEmpty(time) || "-1".equals(time)) {//永久禁播
                tvHint.setText("您已被禁播,请与后台客户联系");

            } else {//禁播一段时间
//                String result = HnDateUtils.dateFormat(time, "yyyy-MM-dd HH:mm");
                tvHint.setText(/*R.string.live_user_account_forbidden*/time);
            }
        }
    }

}
