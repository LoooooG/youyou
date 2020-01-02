package com.hn.library.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import com.hn.library.HnBaseApplication;

/**
 * Created by angcyo on 15-12-16 016 15:51 下午.
 */
public class ClipBoardUtil {

    /**
     * 将文本复制到剪切板
     *
     * @param text    the text
     */
    public static void to( CharSequence text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) HnBaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(text, text);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) HnBaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        }
    }

    /**
     * 从剪切板获取文本
     *
     * @param context the context
     * @return the char sequence
     */
    public static CharSequence from(Context context) {
        CharSequence clip = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                clip = primaryClip.getItemAt(0).getText();
            }
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip = clipboard.getText();
        }
        return clip;
    }


}
