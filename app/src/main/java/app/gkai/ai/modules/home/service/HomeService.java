package app.gkai.ai.modules.home.service;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import app.gkai.ai.core.common.constant.ResultConstant;
import app.gkai.ai.core.common.network.RetrofitUtil;
import app.gkai.ai.core.common.network.RxHelper;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.home.beans.req.FindAppUpdateRep;
import app.gkai.ai.modules.home.beans.resp.RespVersion;
import app.gkai.ai.modules.home.ui.activity.AndroidJSWebViewActivity;
import app.gkai.ai.modules.mine.ui.activity.Promote;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeService {

    /**
     * APP版本信息
     */
    public void findAppUpdate2(AndroidJSWebViewActivity context){

        RetrofitUtil.getInstance().getHttpAPI(HomeAPI.class)
                .findAppUpdate()
                .compose(RxHelper.io_main())
                .subscribe(new Observer<RespVersion<FindAppUpdateRep>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(RespVersion<FindAppUpdateRep> bean) {
                        if (null != context) {
                            int status = bean.getCode();
                            if (status == ResultConstant.SUCCESS) {
                                FindAppUpdateRep findAppUpdateRep = bean.getResult();

                                context.showUploadDialog(findAppUpdateRep);
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

    /**
     * 获得推荐URL
     */
    public void friendUrl(Promote context) {
        //        RetrofitUtil.getInstance().getHttpAPI(HomeAPI.class)
        //                .friendUrl()
        //                .enqueue(new CommonCallback<Resp<String>>(context, (call, response) -> {
        //
        //                    if (null != context) {
        //                        context.dismissDialog();
        //                        int status = response.body().getStatus();
        //                        if (status == ResultConstant.SUCCESS) {
        //                            context.getFriendUrl(response.body().getData());
        //                        } else {
        //                            MyDirectToast.show(context, MyDirectToast.waring, response.body().getMessage());
        //                        }
        //                    }
        //                }));

        RetrofitUtil.getInstance().getHttpAPI(HomeAPI.class)
                .friendUrl()
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<String>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new Observer<Resp<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Resp<String> bean) {
                        if (null != context) {
                            context.dismissDialog();
                            int status = bean.getStatus();
                            if (status == ResultConstant.SUCCESS) {
                                context.getFriendUrl(bean.getData());
                            } else if(status == 10001){
                              context.setFinishToLogin();
                            } else {
                                MyDirectToast.show(context, MyDirectToast.waring, bean.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

}
