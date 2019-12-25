package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunbao.phonelive.bean.GuiZuTypeBean;

import java.util.List;

public class GuiZuAdapter extends BaseAdapter {

    private Context context;

    private List<GuiZuTypeBean> mData;

    public GuiZuAdapter(Context context,List<GuiZuTypeBean> data){
        this.context=context;
        this.mData=data;

    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}











