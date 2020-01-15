package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.widget.squprogress.SquareProgressBar;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.hotniao.livelibrary.model.HnUploadPhotoModel;
import com.loopj.android.http.RequestParams;
import com.videolibrary.activity.HnChooseVideoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

public class HnVideoAuthApplyActivity extends BaseActivity {
    public static final int NoApply = 1;//未申请
    public static final int Uploading = 2;//正在上传
    public static final int AuthPass = 3;//认证通过
    public static final int Authing = 4;//认证中
    public static final int AuthNoPass = 5;//认证不通过
    public static final int CheckNoPass = 6;//审核不通过


    @BindView(R.id.mIvBack)
    ImageView mIvBack;
    @BindView(R.id.mIvAdd)
    ImageView mIvAdd;
    @BindView(R.id.mTvTitle)
    TextView mTvTitle;
    @BindView(R.id.mTvStatue)
    TextView mTvStatue;
    @BindView(R.id.mTvDetail)
    TextView mTvDetail;
    @BindView(R.id.mTvEdit)
    TextView mTvEdit;
    @BindView(R.id.mLLAdd)
    LinearLayout mLLAdd;
    @BindView(R.id.mSqPro)
    SquareProgressBar mSqPro;
    @BindView(R.id.mTvProgress)
    TextView mTvProgress;
    @BindView(R.id.mLlPublishing)
    LinearLayout mLlPublishing;
    @BindView(R.id.mLlPublished)
    LinearLayout mLlPublished;
    @BindView(R.id.mLLPro)
    LinearLayout mLLPro;

