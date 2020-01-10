package com.hotniao.video.activity;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.base.CommListActivity;
import com.hotniao.video.model.BounsModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnTimeUtils;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 邀请用户收益记录
 * create by Mr.x
 * on 13/8/2018
 */
public class BounsAct extends CommListActivity {
    private CommRecyclerAdapter mAdapter;

    private List<BounsModel.DBean.RecordListBean.ItemsBean> mData = new ArrayList<>();

    @Override
    protected String setTitle() {
        return "邀请用户收益记录";
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {


        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                holder.setTextViewText(R.id.mTvDate,
                        HnTimeUtils.millis2String(Long.valueOf(mData.get(position).getInvite().getTime())*1000, new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())));

                holder.setTextViewText(R.id.mTvBounsName, mData.get(position).getUser().getUser_nickname());
//                boolean isAnchor = getIntent().getBooleanExtra("isAnchor", false);
//                if (isAnchor) {
//                    holder.setTextViewText(R.id.mTvBouns, "+" + mData.get(position).getInvite().getConsume() + "元宝");
//                } else {
                holder.setTextViewText(R.id.mTvBouns, "+" + mData.get(position).getInvite().getConsume() + "金币");
//                }
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_bouns;
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
        params.put("type", "coin");
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.BOUNS;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<BounsModel>(BounsModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD() == null) {
                    setEmpty("暂无记录", R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getRecord_list().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty("暂无记录", R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty("暂无记录", R.drawable.empty_com);
            }
        };
    }
}
