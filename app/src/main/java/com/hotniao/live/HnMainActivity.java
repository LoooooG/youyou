package com.hotniao.live;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.model.HnConfigModel;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.ImageTextButton;
import com.hotniao.live.activity.HnLoginActivity;
import com.hotniao.live.activity.HnWebActivity;
import com.hotniao.live.biz.HnMainBiz;
import com.hotniao.live.dialog.HnSignStatePopWindow;
import com.hotniao.live.dialog.HnUpGradeDialog;
import com.hotniao.live.eventbus.HnSignEvent;
import com.hotniao.live.fragment.HnHomeChatGropFragment;
import com.hotniao.live.fragment.HnHomeChildFragment;
import com.hotniao.live.fragment.HnHomeLiveFragment;
import com.hotniao.live.fragment.HnMineFragment;
import com.hotniao.live.fragment.HnMsgFragment;
import com.hotniao.live.fragment.HnShareGetMoneyFragment;
import com.hotniao.live.fragment.WaitingDevFragment;
import com.hotniao.live.model.HnSignStateModel;
import com.hotniao.live.utils.HnAppConfigUtil;
import com.hotniao.livelibrary.biz.HnLocationBiz;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.HnLocationEntity;
import com.hotniao.livelibrary.model.HnNoReadMessageModel;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.ui.HnStartServiceActivity;
import com.hotniao.livelibrary.widget.dialog.HnUserAccountForbiddenDialog;
import com.imlibrary.constant.TCConstants;
import com.imlibrary.login.TCLoginMgr;
import com.loopj.android.http.RequestParams;
import com.tencent.TIMManager;
import com.videolibrary.activity.HnChooseVideoActivity;
import com.videolibrary.activity.TCVideoRecordActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：项目主界面
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
/**
 * author: LooooG
 * Edited on: 2020/1/7 11:14
 * description: 首页修改->小视频、签到、分享赚钱、消息、我的
 */
@Route(path = "/app/HnMainActivity")
public class HnMainActivity extends BaseActivity implements BaseRequestStateListener {

    public static HnLocationEntity mLocEntity;
    @BindView(R.id.ib_home)
    ImageTextButton mIbHome; // 小视频
    @BindView(R.id.ib_msg)
    ImageTextButton mIbMsg; // 消息
    @BindView(R.id.ib_chat)
    ImageTextButton mIbChat; // 首页
    @BindView(R.id.ib_mine)
    ImageTextButton mIbMine; // 我的
    @BindView(R.id.main_bar)
    LinearLayout mMainBar;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;
    @BindView(R.id.mTvSign)
    TextView mTvSign;
    @BindView(R.id.mTvNewMsg)
    TextView mTvNewMsg;
    @BindView(R.id.mIvVideo)
    ImageView mIvVideo; // 丝友直播ICON
    @BindView(R.id.tv_live)
    TextView tvLive; // 丝友直播Label
    @BindView(R.id.mLlVideo)
    LinearLayout mLlVideo; // 丝友直播
    @BindView(R.id.mIvUpload)
    ImageView mIvUpload; // 上传视频 ICON
    @BindView(R.id.tv_upload)
    TextView tvUpload; // 上传视频 LABEL
    @BindView(R.id.mLlUpload)
    LinearLayout mLlUpload; // 上传视频
    //底部标签切换fragment
    private WaitingDevFragment waitingDevFragment;
    private HnHomeChildFragment mHomeFragment; // 小视频
    private HnMsgFragment mMsgFragment; // 消息
    private HnMineFragment mMineFragment; // 我的
    private HnHomeLiveFragment mLiveFragment; // 丝友直播
    private HnHomeChatGropFragment mChatFragment; // 首页
    private Disposable heartBeatObserver;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int color_normal, color_clicked;
    private long mLastTimes = 0;
    private int mTabHomeNor;
    private int mTabMsgNor;
    private int mTabLiveNor;
    private int mTabChatNor;
    private int mTabMeNor;
    //签到是否第一次进入
    private boolean isFirstSign = false;
    private HnSignStatePopWindow mPopWindow;
    //定位信息
    private HnLocationBiz mHnLocationBiz;
    private HnMainBiz mHnMainBiz;


    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        com.hn.library.utils.HnUiUtils.setFullScreen(this);
        setShowTitleBar(false);
        EventBus.getDefault().register(this);
        mHnMainBiz = new HnMainBiz(this);
        mHnMainBiz.setBaseRequestStateListener(this);
        mLocEntity = new HnLocationEntity("0", "0", "北京市", "北京市");

