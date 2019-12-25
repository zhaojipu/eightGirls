package com.yunbao.phonelive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.DiscountRecordAdapter;

public class DiscountRecordActivity extends AbsActivity {
    private ListView mListView;
    private DiscountRecordAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_discount_record;
    }

    @Override
    protected void main() {
        initView();
    }

    private void initView(){
        mListView=findViewById(R.id.mListView);


    }
}
