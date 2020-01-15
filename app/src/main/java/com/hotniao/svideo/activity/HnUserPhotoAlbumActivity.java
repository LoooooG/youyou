package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnSpacesItemGriddingDecoration;
import com.hotniao.svideo.R;
import com.hotniao.svideo.dialog.HnEditHeaderDialog;
import com.hotniao.svideo.model.HnLocalImageModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：相册
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserPhotoAlbumActivity extends BaseActivity {


    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;


    private CommRecyclerAdapter mAdapter;
    private List<HnLocalImageModel> mData = new ArrayList<>();
    private List<String> mDataUrl = new ArrayList<>();

    private HnEditHeaderDialog mHeaderDialog;


    public static void luncher(Activity activity, String iamges) {
        activity.startActivity(new Intent(activity, HnUserPhotoAlbumActivity.class).putExtra("images", iamges));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_photo_album;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setShowTitleBar(true);
        setTitle(R.string.photo_album);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mRecycler.getLayoutParams();
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
    }

    @Override
    public void getInitData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("images"))) {
            String[] images = getIntent().getStringExtra("images").split(",");
            mData.clear();
            mDataUrl.clear();
            for (int i = 0; i < images.length; i++) {
                HnLocalImageModel image = new HnLocalImageModel(images[i], "url");
                mData.add(image);
                mDataUrl.add(images[i]);
            }
            images = null;
        }


        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.addItemDecoration(new HnSpacesItemGriddingDecoration().setDividerWith(8));
        mRecycler.setLayoutManager(manager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                HnLocalImageModel item = mData.get(position);
                ((FrescoImageView) holder.getView(R.id.image)).setController(FrescoConfig.getController(item.getUrl()));


                holder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launcher(view, position, mDataUrl);
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_user_photo_album;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);

        setEmpty("TA还没有相册哦~",R.drawable.empty_com);
    }

    protected void setEmpty(String content, int res) {
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() < 1) {
            mHnLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mHnLoadingLayout.setEmptyText(content).setEmptyImage(res);
        } else {
            mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

    public void launcher(View view, int position, List<String> data) {
        Intent intent = new Intent(this, HnPhotoPagerActivity.class);
        Bundle args = new Bundle();
        args.putInt(HnPhotoPagerActivity.KEY_START_POS, Math.min(data.size(), Math.max(0, position)));

        int[] rt = new int[2];
        view.getLocationOnScreen(rt);

        float w = view.getMeasuredWidth();
        float h = view.getMeasuredHeight();

        float x = rt[0] + w / 2;
        float y = rt[1] + h / 2;

        args.putInt(HnPhotoPagerActivity.KEY_START_X, rt[0]);
        args.putInt(HnPhotoPagerActivity.KEY_START_Y, rt[1]);
        args.putInt(HnPhotoPagerActivity.KEY_START_W, (int) w);
        args.putInt(HnPhotoPagerActivity.KEY_START_H, (int) h);
        args.putStringArrayList(HnPhotoPagerActivity.KEY_PHOTO_LIST, (ArrayList<String>) data);

        intent.putExtras(args);
        startActivity(intent);
    }

}
