package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.HnMainActivity;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnUpLoadVideoModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.svideo.widget.squprogress.SquareProgressBar;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.hotniao.livelibrary.model.HnLocationEntity;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.videolibrary.activity.HnChooseVideoActivity;
import com.videolibrary.activity.HnChooseVideoEditerActivity;
import com.videolibrary.activity.TCVideoEditerActivity;
import com.videolibrary.activity.TCVideoNoEditerActivity;
import com.videolibrary.activity.TCVideoRecordActivity;
import com.videolibrary.eventbus.HnDeleteVideoFileEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：发布视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/videoPublishActivity", group = "app")
public class HnVideoPublishActivity extends BaseActivity {
    @BindView(R.id.mSp)
    SquareProgressBar mSp;
    @BindView(R.id.mTvProgress)
    TextView mTvProgress;
    @BindView(R.id.mLlPublishing)
    LinearLayout mLlPublishing;
    @BindView(R.id.mLlPublished)
    LinearLayout mLlPublished;


    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;

    private String mImagUrl, mShareUrl, mVideoId, mTitle;

    private boolean isPublisSuccess = false;
    private HnLocationEntity mLocationEntity;

    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;

    private int mRequestId = -1;

    public static void luncher(Activity activity, String imageUrl, String videoPath, String title, String cateId, String longTime, boolean isSave) {
        activity.startActivity(new Intent(activity, HnVideoPublishActivity.class).putExtra("imageUrl", imageUrl).putExtra("videoPath", videoPath)
                .putExtra("title", title).putExtra("cateid", cateId).putExtra("longTime", longTime).putExtra("isSave", isSave));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_publish;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(false);
        setShowTitleBar(false);
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");


        mShareAPI = UMShareAPI.get(this);
        mShareAction = new ShareAction(this);
    }

