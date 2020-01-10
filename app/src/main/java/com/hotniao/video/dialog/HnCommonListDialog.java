package com.hotniao.video.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.video.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：火脉
 * 类描述：编辑性别
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnCommonListDialog extends AppCompatDialogFragment {

    @BindView(R.id.list)
    RecyclerView mRecycler;

    private static HnCommonListDialog sDialog;

    public static final int ALL = 0;
    public static final int SELF = 1;

    private BaseActivity mActivity;
    private CommRecyclerAdapter mAdapter;
    private List<String> options = new ArrayList<>();

    public static HnCommonListDialog newInstance(ArrayList<String> options) {
        Bundle args = new Bundle();
        args.putStringArrayList("options",options);
        sDialog = new HnCommonListDialog();
        sDialog.setArguments(args);
        return sDialog;
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> opt = getArguments().getStringArrayList("options");
        options.addAll(opt);
        View view = View.inflate(mActivity, R.layout.dialog_common_list, null);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        alertWindow.setAttributes(params);

        return dialog;
    }

    @OnClick({R.id.tv_cancel})
    public void onClick() {
        dialogDismiss();
    }

    /**
     * dialog消失
     */
    private void dialogDismiss() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    private void setupRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mRecycler.addItemDecoration(new HnSpacesItemDecoration(HnUtils.dip2px(getContext(),0.5f),false));
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(final BaseViewHolder holder, final int position) {
                final String option = options.get(position);
                holder.setTextViewText(R.id.tv_title,option);
                holder.getView(R.id.container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            listener.onItemClick(position);
                            dialogDismiss();
                        }
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_common_list;
            }

            @Override
            public int getItemCount() {
                return options.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
