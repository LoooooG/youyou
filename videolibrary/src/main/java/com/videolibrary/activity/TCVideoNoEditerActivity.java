package com.videolibrary.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.manager.HnAppManager;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnDimenUtil;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.CommDialog;
import com.hn.library.view.HnEditText;
import com.hotniao.livelibrary.biz.HnLocationBiz;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.hotniao.livelibrary.model.HnLocationEntity;
import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoEditer;
import com.tencent.ugc.TXVideoInfoReader;
import com.videolibrary.R;
import com.videolibrary.eventbus.HnDeleteVideoFileEvent;
import com.videolibrary.eventbus.HnSelectMusicEvent;
import com.videolibrary.util.FileUtils;
import com.videolibrary.util.TCConstants;
import com.videolibrary.util.TCUtils;
import com.videolibrary.videoeditor.TCVideoEditView;
import com.videolibrary.view.VideoWorkProgressFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UGC短视频裁剪
 */
public class TCVideoNoEditerActivity extends BaseActivity implements View.OnClickListener, TCVideoEditView.IOnRangeChangeListener,
        TXVideoEditer.TXVideoGenerateListener, TXVideoInfoReader.OnSampleProgrocess, TXVideoEditer.TXVideoPreviewListener {

    private static final String TAG = TCVideoNoEditerActivity.class.getSimpleName();
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

    private int mPayType = 0;


    private HnEditText mEtTitle;
    private HnEditText mEtPayMoney;
    private TextView mTvNum;
    private TextView mTvCate;
    private ImageView mIvCate;
    private TextView mTvSave;
    private TextView mTvArea;
    private TextView mTvPayType;
    private TextView mTvMoneyName;

    private TextView mTvPublish;
    private ImageButton mBtnPlay;
    private FrameLayout mVideoView;
    private LinearLayout mLayoutEditer;
    private RelativeLayout mRlRight;

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

    private String mBGMPath;


    private boolean mIsStopManually;//标记是否手动停止
    private String mRecordProcessedPath;
    private String chooseVideoPath;

    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();

    public static final int Choose_Cate_Code = 10023;//选择分类
    public static final int Open_Location_Code = 10024;//开启定位权限
    public static final int Open_LocationSer_Code = 10025;//开启定位服务

    private MediaPlayer mMediaPlayer;
    private boolean isSave = true;
    private String mCateId, mCateName;

    private HnLocationEntity mLocationEntity;

    //定位信息
    private HnLocationBiz mHnLocationBiz;
    private boolean mClickLocation = false;
    private File outFileImage;

    class BackGroundHandler extends Handler {

        public BackGroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_VIDEO_INFO:
                    TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(chooseVideoPath);
                    if (videoInfo == null) {
                        mLoadProgress.setVisibility(View.GONE);
                        AlertDialog.Builder normalDialog = new AlertDialog.Builder(TCVideoNoEditerActivity.this, R.style.ConfirmDialogStyle);
                        normalDialog.setMessage("暂不支持Android 4.3以下的系统");
                        normalDialog.setCancelable(false);
                        normalDialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
                                if (!TextUtils.isEmpty(getIntent().getStringExtra("chooseUrl"))) {
                                    Intent intent = new Intent(TCVideoNoEditerActivity.this, HnChooseVideoEditerActivity.class);
                                    intent.putExtra(TCConstants.VIDEO_EDITER_PATH, getIntent().getStringExtra("chooseUrl"));
                                    startActivity(intent);
                                } else {
                                    if (mTXVideoInfoReader != null) mTXVideoInfoReader.cancel();
                                }
                                finish();
                            }
                        });
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
                    int ret = mTXVideoEditer.setVideoPath(chooseVideoPath);
                    mTXVideoEditer.initWithPreview(param);
                    if (ret < 0) {
                        AlertDialog.Builder normalDialog = new AlertDialog.Builder(TCVideoNoEditerActivity.this, R.style.ConfirmDialogStyle);
                        normalDialog.setMessage("本机型暂不支持此视频格式");
                        normalDialog.setCancelable(false);
                        normalDialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
                                if (!TextUtils.isEmpty(getIntent().getStringExtra("chooseUrl"))) {
                                    Intent intent = new Intent(TCVideoNoEditerActivity.this, HnChooseVideoEditerActivity.class);
                                    intent.putExtra(TCConstants.VIDEO_EDITER_PATH, getIntent().getStringExtra("chooseUrl"));
                                    startActivity(intent);
                                } else {
                                    if (mTXVideoInfoReader != null) mTXVideoInfoReader.cancel();
                                }
                                finish();
                            }
                        });
                        normalDialog.show();
                        return;
                    }

                    handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
                    mLoadProgress.setVisibility(View.GONE);
                    mTvPublish.setClickable(true);
                    mBtnPlay.setClickable(true);

