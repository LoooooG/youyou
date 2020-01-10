package com.hotniao.video.dialog;

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
import com.hotniao.video.R;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.model.HnChatTypeModel;
import com.hotniao.video.model.HnChatTypeModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

public class HnChatTypeDialog extends DialogFragment {


    private static HnChatTypeDialog dialog;
    private static List<HnChatTypeModel.DBean.ChatCategoryBean> mCate = new ArrayList<>();
    private Activity mActivity;
    private RecyclerView mRecycler;
    private SelDialogListener listener;

    public static HnChatTypeDialog newInstance(List<HnChatTypeModel.DBean.ChatCategoryBean> title) {
        dialog = new HnChatTypeDialog();
        mCate.clear();
        mCate.addAll(title);
        mCate.remove(0);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_chat_type, null);
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
        mRecycler.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRecycler.setHasFixedSize(true);

        mRecycler.setAdapter(new HnCateAdapter(mCate));

        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new HnSelectLiveCateEvent(HnSelectLiveCateEvent.SWITCH_CHAT_TYPE, mCate.get(position).getChat_category_id(), position));
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

    public HnChatTypeDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    public interface SelDialogListener {
        void sureClick();
    }


    class HnCateAdapter extends BaseQuickAdapter<HnChatTypeModel.DBean.ChatCategoryBean, BaseViewHolder> {

        public HnCateAdapter(@Nullable List<HnChatTypeModel.DBean.ChatCategoryBean> data) {
            super(R.layout.adapter_all_cate_dialog, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HnChatTypeModel.DBean.ChatCategoryBean item) {
            if ("-1".equals(item.getChat_category_id())) {
                ((FrescoImageView) helper.getView(R.id.mIvImg)).setImageURI(Uri.parse("res://" + mContext.getPackageName() + "/" + R.drawable.hot_spot_classification));
            } else if ("-2".equals(item.getChat_category_id())) {
                remove(0);
            } else {
                ((FrescoImageView) helper.getView(R.id.mIvImg)).setController(FrescoConfig.getController(item.getChat_category_logo()));
            }

            ((TextView) helper.getView(R.id.mTvName)).setText(item.getChat_category_name());
        }
    }

}
