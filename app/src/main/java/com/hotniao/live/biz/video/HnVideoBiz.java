package com.hotniao.live.biz.video;

import android.app.Activity;
import android.text.TextUtils;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.live.model.HnVideoDetailModel;
import com.hotniao.live.model.HnVideoUrlModel;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于首页界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/13 13:54
 * 修改人：Administrator
 * 修改时间：2017/9/13 13:54
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVideoBiz {
    private static final String TAG = "HnVideoBiz";
    public static final String VideoDetail = "VideoDetail";//视频详情
    public static final String VideoUrl = "VideoUrl";//视频地址
    public static final String VideoZan = "VideoZan";//视频点赞
    public static final String VideoComm = "VideoComm";//视频评论
    public static final String VideoShare = "VideoShare";//视频分享

    private Activity context;

    private BaseRequestStateListener listener;

    public HnVideoBiz(Activity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 获取视频详情
     */
    public void getVideoDetail(final String videoId) {
        RequestParams params = new RequestParams();
        params.put("id", videoId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_VIDEO_DETAIL, params, HnUrl.VIDEO_APP_VIDEO_DETAIL + videoId, new HnResponseHandler<HnVideoDetailModel>(HnVideoDetailModel.class) {
                    @Override
                    public void hnSuccess(String response) {
                        if (model.getC() == 0) {
                            if (listener != null) {
                                listener.requestSuccess(VideoDetail, videoId, model);
                            }
                        } else {
                            if (listener != null) {
                                listener.requestFail(VideoDetail, model.getC(), model.getM());
                            }
                        }
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        if (listener != null) {
                            listener.requestFail(VideoDetail, errCode, msg);
                        }
                    }
                }
        );
    }

    /**
     * 获取视频详情
     */
    public void getVideoUrl(final String videoId) {
        RequestParams params = new RequestParams();
        params.put("video_id", videoId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_VIDEO_URL, params, HnUrl.VIDEO_APP_VIDEO_URL + videoId, new HnResponseHandler<HnVideoUrlModel>(HnVideoUrlModel.class) {
                    @Override
                    public void hnSuccess(String response) {

                        if (model.getC() == 0) {
                            if (listener != null) {
                                listener.requestSuccess(VideoUrl, videoId, model);
                                HnLogUtils.e(TAG, "url:"+model.getD().getUrl());
                            }
                        } else {
                            if (listener != null) {
                                listener.requestFail(VideoUrl, model.getC(), model.getM());
                            }
                        }
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        if (listener != null) {
                            listener.requestFail(VideoUrl, errCode, msg);
                        }
                    }
                }
        );
    }

    /**
     * 点赞视频
     */
    public void clickZanVideo(final String videoId) {
        RequestParams params = new RequestParams();
        params.put("video_id", videoId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_VIDEO_LICK, params, HnUrl.VIDEO_APP_VIDEO_LICK, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                    @Override
                    public void hnSuccess(String response) {
                        if (model.getC() == 0) {
                            if (listener != null) {
                                listener.requestSuccess(VideoZan, videoId, "");
                            }
                        } else {
                            if (listener != null) {
                                listener.requestFail(VideoZan, model.getC(), model.getM());
                            }
                        }
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        if (listener != null) {
                            listener.requestFail(VideoZan, errCode, msg);
                        }
                    }
                }
        );
    }

    /**
     * 评论
     */
    public void commVideo(final String videoId, String fUserId, String content) {
        RequestParams params = new RequestParams();
        params.put("video_id", videoId);
        if (!TextUtils.isEmpty(fUserId)) params.put("f_user_id", fUserId);
        params.put("content", content);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_ADD_REPLY, params, HnUrl.VIDEO_APP_ADD_REPLY, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                    @Override
                    public void hnSuccess(String response) {
                        if (model.getC() == 0) {
                            if (listener != null) {
                                listener.requestSuccess(VideoComm, videoId, "");
                            }
                        } else {
                            if (listener != null) {
                                listener.requestFail(VideoComm, model.getC(), model.getM());
                            }
                        }
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        if (listener != null) {
                            listener.requestFail(VideoComm, errCode, msg);
                        }
                    }
                }
        );
    }

    public void shareSuccess(final String videoId) {
        RequestParams params = new RequestParams();
        params.put("video_id", videoId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_SHARE_VIDEO_SUCCESS, params, "VIDEO_APP_SHARE_VIDEO_SUCCESS", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(VideoShare, videoId, "");
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(VideoShare, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(VideoShare, errCode, msg);
                }
            }
        });
    }
}
