package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.NobleBean;

import java.util.List;


public class NobleAdapter extends BaseAdapter {
    private Context mContext;
    private List<NobleBean> mData;
    private LayoutInflater mInflater;

    public NobleAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void addData(List<NobleBean> data){
        this.mData=data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.noble_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.title.setText(mData.get(position).getTitle());
        holder.titleOne.setText(mData.get(position).getTitleOne());
        return convertView;
    }

    class ViewHolder{
        public TextView title;
        public TextView titleOne;
        public ViewHolder(View view){
            title=view.findViewById(R.id.title);
            titleOne=view.findViewById(R.id.title_one);
        }
    }
}



















