package com.hotniao.livelibrary.widget.gift.bigGift;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：大礼物 -- 管理
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class BigGiftActionManager implements BigGiftActionInter {

    public List<BigGiftChannel> channels = new LinkedList<>();

    public Queue<HnReceiveSocketBean.DataBean> danEntities = new LinkedList<>();
    private String TAG = "SynchActionManager";

    @Override
    public void addDanmu(HnReceiveSocketBean.DataBean dan) {
        danEntities.add(dan);
        looperDan();
    }

    @Override
    public void pollDanmu() {
        looperDan();
    }

    public void addChannel(BigGiftChannel channel) {
        channels.add(channel);
    }

    public BigGiftActionManager() {

    }

    private int POLL_GIFT = 1;
    private int FINISH_GIFT = 2;

    public void looperDan() {

        if (channels.size() > 0) {
            HnLogUtils.d(TAG, "动画是否完成：" + channels.get(0).getIsRunning());
            HnLogUtils.d(TAG, "取之前danEntities的长度为：" + danEntities.size());

            Message receive = handler.obtainMessage();
            receive.what = POLL_GIFT;
            handler.sendMessage(receive);

            HnLogUtils.d(TAG, "取之后danEntities的长度为：" + danEntities.size());
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == POLL_GIFT) {
                if (!channels.get(0).getIsRunning() && danEntities.size() > 0) {
                    HnReceiveSocketBean.DataBean poll = danEntities.poll(); //检索并移除第一个元素
                    channels.get(0).startBigGiftAnimation2(poll, new BigGiftPlayListener() {
                        @Override
                        public void onPlayBegin() {
                            HnLogUtils.i(TAG, "大动画播放开始:" + danEntities.size());
                        }

                        @Override
                        public void onPlayFInish() {
                            handler.sendEmptyMessage(FINISH_GIFT);
                        }
                    });
                }
            } else if (FINISH_GIFT == msg.what) {
                //播放完之后清除视图
                channels.get(0).isRunning = false;
                channels.get(0).clearChildView();
                //查看队列中是否还有大礼物未播放
                if (danEntities != null && danEntities.size() > 0) {
                    Message receive = handler.obtainMessage();
                    receive.what = POLL_GIFT;
                    handler.sendMessage(receive);
                }
                HnLogUtils.i(TAG, "大动画播放结束:" + danEntities.size());
            }

            super.handleMessage(msg);
        }
    };


    /**
     * 清除大礼物动画以及数据
     */
    public void clearAll() {
        handler.removeMessages(POLL_GIFT);
        handler.removeMessages(FINISH_GIFT);
        if (channels != null && channels.size() > 0) {
            for (int i = 0; i < channels.size(); i++) {
                channels.get(i).isRunning = false;
                channels.get(i).clearChildView();
            }
        }
        if (danEntities != null) {
            danEntities.clear();
        }
    }
}
