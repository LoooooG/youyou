package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

public class HnChatRoomIdModel extends BaseResponseModel{


    /**
     * d : {"room_id":"6666729_6666669"}//{"c":0,"m":"请求成功","d":{"room_id":"6666729_6666669"}}
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
         * room_id : 6666729_6666669
         */

        private String room_id;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }
    }
}
