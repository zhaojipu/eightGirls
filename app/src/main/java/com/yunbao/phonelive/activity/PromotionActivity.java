package com.yunbao.phonelive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.PromotionCallBack;
import com.yunbao.phonelive.utils.SpUtil;

public class PromotionActivity extends AbsActivity implements View.OnClickListener {
    private TextView client_count;
    private TextView value;
    private Button zfb_btn;
    private Button record;
    private Button tuiguang_record;
    private ImageView btn_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_promotion;
    }

    @Override
    protected void main() {
        initView();
        initData();
    }

    private void initView() {
        btn_back = findViewById(R.id.btn_back);
        client_count = findViewById(R.id.client_count);
        value = findViewById(R.id.value);
        zfb_btn = findViewById(R.id.zfb_btn);
        record = findViewById(R.id.record);
        tuiguang_record = findViewById(R.id.tuiguang_record);
        zfb_btn.setOnClickListener(this);
        record.setOnClickListener(this);
        tuiguang_record.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void initData() {
        HttpUtil.promotion(new PromotionCallBack() {
            @Override
            public void onPromotion(String total, String invitenum) {
                SpUtil.getInstance().setStringValue("balanceCount", total);
                client_count.setText(total);
                value.setText(invitenum);
            }
        });
    }

    private void forwardZfb() {
        startActivity(new Intent(this, CoinWithdrawalActivity.class));
    }

    private void forwardTuiGuang() {
        startActivity(new Intent(this, TuiGuangRecordActivity.class));

    }

    private void forwardTiXianRecoed() {
        startActivity(new Intent(this, TiXianRecordActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tuiguang_record:
                forwardTuiGuang();
                break;
            case R.id.zfb_btn:
                forwardZfb();
                break;
            case R.id.record:
                forwardTiXianRecoed();
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }


}


















