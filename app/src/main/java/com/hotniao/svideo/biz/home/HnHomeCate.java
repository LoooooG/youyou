package com.hotniao.svideo.biz.home;

import android.text.TextUtils;

import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.svideo.model.HnChatTypeModel;
import com.hotniao.svideo.model.HnHomeLiveCateModel;
import com.hotniao.svideo.model.HnHomeVideoCateModle;

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

public class HnHomeCate {
    public static List<HnChatTypeModel.DBean.ChatCategoryBean> mChatData = new ArrayList<>();
    public static List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mCateData = new ArrayList<>();
    public static List<HnHomeVideoCateModle.DBean.VideoCategoryBean> mVideoCateData = new ArrayList<>();
    private static OnCateListener mListener;


    public static void getChatTypeData() {
        HnHttpUtils.postRequest(HnUrl.Chat_Type, null, HnUrl.Chat_Type, new HnResponseHandler<HnChatTypeModel>(HnChatTypeModel.class) {
            @Override
            public void hnSuccess(String response) {

                if (0 == model.getC()) {
                    if (0 == model.getC() && model.getD().getChat_category() != null) {
                        mChatData.clear();
                        mChatData.addAll(model.getD().getChat_category());
                    }
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                } else {
                    if (mListener != null) {
                        mListener.onError(model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mListener != null) {
                    mListener.onError(errCode, msg);
                }
            }
        });
    }

    public static void getCateData() {
        HnHttpUtils.postRequest(HnUrl.Live_NAVBAR, null, HnUrl.Live_NAVBAR, new HnResponseHandler<HnHomeLiveCateModel>(HnHomeLiveCateModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (0 == model.getC()) {
                    if (0 == model.getC() && model.getD().getLive_category() != null) {
                        mCateData.clear();
                        mCateData.addAll(model.getD().getLive_category());
                    }
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                } else {
                    if (mListener != null) {
                        mListener.onError(model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mListener != null) {
                    mListener.onError(errCode, msg);
                }
            }
        });
    }

    /**
     * 获取视频分类
     */
    public static void getVideoCateData() {
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_INDEX, null, HnUrl.VIDEO_APP_INDEX, new HnResponseHandler<HnHomeVideoCateModle>(HnHomeVideoCateModle.class) {
            @Override
            public void hnSuccess(String response) {
                if (0 == model.getC()) {
                    if (0 == model.getC() && model.getD().getVideo_category() != null) {
                        mVideoCateData.clear();
                        mVideoCateData.addAll(model.getD().getVideo_category());
                    }
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                } else {
                    if (mListener != null) {
                        mListener.onError(model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mListener != null) {
                    mListener.onError(errCode, msg);
                }
            }
        });
    }

    public static void setOnCateListener(OnCateListener listener) {
        mListener = listener;
    }

    public static void removeListener() {
        mListener = null;
    }

    public static String getChatTypeName(String id) {
       /* if (mChatData.size() > 0) {
            return mChatData.get(mChatData.size() - 1).getChat_category_name();
        }*/
        for (int i = 0; i < mChatData.size(); i++) {
            if (TextUtils.equals(mChatData.get(i).getChat_category_id(), id)) {
                return mChatData.get(i).getChat_category_name();
            }
        }
        return "";
    }


    public interface OnCateListener {
        void onSuccess();


        void onError(int errCode, String msg);
    }

}
