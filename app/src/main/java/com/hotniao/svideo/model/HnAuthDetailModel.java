package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.io.Serializable;

/**
 * @创建者 阳石柏
 * @创建时间 2017/5/22 11:54
 * @描述 ${认证详情}
 */
public class HnAuthDetailModel extends BaseResponseModel implements Serializable {


    /**
     * d : {"is_submit":"N","user_certification_result":"","user_certification_status":"C"}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean implements Serializable{
        /**
         * is_submit : N
         * user_certification_result :
         * user_certification_status : C
         */

        private String is_submit;
        private String user_certification_result;
        private String user_certification_status;

        public String getIs_submit() {
            return is_submit;
        }

        public void setIs_submit(String is_submit) {
            this.is_submit = is_submit;
        }

        public String getUser_certification_result() {
            return user_certification_result;
        }

        public void setUser_certification_result(String user_certification_result) {
            this.user_certification_result = user_certification_result;
        }

        public String getUser_certification_status() {
            return user_certification_status;
        }

        public void setUser_certification_status(String user_certification_status) {
            this.user_certification_status = user_certification_status;
        }
    }
}
