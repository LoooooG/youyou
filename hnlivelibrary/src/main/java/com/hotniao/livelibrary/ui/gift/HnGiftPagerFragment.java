package com.hotniao.livelibrary.ui.gift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;
import com.hotniao.livelibrary.widget.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司
 * 项目名称：乐疯直播
 * 类描述：礼物每个选项的viewPager 通过适配器填充recycler->每一个礼物
 * 创建人：Mr.Xu
 * 创建时间：2017/3/29 029
 */
public class HnGiftPagerFragment extends BaseFragment {

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private GiftViewPagerAdapter mPagerAdapter;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private List<List<HnGiftListBean.GiftBean.ItemsBean>> mGiftListData = new ArrayList<>();
    private boolean isLive=true;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_gift_pager;
    }


    public HnGiftPagerFragment(List<List<HnGiftListBean.GiftBean.ItemsBean>> mGiftListData, boolean isLive) {
        if (mGiftListData != null) {
            this.mGiftListData = mGiftListData;
            this.isLive=isLive;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.mIndicator);

        mViewPager.setOffscreenPageLimit(mFragments.size());
        mPagerAdapter = new GiftViewPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);


        mIndicator.setViewPager(mViewPager);
        mIndicator.notifyDataSetChanged();
    }


    /**
     * 创建人：Mr.Xu
     * 方法描述：通过填充加载Recycle
     */
    class GiftViewPagerAdapter extends PagerAdapter {

        private WeakReference<ViewGroup> c;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            c = new WeakReference<ViewGroup>(container);
            RecyclerView gridView = (RecyclerView) LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_gift_list, null);
            gridView.setLayoutManager(new GridLayoutManager(mActivity, 4));
            gridView.setHasFixedSize(true);
            gridView.setAdapter(new HnGiftAdapter(container.getContext(), mGiftListData.get(position),isLive));
            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (mGiftListData != null) {
                return mGiftListData.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        /**
         * 清除选中状态
         */
        public void resetAll() {
            if (c == null || c.get() == null) {
                return;
            }
            if (c.get().getChildCount() > 0) {
                for (int i = 0; i < c.get().getChildCount(); i++) {
                    View v = c.get().getChildAt(i);
                    RecyclerView gridView = (RecyclerView) v;
                    HnGiftAdapter adapter = (HnGiftAdapter) gridView.getAdapter();
                    adapter.clearSelected();
                }
            }
        }

        /**
         * 更新各个GridView的数据状态
         */
        public void updateSelected(String id) {
            if (c == null || c.get() == null) {
                return;
            }
            if (c.get().getChildCount() > 0) {
                for (int i = 0; i < c.get().getChildCount(); i++) {
                    View v = c.get().getChildAt(i);
                    RecyclerView gridView = (RecyclerView) v;
                    HnGiftAdapter giftGridAdapter = (HnGiftAdapter) gridView.getAdapter();
                    giftGridAdapter.updateSelected(id);
                }
            }
        }
    }

    @Subscribe
    public void onGiftEventHandle(HnGiftEven event) {
        //TODO
        mPagerAdapter.resetAll();
        mPagerAdapter.updateSelected(event.getId());
    }


    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