//                    String duration = TCUtils.duration(mTXVideoInfo.duration);
//                    String position = TCUtils.duration(0);

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
        return R.layout.activity_video_editer;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initViews();
        initData();


        mBeautyDialogFragment = new BeautyDialogFragment("Video");
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, new BeautyDialogFragment.OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
                switch (key) {
                    case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                        if (mTXVideoEditer != null) {
                            mTXVideoEditer.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                        }
                        break;
                }
            }

            @Override
            public void dismiss() {
            }
        });
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        if (mHnLocationBiz == null)
            mHnLocationBiz = HnLocationBiz.getInsrance();
        mHnLocationBiz.startLocation(this);
        mHnLocationBiz.setOnLocationListener(new HnLocationBiz.OnLocationListener() {
            @Override
            public void onLocationSuccess(final String province, final String city, String address, String latitudeResult, String longitudeResult) {
                mLocationEntity = new HnLocationEntity(latitudeResult, longitudeResult, city, province);
                if (mTvArea == null) return;
                if (mClickLocation) {
                    CommDialog.newInstance(TCVideoNoEditerActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                        @Override
                        public void leftClick() {
                            mTvArea.setText("未知");
                        }

                        @Override
                        public void rightClick() {
                            mTvArea.setText(city);
                        }
                    }).setTitle(getString(R.string.location)).setContent(getString(R.string.location_you_here) + province + " " + city + getString(R.string.is_sure_user)).setRightText(getString(R.string.useed)).show();
                } else {
                    mTvArea.setText(city);
                }

            }

            @Override
            public void onLocationFail(String errorRease, int code) {
                if (mTvArea != null) mTvArea.setText("未知");
            }
        });
    }

    @Override
    public void getInitData() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        FileUtils.deleteFile(mRecordProcessedPath);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

        mHandlerThread.quit();
        handleOp(OP_CANCEL, 0, 0);
        try {
            mTXVideoEditer.stopPlay();
            if (TextUtils.isEmpty(getIntent().getStringExtra("chooseUrl")))
                mTXVideoInfoReader.cancel();

        } catch (Exception e) {
        }
        mTXVideoEditer.setTXVideoPreviewListener(null);
        mTXVideoEditer.setVideoGenerateListener(null);
        super.onDestroy();
    }


    private void initViews() {
        mEtTitle = findViewById(R.id.mEtTitle);
        mEtPayMoney = findViewById(R.id.mEtPayMoney);
        mTvPayType = findViewById(R.id.mTvPayType);
        mTvMoneyName = findViewById(R.id.mTvMoneyName);
        mTvMoneyName.setText(HnBaseApplication.getmConfig().getCoin());
        mTvNum = findViewById(R.id.mTvNum);
        mTvCate = findViewById(R.id.mTvCate);
        mIvCate = findViewById(R.id.mIvCate);
        mTvSave = findViewById(R.id.mTvSave);
        mTvArea = findViewById(R.id.mTvArea);
        mRlRight = findViewById(R.id.mRlRight);
        mRlRight.setVisibility(View.INVISIBLE);
        mTvSave.setOnClickListener(this);
        mTvArea.setOnClickListener(this);
        mTvPayType.setOnClickListener(this);
        mTvSave.setSelected(isSave);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);

        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);
        mBtnPlay.setClickable(false);

        mTvPublish = (TextView) findViewById(R.id.mTvPublish);
        mTvPublish.setOnClickListener(this);
        mTvPublish.setClickable(false);

        mLayoutEditer = findViewById(R.id.layout_editer);
        mLayoutEditer.setEnabled(true);
        mLayoutEditer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                HnUtils.hideSoftInputFrom(mEtPayMoney, TCVideoNoEditerActivity.this);
                HnUtils.hideSoftInputFrom(mEtTitle, TCVideoNoEditerActivity.this);
                return false;
            }
        });

        findViewById(R.id.mIvBack).setOnClickListener(this);
        findViewById(R.id.mIvFilter).setOnClickListener(this);
        findViewById(R.id.mIvMusic).setOnClickListener(this);
        findViewById(R.id.mLlCate).setOnClickListener(this);


        mLoadProgress = (ProgressBar) findViewById(R.id.progress_load);
        initWorkProgressPopWin();

