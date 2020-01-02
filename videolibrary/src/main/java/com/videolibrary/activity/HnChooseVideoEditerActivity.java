package com.videolibrary.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnToastUtils;
import com.tencent.rtmp.TXLog;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoEditer;
import com.tencent.ugc.TXVideoInfoReader;
import com.videolibrary.R;
import com.videolibrary.eventbus.HnDeleteVideoFileEvent;
import com.videolibrary.util.FileUtils;
import com.videolibrary.util.TCConstants;
import com.videolibrary.util.TCUtils;
import com.videolibrary.videoeditor.EditPannel;
import com.videolibrary.videoeditor.TCVideoEditView;
import com.videolibrary.view.VideoWorkProgressFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UGC短视频裁剪
 */
public class HnChooseVideoEditerActivity extends BaseActivity implements View.OnClickListener, TCVideoEditView.IOnRangeChangeListener,
        TXVideoEditer.TXVideoGenerateListener, TXVideoInfoReader.OnSampleProgrocess, TXVideoEditer.TXVideoPreviewListener, EditPannel.IOnEditCmdListener {

    private static final String TAG = HnChooseVideoEditerActivity.class.getSimpleName();
    private final int STATE_NONE = 0;
    private final int STATE_RESUME = 1;
    private final int STATE_PAUSE = 2;
    private final int STATE_CUT = 3;

    private final int OP_PLAY = 0;
    private final int OP_PAUSE = 1;
    private final int OP_SEEK = 2;
    private final int OP_CUT = 3;
    private final int OP_CANCEL = 4;
    private int mCurrentState = STATE_NONE;

    private TextView mTvDone;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private ImageButton mBtnPlay;
    private FrameLayout mVideoView;
    private RelativeLayout mLayoutEditer;
    private EditPannel mEditPannel;

    private TXVideoEditer mTXVideoEditer;
    private TXVideoInfoReader mTXVideoInfoReader;

    private String mVideoOutputPath;
    private BackGroundHandler mHandler;
    private final int MSG_LOAD_VIDEO_INFO = 1000;
    private final int MSG_RET_VIDEO_INFO = 1001;
    private ProgressBar mLoadProgress;
    private TXVideoEditConstants.TXGenerateResult mResult;
    private int mCutVideoDuration;//裁剪的视频时长

    private boolean mPublish = false;

    //    private Bitmap mWaterMarkLogo;
    private VideoWorkProgressFragment mWorkProgressDialog;

    private boolean mIsStopManually;//标记是否手动停止
    private String mRecordProcessedPath;

    class BackGroundHandler extends Handler {

        public BackGroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_VIDEO_INFO:
                    TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(mRecordProcessedPath);
                    if (videoInfo == null) {
                        mLoadProgress.setVisibility(View.GONE);
                        AlertDialog.Builder normalDialog = new AlertDialog.Builder(HnChooseVideoEditerActivity.this, R.style.ConfirmDialogStyle);
                        normalDialog.setMessage("暂不支持Android 4.3以下的系统");
                        normalDialog.setCancelable(false);
                        normalDialog.setPositiveButton("知道了", null);
                        normalDialog.show();
                        return;
                    }
                    Message mainMsg = new Message();
                    mainMsg.what = MSG_RET_VIDEO_INFO;
                    mainMsg.obj = videoInfo;
                    mMainHandler.sendMessage(mainMsg);
                    break;
            }

        }
    }

    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    @SuppressLint("HandlerLeak")
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RET_VIDEO_INFO:
                    mTXVideoInfo = (TXVideoEditConstants.TXVideoInfo) msg.obj;

                    TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
                    param.videoView = mVideoView;
                    param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
                    int ret = mTXVideoEditer.setVideoPath(mRecordProcessedPath);
                    mTXVideoEditer.initWithPreview(param);
                    if (ret < 0) {
                        AlertDialog.Builder normalDialog = new AlertDialog.Builder(HnChooseVideoEditerActivity.this, R.style.ConfirmDialogStyle);
                        normalDialog.setMessage("本机型暂不支持此视频格式");
                        normalDialog.setCancelable(false);
                        normalDialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        normalDialog.show();
                        return;
                    }

                    handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
                    mLoadProgress.setVisibility(View.GONE);
                    mTvDone.setClickable(true);
                    mBtnPlay.setClickable(true);

                    mEditPannel.setMediaFileInfo(mTXVideoInfo);
                    String duration = TCUtils.duration(mTXVideoInfo.duration);
                    String position = TCUtils.duration(0);

                    mTvCurrent.setText(position);
                    mTvDuration.setText(duration);
                    //createThumbFile(mTXVideoInfo);
                    break;
            }
        }
    };
    private HandlerThread mHandlerThread;


    @Override
    public int getContentViewId() {
        Window window = getWindow();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        return R.layout.activity_choose_video_editer;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setShowTitleBar(false);
        initViews();
        initData();
    }

    @Override
    public void getInitData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteVideoFile(HnDeleteVideoFileEvent event) {
        HnFileUtils.deleteFile(mVideoOutputPath);
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteFile(mVideoOutputPath);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

        mHandlerThread.quit();
        handleOp(OP_CANCEL, 0, 0);
        try {
            mTXVideoEditer.stopPlay();
//            mTXVideoInfoReader.cancel();

        } catch (Exception e) {
        }
        mTXVideoEditer.setTXVideoPreviewListener(null);
        mTXVideoEditer.setVideoGenerateListener(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {

        mEditPannel = (EditPannel) findViewById(R.id.edit_pannel);
        mEditPannel.setRangeChangeListener(this);
        mEditPannel.setEditCmdListener(this);


        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);

        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);
        mBtnPlay.setClickable(false);

        mTvDone = (TextView) findViewById(R.id.btn_done);
        mTvDone.setOnClickListener(this);
        mTvDone.setClickable(false);

        mLayoutEditer = (RelativeLayout) findViewById(R.id.layout_editer);
        mLayoutEditer.setEnabled(true);

        findViewById(R.id.back_tv).setOnClickListener(this);
        mLoadProgress = (ProgressBar) findViewById(R.id.progress_load);
        initWorkProgressPopWin();
    }

    private void initWorkProgressPopWin() {
        if (mWorkProgressDialog == null) {
            mWorkProgressDialog = new VideoWorkProgressFragment();
            mWorkProgressDialog.setOnClickStopListener(new VideoWorkProgressFragment.DismissListener() {
                @Override
                public void dismissListener() {
                    if (isFinishing()) return;
                    mTvDone.setClickable(true);
                    mTvDone.setEnabled(true);
                    mCurrentState = STATE_NONE;
                    if (mTXVideoEditer != null) {
                        mTXVideoEditer.cancel();
                    }
                }
            });
            mWorkProgressDialog.setTitle("正在合成，请勿退出");
        }
    }

    private synchronized boolean handleOp(int state, int startPlayTime, int endPlayTime) {
        switch (state) {
            case OP_PLAY:
                if (mCurrentState == STATE_NONE) {
                    mCutVideoDuration = endPlayTime - startPlayTime;
                    mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                    mCurrentState = STATE_RESUME;
                    return true;
                } else if (mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.resumePlay();
                    mCurrentState = STATE_RESUME;
                    return true;
                }
                break;
            case OP_PAUSE:
                if (mCurrentState == STATE_RESUME) {
                    mTXVideoEditer.pausePlay();
                    mCurrentState = STATE_PAUSE;
                    return true;
                }
                break;
            case OP_SEEK:
                if (mCurrentState == STATE_CUT) {
                    return false;
                }
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                mCutVideoDuration = endPlayTime - startPlayTime;
                mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                mCurrentState = STATE_RESUME;
                return true;
            case OP_CUT:
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                startTranscode();
                mCurrentState = STATE_CUT;
                return true;
            case OP_CANCEL:
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                } else if (mCurrentState == STATE_CUT) {
                    mTXVideoEditer.cancel();
                }
                mCurrentState = STATE_NONE;
                return true;
        }
        return false;
    }

    private void initData() {
        mHandlerThread = new HandlerThread("LoadData");
        mHandlerThread.start();
        mHandler = new BackGroundHandler(mHandlerThread.getLooper());

        // 录制经过预处理的视频路径，在编辑后需要删掉录制源文件
        mRecordProcessedPath = getIntent().getStringExtra(TCConstants.VIDEO_EDITER_PATH);

        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoEditer.setTXVideoPreviewListener(this);

        //加载视频基本信息
        mHandler.sendEmptyMessage(MSG_LOAD_VIDEO_INFO);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //加载缩略图
        mTXVideoInfoReader.getSampleImages(TCConstants.THUMB_COUNT, mRecordProcessedPath, this);

//        mWaterMarkLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_yuan);
    }

    private void createThumbFile() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                File outputVideo = new File(mVideoOutputPath);
                if (outputVideo == null || !outputVideo.exists())
                    return null;
                return "aaaaaaaaaaaaa";
            }

            @Override
            protected void onPostExecute(String s) {
                publishVideo(s);
            }
        };
        task.execute();
    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentState == STATE_PAUSE && !mIsStopManually) {
            handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
            mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCurrentState == STATE_NONE) {//说明是取消合成之后
            handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
            mBtnPlay.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
            mWorkProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCurrentState == STATE_CUT) {
            handleOp(OP_CANCEL, 0, 0);
            if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
                mWorkProgressDialog.dismiss();
            }
        } else {
            mIsStopManually = false;
            handleOp(OP_PAUSE, 0, 0);
            mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
        }
        mTvDone.setClickable(true);
        mTvDone.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_tv) {
            mTXVideoInfoReader.cancel();
            handleOp(OP_CANCEL, 0, 0);
            mTXVideoEditer.setTXVideoPreviewListener(null);
            mTXVideoEditer.setVideoGenerateListener(null);
            finish();
        } else if (v.getId() == R.id.btn_play) {
            mIsStopManually = !mIsStopManually;
            playVideo();
        } else if (v.getId() == R.id.btn_done) {
            if (mCutVideoDuration > (60 * 1000)) {
                HnToastUtils.showToastShort("请选取小于或等于60秒的视频");
                return;
            }
            mPublish = true;
            doSave();
        }
    }

    private void publishVideo(String cover) {

//        Bundle bundle = new Bundle();
//        bundle.putString(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
//        bundle.putString(TCConstants.VIDEO_RECORD_COVERPATH, cover);
//        bundle.putString(TCConstants.VIDEO_RECORD_DURATION, mCutVideoDuration + "");
//        ARouter.getInstance().build("/video/videoPublishBeforeActivity").with(bundle).navigation();

        Intent intent = new Intent(this, TCVideoEditerActivity.class);
        intent.putExtra(TCConstants.VIDEO_EDITER_PATH, mVideoOutputPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, cover);
        intent.putExtra("chooseUrl", mRecordProcessedPath);
        startActivity(intent);
    }


    private void playVideo() {
        if (mCurrentState == STATE_RESUME) {
            handleOp(OP_PAUSE, 0, 0);
        } else {
            handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void doTranscode() {
        mTvDone.setEnabled(false);
        mTvDone.setClickable(false);

//        mTXVideoInfoReader.cancel();
        mLayoutEditer.setEnabled(false);
        handleOp(OP_CUT, 0, 0);
    }

    private void startTranscode() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        mCutVideoDuration = mEditPannel.getSegmentTo() - mEditPannel.getSegmentFrom();
        mWorkProgressDialog.setCancelable(false);
        if (!mWorkProgressDialog.isAdded() && !mWorkProgressDialog.isVisible()) {
            mWorkProgressDialog.show(getFragmentManager(), "progress_dialog");
        }
        try {
            mTXVideoEditer.setCutFromTime(mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());

            String outputPath = Environment.getExternalStorageDirectory() + File.separator + TCConstants.DEFAULT_MEDIA_PACK_FOLDER;
            File outputFolder = new File(outputPath);

            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String current = String.valueOf(System.currentTimeMillis() / 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String time = sdf.format(new Date(Long.valueOf(current + "000")));
            String saveFileName = String.format("TXVideo_%s.mp4", time);
            mVideoOutputPath = outputFolder + "/" + saveFileName;


            mTXVideoEditer.setVideoGenerateListener(this);
            mTXVideoEditer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_720P, mVideoOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doSave() {
        mTvDone.setEnabled(false);
        mTvDone.setClickable(false);

        mTXVideoEditer.stopPlay();
        mLayoutEditer.setEnabled(false);
        doTranscode();
    }


    @Override
    public void onGenerateProgress(final float progress) {
        final int prog = (int) (progress * 100);
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult result) {
        if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
//            updateMediaStore();
            if (mTXVideoInfo != null) {
                mResult = result;
            }
            if (mPublish) {
                createThumbFile();
                mPublish = false;
            } else {
                finish();
            }
        } else {
            TXVideoEditConstants.TXGenerateResult ret = result;
            Toast.makeText(HnChooseVideoEditerActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
            mTvDone.setEnabled(true);
            mTvDone.setClickable(true);
        }

        mCurrentState = STATE_NONE;
    }

    private void updateMediaStore() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(mVideoOutputPath)));
        sendBroadcast(scanIntent);
    }

    @Override
    public void sampleProcess(int number, Bitmap bitmap) {
        mEditPannel.addBitmap(number, bitmap);
        TXLog.d(TAG, "number = " + number + ",bmp = " + bitmap);
    }


    @Override
    public void onPreviewProgress(final int time) {
//        Log.d(TAG, "onPreviewProgress time : " + time);
        try {
            if (mTvCurrent != null) {
                mTvCurrent.setText(TCUtils.duration(time / 1000));
            }
        } catch (OutOfMemoryError e) {
        }

    }

    @Override
    public void onPreviewFinished() {
        TXLog.d(TAG, "---------------onPreviewFinished-----------------");
        handleOp(OP_SEEK, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
    }

    @Override
    public void onKeyDown() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        Log.d(TAG, "onKeyDown");
        handleOp(OP_PAUSE, 0, 0);
    }

    @Override
    public void onKeyUp(int startTime, int endTime) {
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        Log.d(TAG, "onKeyUp");
        handleOp(OP_SEEK, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
    }

    @Override
    public void onCmd(int cmd, EditPannel.EditParams params) {
        switch (cmd) {
            case EditPannel.CMD_SPEED:
//                mTXVideoEditer.setSpeed(params.mSpeedRate);
                break;
            case EditPannel.CMD_FILTER:
                mTXVideoEditer.setFilter(params.mFilterBmp);
                break;
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {

                case TelephonyManager.CALL_STATE_RINGING:   //电话等待接听
                case TelephonyManager.CALL_STATE_OFFHOOK:   //电话接听
                    if (mCurrentState == STATE_CUT) {
                        handleOp(OP_CANCEL, 0, 0);
                        if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
                            mWorkProgressDialog.dismiss();
                        }
                        mBtnPlay.setImageResource(R.drawable.ic_pause);
                    } else {
                        handleOp(OP_PAUSE, 0, 0);
                        mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
                    }
                    mTvDone.setClickable(true);
                    mTvDone.setEnabled(true);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoEditer != null)
                        handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
                    break;
            }
        }
    };

}
