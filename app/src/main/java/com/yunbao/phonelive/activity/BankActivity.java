package com.yunbao.phonelive.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.utils.SpUtil;

public class BankActivity extends AbsActivity implements View.OnClickListener {

    private ImageView btn_back;
    private TextView tixian_count;
    private EditText allow_tixian;
    private EditText bank_name;
    private EditText bank_account;
    private EditText wechat_name;
    private EditText qq_name;
    private Button tixian_btn;
    private String mBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank;
    }

    @Override
    protected void main() {
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        btn_back = findViewById(R.id.btn_back);
        tixian_count = findViewById(R.id.tixian_count);
        allow_tixian = findViewById(R.id.allow_tixian);
        bank_name = findViewById(R.id.bank_name);
        bank_account = findViewById(R.id.bank_account);
        wechat_name = findViewById(R.id.wechat_name);
        qq_name = findViewById(R.id.qq_name);
        tixian_btn = findViewById(R.id.tixian_btn);
        mBalance = SpUtil.getInstance().getStringValue("balanceCount");
        Log.e("mbalance", mBalance + "--");
        if (mBalance != null) {
            tixian_count.setText("可提现筹码" + mBalance + "个");
        }
        tixian_btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tixian_btn:
                if (initIsEdit()) {
                    HttpUtil.yinHangTixian(bank_account.getText().toString(),
                            bank_name.getText().toString(),
                            qq_name.getText().toString(),
                            wechat_name.getText().toString(),
                            mBalance);
                }
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean initIsEdit() {
        if (allow_tixian.getText().toString().isEmpty()) {
            return false;
        }
        if (bank_name.getText().toString().isEmpty()) {
            bank_name.setError("输入持卡人姓名不能为空");
            return false;
        }

        if (bank_account.getText().toString().isEmpty()) {
            bank_account.setError("输入持卡账户不能为空");
            return false;
        }


        if (wechat_name.getText().toString().isEmpty()) {
            wechat_name.setError("微信名字不能为空");
            return false;
        }

        if (qq_name.getText().toString().isEmpty()) {
            qq_name.setError("qq名字不能为空");
            return false;
        }

        if (allow_tixian.getText().toString().isEmpty() &&
                Integer.valueOf(allow_tixian.getText().toString()) > Integer.valueOf(mBalance)) {
            allow_tixian.setError("输入金额不能为空且输入的金额不能超过可提现的额度");
            return false;
        }

        return true;
    }
}















































