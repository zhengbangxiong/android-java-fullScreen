package app.gkai.ai.modules.mine.service;


import android.util.Log;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;

import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.network.BaseObserver;
import app.gkai.ai.core.common.network.RetrofitUtil;
import app.gkai.ai.core.common.network.RxHelper;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.modules.mine.beans.resp.AliOss;
import app.gkai.ai.modules.mine.ui.activity.AuthenticationPictureActivity;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AliyunOss {

    /**
     * 阿里OSS上传Policy
     *
     */
    public void aliOssPolicy(AuthenticationPictureActivity context, int result_code, File file) {
//        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
//                .aliOssPolicy()
//                .enqueue(new CommonCallback<Resp<AliOss>>(context, (call, response) -> {
//
//                    AliOss aliOss=response.body().getData();
//                    AliyunOss aliyunOss=new AliyunOss();
//                    aliyunOss.AliyunOssPath(context,aliOss,result_code,file);
//
//                    //                    if (status== ResultConstant.SUCCESS) {
//                    //                        context.dismissDialog();
//                    //                        context.showToastSuccess(context.getResources().getString(me.yuncun.one.R.string.success));
//                    //                        context.getIs_auth(response.body().getData().getData());
//                    //                    } else {
//                    //                        context.dismissDialog();
//                    //                        MyDirectToast.show(context, MyDirectToast.waring, response.body().getMessage());
//                    //                    }
//
//                }));

        RetrofitUtil.getInstance().getHttpAPI(MineAPI.class)
                .aliOssPolicy()
                .compose(RxHelper.io_main())
                .as(AutoDispose.<Resp<AliOss>>autoDisposable(AndroidLifecycleScopeProvider.from(context)))//此处为防止内存泄漏的解决方法
                .subscribe(new BaseObserver<Resp<AliOss>>(context) {
                    @Override
                    protected void onSuccees(Resp<AliOss> bean) throws Exception {
                        AliOss aliOss=bean.getData();
                        AliyunOss aliyunOss=new AliyunOss();
                        aliyunOss.AliyunOssPath(context,aliOss,result_code,file);
                    }

                    @Override
                    protected void onFailure(Throwable e) throws Exception {
                        context.dismissDialog();
                        context.showToastFailure(context.getResources().getString(app.gkai.ai.R.string.service_busy));
                    }

                });
    }

    public void AliyunOssPath(AuthenticationPictureActivity context, AliOss aliOss, int result_code, File file){


        new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    String fileName= Session.token + System.currentTimeMillis() + StringUtils.getUUIDByRules32Image();
                    String key= aliOss.getDir()+fileName;

                    // MultipartBuilder，是上传文件的query
                    // addFormDataPart方法：@param [String]name, [String]value
                    // addFormDataPart方法：@param [String]name, [String]fileName, [String]fileType, [String]file
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("key", key)
                            .addFormDataPart("policy", aliOss.getPolicy())
                            .addFormDataPart("OSSAccessKeyId", aliOss.getAccessid())
                            .addFormDataPart("success_action_status", "200")
                            .addFormDataPart("signature", aliOss.getSignature())
                            .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/png"), file))

                            .build();


                    // request方法： @param [String]URL, [RequestBody]requestBody
                    Request request = new Request.Builder()
                            .url(aliOss.getHost())
                            .post(requestBody)
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(80, TimeUnit.SECONDS)
                            .build();

                    // response储存服务器的回应
                    Response response = okHttpClient.newCall(request).execute();
                    // 把response转换成string
                    String resposneJson= response.body().string();
                    Log.e("OKHttp----- OK---------",resposneJson);

                   String fileUrl = aliOss.getHost()+"/"+key;
                    context.getPath(fileUrl, result_code);
                    Log.e("OKHttp----- OK---------",fileUrl);
//                    MyDirectToast.show(context, MyDirectToast.waring, resposneJson);
                }catch (Exception e){
                    Log.e("OKHttp-----",e.getMessage());
                    context.getPictureError();
//                    MyDirectToast.show(context, MyDirectToast.fail, e.getMessage());
                }

            }
        }.start();




//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
//        //MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//
//        String  key=aliOss.getDir()+"7898456133.jpg";
//        RetrofitUtil.getInstance().getHttpAPI(MiningAPI.class)
//                .aliOssUpload(
//                        //aliOss.getHost(),
//                        aliOss.getPolicy(),aliOss.getAccessid(),"200",aliOss.getSignature(),key, requestFile)
//                .enqueue(new CommonCallback<ResponseBody>(context, (call, response) -> {
//
//                    String resposneJson= response.body().string();
//                    String fileUrl = aliOss.getHost()+"/"+key;
//                    Log.e("OKHttp-----",resposneJson);
//                    Log.e("OKHttp-----",fileUrl);
//                    MyDirectToast.show(context, MyDirectToast.waring, resposneJson);
//
//                    //                    if (status== ResultConstant.SUCCESS) {
//                    //                        context.dismissDialog();
//                    //                        context.showToastSuccess(context.getResources().getString(me.yuncun.one.R.string.success));
//                    //                        context.getIs_auth(response.body().getData().getData());
//                    //                    } else {
//                    //                        context.dismissDialog();
//                    //                        MyDirectToast.show(context, MyDirectToast.waring, response.body().getMessage());
//                    //                    }
//
//                }));



    }



    public void AliyunOssPath2(AuthenticationPictureActivity context, File file, String path){


        new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    String fileName= Session.token + System.currentTimeMillis() + StringUtils.getUUIDByRules32Image();
                    // MultipartBuilder，是上传文件的query
                    // addFormDataPart方法：@param [String]name, [String]value
                    // addFormDataPart方法：@param [String]name, [String]fileName, [String]fileType, [String]file
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/png"), file))
                            .build();


                    // request方法： @param [String]URL, [RequestBody]requestBody
                    Request request = new Request.Builder()
                            .addHeader("lang", Session.LANGUAGE)
                            .url("" + AppUrl.getEnvironment() + "app/api/auth/scanAuthindentity")
                            .post(requestBody)
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(80, TimeUnit.SECONDS)
                            .build();

                    // response储存服务器的回应
                    Response response = okHttpClient.newCall(request).execute();
                    // 把response转换成string
                    String resposneJson= response.body().string();
                    Log.e("OKHttp-----OK---------",resposneJson);

                    context.getOCRResult(resposneJson, path);
                }catch (Exception e){
                    Log.e("OKHttp-----",e.getMessage());
                    context.getPictureError();
                }

            }
        }.start();


    }





}
