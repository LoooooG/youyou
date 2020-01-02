package com.hotniao.livelibrary.model.event;

/**
 * @创建者 阳石柏
 * @创建时间 2016/9/21 18:00
 * @描述 ${用户河镑减少通知}
 */
public class UserCoinReduceEvent {

    private  String coin;

    public UserCoinReduceEvent(String coin) {
        this.coin = coin;
    }

    public String getCoin() {
        return coin;
    }
}