        Resources resources = getResources();
        color_normal = resources.getColor(R.color.comm_text_color_black);
        color_clicked = getResources().getColor(R.color.main_color);

        //启动服务  进行im链接
        startActivity(new Intent(this, HnStartServiceActivity.class));

        /**
         * 注册极光别名
         */
        if (HnApplication.getmUserBean() == null) {
            if (mHnMainBiz != null) mHnMainBiz.getUserInfo(1);
        } else {
            HnApplication.login(HnApplication.getmUserBean().getUser_id());
        }
        //版本检测
        if (HnApplication.getmConfig() == null || HnApplication.getmConfig().getVersion() == null)
            mHnMainBiz.requestToCheckVersion();
        else setVersion(HnApplication.getmConfig(), null);
        initLocation();
        initTabBarImage();


        heartBeatObserver = Observable.interval(30 * 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aVoid) throws Exception {
                        mHnMainBiz.online();
                    }
                });

//        mMainBar.setBackgroundColor(Color.argb((int) (255*0.8),255,255,255));
    }

    @Override
    public void getInitData() {
        fragmentManager = getSupportFragmentManager();
        mIbChat.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mHnMainBiz != null) {
            mHnMainBiz.getNoReadMessage();
            mHnMainBiz.getUserInfo(2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing() || mHnMainBiz == null) return;
                    if (mTvSign != null && mTvSign.getVisibility() != View.GONE) {
                        mHnMainBiz.getSignState("1");
                    }
                }
            }, 2000);
        }


        /**
         * 判断IM是否登录
         */
        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
            TCLoginMgr.getInstance().imLogin(TCConstants.getIdentify(), TCConstants.getUserSig(), TCConstants.getAppid(), TCConstants.getType());
        }

        /**
         * 配置为null  ，再次获取一次
         */
        if (HnApplication.getmConfig() == null) {
            HnAppConfigUtil.getConfig();
        }

    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        mHnLocationBiz = HnLocationBiz.getInsrance();
        mHnLocationBiz.startLocation(this);
        mHnLocationBiz.setOnLocationListener(new HnLocationBiz.OnLocationListener() {
            @Override
            public void onLocationSuccess(String province, String city, String address, String latitudeResult, String longitudeResult) {
                mLocEntity = null;
                mLocEntity = new HnLocationEntity(latitudeResult, longitudeResult, city, province);
                updateUserCity(mLocEntity.getmCity());
            }

            @Override
            public void onLocationFail(String errorRease, int code) {
            }
        });
    }

    private void updateUserCity(String city) {
        if (city != null) {
            RequestParams param = new RequestParams();
            param.put("city", city);
            HnHttpUtils.postRequest(HnUrl.UPDATE_USER_CITY, param, TAG, new HnResponseHandler<BaseResponseModel>(this,BaseResponseModel.class) {
                @Override
                public void hnSuccess(String response) {
                    HnLogUtils.d(TAG,"updateUserCity success");
                }

                @Override
                public void hnErr(int errCode, String msg) {
                    HnLogUtils.d(TAG,"updateUserCity err:"+errCode);
                }
            });
        }
    }


    @OnClick({R.id.mLlUpload, R.id.mLlVideo, R.id.ib_home, R.id.ib_msg, R.id.ib_chat, R.id.ib_mine})
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        ScaleAnimation videoAni;
        switch (view.getId()) {
            case R.id.mLlUpload: // 上传视频
                // hideFragments(fragmentTransaction);
                videoAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                mIvUpload.startAnimation(videoAni);
                HnChooseVideoActivity.luncher(this, HnChooseVideoActivity.PublishVideo);
                break;
            case R.id.mLlVideo: // 丝友直播
                hideFragments(fragmentTransaction);
                videoAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                clickTabLiveLayout();
                mIvVideo.startAnimation(videoAni);
                break;
            case R.id.ib_home: //首页 小视频
                hideFragments(fragmentTransaction);
                clickTabHomeLayout();

                ScaleAnimation homeAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                mIbHome.startAnimation(homeAni);

                break;
            case R.id.ib_msg: //消息
                hideFragments(fragmentTransaction);
                clickTabFollowLayout();
                ScaleAnimation followAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                mIbMsg.startAnimation(followAni);

                if (mHnMainBiz != null) {
                    mHnMainBiz.getNoReadMessage();
                }
                break;
//            case R.id.ib_live: //直播
//                hideFragments(fragmentTransaction);
//                clickTabLiveLayout();
//
//                ScaleAnimation liveAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
//                mIblive.startAnimation(liveAni);
//
//                break;
            case R.id.ib_chat: //约聊 首页
                hideFragments(fragmentTransaction);
                clickTabMsgLayout();

                ScaleAnimation msgAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                mIbChat.startAnimation(msgAni);
                break;
            case R.id.ib_mine: //我的
                hideFragments(fragmentTransaction);
                clickTabMineLayout();

                ScaleAnimation mineAni = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.bottom_icon);
                mIbMine.startAnimation(mineAni);

                if (mHnMainBiz != null) {
                    if (mTvSign != null && mTvSign.getVisibility() != View.GONE) {
                        mHnMainBiz.getSignState("2");
                    }
                }

                break;
        }

        fragmentTransaction.commitAllowingStateLoss();

    }

    public void openVideo() {


//        if (!HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR, false)) {
//            HnAuthStateActivity.luncher(this);
//            return;
//        }

        if (PermissionHelper.isCameraUseable() && PermissionHelper.isAudioRecordable()) {
            if (PermissionHelper.hasPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                TCVideoRecordActivity.luncher(this);
            } else {
                HnToastUtils.showToastShort("请开启存储权限");
            }
        } else {
            HnToastUtils.showToastShort("请开启相机或录音权限");
        }

    }


    /**
     * 隐藏所有显示的界面
     */

    private void hideFragments(FragmentTransaction transaction) {

        if(waitingDevFragment != null){
            transaction.hide(waitingDevFragment);
        }

        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }

        if (mMsgFragment != null) {
            transaction.hide(mMsgFragment);
        }

        if (mLiveFragment != null) {
            transaction.hide(mLiveFragment);
        }

        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }

        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
    }


    /**
     * 进入首页fragment
     */
    private void clickTabHomeLayout() {

        if (mHomeFragment == null) {
            // mHomeFragment，则创建一个并添加到界面上
            mHomeFragment = new HnHomeChildFragment();
            fragmentTransaction.add(R.id.content_layout, mHomeFragment);
        } else {
            // homeFrag不为空，则直接将它显示出来
            fragmentTransaction.show(mHomeFragment);
        }
        updateMenu(mIbHome);
    }

    private void clickTabWaitDevLayout() {

        if (waitingDevFragment == null) {
            waitingDevFragment = new WaitingDevFragment();
            fragmentTransaction.add(R.id.content_layout, waitingDevFragment);
        } else {
            // homeFrag不为空，则直接将它显示出来
            fragmentTransaction.show(waitingDevFragment);
        }
        updateMenu(null);
    }


    /**
     * 进入关注fragment
     */
    private void clickTabFollowLayout() {

        if (mMsgFragment == null) {
            // mHomeFragment，则创建一个并添加到界面上
            mMsgFragment = new HnMsgFragment();
            fragmentTransaction.add(R.id.content_layout, mMsgFragment);
        } else {
            // homeFrag不为空，则直接将它显示出来
            fragmentTransaction.show(mMsgFragment);
        }
        updateMenu(mIbMsg);
    }

    /**
     * 进入直播fragment
     */
    private void clickTabLiveLayout() {

        if (mLiveFragment == null) {
            // mHomeFragment，则创建一个并添加到界面上
            mLiveFragment = new HnHomeLiveFragment();
            fragmentTransaction.add(R.id.content_layout, mLiveFragment);
        } else {
            // homeFrag不为空，则直接将它显示出来
            fragmentTransaction.show(mLiveFragment);
        }
        if(!mLiveFragment.isVisible()){
            EventBus.getDefault().post(new EventBusBean(0,HnConstants.EventBus.RefreshLiveList,null));
        }
        updateMenu(null);
    }

    /**
     * 进入私信fragment
     */
    private void clickTabMsgLayout() {

        if (mChatFragment == null) {
            // mHomeFragment，则创建一个并添加到界面上
            mChatFragment = new HnHomeChatGropFragment();
            fragmentTransaction.add(R.id.content_layout, mChatFragment);
        } else {
            // homeFrag不为空，则直接将它显示出来
            fragmentTransaction.show(mChatFragment);
        }
        updateMenu(mIbChat);
    }

    /**
     * 进入我的fragment
     */
    private void clickTabMineLayout() {

        if (mMineFragment == null) {
            mMineFragment = new HnMineFragment();
            fragmentTransaction.add(R.id.content_layout, mMineFragment);
        } else {
            fragmentTransaction.show(mMineFragment);
        }
        updateMenu(mIbMine);
    }

    /**
     * 更新菜单状态
     */
    public void updateMenu(ImageTextButton button) {

        if(button == null){
//            tvLive.setTextColor(getResources().getColor(R.color.main_color));
        }else{
//            tvLive.setTextColor(getResources().getColor(R.color.comm_text_color_black));
        }

        if (mIbHome != null && button != mIbHome) {
            //normal
            mIbHome.changeState(mTabHomeNor, color_normal);
        } else {
            mIbHome.changeState(R.drawable.main_video_selected, color_clicked);
        }

        if (mIbMsg != null && button != mIbMsg) {
            //normal
            mIbMsg.changeState(mTabMsgNor, color_normal);

        } else {
            mIbMsg.changeState(R.drawable.msg_selected, color_clicked);
        }
//
//        if (mIblive != null && button != mIblive) {
//            //normal
//            mIblive.changeState(mTabLiveNor, color_normal);
//        } else {
//            mIblive.changeState(R.drawable.zhibo_selected, color_clicked);
//        }

        if (mIbChat != null && button != mIbChat) {
            //normal
            mIbChat.changeState(mTabChatNor, color_normal);

        } else {
            mIbChat.changeState(R.drawable.home_selected, color_clicked);
        }

        if (mIbMine != null && button != mIbMine) {
            //normal
            mIbMine.changeState(mTabMeNor, color_normal);

        } else {
            mIbMine.changeState(R.drawable.my_selected, color_clicked);
        }


    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastTimes > 1000) {
            mLastTimes = System.currentTimeMillis();
            HnToastUtils.showToastShort("再按一次退出");
        } else {
            EventBus.getDefault().post(new EventBusBean(0, "stop_websocket_service", null));
            finish();
            HnAppManager.getInstance().exit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(EventBusBean event) {
        if (event != null) {
            if (HnConstants.EventBus.Switch_Fragment.equals(event.getType())) {//切换到热门
                fragmentManager = getSupportFragmentManager();
                mIbChat.performClick();
            } else if (HnConstants.EventBus.LoginFailure.equals(event.getType())) {//登录状态失效
                HnLoginActivity.luncher(HnMainActivity.this, true);
                HnAppManager.getInstance().finishOthersActivity(HnLoginActivity.class);
            } else if (HnConstants.EventBus.Update_Unread_Count.equals(event.getType())) {
                if (mTvNewMsg != null) mTvNewMsg.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrivateEvent(HnPrivateMsgEvent event) {//收到私信消息
        if (event != null) {
            if (HnWebscoketConstants.Send_Pri_Msg.equals(event.getType())) {
                if (mHnMainBiz != null) {
                    mHnMainBiz.getNoReadMessage();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (heartBeatObserver != null) {
            heartBeatObserver.dispose();
        }
        EventBus.getDefault().post(new EventBusBean(0, "stop_websocket_service", null));
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    /**
     * 根据主题刷新tabbar
     */
    public void refreshTabBar() {

        TypedValue item_bg_color = new TypedValue();
        getTheme().resolveAttribute(R.attr.item_bg_color, item_bg_color, true);
        mMainBar.setBackgroundResource(item_bg_color.resourceId);

        //更换图标
        boolean isDay = mDayNightHelper.isDay();
        Resources resources = getResources();
        color_normal = isDay ? resources.getColor(R.color.black_tran) : resources.getColor(R.color.white);

        initTabBarImage();

        mIbHome.changeState(mTabHomeNor, color_normal);
        mIbMsg.changeState(mTabMsgNor, color_normal);
        mIbChat.changeState(mTabChatNor, color_normal);
//        mIblive.changeState(mTabLiveNor, color_normal);
        // mIbMine.changeState(mTabMeNor, color_normal);
    }


    /**
     * 初始化tabbar图标
     */
    private void initTabBarImage() {
        TypedValue ic_tab_home = new TypedValue();
        TypedValue ic_tab_msg = new TypedValue();
        TypedValue ic_tab_chat = new TypedValue();
        TypedValue ic_tab_me = new TypedValue();
        TypedValue ic_tab_live = new TypedValue();

        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.ic_tab_home, ic_tab_home, true);
        theme.resolveAttribute(R.attr.ic_tab_msg, ic_tab_msg, true);
        theme.resolveAttribute(R.attr.ic_tab_chat, ic_tab_chat, true);
        theme.resolveAttribute(R.attr.ic_tab_me, ic_tab_me, true);
        theme.resolveAttribute(R.attr.ic_tab_live, ic_tab_live, true);

        mTabHomeNor = ic_tab_home.resourceId;
        mTabMsgNor = ic_tab_msg.resourceId;
        mTabChatNor = ic_tab_chat.resourceId;
        mTabMeNor = ic_tab_me.resourceId;
        mTabLiveNor = ic_tab_live.resourceId;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean mUser_Forbidden = bundle.getBoolean("User_Forbidden");
            if (mUser_Forbidden) {
                HnUserAccountForbiddenDialog dialog = HnUserAccountForbiddenDialog.getInstance();
                dialog.show(getSupportFragmentManager(), "HnUserAccountForbiddenDialog");
            }
        }
    }

    /**
     * 更新FragmentUI
     */
    public void updateFragmentUI() {
        refreshTabBar();
        mHomeFragment.refreshUI();

        if (mMsgFragment != null) mMsgFragment.refreshUI();
//        if (mLiveFragment != null) mLiveFragment.refreshUI();
        if (mHomeFragment != null) mHomeFragment.refreshUI();

    }


    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        try {
            if (isFinishing()) return;
            if (HnMainBiz.CheckVersion.equalsIgnoreCase(type)) {//检测版本
                HnConfigModel model = (HnConfigModel) obj;
                setVersion(model.getD(), response);

            } else if (HnMainBiz.NoReadMsg.equalsIgnoreCase(type)) {//未读消息
                HnNoReadMessageModel model = (HnNoReadMessageModel) obj;
                if (model.getD().getUnread() != null) {
                    HnNoReadMessageModel.DBean.UnreadBean bean = model.getD().getUnread();
                    HnPrefUtils.setString(NetConstant.User.Unread_Count, bean.getUser_chat());
                    if (mTvNewMsg == null) return;
                    if (TextUtils.isEmpty(bean.getTotal())) {
                        mTvNewMsg.setVisibility(View.GONE);
                    } else {
                        try {
                            int num = Integer.parseInt(bean.getTotal());
                            if (num > 0) {
                                mTvNewMsg.setVisibility(View.VISIBLE);
                            } else {
                                mTvNewMsg.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            mTvNewMsg.setVisibility(View.GONE);
                        }
                    }

                }
            } else if (HnMainBiz.SignStatue.equalsIgnoreCase(type)) {//签名状态
                HnSignStateModel model = (HnSignStateModel) obj;
                if (model.getC() == 0 && model.getD().getUser_signin() != null) {
                    HnSignStateModel.DBean.UserSigninBean bean = model.getD().getUser_signin();
                    if ("Y".equals(bean.getIs_signin())) {
                        if ("1".equals(response)) mTvSign.setVisibility(View.INVISIBLE);
                        else mTvSign.setVisibility(View.GONE);
                        EventBus.getDefault().post(new HnSignEvent(true));
                    } else {
                        EventBus.getDefault().post(new HnSignEvent(false));
                        mTvSign.setVisibility(View.VISIBLE);
                        if (!isFirstSign) {
                            isFirstSign = true;
                            if (mPopWindow == null) {
                                mPopWindow = new HnSignStatePopWindow(HnMainActivity.this, bean.getTips());
                                mPopWindow.setOnItemClickListener(new HnSignStatePopWindow.OnItemClickListener() {
                                    @Override
                                    public void itemClick() {
                                        HnWebActivity.luncher(HnMainActivity.this, getString(R.string.my_sign_in), HnUrl.USER_SIGNIN_DETAIL, HnWebActivity.Sign);
                                    }

                                    @Override
                                    public void dismissLis() {
                                    }
                                });
                            }
                            mPopWindow.showUp(mIbMine);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {

    }

    private void setVersion(HnConfigModel.DBean mDBean, String response) {
        //保存全局配置信息
        if (true) {
            return;
        }
        if (mDBean != null) {
            if (!TextUtils.isEmpty(response))
                HnPrefUtils.setString(HnConstants.Setting.USER_CONFIG_MSG, response);
            HnBaseApplication.setmConfig(mDBean);
            //版本更新
            if (mDBean.getVersion() != null) {
                String version = mDBean.getVersion().getCode();
                String downloadUrl = mDBean.getVersion().getDownload_url();
                int currentCode = HnUtils.getVersionCode(HnMainActivity.this);
                if (!TextUtils.isEmpty(version)) {
                    if (currentCode < Integer.valueOf(version)) {
                        HnUpGradeDialog mHnUpGradeDialog = HnUpGradeDialog.newInstance(downloadUrl, true,
                                mDBean.getVersion().getIs_force(), mDBean.getVersion().getContent());
                        mHnUpGradeDialog.show(getFragmentManager(), "HnUpGradeDialog");
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (mShareFragment != null) {
//            mShareFragment.onActivityResult(requestCode, resultCode, data);
//        }
    }
}
