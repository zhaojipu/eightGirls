package com.yunbao.phonelive.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.interfaces.OnPronivceCityInterfaces;
import com.yunbao.phonelive.interfaces.PronivceDialogInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

@SuppressLint("ValidFragment")
public class PronivceDialogFragment extends AbsDialogFragment implements NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener,
        NumberPickerView.OnValueChangeListenerInScrolling, View.OnClickListener {
    private PronivceDialogInterface callback;
    private String[] pronivces;
    private Map<String, JSONArray> citys;
    private NumberPickerView mNumberPicker;
    private NumberPickerView mNumberPicker2;
    private String provicesString;
    private String cityString;
    private OnPronivceCityInterfaces interfaces;
    private TextView cance;
    private TextView sure;

    public PronivceDialogFragment(String[] provices, Map<String, JSONArray> citys, OnPronivceCityInterfaces interfaces) {
        this.pronivces = provices;
        this.citys = citys;
        this.interfaces = interfaces;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.province_city_dialog;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    public void setCallback(PronivceDialogInterface callback) {
        this.callback = callback;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNumberPicker = mRootView.findViewById(R.id.picker1);
        mNumberPicker2 = mRootView.findViewById(R.id.picker2);
        cance = mRootView.findViewById(R.id.cance);
        sure = mRootView.findViewById(R.id.sure);
        cance.setOnClickListener(this);
        sure.setOnClickListener(this);
        mNumberPicker.setDividerColor(Color.parseColor("#ffffff"));
        mNumberPicker2.setDividerColor(Color.parseColor("#ffffff"));
        mNumberPicker.refreshByNewDisplayedValues(pronivces);
        mNumberPicker.setOnScrollListener(this);
        mNumberPicker.setOnValueChangedListener(this);
        mNumberPicker.setOnValueChangeListenerInScrolling(this);
        mNumberPicker2.refreshByNewDisplayedValues(getCitys(getCurrentContent(mNumberPicker)));
        mNumberPicker2.setOnScrollListener(this);
        mNumberPicker2.setOnValueChangedListener(this);
        mNumberPicker2.setOnValueChangeListenerInScrolling(this);

        Log.e("shuhsuasdsufs", getCitys(getCurrentContent(mNumberPicker))[0] + "--");


    }

    @Override
    public void onScrollStateChange(NumberPickerView numberPickerView, int i) {

    }

    @Override
    public void onValueChange(NumberPickerView numberPickerView, int oldVal, int newVal) {
        try {
            String[] content = numberPickerView.getDisplayedValues();
            Log.e("选中的值", content[newVal - numberPickerView.getMinValue()] + "--");
            if (content[newVal - numberPickerView.getMinValue()].contains("省")) {
                provicesString = content[newVal - numberPickerView.getMinValue()];
            }

            if (content[newVal - numberPickerView.getMinValue()].contains("市")) {
                cityString = content[newVal - numberPickerView.getMinValue()];
            }

            interfaces.callbackPronivceAndCity(provicesString, cityString, content[newVal - numberPickerView.getMinValue()]);


            mNumberPicker2.refreshByNewDisplayedValues(getCitys(content[newVal - numberPickerView.getMinValue()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValueChangeInScrolling(NumberPickerView numberPickerView, int i, int i1) {

    }

    private String[] getCitys(String key) {
        try {
            JSONArray jsonArray = citys.get(key);
            if (jsonArray != null && jsonArray.length() != 0) {
                String[] citys = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    citys[i] = jsonArray.getJSONObject(i).optString("name", "");
                }
                return citys;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private String getCurrentContent(NumberPickerView pickerView) {
        String[] content = pickerView.getDisplayedValues();
        String currentContent = content[pickerView.getValue() - pickerView.getMinValue()];
        return currentContent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cance:
                dismiss();
                break;
            case R.id.sure:
                dismiss();
                break;
            default:
                break;
        }
    }
}













