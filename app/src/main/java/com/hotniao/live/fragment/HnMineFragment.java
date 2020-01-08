package com.hotniao.live.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.daynight.DayNight;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginBean;
import com.hn.library.model.HnLoginModel;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnEditInfoActivity;
import com.hotniao.live.activity.HnMyFansActivity;
import com.hotniao.live.activity.HnMyFollowActivity;
import com.hotniao.live.activity.HnMyRechargeActivity;
import com.hotniao.live.adapter.HnScrollViewPagerAdapter;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.biz.user.userinfo.HnMineBiz;
import com.hotniao.live.fragment.mine.HnMineFunctionFragment;
import com.hotniao.live.fragment.mine.HnMineVideoFragment;
import com.hotniao.live.fragment.userhome.HnUserHomeBackFragment;
import com.hotniao.live.utils.HnAppConfigUtil;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.widget.scollorlayout.ScrollableLayout;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：我的模块
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMineFragment extends BaseFragment implements BaseRequestStateListener {

    @BindView(R.id.iv_skin)
    ImageView ivSkin;
    @BindView(R.id.iv_icon)
    FrescoImageView ivIcon;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_level)
    HnSkinTextView tvLevel;
    @BindView(R.id.mTvAnchorLv)
    HnSkinTextView mTvAnchorLv;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.ll_care)
    LinearLayout llCare;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.tv_car_number)
    TextView tvCarNumber;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_uid)
    TextView tvUid;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.mTvRecharge)
    TextView mTvRecharge;
    @BindView(R.id.iv_withdrawalsuccessful)
    ImageView ivWithdrawalsuccessful;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_edit_user_info)
    LinearLayout llEditUserInfo;
    @BindView(R.id.mRlCenter)
    RelativeLayout mRlCenter;
    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.iv_head_bg)
    FrescoImageView ivHeadBg;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.shadow_view)
    View shadowView;

    private static String[] mTitles = {HnUiUtils.getString(R.string.mine_my), HnUiUtils.getString(R.string.video_show)};
    private List<BaseScollFragment> mFragments = new ArrayList<>();


    /**
     * 我的业务逻辑类，用户处理我的相关业务
     */
    private HnMineBiz mHnMineFragmentBiz;
    /**
     * 个人用户信息数据对象
     */
    private HnLoginBean result;
    /**
     * 用户id
     */
    private String uid;

    /**
     * 条目存储容器
     */
    private List<RelativeLayout> mRlItemList;
    /**
     * 文字颜色为333的存储容器
     */
    private List<TextView> mText333List;
    /**
     * 文字颜色为666的存储容器
     */
    private List<TextView> mText666List;


    private DayNightHelper mDayNightHelper;

    @OnClick({R.id.tv_uid, R.id.mRlHead, R.id.ll_care, R.id.ll_fans,
            R.id.iv_skin, R.id.mIvEdit, R.id.mTvRecharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvRecharge://充值
                mActivity.openActivity(HnMyRechargeActivity.class);
                break;
            case R.id.ll_care://关注
                mActivity.openActivity(HnMyFollowActivity.class);
                break;
            case R.id.ll_fans://粉丝
                mActivity.openActivity(HnMyFansActivity.class);
                break;

            case R.id.mIvEdit://编辑用户资料
            case R.id.mRlHead://编辑用户资料
                if (result == null) return;
                Bundle bundle = new Bundle();
                bundle.putSerializable(HnConstants.Intent.DATA, result);
                mActivity.openActivity(HnEditInfoActivity.class, bundle);
                break;
            case R.id.iv_skin://切换皮肤模式
                mActivity.showAnimation();
                boolean isDay = mDayNightHelper.isDay();
                mDayNightHelper.setMode(isDay ? DayNight.NIGHT : DayNight.DAY);
                mActivity.setTheme(isDay ? R.style.NightTheme : R.style.DayTheme);
                refreshUI();
                ((HnMainActivity) mActivity).updateFragmentUI();
                break;
            case R.id.tv_uid://优号复制
//                if (result == null) return;
//                ClipBoardUtil.to(result.getUser_id());
//                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.id_clip));
                break;

        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.mine_framgnet;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),rlUserInfo);
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),ivHeadBg);
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),shadowView);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        mHnMineFragmentBiz = new HnMineBiz(mActivity);
        mHnMineFragmentBiz.setBaseRequestStateListener(this);
        mDayNightHelper = new DayNightHelper();


        mFragments.addAll(getFragments());
        HnScrollViewPagerAdapter adapter = new HnScrollViewPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.setAdapter(adapter);
        mTab.setViewPager(mViewPager);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(0));
        mViewPager.setCurrentItem(0);

        if (HnApplication.getmUserBean() != null) {
            updateUi(HnApplication.getmUserBean());
        }
        setListener();
    }

    @Override
    protected void initData() {
        mRlItemList = new ArrayList<>();
        mText333List = new ArrayList<>();
        mText666List = new ArrayList<>();


        //文字颜色为#333333
        mText333List.add(tvNick);
        mText333List.add(tvCarNumber);
        mText333List.add(tvFansNumber);
        mText333List.add(mTvTitle);
        mText333List.add(tvUid);


        refreshUI();

        mHnMineFragmentBiz.requestToUserInfo();
    }


    /**
     * 设置监听
     */
    private void setListener() {
        //刷新监听
        mRefresh.setEnabledNextPtrAtOnce(true);
        mRefresh.setKeepHeaderWhenRefresh(true);
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mFragments.size() > mViewPager.getCurrentItem()) {
                    mFragments.get(mViewPager.getCurrentItem()).pullToRefresh();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mScrollableLayout.isCanPullToRefresh()) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                return false;
            }
        });
        mScrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {

            }
        });

        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void refreshComplete() {
        if (mActivity == null) return;
        if (mRefresh != null) {
            mRefresh.refreshComplete();
        }
        if (mViewPager != null && 0 == mViewPager.getCurrentItem()) {
            updateUi(HnApplication.getmUserBean());

        }
    }

    @Override
    public void requesting() {

    }

    /**
     * 请求成功:获取用户信息
     *
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null) return;
        HnLoginModel model = (HnLoginModel) obj;
        if (model != null && model.getD() != null && model.getD().getUser_id() != null) {
            updateUi(model.getD());
        }
    }


    /**
     * 请求失败:获取用户信息
     *
     * @param type
     * @param
     * @param
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null) return;
        HnToastUtils.showToastShort(msg);
    }

    /**
     * 更新界面数据
     *
     * @param result
     */
    private void updateUi(HnLoginBean result) {
        HnPrefUtils.setBoolean(NetConstant.User.IS_ANCHOR, TextUtils.equals(result.getUser_is_anchor(), "Y"));
        EventBus.getDefault().post(new EventBusBean(0,HnConstants.EventBus.RefreshVideoAuthStatue,null));
        if (mActivity == null || result == null) return;
        try {
            this.result = result;

            //头像
            String logo = result.getUser_avatar();
            ivIcon.setController(FrescoConfig.getController(logo));
            ivHeadBg.setController(FrescoConfig.getController(logo));

            //昵称
            String nick = result.getUser_nickname();
            tvNick.setText(nick);
            //性别

            String sex = result.getUser_sex();
            if ("1".equals(sex)) {//男
                ivSex.setImageResource(R.mipmap.man);
            } else {
                ivSex.setImageResource(R.mipmap.girl);
            }
            //用户等级
            String userLvel = result.getUser_level();
            HnLiveLevelUtil.setAudienceLevBg(tvLevel, userLvel, true);
            //主播等级
            String liveLevel = result.getAnchor_level();
            if (TextUtils.isEmpty(liveLevel) || Integer.parseInt(liveLevel) < 1) {
                mTvAnchorLv.setVisibility(View.GONE);
            } else {
                mTvAnchorLv.setVisibility(View.VISIBLE);
                HnLiveLevelUtil.setAnchorLevBg(mTvAnchorLv, liveLevel, true);
            }
            //签名
            String intro = result.getUser_intro();
            if (!TextUtils.isEmpty(intro)) {
                tvIntro.setText(intro);
            }else {
                tvIntro.setText("你好像忘记签名了~");
            }
            //id
            uid = result.getUser_id();
            tvUid.setText(getString(R.string.u_hao) + uid);
            //关注
            String careNumber = result.getUser_follow_total();
            tvCarNumber.setText(HnUtils.setNoPoint(careNumber));
            //粉丝
            String fans = result.getUser_fans_total();
            tvFansNumber.setText(HnUtils.setNoPoint(fans));

            //vip
            String vip_expire = result.getUser_is_member();
            if (!TextUtils.isEmpty(vip_expire) && !"Y".equals(vip_expire)) {
                ivWithdrawalsuccessful.setVisibility(View.GONE);
            } else {
                ivWithdrawalsuccessful.setVisibility(View.GONE);
            }

            //实名认证
        } catch (Exception e) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mHnMineFragmentBiz != null) {
            mHnMineFragmentBiz.requestToUserInfo();
        }
        if (HnApplication.getmConfig() == null)
            HnAppConfigUtil.getConfig();
    }


    /**
     * 刷新UI界面
     */

    private void refreshUI() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 添加fragment集合
     *
     * @return
     */
    private List<BaseScollFragment> getFragments() {
        FragmentManager manager = getChildFragmentManager();
        List<BaseScollFragment> list = new ArrayList<>();

        HnMineFunctionFragment mineFragment
                = (HnMineFunctionFragment) manager.findFragmentByTag(HnMineFunctionFragment.TAG);
        if (mineFragment == null) {
            mineFragment = HnMineFunctionFragment.getInstance();
        }

//        HnUserHomeBackFragment playBackListFragment
//                = (HnUserHomeBackFragment) manager.findFragmentByTag(HnUserHomeBackFragment.TAG);
//        if (playBackListFragment == null) {
//            playBackListFragment = HnUserHomeBackFragment.getInstance(HnApplication.getmUserBean().getUser_id());
//        }

        HnMineVideoFragment videoFragment
                = (HnMineVideoFragment) manager.findFragmentByTag(HnMineVideoFragment.TAG);
        if (videoFragment == null) {
            videoFragment = HnMineVideoFragment.getInstance();
        }

//        HnMineChatVideoFragment chatFragment
//                = (HnMineChatVideoFragment) manager.findFragmentByTag(HnMineChatVideoFragment.TAG);
//        if (chatFragment == null) {
//            chatFragment = HnMineChatVideoFragment.getInstance();
//        }

        Collections.addAll(list, mineFragment, videoFragment);

        return list;
    }


}