//        initLocation();


        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trim = editable.toString().trim();
                if (TextUtils.isEmpty(trim)) mTvNum.setText("0字");
                else mTvNum.setText(trim.length() + "字");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mIvBack) {
//            mTXVideoInfoReader.cancel();
            handleOp(OP_CANCEL, 0, 0);
            mTXVideoEditer.setTXVideoPreviewListener(null);
            mTXVideoEditer.setVideoGenerateListener(null);
            HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
            if (!TextUtils.isEmpty(getIntent().getStringExtra("chooseUrl"))) {
                Intent intent = new Intent(this, HnChooseVideoEditerActivity.class);
                intent.putExtra(TCConstants.VIDEO_EDITER_PATH, getIntent().getStringExtra("chooseUrl"));
                startActivity(intent);
            } else {
                if (mTXVideoInfoReader != null) mTXVideoInfoReader.cancel();
            }
            finish();
        } else if (v.getId() == R.id.btn_play) {
            mIsStopManually = !mIsStopManually;
            playVideo();
        } else if (v.getId() == R.id.mTvPublish) {
            if (TextUtils.isEmpty(mCateId)) {
                HnToastUtils.showToastShort("请选择视频分类");
                return;
            }
            String s = mEtPayMoney.getText().toString().trim();
            if (mPayType != 0 && TextUtils.isEmpty(s)) {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.input_look_pay_money));
                return;
            }

            mPublish = true;
            doSave();
        } else if (v.getId() == R.id.mIvFilter) {
            try {
                if (mBeautyDialogFragment.isAdded()) {
                    mBeautyDialogFragment.dismiss();
                } else {
                    mBeautyDialogFragment.show(getFragmentManager(), "beauty");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.mIvMusic) {
            ARouter.getInstance().build("/music/musicLoclActivity").navigation();
        } else if (v.getId() == R.id.mLlCate) {
            Bundle bundle = new Bundle();
            bundle.putString("cateId", mCateId);
            bundle.putString("name", mCateName);
            ARouter.getInstance().build("/app/chooseVideoTypeActivity").with(bundle).navigation(this, Choose_Cate_Code);
        } else if (v.getId() == R.id.mTvSave) {
            if (isSave) {
                isSave = false;
            } else {
                isSave = true;
            }
            mTvSave.setSelected(isSave);
        } else if (v.getId() == R.id.mTvArea) {
            final int mLoctType = PermissionHelper.isLocationEnabled(TCVideoNoEditerActivity.this);
            if (-1 != mLoctType) {
                CommDialog.newInstance(TCVideoNoEditerActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        if (PermissionHelper.Open_Location == mLoctType) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, Open_LocationSer_Code); // 设置完成后返回到原来的界面
                        } else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, Open_Location_Code); // 设置完成后返回到原来的界面
                        }
                    }
                }).setTitle(getString(R.string.need_find_you_loction))
                        .setContent(PermissionHelper.Open_Location == mLoctType ? getString(R.string.allow_firebird_to_visit_location) : getString(R.string.open_the_location_service_to_allow_the_fire_bird_to_visit_the_location))
                        .setRightText(getString(R.string.set)).show();
            } else {
                mClickLocation = true;
                if (mHnLocationBiz != null)
                    mHnLocationBiz.startLocation(TCVideoNoEditerActivity.this);
            }
        } else if (v.getId() == R.id.mTvPayType) {
            showPayTypeDialog(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Choose_Cate_Code) {
            if (data != null && !TextUtils.isEmpty(data.getStringExtra("id"))) {
                mCateId = data.getStringExtra("id");
                mCateName = data.getStringExtra("name");
                mIvCate.setVisibility(View.GONE);
                mTvCate.setText(mCateName);
            }
        } else if (Open_LocationSer_Code == requestCode || Open_Location_Code == requestCode) {
            initLocation();
        }
    }


    private void initWorkProgressPopWin() {
        if (mWorkProgressDialog == null) {
            mWorkProgressDialog = new VideoWorkProgressFragment();
            mWorkProgressDialog.setOnClickStopListener(new VideoWorkProgressFragment.DismissListener() {
                @Override
                public void dismissListener() {
                    if (isFinishing()) return;
                    mTvPublish.setClickable(true);
                    mTvPublish.setEnabled(true);
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
        chooseVideoPath = getIntent().getStringExtra(TCConstants.CHOOSE_VIDEO_PATH);

        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoEditer.setTXVideoPreviewListener(this);
        //加载视频基本信息
        mHandler.sendEmptyMessage(MSG_LOAD_VIDEO_INFO);


        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //加载缩略图
//        mTXVideoInfoReader.getSampleImages(TCConstants.THUMB_COUNT, mRecordProcessedPath, this);

//        mWaterMarkLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_yuan);
    }

    private void createThumbFile() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                File outputVideo = new File(mVideoOutputPath);
                if (outputVideo == null || !outputVideo.exists())
                    return null;
                Bitmap bitmap = mTXVideoInfoReader.getSampleImage(0, mVideoOutputPath);
                if (bitmap == null)
                    return null;
                String mediaFileName = outputVideo.getAbsolutePath();
                if (mediaFileName.lastIndexOf(".") != -1) {
                    mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."));
                }
                String folder = mediaFileName;
                File appDir = new File(folder);
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }

                String fileName = "thumbnail" + ".jpg";
                File file = new File(appDir, fileName);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mTCVideoFileInfo.setThumbPath(file.getAbsolutePath());
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                publishVideo();
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
            if (mTXVideoInfo != null) {
                float volume = 50 / (float) 100;
                mTXVideoEditer.setBGMVolume(volume);
                mTXVideoEditer.setVideoVolume(1 - volume);
                if (!TextUtils.isEmpty(mBGMPath)) {
                    int result = mTXVideoEditer.setBGM(mBGMPath);
                    if (result != 0) {
                        mTXVideoEditer.setBGM(null); //不设置BGM
                        HnToastUtils.showToastShort("视频编辑失败,背景音仅支持MP3格式或M4A音频");
                    } else {
                        mTXVideoEditer.setBGMStartTime(0, getMusicDuration(mBGMPath));
                    }
                } else {
                    mTXVideoEditer.setBGM(null); //不设置BGM
                }
                handleOp(OP_PLAY, 0, (int) mTXVideoInfo.duration);
            }
            mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
        }
    }


    /**
     * 播放音乐
     */
    private int getMusicDuration(String url) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            int dura = mMediaPlayer.getDuration();
            return dura;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCurrentState == STATE_NONE) {//说明是取消合成之后
            if (mTXVideoInfo != null)
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
        mTvPublish.setClickable(true);
        mTvPublish.setEnabled(true);
    }


    private void publishVideo() {
        String fileName = HnDateUtils.getCurrentDate("yyyyMMdd").toUpperCase() + EncryptUtils.encryptMD5ToString(HnUtils.createRandom(false, 5)) + ".png";
        outFileImage = HnPhotoUtils.bitmapToFile(HnPhotoUtils.getVideoBg(chooseVideoPath), fileName);
        HnUpLoadPhotoControl.getTenSign(outFileImage, HnUpLoadPhotoControl.UploadImage, HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", key);
                bundle.putString("videoPath", chooseVideoPath);
                bundle.putString("title", mEtTitle.getText().toString().trim());
                bundle.putString("cateid", mCateId);
                bundle.putString("longTime", (mTXVideoInfo.duration / 1000) + "");
                bundle.putBoolean("isSave", isSave);
                bundle.putString("price", mEtPayMoney.getText().toString());
                bundle.putInt("payType", mPayType);
                if (mLocationEntity != null)
                    bundle.putParcelable("location", mLocationEntity);
                ARouter.getInstance().build("/app/videoPublishActivity").with(bundle).navigation();

            }

            @Override
            public void uploadProgress(int progress, int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                HnToastUtils.showToastShort("封面" + msg);
                if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
                    mWorkProgressDialog.dismiss();
                }
            }
        });


