package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.RechargeBean;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.interfaces.OnItemClickListener;
import com.yunbao.phonelive.utils.DpUtil;

import java.util.List;

public class MainMeAdapter1 extends RecyclerView.Adapter<MainMeAdapter1.Vh> {
    private List<RechargeBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<RechargeBean> mOnItemClickListener;

    public MainMeAdapter1(Context context, List<RechargeBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    RechargeBean bean = (RechargeBean) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<RechargeBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        RechargeBean bean = mList.get(position);
        return bean.getType();
    }


    public void setList(List<RechargeBean> list) {
        if (list == null) {
            return;
        }
        boolean changed = false;
        if (mList.size() != list.size()) {
            changed = true;
        } else {
            for (int i = 0, size = mList.size(); i < size; i++) {
                if (!mList.get(i).equals(list.get(i))) {
                    changed = true;
                    break;
                }
            }
        }
        if (changed) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MainMeAdapter1.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res=0;
        switch (viewType){
            case 0:
                res=R.layout.item_main_me_0;
                break;
            case 1:
                res=R.layout.item_main_me_1;
                break;
            case 2:
                res=R.layout.item_main_me_2;
                break;
            case 3:
                res=R.layout.item_main_me_3;
                break;
            default:
                break;
        }
        return new MainMeAdapter1.Vh(mInflater.inflate(res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainMeAdapter1.Vh vh, int position) {
        vh.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(RechargeBean bean) {
            itemView.setTag(bean);
            mName.setText(bean.getMineText());

        }
    }
}


























