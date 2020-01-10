package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * create by Mr.x
 * on 10/8/2018
 */

public class HnChatTypeModel extends BaseResponseModel {


    /**
     * d : {"chat_category":[{"chat_category_id":"7","chat_category_logo":"https://moyan-1255980036.image.myqcloud.com/image/20180809/1533800777656869.jpg","chat_category_name":"测试"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<ChatCategoryBean> chat_category;

        public List<ChatCategoryBean> getChat_category() {
            return chat_category;
        }

        public void setChat_category(List<ChatCategoryBean> chat_category) {
            this.chat_category = chat_category;
        }

        public static class ChatCategoryBean {
            /**
             * chat_category_id : 7
             * chat_category_logo : https://moyan-1255980036.image.myqcloud.com/image/20180809/1533800777656869.jpg
             * chat_category_name : 测试
             */

            private String chat_category_id;
            private String chat_category_logo;
            private String chat_category_name;

            public String getChat_category_id() {
                return chat_category_id;
            }

            public void setChat_category_id(String chat_category_id) {
                this.chat_category_id = chat_category_id;
            }

            public String getChat_category_logo() {
                return chat_category_logo;
            }

            public void setChat_category_logo(String chat_category_logo) {
                this.chat_category_logo = chat_category_logo;
            }

            public String getChat_category_name() {
                return chat_category_name;
            }

            public void setChat_category_name(String chat_category_name) {
                this.chat_category_name = chat_category_name;
            }
        }
    }
}
