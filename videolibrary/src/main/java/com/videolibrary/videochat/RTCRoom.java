package com.videolibrary.videochat;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.hn.library.HnBaseApplication;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.model.HnReceiveVideoChatBean;
import com.imlibrary.TCChatRoomMgr;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.videolibrary.videochat.entity.PusherInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jac on 2017/11/4.
 * Copyright © 2013-2017 Tencent Cloud. All Rights Reserved.
 */
public class RTCRoom {
    private static final String TAG = "RTCRoom";

    public static int RTCROOM_VIDEO_RATIO_9_16 = 1; //视频分辨率为9:16

    private TCChatRoomMgr mTcChatRoomMgr;
    private Context mContext;
    private Handler mHandler;

    private TXLivePusher mTXLivePusher;
    private TXLivePushListenerImpl mTXLivePushListener;

    private HashMap<String, PlayerItem> mPlayers = new LinkedHashMap<>();
    private RoomManager mRoomManager = new RoomManager();

    private RoomListenerCallback roomListenerCallback;

    /**
     * 电话监听器
     */
    private TXPhoneStateListener mPhoneListener;

    /**
     * RealTime ChatRoom 实时音视频通话房间
     */

    public RTCRoom(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        roomListenerCallback = new RoomListenerCallback(null);
        init();

    }

