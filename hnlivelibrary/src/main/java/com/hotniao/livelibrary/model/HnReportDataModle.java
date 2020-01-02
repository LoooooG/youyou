package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述:
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnReportDataModle extends BaseResponseModel {


    /**
     * d : {"report":[{"content":"","id":"0"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<ReportBean> report;

        public List<ReportBean> getReport() {
            return report;
        }

        public void setReport(List<ReportBean> report) {
            this.report = report;
        }

        public static class ReportBean {
            /**
             * content :
             * id : 0
             */

            private String content;
            private String id;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
