package com.videolibrary.videochat.entity;

/**
 * create by Mr.x
 * on 1/8/2018
 */

public class FuzzyEvent {
    public FuzzyEvent(boolean show) {
        this.show = show;
    }

    boolean show = false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
