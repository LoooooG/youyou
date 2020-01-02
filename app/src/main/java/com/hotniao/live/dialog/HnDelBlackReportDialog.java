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
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.eventbus.HnSelectLiveCateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：删除  拉黑   举报
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnDelBlackReportDialog extends DialogFragment implements View.OnClickListener {
    public static final int OnlyDelete = 1;
    public static final int NoDelete = 2;

    private Activity mActivity;
    private static HnDelBlackReportDialog dialog;

    private int mType;

    public static HnDelBlackReportDialog newInstance(int type, boolean isBlack) {
        dialog = new HnDelBlackReportDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putBoolean("isBlack", isBlack);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_del_black_report, null);
        mType = getArguments().getInt("type", OnlyDelete);
        if (OnlyDelete == mType) {
            view.findViewById(R.id.mTvReport).setVisibility(View.GONE);
            view.findViewById(R.id.mTvBlack).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.mTvDelete).setVisibility(View.GONE);
        }

        view.findViewById(R.id.mTvReport).setOnClickListener(this);
        view.findViewById(R.id.mTvCancel).setOnClickListener(this);
        TextView mTvBlack = view.findViewById(R.id.mTvBlack);
        if(getArguments().getBoolean("isBlack",false)){
           mTvBlack.setText("取消拉黑");
        }else {
            mTvBlack.setText("拉黑");
        }
        mTvBlack.setOnClickListener(this);
        view.findViewById(R.id.mTvDelete).setOnClickListener(this);


        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth());
//        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight());
        window.setAttributes(params);
        return dialog;
    }


    public HnDelBlackReportDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvReport:
                if (listener != null) listener.reportClick();
                break;

            case R.id.mTvBlack:
                if (listener != null) listener.blackClick();
                break;
            case R.id.mTvDelete:
                if (listener != null) listener.deleteClick();
                break;
            case R.id.mTvCancel:

                break;
        }
        dismiss();

    }

    public interface SelDialogListener {
        void deleteClick();

        void blackClick();

        void reportClick();
    }


}
