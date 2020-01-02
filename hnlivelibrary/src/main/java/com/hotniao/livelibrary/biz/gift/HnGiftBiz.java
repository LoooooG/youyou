package com.hotniao.livelibrary.biz.gift;

import android.content.Context;
import android.text.TextUtils;

import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseListener;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.giflist.HnDonwloadGiftStateListener;
import com.hotniao.livelibrary.giflist.HnGiftListManager;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表操作
 * 创建人：mj
 * 创建时间：2017/10/18 18:09
 * 修改人：Administrator
 * 修改时间：2017/10/18 18:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnGiftBiz  {

    /**常量：从数据库中获取数据*/
    public   static  final String  GET_GIFT_LIST_FROM_DB="GET_GIFT_LIST";

   /**上下文*/
    private   Context context;
    /**网络请求业务*/
    private  HnLiveBaseRequestBiz  mHnLiveBaseRequestBiz;
    /**礼物管理类，礼物的数据库操作以及下载*/
    private  HnGiftListManager     mHnGiftListManager;
    /**网络请求监听*/
    private HnLiveBaseListener  mHnLiveBaseListener;

    public HnGiftBiz(Context context,HnDonwloadGiftStateListener  listener,HnLiveBaseListener  mHnLiveBaseListener) {
        this.context=context;
        this.mHnLiveBaseListener=mHnLiveBaseListener;
        mHnLiveBaseRequestBiz=new HnLiveBaseRequestBiz();
        mHnGiftListManager= HnGiftListManager.getInstance();
        if(listener!=null) {
            mHnGiftListManager.setHnDonwloadGiftStateListener(listener);
        }
    }


    /**
     * 获取礼物列表
     */
    public void getGiftListData() {
        ArrayList<GiftListBean> giftList = mHnGiftListManager.quaryPutawaListFromDB(context);
        //礼物列表已经存储到数据库中
        if(giftList.size()>0){
            if(mHnLiveBaseListener!=null){
                 mHnLiveBaseListener.requestSuccess(GET_GIFT_LIST_FROM_DB,null,giftList);
            }
            //获取礼物数据
        }else{
            requestToGetGiftList();
        }
    }

    public void getData(){
        RequestParams param=new RequestParams();
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_GIFT_INDEX, param, HnLiveUrl.LIVE_GIFT_INDEX, new HnResponseHandler<HnGiftListModel>(HnGiftListModel.class) {
            @Override
            public void hnSuccess(String response) {
                if(model.getC()==0) {
                    if (mHnLiveBaseListener != null) {
                        mHnLiveBaseListener.requestSuccess("jia_data", response,model);
                    }
                }else {
                    if (mHnLiveBaseListener != null) {
                        mHnLiveBaseListener.requestFail("jia_data", model.getC(),model.getM());
                    }
                }
            }
            @Override
            public void hnErr(int errCode, String msg) {
                if (mHnLiveBaseListener != null) {
                    mHnLiveBaseListener.requestFail("jia_data", errCode,msg);
                }
            }
        });
    }

    /**
     * 网络请求：获取礼物列表
     */
    public  void  requestToGetGiftList(){
        mHnLiveBaseRequestBiz.requestToGetGiftList(mHnLiveBaseListener);
    }
    /**
     * 改变礼物数据
     * @param data
     * @param pageGifts
     */
    public List<HnGiftListBean.GiftBean> chanageGiftData(GiftListBean data, List<HnGiftListBean.GiftBean> pageGifts) {
        String id=  data.getGift_id();
        String zipLocalUrl=  data.getZipDownLocalUrl();
        for (int i = 0; i <pageGifts.size() ; i++) {
            HnGiftListBean.GiftBean giftBean = pageGifts.get(i);
            if (data.getmTabId().equals(giftBean.getId())) {
                for (int j = 0; j <giftBean.getItems().size() ; j++) {
                    String gift_id=giftBean.getItems().get(j).getId();
                    if(id.equals(gift_id)){
                        pageGifts.get(i).getItems().get(j).setAnimation(zipLocalUrl);
                        break;
                    }
                }
            }

        }
        return pageGifts;
    }

    /**
     * 获取到礼物数据后，需要将其分页，每一个viewpager的字布局最多存放10个礼物
     * @param giftList  礼物数据
     * @return
     */
    public ArrayList<ArrayList<GiftListBean>> setViewPagerGiftData(ArrayList<GiftListBean> giftList) {
        ArrayList<ArrayList<GiftListBean>> pageGifts = new ArrayList<>();
        if (giftList != null && giftList.size() > 0) {
            ArrayList<GiftListBean> list = null;
            for (int i = 0; i < giftList.size(); i++) {
                if (i % 10 == 0) {
                    if (list != null && list.size() > 0) {
                        pageGifts.add(list);
                    }
                    list = new ArrayList<>();
                }
                list.add(giftList.get(i));
                if (i == giftList.size() - 1) {
                    pageGifts.add(list);
                }
            }

        }
        return pageGifts;
    }

    /**
     * 数据转换
     * @param gift_list
     * @param
     */
    public void transformData(List<HnGiftListBean.GiftBean> gift_list) {
        if(gift_list==null||gift_list.size()==0) {
            return;
        }
        ArrayList<GiftListBean> list=new ArrayList<>();
        for (int i = 0; i <gift_list.size() ; i++) {
            HnGiftListBean.GiftBean giftBean = gift_list.get(i);
            for (int j = 0; j < giftBean.getItems().size(); j++) {

                HnGiftListBean.GiftBean.ItemsBean result = giftBean.getItems().get(i);
                GiftListBean bean = new GiftListBean();
                bean.setGift_id(result.getId());
                bean.setGiftName(result.getName());
                bean.setDetail(result.getDetail());
                bean.setState(result.getStatus());
                bean.setStaticGiftUrl(result.getIcon());
                bean.setDynamicGiftUrl(result.getIcon_gif());
                bean.setZipDownUrl(result.getAnimation());
                bean.setGiftCoin(result.getCoin());

                bean.setmTabId(giftBean.getId() + "");
                bean.setmTabName(giftBean.getName());
                bean.setSort(result.getSort());
                list.add(bean);
            }
        }
        //先回调给界面
        if(mHnLiveBaseListener!=null){
            mHnLiveBaseListener.requestSuccess(GET_GIFT_LIST_FROM_DB,null,list);
        }
        //数据库操作
        mHnGiftListManager.dealGiftList(context,list);
    }


    /**
     *  下载指定大礼物
     * @param giftListBean  要下载的礼物数据
     * @param context        上下文
     */
    public void downloadBigGift(GiftListBean giftListBean,Context context,Object obj) {
        if(mHnGiftListManager==null||giftListBean==null||context==null) {
            return;
        }
        //数据库操作
        mHnGiftListManager.downLoadBigLift(true,giftListBean,context,obj);
    }

    /**
     * 是否需要下载大礼物
     * @param mGift
     * @return
     */
    public boolean isNeedDownloadBigGift(GiftListBean mGift,Context context) {
        if(mGift!=null&&context!=null){
            String zipUrl=mGift.getZipDownUrl();
            String zipLocalIrl=mGift.getZipDownLocalUrl();
            if(!TextUtils.isEmpty(zipUrl)&&TextUtils.isEmpty(zipLocalIrl)&&context!=null){
                //数据库操作
                mHnGiftListManager.downLoadBigLift(true,mGift,context,null);
                return true;
            }
        }
        return false;
    }


    /**
     * 关闭数据库
     */
    public   void  closeDB(){
        if(mHnGiftListManager!=null){
            mHnGiftListManager.closeDB();
        }
    }

    public void updataGiftGifData(String giftId,String giftUrl){
        if(mHnGiftListManager!=null){
            mHnGiftListManager.updataGiftGifData(context,giftId,giftUrl);
        }
    }
}
