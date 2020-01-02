package com.hn.library.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hn.library.utils.HnToastUtils;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：有过滤特定符号功能的输入框
 * 创建人：Kevin
 * 创建时间：2016/5/6 15:09
 * 修改人：Kevin
 * 修改时间：2016/5/6 15:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFilterEditText extends EditText {
    /**
     * 控件是否有焦点
     */
    private boolean  hasFoucs;

    //输入表情前的光标位置
    private int     cursorPos;
    //输入表情前EditText中的文本
    private String  inputAfterText;
    //是否重置了EditText的内容

    private boolean resetText;
    private EmojiFilter mEmojiInputFilter = new EmojiFilter();

    public HnFilterEditText(Context context) {
        this(context, null);
    }

    public HnFilterEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public HnFilterEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initEditText();
    }

    private void initEditText() {
        this.setFilters(new InputFilter[]{this.mEmojiInputFilter});//emoji表情过滤
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    cursorPos = getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = s.toString();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    try {
                        if (count >= 2) {//表情符号的字符长度最小为2
                            CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                            if (containsEmoji(input.toString())) {
                                resetText = true;
                                HnToastUtils.showToastShort("不支持输入Emoji表情符号");
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                setText(inputAfterText);
                                CharSequence text = getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }


}