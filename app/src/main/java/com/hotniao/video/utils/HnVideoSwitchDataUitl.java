package com.hotniao.video.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.activity.HnVideoDetailActivity;
import com.hotniao.video.model.HnVideoRoomSwitchModel;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.HnLiveRoomSwitchModel;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoSwitchDataUitl {
    private static String oldId = "-1";

    public static void joinRoom(final Context context, String cateId, final String videoId,String priceType) {
        if (!"-1".equals(oldId)) return;
        oldId = videoId;
        RequestParams params = new RequestParams();

        params.put("type", cateId);
        params.put("price_type", priceType);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_VIDEO_BY_TYPE, params, HnUrl.VIDEO_APP_VIDEO_BY_TYPE, new HnResponseHandler<HnVideoRoomSwitchModel>(HnVideoRoomSwitchModel.class) {
            @Override
            public void hnSuccess(String response) {
                oldId = "-1";
                if (context == null) return;
                if (model.getD() == null) return;
                List<HnVideoRoomSwitchModel.DBean> datas = model.getD();
                if (datas != null && datas.size() > 0) {
                    Bundle bundle = new Bundle();
                    for (int i = 0; i < datas.size(); i++) {
                        if (videoId.equals(datas.get(i).getId()))
                            bundle.putInt("pos", i);
                    }
                    bundle.putSerializable("data", (Serializable) datas);


                  HnVideoDetailActivity.luncher((Activity) context,bundle);
                    if (mListener != null) mListener.onSuccess(videoId);
                } else {
                    HnToastUtils.showToastShort("视频已失效~");
                    EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.RefreshVideoList, model));
                    if (mListener != null) mListener.onError(model.getC(), model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                oldId = "-1";
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.RefreshVideoList, msg));
                HnToastUtils.showToastShort(msg);
                if (mListener != null) mListener.onError(errCode, msg);
            }
        });
    }

    public static void setOnVideoListener(OnVideoListener listener) {
        mListener = listener;
    }

    public static void removeListener() {
        mListener = null;
    }

    private static OnVideoListener mListener;


    public interface OnVideoListener {
        void onSuccess(String id);

        void onError(int errCode, String msg);
    }
}
