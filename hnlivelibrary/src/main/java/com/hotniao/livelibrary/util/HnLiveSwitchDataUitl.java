package com.hotniao.livelibrary.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.global.HnUrl;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.HnLiveRoomSwitchModel;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLiveSwitchDataUitl {
    private static String oldId="-1";
    public static void joinRoom(final Context context, String cateId, String payType, final String anchorId,String gameId) {
        if(!"-1".equals(oldId))return;
        oldId=anchorId;
        RequestParams params = new RequestParams();
        params.put("anchor_category_id", cateId);
        params.put("anchor_user_id", anchorId);
        params.put("anchor_live_pay", payType);
        params.put("game_category_id", gameId);
        HnHttpUtils.postRequest(HnUrl.LIVE_ROOM_SWITCH, params, HnUrl.LIVE_ROOM_SWITCH, new HnResponseHandler<HnLiveRoomSwitchModel>(HnLiveRoomSwitchModel.class) {
            @Override
            public void hnSuccess(String response) {
                oldId="-1";
                if (context == null) return;
                if (model.getD() == null) return;
                List<HnLiveRoomSwitchModel.DBean.AnchorsBean> datas = model.getD().getAnchors();
                if (datas != null && datas.size() > 0) {
                    HnLiveListModel model = new HnLiveListModel();
                    List<HnLiveListModel.LiveListBean> list = new ArrayList<HnLiveListModel.LiveListBean>();
                    for (int i = 0; i < datas.size(); i++) {
                        if (anchorId.equals(datas.get(i).getUser_id()))
                            model.setPos(i);

                        HnLiveListModel.LiveListBean bean = new HnLiveListModel.LiveListBean("",
                                datas.get(i).getUser_id(), datas.get(i).getUser_avatar());
                        list.add(bean);
                    }
                    model.setList(list);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", model);
                    context.startActivity(new Intent(context, HnAudienceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(bundle));
                }else {
                    HnToastUtils.showToastShort("主播已离开~");
                    EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.RefreshLiveList, model));
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                oldId="-1";
                EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.RefreshLiveList, msg));
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
