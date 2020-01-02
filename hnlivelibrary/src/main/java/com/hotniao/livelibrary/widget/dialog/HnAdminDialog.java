package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.HnReportDataModle;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理弹窗
 */
public class HnAdminDialog extends DialogFragment implements View.OnClickListener {


    private Activity mActivity;
    private static HnAdminDialog dialog;
    private static String mUserId, mIsAdmin, mAnchorId, mUserName;
    private TextView mTvAdmin, mTvShutup, mTvKicking;
    private static boolean mIsAnchor = false;


    public static HnAdminDialog newInstance(String name, String userId, String isAdmin, String anchorId, boolean isAnchor) {
        dialog = new HnAdminDialog();
        mUserId = userId;
        mIsAdmin = isAdmin;
        mAnchorId = anchorId;
        mUserName = name;
        mIsAnchor = isAnchor;
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_admin, null);

        mTvAdmin = (TextView) view.findViewById(R.id.mTvAdmin);
        mTvShutup = (TextView) view.findViewById(R.id.mTvShutup);
        mTvKicking = (TextView) view.findViewById(R.id.mTvKicking);
        view.findViewById(R.id.mTvBack).setOnClickListener(this);
        mTvShutup.setOnClickListener(this);
        mTvAdmin.setOnClickListener(this);
        mTvKicking.setOnClickListener(this);
        if ("Y".equals(mIsAdmin)) {
            mTvAdmin.setText(R.string.live_cancle_admin);
        } else {
            mTvAdmin.setText(R.string.live_set_admin);
        }

        if (!mIsAnchor) mTvAdmin.setVisibility(View.GONE);

        Dialog dialog = new Dialog(mActivity, com.hn.library.R.style.PXDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(params);
        return dialog;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mTvBack) {
            dismiss();
        } else if (v.getId() == R.id.mTvAdmin) {
            addOrDelAdmin(mUserId, mIsAdmin);
        } else if (v.getId() == R.id.mTvShutup) {
            shutUpUser(mUserId, mAnchorId);
        } else if (v.getId() == R.id.mTvKicking) {
            kickingUser(mUserId, mAnchorId);
        }
    }

    public HnAdminDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void selectReport(String star);


    }


    /**
     * 取消/添加管理员
     *
     * @param userId  用户ID
     * @param isAdmin 是否管理员
     */
    private void addOrDelAdmin(String userId, final String isAdmin) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        String url = "";
        if ("Y".equals(isAdmin)) url = HnLiveUrl.LIVE_ANCHOR_DELROOM_ADMIN;
        else url = HnLiveUrl.LIVE_ANCHOR_ADDROOM_ADMIN;
        HnHttpUtils.postRequest(url, params, url, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                if (model.getC() == 0) {
                    if ("Y".equals(isAdmin))
                        HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_cancle_admin_success));
                    else
                        HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_set_admin_success));
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
                dismiss();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
                dismiss();
            }
        });
    }

    /**
     * 禁言
     *
     * @param userId   用户ID
     * @param anchorId 主播Id
     */
    private void kickingUser(String userId, final String anchorId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("anchor_user_id", anchorId);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_ROOM_KICK, params, HnLiveUrl.LIVE_ROOM_KICK, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                if (model.getC() == 0) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_kicking_people_succrss));
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
                dismiss();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
                dismiss();
            }
        });
    }

    /**
     * 踢人
     *
     * @param userId   用户ID
     * @param anchorId 主播Id
     */
    private void shutUpUser(String userId, final String anchorId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("anchor_user_id", anchorId);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_ROOM_PROHIBIT_TALK, params, HnLiveUrl.LIVE_ROOM_PROHIBIT_TALK, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                if (model.getC() == 0) {
                    HnToastUtils.showToastShort(mUserName + HnUiUtils.getString(R.string.live_shut_up_success));
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
                dismiss();
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
                dismiss();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
