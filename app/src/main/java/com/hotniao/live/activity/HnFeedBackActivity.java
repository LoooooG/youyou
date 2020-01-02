package com.hotniao.live.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnEditText;
import com.hotniao.live.R;
import com.hotniao.live.biz.user.feedback.HnFeedBackBiz;
import com.hn.library.global.HnUrl;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：意见反馈
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFeedBackActivity extends BaseActivity implements BaseRequestStateListener {


    @BindView(R.id.et_data)
    EditText etData;
    @BindView(R.id.iv_pic1)
    FrescoImageView ivPic1;
    @BindView(R.id.iv_pic2)
    FrescoImageView ivPic2;
    @BindView(R.id.iv_pic3)
    FrescoImageView ivPic3;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.et_way)
    HnEditText etWay;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;

    //第一张图片的地址  用于提交给服务器
    private String picPath1;
    //第二张图片的地址  用于提交给服务器
    private String picPath2;
    //第三张图片的地址  用于提交给服务器
    private String picPath3;
    //标识 上传文件的位置
    private int currentUploadPicPos = 0;
    //意见反馈业务逻辑类，处理意见反馈业务
    private HnFeedBackBiz mHnFeedBackBiz;
//    private EditText[] mEts;
//    private HnButtonTextWatcher mWatcher;


    @OnClick({R.id.iv_pic1, R.id.iv_pic2, R.id.iv_pic3, R.id.iv_add, R.id.tv_commit})
    public void onClick(View view) {
        HnUtils.hideSoftInputFrom(HnFeedBackActivity.this,etData,etWay);
        switch (view.getId()) {
            case R.id.iv_pic1://图片1
                currentUploadPicPos = 0;
                mHnFeedBackBiz.addUploadPic();
                break;
            case R.id.iv_pic2://图片2
                currentUploadPicPos = 1;
                mHnFeedBackBiz.addUploadPic();
                break;
            case R.id.iv_pic3://图片3
                currentUploadPicPos = 2;
                mHnFeedBackBiz.addUploadPic();
                break;
            case R.id.iv_add://添加
                if (TextUtils.isEmpty(picPath1)) {
                    currentUploadPicPos = 0;
                } else if (TextUtils.isEmpty(picPath2)) {
                    currentUploadPicPos = 1;
                } else if (TextUtils.isEmpty(picPath3)) {
                    currentUploadPicPos = 2;
                }
                mHnFeedBackBiz.addUploadPic();
                break;
            case R.id.tv_commit://提交
                //意见
                String  feedback=etData.getText().toString();
                //联系方式
                String  way=etWay.getText().toString().trim();
                if(TextUtils.isEmpty(feedback)){
                    CommDialog.newInstance(HnFeedBackActivity.this).setClickListen(new CommDialog.OneSelDialog() {
                        @Override
                        public void sureClick() {

                        }
                    }).setTitle(getString(R.string.str_feedback)).setContent(getString(R.string.please_your_position_gei_me)).setRightText(getString(R.string.i_know)).show();
                    return;
                }
                mHnFeedBackBiz.requestToCommitData(feedback,way,picPath1,picPath2,picPath3);
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.qustion_feekbeak);
        setListener();
        mHnFeedBackBiz = new HnFeedBackBiz(this);
        mHnFeedBackBiz.setBaseRequestStateListener(this);

    }

    /**
     * 对控件设置监听  当用户输入数据时，才可提交
     */
    private void setListener() {
//        mEts = new EditText[]{etData};
//        mWatcher = new HnButtonTextWatcher(tvCommit, mEts);
//        etData.addTextChangedListener(mWatcher);
    }

    @Override
    public void getInitData() {

    }


    /**
     * 请求中
     */
    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.please_wait_time), null);
    }

    /**
     * 请求成功
     *
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        done();
        if ("upload_file".equals(type)) {//上传文件成功，更新ui
            String key = response;
            HnLogUtils.i(TAG, "key：" + key);
            if (!TextUtils.isEmpty(key))
                updatePicData(key);
        }else  if("commit_feed_back".equals(type)){//意见反馈
            HnToastUtils.showToastShort(getString(R.string.commit_success));
            HnAppManager.getInstance().finishActivity();
        }
    }

    /**
     * 请求失败
     *
     * @param type
     * @param code
     * @param msg
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        done();
        if ("get_qiniu_token".equals(type)) {//获取七牛token失败
            HnToastUtils.showToastShort(msg);
        } else if ("upload_file".equals(type)) {//上传文件失败
            HnToastUtils.showToastShort(msg);
        }else  if("commit_feed_back".equals(type)){//意见反馈
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 更新图片相关资源
     *
     * @param key    七牛服务器返回的数据，用于提交给自己的服务器

     */
    private void updatePicData(String key) {
        switch (currentUploadPicPos) {
            case 0:
                picPath1 = key;
                ivPic1.setController(FrescoConfig.getController(key));
                ivPic1.setVisibility(View.VISIBLE);
                break;
            case 1:
                picPath2 = key;
                ivPic2.setController(FrescoConfig.getController(key));
                ivPic2.setVisibility(View.VISIBLE);
                break;
            case 2:
                picPath3 = key;
                ivPic3.setController(FrescoConfig.getController(key));
                ivPic3.setVisibility(View.VISIBLE);
                break;

        }
        if (!TextUtils.isEmpty(picPath2) && !TextUtils.isEmpty(picPath2) && !TextUtils.isEmpty(picPath3)) {
              llAdd.setVisibility(View.GONE);
        }
    }


}
