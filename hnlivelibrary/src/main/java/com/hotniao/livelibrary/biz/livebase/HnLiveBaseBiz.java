package com.hotniao.livelibrary.biz.livebase;

import android.text.TextUtils;

import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnOnlineModel;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.OnlineBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，处理直播间主播端，用户端公共的业务计算相关操作;网络操作.ui操作请尽量不要在该类处理
 * 创建人：mj
 * 创建时间：2017/9/14 20:03
 * 修改人：Administrator
 * 修改时间：2017/9/14 20:03
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveBaseBiz {






    /**
     * 添加系统公告到消息列表中
     * @param
     * @param notice  系统公告
     */
    public ArrayList<HnReceiveSocketBean> addSystemNotice(List<String> notice) {
        ArrayList<HnReceiveSocketBean> messageList=new ArrayList<>();
        if(notice==null){  return messageList;}
       for (int i=0;i<notice.size();i++){
           HnReceiveSocketBean  bean=new HnReceiveSocketBean();
           bean.setType(HnWebscoketConstants.Notice);
           bean.setNotice(notice.get(i));
           messageList.add(bean);
       }
        return messageList;
    }

    /**
     * 将公聊消息添加到适配器中
     * @param object          数据源
     * @param messageList     公聊消息列表
     * @return
     */
    public ArrayList<HnReceiveSocketBean> addMsgData(Object object, ArrayList<HnReceiveSocketBean> messageList) {
        if(object==null)  {return messageList;}
        final HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if (bean != null) {
            if (messageList != null) {
                if (messageList.size() > 200) {
                    messageList.remove(0);
                    messageList.add(bean);
                } else {
                    messageList.add(bean);
                }
            }
        }
        return messageList;
    }




    /**
     * 获取需要显示的在线人数据列表
     * 说明： 1.当实际在线人数大于等于30时，直接从实际在线人数获取头30即可
     * @param onlineList  在线实际人数列表
     */
    public ArrayList<OnlineBean> getShowOnlineList(ArrayList<OnlineBean>  onlineList) {
        ArrayList<OnlineBean> list=new ArrayList<>();
        if (onlineList.size() >= 30) {//当实际在线人数大于等于30时
            for (int i = 0; i <30 ; i++) {
                list.add(onlineList.get(i));
            }
        }else{//当实际在线人数小于30时
            for (int i = 0; i <onlineList.size() ; i++) {
                list.add(list.size(),onlineList.get(i));
            }
            int count=30-list.size();

        }
        return   list;
    }

    /**
     * 用户端接到Online类型后的数据后，获取里面的在线人员列表  不能包括主播
     *
     * @param object 数据源
     * @return anchor_id     主播id
     */
    public ArrayList<OnlineBean> getOnlienList(Object object, String anchor_id) {
        ArrayList<OnlineBean> list = new ArrayList<>();
        HnOnlineModel model = (HnOnlineModel) object;
        if (model == null || model.getData() == null) return list;
        List<HnOnlineModel.DataBean.UsersBean> items = model.getData().getUsers();
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!TextUtils.isEmpty(anchor_id) && !anchor_id.equals(items.get(i).getUser_id())) {
                    OnlineBean bean = new OnlineBean(items.get(i).getUser_id(), items.get(i).getUser_avatar(), "0", items.get(i).getUser_is_member());
                    list.add(bean);
                }
            }
        }
        return list;
    }






    /**
     * 当大礼物下载完成后，更新礼物数据
     * @param isShow                    是否显示需要下载的数据
     * @param data                      礼物数据
     * @param mBigGiftActionManager     大礼物管理器
     * @param gifts                     礼物列表
     * @return
     */
    public ArrayList<GiftListBean> updateGiftListData(Object obj,boolean isShow, GiftListBean data, BigGiftActionManager mBigGiftActionManager, ArrayList<GiftListBean> gifts) {
        if(data!=null){
            String gift_id=data.getGift_id();
            String localUrl=data.getZipDownLocalUrl();
            for (int i = 0; i <gifts.size() ; i++) {
                String id=gifts.get(i).getGift_id();
                if(gift_id.equals(id)){
                    gifts.get(i).setZipDownLocalUrl(localUrl);
                }
            }
            if(isShow){
                if(obj!=null) {
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) obj;
                    if (bean != null&&bean.getData()!=null){
                        bean.getData().setBigGiftAddress(data.getZipDownLocalUrl());
                    }
                    if (bean != null&&bean.getData()!=null&&!TextUtils.isEmpty(bean.getData().getBigGiftAddress())) {
                        mBigGiftActionManager.addDanmu(bean.getData());
                    }
                }
            }
        }
        return  gifts;
    }

    /**
     * 获取对应的礼物数据
     * @param object    推送的数据
     * @param giftList  礼物列表
     * @return
     */
    public HnReceiveSocketBean getGiftData(Object object, List<GiftListBean> giftList) {
        final HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if(bean!=null&&giftList.size()>0){
            String id=bean.getData().getLive_gift().getLive_gift_id();
            for (int i = 0; i <giftList.size() ; i++) {
                String gift_id=giftList.get(i).getGift_id();
                String name=giftList.get(i).getGiftName();
                if(id.equals(gift_id)){
                    bean.getData().getLive_gift().setLive_gift_name(name);
                    return  bean;
                }
            }
        }
        return null;
    }
}
