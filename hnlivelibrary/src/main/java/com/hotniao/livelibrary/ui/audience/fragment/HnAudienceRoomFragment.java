package com.hotniao.livelibrary.ui.audience.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.HnLiveListModel;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户直播间
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 *
 * @author mj
 */
public class HnAudienceRoomFragment extends BaseFragment {

    /**
     * 观众直播视频层
     */
    private HnAudienceLiveFragment mLiveFragment;
    /**
     * 观众直播交互层
     */
    private HnAudienceTopFragment mInfoFragment;
    /**
     * 直播所需要的数据
     */
    private HnLiveListModel.LiveListBean bean;

    public static HnAudienceRoomFragment newInstance(HnLiveListModel.LiveListBean bean) {
        Bundle args = new Bundle();
        args.putParcelable("data", bean);
        HnAudienceRoomFragment fragment = new HnAudienceRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.live_audience_room_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        try {
            if (bundle != null) {
                bean = bundle.getParcelable("data");
                initFargment();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 初始化fragment
     * HnAudienceLiveFragment 视频fragment 在底层
     * HnAudienceTopFragment 直播间信息 ，在上层(viewpager---<展示信息的fragment,一个是空的fragment>)
     */
    private void initFargment() {
        try {
            mLiveFragment = HnAudienceLiveFragment.newInstance(bean);
            mInfoFragment = HnAudienceTopFragment.newInstance(bean);
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.live_framel, mLiveFragment)
                    .commitAllowingStateLoss();
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.info_frame, mInfoFragment)
                    .commitAllowingStateLoss();
        }catch (Exception e){}
    }


}

