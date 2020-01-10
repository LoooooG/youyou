package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * create by Mr.x
 * on 10/8/2018
 */

public class CheckStatueModel extends BaseResponseModel {


    /**
     * d : {"status":2}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * status : 2
         */

        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
