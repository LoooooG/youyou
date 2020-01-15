package com.hotniao.svideo.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.UploadFileResponseModel;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginBean;
import com.hn.library.model.HnLoginModel;
import com.hn.library.picker.address_picker.City;
import com.hn.library.picker.address_picker.HnAddressPickerTask;
import com.hn.library.picker.address_picker.Province;
import com.hn.library.picker.date_picker.HnDatePickerDialog;
import com.hn.library.utils.DateUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.HnMainActivity;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.bindPhone.HnFirstBindPhoneActivity;
import com.hotniao.svideo.activity.bindPhone.HnHaveBindPhoneActivity;
import com.hotniao.svideo.biz.home.HnHomeCate;
import com.hotniao.svideo.biz.user.userinfo.HnMineBiz;
import com.hotniao.svideo.dialog.HnEditSexDialog;
import com.hotniao.svideo.dialog.HnUserFellingsDialog;
import com.hotniao.svideo.model.HnAuthDetailModel;
import com.hotniao.svideo.model.HnLocalImageModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;
import com.videolibrary.activity.HnChooseVideoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：编辑个人资料
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnEditInfoActivity extends BaseActivity implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {


    @BindView(R.id.fiv_header)
    FrescoImageView fivHeader;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_nick)
    TextView tvNick;


    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.loading)
    HnLoadingLayout loading;
    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.tv_sig)
    TextView tvSig;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.mTvLv)
    HnSkinTextView mTvLv;
    @BindView(R.id.mTvAnchorLv)
    HnSkinTextView mTvAnchorLv;
    @BindView(R.id.mTvRealName)
    TextView mTvRealName;
    @BindView(R.id.mTvBind)
    TextView mTvBindView;
    @BindView(R.id.mRlAnchorLv)
    RelativeLayout mRlAnchorLv;
    @BindView(R.id.mRecyclerImg)
    RecyclerView mRecyclerImg;

    @BindView(R.id.mTvAge)
    TextView mTvAge;
    @BindView(R.id.mTvArea)
    TextView mTvArea;
    @BindView(R.id.mTvStar)
    TextView mTvStar;
    @BindView(R.id.mTvLove)
    TextView mTvLove;
    @BindView(R.id.mTvJob)
    TextView mTvJob;
    @BindView(R.id.mTvFeelings)
    TextView mTvFeelings;
    @BindView(R.id.mRlRealName)
    RelativeLayout mRlRealName;
    @BindView(R.id.mRlBind)
    RelativeLayout mRlBind;

    //我的业务逻辑类，用户处理我的相关业务
    private HnMineBiz mHnMineBiz;
    //个人用户信息数据对象
    private HnLoginBean result;
    //性别选择器
    private HnEditSexDialog mSexDialog;
    //实名认证状态  2是通过
    private String mRealNameState = "0";

    private HnAddressPickerTask mAreaTask;
    private HnDatePickerDialog mDateDialog;

    private List<HnLocalImageModel> mAblmData = new ArrayList<>();
    private CommRecyclerAdapter mAblmAdapter;

    private String mVideoStatue;


    @OnClick({R.id.fiv_header, R.id.mRlNick, R.id.mRlSex, R.id.mRlIntro,
            R.id.mRlLv, R.id.mRlAnchorLv, R.id.mRlRealName, R.id.mRlBind,
            R.id.mRlImg, R.id.mRlAge, R.id.mRlArea, R.id.mRlStar,
            R.id.mRlLove, R.id.mRlJob, R.id.mRlFeelings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fiv_header://头像
                mHnMineBiz.updateHeader();
                break;
            case R.id.mRlNick://昵称
                String nick = tvNick.getText().toString();
                HnEditNickInfoActivity.launcher(this, "请输入昵称", TextUtils.isEmpty(nick) ? "" : nick, HnEditNickInfoActivity.Nick);
                break;
            case R.id.mRlSex://性别
                mSexDialog = HnEditSexDialog.newInstance();
                mSexDialog.show(getSupportFragmentManager(), "sex");
                break;
            case R.id.mRlIntro://签名
                String intro = HnApplication.getmUserBean().getUser_intro();
                HnEditNickInfoActivity.launcher(this, "设置您的个性签名", TextUtils.isEmpty(intro) ? "" : intro, HnEditNickInfoActivity.Sign);
                break;
            case R.id.mRlLv:
                HnWebActivity.luncher(HnEditInfoActivity.this, getString(R.string.user_level), HnUrl.USER_LEVEL_USER, HnWebActivity.Level);
                break;
            case R.id.mRlAnchorLv:
                HnWebActivity.luncher(HnEditInfoActivity.this, getString(R.string.zhubo_lv), HnUrl.USER_LEVEL_ANCHOR, HnWebActivity.Level);
                break;
            case R.id.mRlRealName:
                checkAnchorStatus(true);
                break;
            case R.id.mRlBind:
                if (TextUtils.isEmpty(result.getUser_phone())) {
                    openActivity(HnFirstBindPhoneActivity.class);
                } else {
                    HnHaveBindPhoneActivity.luncher(HnEditInfoActivity.this, result.getUser_phone());
                }
                break;
            case R.id.mRlImg://相册
                HnMyPhotoAlbumActivity.luncher(this, result == null ? "" : result.getUser_img());
                break;

            case R.id.mRlAge://年龄
                showDateDialog();
                break;
            case R.id.mRlArea://地址
                Province selectProvince = mAreaTask.getSelectProvince();
                City selectCity = mAreaTask.getSelectCity();
                if (selectProvince != null && selectCity != null) {
                    mAreaTask.showAddressDialog(selectProvince, selectCity, null, false, new HnAddressPickerTask.onPickedListener() {
                        @Override
                        public void onPicked(String provinceName, String cityName, String CountName) {
                            if (mHnMineBiz != null)
                                mHnMineBiz.saveArea(provinceName + "-" + cityName);
                        }
                    });
                } else {
                    mAreaTask.showAddressDialog(new Province(""), new City(""), null, false, new HnAddressPickerTask.onPickedListener() {
                        @Override
                        public void onPicked(String provinceName, String cityName, String CountName) {
                            if (mHnMineBiz != null)
                                mHnMineBiz.saveArea(provinceName + "-" + cityName);
                        }
                    });
                }
                break;
            case R.id.mRlStar://星座
                showDateDialog();
                break;
            case R.id.mRlFeelings://情感
                HnUserFellingsDialog.newInstance().setItemClickListener(new HnUserFellingsDialog.OnClickDialogListener() {
                    @Override
                    public void clickState(String value) {
                        mHnMineBiz.saveFeeling(value);
                    }
                }).show(getFragmentManager(), "fell");

                break;
            case R.id.mRlLove://爱好
                String love = result.getUser_hobby();
                HnEditNickInfoActivity.launcher(this, "请输入您的爱好", TextUtils.isEmpty(love) ? "" : love, HnEditNickInfoActivity.Love);
                break;
            case R.id.mRlJob://工作
                String job = result.getUser_profession();
                HnEditNickInfoActivity.launcher(this, "请输入您的职业", TextUtils.isEmpty(job) ? "" : job, HnEditNickInfoActivity.Job);
                break;
        }
    }

    private void checkAnchorStatus(boolean isClick) {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.NoApply);
            }
            mTvRealName.setText("未认证");
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.Authing);
            }
            mTvRealName.setText("认证中");
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthNoPass);
            }
            mTvRealName.setText("认证不通过");
        } else if ("3".equals(mVideoStatue) || "6".equals(mVideoStatue)){
            mTvRealName.setText("已认证");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_edit_info;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        loading.setStatus(HnLoadingLayout.Loading);
        loading.setOnReloadListener(this);
        setShowBack(true);
        setTitle(R.string.edit_user_info);
        EventBus.getDefault().register(this);
        mHnMineBiz = new HnMineBiz(this);
        mHnMineBiz.setBaseRequestStateListener(this);
        mAreaTask = new HnAddressPickerTask(this, mTvArea);
        mAreaTask.execute();

        initAblmAdapter();

    }

    private void initAblmAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerImg.setLayoutManager(manager);

        mAblmAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                if ("add".equals(mAblmData.get(position).getType())) {
                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setImageURI(Uri.parse("res://" + getPackageName() + "/" + R.drawable.tianjia));
                } else {
                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(mAblmData.get(position).getUrl()));
                }
                holder.getView(R.id.mIvImg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HnMyPhotoAlbumActivity.luncher(HnEditInfoActivity.this, result.getUser_img());
                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_user_info_ablm;
            }

            @Override
            public int getItemCount() {
                return mAblmData.size();
            }
        };
        mRecyclerImg.setAdapter(mAblmAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHnMineBiz.requestToUserInfo();
    }

    @Override
    public void getInitData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            result = (HnLoginBean) bundle.getSerializable(HnConstants.Intent.DATA);
            if (result != null) {
                loading.setStatus(HnLoadingLayout.Success);
                updateUi();
            } else {
                mHnMineBiz.requestToUserInfo();
            }
        }
    }


    private void showDateDialog() {
        if (mDateDialog == null) {
            mDateDialog = new HnDatePickerDialog(this);
            mDateDialog.setBirthdayListener(new HnDatePickerDialog.OnDatePickListener() {
                @Override
                public void onClick(String year, String months, String day) {
                    if (TextUtils.isEmpty(year) || TextUtils.isEmpty(months) || TextUtils.isEmpty(day))
                        return;

                    int month = Integer.parseInt(months);
                    String mMonth = "01";
                    if (month < 10) mMonth = "0" + month;
                    else mMonth = month + "";
                    String birth = year + "-" + mMonth + "-" + day;

                    if (mHnMineBiz != null)
                        mHnMineBiz.saveAgeAndStar(birth, DateUtils.getUserStar(birth));
                }
            });
        }
        mDateDialog.show();

    }

    /**
     * 请求中
     */
    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.please_wait_time), null);
    }

    /**
     * 请求成功
     *
     * @param type
     */
    @Override
    public void requestSuccess(String type, String star, final Object obj) {
        if (loading == null || isFinishing()) return;
        if (!"user_info".equals(type)) done();
        if (HnLoadingLayout.Success != loading.getStatus()) {
            loading.setStatus(HnLoadingLayout.Success);
        }
        if ("upload_user_header".equals(type)) {//上传七牛
            UploadFileResponseModel model = (UploadFileResponseModel) obj;
            if (model != null) {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.upload_succeed));
                fivHeader.setController(FrescoConfig.getController(model.getUrl()));
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_User_Header, model.getUrl()));
            }
        } else if ("user_info".equals(type)) {//获取用户信息
            HnLoginModel model = (HnLoginModel) obj;
            if (model != null && model.getD() != null && model.getD().getUser_id() != null) {
                result = model.getD();
                updateUi();
            } else {
                loading.setStatus(HnLoadingLayout.Empty);
            }
        } else if ("save_nick".equals(type)) {//保存昵称
            String nick = (String) obj;
            tvNick.setText(nick);
            EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_User_Nick, nick));
        } else if ("save_intro".equals(type)) {//保存签名
            String intro = (String) obj;
            if (!TextUtils.isEmpty(intro)) {
                tvSig.setText(intro);
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_User_Intro, intro));
            }
        } else if ("save_avator".equals(type)) {//上传头像到自己的服务器成功
            String key = (String) obj;
            HnLogUtils.i(TAG, "key：" + key);
            if (!TextUtils.isEmpty(key)) {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.upload_succeed));
                fivHeader.setController(FrescoConfig.getController(key));
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_User_Header, key));
            }
        } else if (HnMineBiz.StarAndAge.equals(type)) {
            int diffYear = DateUtils.getAge((String) obj);
            mTvAge.setText(diffYear + "");
            mTvStar.setText(star);
            if (HnApplication.getmUserBean() != null) {
                HnApplication.getmUserBean().setUser_birth((String) obj);
                HnApplication.getmUserBean().setUser_constellation(star);
            }
        } else if (HnMineBiz.Area.equals(type)) {
            mTvArea.setText((String) obj);
            if (HnApplication.getmUserBean() != null) {
                HnApplication.getmUserBean().setUser_home_town((String) obj);
            }
        } else if (HnMineBiz.Feeling.equals(type)) {
            mTvFeelings.setText((String) obj);
            if (HnApplication.getmUserBean() != null) {
                HnApplication.getmUserBean().setUser_emotional_state((String) obj);
            }
        }


    }

    /**
     * \
     * 请求失败
     *
     * @param type
     * @param code
     * @param msg
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if (loading == null) return;
        done();
        if ("upload_user_header".equals(type)) {//上传七牛
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.upload_fail));
        } else if ("user_info".equals(type)) {//获取用户信息
            if (REQUEST_NET_ERROR == code) {
                loading.setStatus(HnLoadingLayout.No_Network);
            } else {
                loading.setStatus(HnLoadingLayout.Error);
            }
        } else if ("save_nick".equals(type) || "save_intro".equals(type) || HnMineBiz.StarAndAge.equals(type) || HnMineBiz.Feeling.equals(type)) {//保存昵称/签名
            HnToastUtils.showToastShort(msg);
        } else if ("upload_pic_file".equals(type)) {//上传身份证照
            HnToastUtils.showToastShort(msg);
        } else if ("get_qiniu_token".equals(type)) {//获取七牛token
            HnToastUtils.showToastShort(msg);
        } else if (HnMineBiz.Area.equals(type)) {
            HnToastUtils.showToastShort(msg);
            mTvArea.setText(result.getUser_home_town());
        }
    }

    /**
     * 更新界面ui
     */
    private void updateUi() {
        if (isFinishing() || result == null) return;
        checkAnchorStatus(false);
        //头像
        String logo = result.getUser_avatar();
        fivHeader.setController(FrescoConfig.getController(logo));
        //昵称
        String nick = result.getUser_nickname();
        tvNick.setText(nick);
        //性别
        String sex = result.getUser_sex();
        if ("1".equals(sex)) {//男
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
        //签名
        String intro = result.getUser_intro();
        if (!TextUtils.isEmpty(intro)) {
            tvSig.setText(intro);
        }

        mTvId.setText(result.getUser_id());
        HnLiveLevelUtil.setAudienceLevBg(mTvLv, result.getUser_level(), true);

        if (TextUtils.isEmpty(result.getAnchor_level()) || 1 > Integer.parseInt(result.getAnchor_level())) {
            mRlAnchorLv.setVisibility(View.GONE);
        } else {
            mRlAnchorLv.setVisibility(View.VISIBLE);
//            mTvAnchorLv.setText("Lv" + result.getAnchor_level());
            HnLiveLevelUtil.setAnchorLevBg(mTvAnchorLv,result.getAnchor_level(),true);
        }


        if (TextUtils.isEmpty(result.getUser_phone())) {
            mTvBindView.setText(R.string.no_bind);
        } else {
            mTvBindView.setText(R.string.have_bind);
        }

        mTvAge.setText(DateUtils.getAge(result.getUser_birth()) + "");
        mTvStar.setText(result.getUser_constellation());
        mTvArea.setText(result.getUser_home_town());
        mTvLove.setText(TextUtils.isEmpty(result.getUser_hobby())?"Ni好像忘记写爱好了":result.getUser_hobby());
        mTvJob.setText(TextUtils.isEmpty(result.getUser_profession())?"Ni好像忘记写职业了":result.getUser_profession());
        mTvFeelings.setText(result.getUser_emotional_state());

        mAblmData.clear();
        if (!TextUtils.isEmpty(result.getUser_img())) {
            String[] images = result.getUser_img().split(",");
            int len = images.length;
            if (images.length > 3) len = 3;
            for (int i = 0; i < len; i++) {
                HnLocalImageModel image = new HnLocalImageModel(images[i], "url");
                mAblmData.add(image);
            }

            images = null;
        } else {
            HnLocalImageModel image = new HnLocalImageModel("", "add");
            mAblmData.add(image);
        }
        if (mAblmAdapter != null) mAblmAdapter.notifyDataSetChanged();
    }


    @Override
    public void onReload(View v) {
        mHnMineBiz.requestToUserInfo();
    }

    @Subscribe
    public void onEventBusCallBack(EventBusBean event) {
        if (event != null) {
            if (HnConstants.EventBus.Sava_Nick.equals(event.getType())) {//更新昵称
                String nick = (String) event.getObj();
                if (TextUtils.isEmpty(nick)) return;
                mHnMineBiz.requestToSavaNick(nick);
            } else if (HnConstants.EventBus.Sava_Intro.equals(event.getType())) {//更新用户签名
                String intro = (String) event.getObj();
                mHnMineBiz.requestToSavaIntro(intro);
            } else if (HnConstants.EventBus.Update_User_Sex.equals(event.getType())) {//更新用户性别
                String sex = (String) event.getObj();
                if ("1".equals(sex)) {//男
                    tvSex.setText("男");
                } else {
                    tvSex.setText("女");
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HnEditNickInfoActivity.Nick) {
            if (result != null) result.setUser_nickname(HnApplication.mUserBean.getUser_nickname());
            tvNick.setText(TextUtils.isEmpty(HnApplication.mUserBean.getUser_nickname()) ? "" : HnApplication.mUserBean.getUser_nickname());
        } else if (requestCode == HnEditNickInfoActivity.Sign) {
            if (result != null) result.setUser_intro(HnApplication.mUserBean.getUser_intro());
            tvSig.setText(TextUtils.isEmpty(HnApplication.mUserBean.getUser_intro()) ? "" : HnApplication.mUserBean.getUser_intro());
        } else if (requestCode == HnEditNickInfoActivity.Love) {
            if (result != null) result.setUser_hobby(HnApplication.mUserBean.getUser_hobby());
            mTvLove.setText(TextUtils.isEmpty(HnApplication.mUserBean.getUser_hobby()) ? "" : HnApplication.mUserBean.getUser_hobby());
        } else if (requestCode == HnEditNickInfoActivity.Job) {
            if (result != null) result.setUser_intro(HnApplication.mUserBean.getUser_profession());
            mTvJob.setText(TextUtils.isEmpty(HnApplication.mUserBean.getUser_profession()) ? "" : HnApplication.mUserBean.getUser_profession());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置状态
     * 用户视频认证状态：0未认证 1认证中 2认证未通过 3认证通过 4审核中 5审核不通过 6审核通过
     */
    private void setStatue(final boolean isClick) {
        setTvStatue(isClick);
    }

    private void setTvStatue(boolean isClick) {
        mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            mTvRealName.setText("未认证");
            if (isClick) {
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.NoApply);
            }

        } else if ("1".equals(mVideoStatue)) {
            mTvRealName.setText("认证中");
            if (isClick) {
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.Authing);
            }

        } else if ("2".equals(mVideoStatue)) {
            mTvRealName.setText("认证未通过");
            if (isClick) {
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthNoPass);
            }

        } else if ("3".equals(mVideoStatue)) {
            mTvRealName.setText("已认证");
            if (isClick) {
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthPass);
            }

        } else if ("4".equals(mVideoStatue)) {
            mTvRealName.setText("审核中");
            if (isClick) {
                HnVideoAuthStatueActivity.luncher(this, mVideoStatue);
            }

        } else if ("5".equals(mVideoStatue)) {
            mTvRealName.setText("审核不通过");
            if (isClick) {
                HnVideoAuthStatueActivity.luncher(this, mVideoStatue);
            }

        } else if ("6".equals(mVideoStatue)) {
            mTvRealName.setText("审核通过");
            if (isClick) {
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthPass);
            }
        }
    }
}
