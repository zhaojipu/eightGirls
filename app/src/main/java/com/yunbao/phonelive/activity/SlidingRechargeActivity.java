package com.yunbao.phonelive.activity;

import android.widget.ListView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.SlidingRechargeAdapter;
import com.yunbao.phonelive.bean.RechargeBean;

import java.util.ArrayList;
import java.util.List;

public class SlidingRechargeActivity extends AbsActivity {
    private ListView mListView;
    private SlidingRechargeAdapter mAdapter;
    private List<RechargeBean> mRechargeBeans;
    private int[] mTypes = new int[]{0, 1};
    private String[] quickChargeTitle=new String[]{"茜茜快充(24小时)","娜娜快充(24小时)","小可爱充值(24小时)","思思充值(24小时)"};
    private String[] quickChargeCount=new String[]{"金币10万+","金币10万+","金币10万+","金币10万+"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sliding_recharge;
    }

    @Override
    protected void main() {
        initView();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.mListView);
    }

    private void initData() {
        mRechargeBeans = new ArrayList<>();
        for (int i = 0; i < mTypes.length; i++) {
            RechargeBean rechargeBean = new RechargeBean();
            rechargeBean.setType(i);
            mRechargeBeans.add(rechargeBean);
        }

        for (int i = 0; i < quickChargeTitle.length; i++) {
            RechargeBean rechargeBean = new RechargeBean();
            rechargeBean.setType(2);
            rechargeBean.setQuickCharge(quickChargeTitle[i]);
            rechargeBean.setmUch(quickChargeCount[i]);
            mRechargeBeans.add(rechargeBean);
        }

        RechargeBean rechargeBean = new RechargeBean();
        rechargeBean.setType(3);
        mRechargeBeans.add(rechargeBean);

        mAdapter = new SlidingRechargeAdapter(this, mRechargeBeans);
        mListView.setAdapter(mAdapter);
    }
}



















