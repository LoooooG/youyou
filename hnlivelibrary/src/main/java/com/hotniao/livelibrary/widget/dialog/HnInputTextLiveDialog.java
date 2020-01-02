package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnDimenUtil;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.bean.Emoji;
import com.hotniao.livelibrary.model.event.EmojiClickEvent;
import com.hotniao.livelibrary.model.event.EmojiDeleteEvent;
import com.hotniao.livelibrary.ui.fragment.HnEmojiFragment;
import com.hotniao.livelibrary.util.EmojiUtil;
import com.hotniao.livelibrary.util.HnLiveSystemUtils;
import com.hotniao.livelibrary.util.SystemUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;


/**
 * 文本输入框
 */
public class HnInputTextLiveDialog extends Dialog {

    public interface OnTextSendListener {

        void onTextSend(String msg, boolean isRepleyUser);
    }

    private boolean isDanmu = false;
    private String price = "10";

    private TextView confirmBtn;
    private EditText messageTextView;
    private ToggleButton mToggleButton;
    private ImageView mIvEmoji;

    private Context mContext;
    private InputMethodManager imm;
    private RelativeLayout rlDlg;
    private OnTextSendListener mOnTextSendListener;

    LinearLayout mContainer;
    //是否显示表情
    private boolean isShowEmoj = false;
    private boolean isShowDialog=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public HnInputTextLiveDialog(final BaseActivity context, int theme) {
        super(context, theme);

        mContext = context;
        setContentView(R.layout.dialog_input_text_live);

        LinearLayout rldlgview = (LinearLayout) findViewById(R.id.mLLInput);
        rlDlg = (RelativeLayout) findViewById(R.id.mRlInput);
        messageTextView = (EditText) findViewById(R.id.mEtComm);
        mToggleButton = (ToggleButton) findViewById(R.id.mTbtn);
        confirmBtn = (TextView) findViewById(R.id.mTvSend);
        mIvEmoji = (ImageView) findViewById(R.id.iv_emoji);
        mContainer = (LinearLayout) findViewById(R.id.mllEjom);


        mIvEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowEmoj) {
                    isShowEmoj = false;
                    mIvEmoji.setImageResource(R.mipmap.smile);
                    if (mContainer.getVisibility() == View.VISIBLE) {
                        if (SystemUtils.isKeyBoardShow(context)) {
                            messageTextView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mContainer != null)
                                        mContainer.setVisibility(View.GONE);
//                                    ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                    HnLiveSystemUtils.showKeyBoard(context, messageTextView);
                                }
                            }, 150L);
                        } else {
                            mContainer.setVisibility(View.GONE);
//                            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                    }
                    SystemUtils.showKeyBoard(messageTextView);
                } else {
                    isShowEmoj = true;
                    mIvEmoji.setImageResource(R.mipmap.keyboard);
                    SystemUtils.hideSoftInput(messageTextView);
                    messageTextView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mContainer != null)
                                mContainer.setVisibility(View.VISIBLE);
                        }
                    }, 150L);

                    context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }
        });

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDanmu = true;
                    messageTextView.setHint(price + HnBaseApplication.getmConfig().getCoin() + "/条");
                } else {
                    isDanmu = false;
                    messageTextView.setHint(R.string.live_input_chat_content);
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg, isDanmu);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    HnToastUtils.showToastShort("请输入发送内容");
                }
                messageTextView.setText(null);
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
                    if (mContainer != null)
                        mContainer.setVisibility(View.GONE);
                    if (mIvEmoji != null)
                        mIvEmoji.setImageResource(R.mipmap.smile);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    dismiss();
                }
            }
        });


        rlDlg.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //阀值设置为屏幕高度的1/3
                int keyHeight = screenHeight / 3;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    isShowDialog= false;
//                    Toast.makeText(mContext, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                    Toast.makeText(mContext, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
                    if (!isShowEmoj&&!isShowDialog) {
                        if (mContainer != null)
                            mContainer.setVisibility(View.GONE);
                        dismiss();
                    }


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


        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                      @Override
                                                      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                          if (actionId == EditorInfo.IME_ACTION_SEND&&messageTextView!=null&&context!=null) {
                                                              ((InputMethodManager) messageTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                      .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                                                                              InputMethodManager.HIDE_NOT_ALWAYS);
                                                              String messageContent = messageTextView.getText().toString().trim();
                                                              boolean mIsDanmu = mToggleButton.isChecked();
                                                              mOnTextSendListener.onTextSend(messageContent, mIsDanmu);
                                                              imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                                                              imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                                                              messageTextView.setText("");
                                                              return true;
                                                          }
                                                          return false;
                                                      }
                                                  }

        );
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(messageTextView.getText().toString()))
                    confirmBtn.setSelected(false);
                else confirmBtn.setSelected(true);
            }
        });
    }

    /**
     * @param hintText
     * @param isReplyUser
     */
    public void setHintText(String hintText, boolean isReplyUser) {
        this.isDanmu = isReplyUser;
        if (messageTextView != null) {
            messageTextView.setHint(hintText);
        }
    }

    public void setPrice(String price) {
        this.price = price;
        if (messageTextView != null && mToggleButton != null && mToggleButton.isChecked()) {
            messageTextView.setHint(price + HnBaseApplication.getmConfig().getCoin() + "/条");
        }
    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
        if (mContainer != null)
            mContainer.setVisibility(View.GONE);
        if (mIvEmoji != null)
            mIvEmoji.setImageResource(R.mipmap.smile);
        if (imm != null)
            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
        isShowEmoj = false;
        isShowDialog = false;

    }

    @Override
    public void show() {
        super.show();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (mContainer != null)
            mContainer.setVisibility(View.GONE);
        if (mIvEmoji != null)
            mIvEmoji.setImageResource(R.mipmap.smile);
        isShowEmoj = false;
        isShowDialog = true;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void emojiDelete(EmojiDeleteEvent deleteEvent) {
        String text = messageTextView.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                messageTextView.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            messageTextView.getText().delete(index, text.length());
            displayTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        messageTextView.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        displayTextView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void emojiClick(EmojiClickEvent event) {

        Emoji emoji = event.getEmoji();
        if (emoji != null) {
            int index = messageTextView.getSelectionEnd();
            Editable editable = messageTextView.getEditableText();
            editable.append(emoji.getContent());

        }
        displayTextView();
    }

    private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(messageTextView, messageTextView.getText().toString(), mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
