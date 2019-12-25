package com.yunbao.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.LiveClassBean;
import com.yunbao.phonelive.custom.MyRadioButton;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.interfaces.OnItemClickListener;

import java.util.List;

/**
 * Created by cxf on 2018/10/7.
 */

public class LiveReadyClassAdapter extends RecyclerView.Adapter<LiveReadyClassAdapter.Vh> {

    private List<LiveClassBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<LiveClassBean> mOnItemClickListener;

    public LiveReadyClassAdapter(Context context, List<LiveClassBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((LiveClassBean) tag, 0);
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<LiveClassBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_ready_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;
        TextView mDes;
        MyRadioButton mRadioButton;

        public Vh(View itemView) {
            super(itemView);
            mThumb = (ImageView) itemView.findViewById(R.id.thumb);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDes = (TextView) itemView.findViewById(R.id.des);
            mRadioButton = (MyRadioButton) itemView.findViewById(R.id.radioButton);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveClassBean bean) {
            itemView.setTag(bean);
            ImgLoader.display(bean.getThumb(), mThumb);
            mName.setText(bean.getName());
            mDes.setText(bean.getDes());
            mRadioButton.doChecked(bean.isChecked());
        }
    }
}
