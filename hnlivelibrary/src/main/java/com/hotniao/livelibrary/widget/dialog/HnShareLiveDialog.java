package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class HnShareLiveDialog extends DialogFragment implements View.OnClickListener {


    private Activity mActivity;
    private static HnShareLiveDialog dialog;

    private SHARE_MEDIA platform = null;
    private static UMShareAPI mShareAPI = null;
    private static ShareAction mShareAction;
    private static String mUrl, mHeadImg, mName, mAnchorId;
    private static boolean mIsAnchor = true;

    public static HnShareLiveDialog newInstance(UMShareAPI shareAPI, ShareAction shareAction, String url, String headImg, String name, String anchorId, boolean isAnchor) {
        dialog = new HnShareLiveDialog();
        mShareAPI = shareAPI;
        mShareAction = shareAction;
        mUrl = url;
        mHeadImg = headImg;
        mName = name;
        mAnchorId = anchorId;
        mIsAnchor = isAnchor;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_live_share, null);
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


    public HnShareLiveDialog setItemShareListener(onShareDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private onShareDialogListener listener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mTvQQ) {

            platform = SHARE_MEDIA.QQ;
        } else if (v.getId() == R.id.mTvSina) {
            platform = SHARE_MEDIA.SINA;
        } else if (v.getId() == R.id.mTvWx) {
            platform = SHARE_MEDIA.WEIXIN;
        } else if (v.getId() == R.id.mTvWxCri) {
            platform = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        if (!TextUtils.isEmpty(NetConstant.setImageUrl(mHeadImg)) && !TextUtils.isEmpty(mUrl))
            mShareAction.setPlatform(platform).withMedia(new UMImage(mActivity, NetConstant.setImageUrl(mHeadImg))).withTargetUrl(mUrl)
                    .withText(String.format(HnUiUtils.getString(R.string.live_share_content), mName, mAnchorId))
                    .withTitle(getString(R.string.live_share_title))
                    .setCallback(umShareListener).share();
        else if (!TextUtils.isEmpty(mUrl))
            mShareAction.setPlatform(platform).withMedia(new UMImage(mActivity, R.drawable.logo)).withTargetUrl(mUrl)
                    .withText(String.format(HnUiUtils.getString(R.string.live_share_content), mName, mAnchorId))
                    .withTitle(getString(R.string.live_share_title))
                    .setCallback(umShareListener).share();
    }

    public interface onShareDialogListener {
        void sureClick();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            shareSuccess(mAnchorId, mIsAnchor);
            try {
                if (mActivity != null && isAdded())
                    dismiss();
            } catch (Exception e) {
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            try {
                if (mActivity != null && isAdded())
                    dismiss();
            } catch (Exception e) {
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            try {
                if (mActivity != null && isAdded())
                    dismiss();
            } catch (Exception e) {
            }
        }
    };

    /**
     * 直播分享成功
     *
     * @param mAnchorId
     */
    public static void shareSuccess(String mAnchorId, boolean isAnchor) {
        String url = "";
        RequestParams params = new RequestParams();
        if (isAnchor) {
            url = HnLiveUrl.LIVE_ANCHOR_ADDSHARE_LOG;
        } else {
            url = HnLiveUrl.LIVE_ROOM_ADDSHARE_LOG;
            params.put("anchor_user_id", mAnchorId);
        }

        HnHttpUtils.postRequest(url, isAnchor ? null : params, url, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
            }

            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
