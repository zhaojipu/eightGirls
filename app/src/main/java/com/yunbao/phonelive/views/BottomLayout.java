package com.yunbao.phonelive.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.CoinBean;
import com.yunbao.phonelive.bean.GuiZuTypeBean;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CallbackAllowTiXian;
import com.yunbao.phonelive.pay.PayCallback;
import com.yunbao.phonelive.pay.ali.AliPayBuilder;
import com.yunbao.phonelive.pay.wx.WxPayBuilder;
import com.yunbao.phonelive.utils.DialogUitl;
import com.yunbao.phonelive.utils.ToastUtil;
import com.yunbao.phonelive.utils.WordUtil;

import java.util.List;

public class BottomLayout extends RelativeLayout {

    private LayoutInflater mInflater;
    private View view;
    private String open;
    private String xufei;
    private TextView openTitle;
    private TextView xufeiTitle;
    private TextView open_value;
    private TextView xufei_value;
    private String firstPrice;
    private String price;
    private Button ImmeOpen;
    private String id;
    private Context mContext;
    private String coin;


    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private String mWxAppID;//微信AppID

    private SparseArray<String> mSparseArray;
    private AppCompatActivity activity;
    private CoinBean mCheckedCoinBean;


    public BottomLayout(Context context,
                        AppCompatActivity activity,
                        String open,
                        String xufei,
                        String firstPrice,
                        String price,
                        String id,
                        String coin) {
        super(context);
        loadData();
        mCheckedCoinBean = new CoinBean();
        this.activity = activity;
        this.open = open;
        this.xufei = xufei;
        this.firstPrice = firstPrice;
        this.price = price;
        this.id = id;
        this.coin = coin;
        mSparseArray = new SparseArray<>();
        init(context);
    }

    public BottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.noble_bottom, this);
        openTitle = view.findViewById(R.id.open_title);
        xufeiTitle = view.findViewById(R.id.xufei_title);
        open_value = view.findViewById(R.id.open_value);
        xufei_value = findViewById(R.id.xufei_value);
        ImmeOpen = findViewById(R.id.ImmeOpen);
        openTitle.setText(open);
        xufeiTitle.setText(xufei);
        open_value.setText(firstPrice + "元/月");
        xufei_value.setText(price + "元/月");
        ImmeOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUtil.liJiOpen(id, new CallbackAllowTiXian() {
                    @Override
                    public void allowTiXian(String balance) {
                        mCheckedCoinBean.setMoney(balance);
                        mCheckedCoinBean.setId("100");
                        mCheckedCoinBean.setCoin(coin);

                        if (mSparseArray == null || mSparseArray.size() == 0) {
                            ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
                            return;
                        }
                        DialogUitl.showStringArrayDialog(context, mSparseArray, mArrayDialogCallback);


                    }
                });
            }
        });

    }


    private DialogUitl.StringArrayDialogCallback mArrayDialogCallback = new DialogUitl.StringArrayDialogCallback() {
        @Override
        public void onItemClick(String text, int tag) {
            switch (tag) {
                case Constants.PAY_TYPE_ALI://支付宝支付
                    aliPay();
                    break;
                case Constants.PAY_TYPE_WX://微信支付
                    wxPay();
                    break;
            }
        }
    };


    private void loadData() {
        Log.e("loadData", "====");
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mAliPartner = obj.getString("aliapp_partner");
                    mAliSellerId = obj.getString("aliapp_seller_id");
                    mAliPrivateKey = obj.getString("aliapp_key_android");
                    mWxAppID = obj.getString("wx_appid");
                    boolean aliPayEnable = obj.getIntValue("aliapp_switch") == 1;//支付宝是否开启
                    boolean wxPayEnable = obj.getIntValue("wx_switch") == 1;//微信支付是否开启
                    if (aliPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_ALI, WordUtil.getString(R.string.coin_pay_ali));
                    }
                    if (wxPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_WX, WordUtil.getString(R.string.coin_pay_wx));
                    }
                }
            }
        });
    }


    private void aliPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(mAliPartner) || TextUtils.isEmpty(mAliSellerId) || TextUtils.isEmpty(mAliPrivateKey)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(activity, mAliPartner, mAliSellerId, mAliPrivateKey);
        builder.setCoinName("贵族");
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }


    PayCallback mPayCallback = new PayCallback() {
        @Override
        public void onSuccess() {
            // checkPayResult();
        }

        @Override
        public void onFailed() {
            //ToastUtil.show(R.string.coin_charge_failed);
        }
    };


    private void wxPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(R.string.coin_wx_not_install);
            return;
        }
        if (TextUtils.isEmpty(mWxAppID)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(mContext, mWxAppID);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }
}















