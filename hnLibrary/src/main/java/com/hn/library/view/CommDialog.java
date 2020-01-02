package com.hn.library.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hn.library.R;
import com.hn.library.utils.HnDimenUtil;


public class CommDialog extends Dialog {

    public final static int ONE_STATE = 1;
    public final static int TWO_STATE = 2;
    public Context context;
    public TextView mTvContent, mTvLeft, mTvRight,mTvTitle;
    private int State = 1;
    private TwoSelDialog mTwoSelDialog;
    private OneSelDialog mOneSelDialog;
    private static CommDialog dialog;

    public interface TwoSelDialog {
        void leftClick();

        void rightClick();
    }

    public interface OneSelDialog {
        void sureClick();
    }

    public static CommDialog newInstance(Context context) {
        dialog = new CommDialog(context);
        return dialog;
    }

    public CommDialog(Context context) {
        super(context, R.style.PXDialog);
        this.context = context;
        this.State = TWO_STATE;
        init();
    }


    public CommDialog showDialog() {
        if (dialog != null) {
            dialog.show();
            return dialog;
        }
        return dialog;
    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comm_dialog, null);
        setContentView(view);
        mTvContent = (TextView) findViewById(R.id.mTvTitle);
        mTvLeft = (TextView) findViewById(R.id.mTvLeft);
        mTvTitle = (TextView) findViewById(R.id.hint);
        mTvRight = (TextView) findViewById(R.id.mTvRight);

        if (State == ONE_STATE) {

        } else {

        }

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = HnDimenUtil.dp2px(context, 300); // 宽度
//        lp.height = HnDimenUtil.dp2px(context,  170); // 高度
        dialogWindow.setAttributes(lp);
    }

    //设置标题
    public CommDialog setCanceledOnOutside(boolean isCancle) {
        if (dialog != null) {
            this.setCanceledOnTouchOutside(isCancle);
            return dialog;
        }
        return null;
    }
    //设置标题
    public CommDialog setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            return dialog;
        }
        return null;
    }


    //设置内容
    public CommDialog setContent(String content) {
        if (mTvContent != null) {
            mTvContent.setText(content);
            return dialog;
        }
        return null;
    }

    //设置左边文本 默认-取消
    public CommDialog setLeftText(String leftText) {
        if (mTvLeft != null) {
            mTvLeft.setText(leftText);
            return dialog;
        }
        return null;
    }

    //设置右边文本 默认-确认
    public CommDialog setRightText(String rightText) {
        if (mTvRight != null) {
            mTvRight.setText(rightText);
            return dialog;
        }
        return null;
    }

    //设置右边文本 默认-确认
    public CommDialog setRightText(String rightText, @ColorRes int color) {
        if (mTvRight != null) {
            mTvRight.setText(rightText);
            mTvRight.setTextColor(context.getResources().getColor(color));
            return dialog;
        }
        return null;
    }

    public CommDialog setClickListen(final OneSelDialog mOneSelDialog) {
        this.mOneSelDialog = mOneSelDialog;
        this.State = ONE_STATE;
        mTvLeft.setVisibility(View.GONE);
        //确定
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOneSelDialog != null)
                    mOneSelDialog.sureClick();
                if (CommDialog.this.isShowing())
                    CommDialog.this.dismiss();
            }
        });
        return dialog;
    }

    public CommDialog setClickListen(final TwoSelDialog mTwoSelDialog) {
        this.mTwoSelDialog = mTwoSelDialog;
        this.State = TWO_STATE;
        //取消
        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoSelDialog != null)
                    mTwoSelDialog.leftClick();
                if (CommDialog.this.isShowing())
                    CommDialog.this.dismiss();
            }
        });
        //确定
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoSelDialog != null)
                    mTwoSelDialog.rightClick();
                if (CommDialog.this.isShowing())
                    CommDialog.this.dismiss();
            }
        });
        return dialog;
    }
}
