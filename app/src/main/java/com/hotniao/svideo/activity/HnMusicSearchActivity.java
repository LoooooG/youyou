package com.hotniao.svideo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.manager.HnAppManager;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.R;
import com.hotniao.svideo.eventbus.HnSearchEvent;
import com.hotniao.svideo.fragment.HnPlatformListFragment;
import com.hotniao.svideo.model.HnMusicDownedModel;
import com.hotniao.svideo.model.HnMusicSearchModel;
import com.hotniao.svideo.utils.HnMusicUtil;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.svideo.widget.SwipeMenuLayout;
import com.hotniao.livelibrary.ui.beauty.utils.VideoMaterialDownloadManager;
import com.hotniao.livelibrary.ui.beauty.utils.VideoMaterialDownloadProgress;
import com.loopj.android.http.RequestParams;
import com.videolibrary.eventbus.HnSelectMusicEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

@SuppressLint("Registered")
public class HnMusicSearchActivity extends BaseActivity {
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

    private List<HnMusicDownedModel> mLocalData;
    private List<HnMusicSearchModel.DBean.ItemsBean> mData = new ArrayList<>();
    private CommRecyclerAdapter mAdapter;

    private String mKey;

    private int mPage = 1;
    private int mPageSize = 20;

    public MediaPlayer mMediaPlayer;


    public static void luncher(Activity activity, String key) {
        activity.startActivity(new Intent(activity, HnMusicSearchActivity.class).putExtra("key", key));
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_search_down_music;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(false);
        setShowTitleBar(false);

        mLocalData = HnMusicUtil.stringToList(HnPrefUtils.getString(HnConstants.MUSIC_DATD, ""));
        mKey = getIntent().getStringExtra("key");
        mTvUsed.setVisibility(View.GONE);
        initAdapter();
        setListener();
        if (!TextUtils.isEmpty(mKey)) {
            mEtSearch.setText(mKey);
            mPage = 1;
            searchMusic(mPage, mKey, HnRefreshDirection.TOP);
        }else {
            searchMusic(mPage, mKey, HnRefreshDirection.TOP);
        }
    }

    private void setListener() {

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        ((InputMethodManager) mEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                .hideSoftInputFromWindow(HnMusicSearchActivity.this.getCurrentFocus().getWindowToken(),
                                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                                        mKey = mEtSearch.getText().toString().trim();
                                                        if (TextUtils.isEmpty(mKey)) {
                                                            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.please_input_search_content));
                                                        } else {
                                                            mPage = 1;
                                                            searchMusic(mPage, mKey, HnRefreshDirection.TOP);

                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }

        );
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                mPage += 1;
                searchMusic(mPage, mKey, HnRefreshDirection.TOP);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mPage = 1;
                searchMusic(mPage, mKey, HnRefreshDirection.TOP);
            }
        });

    }

    @Override
    public void getInitData() {


    }

    public void initAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(final BaseViewHolder holder, final int position) {
                final HnMusicSearchModel.DBean.ItemsBean info = mData.get(position);
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(info.getCover()));
                ((TextView) holder.getView(R.id.mTvSing)).setText(info.getName());
                ((TextView) holder.getView(R.id.mTvSinger)).setText(info.getAuthor());

                ((TextView) holder.getView(R.id.mTvTime)).setText(HnDateUtils.getMinute(info.getDuration()));


                final TextView mTvProgress = holder.getView(R.id.mTvProgress);
                final TextView mTvUse = holder.getView(R.id.mTvUse);

                holder.getView(R.id.mLlContent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopMusic();
                        ((SwipeMenuLayout) holder.getView(R.id.mSwiMLay)).smoothExpand();
                        startMusic(info.getUrl());
                    }
                });
                mTvUse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = null;
                        if (!TextUtils.isEmpty(info.getLocalPath())) {
                            file = new File(info.getLocalPath());
                        }
                        if (TextUtils.isEmpty(info.getLocalPath()) || file == null || !file.exists()) {

                            if (!isNetworkAvailable(HnMusicSearchActivity.this)) {
                                HnToastUtils.showToastShort("请检查网络设置");
                                return;
                            }
                            final VideoMaterialDownloadProgress downloadProgress = VideoMaterialDownloadManager.getInstance().get(info.getId(), info.getUrl());
                            mTvUse.setVisibility(View.GONE);
                            mTvProgress.setVisibility(View.VISIBLE);
                            final VideoMaterialDownloadProgress.Downloadlistener listener = new VideoMaterialDownloadProgress.Downloadlistener() {
                                @Override
                                public void onDownloadFail(final String errorMsg) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HnToastUtils.showToastShort(errorMsg);
                                            mTvProgress.setVisibility(View.GONE);
                                            mTvUse.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onDownloadProgress(final int progress) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTvProgress.setText(progress + "%");
                                        }
                                    });
                                }

                                @Override
                                public void onDownloadSuccess(String filePath) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTvProgress.setVisibility(View.GONE);
                                            mTvUse.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    info.setLocalPath(filePath);
                                    if (mLocalData != null) {
                                        mLocalData.add(new HnMusicDownedModel(info.getId(), info.getName(), info.getAuthor(),
                                                info.getDuration(), info.getCover(), info.getLocalPath(), info.getUrl()));
                                        HnMusicUtil.listToString(mLocalData);
                                    }


                                }
                            };
                            downloadProgress.start(listener);


                        } else {
                            EventBus.getDefault().post(new HnSelectMusicEvent(info.getId(), info.getName(), info.getLocalPath()));
                            HnAppManager.getInstance().finishActivity(HnMusicLoclActivity.class);
                            HnAppManager.getInstance().finishActivity(HnMusicSearchActivity.class);
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

    @OnClick(R.id.mTvCancel)
    public void onClick() {
        finish();
    }


    private void setDataLocalPath() {
        for (int i = 0; i < mLocalData.size(); i++) {
            for (int j = 0; j < mData.size(); j++) {
                if (mLocalData.get(i).getId().equals(mData.get(j).getId()))
                    mData.get(j).setLocalPath(mLocalData.get(i).getLocalPath());
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }

    private void searchMusic(final int page, String key, final HnRefreshDirection state) {
        RequestParams params = new RequestParams();
        params.put("pagesize", mPageSize + "");
        params.put("page", page + "");
        params.put("search", key);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_MUSIC_LIST, params, "VIDEO_APP_MUSIC_LIST", new HnResponseHandler<HnMusicSearchModel>(HnMusicSearchModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD() == null || model.getD().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_search_music), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                setDataLocalPath();

                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_search_music), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mRefresh, mPage, mPageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_search_music), R.drawable.empty_com);
            }
        });
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

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }
}
