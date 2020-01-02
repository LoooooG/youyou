package com.hotniao.livelibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.bean.OnlineBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间人数列表
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnOnlineRecAdapter extends RecyclerView.Adapter<HnOnlineRecAdapter.MyViewHolder> {

    private final Context               context;
    private final ArrayList<OnlineBean> allList;

    public HnOnlineRecAdapter(Context context, ArrayList<OnlineBean> allList) {
        this.context = context;
        this.allList = allList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.live_item_online_recyclerview_layout, null);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        if (allList != null) {

            OnlineBean onlineBean = allList.get(position);

            String avatarUrl = onlineBean.getAvatar();
            if (holder.onlineHeader != null && !TextUtils.isEmpty(avatarUrl)) {
                holder.onlineHeader.setController(FrescoConfig.getController(avatarUrl));
            }else{
                String icon="res:///"+R.drawable.default_live_head;
                holder.onlineHeader.setController(FrescoConfig.getController(icon));
            }

            holder.onlineHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EventBus.getDefault().post(new HnLiveEvent(position, HnLiveConstants.EventBus.Click_User_Logo,allList.get(position)));
                }
            });
            //是否是vip
            String isVip=onlineBean.getIsVip();
            if(TextUtils.isEmpty(isVip)||"N".equals(isVip)){
                holder.ivVip.setVisibility(View.GONE);
            }else{
                holder.ivVip.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public int getItemCount() {

        if (allList != null && allList.size() > 0) {
            return allList.size();
        } else {
            return 0;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        FrescoImageView onlineHeader;
        ImageView    ivVip;

        public MyViewHolder(View view) {
            super(view);
            onlineHeader = (FrescoImageView) view.findViewById(R.id.online_header);
            ivVip = (ImageView) view.findViewById(R.id.iv_vip);
        }
    }
}
