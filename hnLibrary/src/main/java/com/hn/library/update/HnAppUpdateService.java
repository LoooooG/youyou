package com.hn.library.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hn.library.R;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;

import java.io.File;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 应用更新下载的服务
 * 创建人：Kevin
 * 创建时间：2016/6/16 14:52
 * 修改人：Kevin
 * 修改时间：2016/6/16 14:52
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAppUpdateService extends Service {

    private DownloadManager dm;
    private long mDownLoadId = 0;
    private DownloadCompleteReceiver mReceiver;

    private Uri destinationUri;

    @Override
    public void onCreate() {
        super.onCreate();
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String downLoadUrl = intent.getStringExtra("downLoadUrl");
            mReceiver = new DownloadCompleteReceiver();
            registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            startDownload(downLoadUrl);
        } catch (Exception e) {
        }
        return Service.START_STICKY;
    }

    /**
     * 开启下载
     */
    private void startDownload(String downLoadUrl) {

        if (!HnUtils.checkConnectionOk(this)) {
            HnToastUtils.showToastShort("无网络服务,请检查您的网络！");
            return;
        }

        if (mDownLoadId != 0) {
            HnToastUtils.showToastShort("已经在下载队列中");
            return;
        }

        Uri uri = Uri.parse(downLoadUrl);

        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
            HnToastUtils.showToastShort("下载链接不正确");
            return;
        }

        //得到连接请求对象
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //指定在什么网络下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        File file = new File(getDiskFileDir(this), "update.apk");
        if (file.exists()) {
            file.delete();
            HnLogUtils.i("文件存在");
        }

        destinationUri = Uri.fromFile(file);
        request.setDestinationUri(destinationUri);
        //指定下载文件的类型
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        //下载完成后可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //表示允许MediaScanner扫描到这个文件，默认不允许。
        request.allowScanningByMediaScanner();
        //设置下载中通知栏的标题
        request.setTitle(getResources().getString(R.string.app_name));
        //设置下载中通知栏提示的介绍
        //request.setDescription("");

        try {
            mDownLoadId = dm.enqueue(request);
            HnLogUtils.i("mDownLoadId", "mDownLoadId=" + mDownLoadId);
        } catch (Exception e) {
            HnLogUtils.i("NumberFormatException" + e.getMessage());
        }

    }


    /**
     * 下载完成的广播接受者
     */
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long downLoadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Uri downloadFileUri = destinationUri;

            if ((downLoadID == mDownLoadId) && (destinationUri != null)) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.addCategory(Intent.CATEGORY_DEFAULT);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(install);
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
        HnLogUtils.i("update", "onDestroy");
    }

    /**
     * 获取硬盘文件存储的位置
     *
     * @param context
     * @return
     */
    public String getDiskFileDir(Context context) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(null).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

}
