package com.hotniao.svideo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.R;
import com.hotniao.livelibrary.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 文本输入框
 */
public class HnInputTextMsgDialog extends Dialog {

    public interface OnTextSendListener {

        void onTextSend(String msg, boolean isRepleyUser);
    }

    private boolean isRepleyUser = false;

    private TextView confirmBtn;
    private HnEditText messageTextView;

    private static final String TAG = HnInputTextMsgDialog.class.getSimpleName();
    private Context mContext;
    private InputMethodManager imm;
    private RelativeLayout rlDlg;
    private OnTextSendListener mOnTextSendListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public HnInputTextMsgDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_input_text_msg);

        LinearLayout rldlgview = (LinearLayout) findViewById(R.id.mLLInput);
        rlDlg = (RelativeLayout) findViewById(R.id.mRlInput);
        messageTextView = (HnEditText) findViewById(R.id.mEtComm);
        confirmBtn = (TextView) findViewById(R.id.mTvSend);

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTextView != null && imm != null) {
                    String msg = messageTextView.getText().toString().trim();
                    if (!TextUtils.isEmpty(msg)) {

                        mOnTextSendListener.onTextSend(msg, isRepleyUser);
                        imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                        messageTextView.setText("");
                    } else {
                        HnToastUtils.showToastShort("请输入发送内容");
                    }
                    messageTextView.setText(null);
                }
            }
        });


        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });


        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.mLLInput) {
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                }
            }
        });


        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //阀值设置为屏幕高度的1/3
                int keyHeight = screenHeight / 3;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//                    Toast.makeText(mContext, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                    Toast.makeText(mContext, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    /**
     * @param hintText
     * @param isReplyUser
     */
    public void setHintText(String hintText, boolean isReplyUser) {
        this.isRepleyUser = isReplyUser;
        if (messageTextView != null) {
            messageTextView.setHint(hintText);
        }

    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (imm != null)
            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
    }

    @Override
    public void show() {
        super.show();
    }
}
