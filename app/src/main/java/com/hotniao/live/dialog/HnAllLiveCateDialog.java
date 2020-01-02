package com.hotniao.live.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.eventbus.HnSelectLiveCateEvent;
import com.hotniao.live.model.HnHomeLiveCateModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：直播分类弹窗
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnAllLiveCateDialog extends DialogFragment{


    private Activity mActivity;
    private static HnAllLiveCateDialog dialog;
    private RecyclerView mRecycler;
    private static  List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mCate;

    public static HnAllLiveCateDialog newInstance(List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mTitles) {
        dialog = new HnAllLiveCateDialog();
        mCate=mTitles;
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

        mRecycler= (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new GridLayoutManager(mActivity,3));
        mRecycler.setHasFixedSize(true);

        mRecycler.setAdapter(new HnCateAdapter(mCate));

        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new HnSelectLiveCateEvent(HnSelectLiveCateEvent.SWITCH_CATE_LIVE,mCate.get(position).getAnchor_category_id(),position));
                dismiss();
            }
        });


        Dialog dialog = new Dialog(mActivity, R.style.dialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() );
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight());
        window.setAttributes(params);
        return dialog;
    }



    public HnAllLiveCateDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void sureClick();
    }


    class HnCateAdapter  extends BaseQuickAdapter<HnHomeLiveCateModel.DBean.LiveCategoryBean ,BaseViewHolder>{

        public HnCateAdapter(@Nullable List<HnHomeLiveCateModel.DBean.LiveCategoryBean> data) {
            super(R.layout.adapter_all_cate_dialog,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HnHomeLiveCateModel.DBean.LiveCategoryBean item) {
            if("-1".equals(item.getAnchor_category_id()) || "0".equals(item.getAnchor_category_id())){
                ((FrescoImageView)helper.getView(R.id.mIvImg)).setImageURI(Uri.parse("res://"+mContext.getPackageName()+"/"+R.drawable.hot_spot_classification));
            }else {
                ((FrescoImageView)helper.getView(R.id.mIvImg)).setController(FrescoConfig.getController(item.getAnchor_category_logo()));
            }

            ((TextView)helper.getView(R.id.mTvName)).setText(item.getAnchor_category_name());
        }
    }

}
