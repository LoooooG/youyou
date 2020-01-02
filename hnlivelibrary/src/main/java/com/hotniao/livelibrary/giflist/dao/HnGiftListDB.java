package com.hotniao.livelibrary.giflist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表数据库，存储当前最新的礼物列表数据
 * 创建人：mj
 * 创建时间：2017/10/18 9:29
 * 修改人：Administrator
 * 修改时间：2017/10/18 9:29
 * 修改备注：
 * Version:  1.0.0
 */
public class HnGiftListDB {

    private static String TAG = "HnGiftListDB";


    private static final String DATABASE_NAME = "gift_list.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "gift_list";

    private static final String ID = "_id";
    /**
     * 礼物id
     */
    private static final String GIFT_ID = "gift_id";
    /**
     * 礼物名
     */
    private static final String GIFT_NAME = "gift_name";
    /**
     * 礼物价格
     */
    private static final String GIFT_COIN = "gift_coin";
    /**
     * 详情
     */
    private static final String DETAIL = "detail";
    /**
     * 礼物列表静态显示的图片地址
     */
    private static final String STATIC_GIFT_URL = "static_gift_url";
    /**
     * 礼物列表静态显示的本地图片地址
     */
    private static final String STATIC_GIFT_LOCAL_URL = "static_gift_local_url";
    /**
     * 礼物列表动态显示的本地图片地址
     */
    private static final String DYNAMIC_GIFT_LOCAL_URL = "dunamic_gift_local_url";


    /**
     * 礼物列表动态显示的图片地址
     */
    private static final String DYNAMIC_GIFT_URL = "dynamic_gift_url";
    /**
     * 用于大礼物资源包下载地址
     */
    private static final String ZIP_DOWNLOAD_URL = "zip_download_url";
    /**
     * 用于大礼物资源包本地存储地址
     */
    private static final String ZIP_LOCAL_URL = "zip_local_url";
    /**
     * 状态  下架/上架
     */
    private static final String STATE = "state";

    /**
     * 标签 礼物大分类
     */
    private static final String TAB_ID = "tab_id";
    /**
     * 标签 礼物大分类
     */
    private static final String TAB_NAME = "tab_name";
    /**
     * 礼物排序
     */
    private static final String SORT = "sort";


    private static HnGiftListDB instance;
    private GiftListHelper helper;
    private SQLiteDatabase db;


    private static final String CREATE_TABLE = "create table " + TABLE + " ( "
            + ID + "  integer primary key autoincrement, "
            + GIFT_ID + "  text not  null, "
            + GIFT_NAME + " text not  null, "
            + GIFT_COIN + " text not  null, "
            + DETAIL + " text, "
            + STATIC_GIFT_URL + " text not null, "
            + DYNAMIC_GIFT_URL + " text,  "
            + ZIP_DOWNLOAD_URL + " text, "
            + ZIP_LOCAL_URL + " text, "
            + STATE + " text not null, "
            + TAB_ID + " text, "
            + TAB_NAME + " text, "
            + SORT + " text, "
            + DYNAMIC_GIFT_LOCAL_URL + " text) ";

    /**
     * 更新gif
     *
     * @param type
     * @param giftId
     * @param giftUrl
     */
    public void update(int type, String giftId, String giftUrl) {
        ContentValues cv = new ContentValues();
        if (type == 0) {
            cv.put(STATIC_GIFT_LOCAL_URL, giftUrl);
        } else if (type == 1) {
            cv.put(DYNAMIC_GIFT_LOCAL_URL, giftUrl);
        }
        db = helper.getWritableDatabase();
        db.update(TABLE, cv, "gift_id=?", new String[]{giftId});

    }


    private static class GiftListHelper extends SQLiteOpenHelper {