//        Bundle bundle = new Bundle();
//        bundle.putString(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
//        bundle.putString(TCConstants.VIDEO_RECORD_COVERPATH, getIntent().getStringExtra(TCConstants.VIDEO_RECORD_COVERPATH));
//        bundle.putString(TCConstants.VIDEO_RECORD_DURATION, mCutVideoDuration + "");
//        ARouter.getInstance().build("/video/videoPublishBeforeActivity").with(bundle).navigation();
    }

    private void publishVideoToServer() {
        String fileName = HnDateUtils.getCurrentDate("yyyyMMdd").toUpperCase() + EncryptUtils.encryptMD5ToString(HnUtils.createRandom(false, 5)) + ".png";
        outFileImage = HnPhotoUtils.bitmapToFile(HnPhotoUtils.getVideoBg(chooseVideoPath), fileName);
        HnUpLoadPhotoControl.upLoadPhoto(outFileImage, new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", key);
                bundle.putString("videoPath", chooseVideoPath);
                bundle.putString("title", mEtTitle.getText().toString().trim());
                bundle.putString("cateid", mCateId);
                bundle.putString("longTime", (mTXVideoInfo.duration / 1000) + "");
                bundle.putBoolean("isSave", isSave);
                bundle.putString("price", mEtPayMoney.getText().toString());
                bundle.putInt("payType", mPayType);
                if (mLocationEntity != null)
                    bundle.putParcelable("location", mLocationEntity);
                ARouter.getInstance().build("/app/videoPublishActivity").with(bundle).navigation();

            }

            @Override
            public void uploadProgress(int progress, int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                HnToastUtils.showToastShort("封面" + msg);
                if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
                    mWorkProgressDialog.dismiss();
                }
            }
        });
    }


    private void playVideo() {
        if (mCurrentState == STATE_RESUME) {
            handleOp(OP_PAUSE, 0, 0);
        } else {
            if (mTXVideoInfo != null)
                handleOp(OP_PLAY, 0, (int) mTXVideoInfo.duration);
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void doTranscode() {
        mTvPublish.setEnabled(false);
        mTvPublish.setClickable(false);

//        mTXVideoInfoReader.cancel();
        mLayoutEditer.setEnabled(false);
        handleOp(OP_CUT, 0, 0);
    }

    private void startTranscode() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        if (mTXVideoInfo != null)
            mCutVideoDuration = (int) mTXVideoInfo.duration;
        if (mWorkProgressDialog != null && !mWorkProgressDialog.isAdded()) {
            mWorkProgressDialog.setCancelable(false);
            mWorkProgressDialog.show(getFragmentManager(), "progress_dialog");
        }
        try {
            if (mTXVideoInfo != null)
                mTXVideoEditer.setCutFromTime(0, mTXVideoInfo.duration);

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
        mTvPublish.setEnabled(false);
        mTvPublish.setClickable(false);
//        mTXVideoInfoReader.cancel();
        mTXVideoEditer.stopPlay();
        mLayoutEditer.setEnabled(false);
//        doTranscode();
//        publishVideo();
        publishVideoToServer();
    }


    @Override
    public void onGenerateProgress(final float progress) {
        final int prog = (int) (progress * 100);
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult result) {
        if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
            updateMediaStore();
            if (mTXVideoInfo != null) {
                mResult = result;
            }
            if (mPublish) {
                createThumbFile();
                mPublish = false;
            } else {
                HnAppManager.getInstance().finishActivity(HnChooseVideoEditerActivity.class);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("chooseUrl"))) {
                    Intent intent = new Intent(this, HnChooseVideoEditerActivity.class);
                    intent.putExtra(TCConstants.VIDEO_EDITER_PATH, getIntent().getStringExtra("chooseUrl"));
                    startActivity(intent);
                } else {
                    if (mTXVideoInfoReader != null) mTXVideoInfoReader.cancel();
                }
                finish();
            }
        } else {
            TXVideoEditConstants.TXGenerateResult ret = result;
            Toast.makeText(TCVideoNoEditerActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
            mTvPublish.setEnabled(true);
            mTvPublish.setClickable(true);
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
    }


    @Override
    public void onPreviewProgress(final int time) {
    }

    @Override
    public void onPreviewFinished() {
        if (mTXVideoInfo != null)
            handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
    }

    @Override
    public void onKeyDown() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        handleOp(OP_PAUSE, 0, 0);
    }

    @Override
    public void onKeyUp(int startTime, int endTime) {
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        if (mTXVideoInfo != null)
            handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
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
                    mTvPublish.setClickable(true);
                    mTvPublish.setEnabled(true);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoEditer != null && mTXVideoInfo != null)
                        handleOp(OP_PLAY, 0, (int) mTXVideoInfo.duration);
                    break;
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectMusic(HnSelectMusicEvent event) {
        if (event != null) {
            mBGMPath = event.getPath();

        }

    }


    /**
     * 显示付费方式弹窗
     */

    private void showPayTypeDialog(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_live_pay_type, null);
        final PopupWindow popupWindow = new PopupWindow(view, HnDimenUtil.dp2px(this, 120), HnDimenUtil.dp2px(this, 107));
        final TextView mTvFree = (TextView) view.findViewById(R.id.mTvFree);
        final TextView mTvPay = (TextView) view.findViewById(R.id.mTvPay);
        if (mPayType != 0) {
            mTvFree.setSelected(false);
            mTvPay.setSelected(true);
        } else {
            mTvFree.setSelected(true);
            mTvPay.setSelected(false);
        }
        mTvFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//免费
                mPayType = 0;
                mTvPayType.setText(R.string.need_no_charge);
                mTvFree.setSelected(true);
                mTvPay.setSelected(false);
                popupWindow.dismiss();
                mRlRight.setVisibility(View.INVISIBLE);
            }
        });
        mTvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//收费
                mPayType = 1;
                mTvPayType.setText(R.string.need_charge);
                mTvFree.setSelected(false);
                mTvPay.setSelected(true);
                popupWindow.dismiss();
                mRlRight.setVisibility(View.VISIBLE);
            }
        });

        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
        popupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        popupWindow.setFocusable(true);
        backgroundAlpha(0.5f);

        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.showAsDropDown(v, 0, 20);    // 以触发弹出窗的view为基准，出现在view的正下方，弹出的pop_view左上角正对view的左下角  偏移量默认为0,0
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * popwindow消失监听
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteVideoFile(HnDeleteVideoFileEvent event) {
        if (!event.isSaveVideoFile()) {
            HnFileUtils.deleteFile(mVideoOutputPath);
        }
        HnFileUtils.deleteFile(new File(mRecordProcessedPath));
        HnFileUtils.deleteFile(new File(getIntent().getExtras().getString(TCConstants.VIDEO_RECORD_COVERPATH)));
        if (outFileImage != null && outFileImage.exists()) {
            HnFileUtils.deleteFile(outFileImage);
        }
    }
}
