package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户账号被禁用弹框
 * 创建人：mj
 * 创建时间：2017/10/9 12:39
 * 修改人：Administrator
 * 修改时间：2017/10/9 12:39
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserAccountForbiddenDialog extends DialogFragment {


    private  TextView tvOk;
    private  Activity mActivity;



    public static HnUserAccountForbiddenDialog getInstance() {
        HnUserAccountForbiddenDialog sDialog = new HnUserAccountForbiddenDialog();
        return sDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_user_account_forbidden_layout, null);
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
        tvOk= (TextView)  view.findViewById(R.id.px_dialog_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出时 将直播界面关掉
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room,0));
                //停止服务
                EventBus.getDefault().post(new EventBusBean(0,"stop_websocket_service",null));
                //清除uid
                HnPrefUtils.setString(NetConstant.User.UID,"");
                //跳转登录界面
                ARouter.getInstance().build("/main/HnLoginActivity", "app").navigation();
                dismiss();
            }
        });
    }




}
