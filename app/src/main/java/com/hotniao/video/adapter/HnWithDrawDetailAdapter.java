package com.hotniao.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotniao.video.R;
import com.hotniao.video.model.HnWithDrawDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：提现详情
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnWithDrawDetailAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<HnWithDrawDetailModel.DBean.InfoBean> mDatas = new ArrayList<>();

    private String mState="C";//状态，Y：成功，N：失败，C：审核中
    public HnWithDrawDetailAdapter(Context context, List<HnWithDrawDetailModel.DBean.InfoBean> data) {
        mInflater = LayoutInflater.from(context);
        mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_withdraw_detail,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();

        vh.mTv1.setText(mDatas.get(i).getTitle());
        vh.mTv2.setText(mDatas.get(i).getRemark());


        if (i == 0) {
            vh.mImage.setImageResource(R.drawable.submission_of_application);
            vh.mTv3.setText(mDatas.get(i).getCash());
            vh.mTv3.setVisibility(View.VISIBLE);
            vh.mTvLine.setVisibility(View.VISIBLE);

        } else if (i == mDatas.size() - 1) {
            if("Y".equals(mState)){
                vh.mImage.setImageResource(R.drawable.submission_of_application);
            }else if("N".equals(mState)){
                vh.mImage.setImageResource(R.drawable.withdrawal_of_failure);
            }else {
                vh.mImage.setImageResource(R.drawable.account);
            }

            vh.mTv3.setVisibility(View.GONE);
            vh.mTvLine.setVisibility(View.GONE);
        } else {
            vh.mImage.setImageResource(R.drawable.account);
            vh.mTv3.setVisibility(View.GONE);
            vh.mTvLine.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mImage)
        ImageView mImage;
        @BindView(R.id.mTvLine)
        TextView mTvLine;
        @BindView(R.id.mTv1)
        TextView mTv1;
        @BindView(R.id.mTv2)
        TextView mTv2;
        @BindView(R.id.mTv3)
        TextView mTv3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setSuccessState(String state){
        this.mState=state;
    }

}
