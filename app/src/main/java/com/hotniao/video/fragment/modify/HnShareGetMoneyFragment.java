package com.hotniao.video.fragment.modify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnGeneralizeActivity;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.share.HnShareBiz;
import com.hotniao.video.dialog.HnShareDialog;
import com.hotniao.video.model.HnShareRuleModel;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: LooooG
 * created on: 2020/1/6 22:31
 * description: 分享赚钱页面
 */
public class HnShareGetMoneyFragment extends BaseScollFragment implements BaseRequestStateListener {

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
        return R.layout.home_share_get_money_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shareBiz = new HnShareBiz(mActivity);
        shareBiz.setBaseRequestStateListener(this);

        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);

        rvRule.setLayoutManager(new LinearLayoutManager(mActivity));
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
    protected void initData() {
        shareBiz.shareRule();
    }

    @OnClick(R.id.tv_share)
    public void share(){
        if(ruleModel != null){
            HnShareRuleModel.DBean.ShareBean shareBean = ruleModel.getShare();
            HnShareDialog.newInstance(
                    mShareAPI,
                    mShareAction,
                    shareBean.getContent(),
                    shareBean.getLogo(),
                    shareBean.getUrl(),
                    shareBean.getTitle()).show(mActivity.getFragmentManager(), "share");
        }
    }

    @OnClick(R.id.mIvBack)
    public void back(){


    }

    @OnClick(R.id.tv_earning)
    public void tv_earning(){
        openActivity(HnGeneralizeActivity.class);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        Intent itent = new Intent();
        itent.setClass(mActivity, pClass);
        startActivity(itent);
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getScrollableView() {
        return null;
    }
}
