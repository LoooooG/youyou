package com.hotniao.video.activity;

import android.view.View;
import android.widget.TextView;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.base.CommListActivity;
import com.hn.library.global.HnUrl;
import com.hotniao.video.model.HnInvitePeopleModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
//import com.hotniao.livelibrary.widget.dialog.HnUserDetailDialog;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我邀请的人
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMyInvitePeopleActivity extends CommListActivity {


    private CommRecyclerAdapter mAdapter;
    private List<HnInvitePeopleModel.DBean.InviteUserBean.ItemsBean> mData = new ArrayList<>();

    @Override
    protected String setTitle() {
        return HnUiUtils.getString(R.string.my_invite_fiend);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        setTvHeadShow(true);
        mTvHead.setText("");
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((FrescoImageView)holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                ((TextView)holder.getView(R.id.mTvJuniorNum)).setText(getString(R.string.ta_have)+mData.get(position).getUser_invite_total()+getString(R.string.num_junior));
                ((TextView)holder.getView(R.id.mTvName)).setText(mData.get(position).getUser_nickname());
                HnLiveLevelUtil.setAudienceLevBg((HnSkinTextView) holder.getView(R.id.tv_level),mData.get(position).getUser_level(),true);

                holder.getView(R.id.mIvImg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HnUserDetailDialog dialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(),  HnPrefUtils.getString(NetConstant.User.UID, ""), 0);
//                        dialog.setActvity(HnMyInvitePeopleActivity.this);
//                        dialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                        HnUserHomeActivity.luncher(HnMyInvitePeopleActivity.this,mData.get(position).getUser_id());
                    }
                });
            }


            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_my_invite_people;
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
        return HnUrl.USER_INVITE_INDEX;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnInvitePeopleModel>(HnInvitePeopleModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD().getInvite_user() == null) {
                    setEmpty(HnUiUtils.getString(R.string.go_to_invite_friends), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getInvite_user().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();

                mTvHead.setText(HnUiUtils.getString(R.string.all_have)+mData.size()+HnUiUtils.getString(R.string.num_xiaji_daili));
                setEmpty(HnUiUtils.getString(R.string.go_to_invite_friends), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.go_to_invite_friends), R.drawable.empty_com);
            }
        };
    }
}
