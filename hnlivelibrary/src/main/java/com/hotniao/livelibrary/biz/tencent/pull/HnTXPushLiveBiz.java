package com.hotniao.livelibrary.biz.tencent.pull;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.hn.library.utils.HnLogUtils;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.text.SimpleDateFormat;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：腾讯推流api操作类
 * 创建人：mj
 * 创建时间：2017/10/11 12:45
 * 修改人：Administrator
 * 修改时间：2017/10/11 12:45
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTXPushLiveBiz {


    private String TAG = "HnTXPushLiveBiz";
    protected StringBuffer mLogMsg = new StringBuffer("");
    private final int mLogMsgLenLimit = 3000;


    /**
     * 单例
     */
    private HnTXPushLiveBiz() {

    }


    public static class HnTXPushLiveBizHolder {
        private static HnTXPushLiveBiz instance = new HnTXPushLiveBiz();
    }

    public static HnTXPushLiveBiz getInstance() {
        return HnTXPushLiveBiz.HnTXPushLiveBizHolder.instance;
    }


    /**
     * 设置推流配置  若想使用自定义配置，请传入config对象。
     *
     * @param mLivePusher 视频推流
     * @param config      视频参数配置
     * @param platform    云平台
     */
    public TXLivePushConfig setTXLivePushConfig(TXLivePusher mLivePusher, TXLivePushConfig config, String platform, Bitmap bitmap) {
        if (mLivePusher != null) {
            //默认设置视频质量
            mLivePusher.setVideoQuality(VIDEO_QUALITY_HIGH_DEFINITION, true, true);
            if (config != null) {//自定义配置
                mLivePusher.setConfig(config);
            } else {//内置参数配置
                config = new TXLivePushConfig();
                //在开始推流前，使用 TXLivePushConfig 的 setPauseImg 接口设置一张等待图片，图片含义推荐为“主播暂时离开一下下，稍后回来”
//                mLivePushConfig.setPauseImg();
                //SDK 不绑定腾讯云，如果要推流到非腾讯云地址，请在推流前设置 TXLivePushConfig 中的 enableNearestIP 设置为 NO。
                // 但如果您要推流的地址为腾讯云地址，请务必在推流前将其设置为 YES，否则推流质量可能会因为运营商 DNS 不准确而受到影响。
                if ("TX".equals(platform)) {
                    config.enableNearestIP(true);
                } else {
                    config.enableNearestIP(false);
                }
//                if(bitmap!=null){
//                    config.setPauseImg(300,5);
//                    config.setPauseImg(bitmap);
//                }
                //设置为自动对焦
                config.setTouchFocus(false);
                //设置屏幕旋转方向
                config.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
                //设置推流端重连间隔 秒
                config.setConnectRetryInterval(10);
                config.setConnectRetryCount(5);
                // 停止视频采集则会推送pauseImg设置的默认图，停止音频采集则会推送静音数据
                config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO);

                /**美颜调节面板*/
                config.setBeautyFilter(8, 7, 4);
                config.setFaceSlimLevel(0);
                config.setEyeScaleLevel(0);

                mLivePusher.setConfig(config);
            }
        }
        return config;
    }

    /**
     * 自定义设定清晰度
     *
     * @param mLivePusher      视频推流对象
     * @param qulity           SDK 提供了六种基础档位，根据我们服务大多数客户的经验进行积累和配置。其中 STANDARD、HIGH、SUPER 适用于直播模式，
     *                         MAIN_PUBLISHER 和 SUB_PUBLISHER 适用于连麦直播中的大小画面，VIDEOCHAT 用于实时音视频
     * @param adjustBitrate    是否开启 Qos 流量控制，开启后SDK 会根据主播上行网络的好坏自动调整视频码率。相应的代价就是，主播如果网络不好，
     *                         画面会很模糊且有很多马赛克。
     * @param adjustResolution 是否允许动态分辨率，开启后 SDK 会根据当前的视频码率选择相匹配的分辨率，这样能获得更好的清晰度。相应的代价就是，
     *                         动态分辨率的直播流所录制下来的文件，在很多播放器上会有兼容性问题。
     */
    public void setVideoQuality(TXLivePusher mLivePusher, int qulity, boolean adjustBitrate, boolean adjustResolution) {
        if (mLivePusher != null) {
            mLivePusher.setVideoQuality(qulity, adjustBitrate, adjustResolution);
        }
    }


    /**
     * 开始推流
     *
     * @param mLivePusher
     * @param pushUrl
     * @param mLivePushConfig
     */
    public void startPush(TXLivePusher mLivePusher, String pushUrl, TXLivePushConfig mLivePushConfig) {
        if (TextUtils.isEmpty(pushUrl)) {
            HnLogUtils.i(TAG, "推流地址为空,无法进行推流");
            return;
        }
        if (mLivePusher == null) {
            HnLogUtils.i(TAG, "无法进行直播推流，请先初始化。");
            return;
        }
        if (mLivePushConfig == null) {
            HnLogUtils.i(TAG, "未进行推流参数配置，请先进行参数配置");
            return;
        }
        //开始推流   告诉 SDK 音视频流要推到哪个推流URL上去
        mLivePusher.startPusher(pushUrl);

    }


    /**
     * 结束推流，注意做好清理工作
     */
    public void stopRtmpPublish(TXLivePusher mLivePusher) {
        if (mLivePusher == null) {
            mLivePusher.stopCameraPreview(true); //停止摄像头预览
            mLivePusher.stopPusher();            //停止推流
            mLivePusher.setPushListener(null);   //解绑 listener
        }
    }

    /**
     * 结束推流，注意做好清理工作
     *
     * @param mLivePusher 推流对象
     */
    public void stopPublishRtmp(TXLivePusher mLivePusher) {
        if (mLivePusher != null) {
            mLivePusher.stopPusher();
        }
    }

    /**
     * 结束推流，注意做好清理工作
     *
     * @param mLivePusher       推流对象
     * @param mLivePushConfig   推流配置
     * @param mTXCloudVideoView 推流显示控件
     */
    public void destoryPublishRtmp(TXLivePusher mLivePusher, TXLivePushConfig mLivePushConfig, TXCloudVideoView mTXCloudVideoView) {
        if (mLivePusher != null) {
            mLivePusher.stopBGM();
            mLivePusher.stopCameraPreview(true);
            mLivePusher.stopScreenCapture();
            mLivePusher.setPushListener(null);
            mLivePusher.stopPusher();
        }
        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
        }
    }


    /**
     * 切换摄像头 默认是前置
     *
     * @param isFont      是否是前置
     * @param mLivePusher 推流对象
     * @return
     */
    public boolean switchCamera(TXLivePusher mLivePusher, boolean isFont) {
        if (mLivePusher != null) {
            mLivePusher.switchCamera();
            isFont = !isFont;
        }
        HnLogUtils.i(TAG, "是否切换为前置摄像头" + isFont);
        return isFont;
    }

    /**
     * 设置美颜
     *
     * @param mLivePusher 推流对象
     * @param style       磨皮风格：  0：光滑  1：自然  2：朦胧
     *                    param    beautyLevel       磨皮等级： 取值为0-9.取值为0时代表关闭美颜效果.默认值:0,即关闭美颜效果.
     *                    param    whiteningLevel    美白等级： 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     *                    param    ruddyLevel        红润等级： 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     * @return
     */
    public boolean setBeautyFilter(TXLivePusher mLivePusher, int style, int beautyLevel, int whiteningLevel, int ruddyLevel) {
        return mLivePusher.setBeautyFilter(style, beautyLevel, whiteningLevel, ruddyLevel);
    }

    /**
     * 当见面走onStop时调用
     *
     * @param mLivePusher  推流对象
     * @param mCaptureView 推流显示控件
     */
    public void onStop(TXLivePusher mLivePusher, TXCloudVideoView mCaptureView) {
        if (mCaptureView != null) {
            mCaptureView.onPause();  // mCaptureView 是摄像头的图像渲染view
        }
        if (mLivePusher != null) {
            mLivePusher.pauseBGM();
            mLivePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
        }
    }

    public void onResume(TXLivePusher mLivePusher, TXCloudVideoView mCaptureView) {
        if (mCaptureView != null) {
            mCaptureView.onResume();  // mCaptureView 是摄像头的图像渲染view
        }
        if (mLivePusher != null) {
            mLivePusher.resumeBGM();
            mLivePusher.resumePusher();  // 通知 SDK 重回前台推流
        }
    }


    //公用打印辅助函数
    public void appendEventLog(int event, String message) {
        String str = "receive event: " + event + ", " + message;
        Log.d(TAG, str);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String date = sdf.format(System.currentTimeMillis());
        while (mLogMsg.length() > mLogMsgLenLimit) {
            int idx = mLogMsg.indexOf("\n");
            if (idx == 0)
                idx = 1;
            mLogMsg = mLogMsg.delete(0, idx);
        }
        mLogMsg = mLogMsg.append("\n" + "[" + date + "]" + message);
    }


    //公用打印辅助函数
    public String getNetStatusString(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-12s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT) + "|" + status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AVRA:" + status.getInt(TXLiveConstants.NET_STATUS_SET_VIDEO_BITRATE));
        return str;
    }

    public Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}
