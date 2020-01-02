package com.hotniao.livelibrary.giflist;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.hn.library.http.HnHttpUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.control.HnDownloadFileControl;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.giflist.dao.HnGiftListDB;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.msebera.android.httpclient.Header;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表数据操作类
 * 创建人：mj
 * 创建时间：2017/10/17 19:05
 * 修改人：Administrator
 * 修改时间：2017/10/17 19:05
 * 修改备注：
 * Version:  1.0.0
 *
 * @author Administrator
 */
public class HnGiftListManager {

    private String TAG = "HnGiftListManager";
    private String Big_Gift_Local_Url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    /**
     * 礼物列表数据库管理类
     */
    private HnGiftListDB mHnGIftListDaoManager;
    /**
     * 下载监听器
     */
    private HnDonwloadGiftStateListener listener;

    private List<GiftListBean> downloadGiftLists = new ArrayList<>();


    public static class HnGiftListManagerViewHolder {
        private static HnGiftListManager instance = new HnGiftListManager();
    }

    public static HnGiftListManager getInstance() {
        return HnGiftListManagerViewHolder.instance;
    }

    /**
     * 处理礼物列表数据
     *
     * @param context 上下文
     * @param list    礼物列表数据
     */
    public void dealGiftList(Context context, ArrayList<GiftListBean> list) {
        dataMatching(context, list);
    }

    /**
     * 数据匹配
     *
     * @param list 传递过来的最新数据源
     */
    private void dataMatching(final Context context, ArrayList<GiftListBean> list) {
        Observable.just(list)
                .map(new Function<ArrayList<GiftListBean>, ArrayList<GiftListBean>>() {
                    @Override
                    public ArrayList<GiftListBean> apply(@NonNull ArrayList<GiftListBean> data) throws Exception {
                        return getDownloadZipList(context, data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ArrayList<GiftListBean>>() {
                    @Override
                    public void accept(ArrayList<GiftListBean> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            downloadGift(context, list);
                        }
                        ArrayList<GiftListBean> giftList = mHnGIftListDaoManager.queryAll();

                        if (giftList != null && giftList.size() > 0) {
                            downLoadGifAndPic(context, giftList);
                        } else {
                            closeDB();
                        }
                    }
                });

    }

    /**
     * 下载gif图
     *
     * @param context
     * @param list
     */
    private void downLoadGifAndPic(Context context, ArrayList<GiftListBean> list) {
        if (list != null && list.size() > 0) {
            GiftListBean bean = list.get(0);
            String gift_id = bean.getGift_id();
            String giftUrl = bean.getDynamicGiftUrl();
            String giftLocalUrl = bean.getDynamicGiftLocalUrl();
            if (!TextUtils.isEmpty(giftUrl) && TextUtils.isEmpty(giftLocalUrl)) {
                downLoadGifPic(context, list, giftUrl, gift_id, bean);
            } else {
                list.remove(0);
                downLoadGifAndPic(context, list);
            }

        }
    }

    /**
     * 下载gif图
     *
     * @param context
     * @param list
     * @param giftUrl
     * @param gift_id
     * @param bean
     */
    private void downLoadGifPic(final Context context, final ArrayList<GiftListBean> list, String giftUrl, final String gift_id, final GiftListBean bean) {
        try {
            final String giftDir = context.getExternalFilesDir(null).getAbsolutePath() + "/gift/";
            File mainFile = new File(giftDir);
            if (!mainFile.exists()) mainFile.mkdirs();
            int index = giftUrl.lastIndexOf("/");
            final String fileName = giftUrl.substring(index + 1, giftUrl.length());
            final String path = giftDir + fileName;
            File downloadFile = new File(path);
            if (downloadFile.exists()) downloadFile.delete();
//            downloadFile.createNewFile();
//            final File finalDownloadFile = downloadFile;
            HnDownloadFileControl.down(giftUrl, path, new HnDownloadFileControl.DownStutaListener() {
                @Override
                public void downloadSuccess(String url, final String path) {
                    Observable.just("1")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    File file = new File(path);
                                    if (file.exists()) {
                                    }
                                    bean.setDynamicGiftLocalUrl(file.getAbsolutePath());
                                    getHnGiftListDB(context);
                                    mHnGIftListDaoManager.update(bean, gift_id);
                                    list.get(0).setDynamicGiftLocalUrl(file.getAbsolutePath());
                                    downLoadGifAndPic(context, list);
                                }
                            });
                }

                @Override
                public void downloadError(int code, String msg) {
                    Observable.just("1")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
//                                    file.delete();
                                    File file = new File(path);
                                    if (file.exists()) {
                                    }
                                    list.get(0).setDynamicGiftLocalUrl(file.getAbsolutePath());
                                    downLoadGifAndPic(context, list);
                                }
                            });
                }
            });

