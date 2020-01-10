package com.hotniao.video.fragment.near;

import android.app.AppOpsManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnHomeChatAdapter;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.model.HnHomeFastChatModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.biz.HnLocationBiz;
import com.hotniao.livelibrary.model.HnLocationEntity;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：附近模块
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnNearChatFragment extends BaseFragment implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener, HnLocationBiz.OnLocationListener {
    private static final int Open_Location = 0;//开启定位权限
    private static final int Open_LocationSer = 1;//开启定位服务
    private static final int Empty_Data = 2;//空数据

    @BindView(R.id.mRlPer)
    RelativeLayout mRlPer;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtr;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;

    /**
     * 空布局
     */
    private View notDataView;
    private TextView mTvEmptyClick;
    private HnSkinTextView mTvEmpty;
    private int mClickType = 2;//空数据点击类型

    /**
     * 关注界面业务逻辑类
     */
    private HnHomeBiz mHomeBiz;
    /**
     * 关注列表适配器
     */
    private HnHomeChatAdapter mAdapter;
    private List<HnHomeFastChatModel.DBean.ItemsBean> mData = new ArrayList<>();
    /**
     * 页数
     */
    private int mPage = 1;

    /**
     * 专门用于定位的工具
     */
    private HnLocationBiz mHnLocationBiz;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_follow;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoading.setStatus(HnLoadingLayout.Loading);
        mLoading.setOnReloadListener(this);
        mHomeBiz = new HnHomeBiz(mActivity);
        mHomeBiz.setBaseRequestStateListener(this);

        //初始化适配器
        initAdapter();

        initLocation();

    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        if (HnMainActivity.mLocEntity == null) {
            mHnLocationBiz = HnLocationBiz.getInsrance();
            mHnLocationBiz.setOnLocationListener(this);
            mHnLocationBiz.startLocation(mActivity);
        }

    }

    private String mLng, mLat,mLocal;

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        mPage = 1;
        if (HnMainActivity.mLocEntity != null) {
            mLng = HnMainActivity.mLocEntity.getmLng();
            mLat = HnMainActivity.mLocEntity.getmLat();
            mLocal = HnMainActivity.mLocEntity.getmCity();
        }
        mHomeBiz.getNearAnchorChatFast(mPage, mLng, mLat,mLocal);
    }

    @Override
    protected void initEvent() {
        mPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                mHomeBiz.getNearAnchorChatFast(mPage, mLng, mLat,mLocal);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHomeBiz.getNearAnchorChatFast(mPage, mLng, mLat,mLocal);
            }
        });

    }


    @Override
    public void requesting() {
    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null || mLoading == null) return;
        if (HnHomeBiz.ChatFastNear.equals(type)) {
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            mActivity.closeRefresh(mPtr);
            if (!isLocationEnabled()) {
                setEmpty();
                return;
            }
            HnHomeFastChatModel model = (HnHomeFastChatModel) obj;

            if (model.getD() != null && model.getD().getItems() != null) {
                updateUI(model.getD().getItems());
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    setEmpty();
                }
            }
        }

    }


    @Override
    public void requestFail(String type, int code, String msg) {
        if (mLoading == null) return;
        if (HnHomeBiz.ChatFastNear.equals(type)) {
            mActivity.closeRefresh(mPtr);
            if (mPage == 1) {
                setEmpty();
            } else {
                HnToastUtils.showToastShort(msg);
                setEmpty();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity != null && mAdapter != null) {
            if (mAdapter.getItemCount() < 1) {
                setEmpty();
            }
        }
    }

    private void setEmpty() {
        mAdapter.setNewData(null);
        if (!isLocationEnabled()) {
            setEmpty(R.drawable.home_open_position,
                    mClickType == Open_Location ? R.string.you_not_open_location_jurisdiction : R.string.you_not_open_location_service, mClickType);
        } else {
            setEmpty(R.drawable.home_open_position, R.string.no_data, Empty_Data);
        }
        if ((ViewGroup) (notDataView.getParent()) != null)
            ((ViewGroup) (notDataView.getParent())).removeView(notDataView);
        mAdapter.setEmptyView(notDataView);
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
    }

    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        initData();
    }


    /**
     * 更新界面ui
     *
     * @param items
     */
    private void updateUI(List<HnHomeFastChatModel.DBean.ItemsBean> items) {
        if (items != null && mAdapter != null) {
            if (items.size() > 0) {
                if (mPage == 1) {
                    mAdapter.setNewData(items);
                } else {
                    mAdapter.addData(items);
                }
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    mAdapter.setNewData(null);
                    if (!isLocationEnabled()) {
                        setEmpty(R.drawable.home_open_position,
                                mClickType == Open_Location ? R.string.you_not_open_location_jurisdiction : R.string.you_not_open_location_service, mClickType);
                    } else {
                        setEmpty(R.drawable.home_open_position, R.string.no_data, Empty_Data);
                    }
                    if ((ViewGroup) (notDataView.getParent()) != null)
                        ((ViewGroup) (notDataView.getParent())).removeView(notDataView);
                    mAdapter.setEmptyView(notDataView);

                }
            }
            HnUiUtils.setRefreshMode(mPtr, mPage, 10, mAdapter.getItemCount());
        }


    }


    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new HnHomeChatAdapter();
        //空页面
        notDataView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty_follow_fragment, (ViewGroup) mRecyclerView.getParent(), false);
        mTvEmptyClick = (TextView) notDataView.findViewById(R.id.tv_go_hot);
        mTvEmptyClick.setVisibility(View.GONE);
        mTvEmpty = (HnSkinTextView) notDataView.findViewById(R.id.mTvEmpty);
        mTvEmptyClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Open_Location == mClickType) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 1); // 设置完成后返回到原来的界面
                } else if (Open_LocationSer == mClickType) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            }
        });
        mRecyclerView.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mRecyclerView.setAdapter(mAdapter);

    }


    /**
     * 定位成功
     *
     * @param province
     * @param city
     * @param address
     * @param latitudeResult
     * @param longitudeResult
     */
    @Override
    public void onLocationSuccess(String province, String city, String address, String latitudeResult, String longitudeResult) {
        HnMainActivity.mLocEntity = new HnLocationEntity(latitudeResult, longitudeResult, city, province);
        mLng = HnMainActivity.mLocEntity.getmLng();
        mLat = HnMainActivity.mLocEntity.getmLat();
        mLocal = HnMainActivity.mLocEntity.getmCity();

        if (Open_Location == mClickType || Open_LocationSer == mClickType) {
            getData();
        }
    }

    @Override
    public void onLocationFail(String errorRease, int code) {

    }

    /**
     * 判断定位
     *
     * @return
     */
    public boolean isLocationEnabled() {

        if (!PermissionHelper.isLocServiceEnable(mActivity)) {//检测是否开启定位服务
            mClickType = Open_LocationSer;
            return false;
        } else {//检测用户是否将当前应用的定位权限拒绝
            int checkResult = PermissionHelper.checkOp(mActivity, 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
            int checkResult2 = PermissionHelper.checkOp(mActivity, 1, AppOpsManager.OPSTR_FINE_LOCATION);
            if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                mClickType = Open_Location;
                return false;
            }
        }
        return true;
    }

    /**
     * 设置空布局数据
     *
     * @param imgId
     * @param stringId
     * @param type
     */
    private void setEmpty(int imgId, int stringId, int type) {
        mTvEmpty.setTopDrawable(imgId);
        mTvEmpty.setText(stringId);
        mClickType = type;
        if (Empty_Data == type) {
            mTvEmptyClick.setVisibility(View.GONE);
        } else {
            mTvEmptyClick.setVisibility(View.VISIBLE);
            mTvEmptyClick.setText(getString(R.string.now_open));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (0 == requestCode || 1 == requestCode) initLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
