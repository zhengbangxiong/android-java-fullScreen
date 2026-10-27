package app.gkai.ai.modules.mine.service;

import com.qipeng.captcha.QPCaptcha;
import com.qipeng.captcha.QPCaptchaConfig;
import com.qipeng.captcha.QPCaptchaListener;
import com.qipeng.captcha.utils.QPUtils;
import app.gkai.ai.core.app.AppApplication;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.constant.ResultConstant;
import app.gkai.ai.core.common.network.BaseObserver;
import app.gkai.ai.core.common.network.RetrofitUtil;
import app.gkai.ai.core.common.network.RxHelper;
import app.gkai.ai.core.common.util.JsonUtils;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.mine.beans.req.AuthIdentityReq;
import app.gkai.ai.modules.mine.beans.req.SendMailReq;
import app.gkai.ai.modules.mine.beans.req.UnbindReq;
import app.gkai.ai.modules.mine.ui.activity.AuthenticationActivity;

import org.json.JSONObject;

/**
 * 会员实名认证
 */
public class MemberAuthService {

    private String username = "";

    /**
     * yunpian加载滑动条
     */
    public void yunpian(AuthenticationActivity context, String name) {
        username = name;
        QPCaptcha.getInstance().init(AppApplication.getmContext(), AppUrl.YUNPIAN_ID);
        QPCaptchaConfig config = new QPCaptchaConfig.Builder(context)
                .setAlpha(0.7f)
                .setWidth(QPUtils.getScreenWidth(context) - QPUtils.dip2px(context, 32f))
                .setLangPackModel(null)
                .showLoadingView(true)
                .setLang(false ? QPCaptchaConfig.LANG_EN : QPCaptchaConfig.LANG_ZH)
                .setCallback(new QPCaptchaListener() {
                    @Override
                    public void onLoaded() {
                        //                        Toast.makeText(context, "onLoaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String msg) {
                        //                        Toast.makeText(context, "onSuccess = " + msg, Toast.LENGTH_SHORT).show();
                        try {

                            JSONObject jsonobject = new JSONObject(msg.toString());
                            context.showDialog();
                            getYpCheckCaptcha(context, jsonobject.getString("authenticate"), jsonobject.getString("token"));
                        } catch (Exception e) {
                            context.dismissDialog();
                            MyDirectToast.show(context, MyDirectToast.waring, context.getResources().getString(app.gkai.ai.R.string.service_busy));
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFail(String msg) {
                        //                        Toast.makeText(context, "onFail msg = " + msg, Toast.LENGTH_SHORT).show();
                        //                        MyDirectToast.show(context, MyDirectToast.waring, "" + msg);
                    }

                    @Override
                    public void onError(String msg) {
                        //                        Toast.makeText(context, "onError msg = " + msg, Toast.LENGTH_SHORT).show();
                        MyDirectToast.show(context, MyDirectToast.waring, "" + msg);
                    }

                    @Override
                    public void onCancel() {
                        //                        Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        QPCaptcha.getInstance().verify(config);

    }

    /**
     * 滑动成功获取令牌
     *
     * @param context
     * @param authenticate
     * @param token
     */
    public void getYpCheckCaptcha(AuthenticationActivity context, String authenticate, String token) {

        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
                .PostYpCheckCaptcha(authenticate, token)
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<String>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<String>>(context) {
                    @Override
                    protected void onSuccees(Resp<String> bean) throws Exception {
                        int status = bean.getStatus();
                        if (status == ResultConstant.SUCCESS) {
                            SendMailReq sendMailReq = new SendMailReq();
                            sendMailReq.setName(username);
                            sendMailReq.setCheckCodeToken(bean.getData());
                            sendMail(context, sendMailReq);
                        } else {
                            context.dismissDialog();
                            MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.network_timeout));
                        e.printStackTrace();
                    }

                });

    }

    /**
     * 发送邮件验证码(需登录)
     */
    public void sendMail(AuthenticationActivity context, SendMailReq sendMailReq) {
        sendMailReq.setFrom("google");

        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
                .sendmailByLogin(JsonUtils.getJsonBody(JsonUtils.toJsonStr(sendMailReq)))
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<Void>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<Void>>(context) {
                    @Override
                    protected void onSuccees(Resp<Void> bean) throws Exception {
                        int status = bean.getStatus();
                        if (status == ResultConstant.SUCCESS) {
                            context.dismissDialog();
                            context.setCountDownTimerUtils();
                        } else {
                            context.dismissDialog();
                            MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.network_timeout));
                        e.printStackTrace();
                    }

                });
    }


    /**
     * 提交实名认证信息
     *
     */
    public void authIdentity(AuthenticationActivity context, AuthIdentityReq authIdentityReq) {

        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
                .authIdentity(JsonUtils.getJsonBody(JsonUtils.toJsonStr(authIdentityReq)))
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<Void>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<Void>>(context) {
                    @Override
                    protected void onSuccees(Resp<Void> bean) throws Exception {
                        int status = bean.getStatus();
                        if (status== ResultConstant.SUCCESS) {
                            context.dismissDialog();
                            context.showToastSuccess(context.getResources().getString(app.gkai.ai.R.string.success));
                            context.setStartActivity();
                        } else {
                            context.dismissDialog();
                            MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.service_busy));
                    }

                });
    }




    /**
     * 申请解绑
     *
     */
    public void addUnbind(AuthenticationActivity context, UnbindReq mUnbindReq) {

        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
                .addUnbind(JsonUtils.getJsonBody(JsonUtils.toJsonStr(mUnbindReq)))
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<Void>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<Void>>(context) {
                    @Override
                    protected void onSuccees(Resp<Void> bean) throws Exception {
                        int status = bean.getStatus();
                        if (status== ResultConstant.SUCCESS) {
                            context.dismissDialog();
                            context.showToastSuccess(context.getResources().getString(app.gkai.ai.R.string.success));
                            context.setStartActivity();
                        } else {
                            context.dismissDialog();
                            MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.service_busy));
                    }

                });
    }






}
