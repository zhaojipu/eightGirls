package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.DiscountRecordBean;

import org.w3c.dom.Text;

import java.util.List;

public class DiscountRecordAdapter extends BaseAdapter {

    private List<DiscountRecordBean> mList;
    private Context context;
    private LayoutInflater inflater;

    public DiscountRecordAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setList(List<DiscountRecordBean> beans){
        if (beans!=null){
            beans.clear();
        }

        this.mList=beans;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view=inflater.inflate(R.layout.discount_record_item,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.mEnoyView.setText(mList.get(i).getAmount());
        holder.mDateView.setText(mList.get(i).getDate());
        holder.mDiscountTypeView.setText(mList.get(i).getDiscountType());
        return view;
    }

    class ViewHolder{
        private TextView mDiscountTypeView;
        private TextView mDateView;
        private TextView mEnoyView;

        public ViewHolder(View view){
            mDiscountTypeView=view.findViewById(R.id.discount_type);
            mDateView=view.findViewById(R.id.date);
            mEnoyView=view.findViewById(R.id.menoy);

        }
    }


}
















