package com.hn.library.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hn.library.utils.HnDimenUtil;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：图片处理工具类的封装（基于Glide）
 * 创建人：Kevin
 * 创建时间：2016/5/13 9:43
 * 修改人：Kevin
 * 修改时间：2016/5/13 9:43
 * 修改备注：
 * Version:  1.0.0
 */
public class HnImageLoader {

    private Context mContext;
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public HnImageLoader(Context context) {
        this.mContext = context;
    }

    // 将资源ID转为Uri
    public Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    // 加载访问地址的图片
    public void loadUrlImage(String url, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .into(imageView);
    }

    // 加载drawable图片
    public void loadResImage(int resId, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .into(imageView);
    }

    // 加载本地图片
    public void loadLocalImage(String path,int loadResId, ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .into(imageView);
    }
/****************************圆形图片***************************************/

    // 加载有访问地址的圆型图片
    public void loadCircleImage(String url, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    // 加载drawable圆型图片
    public void loadCircleResImage(int resId,int loadResId, ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(String path, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }


    /****************************圆角型图片***************************************/

    // 加载有访问地址的圆角型图片
    public void loadRoundImage(String url, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideRoundTransform(mContext, HnDimenUtil.dp2px(mContext,2.0f)))
                .into(imageView);
    }

    // 加载drawable圆角型图片
    public void loadRoundResImage(int resId,int loadResId, ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideRoundTransform(mContext, HnDimenUtil.dp2px(mContext,2.0f)))
                .into(imageView);
    }

    // 加载本地圆角型图片
    public void loadRoundLocalImage(String path, int loadResId,ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)
                .placeholder(loadResId)
                .error(loadResId)
                .crossFade()
                .transform(new GlideRoundTransform(mContext, HnDimenUtil.dp2px(mContext,2.0f)))
                .into(imageView);
    }


}
