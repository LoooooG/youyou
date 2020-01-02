package com.hotniao.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.base.CommListActivity;
import com.hotniao.live.model.HnVideoMessageModel;
import com.hotniao.live.model.HnVideoRoomSwitchModel;
import com.hotniao.live.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频消息列表
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoMessageActivity extends CommListActivity {

    private CommRecyclerAdapter mAdapter;
    private List<HnVideoMessageModel.DBean.ItemsBean> mData = new ArrayList<>();

    @Override
    protected String setTitle() {
        return getString(R.string.video_message_detail);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {

        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                final HnVideoMessageModel.DBean.ItemsBean item = mData.get(position);
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(item.getUser_avatar()));
                ((FrescoImageView) holder.getView(R.id.mIvVideo)).setController(FrescoConfig.getHeadController(item.getCover()));

                ((TextView) holder.getView(R.id.mTvName)).setText(item.getUser_nickname());
                ((TextView) holder.getView(R.id.mTvTime)).setText(HnDateUtils.stampToDateMm(item.getCreate_time()));
                //1点赞 2评论
                if ("1".equals(item.getType())) {
                    holder.getView(R.id.mTvType).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mTvContent).setVisibility(View.INVISIBLE);
                    ((TextView) holder.getView(R.id.mTvType)).setText(R.string.zan_you_video);
                } else {
                    holder.getView(R.id.mTvType).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.mTvContent).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.mTvContent)).setText(item.getContent());
                }
                if ("0".equals(item.getIs_read())) {
                    holder.getView(R.id.mIvNew).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.mIvNew).setVisibility(View.GONE);
                }

                holder.getView(R.id.mRlClick).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        List<HnVideoRoomSwitchModel.DBean> datas = new ArrayList<>();
                        HnVideoRoomSwitchModel.DBean bean = new HnVideoRoomSwitchModel.DBean();
                        bean.setCover(item.getCover());
                        bean.setId(item.getId());
                        datas.add(bean);

                        Bundle bundle = new Bundle();
                        bundle.putInt("pos", 0);
                        bundle.putSerializable("data", (Serializable) datas);

                        HnVideoDetailActivity.luncher(HnVideoMessageActivity.this, bundle);


                        item.setIs_read("1");
                        mAdapter.notifyItemChanged(position);

                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_video_message;
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
        return HnUrl.VIDEO_MSG_INDEX;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnVideoMessageModel>(HnVideoMessageModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model == null || model.getD() == null || model.getD().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_video_message), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_video_message), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_video_message), R.drawable.empty_com);
            }
        };
    }


}
