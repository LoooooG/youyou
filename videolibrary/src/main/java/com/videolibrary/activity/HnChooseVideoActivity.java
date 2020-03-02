package com.videolibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.EventBusBean;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hn.library.view.HnSpacesItemGriddingDecoration;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.hotniao.livelibrary.model.HnUploadPhotoModel;
import com.loopj.android.http.RequestParams;
import com.tencent.liteav.basic.log.TXCLog;
import com.videolibrary.R;
import com.videolibrary.model.TCVideoFileInfo;
import com.videolibrary.util.HnGetAllVideoBiz;
import com.videolibrary.util.TCConstants;
import com.videolibrary.util.TCUtils;
import com.videolibrary.view.VideoWorkProgressFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：选择视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnChooseVideoActivity extends BaseActivity {

    public static final String SmallVideo = "SmallVideo";
    public static final String ChatVideo = "ChatVideo";
    public static final String PublishVideo = "PublishVideo";
    public static final int RequestCode = 10010;

    private RecyclerView mRecycler;
    private CommRecyclerAdapter mAdapter;

    private List<TCVideoFileInfo> mData = new ArrayList<>();
    private int mSelectPositon = -1;
    private HnLoadingLayout mLoadingLayout;


    private HnGetAllVideoBiz mVideoBiz;
    private HandlerThread mHandlerThread;
    private Handler mHandler;


    public static void luncher(Activity activity, String type) {
        activity.startActivityForResult(new Intent(activity, HnChooseVideoActivity.class).putExtra("type", type), RequestCode);
    }

    @SuppressLint("HandlerLeak")
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isFinishing()) return;
            if (mLoadingLayout != null)
                mLoadingLayout.setStatus(HnLoadingLayout.Success);
            ArrayList<TCVideoFileInfo> fileInfoArrayList = (ArrayList<TCVideoFileInfo>) msg.obj;
            mData.addAll(fileInfoArrayList);
            mAdapter.notifyDataSetChanged();
            setEmpty("没有找到视频哦~", R.drawable.empty_com);
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_video;
    }


    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.all_video_file);
        setShowBack(true);
        setShowSubTitle(true);
        mSubtitle.setText(R.string.sure);
        mSubtitle.setTextColor(getResources().getColor(R.color.comm_text_color_white));
        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectPositon == -1) {
                    HnToastUtils.showToastShort("请选择视频");
                    return;
                } else {
                    doSelect(mSelectPositon);
                }
            }
        });


        mVideoBiz = HnGetAllVideoBiz.getInstance(this);
        mHandlerThread = new HandlerThread("LoadList");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        initView();
        loadVideoList();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void initView() {
        mLoadingLayout = findViewById(R.id.mHnLoadingLayout);
        mLoadingLayout.setStatus(HnLoadingLayout.Loading);
        //错误重新加载
        mLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadingLayout.setStatus(HnLoadingLayout.Loading);
                loadVideoList();
            }
        });

        mRecycler = findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new HnSpacesItemGriddingDecoration().setDividerWith(4));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                final TCVideoFileInfo fileInfo = mData.get(position);

                final ImageView mIvSelect = holder.getView(R.id.mIvSelect);
                ((TextView) holder.getView(R.id.mTvTime)).setText(TCUtils.formattedTime(fileInfo.getDuration() / 1000));
                Glide.with(HnChooseVideoActivity.this).load(Uri.fromFile(new File(fileInfo.getFilePath()))).dontAnimate().into((ImageView) holder.getView(R.id.mIvImg));
                if (fileInfo.isSelected())
                    mIvSelect.setImageResource(R.drawable.xiangc_xuanz);
                else {
                    mIvSelect.setImageResource(R.drawable.xiangc);
                }
                holder.getView(R.id.mIvImg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectPositon != -1) {
                            mData.get(mSelectPositon).setSelected(false);
                        }
                        notifyItemChanged(mSelectPositon);
                        fileInfo.setSelected(true);
                        mSelectPositon = position;
                        notifyItemChanged(position);
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_choose_video;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void getInitData() {

    }


    private void loadVideoList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<TCVideoFileInfo> fileInfoArrayList = mVideoBiz.getAllVideo();
                    Message msg = new Message();
                    msg.obj = fileInfoArrayList;
                    mMainHandler.sendMessage(msg);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideoList();
        }
    }


    private void doSelect(int position) {
        TCVideoFileInfo fileInfo = mData.get(position);
        if (fileInfo == null) {
            showErrorDialog("选择的文件不存在");
            return;
        }
        if (isVideoDamaged(fileInfo)) {
            showErrorDialog("该视频文件已经损坏");
            return;
        }
        File file = new File(fileInfo.getFilePath());
        if (!file.exists()) {
            showErrorDialog("选择的文件不存在");
            return;
        }

        String type = getIntent().getStringExtra("type");
        if (SmallVideo.equals(type) || PublishVideo.equals(type)) {
            if (fileInfo.getDuration() < (5 * 1000)) {
                HnToastUtils.showToastShort("请选择大于5秒的视频");
                return;
            }
            if (fileInfo.getDuration() >(300 * 1000)) {
                HnToastUtils.showToastShort("不支持时长大于5分钟的视频");
                return;
            }
        }

        if (SmallVideo.equals(type)) {
            Intent intent = new Intent(this, HnChooseVideoEditerActivity.class);
            intent.putExtra(TCConstants.VIDEO_EDITER_PATH, fileInfo.getFilePath());
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, fileInfo.getFilePath());
            intent.putExtra(TCConstants.VIDEO_RECORD_DURATION, fileInfo.getDuration());
            startActivity(intent);
        } else if(PublishVideo.equals(type)){
            Intent intent = new Intent(this, TCVideoNoEditerActivity.class);
            intent.putExtra(TCConstants.CHOOSE_VIDEO_PATH, fileInfo.getFilePath());
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, fileInfo.getFilePath());
            startActivity(intent);
        } else {
            if (fileInfo.getDuration() > (30 * 1000)) {
                HnToastUtils.showToastShort("简介视频为小于或等于30秒的视频");
                return;
            }
//            Intent intent = new Intent();
//            intent.putExtra("path", fileInfo.getFilePath());
//            setResult(RequestCode, intent);

            EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.ChooseChatVideo, fileInfo.getFilePath()));
            finish();
        }


    }

    /**
     * 检测视频是否损坏
     *
     * @param info
     * @return
     */
    private boolean isVideoDamaged(TCVideoFileInfo info) {
        if (info.getDuration() == 0) {
            //数据库获取到的时间为0，使用Retriever再次确认是否损坏
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(info.getFilePath());
            } catch (Exception e) {
                return true;//无法正常打开，也是错误
            }
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (TextUtils.isEmpty(duration))
                return true;
            return Integer.valueOf(duration) == 0;
        }
        return false;
    }

    private boolean isVideoDamaged(List<TCVideoFileInfo> list) {
        for (TCVideoFileInfo info : list) {
            if (isVideoDamaged(info)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    protected void onDestroy() {
        mHandlerThread.getLooper().quit();
        mHandlerThread.quit();
        super.onDestroy();
    }

    private void showErrorDialog(String msg) {
        CommDialog.newInstance(this).setCanceledOnOutside(true).setClickListen(new CommDialog.OneSelDialog() {
            @Override
            public void sureClick() {

            }
        }).setTitle("选择视频").setContent(msg).show();
    }


    protected void setEmpty(String content, int res) {
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() < 1) {
            mLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mLoadingLayout.setEmptyText(content).setEmptyImage(res);
        } else {
            mLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

}
