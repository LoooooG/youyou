package com.hn.library.picker.address_picker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.hn.library.R;
import com.hn.library.picker.wheel.OnWheelChangedListener;
import com.hn.library.picker.wheel.OnWheelClickedListener;
import com.hn.library.picker.wheel.WheelView;
import com.hn.library.picker.wheel.adapter.AddressWheelTextAdapter;
import com.hn.library.utils.HnLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：IndianaLive
 * 类描述：城市选择对话框
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class AddressDialog extends Dialog {

	private final static int DEFAULT_ITEMS = 5;
	private final static int UPDATE_CITY_WHEEL = 11;
	private final static int UPDATE_COUNTY_WHEEL = 12;

	private Activity mContext;

	private ArrayList<Province> mProvinces = new ArrayList<Province>();
	private ArrayList<City>     mCities    = new ArrayList<City>();
	private ArrayList<County>   mCounties  = new ArrayList<County>();
	AddressWheelTextAdapter provinceAdapter;
	AddressWheelTextAdapter cityAdapter;
	AddressWheelTextAdapter countyAdapter;
	WheelView provinceWheel;
	WheelView citiesWheel;
	WheelView countiesWheel;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!isShowing()) {
				return;
			}
			switch (msg.what) {
			case UPDATE_CITY_WHEEL:
				mCities.clear();
				mCities.addAll(mProvinces.get(msg.arg1).getCity());
				citiesWheel.invalidateWheel(true);
				citiesWheel.setCurrentItem(0, false);

				mCounties.clear();
				mCounties.addAll(mCities.get(0).getDict());
				countiesWheel.invalidateWheel(true);
				countiesWheel.setCurrentItem(0, false);
				break;
			case UPDATE_COUNTY_WHEEL:
				mCounties.clear();
				mCounties.addAll(mCities.get(msg.arg1).getDict());
				countiesWheel.invalidateWheel(true);
				countiesWheel.setCurrentItem(0, false);
				break;
			default:
				break;
			}
		}
	};
	private OnWheelChangedListener mProvinceChangeListener;
	private OnWheelChangedListener mCitiesChangeListener;

	public  interface onCityPickedListener {
		 void onPicked(Province selectProvince, City selectCity,
					   County selectCounty);
	}


	public AddressDialog(Activity context, List<Province> provinces, Province defaultProvince, City defaultCity, County defaultCounty,
						 boolean isShowCounty, final onCityPickedListener listener) {
		super(context);
		mContext = context;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#00000000")));
		getWindow().setWindowAnimations(R.style.AnimBottom);
		View rootView = getLayoutInflater().inflate(R.layout.dialog_address_picker, null);
		int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params = new LayoutParams(screenWidth, LayoutParams.MATCH_PARENT);
		super.setContentView(rootView, params);

		mProvinces.addAll(provinces);
		initViews(isShowCounty);
		setDefaultArea(defaultProvince, defaultCity, defaultCounty);

		View done = findViewById(R.id.done);
		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					Province province = mProvinces.size() > 0 ? mProvinces
							.get(provinceWheel.getCurrentItem()) : new Province();
					City city = mCities.size() > 0 ? mCities.get(citiesWheel
							.getCurrentItem()) : new City();
					County county = mCounties.size() > 0 ? mCounties
							.get(countiesWheel.getCurrentItem()) : new County();
					listener.onPicked(province, city, county);
				}
				dismiss();
			}
		});

		View cancel = findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		TextView title = (TextView) findViewById(R.id.tv_city);
		if(isShowCounty){
			title.setText("省市区");
		}else {
			title.setText("省市");
		}

	}

	/**
	 * 设置默认选择区域
	 * @param defaultProvince
	 * @param defaultCity
	 * @param defaultCounty
	 */
	private void setDefaultArea(Province defaultProvince, City defaultCity, County defaultCounty) {

		int provinceItem = 0;
		int cityItem = 0;
		int countyItem = 0;

		if (defaultProvince == null) {
			defaultProvince = mProvinces.get(0);
			provinceItem = 0;
		} else {
			for (int i = 0; i < mProvinces.size(); i++) {
				if (mProvinces.get(i).getRegion_name()
						.equals(defaultProvince.getRegion_name())) {
					provinceItem = i;
					break;
				}
			}
		}
		mCities.clear();
		mCities.addAll(defaultProvince.getCity());

		if (mCities.size() == 0) {
			mCities.add(new City());
			cityItem = 0;
			HnLogUtils.i("mCities.size() == 0");
		} else if (defaultCity == null) {
			defaultCity = mCities.get(0);
			cityItem = 0;
			HnLogUtils.i("defaultCity == null");
		} else {
			for (int i = 0; i < mCities.size(); i++) {
				if (mCities.get(i).getRegion_name().equals(defaultCity.getRegion_name())) {
					cityItem = i;
					break;
				}
			}
		}

		mCounties.clear();
		mCounties.addAll(defaultCity.getDict());
		if (mCounties.size() == 0) {
			mCounties.add(new County());
			countyItem = 0;
		} else if (defaultCounty == null) {
			defaultCounty = mCounties.get(0);
			countyItem = 0;
		} else {
			for (int i = 0; i < mCounties.size(); i++) {
				if (mCounties.get(i).getRegion_name()
						.equals(defaultCounty.getRegion_name())) {
					countyItem = i;
					break;
				}
			}
		}

		provinceWheel.setCurrentItem(provinceItem, false);
		citiesWheel.setCurrentItem(cityItem, false);
		countiesWheel.setCurrentItem(countyItem, false);

		mProvinceChangeListener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				mHandler.removeMessages(UPDATE_CITY_WHEEL);
				Message msg = Message.obtain();
				msg.what = UPDATE_CITY_WHEEL;
				msg.arg1 = newValue;
				mHandler.sendMessageDelayed(msg, 50);
			}
		};
		provinceWheel.addChangingListener(mProvinceChangeListener);

		mCitiesChangeListener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				mHandler.removeMessages(UPDATE_COUNTY_WHEEL);
				Message msg = Message.obtain();
				msg.what = UPDATE_COUNTY_WHEEL;
				msg.arg1 = newValue;
				mHandler.sendMessageDelayed(msg, 50);
			}
		};

		citiesWheel.addChangingListener(mCitiesChangeListener);

	}


	private void initViews(boolean isShowCounty) {

		provinceWheel = (WheelView) findViewById(R.id.provinceWheel);
		citiesWheel = (WheelView) findViewById(R.id.citiesWheel);
		countiesWheel = (WheelView) findViewById(R.id.countiesWheel);
		countiesWheel.setVisibility(isShowCounty?View.VISIBLE:View.GONE);
		
		provinceAdapter = new AddressWheelTextAdapter(mContext,
				R.layout.wheel_text) {

			@Override
			public int getItemsCount() {

				return mProvinces.size();
			}

			@Override
			protected CharSequence getItemText(int index) {

				return mProvinces.get(index).getRegion_name();
			}
		};

		cityAdapter = new AddressWheelTextAdapter(mContext,
				R.layout.wheel_text) {

			@Override
			public int getItemsCount() {

				return mCities.size();
			}

			@Override
			protected CharSequence getItemText(int index) {

				return mCities.get(index).getRegion_name();
			}
		};

		countyAdapter = new AddressWheelTextAdapter(mContext,
				R.layout.wheel_text) {

			@Override
			public int getItemsCount() {

				return mCounties.size();
			}

			@Override
			protected CharSequence getItemText(int index) {

				return mCounties.get(index).getRegion_name();
			}
		};

		provinceWheel.setViewAdapter(provinceAdapter);
		provinceWheel.setCyclic(false);
		provinceWheel.setVisibleItems(DEFAULT_ITEMS);

		citiesWheel.setViewAdapter(cityAdapter);
		citiesWheel.setCyclic(false);
		citiesWheel.setVisibleItems(DEFAULT_ITEMS);

		countiesWheel.setViewAdapter(countyAdapter);
		countiesWheel.setCyclic(false);
		countiesWheel.setVisibleItems(DEFAULT_ITEMS);

		OnWheelClickedListener clickListener = new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				if (itemIndex != wheel.getCurrentItem()) {
					wheel.setCurrentItem(itemIndex, true, 500);
				}
			}
		};

		provinceWheel.addClickingListener(clickListener);
		citiesWheel.addClickingListener(clickListener);
		countiesWheel.addClickingListener(clickListener);

	}
}
