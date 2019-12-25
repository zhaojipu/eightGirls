package com.yunbao.phonelive.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CallbackAllowTiXian;
import com.yunbao.phonelive.utils.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DiscountMainActivity extends AbsActivity implements View.OnClickListener {

    private Button mZbbtn;
    private Button mWechatBtn;
    private Button mYhBtn;
    private Button mDiscountRecord;
    private TextView mCoinValue;
    private static final int BOUNS=1;
    private String mBalance;
    private ImageView mBtnBack;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_discount_main;
    }

    @Override
    protected void main() {
        initView();
        initData();
    }

    private void initData() {
        HttpUtil.allowTiXian(new CallbackAllowTiXian() {
            @Override
            public void allowTiXian(String balance) {
                SpUtil.getInstance().setStringValue("balanceCount",balance);
                mCoinValue.setText(balance);
                Log.e("balancebalance",balance+"==");

            }
        });
    }


    private void initView() {
        mZbbtn=findViewById(R.id.zfb_btn);
        mWechatBtn=findViewById(R.id.wechat_btn);
        mYhBtn=findViewById(R.id.yh_btn);
        mDiscountRecord=findViewById(R.id.discount_record);
        mCoinValue=findViewById(R.id.coin_value);
        mBtnBack=findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mZbbtn.setOnClickListener(this);
        mWechatBtn.setOnClickListener(this);
        mYhBtn.setOnClickListener(this);
        mDiscountRecord.setOnClickListener(this);

    }

    private void forwardZfb(){
        startActivity(new Intent(this,CoinWithdrawalActivity.class));
    }

    private void forwardWechat() {
        Log.e("mBalance",mBalance+"==");
        startActivity(new Intent(this,WechatZfActivity.class));
    }

    private void forwardBlank(){
        startActivity(new Intent(this,BankActivity.class));
    }

    private void forwardDiscount(){
        startActivity(new Intent(this,DiscountRecordActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zfb_btn:
                forwardZfb();
                break;
            case R.id.wechat_btn:
                forwardWechat();
                break;
            case R.id.yh_btn:
                forwardBlank();
                break;
            case R.id.discount_record:
                forwardDiscount();
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}










































