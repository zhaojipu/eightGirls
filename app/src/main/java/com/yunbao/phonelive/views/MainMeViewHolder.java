package com.yunbao.phonelive.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.activity.ComplaintActivity;
import com.yunbao.phonelive.activity.DiscountMainActivity;
import com.yunbao.phonelive.activity.DownloadAcivity;
import com.yunbao.phonelive.activity.EditProfileActivity;
import com.yunbao.phonelive.activity.ExchangeActivity;
import com.yunbao.phonelive.activity.ExchangeCoinActivity;
import com.yunbao.phonelive.activity.FansActivity;
import com.yunbao.phonelive.activity.FollowActivity;
import com.yunbao.phonelive.activity.LiveRecordActivity;
import com.yunbao.phonelive.activity.MyCoinActivity;
import com.yunbao.phonelive.activity.MyProfitActivity;
import com.yunbao.phonelive.activity.MyVideoActivity;
import com.yunbao.phonelive.activity.NobleActivity;
import com.yunbao.phonelive.activity.PromotionActivity;
import com.yunbao.phonelive.activity.ReleaseLiveActivity;
import com.yunbao.phonelive.activity.SettingActivity;
import com.yunbao.phonelive.activity.SlidingRechargeActivity;
import com.yunbao.phonelive.activity.WebViewActivity;
import com.yunbao.phonelive.adapter.MainMeAdapter;
import com.yunbao.phonelive.adapter.MainMeAdapter1;
import com.yunbao.phonelive.bean.LevelBean;
import com.yunbao.phonelive.bean.RechargeBean;
import com.yunbao.phonelive.bean.UserBean;
import com.yunbao.phonelive.bean.UserItemBean;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.http.HttpConsts;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.CommonCallback;
import com.yunbao.phonelive.interfaces.LifeCycleAdapter;
import com.yunbao.phonelive.interfaces.MainAppBarLayoutListener;
import com.yunbao.phonelive.interfaces.OnItemClickListener;
import com.yunbao.phonelive.utils.IconUtil;
import com.yunbao.phonelive.utils.L;
import com.yunbao.phonelive.utils.SpUtil;
import com.yunbao.phonelive.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by cxf on 2018/9/22.
 * 我的
 */

public class MainMeViewHolder extends AbsMainChildViewHolder implements View.OnClickListener, OnItemClickListener<RechargeBean> {

    private TextView mTtileView;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mSex;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mID;
    private TextView mLive;
    private TextView mFollow;
    private TextView mFans;
    private boolean mPaused;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewNew;
    private MainMeAdapter mAdapter;
    private String mIdVal;
    private TextView mIdRelative;

    private String[] mMimeTitleType_0 = new String[]{
            "金币充值,1,0",
            "我的收益,12,0",
            "我的认证,13,0",
            "筹码提现,3,0",
            "金币兑换筹码,2,0",
            "筹码兑换金币,11,0",
            "成为贵族,4,0",
            "联系客服,5,0",
            "投诉建议,6,0",
            "收藏回家地址,7,1",
            "App下载地址推广挣钱,8,2",
            "推广提现,9,2",
            "设置,10,3"

    };

    private String mOnlineService;
    private String mMineTuSu;
    private String mMyCertification;

    private List<RechargeBean> rechargeBeanList;


    private MainMeAdapter1 adapter1;


    public MainMeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_me_new;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void init() {
        Log.e("MainHomeViewHolder", "MainMeViewHolder");
        super.init();
        mTtileView = (TextView) findViewById(R.id.titleView);
        mIdRelative = (TextView) findViewById(R.id.sign_text);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSex = (ImageView) findViewById(R.id.sex);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        mLive = (TextView) findViewById(R.id.live);
        mFollow = (TextView) findViewById(R.id.follow);
        mFans = (TextView) findViewById(R.id.fans);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_live).setOnClickListener(this);
        findViewById(R.id.btn_follow).setOnClickListener(this);
        findViewById(R.id.btn_fans).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mRecyclerViewNew = (RecyclerView) findViewById(R.id.recyclerView_new);
        mRecyclerViewNew.setHasFixedSize(true);
        mRecyclerViewNew.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onResume() {
                if (mPaused && mShowed) {
                    loadData();
                }
                mPaused = false;
            }

