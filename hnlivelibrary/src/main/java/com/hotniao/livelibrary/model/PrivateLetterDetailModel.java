package com.hotniao.livelibrary.model;


import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.PrivateLetterDetail;

/**
 * @创建者 阳石柏
 * @创建时间 2016/8/24 11:57
 * @描述 ${私信详情返回Model}
 */
public class PrivateLetterDetailModel extends BaseResponseModel{


    private PrivateLetterDetail d;

    public PrivateLetterDetail getD() {
        return d;
    }

    public void setD(PrivateLetterDetail d) {
        this.d = d;
    }
}
