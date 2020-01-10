package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：yibaobao
 * 类描述：
 * 创建人：oyke
 * 创建时间：2019/1/26 14:43
 * 修改人：oyke
 * 修改时间：2019/1/26 14:43
 * 修改备注：
 * Version:  1.0.0
 */
public class HnShareRuleModel extends BaseResponseModel {


    /**
     * rule : [{"content":"测试内容d235","title":"测试内容2tu0"}]
     * share : {"content":"测试内容at78","logo":"测试内容lo50","url":"测试内容6471"}
     */
    private DBean d;
    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private ShareBean share;
        private List<RuleBean> rule;

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public List<RuleBean> getRule() {
            return rule;
        }

        public void setRule(List<RuleBean> rule) {
            this.rule = rule;
        }

        public static class ShareBean {
            /**
             * content : 测试内容at78
             * logo : 测试内容lo50
             * url : 测试内容6471
             */

            private String title;
            private String content;
            private String logo;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class RuleBean {
            /**
             * content : 测试内容d235
             * title : 测试内容2tu0
             */

            private String content;
            private String title;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }

}
