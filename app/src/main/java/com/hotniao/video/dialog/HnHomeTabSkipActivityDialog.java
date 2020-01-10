package com.hotniao.video.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.tab.listener.CustomTabEntity;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnSpacesItemGriddingDecoration;
import com.hotniao.video.R;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.model.HnHomeLiveCateModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：直播分类弹窗跳转
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeTabSkipActivityDialog extends DialogFragment {


    private Activity mActivity;
    private static HnHomeTabSkipActivityDialog dialog;
    private RecyclerView mRecycler;
    private static List<CustomTabEntity> mCate;

    public static HnHomeTabSkipActivityDialog newInstance(List<CustomTabEntity> mTitles) {
        dialog = new HnHomeTabSkipActivityDialog();
        mCate = mTitles;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_all_cate, null);
        view.findViewById(R.id.mViewCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.mView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.mLlClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);


        GridLayoutManager gridManager = new GridLayoutManager(mActivity, 4);
        gridManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new HnSpacesItemGriddingDecoration().setDividerWith(20));
        mRecycler.setLayoutManager(gridManager);

        mRecycler.setAdapter(new HnCateAdapter(mCate));

        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (listener != null) {
                    listener.sureClick(position);
                }
                dismiss();
            }
        });


        Dialog dialog = new Dialog(mActivity, R.style.topDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth());
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight());
        window.setAttributes(params);
        return dialog;
    }


    public HnHomeTabSkipActivityDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void sureClick(int position);
    }


    class HnCateAdapter extends BaseQuickAdapter<CustomTabEntity, BaseViewHolder> {

        public HnCateAdapter(@Nullable List<CustomTabEntity> data) {
            super(R.layout.adapter_game_tab, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CustomTabEntity item) {

            ((TextView) helper.getView(R.id.mTvTab)).setText(item.getTabTitle());
        }
    }

}