//            HnHttpUtils.downloadFile(true, giftUrl, new FileAsyncHttpResponseHandler(finalDownloadFile) {
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                    file.delete();
//                    list.get(0).setDynamicGiftLocalUrl(finalDownloadFile.getAbsolutePath());
//                    downLoadGifAndPic(context, list);
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, File file) {
//                    bean.setDynamicGiftLocalUrl(file.getAbsolutePath());
//                    getHnGiftListDB(context);
//                    mHnGIftListDaoManager.update(bean, gift_id);
//                    list.get(0).setDynamicGiftLocalUrl(file.getAbsolutePath());
//                    downLoadGifAndPic(context, list);
//                }
//            });
        } catch (Exception e) {
            HnLogUtils.e("aa");
        }
    }


    /**
     * 返回需要下载的zip文件列表
     *
     * @param list
     * @return
     */
    private ArrayList<GiftListBean> getDownloadZipList(Context context, ArrayList<GiftListBean> list) {
        getHnGiftListDB(context);
        ArrayList<GiftListBean> giftLists = new ArrayList<>();
        //删除数据库中的与最新礼物列表不符合的礼物
        List<String> idList = mHnGIftListDaoManager.queryAllGiftId();
        if (idList != null && idList.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String giftId = list.get(i).getGift_id();
                if (!idList.contains(giftId)) {
                    mHnGIftListDaoManager.delete(giftId);
                }
            }
        }
        //匹配数据库，进行数据库中的数据插入和更新
        for (int i = 0; i < list.size(); i++) {
            GiftListBean giftListBean = list.get(i);
            String id = giftListBean.getGift_id();
            GiftListBean bean = mHnGIftListDaoManager.query(id);
            //数据库中没有
            if (TextUtils.isEmpty(bean.getGift_id())) {
                //插入数据库
                mHnGIftListDaoManager.insert(giftListBean);
                //判断是否是大礼物，是否需要下载
                String state = giftListBean.getState();
                String zipUrl = giftListBean.getZipDownUrl();
                //需要下载zi[
                if ("1".equals(state) && !TextUtils.isEmpty(zipUrl)) {
                    giftLists.add(giftListBean);
                    HnLogUtils.i(TAG, "大礼物，需要下载");
                } else {
                    HnLogUtils.i(TAG, "小礼物，无需下载");
                }
                //若数据库中存在
            } else {
                String newGiftName = giftListBean.getGiftName();
                String newStaticUrl = giftListBean.getStaticGiftUrl();
                String newDynamicUrl = giftListBean.getDynamicGiftUrl();
                String newZipUrl = giftListBean.getZipDownUrl();
                String newCoin = giftListBean.getGiftCoin();
                String state = giftListBean.getState();
                String mTabId = giftListBean.getmTabId();
                String mTabName = giftListBean.getmTabName();
                String mSort = giftListBean.getSort();

                String oldGiftName = bean.getGiftName();
                String oldStaticUrl = bean.getStaticGiftUrl();
                String oldDynamicUrl = bean.getDynamicGiftUrl();
                String oldCoin = bean.getGiftCoin();
                String oldZipUrl = bean.getZipDownUrl();
                String oldZipLocalUrl = bean.getZipDownLocalUrl();
                String oldstate = bean.getState();
                String oldTabId = bean.getmTabId();
                String oldTabName = bean.getmTabName();
                String oldSort = bean.getSort();

                //全部数据相同
                if (newGiftName.equals(oldGiftName) && state.equals(oldstate) && newZipUrl.equals(oldZipUrl) && newCoin.equals(oldCoin)
                        && newStaticUrl.equals(oldStaticUrl) && newDynamicUrl.equals(oldDynamicUrl) && mTabId.equals(oldTabId) && mTabName.equals(oldTabName)
                        && mSort.equals(oldSort)) {
                    if (!TextUtils.isEmpty(newZipUrl) && "1".equals(state)) {
                        //判断该大礼物是否在sd卡中存在
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            if (TextUtils.isEmpty(oldZipLocalUrl)) {
                                giftLists.add(giftListBean);
                            } else {
                                File file = new File(oldZipLocalUrl);
                                if (file == null || !file.exists()) {
                                    bean.setZipDownLocalUrl("");
                                    mHnGIftListDaoManager.update(bean, bean.getGift_id());
                                    giftLists.add(giftListBean);
                                }
                            }
                        }
                    }
                } else {
                    //更新数据库
                    mHnGIftListDaoManager.update(giftListBean, id);
                    //大礼物处于上架状态
                    if (!TextUtils.isEmpty(newZipUrl) && "1".equals(newZipUrl)) {
                        //判断该大礼物是否在sd卡中存在
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            File file = new File(oldZipLocalUrl);
                            if (file != null || !file.exists()) {
                                giftLists.add(giftListBean);
                                HnLogUtils.i(TAG, "sd卡中不存在大礼物，需要下载");
                            }
                        }
                    }
                }
            }
        }
        return giftLists;
    }

    /**
     * 下载礼物
     *
     * @param list
     */
    public void downloadGift(final Context context, final ArrayList<GiftListBean> list) {
        if (context == null || list.size() == 0) {
            return;
        }
        ArrayList<GiftListBean> result = addDownloadListData(list);
        if (result != null && result.size() > 0) {
            Observable.fromIterable(result)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<GiftListBean, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull GiftListBean data) throws Exception {
                            return downLoadBigLift(data, context);
                        }
                    })
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aVoid) throws Exception {

                        }
                    });
        }
    }


    /**
     * 获取礼物列表数据库中数据库对象
     *
     * @return
     */
    public HnGiftListDB getHnGiftListDB(Context context) {
        if (mHnGIftListDaoManager == null) {
            mHnGIftListDaoManager = HnGiftListDB.getInstance(context);
        }
        return mHnGIftListDaoManager;
    }


    /**
     * 下载大礼物
     *
     * @param data    礼物数据
     * @param context
     * @return
     */
    public boolean downLoadBigLift(final GiftListBean data, final Context context) {
        if (data == null || TextUtils.isEmpty(data.getZipDownUrl())) {
            return false;
        }
        try {
            HnLogUtils.i(TAG, "正在下载zip文件");
            String zipUrl = data.getZipDownUrl();
            int firstIndex = zipUrl.lastIndexOf("/");
            final String fileName = zipUrl.substring(firstIndex + 1, zipUrl.length());
            final String dir = context.getExternalFilesDir(null)  + "/zip/";
            int lastIndex = fileName.lastIndexOf(".");
            final String zipdir = context.getExternalFilesDir(null)  + "/zip/" + fileName.substring(0, lastIndex);
            final String path = dir + fileName;
            HnLogUtils.i(TAG, "文件包下载的地址:" + zipUrl);
            HnLogUtils.i(TAG, "文件包本地存储的地址:" + path + "--->" + zipdir);
            File downloadFile = null;
            //主目录
            File mainFile = new File(dir);
            if (!mainFile.exists()) {
                mainFile.mkdirs();
            }
            File zipFile = new File(zipdir);
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            }
            if (mainFile != null && mainFile.exists()) {
                downloadFile = new File(path);
                if (downloadFile.exists()) {
                    downloadFile.delete();
                }
                downloadFile.createNewFile();
                HnLogUtils.i(TAG, "文件是否创建成功：" + downloadFile.exists());
                HnDownloadFileControl.down(zipUrl, path, new HnDownloadFileControl.DownStutaListener() {
                    @Override
                    public void downloadSuccess(String url, final String path) {
                        Observable.just("1")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        //更新数据
                                        File file = new File(path);
                                        if (file.exists()) {
                                        }
                                        if (unZip(file.getAbsolutePath(), zipdir)) {
                                            data.setZipDownLocalUrl(zipdir);
                                            getHnGiftListDB(context);
                                            mHnGIftListDaoManager.update(data, data.getGift_id());
                                            HnLogUtils.i(TAG, "zip文件下载完成" + zipdir);
                                            if (listener != null) {
                                                listener.downloadGiftSuccess(false, data, null);
                                            }
                                        } else {
                                            if (listener != null) {
                                                listener.downloadGiftFail(1, "解压失败", data);
                                            }
                                        }
                                        HnFileUtils.deleteFile(file);
                                        removeDownloadListData(data);
                                    }
                                });
                    }

                    @Override
                    public void downloadError(final int code, final String msg) {
                        Observable.just("1")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        if (listener != null) {
                                            listener.downloadGiftFail(code, msg, data);
                                        }
//                                        file.delete();
                                        HnLogUtils.i(TAG, "zip文件下载失败:" + msg);

                                    }
                                });
                    }
                });
