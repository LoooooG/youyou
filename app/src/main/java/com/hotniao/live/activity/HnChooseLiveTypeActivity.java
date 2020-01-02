package com.hotniao.live.activity;

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
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.biz.home.HnHomeCate;
import com.hotniao.live.model.HnHomeLiveCateModel;

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
@Route(path = "/live/chooseLiveTypeActivity")
public class HnChooseLiveTypeActivity extends BaseActivity {
    @BindView(R.id.mTvNowType)
    TextView mTvNowType;
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
    @BindView(R.id.mTvHead)
    TextView mTvHead;

    private CommRecyclerAdapter mAdapter;
    private List<Object> mData = new ArrayList<>();
    private HnHomeLiveCateModel.DBean mDbean;
    /**
     * 分类id   分类名
     */
    private String mSelectItem = "-1", mCateName;
    /**
     * 是否选择
     */
    private boolean isChange = false;


    /**
     * 跳转到选择直播类型页面
     *
     * @param activity
     * @param id       直播类型Id
     * @param type     直播类型
     */
    public static void luncher(Activity activity, String id, String type) {
        Intent intent = new Intent(activity, HnChooseLiveTypeActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, HnBeforeLiveSettingActivity.Choose_Cate_Code);

    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_live_type;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {

        setTitle(R.string.choose_live_type);
        setShowBack(true);
        mLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mSelectItem = getIntent().getStringExtra("id");
        mCateName = getIntent().getStringExtra("type");


        mRG.setVisibility(View.GONE);
        mTvNowType.setGravity(Gravity.LEFT);
        mTvNowType.setVisibility(View.VISIBLE);

        if ("0".equals(mSelectItem) && HnHomeCate.mCateData.size() > 0) {
            mSelectItem = HnHomeCate.mCateData.get(0).getAnchor_category_id();
            mCateName = HnHomeCate.mCateData.get(0).getAnchor_category_name();
        }

        if (TextUtils.isEmpty(mCateName))
            mTvNowType.setText("无");
        else
            mTvNowType.setText(mCateName);

        mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.mRb1:
                        changeData(false);
                        break;
                    case R.id.mRb2:
                        changeData(true);
                        break;
                }
            }
        });

    }

    /**
     * 根据选择类型  改变数据源
     *
     * @param isGame
     */
    private void changeData(boolean isGame) {
        if (mDbean != null && mData != null) {
            mData.clear();
            mData.addAll(mDbean.getLive_category());
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getInitData() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                final HnHomeLiveCateModel.DBean.LiveCategoryBean liveCategoryBean = (HnHomeLiveCateModel.DBean.LiveCategoryBean) mData.get(position);
                ((TextView) holder.getView(R.id.mTvType)).setText(liveCategoryBean.getAnchor_category_name());
                if (mSelectItem.equals(liveCategoryBean.getAnchor_category_id())
                        && liveCategoryBean.getAnchor_category_name().equals(mCateName))
                    holder.getView(R.id.mIvChoose).setVisibility(View.VISIBLE);
                else holder.getView(R.id.mIvChoose).setVisibility(View.GONE);
                holder.getView(R.id.mRlItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isChange = true;
                        setResult(1, new Intent().putExtra("id", liveCategoryBean.getAnchor_category_id())
                                .putExtra("type", liveCategoryBean.getAnchor_category_name()));
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
        HnHttpUtils.postRequest(HnUrl.Live_NAVBAR, null, HnUrl.Live_NAVBAR, new HnResponseHandler<HnHomeLiveCateModel>(HnHomeLiveCateModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (0 == model.getC() && model.getD() != null) {
                    mDbean = model.getD();
                    mData.clear();
                    /**
                     * 如果没有游戏分类  则只显示娱乐分类
                     */
                    mRG.setVisibility(View.GONE);
                    mTvNowType.setGravity(Gravity.LEFT);
                    mTvNowType.setVisibility(View.VISIBLE);
                    mData.addAll(model.getD().getLive_category());
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
        /**
         * 如果没有选择  并且不是游戏  则返回原来的数据
         * 考虑到游戏下架的情况
         */
        if (!isChange ) {
            setResult(1, new Intent().putExtra("id", mSelectItem)
                    .putExtra("type", mCateName));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
