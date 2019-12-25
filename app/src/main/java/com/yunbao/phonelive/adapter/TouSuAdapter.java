package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunbao.phonelive.R;

import java.util.List;

public class TouSuAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> mData;

    public TouSuAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addData(List<String> datas){
        if (mData!=null){
            mData.clear();
        }
        mData=datas;
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
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.tousu_item, viewGroup, false);
            holder.mTusujianyi = view.findViewById(R.id.tusujianyi);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.mTusujianyi.setText(mData.get(i));
        return view;
    }

    class ViewHolder {
        public TextView mTusujianyi;
    }

}

































