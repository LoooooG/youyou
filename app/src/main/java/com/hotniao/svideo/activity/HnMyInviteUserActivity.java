package com.hotniao.svideo.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.R;
import com.hotniao.svideo.adapter.HnMyInviteUserAdapter;
import com.hotniao.svideo.model.HnMyInviteUserModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * 我邀请的人
 */
public class HnMyInviteUserActivity extends BaseActivity implements HnLoadingLayout.OnReloadListener {

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout ptrRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;


    private HnMyInviteUserAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    /**
     * 页数
     */
    private int mPage = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_invite_user;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle("我邀请的人");
        setShowBack(true);
        initView();

    }

    private void initView() {

        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyText("暂无记录").setEmptyImage(R.drawable.empty_com);
        mHnLoadingLayout.setOnReloadListener(this);
        //初始化适配器
        initAdapter();
        //设置刷新监听
        setRefreshListener();

    }

    @Override
    public void getInitData() {
        mPage = 1;
        getData(1);
    }


    /**
     * 初始化适配器
     */
    public void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HnMyInviteUserAdapter();
            mLayoutManager = new LinearLayoutManager(HnUiUtils.getContext());
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerview.setLayoutManager(mLayoutManager);
            recyclerview.setHasFixedSize(true);
            recyclerview.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置刷新监听
     */
    private void setRefreshListener() {
        ptrRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                getData(mPage);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                getData(mPage);
            }
        });
    }


    /**
     * 获取数据
     */
    private void getData(final int mPage) {
        RequestParams params = new RequestParams();
        params.put("page",mPage);
        params.put("pagesize","10");
        HnHttpUtils.postRequest(HnUrl.MY_INVITE_USER, params, HnUrl.MY_INVITE_USER, new HnResponseHandler<HnMyInviteUserModel>(HnMyInviteUserModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                closeRefresh(ptrRefresh);
                setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
                if (model.getD() != null && model.getD().getItems() != null && model.getD().getItems().size() > 0) {
                    List<HnMyInviteUserModel.DBean.ItemsBean> items = model.getD().getItems();
                    if (mPage == 1) {
                        mAdapter.setNewData(items);
                    } else {
                        mAdapter.addData(items);
                    }
                } else {
                    if (mPage == 1) {
                        setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                        mHnLoadingLayout.setEmptyText("暂无记录");
                    }
                }
                HnUiUtils.setRefreshMode(ptrRefresh, mPage, 10, mAdapter.getItemCount());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mHnLoadingLayout == null) return;
                closeRefresh(ptrRefresh);
                if (mPage == 1) {
                    if (REQUEST_NET_ERROR == errCode) {
                        setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                    } else {
                        setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                    }
                } else {
                    HnToastUtils.showToastShort(msg);
                }
            }
        });
    }

    @Override
    public void onReload(View v) {
        getInitData();
    }
}
