package com.yunbao.phonelive.utils;

import android.content.Context;
import android.util.Log;

import com.yunbao.phonelive.bean.ProvinceCityBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJsonDataUtil {

    public static ProvinceCityBean initJsonData(Context context) {
        String jsonData = new GetJsonDataUtil().getJson(context, "province.json");
        ProvinceCityBean bean=new ProvinceCityBean();
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String province=jsonObject.optString("name","");
                bean.provinceLists.add(province);
                JSONArray cityJsonArray=jsonObject.getJSONArray("city");
                bean.citys.put(province,cityJsonArray);
            }

            return bean;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;

    }
}
