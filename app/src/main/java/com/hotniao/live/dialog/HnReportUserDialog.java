package com.hotniao.live.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.live.R;

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

public class HnReportUserDialog  extends DialogFragment implements   View.OnClickListener{

    private Activity mActivity;

    private TextView tvCancle;
    private TextView   tvSure;
    private HnEditText mEtContent;

    private String nick;

    public  static  HnReportUserDialog  getInstance(){
        HnReportUserDialog dialog=new HnReportUserDialog();
        return  dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.dialog_report_user_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.ProgressDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.CENTER);
        //初始化组件
        initView(view);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth()*0.8f);
        alertWindow.setAttributes(params);

        return dialog;
    }

    private void initView(View view) {
        tvCancle= (TextView) view.findViewById(R.id.tv_cancle);
        tvCancle.setOnClickListener(this);
        tvSure= (TextView) view.findViewById(R.id.tv_ok);
        tvSure.setOnClickListener(this);
        mEtContent= (HnEditText) view.findViewById(R.id.mEtContent);
        view.findViewById(R.id.mIvClose).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle:
            case R.id.mIvClose:
                dismiss();
                break;
            case R.id.tv_ok:
                nick=mEtContent.getText().toString();
                if(TextUtils.isEmpty(nick.trim())||"".equals(nick.trim())){
                    HnToastUtils.showToastShort(getString(R.string.input_report_content));
                    return;
                }
                if(listener!=null)listener.sureClick(nick);
                dismiss();
                break;
        }
    }

    private DialogClickListener listener;
    public HnReportUserDialog setOnDialogCLickListener(DialogClickListener listener){
        this.listener=listener;
        return this;
    }
    public interface DialogClickListener {
        void sureClick(String content);
    }

}

