package com.hotniao.live.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hotniao.live.R;
import com.hotniao.live.utils.HnUiUtils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：选择情感状况
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserFellingsDialog extends DialogFragment implements View.OnClickListener {


    private Activity mActivity;
    private static HnUserFellingsDialog dialog;


    public static HnUserFellingsDialog newInstance() {
        dialog = new HnUserFellingsDialog();
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_user_fellings, null);
        view.findViewById(R.id.mTvOneself).setOnClickListener(this);
        view.findViewById(R.id.mTvSpinsterhood).setOnClickListener(this);
        view.findViewById(R.id.mTvMarried).setOnClickListener(this);
        view.findViewById(R.id.mTvPrivary).setOnClickListener(this);


        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (mActivity.getWindowManager().getDefaultDisplay().getWidth());
//        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        window.setAttributes(params);
        return dialog;
    }


    public HnUserFellingsDialog setItemClickListener(OnClickDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private OnClickDialogListener listener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvOneself:
                if (listener != null) listener.clickState(HnUiUtils.getString(R.string.oneself));
                break;
            case R.id.mTvSpinsterhood:
                if (listener != null)
                    listener.clickState(HnUiUtils.getString(R.string.spinsterhood));

                break;
            case R.id.mTvMarried:
                if (listener != null) listener.clickState(HnUiUtils.getString(R.string.married));
                break;
            case R.id.mTvPrivary:
                if (listener != null) listener.clickState(HnUiUtils.getString(R.string.privary));

                break;
        }
        dismiss();
    }

    public interface OnClickDialogListener {
        void clickState(String value);
    }


}
