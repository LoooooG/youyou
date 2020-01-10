package com.hotniao.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnBlackListAdapter;
import com.hotniao.video.model.HnBlackListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;
import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;
import static com.hotniao.video.R.string.code;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/25 15:46
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBlackListActivity extends BaseActivity {
    private static final String TAG = "HnBlackListActivity";
    @BindView(R.id.rl_black_list)
    RecyclerView rlBlackList;
    @BindView(R.id.record_ptr_refresh)
    PtrClassicFrameLayout recordPtrRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    private HnBlackListAdapter listAdapter;

    private int page = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_black_list;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle("我的黑名单");
        setShowBack(true);
        listAdapter = new HnBlackListAdapter(this);
        rlBlackList.setAdapter(listAdapter);
        rlBlackList.setLayoutManager(new LinearLayoutManager(this));
        /*mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(HnUiUtils.getString(R.string.now_no_record));
        //刷新处理
        recordPtrRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page += 1;
                getInitData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                page = 1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                getInitData();
            }
        });*/
    }

    @Override
    public void getInitData() {
        RequestParam param = new RequestParam();
        param.put("page", page + "");
        param.put("pagesize", 20 + "");
        HnHttpUtils.getRequest(HnUrl.USER_BLACK_LIST, param, TAG, new HnResponseHandler<HnBlackListBean>(this, HnBlackListBean.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d(TAG, "hnSuccess");
                try {
//                    recordPtrRefresh.refreshComplete();
                    if (model.getC() == 0) {

                        System.out.println("model: " + model.toString());
                        System.out.println("model.getBean: " + model.getdBean().toString());

                        List<HnBlackListBean.DBean.ItemsBean> itemsList = model.getdBean().getItems();
                        HnLogUtils.d(TAG, "itemsList:"+itemsList.toString());
                        if (page == 1) {
                            if (itemsList.size() == 0) {
                                HnLogUtils.d(TAG, "itemsList.isEmpty()");
                                mHnLoadingLayout.setStatus(HnLoadingLayout.Empty);
                                mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.now_no_record));
                                return;
                            }
//                            mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                            listAdapter.setNewData(itemsList);
                        } else {
                            listAdapter.addData(itemsList);
                            if (itemsList.isEmpty()) {
//                                recordPtrRefresh.setMode(PtrFrameLayout.Mode.NONE);
                            }
                        }
//                        HnUiUtils.setRefreshMode(recordPtrRefresh, page, 20, listAdapter.getItemCount());
                    }
                } catch (Exception e) {
                    hnErr(RESPONSE_CODE_BAD, "");
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.e(TAG,"errCode:"+errCode+",msg:"+msg);
                if (page == 1) {
                    if (REQUEST_NET_ERROR == code) {
                        setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                    } else {
                        setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                    }
                } else {
//                    recordPtrRefresh.refreshComplete();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