//


//                HnHttpUtils.downloadFile(true, zipUrl, new FileAsyncHttpResponseHandler(downloadFile) {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                        if (listener != null) {
//                            listener.downloadGiftFail(statusCode, throwable.getMessage(), data);
//                        }
//                        file.delete();
//                        HnLogUtils.i(TAG, "zip文件下载失败:" + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, File file) {
//                        //更新数据
//                        if (unZip(file.getAbsolutePath(), zipdir)) {
//                            data.setZipDownLocalUrl(zipdir);
//                            getHnGiftListDB(context);
//                            mHnGIftListDaoManager.update(data, data.getGift_id());
//                            HnLogUtils.i(TAG, "zip文件下载完成" + zipdir);
//                            if (listener != null) {
//                                listener.downloadGiftSuccess(false, data, null);
//                            }
//                        } else {
//                            if (listener != null) {
//                                listener.downloadGiftFail(statusCode, "解压失败", data);
//                            }
//                        }
//                        file.delete();
//                        removeDownloadListData(data);
//
//                    }
//                });
            }
        } catch (Exception e) {
            HnLogUtils.i(TAG, "下载出现异常：" + e.getMessage());
        }
        return true;

    }


    /**
     * 下载某个大礼物后是否马上显示
     *
     * @param data    礼物数据
     * @param context 上下文
     * @param isShow  下载后是否显示
     * @return
     */
    public boolean downLoadBigLift(final boolean isShow, final GiftListBean data, final Context context, final Object object) {
        if (data == null || TextUtils.isEmpty(data.getZipDownUrl())) {
            return false;
        }
        ArrayList<GiftListBean> result = addDownloadListData(data);
        if (result != null && result.size() > 0) {
            try {
                HnLogUtils.i(TAG, "正在下载zip文件");
                final String zipUrl = data.getZipDownUrl();
                int firstIndex = zipUrl.lastIndexOf("/");
                final String fileName = zipUrl.substring(firstIndex + 1, zipUrl.length());
                final String dir = Big_Gift_Local_Url + "/zip/";
                int lastIndex = fileName.lastIndexOf(".");
                final String zipdir = Big_Gift_Local_Url  + "/zip/" + fileName.substring(0, lastIndex);
                final String path = dir + fileName;
                HnLogUtils.i(TAG, "文件包下载的地址:" + zipUrl);
                HnLogUtils.i(TAG, "文件包本地存储的地址:" + path + "-->" + zipdir);
                File downloadFile = null;
                //主目录
                File mainFile = new File(dir);
                if (!mainFile.exists()) {
                    mainFile.mkdirs();
                }
                File zipFile = new File(zipdir);
                if (!zipFile.exists()) {
                    zipFile.mkdirs();
                }
                if (mainFile != null && mainFile.exists()) {
                    downloadFile = new File(path);
                    if (downloadFile.exists()) {
                        downloadFile.delete();
                    }
                    downloadFile.createNewFile();
                    HnLogUtils.i(TAG, "文件是否创建成功：" + downloadFile.exists());
                    HnDownloadFileControl.down(zipUrl, path, new HnDownloadFileControl.DownStutaListener() {
                        @Override
                        public void downloadSuccess(String url, final String path) {
                            Observable.just("1")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String s) throws Exception {
                                            //更新数据
                                            File file = new File(path);
                                            if (file.exists()) {
                                            }
                                            if (unZip(file.getAbsolutePath(), zipdir)) {
                                                data.setZipDownLocalUrl(zipdir);
                                                getHnGiftListDB(context);
                                                mHnGIftListDaoManager.update(data, data.getGift_id());
                                                HnLogUtils.i(TAG, "zip文件下载完成" + zipdir);
                                                if (listener != null) {
                                                    listener.downloadGiftSuccess(isShow, data, object);
                                                }
                                            } else {
                                                if (listener != null) {
                                                    listener.downloadGiftFail(1001, "解压失败", data);
                                                }

                                            }
                                            HnFileUtils.deleteFile(file);
                                            removeDownloadListData(data);
                                        }
                                    });
                        }

                        @Override
                        public void downloadError(final int code, final String msg) {
                            Observable.just("1")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String s) throws Exception {
                                            if (listener != null) {
                                                listener.downloadGiftFail(code, msg, data);
                                            }
//                                            file.delete();
                                            removeDownloadListData(data);
                                            HnLogUtils.i(TAG, "zip文件下载失败:" + msg);
                                        }
                                    });
                        }
                    });

