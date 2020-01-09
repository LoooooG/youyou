package com.hotniao.live.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnVideoDetailActivity;
import com.hotniao.live.model.HnVideoModel;
import com.hotniao.live.model.HnVideoRoomSwitchModel;
import com.hotniao.live.model.bean.HnHomeHotBean;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.utils.HnVideoSwitchDataUitl;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：首页 热门
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeVideoAdapter extends BaseQuickAdapter<HnVideoModel.DBean.ItemsBean, BaseViewHolder> {
    private boolean isMain = true;

    public HnHomeVideoAdapter(List<HnVideoModel.DBean.ItemsBean> mData) {
        super(R.layout.adapter_home_video, mData);
    }

    public HnHomeVideoAdapter(List<HnVideoModel.DBean.ItemsBean> mData, boolean isMain) {
        super(R.layout.adapter_home_video, mData);
        this.isMain = isMain;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HnVideoModel.DBean.ItemsBean item) {
        ((FrescoImageView) helper.getView(R.id.mIvLogo)).setController(FrescoConfig.getController(item.getCover()));
        ((FrescoImageView) helper.getView(R.id.iv_avatar)).setController(FrescoConfig.getController(item.getUser_avatar()));
//        helper.setText(R.id.mTvTitle, TextUtils.isEmpty(item.getTitle()) ? "没有标题哦~" : item.getTitle());
        helper.setText(R.id.mLikeNum, HnUtils.setNoPoint(item.getLike_num()));
        helper.setText(R.id.mTvName, item.getUser_nickname());

        if (TextUtils.isEmpty(item.getPrice())) {
            item.setNeedPay(false);
            helper.setText(R.id.mTvType, R.string.donot_pay_coin);
        } else {
            try {
                int price = Integer.parseInt(item.getPrice());
                if (0 < price) {
                    item.setNeedPay(true);
                    helper.setText(R.id.mTvType, price + HnApplication.getmConfig().getCoin());
                } else {
                    item.setNeedPay(false);
                    helper.setText(R.id.mTvType, R.string.donot_pay_coin);
                }

            } catch (Exception e) {
                item.setNeedPay(false);
                helper.setText(R.id.mTvType, R.string.donot_pay_coin);
            }
        }


        helper.getView(R.id.mRlClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMain) {
                    HnVideoSwitchDataUitl.joinRoom(mContext, item.getCategory_id(), item.getId(),item.isNeedPay()?"2":"1");
                    HnVideoSwitchDataUitl.setOnVideoListener(new HnVideoSwitchDataUitl.OnVideoListener() {
                        @Override
                        public void onSuccess(String id) {
//                            try {
//                                int num = Integer.parseInt(item.getWatch_num());
//                                item.setWatch_num((num + 1) + "");
//                            } catch (Exception e) {
//                            }
//                            helper.setText(R.id.mTvNum, HnUtils.setNoPoint(item.getWatch_num()));
                            HnVideoSwitchDataUitl.removeListener();
                        }

                        @Override
                        public void onError(int errCode, String msg) {
                            HnVideoSwitchDataUitl.removeListener();
                        }
                    });
                } else {
                    List<HnVideoRoomSwitchModel.DBean> datas = new ArrayList<>();
                    for (int i = 0; i < mData.size(); i++) {
                        HnVideoRoomSwitchModel.DBean bean = new HnVideoRoomSwitchModel.DBean();
                        bean.setId(mData.get(i).getId());
                        bean.setCover(mData.get(i).getCover());
                        datas.add(bean);
                    }
                    if (datas != null && datas.size() > 0) {
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < datas.size(); i++) {
                            if (item.getId().equals(datas.get(i).getId()))
                                bundle.putInt("pos", i);
                        }
                        bundle.putSerializable("data", (Serializable) datas);
                        HnVideoDetailActivity.luncher((Activity) mContext, bundle);
                    }
                }
            }
        });

    }


}
