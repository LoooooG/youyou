package com.hotniao.live.utils;

import android.text.TextUtils;

import com.hn.library.global.HnConstants;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.live.model.HnMusicDownedModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：音乐
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMusicUtil {

    /**
     * list转string
     *
     * @param data
     */
    public synchronized static void  listToString(List<HnMusicDownedModel> data) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject tmpObj = null;
            int count = data.size();
            for (int i = 0; i < count; i++) {
                tmpObj = new JSONObject();
                tmpObj.put("singer", data.get(i).getSinger());
                tmpObj.put("singName", data.get(i).getSingName());
                tmpObj.put("cover", data.get(i).getCover());
                tmpObj.put("localPath", data.get(i).getLocalPath());
                tmpObj.put("time", data.get(i).getTime());
                tmpObj.put("id", data.get(i).getId());
                tmpObj.put("serverPath", data.get(i).getServerPath());
                jsonArray.put(tmpObj);
                tmpObj = null;
            }
            String str = jsonArray.toString(); // 将JSONArray转换得到String
            //存入数据
            HnPrefUtils.setString(HnConstants.MUSIC_DATD, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * string 转list
     *
     * @param str
     */
    public  synchronized static List<HnMusicDownedModel> stringToList(String str) {
        List<HnMusicDownedModel> mData = new ArrayList<>();
        if (TextUtils.isEmpty(str)) return mData;
        try {

            JSONArray jsonArray = new JSONArray(str);
            HnMusicDownedModel model = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                model = new HnMusicDownedModel();
                model.setCover(jsonArray.optJSONObject(i).optString("cover"));
                model.setLocalPath(jsonArray.optJSONObject(i).optString("localPath"));
                model.setSinger(jsonArray.optJSONObject(i).optString("singer"));
                model.setSingName(jsonArray.optJSONObject(i).optString("singName"));
                model.setTime(jsonArray.optJSONObject(i).optString("time"));
                model.setId(jsonArray.optJSONObject(i).optString("id"));
                model.setServerPath(jsonArray.optJSONObject(i).optString("serverPath"));
                mData.add(model);
                model = null;
            }
            return mData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mData;
    }
}
