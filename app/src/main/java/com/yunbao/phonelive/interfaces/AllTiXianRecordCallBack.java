package com.yunbao.phonelive.interfaces;

import com.yunbao.phonelive.bean.DiscountRecordBean;
import com.yunbao.phonelive.bean.RecordBean;

import java.util.List;

public interface AllTiXianRecordCallBack {
    void recordCallBack(List<DiscountRecordBean> beans);
}
