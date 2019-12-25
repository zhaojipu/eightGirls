package com.yunbao.phonelive.activity;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.ProvinceCityBean;
import com.yunbao.phonelive.dialog.MainStartDialogFragment;
import com.yunbao.phonelive.dialog.PronivceDialogFragment;
import com.yunbao.phonelive.interfaces.PronivceDialogInterface;
import com.yunbao.phonelive.utils.DialogUitl;
import com.yunbao.phonelive.utils.GetJsonDataUtil;
import com.yunbao.phonelive.utils.ParseJsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReleaseLiveActivity extends AbsActivity implements View.OnClickListener,PronivceDialogInterface {

    private LinearLayout mFangjianType;
    private LinearLayout mSettingGame;
    private LinearLayout mEditCode;
    private LinearLayout mPronivceCity;
    private ProvinceCityBean bean;
    private List<String> provinces;
    private String[] provinceStrs;
    private Map<String,JSONArray> mCitys;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_live;
    }

    @Override
    protected void main() {
        initData();
        initView();
    }

    private void initData() {
        bean=ParseJsonDataUtil.initJsonData(this);
        provinces=bean.provinceLists;
        mCitys=bean.citys;
        provinceStrs=new String[provinces.size()];
        for (int i = 0; i < provinces.size(); i++) {
            provinceStrs[i]=provinces.get(i);

        }
    }


    private void editAvatar() {
        //DialogUitl.showFanJianfeilei(mContext,R.layout.fangjian_leixin_dialog);
    }

    private void settingGame(){
        DialogUitl.showKaiLiveDialog(mContext,R.layout.setting_game_dialog);
    }

    private void editCode(){
        //DialogUitl.showKaiLiveDialog(mContext,R.layout.select_code_dialog);
    }

    private void provinceCity(){
        DialogUitl.showProvinceDialog(mContext, provinceStrs);
    }

    private void initView(){
        mSettingGame=findViewById(R.id.setting_game);
        mEditCode=findViewById(R.id.edit_code);
        mPronivceCity=findViewById(R.id.pronivce_city);
        mFangjianType.setOnClickListener(this);
        mSettingGame.setOnClickListener(this);
        mEditCode.setOnClickListener(this);
        mPronivceCity.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_game:
                settingGame();
                break;
            case R.id.edit_code:
                editCode();
                break;
            case R.id.pronivce_city:
                //provinceCity();
                showStartDialog();
                break;
            default:
                break;
        }
    }


    private void showStartDialog() {
//        PronivceDialogFragment dialogFragment = new PronivceDialogFragment(provinceStrs,mCitys);
//        dialogFragment.setCallback(this);
//        dialogFragment.show(getSupportFragmentManager(), "PronivceDialogFragment");
    }


    @Override
    public void callBackDialogFragment(PronivceDialogFragment dialogFragment) {
        Log.e("callBackDialogFragment","callBackDialogFragment");
    }
}

































