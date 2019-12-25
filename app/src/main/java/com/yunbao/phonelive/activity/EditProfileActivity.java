package com.yunbao.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.Constants;
import com.yunbao.phonelive.R;
import com.yunbao.phonelive.bean.UserBean;
import com.yunbao.phonelive.dialog.PersonalDialog;
import com.yunbao.phonelive.glide.ImgLoader;
import com.yunbao.phonelive.http.HttpCallback;
import com.yunbao.phonelive.http.HttpConsts;
import com.yunbao.phonelive.http.HttpUtil;
import com.yunbao.phonelive.interfaces.ActivityResultCallback;
import com.yunbao.phonelive.interfaces.CommonCallback;
import com.yunbao.phonelive.interfaces.ImageResultCallback;
import com.yunbao.phonelive.utils.DialogUitl;
import com.yunbao.phonelive.utils.ProcessImageUtil;
import com.yunbao.phonelive.utils.ToastUtil;
import com.yunbao.phonelive.utils.WordUtil;

import java.io.File;

/**
 * Created by cxf on 2018/9/29.
 * 我的 编辑资料
 */

public class EditProfileActivity extends AbsActivity implements RadioGroup.OnCheckedChangeListener {

    private ImageView mAvatar;
    private TextView mName;
    private TextView mSign;
    private TextView mBirthday;
    private TextView mSex;
    private ProcessImageUtil mImageUtil;
    private UserBean mUserBean;
    private Intent mIntent;
    private String mId;
    private TextView mIdText;
    private RadioGroup mRadioGroup;
    private RadioButton mSexMan;
    private RadioButton mSexwoman;
    private View mShadow;
    private PersonalDialog myDialog;
    private TextView mEmotionText;
    private TextView mCountyText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void main() {
        mRadioGroup=findViewById(R.id.radio_group);
        mIdText=findViewById(R.id.id_text);
        mIntent=getIntent();
        mId=mIntent.getStringExtra("mId");
        if (mId!=null){
            mIdText.setText(mId);
        }
        setTitle(WordUtil.getString(R.string.edit_profile));
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSign = (TextView)findViewById(R.id.sign);
        mBirthday = (TextView)findViewById(R.id.birthday);
        mSex = (TextView) findViewById(R.id.sex);
        mSexMan=findViewById(R.id.sex_man);
        mSexwoman=findViewById(R.id.sexwoman);
        mShadow=findViewById(R.id.shadow);
        mEmotionText=findViewById(R.id.emotion_text);
        mCountyText=findViewById(R.id.county_text);
        mSign=findViewById(R.id.sign);
        myDialog=new PersonalDialog(this,R.style.dialog);
        mImageUtil = new ProcessImageUtil(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(file, mAvatar);
                    HttpUtil.updateAvatar(file, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                ToastUtil.show(R.string.edit_profile_update_avatar_success);
                                UserBean bean = AppConfig.getInstance().getUserBean();
                                if (bean != null) {
                                    JSONObject obj = JSON.parseObject(info[0]);
                                    bean.setAvatar(obj.getString("avatar"));
                                    bean.setAvatarThumb(obj.getString("avatarThumb"));
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        });
        mUserBean = AppConfig.getInstance().getUserBean();

        if (mUserBean.getSex() == Constants.SEX_MALE) {
            mSexMan.setChecked(true);
        }

        if (mUserBean.getSex() == Constants.SEX_FEMALE){
            mSexwoman.setChecked(true);
        }

        if (mUserBean != null) {
            showData(mUserBean);
        } else {
            HttpUtil.getBaseInfo(new CommonCallback<UserBean>() {
                @Override
                public void callback(UserBean u) {
                    mUserBean = u;
                    showData(u);
                }
            });
        }
    }


    public void editProfileClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_avatar:
                editAvatar();
                break;
            case R.id.btn_name:
                edit(Constants.PERSONALNAME_TYPE,Constants.PERSONALNAME,Constants.PERSONALNAME_CONTEXT);
                break;
            case R.id.btn_sign:
                edit(Constants.PERSONSIGN_TYPE,Constants.PERSONSIGN,Constants.PERSONSIGN_CONTENT);
                break;
            case R.id.btn_birthday:
                editBirthday();
                break;
            case R.id.btn_sex:
                //forwardSex();
                break;
            case R.id.btn_impression:
                forwardImpress();
                break;
            case R.id.shadow:
                mShadow.setVisibility(View.GONE);
                mShadow.setAlpha(0);
                myDialog.dismiss();
                break;
            case R.id.county:
                edit(Constants.PERSONCOUNTY_TYPE,Constants.PERSONCOUNTY,Constants.PERSONCOUNTY_CONTENT);
                break;
            case R.id.emotion:
                eMotion();
                break;
            case R.id.business:
                edit(Constants.PERSONBUSINESS_TYPE,Constants.PERSONBUSINESS,Constants.PERSONBUSINESS_CONTENT);
                break;
            default:
                break;

        }
    }

    private void eMotion(){
        DialogUitl.showEmotion(mContext,new Integer[]{
                R.string.secrecy,
                R.string.single,
                R.string.love,
                R.string.married,
                R.string.homosexual}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                Log.e("===","text"+text);
                editQingGan(text);

            }
        });
    }

    private void editQingGan(final String content){
        HttpUtil.updateFields("{\"feelings\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setFeelings(content);
                            mEmotionText.setText(content);

                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext,new Integer[]{
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

    private void uploadNickName(EditText mEditText){
        if (!canClick()) {
            return;
        }
        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(R.string.edit_profile_name_empty);
            return;
        }
        HttpUtil.updateFields("{\"user_nicename\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setUserNiceName(content);
                        }
                        Intent intent = getIntent();
                        intent.putExtra(Constants.NICK_NAME, content);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    private void uploadContury(EditText mEditText){
        if (!canClick()) {
            return;
        }

        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请输入家乡");
            return;
        }

        HttpUtil.updateFields("{\"country\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setCountry(content);
                            mCountyText.setText(content);
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }



    private void uploadSign(EditText mEditText){
        if (!canClick()) {
            return;
        }

        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(R.string.edit_profile_sign_empty);
            return;
        }

        HttpUtil.updateFields("{\"signature\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setSignature(content);
                            mSign.setText(content);
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void uploadFeelings(final String content){
        HttpUtil.updateFields("{\"feelings\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

            }
        });
    }

    private void edit(final int type, String title, String message){
        DialogUitl.showPersonalDialog(mContext, type,title, message, new DialogUitl.PersonalDialogCallback() {
            @Override
            public void sure(EditText editText) {
                switch (type){
                    case Constants.PERSONALNAME_TYPE:
                        uploadNickName(editText);
                        break;
                    case Constants.PERSONSIGN_TYPE:
                        uploadSign(editText);
                        break;
                    case Constants.PERSONCOUNTY_TYPE:
                        uploadContury(editText);
                        break;
                    case Constants.PERSONBUSINESS_TYPE:

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void cancl(Dialog dialog) {
                dialog.cancel();
            }


        });
    }

    private void forwardName() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditNameActivity.class);
        intent.putExtra(Constants.NICK_NAME, mUserBean.getUserNiceName());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String name = intent.getStringExtra(Constants.NICK_NAME);
                    mUserBean.setUserNiceName(name);
                    mName.setText(name);
                }
            }
        });
    }


    private void forwardSign() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditSignActivity.class);
        intent.putExtra(Constants.SIGN, mUserBean.getSignature());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String sign = intent.getStringExtra(Constants.SIGN);
                    mUserBean.setSignature(sign);
                    mSign.setText(sign);
                }
            }

        });
    }

    private void editBirthday() {
        if (mUserBean == null) {
            return;
        }
        DialogUitl.showDatePickerDialog(mContext, new DialogUitl.DataPickerCallback() {
            @Override
            public void onConfirmClick(final String date) {
                HttpUtil.updateFields("{\"birthday\":\"" + date + "\"}", new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            if (info.length > 0) {
                                ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                                mUserBean.setBirthday(date);
                                mBirthday.setText(date);
                            }
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        });
    }

    private void forwardSex() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditSexActivity.class);
        intent.putExtra(Constants.SEX, mUserBean.getSex());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    int sex = intent.getIntExtra(Constants.SEX, 0);
                    if (sex == 1) {
                        mSex.setText(R.string.sex_male);
                        mUserBean.setSex(sex);
                    } else if (sex == 2) {
                        mSex.setText(R.string.sex_female);
                        mUserBean.setSex(sex);
                    }
                }
            }

        });
    }

    /**
     * 我的印象
     */
    private void forwardImpress() {
        startActivity(new Intent(mContext, MyImpressActivity.class));
    }

    @Override
    protected void onDestroy() {
        if (mImageUtil != null) {
            mImageUtil.release();
        }
        HttpUtil.cancel(HttpConsts.UPDATE_AVATAR);
        HttpUtil.cancel(HttpConsts.UPDATE_FIELDS);
        super.onDestroy();
    }

    private void showData(UserBean u) {
        ImgLoader.displayAvatar(u.getAvatar(), mAvatar);
        mName.setText(u.getUserNiceName());
        mSign.setText(u.getSignature());
        mBirthday.setText(u.getBirthday());
        mSex.setText(u.getSex() == 1 ? R.string.sex_male : R.string.sex_female);
        mEmotionText.setText(u.getFeelings());
        mCountyText.setText(u.getCountry());


    }

    private void setSex(int sex) {
        HttpUtil.updateFields("{\"sex\":\"" + sex + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.e("onSuccess","====");
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.sex_man:
                setSex(Constants.SEX_MALE);
                break;
            case R.id.sexwoman:
                setSex(Constants.SEX_FEMALE);
                break;
            default:
                break;
        }
    }

}

