        public GiftListHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            HnLogUtils.i(TAG, "标创建成功");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + CREATE_TABLE);
            onCreate(db);
            HnLogUtils.i(TAG, "表删除成功");
        }
    }


    private HnGiftListDB(Context context) {
        helper = new GiftListHelper(context);
    }

    private HnGiftListDB() {

    }

    public static HnGiftListDB getInstance(Context context) {
        if (instance == null) {
            synchronized (HnGiftListDB.class) {
                if (instance == null) {
                    instance = new HnGiftListDB(context);
                }
            }
        }
        return instance;
    }


    /**
     * 插入数据
     *
     * @param bean
     */
    public void insert(GiftListBean bean) {
        ContentValues cv = new ContentValues();
        cv.put(GIFT_ID, bean.getGift_id());
        cv.put(GIFT_NAME, bean.getGiftName());
        cv.put(GIFT_COIN, bean.getGiftCoin());
        cv.put(DETAIL, bean.getDetail());
        cv.put(STATIC_GIFT_URL, bean.getStaticGiftUrl());
        cv.put(DYNAMIC_GIFT_URL, bean.getDynamicGiftUrl());
        cv.put(ZIP_DOWNLOAD_URL, bean.getZipDownUrl());
        cv.put(ZIP_LOCAL_URL, bean.getZipDownLocalUrl());
        cv.put(STATE, bean.getState());
        cv.put(TAB_ID, bean.getmTabId());
        cv.put(TAB_NAME, bean.getmTabName());
        cv.put(SORT, bean.getSort());
        cv.put(DYNAMIC_GIFT_LOCAL_URL, bean.getDynamicGiftLocalUrl());
        db = helper.getWritableDatabase();
        long id = db.insert(TABLE, null, cv);
        HnLogUtils.d(TAG, "插入数据返回的id:" + id);
    }

    /**
     * 更新数据
     *
     * @param bean
     */
    public void update(GiftListBean bean, String gift_id) {
        ContentValues cv = new ContentValues();
        cv.put(GIFT_NAME, bean.getGiftName());
        cv.put(GIFT_COIN, bean.getGiftCoin());
        cv.put(DETAIL, bean.getDetail());
        cv.put(STATIC_GIFT_URL, bean.getStaticGiftUrl());
        cv.put(DYNAMIC_GIFT_URL, bean.getDynamicGiftUrl());
        cv.put(ZIP_DOWNLOAD_URL, bean.getZipDownUrl());
        cv.put(ZIP_LOCAL_URL, bean.getZipDownLocalUrl());
        cv.put(STATE, bean.getState());
        cv.put(TAB_ID, bean.getmTabId());
        cv.put(TAB_NAME, bean.getmTabName());
        cv.put(SORT, bean.getSort());
        cv.put(DYNAMIC_GIFT_LOCAL_URL, bean.getDynamicGiftLocalUrl());
        db = helper.getWritableDatabase();
        long id = db.update(TABLE, cv, "gift_id=?", new String[]{gift_id});
        HnLogUtils.d(TAG, "更新数据返回的id:" + id);
    }

    /**
     * 查询对应的数据
     *
     * @param gift_id
     * @return
     */
    public GiftListBean query(String gift_id) {
        GiftListBean bean = new GiftListBean();
        HnLogUtils.d(TAG, "gift_id:" + gift_id);
        String sql = "select *  from  gift_list  where  gift_id=?";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, new String[]{gift_id});
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String id = c.getString(1);
            String name = c.getString(2);
            String coin = c.getString(3);
            String detail = c.getString(4);
            String staticGIftUrl = c.getString(5);
            String dynamic = c.getString(6);
            String zipdownloadurl = c.getString(7);
            String ziplocalUrl = c.getString(8);
            String state = c.getString(9);
            String tabId = c.getString(10);
            String tabName = c.getString(11);
            String sort = c.getString(12);
            String dyLocalGif = c.getString(13);

            bean.setGift_id(id);
            bean.setGiftName(name);
            bean.setGiftCoin(coin);
            bean.setDetail(detail);
            bean.setStaticGiftUrl(staticGIftUrl);
            bean.setDynamicGiftUrl(dynamic);
            bean.setZipDownUrl(zipdownloadurl);
            bean.setZipDownLocalUrl(ziplocalUrl);
            bean.setState(state);
            bean.setmTabId(tabId);
            bean.setmTabName(tabName);
            bean.setSort(sort);
            bean.setDynamicGiftLocalUrl(dyLocalGif);
        }
        HnLogUtils.d(TAG, "查询的数据:" + bean.toString());
        c.close();
        return bean;
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public ArrayList<GiftListBean> queryAll() {
        ArrayList<GiftListBean> list = new ArrayList<>();
        String sql = "select *  from  gift_list";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            GiftListBean bean = new GiftListBean();
            String id = c.getString(1);
            String name = c.getString(2);
            String coin = c.getString(3);
            String detail = c.getString(4);
            String staticGIftUrl = c.getString(5);
            String dynamic = c.getString(6);
            String zipdownloadurl = c.getString(7);
            String ziplocalUrl = c.getString(8);
            String state = c.getString(9);
            String tabId = c.getString(10);
            String tabName = c.getString(11);
            String sort = c.getString(12);
            String dyLocalGif = c.getString(13);


            bean.setGift_id(id);
            bean.setGiftName(name);
            bean.setGiftCoin(coin);
            bean.setDetail(detail);
            bean.setStaticGiftUrl(staticGIftUrl);
            bean.setDynamicGiftUrl(dynamic);
            bean.setZipDownUrl(zipdownloadurl);
            bean.setZipDownLocalUrl(ziplocalUrl);
            bean.setState(state);
            bean.setmTabId(tabId);
            bean.setmTabName(tabName);
            bean.setSort(sort);
            bean.setDynamicGiftLocalUrl(dyLocalGif);
            list.add(bean);

        }
        HnLogUtils.d(TAG, "查询的数据的长度:" + list.size());
        c.close();
        return list;
    }


    /**
     * 查询所有的上架礼物数据
     *
     * @return
     */
    public ArrayList<GiftListBean> queryAllForPutawayGift() {
        ArrayList<GiftListBean> list = new ArrayList<>();
        String sql = "select *  from  gift_list  where  state=? ";
        String[] selectionArgs = {"1"};
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, selectionArgs);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            GiftListBean bean = new GiftListBean();
            String id = c.getString(1);
            String name = c.getString(2);
            String coin = c.getString(3);
            String detail = c.getString(4);
            String staticGIftUrl = c.getString(5);
            String dynamic = c.getString(6);
            String zipdownloadurl = c.getString(7);
            String ziplocalUrl = c.getString(8);
            String state = c.getString(9);
            String tabId = c.getString(10);
            String tabName = c.getString(11);
            String sort = c.getString(12);
            String dyLocalGif = c.getString(13);

            bean.setGift_id(id);
            bean.setGiftName(name);
            bean.setGiftCoin(coin);
            bean.setDetail(detail);
            bean.setStaticGiftUrl(staticGIftUrl);
            bean.setDynamicGiftUrl(dynamic);
            bean.setZipDownUrl(zipdownloadurl);
            bean.setZipDownLocalUrl(ziplocalUrl);
            bean.setState(state);
            bean.setmTabId(tabId);
            bean.setmTabName(tabName);
            bean.setSort(sort);
            bean.setDynamicGiftLocalUrl(dyLocalGif);

            list.add(bean);
        }
        HnLogUtils.d(TAG, "查询的数据的长度:" + list.size());
        c.close();
        return list;
    }


    /**
     * 查询所有的数据
     *
     * @return
     */
    public List<String> queryAllGiftId() {
        List<String> list = new ArrayList<>();
        String sql = "select *  from  gift_list";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String id = c.getString(1);
            list.add(id);
        }
        HnLogUtils.d(TAG, "查询的数据的长度:" + list.size());
        c.close();
        return list;
    }

    /**
     * 删除数据
     *
     * @param gift_id
     * @return
     */
    public long delete(String gift_id) {
        db = helper.getWritableDatabase();
        long ids = db.delete(TABLE, "gift_id=?", new String[]{gift_id});
        HnLogUtils.d(TAG, "删除数据:" + ids);
        return ids;
    }

    /**
     * 清除所有的数据
     */
    public int deleteAllData() {
        int id = db.delete(TABLE, null, null);
        HnLogUtils.i("数据库已清空");
        return id;
    }

    /**
     * \
     * 关闭数据库
     */
    public void closeDB() {
        if (db != null) {
            db.close();
        }
        if (helper != null) {
            helper.close();
        }
    }

}
