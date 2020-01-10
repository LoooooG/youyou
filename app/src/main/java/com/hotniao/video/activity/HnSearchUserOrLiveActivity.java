package com.hotniao.video.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.video.R;
import com.hotniao.video.eventbus.HnSearchEvent;
import com.hotniao.video.fragment.search.HnSearchLiveFragment;
import com.hotniao.video.fragment.search.HnSearchUserFragment;
import com.hotniao.video.utils.HnUiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：搜索
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSearchUserOrLiveActivity extends BaseActivity {
    @BindView(R.id.mEtSearch)
    HnEditText mEtSearch;

    private String mSearchContent;
    private List<String> mTitles;
    private List<Fragment> mFragments;


    @Override
    public int getContentViewId() {
        return R.layout.activity_search_user_live;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        HnSearchUserFragment fragment = HnSearchUserFragment.getInstance();
        Bundle bundle=new Bundle();
        bundle.putString("keyWord",getIntent().getStringExtra("keyWord"));
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commitAllowingStateLoss();

        mEtSearch.setText(getIntent().getStringExtra("keyWord"));
    }


    @Override
    public void getInitData() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        ((InputMethodManager) mEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                .hideSoftInputFromWindow(HnSearchUserOrLiveActivity.this.getCurrentFocus().getWindowToken(),
                                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                                        String content = mEtSearch.getText().toString().trim();
                                                        if (TextUtils.isEmpty(content)) {
                                                            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.please_input_search_content));
                                                        } else {
                                                            EventBus.getDefault().post(new HnSearchEvent(content));
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }

        );
    }


    @OnClick({R.id.mTvCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvCancel:
                finish();
                break;

        }
    }


}
