package com.yunbao.phonelive.activity;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.TiXianCallBack;
import com.yunbao.phonelive.utils.SpUtil;

public class WechatZfActivity extends AbsActivity implements View.OnClickListener {
    private EditText tixian_coin;
    private EditText wechat_name;
    private EditText qq_name;
    private Button tixian_btn;
    private String mBalance;
    private TextView allow_text;
    private ImageView btn_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wechat_zf;
    }

    @Override
    protected void main() {
        initView();
    }

    private void initView() {
        allow_text = findViewById(R.id.allow_text);
        tixian_coin = findViewById(R.id.tixian_coin);
        wechat_name = findViewById(R.id.wechat_name);
        qq_name = findViewById(R.id.qq_name);
        tixian_btn = findViewById(R.id.tixian_btn);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        mBalance = SpUtil.getInstance().getStringValue("balanceCount");
        Log.e("mbalance", mBalance + "--");
        if (mBalance != null) {
            allow_text.setText("可提现金币" + mBalance + "个");
        }
        tixian_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tixian_btn:
                if (initIsEdit()) {
                    HttpUtil.weixinTixian(qq_name.getText().toString(),
                            wechat_name.getText().toString(),
                            mBalance, new TiXianCallBack() {
                                @Override
                                public void onSuccess() {
                                    finish();
                                }
                            });
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
        if (tixian_coin.getText().toString().isEmpty()) {
            return false;
        }

        if (wechat_name.getText().toString().isEmpty()) {
            wechat_name.setError("账号不能为空");
            return false;
        }

        if (qq_name.getText().toString().isEmpty()) {
            qq_name.setError("账号不能为空");
            return false;
        }

        if (tixian_coin.getText().toString().isEmpty() && Integer.valueOf(tixian_coin.getText().toString()) > Integer.valueOf(mBalance)) {
            tixian_coin.setError("输入金额不能为空且输入的金额不能超过可提现的额度");
            return false;
        }

        return true;
    }
}




























