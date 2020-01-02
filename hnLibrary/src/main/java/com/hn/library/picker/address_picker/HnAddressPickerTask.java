package com.hn.library.picker.address_picker;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hn.library.utils.HnLogUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.util.EncodingUtils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：IndianaLive
 * 类描述：地址初始化异步任务
 * 创建人：Kevin
 * 创建时间：2016/9/27 14:22
 * 修改人：Kevin
 * 修改时间：2016/9/27 14:22
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAddressPickerTask extends AsyncTask<Integer, Integer, Boolean> {

    private ArrayList<Province> mProvinces = new ArrayList<Province>();
    private ArrayList<City>     mCities    = new ArrayList<City>();
    private ArrayList<County>   mCountys   = new ArrayList<County>();

    private Activity mContext;
    // 要设置的文本
    private TextView mTv;
    private Province selectProvince;
    private City     selectCity;
    private County   selectCounty;

    public County getSelectCounty() {
        return selectCounty;
    }

    public void setSelectCounty(County selectCounty) {
        this.selectCounty = selectCounty;
    }

    public City getSelectCity() {
        return selectCity;
    }

    public void setSelectCity(City selectCity) {
        this.selectCity = selectCity;
    }

    public Province getSelectProvince() {
        return selectProvince;
    }

    public void setSelectProvince(Province selectProvince) {
        this.selectProvince = selectProvince;
    }


    public HnAddressPickerTask(Activity context, TextView tv) {
        mContext = context;
        mTv = tv;
    }


    @Override
    protected void onPostExecute(Boolean result) {

    }

    /**
     * 子线程中去解析处理json数据
     *
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(Integer... params) {
        String address = null;
        InputStream in = null;
        try {
            in = mContext.getResources().getAssets().open("city_list.json");
            byte[] arrayOfByte = new byte[in.available()];
            in.read(arrayOfByte);
            address = EncodingUtils.getString(arrayOfByte, "UTF-8");
            JSONArray jsonList = new JSONArray(address);
            Gson gson = new Gson();
            for (int i = 0; i < jsonList.length(); i++) {
                try {
                    mProvinces.add(gson.fromJson(jsonList.getString(i), Province.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * 显示地址dialog
     */
    public void showAddressDialog(Province defaultProvince, City defaultCity, County defaultCounty, final boolean isShowCounty, final onPickedListener listener) {

        if (mProvinces.size() == 0) {
            Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < mProvinces.size(); i++) {
            if (defaultProvince == null) {
                break;
            }
            if (defaultProvince.getRegion_name().equals(mProvinces.get(i).getRegion_name())) {
                selectProvince = mProvinces.get(i);
                mCities.clear();
                mCities.addAll(selectProvince.getCity());
                break;
            }
        }


        for (int i = 0; i < mCities.size(); i++) {
            if (defaultCity == null) {
                break;
            }
            if (defaultCity.getRegion_name().equals(mCities.get(i).getRegion_name())) {
                selectCity = mCities.get(i);
                mCountys.clear();
                mCountys.addAll(selectCity.getDict());
                break;
            }
        }

        for (int i = 0; i < mCountys.size(); i++) {
            if (defaultCounty == null) {
                break;
            }
            if (defaultCounty.getRegion_name().equals(mCountys.get(i).getRegion_name())) {
                selectCounty = mCountys.get(i);
                HnLogUtils.i("selectCounty=" + selectCounty.getRegion_name());
                break;
            }
        }

        new AddressDialog(mContext, mProvinces, selectProvince, selectCity, selectCounty, isShowCounty,
                new AddressDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(Province selectProvince, City selectCity, County selectCounty) {

                        if (selectProvince != null && selectCity != null) {

                            HnAddressPickerTask.this.selectProvince = selectProvince;
                            HnAddressPickerTask.this.selectCity = selectCity;
                            HnAddressPickerTask.this.selectCounty = selectCounty;

                            String proName = selectProvince.getRegion_name();
                            String cityName = selectCity.getRegion_name();
                            String countyName = selectCounty.getRegion_name();

                            if (isShowCounty) {
                                HnLogUtils.i("provinceItem=" + proName);
                                HnLogUtils.i("cityItem=" + cityName);
                                HnLogUtils.i("countyItem=" + countyName);

                                mTv.setText(proName + "-" + cityName + "-" + countyName);
                            } else {
                                mTv.setText(proName + "-" + cityName);
                            }

                            listener.onPicked(proName, cityName, countyName);

                        }
                    }
                }).show();
    }

    public interface onPickedListener {
        void onPicked(String provinceName, String cityName,
                      String CountName);
    }
}