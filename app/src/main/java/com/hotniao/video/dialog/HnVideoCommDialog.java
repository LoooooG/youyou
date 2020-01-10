package com.hotniao.video.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.fragment.HnPlatformListFragment;
import com.hotniao.video.model.HnHomeLiveCateModel;
import com.hotniao.video.model.HnVideoCommModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.util.DataTimeUtils;
import com.hotniao.livelibrary.util.HnLiveScreentUtils;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频详情评论弹窗
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoCommDialog extends DialogFragment {

    protected RecyclerView mRecycler;
    protected PtrClassicFrameLayout mRefresh;
    protected HnLoadingLayout mLoadingLayout;
    protected TextView mTvCommNum;

    private HnCommAdapter mAdapter;
    private int mPage = 1;

    private Activity mActivity;
    private static HnVideoCommDialog dialog;
    private List<HnVideoCommModel.DBean.ItemsBean> mData = new ArrayList<>();
    private String mVideoId;

    public static HnVideoCommDialog newInstance(String videoId, String num) {
        dialog = new HnVideoCommDialog();
        Bundle bundle = new Bundle();
        bundle.putString("videoId", videoId);
        bundle.putString("num", num);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        mVideoId = getArguments().getString("videoId");
        View view = View.inflate(mActivity, R.layout.dailog_video_comm, null);

        initView(view);


        Dialog dialog = new Dialog(mActivity, R.style.BottomDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth());
//        params.height = (mActivity.getWindowManager().getDefaultDisplay().getWidth()/ 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f);
        window.setAttributes(params);
        return dialog;
    }

    private void initView(View view) {

        view.findViewById(R.id.mIvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.mViewCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.mLLInput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.replyClick(mVideoId,null);
            }
        });
        mRefresh = (PtrClassicFrameLayout) view.findViewById(R.id.mRefresh);
        mTvCommNum = (TextView) view.findViewById(R.id.mTvCommNum);
        mLoadingLayout = (HnLoadingLayout) view.findViewById(R.id.mHnLoadingLayout);
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycler.setHasFixedSize(true);
        mAdapter = new HnCommAdapter(mData);
        mRecycler.setAdapter(mAdapter);

        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) listener.replyClick(mVideoId, mData.get(position));
            }
        });


        mRefresh.setMode(PtrFrameLayout.Mode.REFRESH);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                mPage += 1;
                getCommData(mPage, HnRefreshDirection.BOTH);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mPage = 1;
                getCommData(mPage, HnRefreshDirection.TOP);
            }
        });
        mPage = 1;
        getCommData(mPage, HnRefreshDirection.TOP);

        setCommNum(getArguments().getString("num"));
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mData != null) mData.clear();
    }

    public HnVideoCommDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void replyClick(String videoId, HnVideoCommModel.DBean.ItemsBean item);
    }


    class HnCommAdapter extends BaseQuickAdapter<HnVideoCommModel.DBean.ItemsBean, BaseViewHolder> {

        public HnCommAdapter(@Nullable List<HnVideoCommModel.DBean.ItemsBean> data) {
            super(R.layout.adapter_video_detail_comm, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HnVideoCommModel.DBean.ItemsBean item) {
            ((FrescoImageView) helper.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(item.getUser_avatar()));
            helper.setText(R.id.mTvName, item.getUser_nickname());
            helper.setText(R.id.mTvContent, "0".equals(item.getF_user_id()) ? item.getContent() : "回复  " + item.getF_user_nickname() + "：" + item.getContent());
            helper.setText(R.id.mTvTime, DataTimeUtils.getTimestampString(Long.valueOf(item.getCreate_time())*1000));
        }
    }

    private void getCommData(final int page, final HnRefreshDirection state) {
        RequestParams params = new RequestParams();
        params.put("video_id", mVideoId);
        params.put("page", page + "");
        params.put("pagesize", "20");
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_REPLY_LIST, params, HnUrl.VIDEO_APP_REPLY_LIST, new HnResponseHandler<HnVideoCommModel>(HnVideoCommModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                if (model.getD().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_video_comm), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_video_comm), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mRefresh, page, 20, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_video_comm), R.drawable.empty_com);
            }
        });
    }

    /**
     * 设置评论数量
     */
    private void setCommNum(String num) {
        if (mTvCommNum == null) return;
        mTvCommNum.setText(String.format(HnUiUtils.getString(R.string.video_comm_num), num));
    }

    protected void setEmpty(String content, int res) {
        if (isAdded()) {
            if (mActivity == null) return;
            if (mData == null || mLoadingLayout == null) return;
            if (mData.size() < 1) {
                mLoadingLayout.setEmptyText(content);
                mLoadingLayout.setEmptyImage(res);
                setLoadViewState(HnLoadingLayout.Empty, mLoadingLayout);
            } else {
                setLoadViewState(HnLoadingLayout.Success, mLoadingLayout);
            }
        }
    }

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }

    protected void setLoadViewState(int state, HnLoadingLayout mHnLoadingLayout) {
        if (mHnLoadingLayout == null) return;
        if (state != mHnLoadingLayout.getStatus()) {
            mHnLoadingLayout.setStatus(state);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(EventBusBean event) {//是否有未读消息
        if (HnConstants.EventBus.RefreshVideoCommList.equals(event.getType())) {
            if (mActivity == null) return;
            mPage = 1;
            getCommData(mPage, HnRefreshDirection.TOP);
            setCommNum(event.getObj() + "");
        }
    }
}
