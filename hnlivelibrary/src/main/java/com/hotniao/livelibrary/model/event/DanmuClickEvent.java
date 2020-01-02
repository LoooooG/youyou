package com.hotniao.livelibrary.model.event;

import android.view.View;

import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;


/**
 * @创建者 阳石柏
 * @创建时间 2016/8/23 9:06
 * @描述 ${弹幕点击事件Event}
 */
public class DanmuClickEvent {

    private final ReceivedSockedBean entity;
    private final View v;

    public DanmuClickEvent(View v, ReceivedSockedBean entity) {
        this.entity = entity;
        this.v = v;
    }

    public ReceivedSockedBean getEntity() {
        return entity;
    }

    public View getV() {
        return v;
    }
}
