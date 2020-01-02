package com.hn.library.indexlayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class PinyinUtil {
    private static final String PATTERN_POLYPHONE = "^#[a-zA-Z]+#.+";
    private static final String PATTERN_LETTER = "^[a-zA-Z].*+";

    /**
     *
     * 输入汉字返回拼音的通用方法函数。
     */
    public static String getPinYin(String inputString) {
        if (inputString == null) return "";
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(inputString);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (HanziToPinyin.Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }

        return sb.toString().toUpperCase();
    }

    /**
     * Are start with a letter
     *
     * @return if return false, index should be #
     */
    static boolean matchingLetter(String inputString) {
        return Pattern.matches(PATTERN_LETTER, inputString);
    }

    static boolean matchingPolyphone(String inputString) {
        return Pattern.matches(PATTERN_POLYPHONE, inputString);
    }

    static String gePolyphoneInitial(String inputString) {
        return inputString.substring(1, 2);
    }

    static String getPolyphoneRealPinyin(String inputString) {
        String[] splits = inputString.split("#");
        return splits[1];
    }

    static String getPolyphoneRealHanzi(String inputString) {
        String[] splits = inputString.split("#");
        return splits[2];
    }
}
