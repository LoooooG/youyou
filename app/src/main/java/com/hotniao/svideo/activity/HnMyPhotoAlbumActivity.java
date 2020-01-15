package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.dialog.HnEditHeaderDialog;
import com.hotniao.svideo.model.HnLocalImageModel;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：相册
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMyPhotoAlbumActivity extends BaseActivity {


    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;


    private CommRecyclerAdapter mAdapter;
    private List<HnLocalImageModel> mData = new ArrayList<>();

    private List<String> mDataUrl = new ArrayList<>();
    private HnEditHeaderDialog mHeaderDialog;


    public static void luncher(Activity activity, String iamges) {
        activity.startActivity(new Intent(activity, HnMyPhotoAlbumActivity.class).putExtra("images", iamges));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_photo_album;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setShowTitleBar(true);
        setTitle(R.string.my_photo_album);
        setShowSubTitle(true);
        mSubtitle.setText(R.string.save);
        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String images = "";
                for (int i = 0; i < mData.size() - 1; i++) {
                    images = images + mData.get(i).getUrl() + ",";
                }
                if (!TextUtils.isEmpty(images))
                    images = images.substring(0, images.length() - 1);
                saveAblm(images);
            }
        });
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);

    }

    @Override
    public void getInitData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("images"))) {
            String[] images = getIntent().getStringExtra("images").split(",");
            mData.clear();
            for (int i = 0; i < images.length; i++) {
                HnLocalImageModel image = new HnLocalImageModel(images[i], "url");
                mData.add(image);
            }
            images = null;
        }
        HnLocalImageModel image = new HnLocalImageModel("", "add");
        mData.add(image);


        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                HnLocalImageModel item = mData.get(position);
                if ("add".equals(item.getType())) {
                    ((FrescoImageView) holder.getView(R.id.image)).setImageURI(Uri.parse("res://" + getPackageName() + "/" + R.drawable.tianjia));
                    holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                } else {
                    ((FrescoImageView) holder.getView(R.id.image)).setController(FrescoConfig.getController(item.getUrl()));
                    holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                }


                holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.remove(position);
                        if (!"add".equals(mData.get(mData.size() - 1).getType())) {
                            HnLocalImageModel entity = new HnLocalImageModel();
                            entity.setType("add");
                            mData.add(entity);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
                holder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ("add".equals(mData.get(position).getType())) {
                            if (mHeaderDialog == null)
                                mHeaderDialog = HnEditHeaderDialog.newInstance();
                            mHeaderDialog.show(getSupportFragmentManager(), "header");
                            mHeaderDialog.setOnImageCallBack(new HnEditHeaderDialog.OnImageCallBack() {
                                @Override
                                public void onImage(Bitmap bitmap, Uri uri) {
                                    if (bitmap != null) {
                                        String fileName = HnDateUtils.getCurrentDate("yyyyMMdd").toUpperCase() + EncryptUtils.encryptMD5ToString(HnUtils.createRandom(false, 5)) + ".png";
                                        File picFile = HnPhotoUtils.bitmapToFile(bitmap, fileName);
                                        if (picFile.exists()) {
                                            upLoadImag(picFile);
                                        }
                                    }
                                }
                            });

                        } else {
                            launcher(view, position, mData);
                        }
                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_mine_photo_album;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private void upLoadImag(final File file) {
        showDoing(getResources().getString(R.string.please_wait_time), null);
        HnUpLoadPhotoControl.getTenSign(file, HnUpLoadPhotoControl.UploadImage, HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                HnFileUtils.deleteFile(file);
                done();
                HnLocalImageModel entity = new HnLocalImageModel();
                entity.setType("url");
                entity.setUrl(key);
//                HnLocalImageModel addItem = mData.get(mData.size() - 1);
//                mData.remove(mData.size()-1)
                if (mData.size() < 2)
                    mData.add(0, entity);
                else
                    mData.add(mData.size() - 1, entity);
                mAdapter.notifyDataSetChanged();
                mDataUrl.add(key);
            }

            @Override
            public void uploadProgress(int progress,int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                done();
                HnToastUtils.showToastShort(msg);
            }
        });
    }


    /**
     * 保存用户上传相册
     */
    public void saveAblm(String userImg) {
        RequestParams param = new RequestParams();
        param.put("user_img", userImg);
        HnHttpUtils.postRequest(HnUrl.SAVE_USER_INFO, param, "SAVE_USER_INFO", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnToastUtils.showToastShort("保存成功");
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg + ",请稍后再试~");
            }
        });
    }


    private void launcher(View view, int position, List<HnLocalImageModel> datas) {
        if (datas == null && datas.size() == 0) return;
        mDataUrl.clear();
        for (int i = 0; i < datas.size() - 1; i++) {
            mDataUrl.add(datas.get(i).getUrl());
        }
        Intent intent = new Intent(this, HnPhotoPagerActivity.class);
        Bundle args = new Bundle();
        args.putInt(HnPhotoPagerActivity.KEY_START_POS, Math.min(mDataUrl.size(), Math.max(0, position)));

        int[] rt = new int[2];
        view.getLocationOnScreen(rt);

        float w = view.getMeasuredWidth();
        float h = view.getMeasuredHeight();

        float x = rt[0] + w / 2;
        float y = rt[1] + h / 2;

        args.putInt(HnPhotoPagerActivity.KEY_START_X, rt[0]);
        args.putInt(HnPhotoPagerActivity.KEY_START_Y, rt[1]);
        args.putInt(HnPhotoPagerActivity.KEY_START_W, (int) w);
        args.putInt(HnPhotoPagerActivity.KEY_START_H, (int) h);
        args.putStringArrayList(HnPhotoPagerActivity.KEY_PHOTO_LIST, (ArrayList<String>) mDataUrl);

        intent.putExtras(args);
        startActivity(intent);
    }

}
