package com.hotniao.livelibrary.util;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.widget.dialog.HNNetDialog;
import com.tencent.rtmp.TXLiveConstants;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播工具类
 * 创建人：Administrator
 * 创建时间：2017/9/15 16:27
 * 修改人：Administrator
 * 修改时间：2017/9/15 16:27
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveUtils {

    /**
     * 隐藏键盘
     * @param view  控件
     * @param context     上下文
     */
    public static void hideSoftKeyBoard(View view, Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 弹出软键盘
     */
    public static void openSoftKeyBoard(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 可设定[确定]和[取消]内容及颜色的交互对话框
     */
    public static HNNetDialog netDialog(Context context, String ok, String cancel, int okColorId, int cancelColorId, String title, String description, View.OnClickListener listener) {
        return HNNetDialog.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(true)
                .setView(R.layout.live_dialog_net_layout)
                .setOKText(ok)
                .setCancelText(cancel)
                .okBtnTxtColor(okColorId)
                .cancelBtnTxtColor(cancelColorId)
                .addListener(listener)
                .setTitle(title)
                .setDescription(description);
    }


    //公用打印辅助函数
    public static  String getNetStatusString(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-12s %-12s",
                "CPU:"+status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH)+"*"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:"+status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED)+"Kbps",
                "JIT:"+status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "ARA:"+status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE)+"Kbps",
                "QUE:"+status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE)+"|"+status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:"+status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT)+"|"+status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE)+"Kbps",
                "SVR:"+status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AVRA:"+status.getInt(TXLiveConstants.NET_STATUS_SET_VIDEO_BITRATE),
                "PLA:"+status.getInt(TXLiveConstants.NET_STATUS_PLAYABLE_DURATION));
        return str;
    }

}
