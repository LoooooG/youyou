package com.hotniao.live.fragment;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnInviteChatBeforeActivity;
import com.hotniao.live.activity.HnInviteChatPreviewActivity;
import com.hotniao.live.activity.HnPrivateChatActivity;
import com.hotniao.live.activity.HnUserClosedRankActivity;
import com.hotniao.live.activity.HnUserHomeActivity;
import com.hotniao.live.adapter.HnPhotoAdapter;
import com.hotniao.live.adapter.HnScrollViewPagerAdapter;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.dialog.HnDelBlackReportDialog;
import com.hotniao.live.dialog.HnReportUserDialog;
import com.hotniao.live.dialog.HnShareDialog;
import com.hotniao.live.fragment.userhome.HnUserHomeBackFragment;
import com.hotniao.live.fragment.userhome.HnUserHomeInfoFragment;
import com.hotniao.live.fragment.userhome.HnUserHomeVideoFragment;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.widget.scollorlayout.ScrollableLayout;
import com.hotniao.livelibrary.biz.HnUserDetailBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：用户主页
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserHomeFragment extends BaseFragment implements BaseRequestStateListener {
    @BindView(R.id.mIvBack)
    AppCompatImageButton mIvBack;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mIvMore)
    ImageView mIvMore;
    @BindView(R.id.mIvShare)
    ImageView mIvShare;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvImg;
    @BindView(R.id.mRlTop)
    RelativeLayout mRlTop;

    @BindView(R.id.mTvUserState)
    TextView mTvUserState;
    @BindView(R.id.mTvFansNum)
    TextView mTvFansNum;
    @BindView(R.id.mTvFouseNum)
    TextView mTvFouseNum;
    @BindView(R.id.mTvFocus)
    TextView mTvFocus;
    @BindView(R.id.mTvPrice)
    TextView mTvPrice;

    @BindView(R.id.mRecyclerClosed)
    RecyclerView mRecyclerClosed;

    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.mTvLv)
    HnSkinTextView mTvLv;
    @BindView(R.id.mTvAnchorLv)
    HnSkinTextView mTvAnchorLv;
    @BindView(R.id.mIvVip)
    ImageView mIvVip;
    @BindView(R.id.mIvSex)
    ImageView mIvSex;
    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;

    @BindView(R.id.mRlBottom)
    RelativeLayout mRlBottom;
    @BindView(R.id.mLlChatVideo)
    LinearLayout mLlChatVideo;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.mRlClosedFriends)
    RelativeLayout mRlClosedFriends;


    private HnUserDetailBiz mDetailBiz;
    private static String[] mTitles = {HnUiUtils.getString(R.string.user_info), HnUiUtils.getString(R.string.video_show)};
    private List<BaseScollFragment> mFragments = new ArrayList<>();

    private String mUid;

    public HnUserInfoDetailBean mUserInfo;
    private boolean mIsCared = false;

    private List<String> mPhotoData = new ArrayList<>();
    private HnPhotoAdapter mPhotoAdapter;

    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;

    private HnScrollViewPagerAdapter adapter;

    public static HnUserHomeFragment newInstance(String userId){
        HnUserHomeFragment fragment = new HnUserHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_home;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);

        mDetailBiz = new HnUserDetailBiz(mActivity);
        mDetailBiz.setBaseRequestStateListener(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mUid = getArguments().getString("userId");
        setListener();

        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager mPhotoManager = new LinearLayoutManager(mActivity);
        mPhotoManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerClosed.setLayoutManager(mPhotoManager);
        mRecyclerClosed.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mPhotoAdapter = new HnPhotoAdapter(mPhotoData, "circle");
        mPhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HnUserClosedRankActivity.luncher(mActivity, mUid, mUserInfo.getUser_nickname());
            }
        });

        mRecyclerClosed.setAdapter(mPhotoAdapter);

    }

    @Override
    protected void initData() {
        mDetailBiz.requestToUserDetail(mUid, mUid);
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
                    mRlTop.setVisibility(View.INVISIBLE);
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
                float a = (float) (currentY * 0.5);
                float factor = 10 * currentY * 0.1f / maxY;
                mRlTop.setBackgroundColor((Integer) new ArgbEvaluator()
                        .evaluate(Math.min(1, factor),
                                Color.TRANSPARENT, getResources().getColor(R.color.white)));
                mTvName.setTextColor(factor > 0.8 ? getResources().getColor(R.color.comm_text_color_black) : getResources().getColor(R.color.comm_text_color_white));
                mIvBack.setImageResource(factor > 0.8 ? R.drawable.ic_back_black : R.drawable.ic_back_white);
                mIvMore.setImageResource(factor > 0.8 ? R.drawable.yonghuzhuye_more_s : R.drawable.yonghuzhuye_more);
                mIvShare.setImageResource(factor > 0.8 ? R.drawable.yonghuzhuye_fenxiang_s : R.drawable.yonghuzhuye_fenxiang);
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
        if (mActivity.isFinishing()) return;
        if (mRlTop != null) mRlTop.setVisibility(View.VISIBLE);
        if (mRefresh != null) mRefresh.refreshComplete();

    }


    private void updataMessgae() {
        if (mUserInfo == null) return;
        if (mUserInfo.getUser_id().equals(HnApplication.getmUserBean().getUser_id())) {
            mRlBottom.setVisibility(View.GONE);
            mIvMore.setVisibility(View.GONE);
        } else {
            mRlBottom.setVisibility(View.VISIBLE);
            mIvMore.setVisibility(View.VISIBLE);
        }
        String userLvel = mUserInfo.getUser_level();
        HnLiveLevelUtil.setAudienceLevBg(mTvLv, userLvel, true);
        //主播等级
        String liveLevel = mUserInfo.getAnchor_level();
        if (TextUtils.isEmpty(liveLevel) || Integer.parseInt(liveLevel) < 1) {
            mTvAnchorLv.setVisibility(View.GONE);
        } else {
            mTvAnchorLv.setVisibility(View.VISIBLE);
            HnLiveLevelUtil.setAnchorLevBg(mTvAnchorLv, liveLevel, true);
        }


        mIvImg.setController(FrescoConfig.getController(mUserInfo.getUser_avatar()));
        //vip
        String vip_expire = mUserInfo.getUser_is_member();
        if (!TextUtils.isEmpty(vip_expire) && "Y".equals(vip_expire)) {
            mIvVip.setVisibility(View.VISIBLE);
        } else {
            mIvVip.setVisibility(View.GONE);
        }
        mTvName.setText(mUserInfo.getUser_nickname());

        mTvId.setText("ID:" + mUserInfo.getUser_id());
        //关注
        String careNumber = mUserInfo.getUser_follow_total();
        mTvFouseNum.setText(HnUtils.setNoPoint(careNumber));
        //粉丝
        String fans = mUserInfo.getUser_fans_total();
        mTvFansNum.setText(HnUtils.setNoPoint(fans));


        String sex = mUserInfo.getUser_sex();
        if ("1".equals(sex)) {//男
            mIvSex.setImageResource(R.mipmap.man);
        } else {
            mIvSex.setImageResource(R.mipmap.girl);
        }

        //是否已关注
        String isFollow = mUserInfo.getIs_follow();
        if ("N".equals(isFollow)) {
            mIsCared = false;
            mTvFocus.setText("+关注");
        } else {
            mIsCared = true;
            mTvFocus.setText(com.hotniao.livelibrary.R.string.live_cancle_care);
        }
        if("Y".equals(mUserInfo.getUser_is_anchor())){
            mTvUserState.setVisibility(View.VISIBLE);
            //是否在线 0 不在 1在
            if ("0".equals(mUserInfo.getIs_online())) {
                mTvUserState.setText(R.string.no_online);
            } else {
                mTvUserState.setText(R.string.onlines);
            }

        }else{
            mTvUserState.setVisibility(View.GONE);
        }
        mTvPrice.setText(String.format(HnUiUtils.getString(R.string.one_to_one_chat_video_pay_min),
                TextUtils.isEmpty(mUserInfo.getPrivate_price()) ? "0" : mUserInfo.getPrivate_price(), HnApplication.getmConfig().getCoin()));
        //亲密榜
        if (mUserInfo.getRank() != null) {
            mPhotoData.clear();
            mPhotoData.addAll(mUserInfo.getRank());
        }
        mPhotoAdapter.notifyDataSetChanged();
    }


