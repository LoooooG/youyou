package com.hotniao.live.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.base.CommListActivity;
import com.hotniao.live.biz.user.admin.HnAdminBiz;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.HnMyAdminModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的房管
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMyAdminActivity extends CommListActivity implements BaseRequestStateListener {

    private CommRecyclerAdapter mAdapter;
    private List<HnMyAdminModel.DBean.AnchorAdminBean.ItemsBean> mData = new ArrayList<>();

    private HnAdminBiz mAdminBiz;

    @Override
    protected String setTitle() {
        return HnUiUtils.getString(R.string.my_admin);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        setShowSubTitle(true);
        mSubtitle.setText(R.string.add_admin);
        mAdminBiz = new HnAdminBiz(this);
        mAdminBiz.setRegisterListener(this);

        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HnSearchAdminActivity.class);
            }
        });

        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.mTvName)).setText(mData.get(position).getUser_nickname());
                ((TextView) holder.getView(R.id.mTvFansNum)).setText(HnUiUtils.getString(R.string.fans_m) + mData.get(position).getUser_fans_total());
                //用户等级
                HnSkinTextView tvUserLevel = holder.getView(R.id.mTvUserLv);
                HnLiveLevelUtil.setAudienceLevBg(tvUserLevel, mData.get(position).getUser_level(), true);
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                holder.getView(R.id.mTvAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdminBiz.cancelAdmin(mData.get(position).getUser_id(), position);
                    }
                });
                holder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HnUserDetailDialog mHnUserDetailDialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(), HnApplication.getmUserBean().getUser_id(), 0);
//                        mHnUserDetailDialog.setActvity(HnMyAdminActivity.this);
//                        mHnUserDetailDialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                        HnUserHomeActivity.luncher(HnMyAdminActivity.this,mData.get(position).getUser_id());
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

        return mAdapter;
    }

    @Override
    protected RequestParams setRequestParam() {
        RequestParams params = new RequestParams();
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.LIVE_ROOMADMIN_INDEX;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnMyAdminModel>(HnMyAdminModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD().getAnchor_admin() == null) {
                    setEmpty(HnUiUtils. getString(R.string.now_no_admin_go_add), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getAnchor_admin().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_admin_go_add), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_admin_go_add), R.drawable.empty_com);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(HnRefreshDirection.TOP, 1);
    }

    @Override
    public void requesting() {
        showDoing(HnUiUtils.getString(R.string.loading), null);
    }

    @Override
    public void requestSuccess(String type, String uid, Object obj) {
        if(isFinishing())return;
        done();
        if (HnAdminBiz.Add_Admin.equals(type)) {

        } else if (HnAdminBiz.Delete_Admin.equals(type)) {
            int pos = (int) obj;
            if (!TextUtils.isEmpty(uid) && uid.equals(mData.get(pos).getUser_id())) {
                if (isFinishing()) return;

                mData.remove(pos);
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.operate_success));
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        done();
        if (HnAdminBiz.Add_Admin.equals(type)) {

        } else if (HnAdminBiz.Delete_Admin.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }
}
