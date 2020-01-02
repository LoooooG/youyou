package com.hotniao.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.live.R;
import com.hotniao.live.dialog.HnCommonListDialog;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：yibaobao
 * 类描述：
 * 创建人：oyke
 * 创建时间：2019/2/21 16:51
 * 修改人：oyke
 * 修改时间：2019/2/21 16:51
 * 修改备注：
 * Version:  1.0.0
 */
public class HnReleasePlayBackActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_price)
    HnEditText etPrice;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.ll_release_layout)
    RelativeLayout llReleaseLayout;
    @BindView(R.id.ll_success_layout)
    LinearLayout llSuccessLayout;
    private int type;
    private String logId;

    public static void launcher(Activity activity, String logId) {
        Intent intent = new Intent(activity, HnReleasePlayBackActivity.class);
        intent.putExtra("logId", logId);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.release_playback_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle("发布回放");
        setShowBack(true);
        logId = getIntent().getStringExtra("logId");
        if (TextUtils.isEmpty(logId)) {
            finish();
        }
    }

    @Override
    public void getInitData() {

    }

    @OnClick(R.id.rl_type)
    public void typeClick() {
        final ArrayList<String> options = new ArrayList<>();
        options.add("免费");
        options.add("VIP");
        options.add("付费");
        HnCommonListDialog dialog = HnCommonListDialog.newInstance(options);
        dialog.setOnItemClickListener(new HnCommonListDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                type = position;
                tvType.setText(options.get(position));
                if (position == 2) {
                    etPrice.requestFocus();
                    rlPrice.setVisibility(View.VISIBLE);
                } else {
                    rlPrice.setVisibility(View.GONE);
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.tv_release)
    public void release() {
        RequestParams params = new RequestParams();
        params.put("anchor_live_log_id", logId);
        if (type == 2) {
            String price = etPrice.getText().toString();
            if (!TextUtils.isEmpty(price)) {
                params.put("price", price);
            } else {
                HnToastUtils.showToastShort("请填写观看金额");
                return;
            }
        }
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.RELEASE_PLAYBACK, params, TAG, new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                llReleaseLayout.setVisibility(View.GONE);
                llSuccessLayout.setVisibility(View.VISIBLE);
                HnAppManager.getInstance().finishActivity(HnAnchorStopLiveActivity.class);
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {

    }

    @Override
    public void requestFail(String type, int code, String msg) {

    }

}
