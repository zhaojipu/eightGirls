package com.yunbao.phonelive.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.activity.LiveActivity;
import com.yunbao.phonelive.activity.LiveAnchorActivity;
import com.yunbao.phonelive.activity.LiveChooseClassActivity;
import com.yunbao.phonelive.adapter.LiveReadyShareAdapter;
import com.yunbao.phonelive.bean.LiveRoomTypeBean;
import com.yunbao.phonelive.bean.ProvinceCityBean;
import com.yunbao.phonelive.dialog.LiveRoomTypeDialogFragment;
import com.yunbao.phonelive.dialog.LiveTimeDialogFragment;
import com.yunbao.phonelive.dialog.PronivceDialogFragment;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpConsts;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.ActivityResultCallback;
import com.yunbao.phonelive.interfaces.CommonCallback;
import com.yunbao.phonelive.interfaces.ImageResultCallback;
import com.yunbao.phonelive.interfaces.LifeCycleAdapter;
import com.yunbao.phonelive.interfaces.OnFangJianItemClick;
import com.yunbao.phonelive.interfaces.OnOpenOrCloseInterface;
import com.yunbao.phonelive.interfaces.OnPronivceCityInterfaces;
import com.yunbao.phonelive.interfaces.PronivceDialogInterface;
import com.yunbao.phonelive.mob.MobCallback;
import com.yunbao.phonelive.utils.DialogUitl;
import com.yunbao.phonelive.utils.L;
import com.yunbao.phonelive.utils.ParseJsonDataUtil;
import com.yunbao.phonelive.utils.ProcessImageUtil;
import com.yunbao.phonelive.utils.StringUtil;
import com.yunbao.phonelive.utils.ToastUtil;
import com.yunbao.phonelive.utils.WordUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by cxf on 2018/10/7.
 * 开播前准备
 */

public class LiveReadyViewHolder extends AbsViewHolder implements View.OnClickListener, PronivceDialogInterface, OnOpenOrCloseInterface, OnFangJianItemClick, OnPronivceCityInterfaces {

    private ImageView mAvatar;
    private TextView mCoverText;
    private EditText mEditTitle;
    private RecyclerView mLiveShareRecyclerView;
    private LiveReadyShareAdapter mLiveShareAdapter;
    private ProcessImageUtil mImageUtil;
    private File mAvatarFile;
    private TextView mLocation;
    private TextView mLiveClass;
    private TextView mLiveTypeTextView;//房间类型TextView
    private int mLiveClassID;//直播频道id
    private int mLiveType;//房间类型
    private int mLiveTypeVal;//房间密码，门票收费金额
    private int mLiveTimeCoin;//计时收费金额
    private ActivityResultCallback mActivityResultCallback;
    private CommonCallback<LiveRoomTypeBean> mLiveTypeCallback;
    private LinearLayout mSettingGame;
    private LinearLayout mFangjianType;
    private LinearLayout mPronivceCity;
    private LinearLayout mEditCode;
    private ProvinceCityBean bean;
    private List<String> provinces;
    private String[] provinceStrs;
    private Map<String, JSONArray> mCitys;
    private FragmentManager manager;
    private String is_report;
    private String province;
    private String city;
    private TextView type_text;
    private TextView game_type_text;
    private TextView city_type_text;
    private ImageView nav_fanhui;
    private AppCompatActivity activity;


    public LiveReadyViewHolder(Context context, AppCompatActivity activity, FragmentManager manager, ViewGroup parentView) {
        super(context, parentView);
        this.manager = manager;
        this.activity = activity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_ready;
    }

