package com.hotniao.svideo.fragment.modify;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.refresh.PtrHandler;
import com.hn.library.refresh.PtrUIHandler;
import com.hn.library.refresh.indicator.PtrIndicator;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.svideo.HnMainActivity;
import com.hotniao.svideo.R;
import com.hotniao.svideo.adapter.HnHomeHotExtAdapter;
import com.hotniao.svideo.adapter.HnHomeVideoAdapter;
import com.hotniao.svideo.base.BaseScollFragment;
import com.hotniao.svideo.biz.home.HnHomeBiz;
import com.hotniao.svideo.eventbus.HnSelectLiveCateEvent;
import com.hotniao.svideo.fragment.HnHomeChildFragment;
import com.hotniao.svideo.model.HnBannerModel;
import com.hotniao.svideo.model.HnVideoModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: LooooG
 * created on: 2020/1/9 20:48
 * description: 首页视频页面
 */
@SuppressLint("ValidFragment")
public class HnIndexVideoFragment extends BaseScollFragment implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {
    public static final String TAG = "HnIndexVideoFragment";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtr;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    @BindView(R.id.mRlPer)
    RelativeLayout mRlPer;

    private View mEmptyLayout;

    /**
     * 头部布局  广告位
     */
    private View mHeaderView;
    private ConvenientBanner mBanner;
    /**
     * 广告数据源
     */
    private List<String> imgUrl = new ArrayList<>();
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 业务逻辑类，处理首页业务
     */
    private HnHomeBiz mHnHomeBiz;

    /**
     * 热门直播列表适配器
     */
    private HnHomeVideoAdapter mAdapter;
    /**
     * 直播列表数据源
     */
    private List<HnVideoModel.DBean.ItemsBean> mDatas = new ArrayList<>();

    private DayNightHelper mDayNightHelper;

    private String mVideoType = "0";


    @SuppressLint("ValidFragment")
    public HnIndexVideoFragment(String type) {
        this.mVideoType = type;
    }

    @Override
    public int getContentViewId() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return R.layout.common_loading_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLoading.setStatus(HnLoadingLayout.Loading);
        mLoading.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.now_no_data));
        mLoading.setOnReloadListener(this);
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
        //初始化适配器
        initAdapter();
        //事件监听
        initEvent();
        mDayNightHelper = new DayNightHelper();
        mPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                getData(mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
    }

    @Override
    protected void initData() {
        mPage = 1;
        getData(mPage);
        mHnHomeBiz.getBanner(1);
        refreshUI();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mRecyclerView.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mAdapter = new HnHomeVideoAdapter(mDatas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


        mHeaderView = mActivity.getLayoutInflater().inflate(R.layout.bannerview, null);
        mBanner = mHeaderView.findViewById(R.id.convenientBanner);
        mAdapter.addHeaderView(mHeaderView);
        com.hn.library.utils.HnUiUtils.setBannerHeight(mActivity, mBanner);


        /**空数据*/
        mEmptyLayout = LayoutInflater.from(mActivity).inflate(R.layout.empty_data_layout, (ViewGroup) mRecyclerView.getParent(), false);
        HnLoadingLayout mLoadLayout = mEmptyLayout.findViewById(R.id.mLoading);
        mLoadLayout.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.now_no_data));
        mLoadLayout.setStatus(HnLoadingLayout.Empty);

    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        //刷新监听
        mPtr.disableWhenHorizontalMove(true);
        mPtr.setMode(PtrFrameLayout.Mode.BOTH);
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

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null || mLoading == null) return;
        if (HnHomeBiz.FirstVideo.equals(type)) {
            HnVideoModel model = (HnVideoModel) obj;
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            mActivity.closeRefresh(mPtr);
            refreshComplete();

            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            } else {
                showEmptyView();
                mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            }
        } else if (HnHomeBiz.Banner.equals(type)) {
            HnBannerModel model = (HnBannerModel) obj;
            if (model.getD().getCarousel() == null || mBanner == null || mHnHomeBiz == null) return;
            mHnHomeBiz.initViewpager(mBanner, imgUrl, model.getD().getCarousel());
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mLoading == null) return;
        if (HnHomeBiz.FirstVideo.equals(type)) {
            mActivity.closeRefresh(mPtr);
            refreshComplete();
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            showEmptyView();
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 更新界面数据
     *
     * @param data 首页数据
     */
    private void updateUI(HnVideoModel.DBean data) {
        //直播列表
        List<HnVideoModel.DBean.ItemsBean> lives = data.getItems();

        if (lives != null && mAdapter != null) {
            if (lives.size() > 0) {
                if (mPage == 1) {
                    mDatas.clear();
                }
                mDatas.addAll(lives);
                mAdapter.notifyDataSetChanged();
                lives.clear();
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    mDatas.clear();
                }
            }
        }
        showEmptyView();
        // HnUiUtils.setRefreshMode(mPtr, mPage, 10, mDatas.size());

    }


    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }


    /**
     * 刷新UI界面
     */
    public void refreshUI() {
        //背景色
        TypedValue background = new TypedValue();
        //字体颜色#333333
        TypedValue textColor333 = new TypedValue();
        //条目背景颜色
        TypedValue item_color = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.pageBg_color, background, true);
        theme.resolveAttribute(R.attr.item_bg_color, item_color, true);
        theme.resolveAttribute(R.attr.text_color_333, textColor333, true);

        mRlPer.setBackgroundResource(background.resourceId);
        //根布局背景色
//        mLoading.setBackgroundResource(background.resourceId);
        Resources resources = getResources();
        mHeaderView.setBackgroundResource(background.resourceId);

        boolean isDay = mDayNightHelper.isDay();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventSelectCate(HnSelectLiveCateEvent event) {
        if (HnSelectLiveCateEvent.REFRESH_UI_VIDEO == event.getType()) {
            refreshUI();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 父fragment通知子fragment刷新
     */
    @Override
    public void pullToRefresh() {
        mPage = 1;
        mHnHomeBiz.getBanner(1);
        getData(mPage);
    }


    private void getData(int page) {
        if (HnMainActivity.mLocEntity == null)
            mHnHomeBiz.getIndexVideo(page, 1, null, null, null, mVideoType, null, null);
        else
            mHnHomeBiz.getIndexVideo(page, 1, HnMainActivity.mLocEntity.getmLng(), HnMainActivity.mLocEntity.getmLat(), null, mVideoType, null, null);
    }

    /**
     * 通知父fragment刷新结束
     */
    @Override
    public void refreshComplete() {
        if (this.getParentFragment() instanceof HnIndexVideoGroupFragment) {
            ((HnIndexVideoGroupFragment) (this.getParentFragment())).refreshComplete();
        }
    }

    /**
     * 没有直播列表数据时调用
     */
    public void showEmptyView() {
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
        if (mDatas == null || mDatas.size() < 1) {
            if (mAdapter != null && mEmptyLayout != null) {
                mAdapter.setNewData(null);
                if ((ViewGroup) (mEmptyLayout.getParent()) != null)
                    ((ViewGroup) (mEmptyLayout.getParent())).removeView(mEmptyLayout);
                mAdapter.setEmptyView(mEmptyLayout);
            }
        }

    }


}
