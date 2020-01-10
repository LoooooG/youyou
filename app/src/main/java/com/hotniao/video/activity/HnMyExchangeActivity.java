package com.hotniao.video.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnMyExchangeAdapter;
import com.hotniao.video.biz.user.account.HnMyExchangeBiz;
import com.hotniao.video.model.HnExchangeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;


/**
 * 兑换
 */

public class HnMyExchangeActivity extends BaseActivity implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;


    //我的账户业务逻辑类，我的账户相关业务
    private HnMyExchangeBiz mHnExchagneBiz;
    //充值列表适配器
    private HnMyExchangeAdapter mApdater;
    public Bundle bundle;

    private List<HnExchangeModel.DBean.ItemsBean> rechargeList = new ArrayList<>();


    @Override
    public int getContentViewId() {
        return R.layout.activity_exchange;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle("兑换金币");
        setShowSubTitle(true);
        mSubtitle.setText("兑换记录");
        mSubtitle.setTextColor(getResources().getColor(R.color.comm_text_color_white));
        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HnExchangeDetailActivity.class);
            }
        });
        mHnExchagneBiz = new HnMyExchangeBiz(this);
        mHnExchagneBiz.setBaseRequestStateListener(this);
        initAdpter();

    }

    @Override
    public void getInitData() {
        if (mHnExchagneBiz != null)
            mHnExchagneBiz.requestToMyExchange();
    }


    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        getInitData();
    }

    /**
     * 更新界面ui
     *
     * @param model
     */
    private void updateUi(HnExchangeModel model) {

        rechargeList = model.getD().getItems();

        //充值列表
        if (rechargeList != null && rechargeList.size() > 0) {
            mApdater.setNewData(rechargeList);
        }


    }

    /**
     * 初始化适配器
     */
    public void initAdpter() {
        if (mApdater == null) {
            mApdater = new HnMyExchangeAdapter(this);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            mRecyclerView.setAdapter(mApdater);
            mApdater.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    List<HnExchangeModel.DBean.ItemsBean> rechargeList = adapter.getData();
                    mApdater.notifyDataSetChanged();
                    mHnExchagneBiz.doExchange(rechargeList.get(position).getId());

                }
            });

        } else {
            mApdater.notifyDataSetChanged();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        HnLogUtils.i(TAG, "重新刷新界面数据");
        if (mHnExchagneBiz != null)
            mHnExchagneBiz.requestToMyExchange();
    }


    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.loading), null);
    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if (HnMyExchangeBiz.EXCHANGE_INFO.equals(type)) {
            HnExchangeModel model = (HnExchangeModel) obj;
            if (model != null && model.getD() != null) {
                setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
                updateUi(model);
            }
        } else if (HnMyExchangeBiz.EXCHANGE.equals(type)) {
            HnToastUtils.showToastShort("兑换成功");
        }
    }


    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null || isFinishing()) return;
        done();
        if (HnMyExchangeBiz.EXCHANGE_INFO.equals(type)) {
            if (REQUEST_NET_ERROR == code) {
                setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
            } else {
                setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
            }
        } else if (HnMyExchangeBiz.EXCHANGE.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

}
