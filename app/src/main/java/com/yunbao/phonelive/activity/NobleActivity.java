package com.yunbao.phonelive.activity;


import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.NobleAdapter;
import com.yunbao.phonelive.bean.GuiZuTypeBean;
import com.yunbao.phonelive.bean.NobleBean;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.views.BottomLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NobleActivity extends AbsActivity implements TabLayout.OnTabSelectedListener {

    private RadioGroup mRadioGroup;
    private ImageView mTypeImg;
    private TextView mTypeText;
    private RadioButton mDingHuang;
    private RadioButton mJunWang;
    private RadioButton mGongJue;
    private RadioButton mLingZhu;
    private RadioButton mQiShi;
    private RadioButton mJianShi;
    private ListView mListView;
    private NobleAdapter mAdapter;
    private List<NobleBean> nobleBeans = new ArrayList<>();
    private NobleBean bean = new NobleBean();
    private String[] mQishiTitle = new String[]{"专属坐骑", "超值优惠", "万众瞩目", "万众瞩目"};
    private String[] opens = new String[]{"开通帝皇", "开通君王", "开通公爵", "开通领主", "开通骑士", "开通剑士"};
    private String[] xufei = new String[]{"续费帝皇", "续费君王", "续费公爵", "续费领主", "续费骑士", "续费剑士"};
    private String[] guizuInfo = new String[4];
    private List<GuiZuTypeBean> guizuinfosList = new ArrayList<>();
    private BottomLayout bottomLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TabLayout tabLayout;
    private String price;
    private String firstPrice;
    private ImageView btn_back;
    private String nobleId;
    private String coin;


    private void onLoadData() {
        HttpUtil.guizu(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                for (int i = 0; i < info.length; i++) {
                    try {
                        JSONObject guizuObject = new JSONObject(info[i]);
                        GuiZuTypeBean bean = new GuiZuTypeBean();
                        bean.setMount(guizuObject.optString("mount", ""));
                        bean.setAttention1(guizuObject.optString("attention1", ""));
                        bean.setAttention2(guizuObject.optString("attention2", ""));
                        bean.setDiscounts(guizuObject.optString("discounts", ""));
                        bean.setFristprice(guizuObject.optString("first_price"));
                        bean.setPrice(guizuObject.optString("price"));
                        bean.setNoble(guizuObject.optString("noble", ""));
                        bean.setId(guizuObject.optString("id", ""));
                        bean.setImg(guizuObject.optString("img", ""));
                        bean.setCoin(guizuObject.optString("coin", ""));
                        guizuinfosList.add(bean);
                        TabLayout.Tab tab = tabLayout.newTab();
                        tab.setTag(guizuObject.optString("id", ""));
                        tab.setText(guizuObject.optString("noble", ""));
                        tabLayout.addTab(tab);

                        //默认显示第一个数据
                        initMoRenData(guizuinfosList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initMoRenData(List<GuiZuTypeBean> list) {
        GuiZuTypeBean bean = list.get(0);
        Log.e("mTypeImg", bean.getImg() + "--");
        ImgLoader.display(bean.getImg(), mTypeImg);
        mTypeText.setText(bean.getNoble());
        guizuInfo[0] = bean.getMount();
        guizuInfo[1] = bean.getDiscounts();
        guizuInfo[2] = bean.getAttention1();
        guizuInfo[3] = bean.getAttention1();
        price = bean.getPrice();
        firstPrice = bean.getFristprice();
        dataChange(mQishiTitle, guizuInfo, opens[4], xufei[4], firstPrice, price, bean.getId(), bean.getCoin());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_noble;
    }

    @Override
    protected void main() {
        initView();
        onLoadData();
    }

    private void initView() {
        mAdapter = new NobleAdapter(this);
        btn_back = findViewById(R.id.btn_back);
        mRadioGroup = findViewById(R.id.radio_group);
        mTypeImg = findViewById(R.id.type_img);
        mTypeText = findViewById(R.id.type_text);
        mDingHuang = findViewById(R.id.type_one);
        mJunWang = findViewById(R.id.type_two);
        mGongJue = findViewById(R.id.type_three);
        mLingZhu = findViewById(R.id.type_four);
        mQiShi = findViewById(R.id.type_five);
        mJianShi = findViewById(R.id.type_six);
        mListView = findViewById(R.id.mListView);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void dataChange(String[] titles,
                            String[] titleOnes,
                            String open,
                            String xufei,
                            String fristPrice,
                            String price,
                            String id,
                            String coin) {
        nobleBeans.clear();
        for (int i = 0; i < titles.length; i++) {
            bean = new NobleBean();
            bean.setTitle(titles[i]);
            bean.setTitleOne(titleOnes[i]);
            nobleBeans.add(bean);
        }

        mAdapter.addData(nobleBeans);

        if (bottomLayout != null) {
            mListView.removeFooterView(bottomLayout);
        }
        bottomLayout = new BottomLayout(this, this, open, xufei, fristPrice, price, id, coin);
        mListView.addFooterView(bottomLayout);

        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bean != null) {
            bean = null;
        }

        if (nobleBeans != null) {
            nobleBeans = null;
        }

        if (bottomLayout != null) {
            bottomLayout = null;
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (guizuinfosList.size() == 1) {
            return;
        }
        Log.e("guizuinfosList", guizuinfosList.size() + "--");
        switch (tab.getText().toString()) {
            case "骑士":
                mTypeImg.setImageResource(R.drawable.img_qishi);
                mTypeText.setText(mQiShi.getText().toString());


                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("骑士")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                        nobleId = bean.getId();
                        coin = bean.getCoin();
                    }
                }
                dataChange(mQishiTitle, guizuInfo, opens[4], xufei[4], firstPrice, price, nobleId, coin);
                break;
            case "剑士":
                mTypeImg.setImageResource(R.drawable.img_jianshi);
                mTypeText.setText(mJianShi.getText().toString());

                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("剑士")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                        nobleId = bean.getId();
                        coin = bean.getCoin();

                    }
                }
                dataChange(mQishiTitle, guizuInfo, opens[5], xufei[5], firstPrice, price, nobleId, coin);
                break;
            case "帝皇":
                mTypeImg.setImageResource(R.drawable.img_dihuang);
                mTypeText.setText(mDingHuang.getText().toString());
                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("帝皇")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                    }
                }
                //dataChange(mQishiTitle, guizuInfo, opens[0], xufei[0], firstPrice, price);
                break;
            case "君王":
                mTypeImg.setImageResource(R.drawable.img_junwang);
                mTypeText.setText(mJunWang.getText().toString());
                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("君王")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                    }
                }
                //dataChange(mQishiTitle, guizuInfo, opens[1], xufei[1], firstPrice, price);
                break;
            case "公爵":
                mTypeImg.setImageResource(R.drawable.img_gongjue);
                mTypeText.setText(mGongJue.getText().toString());
                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("公爵")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                    }
                }
                //dataChange(mQishiTitle, guizuInfo, opens[2], xufei[2], firstPrice, price);
                break;
            case "领主":
                mTypeImg.setImageResource(R.drawable.img_lingzhu);
                mTypeText.setText(mLingZhu.getText().toString());
                for (int i = 0; i < guizuinfosList.size(); i++) {
                    if (guizuinfosList.get(i).getNoble().equals("领主")) {
                        GuiZuTypeBean bean = guizuinfosList.get(i);
                        guizuInfo[0] = bean.getMount();
                        guizuInfo[1] = bean.getDiscounts();
                        guizuInfo[2] = bean.getAttention1();
                        guizuInfo[3] = bean.getAttention1();
                        price = bean.getPrice();
                        firstPrice = bean.getFristprice();
                    }
                }
                //dataChange(mQishiTitle, guizuInfo, opens[3], xufei[3], firstPrice, price);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}











