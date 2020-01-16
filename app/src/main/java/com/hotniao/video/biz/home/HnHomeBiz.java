package com.hotniao.video.biz.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.hn.library.banner.HnBannerLayout;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnWebActivity;
import com.hotniao.video.model.HnBannerModel;
import com.hotniao.video.model.HnHomeFastChatModel;
import com.hotniao.video.model.HnHomeHotAnchorChatModel;
import com.hotniao.video.model.HnHomeHotModel;
import com.hotniao.video.model.HnPlayBackModel;
import com.hotniao.video.model.HnVideoModel;
import com.hotniao.video.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import java.util.List;

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
public class HnHomeBiz {

    public static final String Banner = "banner";
    public static final String HotLive = "hotlive";
    public static final String HotVideo = "hotvideo";
    public static final String FirstVideo = "firstvideo";
    public static final String DeleteVideo = "DeleteVideo";
    public static final String Love = "love";
    public static final String ChatFast = "ChatFast";
    public static final String ChatFastNear = "ChatFastNear";
    public static final String PLAYBACK_LIST = "playback_list";
    private String TAG = "HnHomeBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnHomeBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网络请求:获取首页热门数据
     *
     * @param mPage  页数
     * @param cateId 分类
     */
    public void requestToHotList(int mPage, String cateId) {
        RequestParams param = new RequestParams();
        param.put("page", mPage + "");
        param.put("pagesize", 20 + "");
        if (!"-1".equals(cateId))
            param.put("anchor_category_id", cateId + "");

        HnHttpUtils.postRequest(HnUrl.Live_HOT, param, TAG, new HnResponseHandler<HnHomeHotModel>(HnHomeHotModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("hot_list", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("hot_list", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("hot_list", errCode, msg);
                }
            }
        });
    }