    /**
     * 初始化RTCRoom 上下文
     */
    public void init() {

        if (HnBaseApplication.getmUserBean() == null) {
            mRoomManager.setState(RoomState.Absent);
            mRoomManager.setSelfUserID(HnPrefUtils.getString(NetConstant.User.UID, "00000123"));
            mRoomManager.setSelfUserName("none");
            mRoomManager.setAvatarUrl("none");
            mTcChatRoomMgr = TCChatRoomMgr.getInstance(HnPrefUtils.getString(NetConstant.User.UID, "00000123"));
        } else {
            mRoomManager.setState(RoomState.Absent);
            mRoomManager.setSelfUserID(HnBaseApplication.getmUserBean().getUser_id());
            mRoomManager.setSelfUserName(HnBaseApplication.getmUserBean().getUser_nickname());
            mRoomManager.setAvatarUrl(HnBaseApplication.getmUserBean().getUser_avatar());

            mTcChatRoomMgr = TCChatRoomMgr.getInstance(HnBaseApplication.getmUserBean().getUser_id());
        }
        mTcChatRoomMgr.setMessageListener(new TCChatRoomMgr.TCChatRoomListener() {
            @Override
            public void onJoinGroupCallback(int code, final String roomID) {


            }

            @Override
            public void onSendMsgCallback(int code, TIMMessage timMessage) {

            }

            @Override
            public void onReceiveMsg(int type, String content) {
                roomListenerCallback.onRecvRoomTextMsg(content);
            }

            @Override
            public void onGroupDelete(String groupId) {
                HnLogUtils.e("");
                if ("000000".equalsIgnoreCase(groupId)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRoomManager.updatePushers(roomListenerCallback);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRoomManager.setState(RoomState.Absent);
                            roomListenerCallback.onRoomClosed(mRoomManager.getRoomId());
                        }
                    });
                }

            }
        });
    }


    /**
     * RTCRoom 创建房间Callback
     */
    public interface CreateRoomCallback {
        void onError(int errCode, String errInfo);

        void onSuccess(String name);
    }

    /**
     * 创建房间，由会议创建者调用
     *
     * @param roomName 房间名
     * @param cb       房间创建完成的回调，里面会携带roomID
     */
    public void createRoom(String roomName, final String userId, String myid, String pushUrl,
                           final CreateRoomCallback cb) {

        mRoomManager.setRoomName(roomName);
        mRoomManager.setSelfUserID(myid);
        //1. 在应用层调用startLocalPreview，启动本地预览
        //2. 请求CGI:get_push_url，异步获取到推流地址pushUrl
        //3.开始推流
        startPushStream(pushUrl, new PusherStreamCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                cb.onError(errCode, errInfo);
            }

            @Override
            public void onSuccess() {
                //推流过程中，可能会重复收到PUSH_EVT_PUSH_BEGIN事件，onSuccess可能会被回调多次，如果已经创建的房间，直接返回
                String roomID = mRoomManager.getRoomId();
                if (roomID != null && roomID.length() > 0) {
                    HnLogUtils.d(TAG,"onSuccess");
                    return;
                }
                cb.onSuccess("");
            }
        });

    }

    /**
     * 主播接受视频
     *
     * @param roomId
     * @param bean
     * @param cb
     */
    public void joinVideoChatRoom(final String roomId, final HnReceiveVideoChatBean bean, final CreateRoomCallback cb) {
        mRoomManager.setRoomId(roomId);
        mRoomManager.setState(RoomState.Created);
        if (TextUtils.isEmpty(bean.getData().getPlay_rtmp())) {
            cb.onError(-1, "通话地址错误");
            return;
        }
        //5.调用IM的joinGroup，参数是roomId（roomId就是groupId）
        jionGroup(roomId, new JoinGroupCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                cb.onError(errCode, errInfo);
            }

            @Override
            public void onSuccess() {
                mRoomManager.setState(RoomState.Jioned);
                setPullUrl(roomId, bean.getData().getPlay_rtmp());
                cb.onSuccess(roomId);
            }
        });
    }

    private void setPullUrl(final String roomID, String pushUrl) {
        //3. 请求CGI:get_pushers，异步获取房间里所有正在推流的成员

        //4. 调用listener.onGetPusherList，把房间成员通知出去（应用层代码在收到这个通知后，调用addRemoteView播放每一个成员的流）
        List<PusherInfo> pusherList = new ArrayList<>();
        PusherInfo info = new PusherInfo();
        info.userID = roomID;
        info.accelerateURL = pushUrl;
        pusherList.add(info);

        mRoomManager.mergerPushers(pusherList, null, null);

        roomListenerCallback.onPusherJoin(info);
        /**
         * 房间成员更新
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRoomManager.updatePushers(roomListenerCallback);
            }
        });
    }


    /**
     * 设置房间事件回调
     *
     * @param listener
     */
    public void setRTCRoomListener(IRTCRoomListener listener) {
        roomListenerCallback.setRoomMemberEventListener(listener);
    }

    /**
     * RTCRoom 进入房间Callback
     */
    public interface EnterRoomCallback {
        void onError(int errCode, String errInfo);

        void onSuccess();
    }

    /**
     * 进入房间，由会议参与者调用
     *
     * @param cb 进入房间完成的回调
     */
    public void enterRoom(@NonNull final HnReceiveVideoChatBean.DataBean roomInfo, final String pushUrl,
                          final EnterRoomCallback cb) {
        mRoomManager.setRoomId(roomInfo.getF_user_id());
        //1. 应用层调用startLocalPreview，启动本地预览

        //2. 调用IM的joinGroup

        //5. 请求CGI:get_push_url，异步获取到推流地址pushUrl room_1513661582462_157173
        //6. 开始推流
        HnLogUtils.d(TAG,"enterRoom");
        startPushStream(pushUrl, new PusherStreamCallback() {
            @Override
            public void onError(int code, String info) {
                HnLogUtils.d(TAG,"error");
                cb.onError(code, info);
            }

            @Override
            public void onSuccess() {
                //7. 推流成功，请求CGI:add_pusher，把自己加入房间成员列表
                HnLogUtils.d(TAG,"onSuccess");
                mRoomManager.setState(mRoomManager.getState() | RoomState.Jioned);
                setPullUrl(roomInfo.getF_user_id(), roomInfo.getPlay_rtmp());
                cb.onSuccess();
            }
        });
    }

    /**
     * RTCRoom 离开房间Callback
     */
    public interface ExitRoomCallback {
        void onError(int errCode, String errInfo);

        void onSuccess();
    }

    /**
     * 离开房间
     *
     * @param callback 离开房间完成的回调
     */
    public void exitRoom(final ExitRoomCallback callback) {

        //1. 应用层结束播放所有的流

        //3. 结束本地推流
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopLocalPreview();
            }
        });

        //4. 关闭所有播放器，清理房间信息
        for (Map.Entry<String, PlayerItem> entry : mPlayers.entrySet()) {
            entry.getValue().destroy();
        }
        mPlayers.clear();

        //5. 调用IM的quitGroup
        mTcChatRoomMgr.quitGroup(mRoomManager.getRoomId());


        //清理roomManager
        mRoomManager.clean();
    }

    /**
     * 启动摄像头预览
     *
     * @param videoView 摄像头预览组件
     */
    public synchronized void startLocalPreview(final @NonNull TXCloudVideoView videoView) {

        initLivePusher();

        if (mTXLivePusher != null) {
            videoView.setVisibility(View.VISIBLE);
            mTXLivePusher.startCameraPreview(videoView);
        }
    }

    /**
     * 停止摄像头预览
     */
    public synchronized void stopLocalPreview() {
        if (mTXLivePusher != null) {
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopCameraPreview(true);
            mTXLivePusher.stopPusher();
        }

        unInitLivePusher();
    }

    /**
     * 启动远端视频
     *
     * @param videoView  视频预览组件
     * @param pusherInfo 对应视频的成员
     */
    public void addRemoteView(final @NonNull TXCloudVideoView videoView, final @NonNull PusherInfo pusherInfo) {

        synchronized (this) {

            if (mPlayers.containsKey(pusherInfo.userID)) {
                PlayerItem pusherPlayer = mPlayers.get(pusherInfo.userID);
                if (pusherPlayer.player.isPlaying()) {
                    return;
                } else {
                    pusherPlayer = mPlayers.remove(pusherInfo.userID);
                    pusherPlayer.destroy();
                }
            }

            final TXLivePlayer player = new TXLivePlayer(mContext);
            TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
            //极速模式
            mPlayConfig.setAutoAdjustCacheTime(true);
            mPlayConfig.setMinAutoAdjustCacheTime(1);
            mPlayConfig.setMaxAutoAdjustCacheTime(1);
            //设置播放器重连间隔. 秒
            mPlayConfig.setConnectRetryInterval(10);
            mPlayConfig.setConnectRetryCount(5);
            player.setConfig(mPlayConfig);


            videoView.setVisibility(View.VISIBLE);
            player.setPlayerView(videoView);
            player.enableHardwareDecode(true);
            player.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            PlayerItem pusherPlayer = new PlayerItem(videoView, pusherInfo, player);
            mPlayers.put(pusherInfo.userID, pusherPlayer);

            player.setPlayListener(new ITXLivePlayListener() {
                @Override
                public void onPlayEvent(int event, Bundle param) {
                    if (event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                        if (mPlayers.containsKey(pusherInfo.userID)) {
                            PlayerItem item = mPlayers.remove(pusherInfo.userID);
                            if (item != null) {
                                item.destroy();
                            }
                        }
                        // 刷新下pushers
                        mRoomManager.updatePushers(roomListenerCallback);
                    }
                    if (roomListenerCallback != null) roomListenerCallback.onVideoPlayEvent(event);
                }

                @Override
                public void onNetStatus(Bundle status) {
                    HnLogUtils.e("aaaa");
                }
            });

            int result = player.startPlay(pusherInfo.accelerateURL, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
            if (result != 0) {
            }
        }
    }

    /**
     * 停止远端视频
     *
     * @param pusherInfo 对应视频的成员
     */
    public void delRemoteView(final @NonNull PusherInfo pusherInfo) {

        synchronized (this) {
            if (!mPlayers.containsKey(pusherInfo.userID)) {
                return;
            }

            PlayerItem pusherPlayer = mPlayers.remove(pusherInfo.userID);
            pusherPlayer.destroy();
        }
    }

    /**
     * 从前台切换到后台，关闭采集摄像头数据，推送默认图片
     */
    public void switchToBackground() {

        if (mTXLivePusher != null && mTXLivePusher.isPushing()) {
            mTXLivePusher.pausePusher();
        }

        for (Map.Entry<String, PlayerItem> entry : mPlayers.entrySet()) {
            entry.getValue().pause();
        }
    }

    /**
     * 由后台切换到前台，开启摄像头数据采集
     */
    public void switchToForeground() {

        if (mTXLivePusher != null && mTXLivePusher.isPushing()) {
            mTXLivePusher.resumePusher();
        }

        for (Map.Entry<String, PlayerItem> entry : mPlayers.entrySet()) {
            entry.getValue().resume();
        }
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        if (mTXLivePusher != null) {
            mTXLivePusher.switchCamera();
        }
    }

    /**
     * 静音设置
     *
     * @param isMute 静音变量, true 表示静音， 否则 false
     */
    public void setMute(boolean isMute) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setMute(isMute);
        }
    }

    /**
     * 设置高清音频
     *
     * @param enable true 表示启用高清音频（48K采样），否则 false（16K采样）
     */
    public void setHDAudio(boolean enable) {
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setAudioSampleRate(enable ? 48000 : 16000);
            mTXLivePusher.setConfig(config);
        }
    }

    /**
     * 设置视频分辨率
     *
     * @param resolution 视频分辨率参数值
     */
    public void setVideoResolution(int resolution) {
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setVideoResolution(resolution);
            mTXLivePusher.setConfig(config);
        }
    }

    /**
     * 设置美颜效果.
     *
     * @param style          美颜风格.三种美颜风格：0 ：光滑  1：自然  2：朦胧
     * @param beautyLevel    美颜等级.美颜等级即 beautyLevel 取值为0-9.取值为0时代表关闭美颜效果.默认值:0,即关闭美颜效果.
     * @param whiteningLevel 美白等级.美白等级即 whiteningLevel 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     * @param ruddyLevel     红润等级.美白等级即 ruddyLevel 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     * @return 是否成功设置美白和美颜效果. true:设置成功. false:设置失败.
     */
    public boolean setBeautyFilter(int style, int beautyLevel, int whiteningLevel, int ruddyLevel) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setBeautyFilter(style, beautyLevel, whiteningLevel, ruddyLevel);
        }
        return false;
    }

    /**
     * 调整摄像头焦距
     *
     * @param value 焦距，取值 0~getMaxZoom();
     * @return true : 成功 false : 失败
     */
    public boolean setZoom(int value) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setZoom(value);
        }
        return false;
    }

    /**
     * 设置播放端水平镜像与否(tips：推流端前置摄像头默认看到的是镜像画面，后置摄像头默认看到的是非镜像画面)
     *
     * @param enable true:播放端看到的是镜像画面,false:播放端看到的是镜像画面
     */
    public boolean setMirror(boolean enable) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setMirror(enable);
        }
        return false;
    }

    /**
     * 调整曝光
     *
     * @param value 曝光比例，表示该手机支持最大曝光调整值的比例，取值范围从-1到1。
     *              负数表示调低曝光，-1是最小值，对应getMinExposureCompensation。
     *              正数表示调高曝光，1是最大值，对应getMaxExposureCompensation。
     *              0表示不调整曝光
     */
    public void setExposureCompensation(float value) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setExposureCompensation(value);
        }
    }

    /**
     * 设置麦克风的音量大小.
     * <p>该接口用于混音处理,比如将背景音乐与麦克风采集到的声音混合后播放.
     *
     * @param x: 音量大小,1为正常音量,建议值为0~2,如果需要调大音量可以设置更大的值.
     * @return 是否成功设置麦克风的音量大小. true:设置麦克风的音量成功. false:设置麦克风的音量失败.
     */
    public boolean setMicVolume(float x) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setMicVolume(x);
        }
        return false;
    }

    /**
     * 设置背景音乐的音量大小.
     * <p>该接口用于混音处理,比如将背景音乐与麦克风采集到的声音混合后播放.
     *
     * @param x 音量大小,1为正常音量,建议值为0~2,如果需要调大背景音量可以设置更大的值.
     * @return 是否成功设置背景音乐的音量大小. true:设置背景音的音量成功. false:设置背景音的音量失败.
     */
    public boolean setBGMVolume(float x) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setBGMVolume(x);
        }
        return false;
    }

    /**
     * 设置图像渲染角度.
     *
     * @param rotation 图像渲染角度.
     */
    public void setRenderRotation(int rotation) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setRenderRotation(rotation);
        }
    }

    /**
     * setFilterImage 设置指定素材滤镜特效
     *
     * @param bmp: 指定素材，即颜色查找表图片。注意：一定要用png图片格式！！！
     *             demo用到的滤镜查找表图片位于RTMPAndroidDemo/app/src/main/res/drawable-xxhdpi/目录下。
     */
    public void setFilter(Bitmap bmp) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setFilter(bmp);
        }
    }

    public void setMotionTmpl(String specialValue) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setMotionTmpl(specialValue);
        }
    }

    public boolean setGreenScreenFile(String file) {
        if (mTXLivePusher != null) {
            return mTXLivePusher.setGreenScreenFile(file);
        }
        return false;
    }

    public void setEyeScaleLevel(int level) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setEyeScaleLevel(level);
        }
    }

    public void setFaceSlimLevel(int level) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setFaceSlimLevel(level);
        }
    }

    public void setFaceVLevel(int level) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setFaceVLevel(level);
        }
    }

    public void setSpecialRatio(float ratio) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setSpecialRatio(ratio);
        }
    }

    public void setFaceShortLevel(int level) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setFaceShortLevel(level);
        }
    }

    public void setChinLevel(int scale) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setChinLevel(scale);
        }
    }

    public void setNoseSlimLevel(int scale) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setNoseSlimLevel(scale);
        }
    }

    public void setVideoQuality(int quality, boolean adjustBitrate, boolean adjustResolution) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setVideoQuality(quality, adjustBitrate, adjustResolution);
        }
    }

    public void setReverb(int reverbType) {
        if (mTXLivePusher != null) {
            mTXLivePusher.setReverb(reverbType);
        }
    }

    /**
     * 设置从前台切换到后台，推送的图片
     *
     * @param bitmap
     */
    public void setPauseImage(final @Nullable Bitmap bitmap) {
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setPauseImg(bitmap);
            config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mTXLivePusher.setConfig(config);
        }
    }

    /**
     * 从前台切换到后台，关闭采集摄像头数据
     *
     * @param id 设置默认显示图片的资源文件
     */
    public void setPauseImage(final @IdRes int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setPauseImg(bitmap);
            config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mTXLivePusher.setConfig(config);
        }
    }

    /**
     * 设置视频的码率区间
     *
     * @param minBitrate
     * @param maxBitrate
     */
    public void setBitrateRange(int minBitrate, int maxBitrate) {
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setMaxVideoBitrate(maxBitrate);
            config.setMinVideoBitrate(minBitrate);
            mTXLivePusher.setConfig(config);
        }
    }

    /**
     * 设置推流端视频的分辨率
     */
    public void setVideoRatio(int videoRatio) {
        if (mTXLivePusher != null) {
            TXLivePushConfig config = mTXLivePusher.getConfig();
            config.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mTXLivePusher.setConfig(config);
        }
    }


    private void initLivePusher() {
        if (mTXLivePusher == null) {
            TXLivePushConfig config = new TXLivePushConfig();
            config.setConnectRetryInterval(10);
            config.setConnectRetryCount(5);
            config.setTouchFocus(false);
            config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mTXLivePusher = new TXLivePusher(this.mContext);
            mTXLivePusher.setConfig(config);
            mTXLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 3, 2);
            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT, true, true);


            mTXLivePushListener = new TXLivePushListenerImpl();
            mTXLivePusher.setPushListener(mTXLivePushListener);

            //电话监听
            mPhoneListener = new TXPhoneStateListener(mTXLivePusher);
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void unInitLivePusher() {
        if (mTXLivePusher != null) {
            mTXLivePushListener = null;
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopCameraPreview(true);
            mTXLivePusher.stopPusher();
            mTXLivePusher = null;
        }
    }

    public interface PusherStreamCallback {
        void onError(int errCode, String errInfo);

        void onSuccess();
    }

    public void startPushStream(final String url, final PusherStreamCallback callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTXLivePushListener != null && callback != null) {
                    if (mTXLivePushListener.cameraEnable() == false) {
                        callback.onError(-1, "获取相机权限失败，请前往设置打开应用权限");
                        return;
                    }
                    if (mTXLivePushListener.micEnable() == false) {
                        callback.onError(-1, "获取麦克风权限失败，请前往设置打开应用权限");
                        return;
                    }
                }
                if (mTXLivePusher != null) {
                    //[RTCRoom] 开始推流 PushUrl {%s}", url;
                    if (callback != null)
                        mTXLivePushListener.setCallback(callback);
                    mTXLivePusher.startPusher(url);
                }
            }
        });
    }


    public interface JoinGroupCallback {
        void onError(int errCode, String errInfo);

        void onSuccess();
    }

    private void jionGroup(final String roomID, final JoinGroupCallback callback) {
        TIMGroupManager.getInstance().applyJoinGroup(roomID, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (null != callback)
                    callback.onError(i, "聊天室未连接");
            }

            @Override
            public void onSuccess() {
                TIMManager.getInstance().getConversation(TIMConversationType.Group, roomID);
                //加入群组成功后，发送加入消息
//                        sendMessage(TCConstants.IMCMD_ENTER_LIVE, "");
                if (null != callback) {
                    callback.onSuccess();
                }
            }
        });
    }

    private interface AddPusherCallback {
        void onError(int errCode, String errInfo);

        void onSuccess();
    }


    private void runOnUiThread(final Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    private final class PlayerItem {
        public TXCloudVideoView view;
        public PusherInfo pusher;
        public TXLivePlayer player;

        public PlayerItem(TXCloudVideoView view, PusherInfo pusher, TXLivePlayer player) {
            this.view = view;
            this.pusher = pusher;
            this.player = player;
        }

        public void resume() {
            this.player.resume();
        }

        public void pause() {
            this.player.pause();
        }

        public void destroy() {
            this.player.stopPlay(true);
            this.view.onDestroy();
        }
    }


    private class RoomState {
        public static final int Absent = 0;            //会话不存在
        public static final int Empty = 1;            //会话存在，但没有成员
        public static final int Created = 2;            //我创建了会话
        public static final int Jioned = 4;            //我加入了会话
    }

    private class RoomManager {

        private HashMap<String, PusherInfo> mPushers = new LinkedHashMap<>();
        private String roomId;
        private String roomName;
        private String selfUserID;
        private String selfUserName;
        private String avatarUrl;
        private int roomState = RoomState.Absent;

        public boolean isDestroyed() {
            return roomState == RoomState.Absent;
        }

        public void setState(int state) {
            roomState = state;
        }

        public int getState() {
            return roomState;
        }

        public boolean isState(int state) {
            return (state & roomState) == state;
        }

        public String getAvatarUrl() {
            return avatarUrl == null ? "null" : avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getRoomId() {
            return roomId;
        }

        public synchronized void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName == null ? "未命名" : roomName;
        }

        public synchronized void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getSelfUserID() {
            return selfUserID;
        }

        public synchronized void setSelfUserID(String selfUserID) {
            this.selfUserID = selfUserID;
        }

        public void setSelfUserName(String selfUserName) {
            this.selfUserName = selfUserName;
        }

        public String getSelfUserName() {
            return selfUserName == null ? "null" : selfUserName;
        }

        public synchronized void clean() {
            roomId = "";
            roomName = "";
            roomState = RoomState.Absent;
            mPushers.clear();
        }

        public synchronized void mergerPushers(List<PusherInfo> members, List<PusherInfo> newMembers, List<PusherInfo> delMembers) {

            if (members == null) {
                if (delMembers != null) {
                    delMembers.clear();
                    for (Map.Entry<String, PusherInfo> entry : mPushers.entrySet()) {
                        delMembers.add(entry.getValue());
                    }
                }
                mPushers.clear();
                return;
            }

            HashMap<String, PusherInfo> memberHashMap = new LinkedHashMap<>();

            for (PusherInfo member : members) {
                if (member.userID != null && !member.userID.equals(selfUserID)) {
                    if (!mPushers.containsKey(member.userID)) {
                        if (newMembers != null)
                            newMembers.add(member);
                    }
                    memberHashMap.put(member.userID, member);
                }
            }

            if (delMembers != null) {
                for (Map.Entry<String, PusherInfo> entry : mPushers.entrySet()) {
                    if (!memberHashMap.containsKey(entry.getKey())) {
                        delMembers.add(entry.getValue());
                    }
                }
            }

            this.mPushers.clear();
            this.mPushers = memberHashMap;
        }

        private void updatePushers(final IRTCRoomListener callback) {

            if (roomId == null) return;
//
//            mHttpRequest.getPushers(roomId, new HttpRequests.OnResponseCallback<HttpResponse.PusherList>() {
//                @Override
//                public void onResponse(int retcode, @Nullable String retmsg, @Nullable final HttpResponse.PusherList data) {
//                    if (retcode == HttpResponse.CODE_OK && data != null && data.pushers != null) {
//
//                        List<PusherInfo> newMembers = new ArrayList<>();
//                        List<PusherInfo> delMembers = new ArrayList<>();
//                        mergerPushers(data.pushers, newMembers, delMembers);
//
//
//                        for (PusherInfo member : delMembers) {
//                            callback.onPusherQuit(member);
//                        }
//
//                        for (PusherInfo member : newMembers) {
//                            callback.onPusherJoin(member);
//                        }
//                    }
//                }
//            });
        }
    }

    /**
     * IM消息
     */
    private class RoomListenerCallback implements IRTCRoomListener {

        private final Handler handler;
        private IRTCRoomListener roomMemberEventListener;

        public RoomListenerCallback(IRTCRoomListener roomMemberEventListener) {
            this.roomMemberEventListener = roomMemberEventListener;
            handler = new Handler(Looper.getMainLooper());
        }


        public void setRoomMemberEventListener(IRTCRoomListener roomMemberEventListener) {
            this.roomMemberEventListener = roomMemberEventListener;
        }

        /**
         * 新成员加入
         *
         * @param member
         */
        @Override
        public void onPusherJoin(final PusherInfo member) {
            new Exception("-----RTCRoom->onPusherJoin------").printStackTrace();
            if (roomMemberEventListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onPusherJoin(member);
                    }
                });
            }
        }

        /**
         * 成员离开房间通知
         *
         * @param member
         */
        @Override
        public void onPusherQuit(final PusherInfo member) {
            if (roomMemberEventListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onPusherQuit(member);
                    }
                });
        }

        /**
         * 收到房间解散通知
         *
         * @param roomId
         */
        @Override
        public void onRoomClosed(final String roomId) {
            if (roomMemberEventListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onRoomClosed(roomId);
                    }
                });
        }


        @Override
        public void onRecvRoomTextMsg(final String message) {
            if (roomMemberEventListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onRecvRoomTextMsg(message);
                    }
                });
        }

        @Override
        public void onVideoPlayEvent(final int event) {
            if (roomMemberEventListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onVideoPlayEvent(event);
                    }
                });
        }

        @Override
        public void onError(final int errorCode, final String errorMessage) {
            if (roomMemberEventListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        roomMemberEventListener.onError(errorCode, errorMessage);
                    }
                });
        }
    }


    /**
     * 视频流消息
     */
    private class TXLivePushListenerImpl implements ITXLivePushListener {
        private boolean mCameraEnable = true;
        private boolean mMicEnable = true;
        private PusherStreamCallback mCallback = null;

        public void setCallback(PusherStreamCallback callback) {
            mCallback = callback;
        }

        public boolean cameraEnable() {
            return mCameraEnable;
        }

        public boolean micEnable() {
            return mMicEnable;
        }

        @Override
        public void onPushEvent(int event, Bundle param) {
            if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
                //"[RTCRoom] 推流成功");
                if (mCallback != null) {
                    mCallback.onSuccess();
                }
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
                mCameraEnable = false;
                //[RTCRoom] 推流失败：打开摄像头失败");
                if (mCallback != null) {
                    mCallback.onError(-1, "获取摄像头权限失败，请前往设置打开应用权限");
                } else {
                    roomListenerCallback.onError(-1, "获取摄像头权限失败，请前往设置打开应用权限");
                }
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                mMicEnable = false;
                //"[RTCRoom] 推流失败：打开麦克风失败");
                if (mCallback != null) {
                    mCallback.onError(-1, "获取麦克风权限失败，请前往设置打开应用权限");
                } else {
                    roomListenerCallback.onError(-1, "获取麦克风权限失败，请前往设置打开应用权限");
                }
            } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
                //"[LiveRoom] 推流失败：网络断开");
                roomListenerCallback.onError(event, "网络断开，推流失败");
            }
        }

        @Override
        public void onNetStatus(Bundle status) {

        }
    }

    /**
     * 电话监听
     */
    class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
//                    if (pusher != null) pusher.pausePusher();

                    HnLogUtils.i("HnTXPushLiveManager", "电话等待接听 暂停推流");
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    if (pusher != null) pusher.pausePusher();
                    if (roomListenerCallback != null) roomListenerCallback.onPusherQuit(null);
                    HnLogUtils.i("HnTXPushLiveManager", "电话电话接听 暂停推流");
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
//                    if (pusher != null) pusher.resumePusher();
                    HnLogUtils.i("HnTXPushLiveManager", "电话挂机 重新推流");
                    break;
            }
        }
    }

    ;


}