    @Override
    public void getInitData() {
        mSp.setImage(getIntent().getStringExtra("imageUrl"));
        mSp.setColorRGB(0xff58cf58);
        mTitle = getIntent().getStringExtra("title");
        mImagUrl = getIntent().getStringExtra("imageUrl");
        mLocationEntity = getIntent().getParcelableExtra("location");

        HnUpLoadPhotoControl.getTenSign(new File(getIntent().getStringExtra("videoPath")), HnUpLoadPhotoControl.UploadVideo, HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                mRequestId = -1;
//                if (!getIntent().getBooleanExtra("isSave", true))
//                    FileUtils.deleteFile(getIntent().getStringExtra("videoPath"));
                EventBus.getDefault().post(new HnDeleteVideoFileEvent(getIntent().getBooleanExtra("isSave", true)));
                publish(key);
            }

            @Override
            public void uploadProgress(int progress, int requestId) {
                mRequestId = requestId;
                mTvProgress.setText(progress + "");
                mSp.setProgress(progress);
            }

            @Override
            public void uploadError(int code, String msg) {
                mRequestId = -1;
                HnToastUtils.showToastShort(msg);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (isPublisSuccess) {
                HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
                HnAppManager.getInstance().finishActivity(HnChooseVideoActivity.class);
                HnAppManager.getInstance().finishActivity(TCVideoEditerActivity.class);
                HnAppManager.getInstance().finishActivity(TCVideoNoEditerActivity.class);
                HnAppManager.getInstance().finishActivity(TCVideoRecordActivity.class);
                finish();
                return true;
            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.mIvBack, R.id.mIvQq, R.id.mIvWx, R.id.mIvSina, R.id.mIvFrind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                if (isPublisSuccess) {
                    HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
                    HnAppManager.getInstance().finishActivity(HnChooseVideoActivity.class);
                    HnAppManager.getInstance().finishActivity(TCVideoEditerActivity.class);
                    HnAppManager.getInstance().finishActivity(TCVideoNoEditerActivity.class);
                    HnAppManager.getInstance().finishActivity(TCVideoRecordActivity.class);
                    finish();
                }

                break;
            case R.id.mIvQq:
                if (TextUtils.isEmpty(mShareUrl)) return;
                mShareAction.setPlatform(SHARE_MEDIA.QQ).withMedia(new UMImage(this, mImagUrl))
                        .withTargetUrl(mShareUrl).withText(HnUiUtils.getString(R.string.here_have_a_small_video_have_open))
                        .withTitle(HnUiUtils.getString(R.string.is_heris_here_most_voluptuous_little_video))
                        .setCallback(umShareListener).share();
                break;
            case R.id.mIvWx:
                if (TextUtils.isEmpty(mShareUrl)) return;
                mShareAction.setPlatform(SHARE_MEDIA.WEIXIN).withMedia(new UMImage(this, mImagUrl))
                        .withTargetUrl(mShareUrl).withText(HnUiUtils.getString(R.string.here_have_a_small_video_have_open))
                        .withTitle(HnUiUtils.getString(R.string.is_heris_here_most_voluptuous_little_video))
                        .setCallback(umShareListener).share();
                break;
            case R.id.mIvSina:
                if (TextUtils.isEmpty(mShareUrl)) return;
                mShareAction.setPlatform(SHARE_MEDIA.SINA).withMedia(new UMImage(this, mImagUrl))
                        .withTargetUrl(mShareUrl).withText(HnUiUtils.getString(R.string.here_have_a_small_video_have_open))
                        .withTitle(HnUiUtils.getString(R.string.is_heris_here_most_voluptuous_little_video))
                        .setCallback(umShareListener).share();
                break;
            case R.id.mIvFrind:
                if (TextUtils.isEmpty(mShareUrl)) return;
                mShareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(new UMImage(this, mImagUrl))
                        .withTargetUrl(mShareUrl).withText(HnUiUtils.getString(R.string.here_have_a_small_video_have_open))
                        .withTitle(HnUiUtils.getString(R.string.is_heris_here_most_voluptuous_little_video))
                        .setCallback(umShareListener).share();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wakeLock != null)
            wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock != null)
            wakeLock.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //将数据返给SDK处理
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (TextUtils.isEmpty(mVideoId)) return;
            shareSuccess();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
        }
    };


    private void publish(String videoUrl) {
        RequestParams params = new RequestParams();
        if (mLocationEntity != null) {
            params.put("city", mLocationEntity.getmCity());
            params.put("lat", mLocationEntity.getmLat());
            params.put("lng", mLocationEntity.getmLng());
        } else if (HnMainActivity.mLocEntity != null) {
            params.put("city", HnMainActivity.mLocEntity.getmCity());
            params.put("lat", HnMainActivity.mLocEntity.getmLat());
            params.put("lng", HnMainActivity.mLocEntity.getmLng());
        }

        params.put("type", getIntent().getStringExtra("cateid"));

        if (!TextUtils.isEmpty(mTitle)) params.put("title", mTitle);
        if (0 != getIntent().getIntExtra("payType", 0)) {
            params.put("price", getIntent().getStringExtra("price"));
        }
        params.put("play_url", videoUrl);
        params.put("duration", getIntent().getStringExtra("longTime"));
        params.put("cover", getIntent().getStringExtra("imageUrl"));
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_ADD_VIDEO, params, "VIDEO_APP_ADD_VIDEO", new HnResponseHandler<HnUpLoadVideoModel>(HnUpLoadVideoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                isPublisSuccess = true;
                mShareUrl = model.getD().getShare_url();
                mVideoId = model.getD().getVideo_id();
                mLlPublishing.setVisibility(View.GONE);
                mLlPublished.setVisibility(View.VISIBLE);

                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.RefreshVideoMineList, null));
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    private void shareSuccess() {
        RequestParams params = new RequestParams();
        params.put("video_id", mVideoId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_SHARE_VIDEO_SUCCESS, params, "VIDEO_APP_SHARE_VIDEO_SUCCESS", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (-1 != mRequestId) {
            HnUpLoadPhotoControl.canclerUpload(mRequestId);
        }
    }
}
