package app.gkai.ai.modules.splash.service;

import app.gkai.ai.modules.splash.beans.rep.CommonSettingRep;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.ResultConstant;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.network.BaseObserver;
import app.gkai.ai.core.common.network.RetrofitUtil;
import app.gkai.ai.core.common.network.RxHelper;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.splash.ui.activity.GuideActivity;

public class GuideService {

    /**
     * 系统基本设置
     */
    public void commonSetting(GuideActivity context) {
        //        RetrofitUtil.getInstance().getHttpAPI(SplashAPI.class)
        //                .commonSetting(1)
        //                .enqueue(new CommonCallback<Resp<CommonSettingRep>>(context, (call, response) -> {
        //
        //                    if(null != context) {
        //                        context.dismissDialog();
        //                        int status = response.body().getStatus();
        //                        if (status == ResultConstant.SUCCESS) {
        //                            context.SetStartWebView(response.body().getData().getDes());
        //
        //                        } else {
        //                            context.dismissDialog();
        //                            MyDirectToast.show(context, MyDirectToast.waring, response.body().getMessage());
        //                        }
        //                    }
        //                }));
        String sname = "agreement_cn";
        if(Constants.LANGUAGE_CN.equals(Session.LANGUAGE)){
            sname = "agreement_cn";
        }else if(Constants.LANGUAGE_EN.equals(Session.LANGUAGE)){
            sname = "agreement_en";
        }

        RetrofitUtil.getInstance().getHttpAPI(SplashAPI.class)
                // TODO: 2021/7/30 修改配置   这里改成传sname 
                .commonSetting(sname)
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<CommonSettingRep>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<CommonSettingRep>>(context) {
                    @Override
                    protected void onSuccees(Resp<CommonSettingRep> bean) throws Exception {
                        if (null != context) {
                            context.dismissDialog();
                            int status = bean.getStatus();
                            if (status == ResultConstant.SUCCESS) {
                                //                            Orders orders = response.body().getData();
                                // TODO: 2021/8/13 修改配置 getDes改成getValue();
                                context.SetStartWebView(bean.getData().getDes());

                            } else {
                                MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        e.printStackTrace();
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.service_busy));
                    }

                });
    }


}
