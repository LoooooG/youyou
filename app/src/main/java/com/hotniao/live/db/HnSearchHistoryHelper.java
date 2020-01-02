package com.hotniao.live.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.hn.library.utils.HnLogUtils;
import com.hotniao.live.HnApplication;

import java.util.ArrayList;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：搜索历史数据库
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSearchHistoryHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "search.db";
    private static String TABLE_NAME = "search_history";
    private static int  version = 1;
    /**搜索记录数据库表中的的内容字段*/
    private   String CONTENT = "content";

    private static HnSearchHistoryHelper instance;
    private static SQLiteDatabase mDb;

    private HnSearchHistoryHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    public static HnSearchHistoryHelper getInstance() {
        if (instance == null) {
            instance = new HnSearchHistoryHelper(HnApplication.getContext());
            mDb = instance.getWritableDatabase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + " (id integer primary key autoincrement,"+CONTENT+" varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 插入数据库
     *
     * @param content  搜多的历史内容
     */
    public void insert(String content) {

        if(TextUtils.isEmpty(content)) return;

        if(hasData(content)){
            mDb.delete(TABLE_NAME, "content=?", new String[]{content});
        }

        ContentValues values = new ContentValues();
        values.put(CONTENT, content);

        mDb.insert(TABLE_NAME, null, values);
    }

    /**
     * 清除数据库中的数据
     */
    public void clearDataBase() {
        mDb.delete(TABLE_NAME,null,null);
        HnLogUtils.i("数据库已清空");
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    public boolean hasData(String tempName) {

        Cursor cursor = mDb.rawQuery(
                "select id as _id,content from search_history where content =?", new String[]{tempName});

        //判断是否有下一个
        return cursor.moveToNext();
    }



    /**
     * 显示搜索历史记录ListeView
     */
    public ArrayList<String> getHistoryLists() {

        ArrayList<String> searchHistoryDatas = new ArrayList<>();
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, "id desc", "");

        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(CONTENT));
            searchHistoryDatas.add(data);
            if(searchHistoryDatas.size()==8) break;
        }

        cursor.close();

        return searchHistoryDatas;

    }

    /**
     * 关闭数据库
     */
    public void close() {
        mDb.close();
        instance.close();
    }

}
