package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.video.model.bean.HnLiveNoticeBean;

/**
 * @创建者 mj
 * @创建时间 2017/5/3 14:45
 * @描述 ${开播设置Model}
 */
public class HnLiveNoticeModel extends BaseResponseModel {

    public HnLiveNoticeBean d;

    public HnLiveNoticeBean getD() {
        return d;
    }

    public void setD(HnLiveNoticeBean d) {
        this.d = d;
    }
}
