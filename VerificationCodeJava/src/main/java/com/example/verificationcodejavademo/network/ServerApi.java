package com.example.verificationcodejavademo.network;


import com.example.verificationcodejavademo.model.CaptchaCheckIt;
import com.example.verificationcodejavademo.model.CaptchaGetIt;
import com.example.verificationcodejavademo.model.WordCaptchaGetIt;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */
public interface ServerApi {

//    final static String BaseUrl = "http://192.168.3.6:8000/app/api/";
//      final static String BaseUrl = "http://47.90.4.173:10000/app/api/";
//      final static String BaseUrl = "http://47.90.4.173:8100/app/api/";
    //生产网络地址
    final static String BaseUrl = "http://47.90.4.173:8000/app/api/";

    //获取验证码
    @POST("captcha/slide/get")
    Observable<BaseResponse<CaptchaGetIt>> getAsync(@Body RequestBody body);

    //获取文字的验证码
    @POST("captcha/slide/get")
    Observable<BaseResponse<WordCaptchaGetIt>> getWordCaptchaAsync(@Body RequestBody body);

    //核对验证码
    @POST("captcha/slide/check")
    Observable<BaseResponse<CaptchaCheckIt>> checkAsync(@Body RequestBody body);
}