@OnClick({R.id.mIvBack, R.id.mIvMore, R.id.mIvShare, R.id.mRlClosedFriends, R.id.mLlFocus, R.id.mLlChat, R.id.mLlChatVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                if(getActivity() instanceof HnInviteChatPreviewActivity){
                    ((HnInviteChatPreviewActivity)getActivity()).setCurrentPage(0);
                }
                break;
            case R.id.mIvMore:
                if (mUserInfo == null) return;
                HnDelBlackReportDialog.newInstance(HnDelBlackReportDialog.NoDelete, mUserInfo.isIs_black()).setClickListen(new HnDelBlackReportDialog.SelDialogListener() {
                    @Override
                    public void deleteClick() {

                    }

                    @Override
                    public void blackClick() {
                        if (HnApplication.getmUserBean().getUser_id().equals(mUserInfo.getUser_id()))
                            return;
                        if (mDetailBiz != null)
                            mDetailBiz.blackOpro(mUid, mUserInfo.isIs_black() ? "2" : "1");

                    }

                    @Override
                    public void reportClick() {
                        if (HnApplication.getmUserBean().getUser_id().equals(mUserInfo.getUser_id()))
                            return;
                        HnReportUserDialog.getInstance().setOnDialogCLickListener(new HnReportUserDialog.DialogClickListener() {
                            @Override
                            public void sureClick(String content) {
                                if (mDetailBiz != null) mDetailBiz.report(mUid, content);
                            }
                        }).show(mActivity.getFragmentManager(), "report");

                    }
                }).show(mActivity.getFragmentManager(), "more");
                break;
            case R.id.mIvShare:
                if (mUserInfo == null) return;
                HnShareDialog.newInstance(mShareAPI, mShareAction, mUserInfo.getUser_nickname() + getString(R.string.join_this_app_invite_you), HnUrl.setImageUrl(mUserInfo.getUser_avatar()),
                        mUserInfo.getShare_url(), mUserInfo.getUser_nickname() + getString(R.string.join_this_app)).show(mActivity.getFragmentManager(), "share");

                break;

            case R.id.mRlClosedFriends:

                break;
            case R.id.mLlFocus:
                if (mUserInfo == null) return;
                if (HnApplication.getmUserBean().getUser_id().equals(mUserInfo.getUser_id()))
                    return;
                mTvFocus.setEnabled(false);
                mDetailBiz.requestToFollow(mIsCared, mUid, mUid);
                break;
            case R.id.mLlChat:
                if (mUserInfo == null) return;
                if (HnApplication.getmUserBean().getUser_id().equals(mUserInfo.getUser_id()))
                    return;
                Bundle bundle = new Bundle();
                bundle.putString(HnLiveConstants.Intent.DATA, mUserInfo.getUser_id());
                bundle.putString(HnLiveConstants.Intent.Name, mUserInfo.getUser_nickname());
                bundle.putString(HnLiveConstants.Intent.ChatRoomId, mUserInfo.getChat_room_id());
                startActivity(new Intent(mActivity, HnPrivateChatActivity.class).putExtras(bundle));
                break;
            case R.id.mLlChatVideo:
                if (mUserInfo == null) return;
                if (HnApplication.getmUserBean().getUser_id().equals(mUserInfo.getUser_id()))
                    return;
                if(getActivity() instanceof HnInviteChatPreviewActivity){
                    ((HnInviteChatPreviewActivity)getActivity()).setCurrentPage(0);
                }
                break;

        }
    }

    /**
     * 添加fragment集合
     *
     * @return
     */
    private List<BaseScollFragment> getFragments(boolean isAnchor) {
        FragmentManager manager = getChildFragmentManager();
        List<BaseScollFragment> list = new ArrayList<>();

        HnUserHomeInfoFragment mineFragment
                = (HnUserHomeInfoFragment) manager.findFragmentByTag(HnUserHomeInfoFragment.TAG);
        if (mineFragment == null) {
            mineFragment = HnUserHomeInfoFragment.getInstance(mUid);
        }

        HnUserHomeBackFragment videoFragment
                = (HnUserHomeBackFragment) manager.findFragmentByTag(HnUserHomeBackFragment.TAG);
        if (videoFragment == null) {
            videoFragment = HnUserHomeBackFragment.getInstance(mUid);
        }

        HnUserHomeVideoFragment chatFragment
                = (HnUserHomeVideoFragment) manager.findFragmentByTag(HnUserHomeVideoFragment.TAG);
        if (chatFragment == null) {
            chatFragment = HnUserHomeVideoFragment.getInstance(mUid);
        }

        if(isAnchor){
            Collections.addAll(list, mineFragment,videoFragment, chatFragment);
        }else{
            Collections.addAll(list, mineFragment);
        }

        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoEvent(HnUserInfoDetailBean info) {
        if (info != null) {
            mUserInfo = info;
            initViews("Y".equals(info.getUser_is_anchor()));
            updataMessgae();
            hideLayoutByUserStatus("Y".equals(info.getUser_is_anchor()));
        }
    }

    private void initViews(boolean isAnchor){
        if(!isAnchor){
           rlPrice.setVisibility(View.GONE);
           mRlClosedFriends.setVisibility(View.GONE);
        }
        String[] mTitles;
        if(isAnchor){
            mTitles = new String[]{HnUiUtils.getString(R.string.user_info),HnUiUtils.getString(R.string.user_play_back), HnUiUtils.getString(R.string.video_show)};
        }else{
            mTitles = new String[]{HnUiUtils.getString(R.string.user_info)};
        }
        mFragments.addAll(getFragments(isAnchor));
        if(adapter == null){
            adapter = new HnScrollViewPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
            mViewPager.setOffscreenPageLimit(mFragments.size());
            mViewPager.setAdapter(adapter);
            mTab.setViewPager(mViewPager);
            mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(0));
            mViewPager.setCurrentItem(0);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    //判断是否为主播对部分功能进行隐藏
    private void hideLayoutByUserStatus(boolean isAnchor){
        if(!isAnchor){
            mTvUserState.setVisibility(View.GONE);
            mLlChatVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requesting() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(HnFollowEvent event) {
        if (event != null) {
            String uid = event.getUid();
            boolean isFollow = event.isFollow();
            if (!TextUtils.isEmpty(uid) && mUserInfo != null) {
                if (uid.equals(mUserInfo.getUser_id())) {
                    if (isFollow) {
                        mIsCared = true;
                        mUserInfo.setIs_follow("Y");
                    } else {
                        mIsCared = false;
                        mUserInfo.setIs_follow("N");
                    }
                    if ("N".equals(mUserInfo.getIs_follow())) {
                        mIsCared = false;
                        mTvFocus.setText("+关注");
                    } else {
                        mIsCared = true;
                        mTvFocus.setText(com.hotniao.livelibrary.R.string.live_cancle_care);
                    }
                }
            }
        }

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity.isFinishing()) return;
        if (HnUserDetailBiz.Follow.equals(type)) {
            mTvFocus.setEnabled(true);
            if (mIsCared) {
                mIsCared = false;
                mTvFocus.setText("+关注");
                mUserInfo.setIs_follow("N");
                HnToastUtils.showToastShort(getString(com.hotniao.livelibrary.R.string.live_cancle_follow_success));
                EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Follow, mUid));
                EventBus.getDefault().post(new HnFollowEvent(mUid, false));
            } else {
                mIsCared = true;
                mTvFocus.setText(com.hotniao.livelibrary.R.string.live_cancle_care);
                mUserInfo.setIs_follow("Y");
                HnToastUtils.showToastShort(getString(com.hotniao.livelibrary.R.string.live_follow_success));
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mUid));
                EventBus.getDefault().post(new HnFollowEvent(mUid, true));
            }
            EventBus.getDefault().post(new HnFollowEvent(mUid, mIsCared));
        } else if (HnUserDetailBiz.ADD_BLACK.equals(type)) {
            if (mUserInfo.isIs_black()) {
                HnToastUtils.showToastShort("取消拉黑成功");
            } else {
                HnToastUtils.showToastShort("拉黑成功");
            }
            mUserInfo.setIs_black(!mUserInfo.isIs_black());


        } else if (HnUserDetailBiz.REPORT.equals(type)) {
            HnToastUtils.showToastShort("举报成功");
        }else if(HnUserDetailBiz.UserInfoDetail.equals(type)){
            HnUserInfoDetailModel model = (HnUserInfoDetailModel) obj;
            if (model != null && model.getD() != null) {
                mUserInfo = model.getD();
                EventBus.getDefault().post(mUserInfo);
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        HnToastUtils.showToastShort(msg);
    }




}
