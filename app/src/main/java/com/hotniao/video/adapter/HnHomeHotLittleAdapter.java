package com.hotniao.video.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.model.bean.HnHomeHotBean;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;
import com.reslibrarytwo.HnSkinTextView;

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
public class HnHomeHotLittleAdapter extends BaseQuickAdapter<HnHomeHotBean.ItemsBean, BaseViewHolder> {

    public HnHomeHotLittleAdapter() {
        super(R.layout.item_home_hot_littile);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HnHomeHotBean.ItemsBean item) {
        helper.addOnClickListener(R.id.fiv_live_logo);
        //封面logo
        FrescoImageView mFivLogo = (FrescoImageView) helper.getView(R.id.fiv_live_logo);
        mFivLogo.setController(FrescoConfig.getController(item.getAnchor_live_img()));
        //在线人数
        helper.setText(R.id.mTvNum, item.getAnchor_live_onlines());
        //直播模式0：免费，1：VIP，2：门票，3：计时
        TextView mTvType = helper.getView(R.id.mTvType);
        String live_tyoe = item.getAnchor_live_pay();
        if ("0".equals(live_tyoe)) {
            mTvType.setText("免费");
        } else if ("1".equals(live_tyoe)) {
            mTvType.setText("VIP");
        } else if ("2".equals(live_tyoe) || "3".equals(live_tyoe)) {
            mTvType.setText("付费");
        } else {
            mTvType.setText("免费");
        }
        HnSkinTextView mTvLv = helper.getView(R.id.mTvLv);
        HnLiveLevelUtil.setAudienceLevBg(mTvLv, item.getUser_level(), true);
        //标题
        helper.setText(R.id.tv_live_title, item.getUser_nickname());

        helper.getView(R.id.fiv_live_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HnLiveSwitchDataUitl.joinRoom(mContext, item.getAnchor_category_id(), item.getAnchor_live_pay(), item.getUser_id(), item.getAnchor_game_category_id());
            }
        });
    }


}
