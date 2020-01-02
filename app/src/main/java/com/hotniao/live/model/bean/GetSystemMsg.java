package com.hotniao.live.model.bean;

import java.util.List;

/**
 * @创建者 阳石柏
 * @创建时间 2016/10/28 15:20
 * @描述 ${获取系统消息返回data}
 */
public class GetSystemMsg {


    private List<SystemDialogBean> system_dialog;

    public List<SystemDialogBean> getSystem_dialog() {
        return system_dialog;
    }

    public void setSystem_dialog(List<SystemDialogBean> system_dialog) {
        this.system_dialog = system_dialog;
    }

    public static class SystemDialogBean {
        /**
         * dialog_id : 2
         * msg : 123
         * time : 0
         */

        private String dialog_id;
        private String msg;
        private String time;

        public String getDialog_id() {
            return dialog_id;
        }

        public void setDialog_id(String dialog_id) {
            this.dialog_id = dialog_id;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
