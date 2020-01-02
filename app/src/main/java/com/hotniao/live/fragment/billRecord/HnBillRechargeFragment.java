package com.hotniao.live.fragment.billRecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnBillRechargeAdapter;
import com.hotniao.live.biz.user.account.HnRechargeWithdrawBiz;
import com.hotniao.live.model.PayLogModel;
import com.hotniao.live.utils.HnUiUtils;

import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 - 充值
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillRechargeFragment extends BaseFragment implements HnLoadingLayout.OnReloadListener,BaseRequestStateListener {

    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mLvBillRec;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;

    /**适配器管理对象*/
    private LinearLayoutManager mLayoutManager;
    /**充值记录列表适配器*/
    private HnBillRechargeAdapter mSendAdapter;
    /**页数*/
    private  int  mPage=1;
    /**账户细节之充值/提现逻辑类*/
    private HnRechargeWithdrawBiz  mHnRechargeWithdrawBiz;

    public static HnBillRechargeFragment newInstance() {
        Bundle args = new Bundle();
        HnBillRechargeFragment fragment = new HnBillRechargeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.framgnet_bill_info;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.not_recharge_record)).setEmptyImage( R.drawable.empty_com);
        mHnLoadingLayout.setOnReloadListener(this);
        mHnRechargeWithdrawBiz=new HnRechargeWithdrawBiz(mActivity);
        mHnRechargeWithdrawBiz.setBaseRequestStateListener(this);
        //初始化适配器
        initAdapter();
        //设置刷新监听
        setRefreshListener();
    }

    @Override
    protected void initData() {
        mPage=1;
        mHnRechargeWithdrawBiz.requestToRechargeList(mPage);
    }

    /**
     * 请求中
     */
    @Override
    public void requesting() {

    }

    /**
     * 请求成功
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
         if(mHnLoadingLayout==null) return;
         mActivity.closeRefresh(mSwipeRefresh);
         if("recharge_list".equals(type)){//获取充值记录列表
             mActivity.setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
             PayLogModel  model= (PayLogModel) obj;
             if(model.getD()!=null&&model.getD().getRecharge_list()!=null&&model.getD().getRecharge_list().getItems().size()>0){
                 List<PayLogModel.DBean.RechargeListBean.ItemsBean> items = model.getD().getRecharge_list().getItems();
                 if(mPage==1){
                     mSendAdapter.setNewData(items);
                 }else{
                     mSendAdapter.addData(items);
                 }
             }else{
                 if(mPage==1){
                     mActivity.setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                     mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.not_recharge_record));
                 }
             }
             HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20,mSendAdapter.getItemCount());
         }
    }

    /**
     * 请求失败
     * @param type
     * @param code
     * @param msg
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if(mHnLoadingLayout==null) return;
        mActivity.closeRefresh(mSwipeRefresh);
        if("recharge_list".equals(type)){//获取充值记录列表
            if(mPage==1) {
                if (REQUEST_NET_ERROR == code) {
                    mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    mActivity.setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                }
            }else{
                HnToastUtils.showToastShort(msg);
            }
        }
    }

    /**
     * 重新加载
     * @param v
     */
    @Override
    public void onReload(View v) {
        initData();
    }

    /**
     * 初始化适配器
     */
    public  void initAdapter(){
        if(mSendAdapter==null) {
            mSendAdapter = new HnBillRechargeAdapter();
            mLayoutManager = new LinearLayoutManager(HnUiUtils.getContext());
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            mLvBillRec.setLayoutManager(mLayoutManager);
            mLvBillRec.setHasFixedSize(true);
            mLvBillRec.setAdapter(mSendAdapter);
        }else {
            mSendAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置刷新监听
     */
    private void setRefreshListener() {
        mSwipeRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage+=1;
                mHnRechargeWithdrawBiz.requestToRechargeList(mPage);

            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage=1;
                mHnRechargeWithdrawBiz.requestToRechargeList(mPage);
            }
        });
    }

}