    @BindView(R.id.rl_background)
    RelativeLayout rlBackground;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.mTvState)
    TextView mTvState;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_reauth)
    TextView mTvReauth;
    @BindView(R.id.ll_cert_status)
    LinearLayout llCertStatus;

    private int mType, mOldType = -1;
    private String mVideoUrl, mVideoCover;
    private HnUploadPhotoModel.DBean.ConfigBean mUploadBean;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private int mUploadingType = HnUpLoadPhotoControl.UploadImage;
    private String mVideoLocalPath;
    private Bitmap mCoverBitmap;
    private File picFile;

    private int mRequestId = -1;


    public static void lunchor(Activity activity, int type) {
        activity.startActivity(new Intent(activity, HnVideoAuthApplyActivity.class).putExtra("type", type));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_auth_apply;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setShowTitleBar(false);
        mType = getIntent().getIntExtra("type", NoApply);
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        if (CheckNoPass == mType) {
            mOldType = CheckNoPass;
            mType = NoApply;
        }
        llCertStatus.setVisibility(View.GONE);
        setStatue();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (Uploading == mType) {
                setUploadingDialog();
            } else if (-1 == mOldType || CheckNoPass == mOldType) {
                finish();
            } else {
                mType = mOldType;
                mOldType = -1;
                setStatue();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setStatue() {
        if (NoApply == mType || CheckNoPass == mType) {
            mLLAdd.setVisibility(View.VISIBLE);
            mLLPro.setVisibility(View.GONE);
            mTvTitle.setVisibility(View.VISIBLE);
            llCertStatus.setVisibility(View.GONE);
            rlBackground.setBackgroundResource(R.drawable.bg_live);
        } else if (Uploading == mType) {
            llCertStatus.setVisibility(View.GONE);
            mTvTitle.setVisibility(View.GONE);
            mLLAdd.setVisibility(View.GONE);
            mLLPro.setVisibility(View.VISIBLE);
            mLlPublishing.setVisibility(View.VISIBLE);
            mLlPublished.setVisibility(View.GONE);
            rlBackground.setBackgroundResource(R.drawable.bg_live);
        } else if (AuthPass == mType) {
            mTvStatue.setText("您的约聊认证已通过！");
            mTvEdit.setText("修改约聊封面");
            llCertStatus.setVisibility(View.GONE);
            mTvTitle.setVisibility(View.VISIBLE);
            mLLAdd.setVisibility(View.GONE);
            mLLPro.setVisibility(View.VISIBLE);
            mLlPublishing.setVisibility(View.GONE);
            mTvStatue.setVisibility(View.VISIBLE);
            mTvDetail.setVisibility(View.VISIBLE);
            mLlPublished.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);
            mSqPro.setProgress(100);
            mSqPro.setImage(HnApplication.getmUserBean().getUser_video_cover());
            rlBackground.setBackgroundResource(R.drawable.bg_live);
        } else if (Authing == mType) {
           /* mTvDetail.setText("您的约聊封面视频正在认证中,请耐心等待...");

            mTvTitle.setVisibility(View.VISIBLE);

            mLlPublishing.setVisibility(View.GONE);
            mTvStatue.setVisibility(View.GONE);
            mTvDetail.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.GONE);
            mLlPublished.setVisibility(View.VISIBLE);
            mSqPro.setProgress(100);
            rlBackground.setBackgroundResource(R.drawable.under_review);
            mSqPro.setImage(HnApplication.getmUserBean().getUser_video_cover());*/
            rlBackground.setBackgroundColor(getResources().getColor(R.color.white));
            mTvTitle.setVisibility(View.VISIBLE);
            mLLAdd.setVisibility(View.GONE);
            mLLPro.setVisibility(View.GONE);
            llCertStatus.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.under_review);
            mTvState.setText(R.string.sublim_success);
            tvDetail.setVisibility(View.VISIBLE);
            tvDetail.setText("你的约聊视频正在审核中，请耐心等待");
            tvDetail.setTextColor(getResources().getColor(R.color.comm_text_color_black_s));
            mTvReauth.setVisibility(View.GONE);
        } else if (AuthNoPass == mType) {
           /* mTvStatue.setText("认证未通过");
            mTvEdit.setText("重新提交");
            rlBackground.setBackgroundResource(R.drawable.non_approval);
            mTvTitle.setVisibility(View.VISIBLE);
            mLlPublishing.setVisibility(View.GONE);
            mTvStatue.setVisibility(View.VISIBLE);
            mTvDetail.setVisibility(View.GONE);
            mTvEdit.setVisibility(View.VISIBLE);
            mLlPublished.setVisibility(View.VISIBLE);*/
            mTvTitle.setVisibility(View.VISIBLE);
            mLLAdd.setVisibility(View.GONE);
            mLLPro.setVisibility(View.GONE);
            llCertStatus.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.non_approval);
            mTvReauth.setVisibility(View.VISIBLE);
            rlBackground.setBackgroundColor(getResources().getColor(R.color.white));
            tvDetail.setVisibility(View.GONE);
            mTvState.setText("抱歉，您的约聊视频审核未通过");
            mTvReauth.setText(R.string.auth_fai);
        }
    }

    @Override
    public void getInitData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock != null)
            wakeLock.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wakeLock != null)
            wakeLock.acquire();
    }


    private void setUploadingDialog() {
        CommDialog.newInstance(HnVideoAuthApplyActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {

                if (-1 != mRequestId) {
                    HnUpLoadPhotoControl.canclerUpload(mRequestId);
                    mRequestId = -1;
                }
                if ((-1 == mOldType || CheckNoPass == mOldType) && Uploading != mType) {
                    finish();
                } else {
                    if (Uploading == mType) {
                        mType = NoApply;
                    } else {
                        mType = mOldType;
                        mOldType = -1;
                    }
                    setStatue();
                }
            }
        }).setTitle("视屏认证").setContent("视频还在上传中，取消后将中断上传~").show();
    }

    @OnClick({R.id.mIvBack, R.id.mIvAdd, R.id.mTvSumbit, R.id.mTvEdit, R.id.mSqPro,R.id.tv_reauth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                if (Uploading == mType) {
                    setUploadingDialog();
                } else if (-1 == mOldType || CheckNoPass == mOldType) {
                    finish();
                } else {
                    mType = mOldType;
                    mOldType = -1;
                    setStatue();
                }

                break;
            case R.id.mIvAdd:

                HnChooseVideoActivity.luncher(HnVideoAuthApplyActivity.this, HnChooseVideoActivity.ChatVideo);
                break;
            case R.id.mTvSumbit:
                if (TextUtils.isEmpty(mVideoLocalPath)) {
                    HnToastUtils.showCenterToast("请选择上传视频");
                } else {
                    uploadVideo(mVideoLocalPath);
                }
                break;
            case R.id.mTvEdit:
            case R.id.tv_reauth:
                if (AuthNoPass == mType) {
                    mOldType = mType;
                    mType = NoApply;
                    setStatue();
                } else if (AuthPass == mType) {
                    mOldType = mType;
                    mType = NoApply;
                    setStatue();
                }
                break;
            case R.id.mSqPro:
                if (AuthPass == mType)
                    HnPlayBackVideoActivity.luncher(HnVideoAuthApplyActivity.this, HnApplication.getmUserBean().getUser_id(), HnApplication.getmUserBean().getUser_video(), 2, HnApplication.getmUserBean().getUser_video_cover());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (HnChooseVideoActivity.RequestCode == requestCode) {
            /**
             *
             */
            if (data != null) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chooseVideoEvent(EventBusBean event) {
        if (event != null && HnConstants.EventBus.ChooseChatVideo == event.getType()) {
            if (!TextUtils.isEmpty(event.getObj() + "")) {
                mVideoLocalPath = (String) event.getObj();
                mCoverBitmap = HnPhotoUtils.getVideoBg(mVideoLocalPath);
                mIvAdd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mIvAdd.setImageBitmap(mCoverBitmap);
                mSqPro.setColorRGB(0xff58cf58);
                mSqPro.setProgress(0);
                mSqPro.setImage(mCoverBitmap);
            }
        }
    }


    /**
     * 上传简介视频和封面
     */
    private void uploadVideo(final String path) {
        mType = Uploading;
        setStatue();
        String fileName = HnDateUtils.getCurrentDate("yyyyMMdd").toUpperCase() + EncryptUtils.encryptMD5ToString(HnUtils.createRandom(false, 5)) + ".png";
        picFile = HnPhotoUtils.bitmapToFile(mCoverBitmap, fileName);
        if (picFile.exists()) {
            if (mUploadBean == null)
                HnUpLoadPhotoControl.getTenSign(picFile, HnUpLoadPhotoControl.UploadImage, HnUpLoadPhotoControl.ReadPublic);
            else {
                HnUpLoadPhotoControl.upload(mUploadBean, picFile, HnUpLoadPhotoControl.UploadImage);
            }
            mUploadingType = HnUpLoadPhotoControl.UploadImage;
            HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
                @Override
                public void uploadSuccess(final String key, Object obj, int type) {
                    if (HnUpLoadPhotoControl.UploadImage == type && Uploading == mType) {
                        mVideoCover = key;
                        mUploadBean = (HnUploadPhotoModel.DBean.ConfigBean) obj;
                        HnUpLoadPhotoControl.upload(mUploadBean, new File(path), HnUpLoadPhotoControl.UploadVideo);
                        mUploadingType = HnUpLoadPhotoControl.UploadVideo;
                    } else {
                        mRequestId = -1;
                        mVideoUrl = key;
                        saveVideo(mVideoUrl, mVideoCover);
                    }


                }

                @Override
                public void uploadProgress(int progress, int requestId) {
                    mRequestId = requestId;

                    if (mUploadingType == HnUpLoadPhotoControl.UploadVideo) {
                        mSqPro.setProgress(progress);
                        mTvProgress.setText(progress + "");
                    }
                }

                @Override
                public void uploadError(int code, String msg) {
                    mRequestId = -1;
                    mType = NoApply;
                    setStatue();
                    HnToastUtils.showToastShort(msg);
                }
            });
        }
    }

    public Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                    int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 保存用户上传简介的视频
     */
    public void saveVideo(String video, final String videoCover) {
        if (isFinishing()) return;
        RequestParams param = new RequestParams();
        param.put("user_video", video + "");
        param.put("user_video_cover", videoCover + "");

        HnHttpUtils.postRequest(HnUrl.SAVE_USER_INFO, param, "SAVE_USER_INFO", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                HnFileUtils.deleteFile(picFile);
                HnApplication.getmUserBean().setUser_video_cover(videoCover);
                if (AuthPass == mOldType || CheckNoPass == mOldType) {
                    HnAppManager.getInstance().finishActivity(HnVideoAuthStatueActivity.class);
                    HnAppManager.getInstance().finishActivity(HnAnchorFunctionActivity.class);
                    mOldType = -1;
                    HnApplication.getmUserBean().setVideo_authentication("4");
                    HnVideoAuthStatueActivity.luncher(HnVideoAuthApplyActivity.this, "4");
                    finish();
                } else {
                    mOldType = -1;
                    mType = Authing;
                    setStatue();
                }


            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                HnToastUtils.showToastShort(msg + ",请稍后再试~");
                mType = NoApply;
                setStatue();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (-1 != mRequestId) HnUpLoadPhotoControl.canclerUpload(mRequestId);
        if (mCoverBitmap != null) mCoverBitmap.recycle();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
