package com.hn.library.picker.date_picker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hn.library.R;
import com.hn.library.picker.wheel.OnWheelChangedListener;
import com.hn.library.picker.wheel.OnWheelScrollListener;
import com.hn.library.picker.wheel.WheelView;
import com.hn.library.picker.wheel.adapter.DateWheelTextAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：日期选择对话框
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnDatePickerDialog extends Dialog implements View.OnClickListener {

	private Activity mContext;
	private WheelView mWvYear;
	private WheelView mWvMonth;
	private WheelView mWvDay;

	private ImageButton mBtnSure;
	private ImageButton mBtnCancel;

	private ArrayList<String> mArrayYears = new ArrayList<>();
	private ArrayList<String> mArrayMonths = new ArrayList<>();
	private ArrayList<String> mArrayDays = new ArrayList<>();
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;

	private int mMonth;
	private int mDay;

	private int mCurrentYear = getYear();
	private int mCurrentMonth = 1;
	private int mCurrentDay = 1;

	private int mMaxTextSize = 18;
	private int mMinTextSize = 16;

	private boolean mIsSetData = false;

	private String mSelectYear;
	private String mSelectMonth;
	private String mSelectDay;

	private OnDatePickListener mOnDatePickListener;

	public HnDatePickerDialog(Activity context) {
		super(context, R.style.PXDialog);
		this.mContext = context;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_date_picker);
		mWvYear = (WheelView) findViewById(R.id.wv_birth_year);
		mWvMonth = (WheelView) findViewById(R.id.wv_birth_month);
		mWvDay = (WheelView) findViewById(R.id.wv_birth_day);
		mBtnSure = (ImageButton) findViewById(R.id.btn_date_ok);
		mBtnCancel = (ImageButton) findViewById(R.id.btn_date_cancel);

		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);

		if (!mIsSetData) {
			setDate(getYear(), getMonth(), getDay());
			this.mCurrentDay = 1;
			this.mCurrentMonth = 1;
		}
		initYears();
		mYearAdapter = new CalendarTextAdapter(mContext, mArrayYears, setYear(mCurrentYear), mMaxTextSize, mMinTextSize);
		mWvYear.setVisibleItems(3);
		mWvYear.setViewAdapter(mYearAdapter);
		mWvYear.setCurrentItem(setYear(mCurrentYear));

		initMonths(mMonth);
		mMonthAdapter = new CalendarTextAdapter(mContext, mArrayMonths, setMonth(mCurrentMonth), mMaxTextSize, mMinTextSize);
		mWvMonth.setVisibleItems(3);
		mWvMonth.setViewAdapter(mMonthAdapter);
		mWvMonth.setCurrentItem(setMonth(mCurrentMonth));

		initDays(mDay);
		mDaydapter = new CalendarTextAdapter(mContext, mArrayDays, mCurrentDay - 1, mMaxTextSize, mMinTextSize);
		mWvDay.setVisibleItems(3);
		mWvDay.setViewAdapter(mDaydapter);
		mWvDay.setCurrentItem(mCurrentDay - 1);

		//年份对应滚轮的监听
		mWvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				mSelectYear = currentText;
				setTextViewSize(currentText, mYearAdapter);
				mCurrentYear = Integer.parseInt(currentText);
				setYear(mCurrentYear);
				initMonths(mMonth);
				mMonthAdapter = new CalendarTextAdapter(mContext, mArrayMonths, 0, mMaxTextSize, mMinTextSize);
				mWvMonth.setVisibleItems(3);
				mWvMonth.setViewAdapter(mMonthAdapter);
				mWvMonth.setCurrentItem(0);
			}
		});

		mWvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextViewSize(currentText, mYearAdapter);
			}
		});

		//月份对应滚轮的监听
		mWvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				mSelectMonth = currentText;
				setTextViewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(currentText));
				initDays(mDay);
				mDaydapter = new CalendarTextAdapter(mContext, mArrayDays, 0, mMaxTextSize, mMinTextSize);
				mWvDay.setVisibleItems(3);
				mWvDay.setViewAdapter(mDaydapter);
				mWvDay.setCurrentItem(0);
			}
		});

		//天对应滚轮的监听
		mWvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextViewSize(currentText, mMonthAdapter);
			}
		});

		mWvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextViewSize(currentText, mDaydapter);
				mSelectDay = currentText;
			}
		});

		mWvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextViewSize(currentText, mDaydapter);
			}
		});

		Window alertWindow = getWindow();
		alertWindow.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams params = alertWindow.getAttributes();
		params.width =  mContext.getWindowManager().getDefaultDisplay().getWidth();
		alertWindow.setAttributes(params);
		setCanceledOnTouchOutside(true);

	}

	/**
	 * 初始化要显示的年数据
	 */
	public void initYears() {
		for (int i = getYear(); i > 1950; i--) {
			mArrayYears.add(i + "");
		}
	}

	/**
	 * 初始化要显示的月数据
	 * @param months
	 */
	public void initMonths(int months) {
		mArrayMonths.clear();
		for (int i = 1; i <= months; i++) {
			mArrayMonths.add(i + "");
		}
	}

	/**
	 * 初始化要显示的天数据
	 * @param days
	 */
	public void initDays(int days) {
		mArrayDays.clear();
		for (int i = 1; i <= days; i++) {
			mArrayDays.add(i + "");
		}
	}

	private class CalendarTextAdapter extends DateWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		public CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}


	@Override
	public void onClick(View v) {

		if (v == mBtnSure) {
			if (mOnDatePickListener != null) {
				mOnDatePickListener.onClick(mSelectYear, mSelectMonth, mSelectDay);
			}
		}
		dismiss();

	}

	public void setBirthdayListener(OnDatePickListener onDatePickListener) {
		this.mOnDatePickListener = onDatePickListener;
	}


	public interface OnDatePickListener {
		void onClick(String year, String month, String day);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param currentItemText
	 * @param adapter
	 */
	public void setTextViewSize(String currentItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (currentItemText.equals(currentText)) {
				textvew.setTextSize(mMaxTextSize);
				textvew.setTextColor(Color.BLACK);
			} else {
				textvew.setTextSize(mMinTextSize);
				textvew.setTextColor(Color.GRAY);
			}
		}
	}

	/**
	 * 设置天是否显示
	 * @param isShowDay
	 */
	public void setDayShow(boolean isShowDay){
		if(mWvDay!=null){
			mWvDay.setVisibility(isShowDay? View.VISIBLE: View.GONE);
		}
	}

	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}


	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day) {
		mSelectYear = year + "";
		mSelectMonth = month + "";
		mSelectDay = day + "";
		mIsSetData = true;
		this.mCurrentYear = year;
		this.mCurrentMonth = month;
		this.mCurrentDay = day;
		if (year == getYear()) {
			this.mMonth = getMonth();
		} else {
			this.mMonth = 12;
		}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
		if (year != getYear()) {
			this.mMonth = 12;
		} else {
			this.mMonth = getMonth();
		}
		for (int i = getYear(); i > 1950; i--) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份
	 * 
	 * @param month
	 * @return
	 */
	public int setMonth(int month) {
		int monthIndex = 0;
		calDays(mCurrentYear, month);
		for (int i = 1; i < this.mMonth; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}

	/**
	 * 计算每月多少天
	 * 
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		leayyear = year % 4 == 0 && year % 100 != 0;
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.mDay = 31;
				break;
			case 2:
				if (leayyear) {
					this.mDay = 29;
				} else {
					this.mDay = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.mDay = 30;
				break;
			}
		}
		if (year == getYear() && month == getMonth()) {
			this.mDay = getDay();
		}
	}
}