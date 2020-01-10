package com.hotniao.video.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.manager.HnAppManager;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnEditText;
import com.hotniao.video.R;
import com.hotniao.video.model.HnMusicDownedModel;
import com.hotniao.video.model.HnMusicSearchModel;
import com.hotniao.video.utils.HnMusicUtil;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.widget.SwipeMenuLayout;
import com.hotniao.livelibrary.ui.beauty.utils.VideoMaterialDownloadManager;
import com.hotniao.livelibrary.ui.beauty.utils.VideoMaterialDownloadProgress;
import com.videolibrary.eventbus.HnSelectMusicEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：音乐
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

@Route(path = "/music/musicLoclActivity")
public class HnMusicLoclActivity extends BaseActivity {
    @BindView(R.id.mEtSearch)
    HnEditText mEtSearch;
    @BindView(R.id.mTvUsed)
    TextView mTvUsed;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;

    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mLoadingLayout;


    public MediaPlayer mMediaPlayer;

    private List<HnMusicDownedModel> mData = new ArrayList<>();
    private CommRecyclerAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_down_music;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(false);
        setShowTitleBar(false);
        mEtSearch.setFocusable(false);
        mEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(HnMusicSearchActivity.class);
            }
        });
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setMode(PtrFrameLayout.Mode.NONE);
    }

    @Override
    public void getInitData() {


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(final BaseViewHolder holder, final int position) {
                final HnMusicDownedModel info = mData.get(position);
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(info.getCover()));
                ((TextView) holder.getView(R.id.mTvSing)).setText(info.getSingName());
                ((TextView) holder.getView(R.id.mTvSinger)).setText(info.getSinger());

                ((TextView) holder.getView(R.id.mTvTime)).setText(HnDateUtils.getMinute(info.getTime()));

                holder.getView(R.id.mLlContent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopMusic();
                        ((SwipeMenuLayout) holder.getView(R.id.mSwiMLay)).smoothExpand();
                        startMusic(info.getLocalPath());
                    }
                });
                holder.getView(R.id.mTvUse).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(mData.get(position).getLocalPath());
                        if (!file.exists()) {
                            CommDialog.newInstance(HnMusicLoclActivity.this).setCanceledOnOutside(true).setClickListen(new CommDialog.TwoSelDialog() {
                                @Override
                                public void leftClick() {

                                }

                                @Override
                                public void rightClick() {
                                    HnMusicSearchActivity.luncher(HnMusicLoclActivity.this, info.getSingName());
                                }
                            }).setTitle("音乐").setContent("该文件不存在,是否重新下载？").show();
                        } else {
                            EventBus.getDefault().post(new HnSelectMusicEvent(info.getId(), info.getSingName(), info.getLocalPath()));
                            HnAppManager.getInstance().finishActivity(HnMusicLoclActivity.class);
                        }
                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_music_serch_down;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);


    }


    /**
     * 播放音乐
     */
    private void startMusic(String url) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mMediaPlayer != null)
                        mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止音乐
     */
    public void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData.clear();
        mData.addAll(HnMusicUtil.stringToList(HnPrefUtils.getString(HnConstants.MUSIC_DATD, "")));
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        setEmpty(HnUiUtils.getString(R.string.now_no_dowm_music), R.drawable.empty_com);
    }

    @OnClick(R.id.mTvCancel)
    public void onClick() {
        finish();
    }

    protected void setEmpty(String content, int res) {
        if (isFinishing()) return;
        if (mAdapter == null || mLoadingLayout == null) return;
        if (mAdapter.getItemCount() < 1) {
            mLoadingLayout.setEmptyText(content);
            mLoadingLayout.setEmptyImage(res);
            setLoadViewState(HnLoadingLayout.Empty, mLoadingLayout);

        } else {
            setLoadViewState(HnLoadingLayout.Success, mLoadingLayout);
        }
    }
}
