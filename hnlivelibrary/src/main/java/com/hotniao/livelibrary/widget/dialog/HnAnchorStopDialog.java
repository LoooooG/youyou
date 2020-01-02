package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.BaseActivity;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * @创建者 songxuefeng
 * @创建时间 2016/8/15 17:49
 * @描述 直播间退出
 */

public class HnAnchorStopDialog extends DialogFragment implements View.OnClickListener {


    private String TAG = "HnAnchorStopDialog";

    private Activity mActivity;

    private TextView mPxDialogTitle;
    private TextView tvCancle;
    private TextView tvOk;

    /**
     * 在线人数
     */
    private long liveOnline = 0;
    /**
     * 标识符 0 主播端  1 用户端  不同端显示不同的ui
     */
    private int type = 0;
    /**
     * 直播类型  0：免费，1：VIP，2：门票，3：计时
     **/
    private String mLiveType;

    /**
     * 要结束的界面
     */
    private BaseActivity mBaseActivity;

    public static HnAnchorStopDialog getInstance(long liveTime, int type, String mLiveType) {
        HnAnchorStopDialog sDialog = new HnAnchorStopDialog();
        Bundle b = new Bundle();
        b.putLong("live_online", liveTime);
        b.putInt("type", type);
        b.putString("liveType", mLiveType);
        sDialog.setArguments(b);
        return sDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            liveOnline = bundle.getLong("live_online");
            type = bundle.getInt("type");
            mLiveType = bundle.getString("liveType");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_exit_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.live_Dialog_Style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
//        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
        alertWindow.setAttributes(params);
        //初始化组件
        initView(view);
        return dialog;
    }


    /*
    *  初始化视图
     */
    private void initView(View view) {
        mPxDialogTitle = (TextView) view.findViewById(R.id.px_dialog_title);
        tvCancle = (TextView) view.findViewById(R.id.px_dialog_cancel);
        tvCancle.setOnClickListener(this);
        tvOk = (TextView) view.findViewById(R.id.px_dialog_ok);
        tvOk.setOnClickListener(this);
        if (type == 0) {//主播端
            mPxDialogTitle.setText(String.format(getString(R.string.live_anchor_live_exit), liveOnline + ""));
        } else {//用户端
            if ("2".equals(mLiveType)) {
                mPxDialogTitle.setText(getString(R.string.live_sure_exit_men_piao_live));
                tvOk.setText(getString(R.string.live_leave_room));
            } else {
                mPxDialogTitle.setText(getString(R.string.live_sure_exit_live));
            }
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.px_dialog_cancel) {//取消
            dismiss();
        } else if (i == R.id.px_dialog_ok) {//推出直播间  进入停止直播界面
            dismiss();
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, null));
            if (type == 0) {//主播端
                Bundle bundle=new Bundle();
                bundle.putString("payType",mLiveType);
                ARouter.getInstance().build("/app/HnAnchorStopLiveActivity").with(bundle).navigation();
            } else if (mBaseActivity != null) {
                HnLogUtils.i(TAG, "结束直播界面");
                HnAppManager.getInstance().finishActivity(HnAudienceActivity.class);
            }
        }
    }


    /**
     * 设置要结束的界面
     *
     * @param activity
     */
    public void setBaseActvity(BaseActivity activity) {
        this.mBaseActivity = activity;
    }
}
