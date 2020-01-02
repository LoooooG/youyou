package com.hotniao.live.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.model.bean.HnHomeHotBean;
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
public class HnHomeHotExtAdapter extends BaseQuickAdapter<HnHomeHotBean.ItemsBean, BaseViewHolder> {
    //是否显示等级
    private boolean isShowLv=false;
    public HnHomeHotExtAdapter(boolean isShowLv) {
        super(R.layout.item_home_hot);
        this.isShowLv=isShowLv;
    }
    public HnHomeHotExtAdapter() {
        super(R.layout.item_home_hot);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HnHomeHotBean.ItemsBean item) {
        //封面logo
        FrescoImageView mFivLogo = helper.getView(R.id.fiv_live_logo);
        mFivLogo.setController(FrescoConfig.getController(item.getAnchor_live_img()));
        //在线人数
        helper.setText(R.id.mTvNum, item.getAnchor_live_onlines());
        //直播模式 	主播直播收费类型，0：免费，1：VIP，2：门票，3：计时
        TextView mTvType = helper.getView(R.id.mTvType);
        String live_tyoe = item.getAnchor_live_pay();
        if ("0".equals(live_tyoe)) {
            mTvType.setBackgroundResource(R.drawable.icon_free);
        } else if ("1".equals(live_tyoe)) {
            mTvType.setBackgroundResource(R.drawable.icon_vip);
        } else if ("2".equals(live_tyoe) || "3".equals(live_tyoe)) {
            mTvType.setBackgroundResource(R.drawable.icon_pay);
        } else {
            mTvType.setBackgroundResource(R.drawable.icon_free);
        }
        HnSkinTextView mTvLv = helper.getView(R.id.mTvLv);
        if(isShowLv){
            mTvLv.setVisibility(View.VISIBLE);
            HnLiveLevelUtil.setAnchorLevBg(mTvLv, item.getAnchor_level(), true);
        }else {
            mTvLv.setVisibility(View.GONE);
        }

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
