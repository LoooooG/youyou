package com.hotniao.svideo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnBlackListBean;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/25 15:48
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBlackListAdapter extends BaseQuickAdapter<HnBlackListBean.DBean.ItemsBean,BaseViewHolder> {
    private static final String TAG = "HnBlackListAdapter";
    private Context context;
    public HnBlackListAdapter(Context context) {
        super(R.layout.adapter_black_list);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HnBlackListBean.DBean.ItemsBean item) {
        if (item != null) {
            helper.setText(R.id.tv_nickname, item.getUser_nickname());
            HnLiveLevelUtil.setAudienceLevBg((HnSkinTextView) helper.getView(R.id.tv_level), item.getUser_level()+"", true);
            if (!TextUtils.isEmpty(item.getUser_avatar())) {
                ((FrescoImageView)helper.getView(R.id.iv_avatar)).setController(FrescoConfig.getHeadController(item.getUser_avatar()));
            }
            helper.getView(R.id.tv_black_list).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestParams param = new RequestParams();
                    param.put("anchor_user_id",item.getUser_id());
                    param.put("type",2+"");
                    HnHttpUtils.postRequest(HnUrl.USER_PROFILR_ADD_BLACK, param, TAG, new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
                        @Override
                        public void hnSuccess(String response) {
                            if (model.getC() == 0) {
                                HnLogUtils.d(TAG,helper.getLayoutPosition()+"");
                                remove(helper.getLayoutPosition());
                            }
                        }

                        @Override
                        public void hnErr(int errCode, String msg) {
                            HnToastUtils.showToastShort("移除黑名单失败");
                        }
                    });
                }
            });
        }
    }

}
