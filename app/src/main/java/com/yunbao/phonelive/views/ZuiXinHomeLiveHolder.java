package com.yunbao.phonelive.views;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.yunbao.phonelive.R;

public class ZuiXinHomeLiveHolder extends AbsMainChildTopViewHolder {

    public ZuiXinHomeLiveHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zuixin_home_live;
    }

    @Override
    public void init() {
        super.init();
        Log.e("MainHomeViewHolder", "ZuiXinHomeLiveHolder");
    }
}
