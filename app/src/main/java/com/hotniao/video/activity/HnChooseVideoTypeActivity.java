package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.biz.home.HnHomeCate;
import com.hotniao.video.model.HnHomeLiveCateModel;
import com.hotniao.video.model.HnHomeVideoCateModle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
@Route(path = "/app/chooseVideoTypeActivity", group = "app")
public class HnChooseVideoTypeActivity extends BaseActivity {
    @BindView(R.id.mTvNowType)
    TextView mTvNowType;
    @BindView(R.id.mTvHead)
    TextView mTvHead;
    @BindView(R.id.mTvHind)
    TextView mTvHind;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mLoadingLayout)
    HnLoadingLayout mLoadingLayout;
    @BindView(R.id.mRG)
    RadioGroup mRG;
    @BindView(R.id.mRb1)
    RadioButton mRbLive;
    @BindView(R.id.mRb2)
    RadioButton mRbGame;

    private CommRecyclerAdapter mAdapter;
    private List<HnHomeVideoCateModle.DBean.VideoCategoryBean> mData = new ArrayList<>();

    private String mCateId,mCateName;

    /**
     * 跳转到选择直播类型页面
     *
     * @param activity
     */
    public static void luncher(Activity activity, String id) {
        Intent intent = new Intent(activity, HnChooseVideoTypeActivity.class);
        intent.putExtra("cateId", id);
        activity.startActivityForResult(intent, HnVideoPublishBeforeActivity.Choose_Cate_Code);

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_live_type;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {

        setTitle(R.string.choose_video_type);
        mTvHead.setText("当前视频频道：");
        mTvHind.setText("请选择适合自己的分类，精准的分类可以获得更多的点赞哦~");
        setShowBack(true);
        mLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mCateId = getIntent().getStringExtra("cateId");
        mCateName = getIntent().getStringExtra("name");
        if (TextUtils.isEmpty(mCateId)) mCateId = "-1";
        mRG.setVisibility(View.GONE);
        mTvNowType.setGravity(Gravity.LEFT);
        mTvNowType.setVisibility(View.VISIBLE);

        mTvNowType.setText(mCateName);

    }


    @Override
    public void getInitData() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                final HnHomeVideoCateModle.DBean.VideoCategoryBean videoCategoryBean = mData.get(position);
                ((TextView) holder.getView(R.id.mTvType)).setText(videoCategoryBean.getName());
                if (mCateId.equals(videoCategoryBean.getId()))
                    holder.getView(R.id.mIvChoose).setVisibility(View.VISIBLE);
                else holder.getView(R.id.mIvChoose).setVisibility(View.GONE);
                holder.getView(R.id.mRlItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(HnVideoPublishBeforeActivity.Choose_Cate_Code, new Intent().putExtra("id", videoCategoryBean.getId())
                                .putExtra("name", videoCategoryBean.getName()));
                        finish();
                    }
                });
            }


            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_choose_live_type;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
        getCateData();
    }

    private void getCateData() {
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_INDEX, null, HnUrl.Live_NAVBAR, new HnResponseHandler<HnHomeVideoCateModle>(HnHomeVideoCateModle.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (0 == model.getC() && model.getD() != null) {
                    mData.clear();
                    mRG.setVisibility(View.GONE);
                    mTvNowType.setGravity(Gravity.LEFT);
                    mTvNowType.setVisibility(View.VISIBLE);
                    mData.addAll(model.getD().getVideo_category());
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();

                } else {
                    HnToastUtils.showToastShort(model.getM());
                }

                setEmpty("暂无分类", R.drawable.empty_com);
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
                if (isFinishing()) return;

                setEmpty("暂无分类", R.drawable.empty_com);
            }
        });
    }


    protected void setEmpty(String content, int res) {
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() < 1) {
            mLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mLoadingLayout.setEmptyText(content);
        } else {
            mLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
