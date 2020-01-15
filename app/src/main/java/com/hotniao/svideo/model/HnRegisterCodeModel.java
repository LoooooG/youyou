package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/9/4 17:06
 * 修改人：Administrator
 * 修改时间：2017/9/4 17:06
 * 修改备注：
 * Version:  1.0.0
 */
public class HnRegisterCodeModel   extends BaseResponseModel {


    private HnRegisterCodeBean d;

    public HnRegisterCodeBean getD() {
        return d;
    }

    public void setD(HnRegisterCodeBean d) {
        this.d = d;
    }

    public  static  class  HnRegisterCodeBean {
        /**
         * test : 4227
         */

        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }


}
