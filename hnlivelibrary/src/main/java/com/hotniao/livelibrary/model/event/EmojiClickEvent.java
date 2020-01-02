package com.hotniao.livelibrary.model.event;

import com.hotniao.livelibrary.model.bean.Emoji;

/**
 * @创建者 Administrator
 * @创建时间 2016/9/14 13:31
 * @描述 ${TODO}
 */
public class EmojiClickEvent {

    private final Emoji emoji;

    public EmojiClickEvent(Emoji emoji) {
        this.emoji = emoji;
    }

    public Emoji getEmoji() {
        return emoji;
    }
}
