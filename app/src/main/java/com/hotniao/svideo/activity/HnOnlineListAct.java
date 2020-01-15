package com.hotniao.svideo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.base.CommListActivity;
import com.hotniao.svideo.model.HnOnlineListBean;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线用户
 * create by Mr.x
 * on 9/8/2018
 */
public class HnOnlineListAct extends CommListActivity {

    private CommRecyclerAdapter mAdapter;

    private List<HnOnlineListBean.DBean.ItemsBean> mData = new ArrayList<>();

    @Override
    protected String setTitle() {
        return HnUiUtils.getString(R.string.online_title);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                FrescoImageView mIvIcon = holder.getView(R.id.mIvIcon);
                mIvIcon.setImageURI(HnUrl.setImageUri(mData.get(position).getUser_avatar()));
                mIvIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HnUserHomeActivity.luncher(HnOnlineListAct.this, mData.get(position).getUser_id());
                    }
                });
                holder.setTextViewText(R.id.mTvNick, mData.get(position).getUser_nickname());
                holder.getView(R.id.mTvNext).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = mData.get(position).getUser_id();
                        if (!TextUtils.isEmpty(uid)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(HnConstants.Intent.DATA, uid);
                            bundle.putString(HnConstants.Intent.Name, mData.get(position).getUser_nickname());
                            bundle.putString(HnConstants.Intent.ChatRoomId, mData.get(position).getDialog_id());
                            openActivity(HnPrivateChatActivity.class, bundle);
                        }
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_online_list;
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
        return HnUrl.ONLINE_LIST;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnOnlineListBean>(HnOnlineListBean.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD().getItems() == null) {
                    setEmpty("暂无在线用户", R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty("暂无在线用户", R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty("暂无在线用户", R.drawable.empty_com);
            }
        };
    }
}
