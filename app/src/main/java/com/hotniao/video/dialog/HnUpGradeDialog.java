package com.hotniao.video.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.update.HnAppUpdateService;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.utils.HnUserUtil;
import com.hotniao.video.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @创建者 songxuefeng
 * @创建时间 2016/12/8 9:50
 * @描述 升级apk
 */
@SuppressLint("NewApi")
public class HnUpGradeDialog extends DialogFragment {

    @BindView(R.id.px_dialog_description)
    TextView mPxDialogDescription;
    @BindView(R.id.px_dialog_cancel)
    Button mPxDialogCancel;
    @BindView(R.id.px_dialog_ok)
    Button mPxDialogOk;
    @BindView(R.id.dialog_btn_container)
    RelativeLayout mDialogBtnContainer;

    private Activity mActivity;
    private boolean mShowsDialog;
    private String mDownUrl;

    private String mIsForce = "N";
    private String content;

    public static HnUpGradeDialog newInstance(String downurl, boolean shouldUpdata, String is_force, String content) {
        Bundle args = new Bundle();
        args.putString("downurl", downurl);
        args.putBoolean("shouldUpdata", shouldUpdata);
        args.putString("mIsForce", is_force);
        args.putString("content", content);
        HnUpGradeDialog fragment = new HnUpGradeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Bundle bundle = getArguments();
        mDownUrl = bundle.getString("downurl");
        mIsForce = bundle.getString("mIsForce");
        content = bundle.getString("content");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = View.inflate(mActivity, R.layout.dialog_upgrade, null);
        mShowsDialog = getShowsDialog();
        ButterKnife.bind(this, rootView);
        initData();
        Dialog dialog = new Dialog(mActivity, R.style.loading);
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside("Y".equals(mIsForce) ? false : true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = ScreenUtils.dp2px(mActivity, 300f);
        alertWindow.setAttributes(params);
        mPxDialogDescription.setText(TextUtils.isEmpty(content) ? HnUiUtils.getString(R.string.str_version_new_upload) : content);
        if ("N".equals(mIsForce)) {

            mPxDialogCancel.setVisibility(View.VISIBLE);
        } else {
            mPxDialogCancel.setVisibility(View.INVISIBLE);
        }
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && "Y".equals(mIsForce)) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initData() {

    }


    @OnClick({R.id.px_dialog_cancel, R.id.px_dialog_ok})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.px_dialog_cancel:

                if (mShowsDialog) {
                    dismiss();
                }

                break;

            case R.id.px_dialog_ok:

                if (mShowsDialog) {
                    dismiss();
                }

                HnPrefUtils.setBoolean("isupdata", true);

                if (!TextUtils.isEmpty(mDownUrl)) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(mDownUrl));
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                }

                break;
        }
    }
}
