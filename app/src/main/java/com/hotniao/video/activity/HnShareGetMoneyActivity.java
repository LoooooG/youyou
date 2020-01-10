package com.hotniao.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.biz.share.HnShareBiz;
import com.hotniao.video.dialog.HnShareDialog;
import com.hotniao.video.dialog.HnVideoCommDialog;
import com.hotniao.video.model.HnShareRuleModel;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：yibaobao
 * 类描述：
 * 创建人：oyke
 * 创建时间：2019/1/26 13:46
 * 修改人：oyke
 * 修改时间：2019/1/26 13:46
 * 修改备注：
 * Version:  1.0.0
 */
public class HnShareGetMoneyActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.rv_rule)
    RecyclerView rvRule;

    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;
    private CommRecyclerAdapter mAdapter;

    private HnShareBiz shareBiz;
    private HnShareRuleModel.DBean ruleModel;
    private List<HnShareRuleModel.DBean.RuleBean> rules = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.share_get_money_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        shareBiz = new HnShareBiz(this);
        shareBiz.setBaseRequestStateListener(this);

        mShareAPI = UMShareAPI.get(this);
        mShareAction = new ShareAction(this);

        rvRule.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                HnShareRuleModel.DBean.RuleBean ruleBean = rules.get(position);
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvContent = holder.getView(R.id.tv_content);
                tvTitle.setText(ruleBean.getTitle());
                tvContent.setText(ruleBean.getContent());
            }


            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_share_rule;
            }

            @Override
            public int getItemCount() {
                return rules.size();
            }
        };
        rvRule.setAdapter(mAdapter);
    }

    @Override
    public void getInitData() {
        shareBiz.shareRule();
    }

    @OnClick(R.id.tv_share)
    public void share(){
        if(ruleModel != null){
            HnShareRuleModel.DBean.ShareBean shareBean = ruleModel.getShare();
            HnShareDialog.newInstance(mShareAPI, mShareAction, shareBean.getContent(), shareBean.getLogo(), shareBean.getUrl(), shareBean.getTitle()).show(getFragmentManager(), "share");
        }
    }

    @OnClick(R.id.mIvBack)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_earning)
    public void tv_earning(){
        openActivity(HnGeneralizeActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if(HnShareBiz.SHARE_RULE.equals(type)){
            ruleModel = (HnShareRuleModel.DBean) obj;
            rules.addAll(ruleModel.getRule());
            if(mAdapter != null){
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        HnToastUtils.showToastShort(msg);
    }
}
