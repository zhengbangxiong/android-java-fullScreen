package app.gkai.ai.core.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.StatusBarUtil;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.view.LoadingDialog;
import app.gkai.ai.core.common.view.MMToast;
import app.gkai.ai.modules.mine.beans.resp.FreshMemberInfoResp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    
    private Activity mContext;
    private LoadingDialog loadingDialog;
    private MMToast       mmToast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        getClientConfigDao();
        
    }

    /**
     * 仿ios成功提示框
     * @param msg
     */
    public void showToastSuccess(String msg) {
        if (mmToast == null) {
            MMToast.Builder builder=new MMToast.Builder(getActivity())
                    .setMessage(msg)
                    .setSuccess(true);
            mmToast=builder.create();
        }else {
            mmToast.cancel();
            MMToast.Builder builder=new MMToast.Builder(getActivity())
                    .setMessage(msg)
                    .setSuccess(true);
            mmToast=builder.create();
        }
        mmToast.show();
    }

    /**
     * 仿ios错误提示框
     * @param msg
     */
    public void showToastFailure(String msg) {
        if (mmToast == null) {
            MMToast.Builder builder=new MMToast.Builder(getActivity())
                    .setMessage(msg)
                    .setSuccess(false);
            mmToast=builder.create();
        }else {
            mmToast.cancel();
            MMToast.Builder builder=new MMToast.Builder(getActivity())
                    .setMessage(msg)
                    .setSuccess(false);
            mmToast=builder.create();
        }
        mmToast.show();
    }

    /**
     * 仿ios加载框
     * @param msg
     */
    public void showLoadingDialog(String msg) {
        LoadingDialog.Builder builder1=new LoadingDialog.Builder(mContext)
                .setMessage("" + msg)
                .setCancelable(true);
        loadingDialog = builder1.create();
        loadingDialog.show();
    }

    /**
     * 仿ios取消加载框
     */
    public void dismissDialog() {
        try{
            if(loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @param statusColor  状态栏颜色
     * @param statusBarColor  状态栏字体颜色 true为黑色，false为白色
     */
    public void setStatusBar(int statusColor, boolean statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                StatusBarUtil.transparencyBar(mContext);
            } else {
                StatusBarUtil.setStatusBarColor(mContext, getStatusBarColor(statusColor));
            }

            if (isUseBlackFontWithStatusBar(statusBarColor)) {
                StatusBarUtil.setLightStatusBar(mContext, true, isUseFullScreenMode());
            }
        }
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
    public void getClientConfigDao(){

        if(StringUtils.isEmpty(Session.token)) {
            Session.token = ClientConfigDao.GetConfigString(mContext, Constants.token, "");
            if (StringUtils.isEmpty(Session.token)) {
                Session.islogin = false;
            } else {
                Session.islogin = true;
                Session.status = ClientConfigDao.GetConfigString(mContext, Constants.status, "");
                Session.name = ClientConfigDao.GetConfigString(mContext, Constants.name, "");
                Session.nickName = ClientConfigDao.GetConfigString(mContext, Constants.nickName, "");
                Session.mail = ClientConfigDao.GetConfigString(mContext, Constants.mail, "");
                Session.jihuo = ClientConfigDao.GetConfigString(mContext, Constants.jihuo, "");
                Session.jibie = ClientConfigDao.GetConfigString(mContext, Constants.jibie, "");
                Session.isSettingSet = ClientConfigDao.GetConfigString(mContext, Constants.isSettingSet, "");
                Session.isRealname = ClientConfigDao.GetConfigString(mContext, Constants.isRealname, "");
                Session.isPhone = ClientConfigDao.GetConfigString(mContext, Constants.isPhone, "");
                Session.isMail = ClientConfigDao.GetConfigString(mContext, Constants.isMail, "");
                Session.invitationCode = ClientConfigDao.GetConfigString(mContext, Constants.invitationCode, "");
                Session.id = ClientConfigDao.GetConfigString(mContext, Constants.id, "");
                Session.headImg = ClientConfigDao.GetConfigString(mContext, Constants.headImg, "");
                Session.googleStatus = ClientConfigDao.GetConfigString(mContext, Constants.googleStatus, "");
                Session.credibility = ClientConfigDao.GetConfigString(mContext, Constants.credibility, "");
                Session.tag = ClientConfigDao.GetConfigString(mContext, Constants.person_tag, "");
                Session.sex = ClientConfigDao.GetConfigString(mContext, Constants.sex, "");
            }
        }
    }

    /**
     * 保存重新拉取用户redis信息到本地
     * @param resp
     */
    public void setClientConfigDao(Activity context, FreshMemberInfoResp resp){
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

    public void jumpActivity(Class<?> clazz) {
        startActivity(new Intent(mContext, clazz).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }




}
