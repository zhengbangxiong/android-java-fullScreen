package app.gkai.ai.modules.home.service;

import java.util.List;

import app.gkai.ai.core.beans.Page;
import app.gkai.ai.modules.home.beans.req.FindAppUpdateRep;
import app.gkai.ai.modules.home.beans.resp.ArticleResp;
import app.gkai.ai.modules.home.beans.resp.CarouselResp;
import app.gkai.ai.modules.home.beans.resp.RespVersion;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HomeAPI {

    /**
     * 轮播图
     *
     * @return
     */
    @GET("app/api/home/carousel")
    Observable<Resp<CarouselResp>> carousel(@Query("currentPage") String currentPage, @Query("pageSize") String pageSize);

    /**
     * 公告
     */
    @GET("app/api/home/announcement")
    Observable<Resp<Page<ArticleResp>>> announcement(@Query("currentPage") Integer currentPage, @Query("pageSize") Integer pageSize);

    /**
     * 公告详情
     */
    @GET("app/api/home/dt")
    Observable<Resp<ArticleResp>> announcementInfo(@Query("id") Integer articleId);


//    /**
//     * 首页云矿机
//     * @POST：代表post请求
//     * @FormUrlEncoded：用表单数据提交
//     * @Field：替换参数值，是结合post请求的
//     * retrofit+rxjava+Gson
//     */
//    @POST("app/api/machine/home")
//    //@Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    Observable<Resp<Page<CloudMachineResp>>> machineHomeList(@Body RequestBody jsonStr);


    /**
     * 资讯列表
     */
    @GET("app/api/home/news")
    Observable<Resp<Page<ArticleResp>>> news(@Query("currentPage") Integer currentPage, @Query("pageSize") Integer pageSize);


    /**
     * 资讯详情
     */
    @GET("app/api/home/newsInfo")
    Observable<Resp<ArticleResp>> newsInfo(@Query("id") Integer newsId);

    /**
     * 获得推荐URL
     *
     * @return
     */
    @GET("app/api/recommend/friendUrl")
    Observable<Resp<String>> friendUrl();



    /**
     * 帮助列表
     */
    @GET("app/api/home/helps")
    Observable<Resp<Page<ArticleResp>>> helpList(@Query("currentPage") Integer currentPage, @Query("pageSize") Integer pageSize);


    /**
     * 帮助详情
     */
    @GET("app/api/home/helpInfo")
    Observable<Resp<ArticleResp>> HelpInfo(@Query("id") Integer newsId);

    /**
     * APP版本信息
     */
    @GET("coin/api/abApp/getVision")
    Observable<RespVersion<FindAppUpdateRep>> findAppUpdate();

    /**
     * 获取调用哪个滑动令牌 YunPian,YunCun
     */
    @POST("app/api/captcha/slideCaptchaType")
    Observable<Resp<String>> slideCaptchaType();

    /**
     * 查询是否存在需要开启挖矿的矿机
     */
    @POST("app/api/mining/waitOpenMiningMachine")
    Observable<Resp<Integer>> waitOpenMiningMachine();

    /**
     * 矿机类型
     */
    @POST("app/api/machine/type")
    Observable<Resp<List>> machineType();

    /**
     * 矿机类型
     */
    @POST("app/api/machine/homeType")
    Observable<Resp<List>> homeType();

}