//                    HnHttpUtils.downloadFile(true, zipUrl, new FileAsyncHttpResponseHandler(downloadFile) {
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                            if (listener != null) {
//                                listener.downloadGiftFail(statusCode, throwable.getMessage(), data);
//                            }
//                            file.delete();
//                            removeDownloadListData(data);
//                            HnLogUtils.i(TAG, "zip文件下载失败:" + throwable.getMessage());
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, File file) {
//                            //更新数据
//                            if (unZip(file.getAbsolutePath(), zipdir)) {
//                                data.setZipDownLocalUrl(zipdir);
//                                getHnGiftListDB(context);
//                                mHnGIftListDaoManager.update(data, data.getGift_id());
//                                HnLogUtils.i(TAG, "zip文件下载完成" + zipdir);
//                                if (listener != null) {
//                                    listener.downloadGiftSuccess(isShow, data, object);
//                                }
//                            } else {
//                                if (listener != null) {
//                                    listener.downloadGiftFail(statusCode, "解压失败", data);
//                                }
//
//                            }
//                            file.delete();
//                            removeDownloadListData(data);
//
//                        }
//                    });
                }
            } catch (Exception e) {
                HnLogUtils.i(TAG, "下载出现异常：" + e.getMessage());
            }
        }
        return true;


    }


    /**
     * 获取需要下载的礼物列表id集合
     *
     * @return
     */
    public List<String> getGiftIdList() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < downloadGiftLists.size(); i++) {
            ids.add(downloadGiftLists.get(i).getGift_id());
        }
        return ids;
    }

    public void removeDownloadListData(GiftListBean bean) {
        if (bean != null) {
            HnLogUtils.i(TAG, "移除需要下载元素之前的集合长度：" + downloadGiftLists.size());
            for (int i = 0; i < downloadGiftLists.size(); i++) {
                String id = downloadGiftLists.get(i).getGift_id();
                String gift_id = bean.getGift_id();
                if (id.equals(gift_id)) {
                    downloadGiftLists.remove(bean);
                }
            }
            HnLogUtils.i(TAG, "移除需要下载元素之后的集合长度：" + downloadGiftLists.size());
        }
        if (downloadGiftLists != null && downloadGiftLists.size() == 0) {
            closeDB();
        }
    }


    /**
     * 添加下载列表中的元素
     *
     * @param list
     */
    private ArrayList<GiftListBean> addDownloadListData(ArrayList<GiftListBean> list) {
        ArrayList<GiftListBean> data = new ArrayList<>();
        getGiftIdList();
        HnLogUtils.i(TAG, "添加元素之前的下载列表长度:" + downloadGiftLists.size());
        for (int i = 0; i < list.size(); i++) {
            String id = list.get(i).getGift_id();
            List<String> giftIdList = getGiftIdList();
            if (!giftIdList.contains(id)) {
                downloadGiftLists.add(list.get(i));
                data.add(list.get(i));
            }
        }
        HnLogUtils.i(TAG, "添加元素之后的下载列表长度:" + downloadGiftLists.size());
        return data;
    }


    /**
     * 添加下载列表中的元素
     *
     * @param list
     */
    private ArrayList<GiftListBean> addDownloadListData(GiftListBean list) {
        ArrayList<GiftListBean> data = new ArrayList<>();
        getGiftIdList();
        HnLogUtils.i(TAG, "添加元素之前的下载列表长度:" + downloadGiftLists.size());
        String id = list.getGift_id();
        List<String> giftIdList = getGiftIdList();
        if (!giftIdList.contains(id)) {
            downloadGiftLists.add(list);

        }
        data.add(list);
        HnLogUtils.i(TAG, "添加元素之后的下载列表长度:" + downloadGiftLists.size());
        return data;
    }

    /**
     * 从数据库中查询礼物列表
     */
    public ArrayList<GiftListBean> quaryListFromDB(Context context) {
        getHnGiftListDB(context);
        return mHnGIftListDaoManager.queryAll();
    }

    /**
     * 从数据库中查询上架礼物列表
     */
    public ArrayList<GiftListBean> quaryPutawaListFromDB(Context context) {
        getHnGiftListDB(context);
        return mHnGIftListDaoManager.queryAllForPutawayGift();
    }

    public void setHnDonwloadGiftStateListener(HnDonwloadGiftStateListener hnDonwloadGiftStateListener) {
        this.listener = hnDonwloadGiftStateListener;
    }

    /**
     * 解压
     *
     * @param zipFile
     * @param targetDir
     */
    private boolean unZip(String zipFile, String targetDir) {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    HnLogUtils.i("Unzip: ", "=" + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(targetDir, strEntry);
                    File entryDir = new File(targetDir);
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
            HnLogUtils.i(TAG, "解析成功");
        } catch (Exception cwj) {
            HnLogUtils.i(TAG, "解压失败:" + cwj.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 更新礼物gif
     *
     * @param context
     * @param giftId
     * @param giftUrl
     */
    public void updataGiftGifData(Context context, String giftId, String giftUrl) {
        getHnGiftListDB(context);
        if (mHnGIftListDaoManager != null) {
            mHnGIftListDaoManager.update(1, giftId, giftUrl);
        }
        closeDB();
    }


    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (mHnGIftListDaoManager != null) {
            mHnGIftListDaoManager.closeDB();
        }
    }

}
