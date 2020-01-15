package com.hotniao.svideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnAuthStateActivity;
import com.hotniao.svideo.activity.HnWebActivity;
import com.hotniao.svideo.activity.withdraw.HnWithDrawDetailActivity;
import com.hotniao.svideo.model.HnSysMsgDetailInfo;
import com.hotniao.svideo.model.bean.GetSystemMsg;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.util.DataTimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：系统消息
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSystemMsgAdapter extends BaseAdapter {

    private List<GetSystemMsg.SystemDialogBean> items;
    private Context context;
    private Gson mGson;

    public HnSystemMsgAdapter(Context context, List<GetSystemMsg.SystemDialogBean> msgItems) {
        this.context = context;
        this.items = msgItems;
        if (mGson == null) mGson = new Gson();
    }

    @Override
    public int getCount() {
        if (items != null && items.size() > 0) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null && items.size() > 0) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final GetSystemMsg.SystemDialogBean bean = items.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_system_msg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.mUserTime.getVisibility() == View.GONE) {
            holder.mUserTime.setVisibility(View.VISIBLE);
        }

        String update_time = bean.getTime();
        if (!TextUtils.isEmpty(update_time)) {
            long l = Long.parseLong(update_time);
            holder.mUserTime.setText(DataTimeUtils.getTimestampString(l * 1000));
        }

        holder.mUserHeader.setImageURI(Uri.parse("res:///" + R.drawable.logo));
        holder.mTvDes.setVisibility(View.GONE);

        try {
            final HnSysMsgDetailInfo info = mGson.fromJson(bean.getMsg(), HnSysMsgDetailInfo.class);
            if(info!=null){
                holder.mUserContent.setText(info.getData().getContent());
                holder.mUserContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("follow".equals(info.getType())){

                        }else if("certification".equals(info.getType())){
                            context.startActivity(new Intent(context,HnAuthStateActivity.class));
                        }else if("withdraw".equals(info.getType())){
                            HnWithDrawDetailActivity.luncher((Activity) context,info.getData().getWithdraw_log_id(),HnWithDrawDetailActivity.Detail);
                        }else if("general".equals(info.getType())){
                            if(!TextUtils.isEmpty(info.getData().getUrl())) HnWebActivity.luncher((Activity) context, HnUiUtils.getString(R.string.app_name),info.getData().getUrl(),HnWebActivity.Banner);
                        }
                    }
                });
            }

        } catch (Exception e) {
            holder.mUserContent.setText(bean.getMsg());
        }





        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.user_time)
        TextView mUserTime;
        @BindView(R.id.user_header)
        FrescoImageView mUserHeader;
        @BindView(R.id.user_content)
        TextView mUserContent;
        @BindView(R.id.tv_des)
        TextView mTvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
