package com.yunbao.phonelive.interfaces;

import android.app.Dialog;

public interface OnOpenOrCloseInterface {
    void OnItemOpenClick(Dialog dialog,String open);

    void OnItemCloseClick(Dialog dialog,String close);
}
