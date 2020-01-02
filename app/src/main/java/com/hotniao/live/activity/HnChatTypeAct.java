package com.hotniao.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.R;
import com.hotniao.live.biz.home.HnHomeCate;
import com.hotniao.live.dialog.HnHomeTabSkipActivityDialog;
import com.hotniao.live.model.HnChatTypeModel;
import com.hotniao.live.model.HnChatTypeModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 以 HnChooseLiveTypeActivity 为模版
 */
@Route(path = "/live/chooseLiveTypeActivity")
public class HnChatTypeAct extends BaseActivity {
    @BindView(R.id.mTvNowType)
    TextView mTvNowType;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mLoadingLayout)
    HnLoadingLayout mLoadingLayout;
    @BindView(R.id.mRG)
    RadioGroup mRG;
    @BindView(R.id.mRb1)
    RadioButton mRbLive;
    @BindView(R.id.mRb2)
    RadioButton mRbGame;
    @BindView(R.id.mTvHead)
    TextView mTvHead;
    @BindView(R.id.mTvHind)
    TextView mTvHind;

    private CommRecyclerAdapter mAdapter;
    private List<Object> mData = new ArrayList<>();
    private HnChatTypeModel.DBean mDbean;
    /**
     * 分类id   分类名
     */
    private String mSelectItem = "-1", mCateName;
    /**
     * 是否选择
     */
    private boolean isChange = false;


    /**
     * 跳转到选择直播类型页面
     *
     * @param activity
     * @param id       直播类型Id
     * @param type     直播类型
     */
    public static void luncher(Activity activity, String id, String type) {
        Intent intent = new Intent(activity, HnChatTypeAct.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, HnBeforeLiveSettingActivity.Choose_Cate_Code);

    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_live_type;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {

        setTitle("选择约聊类型");
        setShowBack(true);
        mLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mSelectItem = getIntent().getStringExtra("id");
        mCateName = getIntent().getStringExtra("type");
        mTvHead.setText("当前约聊类型：");
        mTvHind.setText("请您选择适合自己的分类，精准的分类可以获得更多的计划哦~");
        HnHomeCate.getChatTypeData();
        HnHomeCate.setOnCateListener(new HnHomeCate.OnCateListener() {
            @Override
            public void onSuccess() {
                if (mTvNowType != null)
                    mTvNowType.setText(HnHomeCate.getChatTypeName(mSelectItem));
            }

            @Override
            public void onError(int errCode, String msg) {

            }
        });
        mRG.setVisibility(View.GONE);
        mTvNowType.setGravity(Gravity.LEFT);
        mTvNowType.setVisibility(View.VISIBLE);

        if ("0".equals(mSelectItem) && HnHomeCate.mChatData.size() > 0) {
            mSelectItem = HnHomeCate.mChatData.get(0).getChat_category_id();
            mCateName = HnHomeCate.mChatData.get(0).getChat_category_name();
        }

        if (TextUtils.isEmpty(mCateName))
            mTvNowType.setText("无");
        else
            mTvNowType.setText(mCateName);

        mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.mRb1:
                        changeData(false);
                        break;
                    case R.id.mRb2:
                        changeData(true);
                        break;
                }
            }
        });

    }

    /**
     * 根据选择类型  改变数据源
     *
     * @param isGame
     */
    private void changeData(boolean isGame) {
        if (mDbean != null && mData != null) {
            mData.clear();
            mData.addAll(mDbean.getChat_category());
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getInitData() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                final HnChatTypeModel.DBean.ChatCategoryBean liveCategoryBean = (HnChatTypeModel.DBean.ChatCategoryBean) mData.get(position);
                ((TextView) holder.getView(R.id.mTvType)).setText(liveCategoryBean.getChat_category_name());
                if (mSelectItem.equals(liveCategoryBean.getChat_category_id())
                        && liveCategoryBean.getChat_category_name().equals(mCateName))
                    holder.getView(R.id.mIvChoose).setVisibility(View.VISIBLE);
                else holder.getView(R.id.mIvChoose).setVisibility(View.GONE);
                holder.getView(R.id.mRlItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isChange = true;
//                        setResult(1, new Intent().putExtra("id", liveCategoryBean.getChat_category_id())
//                                .putExtra("type", liveCategoryBean.getChat_category_name()));
                        EventBus.getDefault().post(liveCategoryBean);
                        finish();
                    }
                });
            }


            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_choose_live_type;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
        getCateData();
    }

    private void getCateData() {
        HnHttpUtils.postRequest(HnUrl.Chat_Type, null, HnUrl.Chat_Type, new HnResponseHandler<HnChatTypeModel>(HnChatTypeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (0 == model.getC() && model.getD() != null) {
                    mDbean = model.getD();
                    mData.clear();
                    /**
                     * 如果没有游戏分类  则只显示娱乐分类
                     */
                    mRG.setVisibility(View.GONE);
                    mTvNowType.setGravity(Gravity.LEFT);
                    mTvNowType.setVisibility(View.VISIBLE);
                    mData.addAll(model.getD().getChat_category());
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();

                } else {
                    HnToastUtils.showToastShort(model.getM());
                }

                setEmpty("暂无分类", R.drawable.empty_com);
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
                if (isFinishing()) return;

                setEmpty("暂无分类", R.drawable.empty_com);
            }
        });
    }


    protected void setEmpty(String content, int res) {
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() < 1) {
            mLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mLoadingLayout.setEmptyText(content);
        } else {
            mLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /**
         * 如果没有选择  并且不是游戏  则返回原来的数据
         * 考虑到游戏下架的情况
         */
        if (!isChange) {
            setResult(1, new Intent().putExtra("id", mSelectItem)
                    .putExtra("type", mCateName));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
