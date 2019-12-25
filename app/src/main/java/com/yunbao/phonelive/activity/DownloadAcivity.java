package com.yunbao.phonelive.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yunbao.phonelive.R;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CallBackImage;

public class DownloadAcivity extends AbsActivity implements View.OnClickListener {
    private ImageView qcord_img;
    private Button save_qcord;
    private Button copy_text;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void main() {
        initView();
        initLoad();
    }

    private void initLoad() {
        HttpUtil.getQcord(new CallBackImage() {
            @Override
            public void getImageUrl(String imgUrl) {
                ImgLoader.display(imgUrl, qcord_img);
            }
        });
    }

    private void initView() {
        qcord_img=findViewById(R.id.qcord_img);
        save_qcord=findViewById(R.id.save_qcord);
        copy_text=findViewById(R.id.copy_text);
        save_qcord.setOnClickListener(this);
        copy_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_qcord:
                break;
            case R.id.copy_text:
                break;
            default:
                break;
        }
    }
}

























