package com.hotniao.live.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hotniao.live.widget.scollorlayout.ScrollableHelper;

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

public abstract class BaseScollFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer  {

    public abstract void pullToRefresh();
    public abstract void refreshComplete();
}
