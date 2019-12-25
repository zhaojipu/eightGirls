package com.yunbao.phonelive.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CoinBlanceCallBack;
import com.yunbao.phonelive.utils.SpUtil;
import com.yunbao.phonelive.utils.ToastUtil;

public class ExchangeCoinActivity extends AbsActivity implements View.OnClickListener {

    private TextView gold_coin;
    private EditText input_edit;
    private Button exchage_chouma;
    private TextView chouma;
    private ImageView btn_back;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_coin;
    }

    @Override
    protected void main() {
        initView();
        initData();
    }

    private void initData() {
        HttpUtil.getBalanceAndCoin(new CoinBlanceCallBack() {
            @Override
            public void getCoinBlance(String balance, final String coin) {
                gold_coin.setText(coin);
                chouma.setText(balance);
                SpUtil.getInstance().setStringValue("coin", coin);
            }
        });
        exchage_chouma.setOnClickListener(this);
    }

    private void initView() {
        btn_back = findViewById(R.id.btn_back);
        gold_coin = findViewById(R.id.gold_coin);
        input_edit = findViewById(R.id.input_edit);
        exchage_chouma = findViewById(R.id.exchage_chouma);
        chouma = findViewById(R.id.chouma);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exchage_chouma:
                String coin = SpUtil.getInstance().getStringValue("coin");
                if (input_edit.getText().toString().isEmpty()) {
                    return;
                }
                if (Integer.valueOf(coin) >= Integer.valueOf(input_edit.getText().toString())) {
                    HttpUtil.choumaDuiHuaicoin(input_edit.getText().toString(), new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            HttpUtil.getBalanceAndCoin(new CoinBlanceCallBack() {
                                @Override
                                public void getCoinBlance(String balance, final String coin) {
                                    gold_coin.setText(coin);
                                    chouma.setText(balance);
                                    SpUtil.getInstance().setStringValue("coin", coin);
                                }
                            });
                        }
                    });
                } else {
                    ToastUtil.show("输入的筹码数量大于可兑换的筹码数量");
                }
                break;
            default:
                break;
        }
    }
}
