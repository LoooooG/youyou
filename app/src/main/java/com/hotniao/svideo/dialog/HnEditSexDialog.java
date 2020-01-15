package com.hotniao.svideo.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.EventBusBean;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.R;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：编辑性别
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnEditSexDialog extends AppCompatDialogFragment {

    private static HnEditSexDialog sDialog;

    private BaseActivity mActivity;

    public static HnEditSexDialog newInstance() {

        Bundle args = new Bundle();
        sDialog = new HnEditSexDialog();
        sDialog.setArguments(args);
        return sDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.dialog_edit_sex, null);

        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        alertWindow.setAttributes(params);

        return dialog;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_male, R.id.tv_female, R.id.tv_sec})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_cancel:
                dialogDismiss();
                break;

            case R.id.tv_male:
                selectSex("1");
                break;

            case R.id.tv_female:
                selectSex("2");
                break;

            case R.id.tv_sec:
                selectSex(HnUiUtils.getString(R.string.edit_secrecy));
                break;

        }
    }


    /**
     * 选择性别
     */
    public void selectSex(final String sex) {

        RequestParams param = new RequestParams();
        param.put("user_sex", sex);
        HnHttpUtils.postRequest(HnUrl.SAVE_USER_INFO, param, "编辑性别", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_User_Sex,sex));
                dialogDismiss();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    /**
     * dialog消失
     */
    private void dialogDismiss() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