            @Override
            public void onPause() {
                mPaused = true;
            }

            @Override
            public void onDestroy() {
                L.e("main----MainMeViewHolder-------LifeCycle---->onDestroy");
                HttpUtil.cancel(HttpConsts.GET_BASE_INFO);
            }
        };
        mAppBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
                mTtileView.setAlpha(1);
            }
        };
        mNeedDispatch = true;


    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
    }


    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            AppConfig appConfig = AppConfig.getInstance();
            UserBean u = appConfig.getUserBean();
            List<UserItemBean> list = appConfig.getUserItemList();
            if (u != null && list != null) {
                showData(u, list);
            }
        }

        HttpUtil.getBaseInfo(mCallback);

        initLoadRecyclerData();
    }

    private void initLoadRecyclerData() {
        List<UserItemBean> list = AppConfig.getInstance().getUserItemList();
        Log.e("mListUserItemBeans", list.size() + "--");

        for (int i = 0; i < list.size(); i++) {
            UserItemBean bean = list.get(i);
            if (bean.getName().equals("在线客服(Beta)")) {
                mOnlineService = bean.getHref();
            }

            if (bean.getName().equals("我的投诉")) {
                mMineTuSu = bean.getHref();
            }

            if (bean.getName().equals("我的认证")) {
                mMyCertification = bean.getHref();
                Log.e("mMyCertification", bean.getHref() + "--");
            }


        }

        rechargeBeanList = new ArrayList<>();
        for (int i = 0; i < mMimeTitleType_0.length; i++) {
            RechargeBean bean = new RechargeBean();
            bean.setType(Integer.valueOf(mMimeTitleType_0[i].split(",")[2]));
            bean.setId(mMimeTitleType_0[i].split(",")[1]);
            bean.setMineText(mMimeTitleType_0[i].split(",")[0]);
            rechargeBeanList.add(bean);
        }

        adapter1 = new MainMeAdapter1(mContext, rechargeBeanList);
        adapter1.setOnItemClickListener(this);
        mRecyclerViewNew.setAdapter(adapter1);
    }


    private CommonCallback<UserBean> mCallback = new CommonCallback<UserBean>() {
        @Override
        public void callback(UserBean bean) {
            List<UserItemBean> list = AppConfig.getInstance().getUserItemList();
            if (bean != null) {
                showData(bean, list);
            }
        }
    };

    private void showData(UserBean u, List<UserItemBean> list) {
        ImgLoader.displayAvatar(u.getAvatar(), mAvatar);
        //mTtileView.setText(u.getUserNiceName());
        mName.setText(u.getUserNiceName());
        //mIdRelative.setText(u.getSignature());
        mSex.setImageResource(IconUtil.getSexIcon(u.getSex()));
        AppConfig appConfig = AppConfig.getInstance();
        LevelBean anchorLevelBean = appConfig.getAnchorLevel(u.getLevelAnchor());
        if (anchorLevelBean != null) {
            ImgLoader.display(anchorLevelBean.getThumb(), mLevelAnchor);
        }
        LevelBean levelBean = appConfig.getLevel(u.getLevel());
        if (levelBean != null) {
            ImgLoader.display(levelBean.getThumb(), mLevel);
        }
        mIdVal = u.getLiangNameTip();
        mID.setText(u.getLiangNameTip());
        mLive.setText(StringUtil.toWan(u.getLives()));
        mFollow.setText(StringUtil.toWan(u.getFollows()));
        mFans.setText(StringUtil.toWan(u.getFans()));

        UserItemBean mBean = new UserItemBean();
        mBean.setName("版本:2.6.1.0");
        list.add(mBean);

        if (list != null && list.size() > 0) {
            if (mAdapter == null) {
                mAdapter = new MainMeAdapter(mContext, list);
                //mAdapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setList(list);
            }
        }
    }

