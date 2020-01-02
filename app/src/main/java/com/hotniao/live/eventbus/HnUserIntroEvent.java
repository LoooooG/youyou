package com.hotniao.live.eventbus;

/**
 * @创建者 songxuefeng
 * @创建时间 2016/8/29 14:07
 * @描述 ${TODO}
 */
public class HnUserIntroEvent {
    public String userIntro;

    public HnUserIntroEvent(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getUserIntro() {
        return userIntro;
    }
}
