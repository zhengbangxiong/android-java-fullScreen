package app.gkai.ai.core.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.NavigationBarUtil;
import app.gkai.ai.core.common.util.StatusBarUtil;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.core.common.view.LoadingDialog;
import app.gkai.ai.core.common.view.MMToast;
import app.gkai.ai.modules.mine.beans.resp.FreshMemberInfoResp;


/**
 * @author xiong
 * @title 基础类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private MMToast       mmToast;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Must run before super/onCreate and setContentView. On API 35/36, doing this later
        // leaves the targetSdk 29 compatibility navigation-bar background in place.
        NavigationBarUtil.showGestureBarOnContent(this);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initView();
        // Some pages update systemUiVisibility in initView, so apply this afterwards.
        NavigationBarUtil.showGestureBarOnContent(this);
        getClientConfigDao();
        //        if (StringUtils.isEmpty(Session.IMEI)) {
        Session.IMEI = getPhoneNumber(this);
        //        }

        Session.LANGUAGE = ClientConfigDao.GetConfigString(this, Constants.LANGUAGE, "");
        //        if (StringUtils.isEmpty(Session.LANGUAGE)) {
        //            System.out.println("----------Locale.US1-----------");
        try{
            if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
                changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
            } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
                changeAppLanguage(Locale.TRADITIONAL_CHINESE);
            } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
                changeAppLanguage(Locale.US);
            } else if ("".equals(Session.LANGUAGE) || "null".equals(Session.LANGUAGE)) {
                changeAppLanguage(Locale.US);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //        }

        //        setStatusBar();
        //        initClickListener();
    }

    //只是被那些已经创建过并且将persistableMode设置为persistAcrossReboots的activtiy调用
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        NavigationBarUtil.showGestureBarOnContent(this);
        super.onCreate(savedInstanceState, persistentState);
        NavigationBarUtil.showGestureBarOnContent(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationBarUtil.showGestureBarOnContent(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            NavigationBarUtil.showGestureBarOnContent(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            //如果不是落在EditText区域，则需要关闭输入法
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            //获取现在拥有焦点的控件view的位置，即EditText
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();

            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 仿ios成功提示框
     *
     * @param msg
     */
    public void showToastSuccess(String msg) {
        if (mmToast == null) {
            MMToast.Builder builder = new MMToast.Builder(this)
                    .setMessage(msg)
                    .setSuccess(true);
            mmToast = builder.create();
        } else {
            mmToast.cancel();
            MMToast.Builder builder = new MMToast.Builder(this)
                    .setMessage(msg)
                    .setSuccess(true);
            mmToast = builder.create();
        }
        mmToast.show();
    }

    /**
     * 仿ios错误提示框
     *
     * @param msg
     */
    public void showToastFailure(String msg) {
        if (mmToast == null) {
            MMToast.Builder builder = new MMToast.Builder(this)
                    .setMessage(msg)
                    .setSuccess(false);
            mmToast = builder.create();
        } else {
            mmToast.cancel();
            MMToast.Builder builder = new MMToast.Builder(this)
                    .setMessage(msg)
                    .setSuccess(false);
            mmToast = builder.create();
        }
        mmToast.show();
    }

    /**
     * 仿ios加载框
     *
     * @param msg
     */
    public void showLoadingDialog(String msg) {
        LoadingDialog.Builder builder1 = new LoadingDialog.Builder(this)
                .setMessage("" + msg)
                .setCancelable(true);
        loadingDialog = builder1.create();
        loadingDialog.show();
    }

    /**
     * 仿ios取消加载框
     */
    public void dismissDialog() {
        try {
            if (loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SetStatuColor(int color, boolean dark, boolean isFullMode) {
        //				StatusBarUtil.setStatusBarColor(this, R.color.white);
        //				StatusBarUtil.setLightStatusBar(this, true, false);

        StatusBarUtil.setStatusBarColor(this, color);
        setAndroidNativeLightStatusBar(this, dark);
    }

    private void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * @param statusColor    状态栏颜色
     * @param statusBarColor 状态栏字体颜色 true为黑色，false为白色
     */
    public void setStatusBar(int statusColor, boolean statusBarColor) {
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isUseFullScreenMode()) {
            StatusBarUtil.transparencyBar(this);
        } else {
            StatusBarUtil.setStatusBarColor(this, getStatusBarColor(statusColor));
        }

        if (isUseBlackFontWithStatusBar(statusBarColor)) {
            StatusBarUtil.setLightStatusBar(this, true, isUseFullScreenMode());
        }
        //        }else{
        //
        //        }
    }

    /**
     * 是否设置成透明状态栏，即就是全屏模式
     */
    protected boolean isUseFullScreenMode() {
        return false;
    }

    /**
     * 更改状态栏颜色，只有非全屏模式下有效
     */
    protected int getStatusBarColor(int statusColor) {
        //        return R.color.white;
        return statusColor;
    }

    /**
     * 是否改变状态栏文字颜色为黑色，默认为黑色
     */
    protected boolean isUseBlackFontWithStatusBar(boolean statusBarColor) {
        //        return true;
        return statusBarColor;
    }

    /**
     * 获取登录信息
     */
    public void getClientConfigDao() {
        if (StringUtils.isEmpty(Session.token)) {
            Session.token = ClientConfigDao.GetConfigString(this, Constants.token, "");
            if (StringUtils.isEmpty(Session.token)) {
                Session.islogin = false;
            } else {

                Session.islogin = true;
                Session.status = ClientConfigDao.GetConfigString(this, Constants.status, "");
                Session.name = ClientConfigDao.GetConfigString(this, Constants.name, "");
                Session.nickName = ClientConfigDao.GetConfigString(this, Constants.nickName, "");
                Session.mail = ClientConfigDao.GetConfigString(this, Constants.mail, "");
                Session.jihuo = ClientConfigDao.GetConfigString(this, Constants.jihuo, "");
                Session.jibie = ClientConfigDao.GetConfigString(this, Constants.jibie, "");
                Session.isSettingSet = ClientConfigDao.GetConfigString(this, Constants.isSettingSet, "");
                Session.isRealname = ClientConfigDao.GetConfigString(this, Constants.isRealname, "");
                Session.isPhone = ClientConfigDao.GetConfigString(this, Constants.isPhone, "");
                Session.isMail = ClientConfigDao.GetConfigString(this, Constants.isMail, "");
                Session.invitationCode = ClientConfigDao.GetConfigString(this, Constants.invitationCode, "");
                Session.id = ClientConfigDao.GetConfigString(this, Constants.id, "");
                Session.headImg = ClientConfigDao.GetConfigString(this, Constants.headImg, "");
                Session.googleStatus = ClientConfigDao.GetConfigString(this, Constants.googleStatus, "");
                Session.credibility = ClientConfigDao.GetConfigString(this, Constants.credibility, "");
                Session.tag = ClientConfigDao.GetConfigString(this, Constants.person_tag, "");
                Session.sex = ClientConfigDao.GetConfigString(this, Constants.sex, "");
                Session.isSwitch = ClientConfigDao.GetConfigString(this, Constants.isSwitch, "");
            }
        }
    }

    /**
     * 清除登录数据
     */
    public void cleanLoginData() {
        Session.islogin = false;
        Session.token = "";
        Session.status = "";
        Session.name = "";
        Session.nickName = "";
        Session.mail = "";
        Session.jihuo = "";
        Session.jibie = "";
        Session.isSettingSet = "";
        Session.isRealname = "";
        Session.isPhone = "";
        Session.isMail = "";
        Session.invitationCode = "";
        Session.id = "";
        Session.headImg = "";
        Session.googleStatus = "";
        Session.credibility = "";
        Session.tag = "";
        Session.sex = "";
        Session.isSwitch = "";


        ClientConfigDao.SetConfigString(this, Constants.token, "");
        ClientConfigDao.SetConfigString(this, Constants.status, "");
        ClientConfigDao.SetConfigString(this, Constants.name, "");
        ClientConfigDao.SetConfigString(this, Constants.nickName, "");
        ClientConfigDao.SetConfigString(this, Constants.mail, "");
        ClientConfigDao.SetConfigString(this, Constants.jihuo, "");
        ClientConfigDao.SetConfigString(this, Constants.jibie, "");
        ClientConfigDao.SetConfigString(this, Constants.isSettingSet, "");
        ClientConfigDao.SetConfigString(this, Constants.isRealname, "");
        ClientConfigDao.SetConfigString(this, Constants.isPhone, "");
        ClientConfigDao.SetConfigString(this, Constants.isMail, "");
        ClientConfigDao.SetConfigString(this, Constants.invitationCode, "");
        ClientConfigDao.SetConfigString(this, Constants.id, "");
        ClientConfigDao.SetConfigString(this, Constants.headImg, "");
        ClientConfigDao.SetConfigString(this, Constants.googleStatus, "");
        ClientConfigDao.SetConfigString(this, Constants.credibility, "");
        ClientConfigDao.SetConfigString(this, Constants.person_tag, "");
        ClientConfigDao.SetConfigString(this, Constants.sex, "");
        ClientConfigDao.SetConfigString(this, Constants.google_download_apk, "");
        ClientConfigDao.SetConfigString(this, Constants.google_auth_key, "");
        ClientConfigDao.SetConfigLong(this, Constants.google_auth_create_time, 0);
        ClientConfigDao.SetConfigString(this, Constants.google_auth_qrcode_text, "");
        ClientConfigDao.SetConfigString(this, Constants.isSwitch, "");
        ClientConfigDao.SetConfigString(this, "eyes_open_shut", "");
    }


    /**
     * 保存重新拉取用户redis信息到本地
     *
     * @param resp
     */
    public void setClientConfigDao(Activity context, FreshMemberInfoResp resp) {
        Session.status = "" + resp.getStatus();
        Session.name = "" + resp.getName();
        Session.nickName = "" + resp.getNickName();
        Session.mail = resp.getMail();
        Session.jihuo = "" + resp.getJihuo();
        Session.jibie = "" + resp.getJibie();
        Session.isSettingSet = "" + resp.getIsSettingSet();
        Session.isRealname = "" + resp.getIsRealname();
        Session.isPhone = "" + resp.getIsPhone();
        Session.isMail = "" + resp.getIsMail();
        Session.invitationCode = resp.getInvitationCode();
        Session.id = "" + resp.getId();
        Session.headImg = resp.getHeadImg();
        Session.googleStatus = "" + resp.getGoogleStatus();
        Session.credibility = "" + resp.getCredibility();
        Session.tag = "" + resp.getTag();
        Session.sex = "" + resp.getSex();
        Session.isSwitch = "" + resp.getIsSwitch();

        ClientConfigDao.SetConfigString(context, Constants.status, Session.status);
        ClientConfigDao.SetConfigString(context, Constants.name, Session.name);
        ClientConfigDao.SetConfigString(context, Constants.nickName, Session.nickName);
        ClientConfigDao.SetConfigString(context, Constants.mail, Session.mail);
        ClientConfigDao.SetConfigString(context, Constants.jihuo, Session.jihuo);
        ClientConfigDao.SetConfigString(context, Constants.jibie, Session.jibie);
        ClientConfigDao.SetConfigString(context, Constants.isSettingSet, Session.isSettingSet);
        ClientConfigDao.SetConfigString(context, Constants.isRealname, Session.isRealname);
        ClientConfigDao.SetConfigString(context, Constants.isPhone, Session.isPhone);
        ClientConfigDao.SetConfigString(context, Constants.isMail, Session.isMail);
        ClientConfigDao.SetConfigString(context, Constants.invitationCode, Session.invitationCode);
        ClientConfigDao.SetConfigString(context, Constants.id, Session.id);
        ClientConfigDao.SetConfigString(context, Constants.headImg, Session.headImg);
        ClientConfigDao.SetConfigString(context, Constants.googleStatus, Session.googleStatus);
        ClientConfigDao.SetConfigString(context, Constants.credibility, Session.credibility);
        ClientConfigDao.SetConfigString(context, Constants.person_tag, Session.tag);
        ClientConfigDao.SetConfigString(context, Constants.sex, Session.sex);
        ClientConfigDao.SetConfigString(context, Constants.isSwitch, Session.isSwitch);
        System.out.println("---------------------保存重新拉取用户redis信息到本地------------------------");
    }

    // 获取手机号
    public String getPhoneNumber(Activity maActivity) {

        //            TelephonyManager tm = (TelephonyManager) maActivity.getSystemService(TELEPHONY_SERVICE);
        String phoneInfo = "";
        try {
            //                int status = tm.getSimState();
            //  				phoneInfo = "ANDROID" + "*" + android.os.Build.MODEL + "*" + tm.getDeviceId();
            //  				if(Utils.IsNullOrEmpty(tm.getDeviceId())){
            phoneInfo = "ANDROID" + "*" + Build.MODEL + "*" +
                    "35" + //we make this look like a valid IMEI
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10; //13 digits
            //  				}
        } catch (Exception e) {
            // TODO: handle exception
        }


        return phoneInfo;
    }

    //    /**
    //     * 初识化监听器
    //     */
    //    protected abstract void initClickListener();

    public void jumpActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void jumpForResultActivity(Class<?> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }


    /**
     * 更改应用语言
     *
     * @param locale
     */
    public void changeAppLanguage(Locale locale) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getResources().updateConfiguration(configuration, metrics);
        //        //重新启动Activity
        //        Intent intent = new Intent(this, HomeMainActivity.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        startActivity(intent);
    }


    /**
     * 初始化界面显示的视图
     */
    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaceSource();
    }

    /**
     * 释放资源
     */
    protected abstract void releaceSource();

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (newConfig.fontScale != 1f) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    //    @Override
    //    public Resources getResources() {
    //        Resources res = super.getResources();
    //        if (res.getConfiguration().fontScale != 1f) {
    //            Configuration newConfig = new Configuration();
    //            newConfig.setToDefaults();
    //            res.updateConfiguration(newConfig, res.getDisplayMetrics());
    //        }
    //        return res;
    //    }

    //    @NonNull
    //    @Override
    //    public Lifecycle getLifecycle() {
    //        return null;
    //    }
}
