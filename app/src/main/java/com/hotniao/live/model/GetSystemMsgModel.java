package com.hotniao.live.model;


import com.hotniao.live.model.bean.GetSystemMsg;
import com.hn.library.http.BaseResponseModel;

/**
 * @创建者 mj
 * @创建时间 2016/10/28 15:20
 * @描述 ${获取系统消息返回Model}
 */
public class GetSystemMsgModel extends BaseResponseModel {

    private GetSystemMsg d;

    public GetSystemMsg getD() {
        return d;
    }

    public void setD(GetSystemMsg d) {
        this.d = d;
    }
}
