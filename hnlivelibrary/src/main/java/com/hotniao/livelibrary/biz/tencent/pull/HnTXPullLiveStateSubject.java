package com.hotniao.livelibrary.biz.tencent.pull;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：推流被观察者
 * 创建人：mj
 * 创建时间：2017/10/11 14:06
 * 修改人：Administrator
 * 修改时间：2017/10/11 14:06
 * 修改备注：
 * Version:  1.0.0
 */
public interface HnTXPullLiveStateSubject {

    //调用这个方法注册一个新的观察者对象
    void  attach(HnTXPullLiveObserver observer);

    //调用这个方法删除一个已注册的观察者对象
    void  detach(HnTXPullLiveObserver observer);

    //调用这个方法通知所有注册的观察者对象
    void notifyObserversToPullLiveIng();

    //调用这个方法通知所有注册的观察者对象
    void notifyObserversToPullLiveFail(int code, String type, Object object);

    //调用这个方法通知所有注册的观察者对象
    void notifyObserversToPullSuccess(int code, String type, Object object);

    //调用这个方法通知所有注册的观察者对象
    void notifyObserversToNetWorkState(int state);
}
