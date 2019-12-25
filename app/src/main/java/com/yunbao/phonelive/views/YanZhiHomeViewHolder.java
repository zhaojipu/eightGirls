package com.yunbao.phonelive.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.MainHomeHotAdapter;
import com.yunbao.phonelive.adapter.RefreshAdapter;
import com.yunbao.phonelive.bean.HotBean;
import com.yunbao.phonelive.bean.LiveBean;
import com.yunbao.phonelive.custom.ItemDecoration;
import com.yunbao.phonelive.custom.RefreshView;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.OnItemClickListener;
import com.yunbao.phonelive.utils.LiveStorge;
import com.yunbao.phonelive.utils.LocalImageViewHolder;

import java.util.ArrayList;
import java.util.List;

public class YanZhiHomeViewHolder extends AbsMainChildTopViewHolder implements OnItemClickListener<LiveBean> {

    private MainHomeHotAdapter mAdapter;
    private ConvenientBanner mBanner;


    public YanZhiHomeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.yanzhi_home_view;
    }

    @Override
    public void init() {
        Log.e("MainHomeViewHolder", "YanZhiHomeViewHolder");
        super.init();
        mBanner = (ConvenientBanner) findViewById(R.id.banner);
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_live);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<LiveBean>() {
            @Override
            public RefreshAdapter<LiveBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainHomeHotAdapter(mContext);
                    mAdapter.setOnItemClickListener(YanZhiHomeViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHot(p, callback);
            }

            @Override
            public List<LiveBean> processData(String[] info) {
                return JSON.parseArray(JSON.parseObject(info[0]).getString("list"), LiveBean.class);
            }

            @Override
            public void onRefresh(List<LiveBean> list) {
                LiveStorge.getInstance().put(Constants.LIVE_HOME, list);
            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 10) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        getHot();
    }


    @Override
    public void onItemClick(LiveBean bean, int position) {
        watchLive(bean, Constants.LIVE_HOME, position);
    }

    @Override
    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    private void getHot() {
        HttpUtil.getHot(1, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                HotBean hotBean = new Gson().fromJson(new String(info[0]), HotBean.class);
                initBanner(hotBean);
            }
        });
    }


    private void initBanner(HotBean hotBean) {
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < hotBean.getSlide().size(); i++) {
            imgs.add(hotBean.getSlide().get(i).getSlide_pic());
        }
        mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public LocalImageViewHolder createHolder(View itemView) {
                return new LocalImageViewHolder(mContext, itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.layout_banner;
            }
        }, imgs)
                .setPageIndicator(new int[]{R.mipmap.indicator_point_select, R.mipmap.indicator_point_nomal})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
        mBanner.startTurning(3000);
    }
}



















