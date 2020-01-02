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
 * 举报弹窗
 */
public class HnReportDialog extends DialogFragment implements View.OnClickListener {
    

    private Activity mActivity;
    private static HnReportDialog dialog;
    private  HnStarAdapter mAdapter;
    private static  String mRoomId;
    public static HnReportDialog newInstance(String roomId) {
        dialog = new HnReportDialog();
        mRoomId=roomId;
        return dialog;
    }
    private static List<HnReportDataModle.DBean.ReportBean> mData=new ArrayList<>();

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
        View view = View.inflate(mActivity, R.layout.live_dialog_report, null);

        RecyclerView mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
         mAdapter=new HnStarAdapter(mData);
        mRecycler.setAdapter(mAdapter);
        view.findViewById(R.id.mTvBack).setOnClickListener(this);

        mRecycler.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                report(mData.get(position).getContent());
                dismiss();
            }
        });
        getReportDate();

        Dialog dialog = new Dialog(mActivity, com.hn.library.R.style.PXDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(params);
        return  dialog;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.mTvBack){
            dismiss();
        }
    }

    public HnReportDialog setClickListen(SelDialogListener listener){
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void selectReport(String star);


    }
    class  HnStarAdapter extends BaseQuickAdapter<HnReportDataModle.DBean.ReportBean,BaseViewHolder>{

        public HnStarAdapter(List<HnReportDataModle.DBean.ReportBean> data) {
            super(R.layout.live_adapter_report,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HnReportDataModle.DBean.ReportBean item) {
            helper.addOnClickListener(R.id.mTvContent);
            ((TextView)helper.getView(R.id.mTvContent)).setText(item.getContent());
        }
    }

    /**
     * 获取举报数据
     */
    private void getReportDate(){
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_REPORT_INDEX, null, HnLiveUrl.LIVE_REPORT_INDEX, new HnResponseHandler<HnReportDataModle>(HnReportDataModle.class) {
            @Override
            public void hnSuccess(String response) {
                if(mActivity==null)return;
                if(model.getC()==0){
                    mData.clear();
                    mData.addAll(model.getD().getReport());
                    if(mAdapter!=null)mAdapter.notifyDataSetChanged();
                }else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
    /**
     *举报
     */
    private void report(String content){
        RequestParams params=new RequestParams();
        params.put("anchor_user_id",mRoomId);
        params.put("content",content);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_REPORT_ROOM, params, HnLiveUrl.LIVE_REPORT_ROOM, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if(mActivity==null)return;
                if(model.getC()==0){
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_report_success));
                }else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
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
