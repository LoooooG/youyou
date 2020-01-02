package com.videolibrary.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.videolibrary.R;


/**
 * Created by hanszhli on 2017/6/5.
 */

public class VideoWorkProgressFragment extends DialogFragment {
    private View mContentView;
    private DismissListener mListener;
    private TextView mTvTitle;
    private String mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(R.style.ConfirmDialogStyle, R.style.DialogFragmentStyle);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.layout_joiner_progress, null);
        mTvTitle = mContentView.findViewById(R.id.mTvTitle);
        if (!TextUtils.isEmpty(mContent)) mTvTitle.setText(mContent);
        return mContentView;
    }

    /**
     * 设置停止按钮的监听
     *
     * @param listener
     */
    public void setOnClickStopListener(DismissListener listener) {
        mListener = listener;
    }

    public void setTitle(String title) {
        mContent = title;
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }

    }


    private DismissListener listener;

    public interface DismissListener {
        void dismissListener();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
        FragmentTransaction transaction = manager.beginTransaction();
        if (this.isAdded()) {
            transaction.remove(this).commit();
        }
        transaction.add(this, tag);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void dismiss() {
        // 和show对应
        if (getFragmentManager() != null && getFragmentManager().beginTransaction() != null && isAdded()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(this);
            transaction.commitAllowingStateLoss();
            if (mListener != null) mListener.dismissListener();
        }
    }
}
