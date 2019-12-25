package com.yunbao.phonelive.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.CoinAdapter;
import com.yunbao.phonelive.bean.CoinBean;
import com.yunbao.phonelive.bean.UserBean;
import com.yunbao.phonelive.event.CoinChangeEvent;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.OnItemClickListener;
import com.yunbao.phonelive.pay.PayCallback;
import com.yunbao.phonelive.pay.ali.AliPayBuilder;
import com.yunbao.phonelive.pay.wx.WxPayBuilder;
import com.yunbao.phonelive.utils.DialogUitl;
import com.yunbao.phonelive.utils.DpUtil;
import com.yunbao.phonelive.utils.ToastUtil;
import com.yunbao.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MainSlidingRechareViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<CoinBean>,View.OnClickListener {
    private View mTop;
    private TextView mBalance;
    private long mBalanceValue;
    private RecyclerView mRecyclerView;
    private CoinAdapter mAdapter;
    private String mCoinName;
    private CoinBean mCheckedCoinBean;
    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private String mWxAppID;//微信AppID
    private boolean mFirstLoad = true;
    private SparseArray<String> mSparseArray;
    private Activity activity;
    private Button wechat_pay;
    private Button zfb_pay;
    private int selectZhiFuType=1;


    public MainSlidingRechareViewHolder(Context context, ViewGroup parentView, Activity activity) {
        super(context, parentView);
        this.activity = activity;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sliding_recharge;
    }

    @Override
    public void init() {
        Log.e("MainHomeViewHolder","MainSlidingRechareViewHolder");
        super.init();
        wechat_pay = (Button) findViewById(R.id.wechat_pay);
        zfb_pay = (Button) findViewById(R.id.zfb_pay);
        wechat_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("===--==","wechat_pay");
                selectZhiFuType=1;
                wechat_pay.setBackgroundColor(Color.parseColor("#fd4ea2"));
                wechat_pay.setTextColor(Color.parseColor("#ffffff"));
                zfb_pay.setBackgroundColor(Color.parseColor("#ffffff"));
                zfb_pay.setTextColor(Color.parseColor("#333333"));
            }
        });

        zfb_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("===--==","zfb_pay");
                selectZhiFuType=2;
                wechat_pay.setBackgroundColor(Color.parseColor("#ffffff"));
                wechat_pay.setTextColor(Color.parseColor("#333333"));
                zfb_pay.setBackgroundColor(Color.parseColor("#fd4ea2"));
                zfb_pay.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        mSparseArray = new SparseArray<>();
        mCoinName = AppConfig.getInstance().getCoinName();
        mTop = findViewById(R.id.top);
        mBalance = (TextView) findViewById(R.id.balance);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CoinAdapter(mContext, DpUtil.dp2px(0), mCoinName);
        mAdapter.setContactView(mTop);
        mAdapter.setOnItemClickListener(this);
        loadData1();
    }


    public void loadData1() {
        Log.e("loadData1", "====");
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalanceValue = Long.parseLong(coin);
                    mBalance.setText(coin);
                    List<CoinBean> list = JSON.parseArray(obj.getString("rules"), CoinBean.class);
                    if (mAdapter != null) {
                        mAdapter.setList(list);
                        mRecyclerView.setAdapter(mAdapter);
                    }
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

    @Override
    public void onItemClick(CoinBean bean, int position) {
        Log.e("benabda","===");
        mCheckedCoinBean = bean;
        if (mSparseArray == null || mSparseArray.size() == 0) {
            ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
            return;
        }

        //DialogUitl.showStringArrayDialog(mContext, mSparseArray, mArrayDialogCallback);

        switch (selectZhiFuType){
            case 0:
                ToastUtil.show("请选择支付方式");
                break;
            case 1:
                wxPay();
                break;
            case 2:
                aliPay();
                break;
            default:
                break;
        }

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
        builder.setCoinName(mCoinName);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }

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

    /**
     * 检查支付结果
     */
    private void checkPayResult() {
        Log.e("loadData1", "checkPayResult");
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalance.setText(coin);
                    long balanceValue = Long.parseLong(coin);
                    if (balanceValue > mBalanceValue) {
                        mBalanceValue = balanceValue;
                        ToastUtil.show(R.string.coin_charge_success);
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setCoin(coin);
                        }
                        EventBus.getDefault().post(new CoinChangeEvent(coin, true));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wechat_btn:
                selectZhiFuType=1;
                wechat_pay.setBackgroundColor(Color.parseColor("#fd4ea2"));
                zfb_pay.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.zfb_btn:
                selectZhiFuType=2;
                wechat_pay.setBackgroundColor(Color.parseColor("#ffffff"));
                zfb_pay.setBackgroundColor(Color.parseColor("#fd4ea2"));
                break;
            default:
                break;
        }
    }
}









































































