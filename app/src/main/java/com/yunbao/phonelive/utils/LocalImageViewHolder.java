package com.yunbao.phonelive.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.phonelive.R;

public class LocalImageViewHolder extends Holder<String> {
    private RoundedImageView imageView;
    private Context mContext;

    public LocalImageViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.iv_img);
    }

    @Override
    public void updateUI(String data) {
        try {

            Glide.with(mContext)
                    .load(data)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
