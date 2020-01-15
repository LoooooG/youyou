package com.hotniao.svideo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.R;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.reslibrarytwo.HnSkinTextView;
import com.videolibrary.activity.HnChooseVideoActivity;
import com.videolibrary.util.TCConstants;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：小视频发布之前的填写信息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

@SuppressLint("Registered")
@Route(path = "/app/videoPublishBeforeActivity",group = "app")
public class HnVideoPublishBeforeActivity extends BaseActivity {
    @BindView(R.id.mEtTitle)
    HnEditText mEtTitle;
    @BindView(R.id.mTvNum)
    TextView mTvNum;
    @BindView(R.id.mTvCate)
    TextView mTvCate;
    @BindView(R.id.mIvCate)
    ImageView mIvCate;
    @BindView(R.id.mTvSave)
    TextView mTvSave;
    public static final int Choose_Cate_Code = 1;//选择分类

    private boolean isSave = true;

    private String mCateId, mCateName;

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_publish_before;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(false);
        setShowTitleBar(false);
        mTvSave.setSelected(true);
        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trim = editable.toString().trim();
                if (TextUtils.isEmpty(trim)) mTvNum.setText("0字");
                else mTvNum.setText(trim.length() + "字");
            }
        });
    }

    @Override
    public void getInitData() {

    }


    @OnClick({R.id.mIvBack, R.id.mLlCate, R.id.mTvSave, R.id.mTvPublish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mLlCate:
                HnChooseVideoTypeActivity.luncher(HnVideoPublishBeforeActivity.this, mCateId);
                break;
            case R.id.mTvSave:
                if (isSave) {
                    isSave = false;
                } else {
                    isSave = true;
                }
                mTvSave.setSelected(isSave);
                break;
            case R.id.mTvPublish:
                if (TextUtils.isEmpty(mCateId)) {
                    HnToastUtils.showToastShort("请选择视频分类");
                    return;
                }
                HnUpLoadPhotoControl.getTenSign(new File(getIntent().getExtras().getString(TCConstants.VIDEO_RECORD_COVERPATH)),HnUpLoadPhotoControl.UploadImage,HnUpLoadPhotoControl.ReadPublic);
                HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
                    @Override
                    public void uploadSuccess(final String key, Object token,int type) {
                        HnVideoPublishActivity.luncher(HnVideoPublishBeforeActivity.this,key,getIntent().getExtras().getString(TCConstants.VIDEO_RECORD_VIDEPATH),
                                mEtTitle.getText().toString().trim(),mCateId,getIntent().getExtras().getString(TCConstants.VIDEO_RECORD_DURATION),isSave);
                        finish();
                    }

                    @Override
                    public void uploadProgress(int progress,int requestId) {

                    }

                    @Override
                    public void uploadError(int code, String msg) {
                        HnToastUtils.showToastShort("封面"+msg);
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Choose_Cate_Code) {
            if (data != null && !TextUtils.isEmpty(data.getStringExtra("id"))) {
                mCateId = data.getStringExtra("id");
                mCateName = data.getStringExtra("name");
                mIvCate.setVisibility(View.GONE);
                mTvCate.setText(mCateName);
            }
        }
    }


}
