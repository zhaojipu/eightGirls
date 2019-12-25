package com.yunbao.phonelive.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.DiscountRecordAdapter;
import com.yunbao.phonelive.bean.DiscountRecordBean;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.AllTiXianRecordCallBack;

import java.util.ArrayList;
import java.util.List;

public class TiXianRecordActivity extends AbsActivity {

    private ListView mListView;
    private DiscountRecordAdapter mAdapter;
    private ImageView btn_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ti_xian_record;
    }

    @Override
    protected void main() {
        initView();
        initData();
    }

    private void initData() {
        HttpUtil.promotionRecord(AppConfig.POMOTION_TIXIAN_RECORD,new AllTiXianRecordCallBack() {
            @Override
            public void recordCallBack(List<DiscountRecordBean> beans) {
                mAdapter.setList(beans);
                mListView.setAdapter(mAdapter);
            }
        });
    }

    private void initView() {
        mListView = findViewById(R.id.mListView);
        mAdapter=new DiscountRecordAdapter(this);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}







































