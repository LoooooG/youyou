package com.hotniao.livelibrary.widget.dialog;

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

import com.hn.library.base.BaseActivity;
import com.hotniao.livelibrary.R;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播结束提示
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveFinishDialog extends AppCompatDialogFragment   implements   View.OnClickListener{


    private BaseActivity mActivity;
    private View btnOk;

    public static HnLiveFinishDialog newInstance() {
        HnLiveFinishDialog sDialog = new HnLiveFinishDialog();
        Bundle b = new Bundle();
        sDialog.setArguments(b);
        return sDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_live_finish_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.live_PayTip);
        btnOk= view.findViewById(R.id.bt_ok);
        btnOk.setOnClickListener(this);
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
        btnOk=view.findViewById(R.id.bt_ok);
        btnOk.setOnClickListener(this);
        return dialog;
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_ok) {
            if (mActivity != null) {
                mActivity.finish();
            }
            dismiss();
        }
    }
}