//    @Override
//    public void onItemClick(UserItemBean bean, int position) {
//        String url = bean.getHref();
//        if (TextUtils.isEmpty(url)) {
//            switch (bean.getId()) {
//                case 1:
//                    forwardProfit();
//                    break;
//                case 29:
//                    forwardCoin();
//                    break;
//                case 13:
//                    forwardSetting();
//                    break;
//                case 19:
//                    forwardMyVideo();
//                    break;
//
//            }
//        } else {
//            WebViewActivity.forward(mContext, url);
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                forwardEditProfile(mIdVal);
                break;
            case R.id.btn_live:
                forwardLiveRecord();
                break;
            case R.id.btn_follow:
                forwardFollow();
                break;
            case R.id.btn_fans:
                forwardFans();
                break;
        }
    }


    /**
     * 编辑个人资料
     */
    private void forwardEditProfile(String mIdVal) {
        Intent mIntent = new Intent(mContext, EditProfileActivity.class);
        mIntent.putExtra("mId", mIdVal);
        mContext.startActivity(mIntent);
    }

    /**
     * 我的关注
     */
    private void forwardFollow() {
        Intent intent = new Intent(mContext, FollowActivity.class);
        intent.putExtra(Constants.TO_UID, AppConfig.getInstance().getUid());
        mContext.startActivity(intent);
    }

    /**
     * 我的粉丝
     */
    private void forwardFans() {
        Intent intent = new Intent(mContext, FansActivity.class);
        intent.putExtra(Constants.TO_UID, AppConfig.getInstance().getUid());
        mContext.startActivity(intent);
    }

    /**
     * 直播记录
     */
    private void forwardLiveRecord() {
        LiveRecordActivity.forward(mContext, AppConfig.getInstance().getUserBean());
    }

    /**
     * 我的收益
     */
    private void forwardProfit() {
        mContext.startActivity(new Intent(mContext, MyProfitActivity.class));
    }

    /**
     * 我的钻石
     */
    private void forwardCoin() {
        mContext.startActivity(new Intent(mContext, MyCoinActivity.class));
    }


    /**
     * 筹码提现
     */
    private void forwardExchange() {
        mContext.startActivity(new Intent(mContext, DiscountMainActivity.class));
    }

    /**
     * 设置
     */
    private void forwardSetting() {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }

    /**
     * 我的视频
     */
    private void forwardMyVideo() {
        mContext.startActivity(new Intent(mContext, MyVideoActivity.class));
    }


    /**
     * 成为贵族
     */
    private void forwardNoble() {
        mContext.startActivity(new Intent(mContext, NobleActivity.class));
    }

    /**
     * 投诉建议
     */
    private void forwardComplaint() {
        Intent mIntent = new Intent(mContext, ComplaintActivity.class);
        mContext.startActivity(mIntent);
    }

    /**
     * 推广提现
     */
    private void forwardPromotion() {
        mContext.startActivity(new Intent(mContext, PromotionActivity.class));
    }

    private void forwardDownLoad() {
        mContext.startActivity(new Intent(mContext, DownloadAcivity.class));
    }

    private void forwardGoldExchange() {
        mContext.startActivity(new Intent(mContext, ExchangeActivity.class));
    }

    private void forwardChipExchange() {
        mContext.startActivity(new Intent(mContext, ExchangeCoinActivity.class));
    }

    @Override
    public void onItemClick(RechargeBean bean, int position) {
        switch (bean.getId()) {
            case "1":
                forwardCoin();
                break;
            case "2":
                forwardGoldExchange();
                break;
            case "3":
                forwardExchange();
                break;
            case "4":
                forwardNoble();
                break;
            case "5":
                WebViewActivity.forward(mContext, mOnlineService);
                break;
            case "6":
                forwardComplaint();
                break;
            case "7":
                break;
            case "8":
                forwardDownLoad();
                break;
            case "9":
                forwardPromotion();
                break;
            case "10":
                forwardSetting();
                break;
            case "11":
                forwardChipExchange();
                break;
            case "12":
                forwardProfit();
                break;
            case "13":
                WebViewActivity.forward(mContext, mMyCertification);
                break;
            default:
                break;

        }
    }

}
