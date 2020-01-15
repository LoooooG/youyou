package com.hotniao.svideo.fragment.userhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.utils.DateUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnMyPhotoAlbumActivity;
import com.hotniao.svideo.activity.HnPhotoPagerActivity;
import com.hotniao.svideo.activity.HnUserHomeActivity;
import com.hotniao.svideo.activity.HnUserHomeGiftActivity;
import com.hotniao.svideo.activity.HnUserPhotoAlbumActivity;
import com.hotniao.svideo.adapter.HnPhotoAdapter;
import com.hotniao.svideo.base.BaseScollFragment;
import com.hotniao.svideo.fragment.HnUserHomeFragment;
import com.hotniao.livelibrary.biz.HnUserDetailBiz;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：用户主页信息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserHomeInfoFragment extends BaseScollFragment implements BaseRequestStateListener {

    public static final String TAG = "HnUserHomeInfoFragment";
    @BindView(R.id.mRecyclerAblm)
    RecyclerView mRecyclerAblm;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.mTvNick)
    TextView mTvNick;
//    @BindView(R.id.mTvRegistTime)
//    TextView mTvRegistTime;
    @BindView(R.id.mTvAge)
    TextView mTvAge;
    @BindView(R.id.mTvArea)
    TextView mTvArea;
    @BindView(R.id.mTvStar)
    TextView mTvStar;
    @BindView(R.id.mTvLove)
    TextView mTvLove;
    @BindView(R.id.mTvJob)
    TextView mTvJob;
    @BindView(R.id.mTvFelling)
    TextView mTvFelling;
    @BindView(R.id.mTvSign)
    TextView mTvSign;
    @BindView(R.id.mTvGiftNum)
    TextView mTvGiftNum;
    @BindView(R.id.mRecyclerGift)
    RecyclerView mRecyclerGift;
    @BindView(R.id.mLlGift)
    LinearLayout mLlGift;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;


    private HnPhotoAdapter mPhotoAdapter;
    private HnPhotoAdapter mGiftAdapter;
    private List<String> mPhotoData = new ArrayList<>();
    private List<String> mGiftData = new ArrayList<>();


    private String mUid;
    private HnUserDetailBiz mDetailBiz;
    public HnUserInfoDetailBean mUserInfo;

    public static HnUserHomeInfoFragment getInstance(String uid) {
        HnUserHomeInfoFragment fragment = new HnUserHomeInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_home_info;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDetailBiz = new HnUserDetailBiz(mActivity);
        mDetailBiz.setBaseRequestStateListener(this);
        mUid = getArguments().getString("uid");
        initAdapter();


    }

    private void initAdapter() {
        LinearLayoutManager mPhotoManager = new LinearLayoutManager(mActivity);
        mPhotoManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerAblm.setLayoutManager(mPhotoManager);
        mPhotoAdapter = new HnPhotoAdapter(mPhotoData);
        mRecyclerAblm.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                HnUserPhotoAlbumActivity.luncher(mActivity, mUserInfo == null ? "" : mUserInfo.getUser_img());
                launcherGallery(view,position,getImageUrls());
            }
        });


        LinearLayoutManager mGiftManager = new LinearLayoutManager(mActivity);
        mGiftManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerGift.setLayoutManager(mGiftManager);
        mGiftAdapter = new HnPhotoAdapter(mGiftData);
        mRecyclerGift.setAdapter(mGiftAdapter);

        mGiftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HnUserHomeGiftActivity.luncher(mActivity, mUid);
            }
        });
    }

    private List<String> getImageUrls(){
        List<String> imageUrls = new ArrayList<>();
        if(mUserInfo != null){
            String imgs = mUserInfo.getUser_img();
            String[] imgArrs = imgs.split(",");
            imageUrls.addAll(Arrays.asList(imgArrs));
            return imageUrls;
        }
        return imageUrls;
    }

    public void launcherGallery(View view, int position, List<String> data) {
        Intent intent = new Intent(mActivity, HnPhotoPagerActivity.class);
        Bundle args = new Bundle();
        args.putInt(HnPhotoPagerActivity.KEY_START_POS, Math.min(data.size(), Math.max(0, position)));

        int[] rt = new int[2];
        view.getLocationOnScreen(rt);

        float w = view.getMeasuredWidth();
        float h = view.getMeasuredHeight();

        float x = rt[0] + w / 2;
        float y = rt[1] + h / 2;

        args.putInt(HnPhotoPagerActivity.KEY_START_X, rt[0]);
        args.putInt(HnPhotoPagerActivity.KEY_START_Y, rt[1]);
        args.putInt(HnPhotoPagerActivity.KEY_START_W, (int) w);
        args.putInt(HnPhotoPagerActivity.KEY_START_H, (int) h);
        args.putStringArrayList(HnPhotoPagerActivity.KEY_PHOTO_LIST, (ArrayList<String>) data);

        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        mDetailBiz.requestToUserDetail(mUid, mUid);
    }

    @Override
    public void pullToRefresh() {
        mDetailBiz.requestToUserDetail(mUid, mUid);
    }

    @Override
    public void refreshComplete() {
        if (mActivity == null) return;
        if (this.getActivity() instanceof HnUserHomeActivity) {
            ((HnUserHomeActivity) (this.getActivity())).refreshComplete();
        }else if(getParentFragment() instanceof HnUserHomeFragment){
            ((HnUserHomeFragment)getParentFragment()).refreshComplete();
        }
    }

    @Override
    public View getScrollableView() {
        return mScrollView;
    }


    @OnClick({R.id.mRlAlbm, R.id.mLlGift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRlAlbm:
//                HnUserPhotoAlbumActivity.luncher(mActivity, mUserInfo == null ? "" : mUserInfo.getUser_img());
                break;
            case R.id.mLlGift:
                HnUserHomeGiftActivity.luncher(mActivity, mUid);
                break;
        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) return;
        if (HnUserDetailBiz.UserInfoDetail.equals(type)) {
            HnUserInfoDetailModel model = (HnUserInfoDetailModel) obj;
            if (model != null && model.getD() != null) {
                mUserInfo = model.getD();
                EventBus.getDefault().post(mUserInfo);
                upDataMessage();
            }
            refreshComplete();
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnUserDetailBiz.UserInfoDetail.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
        refreshComplete();
    }

    private void upDataMessage() {
        if (!isAdded()) return;
        if (mUserInfo == null || mActivity == null) return;

        if (!TextUtils.isEmpty(mUserInfo.getUser_img())) {
            tvEmpty.setVisibility(View.GONE);
            mRecyclerAblm.setVisibility(View.VISIBLE);
            String[] images = mUserInfo.getUser_img().split(",");
            int len = images.length;
            mPhotoData.clear();
            for (int i = 0; i < len; i++) {
                mPhotoData.add(images[i]);
            }
            images = null;
        }else{
            tvEmpty.setVisibility(View.VISIBLE);
            mRecyclerAblm.setVisibility(View.GONE);
        }
        mPhotoAdapter.notifyDataSetChanged();

        mGiftData.clear();
        mGiftData.addAll(mUserInfo.getGift_img());
        if (mGiftData == null || mGiftData.size() < 1) {
            mTvGiftNum.setVisibility(View.GONE);
            mLlGift.setVisibility(View.GONE);
        } else {
            mTvGiftNum.setText(String.format(HnUiUtils.getString(R.string.recive_gift_num), "  " + mUserInfo.getTotal_gift()));
            mTvGiftNum.setVisibility(View.VISIBLE);
            mLlGift.setVisibility(View.VISIBLE);
            mGiftAdapter.notifyDataSetChanged();
        }


        mTvId.setText(mUserInfo.getUser_id());
        mTvNick.setText(mUserInfo.getUser_nickname());
//        mTvRegistTime.setText(HnDateUtils.stampToDateMm(mUserInfo.getUser_register_time()));

        mTvAge.setText(TextUtils.isEmpty(mUserInfo.getUser_birth()) ? "保密" : DateUtils.getAge(mUserInfo.getUser_birth()) + "");
        mTvArea.setText(TextUtils.isEmpty(mUserInfo.getUser_home_town()) ? "保密" : mUserInfo.getUser_home_town());
        mTvStar.setText(TextUtils.isEmpty(mUserInfo.getUser_constellation()) ? "保密" : mUserInfo.getUser_constellation());
        mTvLove.setText(TextUtils.isEmpty(mUserInfo.getUser_hobby()) ? "Ta好像忘记写爱好了" : mUserInfo.getUser_hobby());
        mTvJob.setText(TextUtils.isEmpty(mUserInfo.getUser_profession()) ? "Ta好像忘记写职业了" : mUserInfo.getUser_profession());
        mTvFelling.setText(TextUtils.isEmpty(mUserInfo.getUser_emotional_state()) ? "保密" : mUserInfo.getUser_emotional_state());

        String intro = mUserInfo.getUser_intro();
        if (TextUtils.isEmpty(intro)) intro = "     Ta还没有签名哦~";
        else intro = "     " + intro;
        //为TextView设置不同的字体大小和颜色
        String s = HnUiUtils.getString(R.string.user_sign_colon);
        try {
            SpannableString styledText = new SpannableString(s + intro);
            styledText.setSpan(new TextAppearanceSpan(mActivity, R.style.textSize16), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mActivity, R.style.textSize14), s.length(), s.length() + intro.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder msgbuilder = new SpannableStringBuilder(styledText);
            ForegroundColorSpan klicknickSpan = new ForegroundColorSpan(getResources().getColor(R.color.comm_text_color_black));
            ForegroundColorSpan klickcontentSpan = new ForegroundColorSpan(getResources().getColor(R.color.comm_text_color_black_s));
            msgbuilder.setSpan(klicknickSpan, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            msgbuilder.setSpan(klickcontentSpan, s.length(), s.length() + intro.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTvSign.setText(msgbuilder);
        } catch (Exception e) {
        }

    }
}
