package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.RechargeBean;

import java.util.List;

public class SlidingRechargeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<RechargeBean> mData;
    private Context mContext;

    public SlidingRechargeAdapter(Context context, List<RechargeBean> list) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = list;
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
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        RechargeBean bean = (RechargeBean) getItem(position);
        return bean.getType();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        ViewHolder holder = null;
        if (view == null) {
            switch (type) {
                case 0:
                    view = inflater.inflate(R.layout.sliding_recharge_head, viewGroup, false);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.sliding_recharger_body1, viewGroup, false);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.sliding_recharger_body2, viewGroup, false);
                    holder = new ViewHolder();
                    holder.mQuickCharge = view.findViewById(R.id.mQuickCharge);
                    holder.mUch = view.findViewById(R.id.mUch);
                    break;
                case 3:
                    view = inflater.inflate(R.layout.sliding_reacharger_footer, viewGroup, false);
                    break;
                default:
                    break;
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        switch (type){
            case 2:
                holder.mQuickCharge.setText(mData.get(i).getQuickCharge());
                holder.mUch.setText(mData.get(i).getmUch());
                break;
            default:
                break;
        }

        return view;
    }

    class ViewHolder {
        public TextView mQuickCharge;
        public TextView mUch;
    }


}
