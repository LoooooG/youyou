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

import com.hn.library.utils.HnLogUtils;
import com.hotniao.live.R;
import com.hotniao.live.utils.HnUiUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：分享
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnShareDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "HnShareDialog";
    private Activity mActivity;
    private static HnShareDialog dialog;

    private SHARE_MEDIA platform = null;
    private static UMShareAPI mShareAPI = null;
    private static ShareAction mShareAction;
    private static String mContent, mImagUrl, mUrl, mTitle;

    public static HnShareDialog newInstance(UMShareAPI shareAPI, ShareAction shareAction, String content, String logo, String url, String title) {
        dialog = new HnShareDialog();
        mShareAPI = shareAPI;
        mShareAction = shareAction;
        mContent = "悠悠直播视频交友，面对面结识新朋友，速速来开启属于你和Ta的专属时光吧~";
        mImagUrl = logo;
        mUrl = url;
        mTitle = "悠悠直播-善于发现你身边的另一半";
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_share, null);
        view.findViewById(R.id.mTvQQ).setOnClickListener(this);
        view.findViewById(R.id.mTvWx).setOnClickListener(this);
        view.findViewById(R.id.mTvSina).setOnClickListener(this);
        view.findViewById(R.id.mTvWxCri).setOnClickListener(this);


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


    public HnShareDialog setItemShareListener(onShareDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private onShareDialogListener listener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvQQ:
                platform = SHARE_MEDIA.QQ;

                break;
            case R.id.mTvSina:
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.mTvWx:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.mTvWxCri:
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
        }
        if (TextUtils.isEmpty(mImagUrl)) {
            mShareAction.setPlatform(platform).withMedia(new UMImage(mActivity, R.drawable.default_home_head)).withTargetUrl(mUrl).withText(mContent)
                    .withTitle(TextUtils.isEmpty(mTitle) ? HnUiUtils.getString(R.string.app_name) : mTitle)
                    .setCallback(umShareListener).share();
        } else {
            mShareAction.setPlatform(platform).withMedia(new UMImage(mActivity, mImagUrl)).withTargetUrl(mUrl).withText(mContent)
                    .withTitle(TextUtils.isEmpty(mTitle) ? HnUiUtils.getString(R.string.app_name) : mTitle)
                    .setCallback(umShareListener).share();
        }
        dismiss();

    }

    public interface onShareDialogListener {
        void sureClick();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            HnLogUtils.d(TAG,"onResult");
            if (listener != null) listener.sureClick();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            HnLogUtils.d(TAG,"onCancel");
        }

    };

}
