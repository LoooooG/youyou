package com.hn.library.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.hn.library.HnBaseApplication;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：字符串工具类
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnStringUtils {

	private static Context mContext = HnBaseApplication.getContext();


	public final static String UTF_8 = "utf-8";

	/**
	 * 得到string.xml中的字符数组
	 */
	public static String[] getStrings(int resId) {
		return mContext.getResources().getStringArray(resId);
	}


	public static String getString(int strRes){
     return mContext.getString(strRes);
	}

	/**
	 * 返回一串文字固定位置文字高亮显示
	 *
	 * @param content
	 *            文本内容
	 * @param color
	 *            高亮颜色
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color,
												int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 返回一串文字中指定一个关键字高亮显示
	 * @param color 关键字颜色
	 * @param text 文本
	 * @param keyword 关键字
	 * @return
	 */
	public static CharSequence getHighLightText(int color, String text,
												String keyword) {
		SpannableString s = new SpannableString(text);
		Pattern p = Pattern.compile(keyword);
		Matcher m = p.matcher(s);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			s.setSpan(new ForegroundColorSpan(color), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return s;

	}

	/**
	 * 返回一串文字中指定多个关键字高亮显示
	 * @param color 关键字颜色
	 * @param text 文本
	 * @param keyword 多个关键字
	 * @return
	 */
	public static CharSequence getHighLightText(int color, String text,
												String[] keyword) {
		SpannableString s = new SpannableString(text);
		for (int i = 0; i < keyword.length; i++) {
			Pattern p = Pattern.compile(keyword[i]);
			Matcher m = p.matcher(s);
			while (m.find()) {
				int start = m.start();
				int end = m.end();
				s.setSpan(new ForegroundColorSpan(color), start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			}

		}
		return s;

	}


	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
						/ 1024 / (float) 100))
						+ "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
						+ "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
						/ 1024 / (float) 10))
						+ "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
						+ "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
					+ "GB";
		}
		return size;
	}
}
