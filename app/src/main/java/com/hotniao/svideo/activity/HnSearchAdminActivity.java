package com.hotniao.svideo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.biz.user.admin.HnAdminBiz;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.model.HnSearchAdminModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：搜索房管页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSearchAdminActivity extends BaseActivity implements BaseRequestStateListener {
    @BindView(R.id.mEtSearch)
    HnEditText mEtSearch;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mLoadingLayout;

    private CommRecyclerAdapter mAdapter;
    private List<HnSearchAdminModel.DBean.UsersBean.ItemsBean> mData = new ArrayList<>();

    private HnAdminBiz mAdminBiz;
    //关键字
    private String mKeyWord;
    //页数
    private int mPage = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_admin;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        mAdminBiz = new HnAdminBiz(this);
        mAdminBiz.setRegisterListener(this);

    }

    private void setListener() {
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                mPage = mPage + 1;
                mAdminBiz.searchAdmin(mKeyWord, mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mPage = 1;
                mAdminBiz.searchAdmin(mKeyWord, mPage);
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        ((InputMethodManager) mEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                .hideSoftInputFromWindow(HnSearchAdminActivity.this.getCurrentFocus().getWindowToken(),
                                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                                        mKeyWord= mEtSearch.getText().toString().trim();
                                                        if (TextUtils.isEmpty(mKeyWord)) {
                                                            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.please_input_search_content));
                                                        } else {
                                                            mPage = 1;
                                                            mAdminBiz.searchAdmin(mKeyWord, mPage);
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }

        );
    }

    @Override
    public void getInitData() {
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.mTvName)).setText(mData.get(position).getUser_nickname());
                ((TextView) holder.getView(R.id.mTvFansNum)).setText(HnUiUtils.getString(R.string.fans_m) +mData.get(position).getUser_fans_total());
                //用户等级
                HnSkinTextView tvUserLevel = holder.getView(R.id.mTvUserLv);
                HnLiveLevelUtil.setAudienceLevBg(tvUserLevel, mData.get(position).getUser_level(), true);
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                TextView mTvAdd = (TextView) holder.getView(R.id.mTvAdd);
                if(TextUtils.isEmpty(mData.get(position).getUser_id())||mData.get(position).getUser_id().equals(HnApplication.getmUserBean().getUser_id())){
                    mTvAdd.setVisibility(View.GONE);
                }else {
                    mTvAdd.setVisibility(View.VISIBLE);
                }
                if ("Y".equals(mData.get(position).getIs_anchor_admin())) {
                    mTvAdd.setText(R.string.cancle_admin);
                    mTvAdd.setSelected(false);

                } else {
                    mTvAdd.setText(R.string.add_admin);
                    mTvAdd.setSelected(true);
                }
                holder.getView(R.id.mTvAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(mData.get(position).getUser_id())){
                            HnToastUtils.showToastShort("用户不存在");
                            return;
                        }
                        if(mData.get(position).getUser_id().equals(HnApplication.getmUserBean().getUser_id())){
                            HnToastUtils.showToastShort("不能添加自己为房管哦~");
                            return;
                        }
                        if ("Y".equals(mData.get(position).getIs_anchor_admin())) {
                            mAdminBiz.cancelAdmin(mData.get(position).getUser_id(), position);
                        } else {
                            mAdminBiz.addAdmin(mData.get(position).getUser_id(), position);
                        }
                    }
                });
                holder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HnUserDetailDialog mHnUserDetailDialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(), HnApplication.getmUserBean().getUser_id(), 0);
//                        mHnUserDetailDialog.setActvity(HnSearchAdminActivity.this);
//                        mHnUserDetailDialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                        HnUserHomeActivity.luncher(HnSearchAdminActivity.this,mData.get(position).getUser_id());
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_my_admin;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);

        setListener();
    }


    @OnClick(R.id.mTvCancel)
    public void onClick() {
        finish();
    }

    @Override
    public void requesting() {
    }

    @Override
    public void requestSuccess(String type, String uid, Object obj) {
        if(isFinishing())return;
        done();
        if (HnAdminBiz.Search_Admin.equals(type)) {
            HnSearchAdminModel model = (HnSearchAdminModel) obj;
            if (isFinishing()) return;
            refreshFinish();
            if (model.getD().getUsers() == null) {
                setEmpty(HnUiUtils.getString(R.string.now_no_search_data),R.drawable.home_no_search);
                return;
            }
            if (1 == mPage) {
                mData.clear();
            }
            mData.addAll(model.getD().getUsers().getItems());
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
            setEmpty(HnUiUtils.getString(R.string.now_no_search_data),R.drawable.home_no_search);
            HnUiUtils.setRefreshMode(mRefresh, mPage, 20, mData.size());
        } else if (HnAdminBiz.Add_Admin.equals(type)) {

            int pos = (int) obj;
            if (!TextUtils.isEmpty(uid) && uid.equals(mData.get(pos).getUser_id())) {
                mData.get(pos).setIs_anchor_admin("Y");
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.operate_success));
            }
        } else if (HnAdminBiz.Delete_Admin.equals(type)) {

            int pos = (int) obj;
            if (!TextUtils.isEmpty(uid) && uid.equals(mData.get(pos).getUser_id())) {
                mData.get(pos).setIs_anchor_admin("N");
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.operate_success));
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if(isFinishing())return;
        done();
        if (HnAdminBiz.Search_Admin.equals(type)) {
            refreshFinish();
            setEmpty(HnUiUtils.getString(R.string.now_no_search_data),R.drawable.home_no_search);
            HnToastUtils.showToastShort(msg);
        } else if (HnAdminBiz.Add_Admin.equals(type)) {
            HnToastUtils.showToastShort(msg);
        } else if (HnAdminBiz.Delete_Admin.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }


    protected void setEmpty(String content, int res) {
        if (isFinishing()||mLoadingLayout==null) return;
        if (mData.size() < 1) {
            mLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mLoadingLayout.setEmptyImage(res);
            mLoadingLayout.setEmptyText(content);
        } else {
            mLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }
}
