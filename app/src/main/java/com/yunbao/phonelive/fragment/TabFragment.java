package com.yunbao.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.adapter.TouSuAdapter;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CallBackContent;

import java.util.List;

public class TabFragment extends Fragment implements View.OnClickListener {
    private int type;
    private View view;
    private EditText weixin_content;
    private EditText qq_content;
    private EditText edit_content;
    private Button close;
    private Button tijiao;
    private ListView mListview;
    private TouSuAdapter adapter;

    public static TabFragment getInstance(int type) {
        TabFragment tabFragment = new TabFragment();
        tabFragment.type = type;
        return tabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            switch (type) {
                case Constants.TYPE_ONE:
                    view = inflater.inflate(R.layout.tab_fragment, container, false);
                    initView(view);
                    break;
                case Constants.TYPE_TWO:
                    view = inflater.inflate(R.layout.tab_fragment2, container, false);
                    initTwoView(view);
                    break;
                default:
                    break;
            }

        }

        return view;
    }

    private void initTwoView(View view) {
        mListview=view.findViewById(R.id.mListView);
        adapter=new TouSuAdapter(getContext());
        HttpUtil.tusujilu(new CallBackContent() {
            @Override
            public void onContentList(List<String> strings) {
                adapter.addData(strings);
                mListview.setAdapter(adapter);
            }
        });

    }

    private void initView(View view) {
        weixin_content = view.findViewById(R.id.weixin_content);
        qq_content = view.findViewById(R.id.qq_content);
        close = view.findViewById(R.id.close);
        tijiao = view.findViewById(R.id.tijiao);
        edit_content = view.findViewById(R.id.edit_content);
        close.setOnClickListener(this);
        tijiao.setOnClickListener(this);
    }

    private boolean isEdit() {
        if (edit_content.getText().toString().isEmpty()) {
            edit_content.setError("输入的内容不能为空");
            return false;
        }

        if (qq_content.getText().toString().isEmpty() && weixin_content.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "qq和微信必须填写一项", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                getActivity().finish();
                break;
            case R.id.tijiao:
                if (isEdit()) {
                    //提交接口
                    HttpUtil.tusujianyi(qq_content.getText().toString(),
                            weixin_content.getText().toString(),
                            qq_content.getText().toString());
                }
                break;
            default:
                break;
        }
    }
}




