    @Override
    public void init() {
        initData();
        nav_fanhui = (ImageView) findViewById(R.id.nav_fanhui);
        nav_fanhui.setOnClickListener(this);
        mSettingGame = (LinearLayout) findViewById(R.id.setting_game);
        mFangjianType = (LinearLayout) findViewById(R.id.btn_room_type);
        mPronivceCity = (LinearLayout) findViewById(R.id.pronivce_city);
        city_type_text = (TextView) findViewById(R.id.city_type_text);
        mEditCode = (LinearLayout) findViewById(R.id.edit_code);
        type_text = (TextView) findViewById(R.id.type_text);
        game_type_text = (TextView) findViewById(R.id.game_type_text);
        mFangjianType.setOnClickListener(this);
        mSettingGame.setOnClickListener(this);
        mPronivceCity.setOnClickListener(this);
        mEditCode.setOnClickListener(this);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);
        mCoverText = (TextView) findViewById(R.id.cover_text);
        mEditTitle = (EditText) findViewById(R.id.edit_title);
        mLocation = (TextView) findViewById(R.id.location);
        mLocation.setText(AppConfig.getInstance().getCity());
        mLiveClass = (TextView) findViewById(R.id.live_class);
        mLiveShareRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLiveShareRecyclerView.setHasFixedSize(true);
        mLiveShareRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mLiveShareAdapter = new LiveReadyShareAdapter(mContext);
        mLiveShareRecyclerView.setAdapter(mLiveShareAdapter);
        mImageUtil = ((LiveActivity) mContext).getProcessImageUtil();
        mImageUtil.setImageResultCallback(new ImageResultCallback() {

            @Override
            public void beforeCamera() {
                ((LiveAnchorActivity) mContext).beforeCamera();
            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(file, mAvatar);
                    if (mAvatarFile == null) {
                        mCoverText.setText(WordUtil.getString(R.string.live_cover_2));
                        mCoverText.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_live_cover));
                    }
                    mAvatarFile = file;
                }
            }

            @Override
            public void onFailure() {
            }
        });
        mLiveClass.setOnClickListener(this);
        findViewById(R.id.avatar_group).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_beauty).setOnClickListener(this);
        findViewById(R.id.btn_start_live).setOnClickListener(this);
        mActivityResultCallback = new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                mLiveClassID = intent.getIntExtra(Constants.CLASS_ID, 0);
                mLiveClass.setText(intent.getStringExtra(Constants.CLASS_NAME));
            }
        };
        mLiveTypeCallback = new CommonCallback<LiveRoomTypeBean>() {
            @Override
            public void callback(LiveRoomTypeBean bean) {
                switch (bean.getId()) {
                    case Constants.LIVE_TYPE_NORMAL:
                        onLiveTypeNormal(bean);
                        type_text.setText(bean.getName());
                        break;
                    case Constants.LIVE_TYPE_PWD:
                        onLiveTypePwd(bean);
                        type_text.setText(bean.getName());
                        break;
                    case Constants.LIVE_TYPE_PAY:
                        onLiveTypePay(bean);
                        type_text.setText(bean.getName());
                        break;
                    case Constants.LIVE_TYPE_TIME:
                        onLiveTypeTime(bean);
                        type_text.setText(bean.getName());
                        break;
                }
            }
        };
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.CREATE_ROOM);
            }
        };
    }

    private void initData() {
        bean = ParseJsonDataUtil.initJsonData(mContext);
        provinces = bean.provinceLists;
        mCitys = bean.citys;
        provinceStrs = new String[provinces.size()];
        for (int i = 0; i < provinces.size(); i++) {
            provinceStrs[i] = provinces.get(i);

        }
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.avatar:
                setAvatar();
                break;
            case R.id.avatar_group:
                setAvatar();
                break;
            case R.id.btn_camera:
                toggleCamera();
                break;
            case R.id.btn_close:
                close();
                break;
            case R.id.live_class:
                chooseLiveClass();
                break;
            case R.id.btn_beauty:
                beauty();
                break;
            case R.id.btn_room_type:
                chooseLiveType();
                break;
            case R.id.btn_start_live:
                startLive();
                break;
            case R.id.setting_game:
                settingGame();
                break;
            case R.id.pronivce_city:
                showStartDialog();
                break;
            case R.id.edit_code:
                editCode();
                break;
            case R.id.nav_fanhui:
                break;
            default:
                break;

        }
    }

    private void editCode() {
        DialogUitl.showCode(mContext);
    }


    private void settingGame() {
        String chuji = AppConfig.getInstance().getConfig().getBroadcast();
        String zhongji = AppConfig.getInstance().getConfig().getBroadcast1();
        String gaoji = AppConfig.getInstance().getConfig().getBroadcast2();
        DialogUitl.showGame(mContext, chuji, zhongji, gaoji, this);
    }

    private void editAvatar() {
        DialogUitl.showFanJianfeilei(mContext, this);
    }

    private void showStartDialog() {
        PronivceDialogFragment dialogFragment = new PronivceDialogFragment(provinceStrs, mCitys, this);
        dialogFragment.setCallback(this);
        dialogFragment.show(manager, "PronivceDialogFragment");

    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }

    /**
     * 切换镜头
     */
    private void toggleCamera() {
        ((LiveAnchorActivity) mContext).toggleCamera();
    }

    /**
     * 关闭
     */
    private void close() {
        ((LiveAnchorActivity) mContext).onBackPressed();
    }

    /**
     * 选择直播频道
     */
    private void chooseLiveClass() {
        Intent intent = new Intent(mContext, LiveChooseClassActivity.class);
        intent.putExtra(Constants.CLASS_ID, mLiveClassID);
        mImageUtil.startActivityForResult(intent, mActivityResultCallback);
    }

    /**
     * 设置美颜
     */
    private void beauty() {
        ((LiveAnchorActivity) mContext).beauty();
    }

    /**
     * 选择直播类型
     */
    private void chooseLiveType() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CHECKED_ID, mLiveType);
        LiveRoomTypeDialogFragment fragment = new LiveRoomTypeDialogFragment();
        fragment.setArguments(bundle);
        fragment.setCallback(mLiveTypeCallback);
        fragment.show(((LiveAnchorActivity) mContext).getSupportFragmentManager(), "LiveRoomTypeDialogFragment");
    }

    /**
     * 普通房间
     */
    private void onLiveTypeNormal(LiveRoomTypeBean bean) {
        mLiveType = bean.getId();
        //mLiveTypeTextView.setText(bean.getName());
        mLiveTypeVal = 0;
        mLiveTimeCoin = 0;
    }

    /**
     * 密码房间
     */
    private void onLiveTypePwd(final LiveRoomTypeBean bean) {
        DialogUitl.showSimpleInputDialog(mContext, WordUtil.getString(R.string.live_set_pwd), DialogUitl.INPUT_TYPE_NUMBER_PASSWORD, 8, new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(R.string.live_set_pwd_empty);
                } else {
                    mLiveType = bean.getId();
                    //mLiveTypeTextView.setText(bean.getName());
                    if (StringUtil.isInt(content)) {
                        mLiveTypeVal = Integer.parseInt(content);
                    }
                    mLiveTimeCoin = 0;
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 付费房间
     */
    private void onLiveTypePay(final LiveRoomTypeBean bean) {
        DialogUitl.showSimpleInputDialog(mContext, WordUtil.getString(R.string.live_set_fee), DialogUitl.INPUT_TYPE_NUMBER, 8, new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(R.string.live_set_fee_empty);
                } else {
                    mLiveType = bean.getId();
                    //mLiveTypeTextView.setText(bean.getName());
                    if (StringUtil.isInt(content)) {
                        mLiveTypeVal = Integer.parseInt(content);
                    }
                    mLiveTimeCoin = 0;
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 计时房间
     */
    private void onLiveTypeTime(final LiveRoomTypeBean bean) {
        LiveTimeDialogFragment fragment = new LiveTimeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CHECKED_COIN, mLiveTimeCoin);
        fragment.setArguments(bundle);
        fragment.setCommonCallback(new CommonCallback<Integer>() {
            @Override
            public void callback(Integer coin) {
                mLiveType = bean.getId();
                //mLiveTypeTextView.setText(bean.getName());
                mLiveTypeVal = coin;
                mLiveTimeCoin = coin;
            }
        });
        fragment.show(((LiveAnchorActivity) mContext).getSupportFragmentManager(), "LiveTimeDialogFragment");
    }

    public void hide() {
        if (mContentView != null && mContentView.getVisibility() == View.VISIBLE) {
            mContentView.setVisibility(View.INVISIBLE);
        }
    }


    public void show() {
        if (mContentView != null && mContentView.getVisibility() != View.VISIBLE) {
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击开始直播按钮
     */
    private void startLive() {
        boolean startPreview = ((LiveAnchorActivity) mContext).isStartPreview();
        if (!startPreview) {
            ToastUtil.show(R.string.please_wait);
            return;
        }
        if (mLiveClassID == 0) {
            ToastUtil.show(R.string.live_choose_live_class);
            return;
        }
        if (mLiveShareAdapter != null) {
            String type = mLiveShareAdapter.getShareType();
            if (!TextUtils.isEmpty(type)) {
                ((LiveActivity) mContext).shareLive(type, new MobCallback() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        createRoom();
                    }
                });
            } else {
                createRoom();
            }
        } else {
            createRoom();
        }
    }

    /**
     * 请求创建直播间接口，开始直播
     */
    private void createRoom() {
        if (mLiveClassID == 0) {
            ToastUtil.show(R.string.live_choose_live_class);
            return;
        }
        String title = mEditTitle.getText().toString().trim();
        Log.e("zhexierizi", is_report + "--");
        HttpUtil.createRoom(title, mLiveClassID, mLiveType, mLiveTypeVal, mAvatarFile, is_report, province, city, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    L.e("开播", "createRoom------->" + info[0]);
                    ((LiveAnchorActivity) mContext).startLiveSuccess(info[0], mLiveType, mLiveTypeVal);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    public void release() {
        mImageUtil = null;
        mActivityResultCallback = null;
        mLiveTypeCallback = null;
    }

    @Override
    public void callBackDialogFragment(PronivceDialogFragment dialogFragment) {
        Log.e("callBackDialogFragment", "callBackDialogFragment");
    }

    @Override
    public void OnItemOpenClick(Dialog dialog, String open) {
        is_report = "1";
        game_type_text.setText(open);
        dialog.dismiss();

    }

    @Override
    public void OnItemCloseClick(Dialog dialog, String close) {
        is_report = "0";
        game_type_text.setText(close);
        dialog.dismiss();
    }

    @Override
    public void OnItemClickGongKai(Dialog dialog) {
        mLiveType = 0;
        dialog.dismiss();
    }

    @Override
    public void OnItemClickPwd(Dialog dialog) {
        mLiveType = 1;
        dialog.dismiss();
    }

    @Override
    public void OnItemClickPiao(Dialog dialog) {
        mLiveType = 2;
        dialog.dismiss();
    }

    @Override
    public void OnItemClickTiaoDao(Dialog dialog) {
        mLiveType = 4;
        dialog.dismiss();
    }

    @Override
    public void OnItemClickJiShi(Dialog dialog) {
        mLiveType = 3;
        dialog.dismiss();
    }

    @Override
    public void callbackPronivceAndCity(String province, String city, String provinceAndcity) {
        this.province = province;
        this.city = city;
        city_type_text.setText(provinceAndcity);

    }
}
