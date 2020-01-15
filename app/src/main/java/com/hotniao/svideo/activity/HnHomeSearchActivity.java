package com.hotniao.svideo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.R;
import com.hotniao.svideo.adapter.HnSearchHistoryAdapter;
import com.hotniao.svideo.adapter.HnSearchMatchAdapter;
import com.hotniao.svideo.biz.user.follow.HnFollowBiz;
import com.hotniao.svideo.db.HnSearchHistoryHelper;
import com.hotniao.svideo.model.HnHomeSearchModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：首页搜索
 * 创建人：刘龙龙
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeSearchActivity extends BaseActivity implements BaseRequestStateListener {
    private static final String TAG = "HnHomeSearchActivity";
    @BindView(R.id.search_tv_cancel)
    TextView mSearchCancel;
    @BindView(R.id.search_tv)
    TextView mSearchTv;
    @BindView(R.id.search_et)
    HnEditText mSearchEt;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.recycler_search)
    RecyclerView mRecyclerSearch;
   /* @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtrRefresh;*/
    @BindView(R.id.rl_delete_history)
    RelativeLayout mRlDelete;

    private String mSearchContent;

    private HnSearchHistoryAdapter mHistoryAdapter;
    private HnSearchMatchAdapter mMatchAdapter;

    private int mPage = 1;

    private HnFollowBiz mHnFollowBiz;
    private String mOwnUid;

    private boolean isCLickSearchHistory=false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_homesearch;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {

        setShowTitleBar(false);
        HnSearchHistoryHelper.getInstance();

        mRecyclerSearch.setLayoutManager(new LinearLayoutManager(this));

        mMatchAdapter = new HnSearchMatchAdapter();
        mHistoryAdapter = new HnSearchHistoryAdapter();

        mHnFollowBiz = new HnFollowBiz(this);
        mHnFollowBiz.setRegisterListener(this);

        mOwnUid = HnPrefUtils.getString(NetConstant.User.UID, "");

        setListener();
    }


    /**
     * 设置搜索监听器
     */
    private void setListener() {


       /* mPtrRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                onFuzzySearch(mSearchContent);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

            }
        });*/


        RxTextView.textChanges(mSearchEt)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        mSearchContent = charSequence.toString();
                        HnLogUtils.d("mSearchContent= " + mSearchContent);

                        boolean isEmpty = TextUtils.isEmpty(mSearchContent);

//                        mSearchCancel.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
//                        mSearchTv.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                        mRlDelete.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                        isHintHistory();
//                        mPtrRefresh.setMode(isEmpty ? PtrFrameLayout.Mode.NONE : PtrFrameLayout.Mode.LOAD_MORE);
                        if (isEmpty) {
                            getInitData();
                            return;
                        }

                        mRecyclerSearch.setAdapter(mMatchAdapter);
                        mPage = 1;
                        onFuzzySearch(mSearchContent);
                    }
                });


        // 搜索历史记录条目点击
        mHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String item = (String) adapter.getItem(position);
                mSearchEt.setText(item);
                // mSearchEt.setSelection(item.length());
                //点击搜索到的数据加入本地历史数据库
                HnSearchHistoryHelper.getInstance().insert(item);
                HnLogUtils.d(TAG,"mHistoryAdapter click");
                isCLickSearchHistory = true;
            }
        });


        //搜索结果条目点击
        mMatchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HnHomeSearchModel.DBean.ItemsBean item = (HnHomeSearchModel.DBean.ItemsBean) adapter.getItem(position);
                //点击搜索到的数据加入本地历史数据库
                HnSearchHistoryHelper.getInstance().insert(item.getContent());
                startActivity(new Intent(HnHomeSearchActivity.this, HnSearchUserOrLiveActivity.class).putExtra("keyWord", item.getContent()));
                HnLogUtils.d(TAG,"mMatchAdapter click");
            }
        });

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        ((InputMethodManager) mSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                                        mSearchContent = mSearchEt.getText().toString().trim();
                                                        onFuzzySearch(mSearchContent);
                                                        HnSearchHistoryHelper.getInstance().insert(mSearchContent);
                                                        startActivity(new Intent(HnHomeSearchActivity.this, HnSearchUserOrLiveActivity.class).putExtra("keyWord", mSearchContent));
                                                        HnLogUtils.d(TAG,"onEditorAction");
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }

        );


    }


    @Override
    public void getInitData() {

        mRecyclerSearch.setAdapter(mHistoryAdapter);
        ArrayList<String> historyLists = HnSearchHistoryHelper.getInstance().getHistoryLists();

        mHistoryAdapter.setNewData(historyLists);

        HnLogUtils.d("historyLists=" + historyLists.toString());
        isHintHistory();
    }

    /**
     * 判断是否显示历史删除
     */
    private void isHintHistory() {
        if (mHistoryAdapter != null && mHistoryAdapter.getItemCount() > 0 && TextUtils.isEmpty(mSearchContent)) {
            mRlDelete.setVisibility(View.VISIBLE);
        } else {
            mRlDelete.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.search_tv_cancel, R.id.search_tv, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv_cancel:
                finish();
                break;
            case R.id.search_tv:
                HnUtils.hideSoftInputFrom(mSearchEt, this);
                mSearchContent = mSearchEt.getText().toString().trim();
                onFuzzySearch(mSearchContent);
                HnSearchHistoryHelper.getInstance().insert(mSearchContent);
                break;
            case R.id.tv_delete:
                CommDialog.newInstance(HnHomeSearchActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {

                    }

                    @Override
                    public void rightClick() {
                        HnSearchHistoryHelper.getInstance().clearDataBase();
                        mHistoryAdapter.setNewData(null);
                        isHintHistory();
                    }
                }).setTitle(HnUiUtils.getString(R.string.search_history)).setContent(HnUiUtils.getString(R.string.sure_clear_search_history_record)).show();


                break;
        }
    }


    /**
     * 模糊搜索
     *
     * @param lastKey
     */
    private void onFuzzySearch(String lastKey) {
        if (TextUtils.isEmpty(lastKey)) return;
        RequestParam param = new RequestParam();
        param.put("kw", lastKey);
        param.put("page", mPage + "");
        param.put("pagesize", 20 + "");
        HnHttpUtils.getRequest(HnUrl.SEARCH_GET_RECOMMEND, param, TAG, new HnResponseHandler<HnHomeSearchModel>(this, HnHomeSearchModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                try {
//                    closeRefresh(mPtrRefresh);
                    List<HnHomeSearchModel.DBean.ItemsBean> items = model.getD().getItems();
                    HnLogUtils.d(TAG,"items.size:"+items.size()+",isCLickSearchHistory:"+isCLickSearchHistory);
                    if (isCLickSearchHistory && items.size() == 1) {
                        isCLickSearchHistory = false;
                        startActivity(new Intent(HnHomeSearchActivity.this, HnSearchUserOrLiveActivity.class).putExtra("keyWord", items.get(0).getContent()));
                    } else {
                        mMatchAdapter.setKeyword(mSearchContent);
                        if (mPage == 1) {
                            mMatchAdapter.setNewData(items);
                        } else {
                            mMatchAdapter.addData(items);
                        }
                    }
                    mPage++;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
//                closeRefresh(mPtrRefresh);
            }
        });

    }


    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {

        HnLogUtils.d("type=" + type + " response=" + response);
        if ("cancelFollow".equals(type)) {//取消关注
            int pos = (int) obj;
//            mMatchAdapter.getItem(pos).is_follow = "0";
            mMatchAdapter.notifyItemChanged(pos);
        } else if ("addFollow".equals(type)) {//添加关注
            int pos = (int) obj;
//            mMatchAdapter.getItem(pos).is_follow = "1";
            mMatchAdapter.notifyItemChanged(pos);
        }

    }

    @Override
    public void requestFail(String type, int code, String msg) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}