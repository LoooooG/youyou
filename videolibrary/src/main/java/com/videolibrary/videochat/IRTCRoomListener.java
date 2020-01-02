package com.videolibrary.videochat;


import com.videolibrary.videochat.entity.PusherInfo;

/**
 * Created by jac on 2017/10/30.
 */

public interface IRTCRoomListener {


    /**
     * 新成员加入房间通知
     * @param pusherInfo
     */

    void onPusherJoin(PusherInfo pusherInfo);
    /**
     * 成员离开房间通知
     * @param pusherInfo
     */

    void onPusherQuit(PusherInfo pusherInfo);

    /**
     * 收到房间解散通知
     * @param roomId
     */

    void onRoomClosed(String roomId);

    /**
     *
     * @param msg
     */
    void onRecvRoomTextMsg( String msg);
    /**
     *
     * @param event  腾讯云视频回调
     */
    void onVideoPlayEvent( int event);

    /**
     * 错误回调
     * @param errorCode
     * @param errorMessage
     */
    void onError(int errorCode, String errorMessage);
}
