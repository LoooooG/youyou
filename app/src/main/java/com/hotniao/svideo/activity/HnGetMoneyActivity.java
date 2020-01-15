package com.hotniao.svideo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginModel;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.bindPhone.HnFirstBindPhoneActivity;
import com.hotniao.svideo.activity.withdraw.HnWithDrawDetailActivity;
import com.hotniao.svideo.activity.withdraw.HnWithDrawVerificationActivity;
import com.hotniao.svideo.adapter.HnInviteWithdrawLogAdapter;
import com.hotniao.svideo.biz.share.HnShareBiz;
import com.hotniao.svideo.biz.user.account.HnMyAccountBiz;
import com.hotniao.svideo.model.HnGeneralizeModel;
import com.hotniao.svideo.model.HnInviteWithdrawLogModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HnGetMoneyActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mLvBillRec;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;
    @BindView(R.id.tv_person_num)
    TextView tvPersonNum;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.iv_reward)
    ImageView ivReward;
    @BindView(R.id.tv_earnings)
    TextView tvEarnings;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw;

    /**
     * 提现记录列表适配器
     */
    private HnInviteWithdrawLogAdapter withdrawLogAdapter;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 时间类型
     */
    private List<HnInviteWithdrawLogModel.DBean.ItemsBean> mData = new ArrayList<>();

    private HnShareBiz shareBiz;
    private HnMyAccountBiz accountBiz;

    @Override
    public int getContentViewId() {
        return R.layout.activity_get_money;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle("领取现金");
        setShowBack(true);
        shareBiz = new HnShareBiz(this);
        shareBiz.setBaseRequestStateListener(this);
        accountBiz = new HnMyAccountBiz(this);
        accountBiz.setBaseRequestStateListener(this);
        //初始化适配器
        withdrawLogAdapter = new HnInviteWithdrawLogAdapter(mData);
        mLvBillRec.setLayoutManager(new LinearLayoutManager(this));
        mLvBillRec.setHasFixedSize(true);
        withdrawLogAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HnWithDrawDetailActivity.luncher(HnGetMoneyActivity.this, mData.get(position).getId()+"", HnWithDrawDetailActivity.Detail,true);
            }
        });
        mLvBillRec.setAdapter(withdrawLogAdapter);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.now_no_record)).setEmptyImage(R.drawable.empty_com);
        initEvent();
    }

    private void initEvent() {
        //刷新处理
        mSwipeRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                accountBiz.inviteWithdrawRecord(mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                accountBiz.inviteWithdrawRecord(mPage);
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPage = 1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                getInitData();
            }
        });
    }

    private void initHeaderView(HnGeneralizeModel.DBean model) {
        if (model != null) {
            tvPersonNum.setText(model.getUser_invite_total() + "人");
            tvEarnings.setText(HnUtils.setTwoPoints(model.getUser_invite_reward() + "") + "元");
        }
    }


    @OnClick({R.id.tv_exchange, R.id.rl_invite_user, R.id.tv_withdraw})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_exchange:
                openActivity(HnMyExchangeActivity.class);
                break;
            case R.id.rl_invite_user:
                openActivity(HnMyInviteUserActivity.class);
                break;
            case R.id.tv_withdraw:
                checkWithdraw();
                break;
        }
    }

    private void checkWithdraw(){

        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            HnToastUtils.showToastShort(getString(R.string.please_write_withdraw_money));
            return;
        }
        if (Double.parseDouble(etMoney.getText().toString().trim()) <= 0) {
            HnToastUtils.showToastShort(getString(R.string.with_draw_dayu_zreo));
            return;
        }
        if (Float.valueOf(etMoney.getText().toString()) % 100 != 0) {
            HnToastUtils.showToastShort("提现金额必须为100的整倍数");
            return;
        }
        if (!HnRegexUtils.isMobileExact(etAccount.getText().toString().trim()) && !HnRegexUtils.isEmail(etAccount.getText().toString().trim())) {
            HnToastUtils.showToastShort(getString(R.string.please_true_zfb_id));
            return;
        }
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            HnToastUtils.showToastShort(etName.getHint().toString());
            return;
        }
        if (TextUtils.isEmpty(HnApplication.mUserBean.getUser_phone())) {
            openActivity(HnFirstBindPhoneActivity.class);
            return;
        }

        HnWithDrawVerificationActivity.luncher(HnGetMoneyActivity.this, etMoney.getText().toString(), etAccount.getText().toString(), "支付宝",etName.getText().toString().trim(),true);
    }

    @Override
    public void getInitData() {
        shareBiz.generalizeIndex();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPage = 1;
        accountBiz.inviteWithdrawRecord(mPage);

        HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
            @Override
            public void onSuccess(String uid, HnLoginModel model, String response) {
            }

            @Override
            public void onError(int errCode, String msg) {
                HnToastUtils.showCenterToast(msg);
            }
        });
    }

    private void setEmpty() {
        mHnLoadingLayout.setStatus(mData.size() < 1 ? HnLoadingLayout.Empty : HnLoadingLayout.Success);
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnMyAccountBiz.INVITE_WITHDRAW_RECORD.equals(type)) {
            HnInviteWithdrawLogModel model = (HnInviteWithdrawLogModel) obj;
            try {
                mSwipeRefresh.refreshComplete();
                HnInviteWithdrawLogModel.DBean d = model.getD();
                List<HnInviteWithdrawLogModel.DBean.ItemsBean> items = d.getItems();
//                mTvTotal.setText( HnUtils.setTwoPoints(model.getD().getAmount_total())+HnApplication.getmConfig().getDot());
                mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                if (mPage == 1) {
                    mData.clear();
                }
                mData.addAll(items);
                if (withdrawLogAdapter != null)
                    withdrawLogAdapter.notifyDataSetChanged();
                setEmpty();
                HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20, withdrawLogAdapter.getItemCount());

            } catch (Exception e) {
                setEmpty();
            }
        } else if (HnShareBiz.GENERALIZE_INDEX.equals(type)) {
            HnGeneralizeModel model = (HnGeneralizeModel) obj;
            initHeaderView(model.getD());
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnShareBiz.REWARD_LOG.equals(type)) {
            mSwipeRefresh.refreshComplete();
            setEmpty();
        } else {
            HnToastUtils.showToastShort(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