    /**
     * 获取轮播图
     * 1表示推荐首页 2表示小视频首页 3直播 4约聊
     */
    public void getBanner(int type) {
        RequestParams params = new RequestParams();
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.BANNER, params, TAG, new HnResponseHandler<HnBannerModel>(HnBannerModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(Banner, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(Banner, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(Banner, errCode, msg);
                }
            }
        });

    }


    /**
     * 获取s随机主播
     */
    public void getHomeHotAnchorChat(int num,String chat_category_id) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty("chat_category_id")) {
            params.put("chat_category_id", chat_category_id);
        }
        params.put("num", num + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_ANCHOR_CHAT, params, TAG, new HnResponseHandler<HnHomeHotAnchorChatModel>(HnHomeHotAnchorChatModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(Love, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(Love, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(Love, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取约聊主播
     */
    public void getHomeAnchorChatFast(int page, String chat_category_id,boolean isHome) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty("chat_category_id")) {
            params.put("chat_category_id", chat_category_id);
            if(isHome){
                params.put("chat_category_id", "");
            }

        }
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_PRIVATE_CHAT, params, TAG, new HnResponseHandler<HnHomeFastChatModel>(HnHomeFastChatModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d(TAG,"hnSuccess");
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatFast, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatFast, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d(TAG,"errCode:"+errCode+",msg:"+msg);
                if (listener != null) {
                    listener.requestFail(ChatFast, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取约聊主播
     */
    public void getFollowAnchorChatFast(int page) {
        RequestParams params = new RequestParams();
        params.put("page", page + "");
        params.put("pageSize", 10 + "");
        HnHttpUtils.postRequest(HnUrl.FOLLOW_ANCHOR, params, TAG, new HnResponseHandler<HnHomeFastChatModel>(HnHomeFastChatModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d(TAG,"hnSuccess");
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatFast, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatFast, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d(TAG,"errCode:"+errCode+",msg:"+msg);
                if (listener != null) {
                    listener.requestFail(ChatFast, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取附近约聊主播
     */
    public void getNearAnchorChatFast(int page, String lng, String lat, String local) {
        RequestParams params = new RequestParams();
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        params.put("lng", lng + "");
        params.put("lat", lat + "");
        params.put("local", local);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_NEARNY_ANCHOR, params, TAG, new HnResponseHandler<HnHomeFastChatModel>(HnHomeFastChatModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatFastNear, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatFastNear, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatFastNear, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取首页热门直播
     *
     * @param page
     * @param lng
     * @param lat
     */
    public void getOneHomeHotLive(int page, String lng, String lat) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");
        param.put("pagesize", 10 + "");
//        if (!TextUtils.isEmpty(lng))  param.put("lng", lng + "");
//        if (!TextUtils.isEmpty(lat)) param.put("lat", lat + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_GET_HOME_HOT_LIVE, param, TAG, new HnResponseHandler<HnHomeHotModel>(HnHomeHotModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(HotLive, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(HotLive, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(HotLive, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取首页视频列表
     *
     * @param page
     * @param lng
     * @param lat
     * @param location  //1 首页热门 2我-小视频 0 其他
     * @param city      //城市必传
     * @param type      //分类必传
     * @param userId    //获取其他用户视频必传
     * @param priceType //	0 全部 1免费 2收费
     */
    public void getOneHomeHotVideo(int page, int location, String lng, String lat, String city, String type, String userId, String priceType) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");

        param.put("pagesize", location == 1 ? (20 + "") : (20 + ""));

        param.put("location", location + "");

        if (!TextUtils.isEmpty(lng)) param.put("lng", lng + "");
        if (!TextUtils.isEmpty(lat)) param.put("lat", lat + "");
        if (!TextUtils.isEmpty(city)) param.put("city", city + "");
        if (!TextUtils.isEmpty(type)) param.put("type", type + "");
        if (!TextUtils.isEmpty(userId)) param.put("user_id", userId + "");
        if (!TextUtils.isEmpty(priceType)) param.put("price_type", priceType + "");


        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_LIST, param, TAG, new HnResponseHandler<HnVideoModel>(HnVideoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(HotVideo, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(HotVideo, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(HotVideo, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取首页视频列表
     *
     * @param page
     * @param lng
     * @param lat
     * @param location  //1 首页热门 2我-小视频 0 其他
     * @param city      //城市必传
     * @param type      //分类必传
     * @param userId    //获取其他用户视频必传
     * @param priceType //	0 全部 1免费 2收费
     */
    public void getIndexVideo(int page, int location, String lng, String lat, String city, String type, String userId, String priceType) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");

        param.put("pagesize", location == 1 ? (20 + "") : (20 + ""));

        param.put("location", location + "");

        if (!TextUtils.isEmpty(lng)) param.put("lng", lng + "");
        if (!TextUtils.isEmpty(lat)) param.put("lat", lat + "");
        if (!TextUtils.isEmpty(city)) param.put("city", city + "");
        if (!TextUtils.isEmpty(type)) param.put("type", type + "");
        if (!TextUtils.isEmpty(userId)) param.put("user_id", userId + "");
        if (!TextUtils.isEmpty(priceType)) param.put("price_type", priceType + "");


        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_FIRST, param, TAG, new HnResponseHandler<HnVideoModel>(HnVideoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(FirstVideo, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(FirstVideo, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(FirstVideo, errCode, msg);
                }
            }
        });

    }

    /**
     * 删除视频
     */
    public void deleteVideo(final String video_id, final int position) {
        RequestParams param = new RequestParams();
        param.put("video_id", video_id + "");
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_DELETE_VIDEO, param, "VIDEO_APP_DELETE_VIDEO", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(DeleteVideo, video_id, position);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(DeleteVideo, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(DeleteVideo, errCode, msg);
                }
            }
        });

    }

    /**
     * 获取回放列表
     */
    public void getPlayBackList(String categoryId,int page) {
        RequestParams params = new RequestParams();
        params.put("anchor_category_id", categoryId);
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        HnHttpUtils.postRequest(HnUrl.PLAYBACK_LIST, params, TAG, new HnResponseHandler<HnPlayBackModel>(HnPlayBackModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d(TAG,"hnSuccess");
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(PLAYBACK_LIST, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(PLAYBACK_LIST, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d(TAG,"errCode:"+errCode+",msg:"+msg);
                if (listener != null) {
                    listener.requestFail(PLAYBACK_LIST, errCode, msg);
                }
            }
        });

    }

    /**
     * 设置bannner
     *
     * @param bannerBeen
     */
    public void initViewpager(ConvenientBanner mBanner, List<String> imgUrl, final List<HnBannerModel.DBean.CarouselBean> bannerBeen) {
        if (bannerBeen.size() == 0) {
            imgUrl.clear();
            imgUrl.add(HnBannerLayout.NO_IMAGE);
        } else {
            imgUrl.clear();
            for (int i = 0; i < bannerBeen.size(); i++) {
                imgUrl.add(HnUrl.setImageUrl(bannerBeen.get(i).getCarousel_url()));
            }
        }

        mBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, imgUrl).startTurning(3000)
                .setPageIndicator(new int[]{R.drawable.indicator_point_select, R.drawable.indicator_point_nomal})
                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(this)//监听翻页事件
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (position < 0 || position > bannerBeen.size() - 1) return;
                        HnBannerModel.DBean.CarouselBean bean = bannerBeen.get(position);
                        if (bean != null) {
                            String type = bean.getCarousel_href();
                            if (!TextUtils.isEmpty(type) && type.contains("http")) {//外链
                                HnWebActivity.luncher(context, HnUiUtils.getString(R.string.app_name), bean.getCarousel_href(), HnWebActivity.Banner);
                            } else {
//                                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.invalid_url));
                            }
                        } else {
//                            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.invalid_url));
                        }
                    }
                });

    }

    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).placeholder(R.drawable.default_banner).error(R.drawable.default_banner).crossFade().into(imageView);
        }
    }
}
