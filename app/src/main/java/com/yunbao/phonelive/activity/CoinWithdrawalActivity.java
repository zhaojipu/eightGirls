package com.yunbao.phonelive.activity;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpClient;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.TiXianCallBack;
import com.yunbao.phonelive.utils.SpUtil;
import com.yunbao.phonelive.utils.ToastUtil;

public class CoinWithdrawalActivity extends AbsActivity implements View.OnClickListener {
    private String mBalance;
    private TextView mAllowCount;
    private EditText mEditCount;
    private EditText mZfbZhanghao;
    private EditText mZfbName;
    private EditText mWechatZhanghao;
    private EditText mQqZhanghao;
    private Button mTixianBtn;
    private ImageView btn_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_withdrawal;
    }

    @Override
    protected void main() {
        initData();
        initView();

    }

    private void initData() {
        mBalance = SpUtil.getInstance().getStringValue("balanceCount");
        Log.e("mbalance", mBalance + "--");
    }

    private void initView() {
        mAllowCount = findViewById(R.id.allow_count);
        mEditCount = findViewById(R.id.edit_count);
        mZfbZhanghao = findViewById(R.id.zfb_zhanghao);
        mZfbName = findViewById(R.id.zfb_name);
        mWechatZhanghao = findViewById(R.id.wechat_zhanghao);
        mQqZhanghao = findViewById(R.id.qq_zhanghao);
        mTixianBtn = findViewById(R.id.tixian_btn);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTixianBtn.setOnClickListener(this);
        if (mBalance != null) {
            mAllowCount.setText("可提现筹码" + mBalance + "个");
        }
    }

    private boolean initIsEdit() {
        if (mEditCount.getText().toString().isEmpty()) {
            return false;
        }

        if (mZfbZhanghao.getText().toString().isEmpty()) {
            mZfbZhanghao.setError("账号不能为空");
            return false;
        }

        if (mZfbName.getText().toString().isEmpty()) {
            mZfbName.setError("账号名称不能为空");
            return false;
        }

        if (mWechatZhanghao.getText().toString().isEmpty()) {
            mWechatZhanghao.setError("微信账号不能为空");
            return false;
        }


        if (mQqZhanghao.getText().toString().isEmpty()) {
            mQqZhanghao.setError("QQ账号不能为空");
            return false;
        }

        if (mEditCount.getText().toString().isEmpty() &&
                Integer.valueOf(mEditCount.getText().toString()) > Integer.valueOf(mBalance)) {
            mEditCount.setError("输入金额不能为空且输入的金额不能超过可提现的额度");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tixian_btn:
                if (initIsEdit()) {
                    HttpUtil.zfbTixian(mZfbZhanghao.getText().toString(),
                            mZfbName.getText().toString(),
                            mQqZhanghao.getText().toString(),
                            mWechatZhanghao.getText().toString(),
                            mBalance, new TiXianCallBack() {
                                @Override
                                public void onSuccess() {
                                    ToastUtil.show("提现已提交");
                                    finish();
                                }
                            });
                }
                break;
            default:
                break;
        }
    }
}
















