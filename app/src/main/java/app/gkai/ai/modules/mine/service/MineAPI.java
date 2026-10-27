package app.gkai.ai.modules.mine.service;

import app.gkai.ai.core.beans.Page;
import app.gkai.ai.modules.mine.beans.resp.AuthindentityResp;
import app.gkai.ai.modules.mine.beans.resp.PartnerInfoResp;
import app.gkai.ai.modules.mine.beans.resp.RebateListResp;
import app.gkai.ai.modules.mine.beans.resp.TeamMineResp;
import app.gkai.ai.modules.splash.beans.rep.CommonSettingRep;
import app.gkai.ai.modules.userbase.beans.resp.Resp;
import app.gkai.ai.modules.mine.beans.resp.AliOss;
import app.gkai.ai.modules.mine.beans.resp.DemandListResq;
import app.gkai.ai.modules.mine.beans.resp.FindAssetLogResp;
import app.gkai.ai.modules.mine.beans.resp.FreshMemberInfoResp;
import app.gkai.ai.modules.mine.beans.resp.ShowDemandResp;
import app.gkai.ai.modules.mine.beans.resp.TeamwalletInfoResp;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MineAPI {


    /**
     * 我的-->安全中心-->修改登录密码
     */
    @POST("app/api/password/modifyPwd")
    Observable<Resp<Void>> modifyPass(@Body RequestBody jsonStr);

    /**
     * 是否已经实名认证
     */
    @GET("app/api/auth/is_auth")
    Observable<Resp<Integer>> is_auth();

    /**
     * 用户认证信息
     */
    @GET("app/api/auth/authindentity")
    Observable<Resp<AuthindentityResp>> authindentity();

    /**
     * 我的-->安全中心-->会员提交 实名认证信息
     */
    @POST("app/api/auth/addAuthIdentity")
    Observable<Resp<Void>> authIdentity(@Body RequestBody jsonStr);

    /**
     * 阿里OSS上传Policy
     */
    @GET("app/api/member/info/aliOssPolicy")
    Observable<Resp<AliOss>> aliOssPolicy();


    /**
     * 阿里OSS上传Policy
     *
     * @Part MultipartBody.Part file
     */
    @Multipart
    @POST("https://waterio.oss-cn-hongkong.aliyuncs.com")
    Observable<ResponseBody> aliOssUpload(
            //@Path("url")String url,
            @Query("policy") String policy,
            @Query("OSSAccessKeyId") String OSSAccessKeyId,
            @Query("success_action_status") String success_action_status,
            @Query("signature") String signature,
            @Query("key") String key,
            @Part RequestBody file
            //@Part MultipartBody.Part file
    );


    /**
     * 修改个人信息
     */
    @POST("app/api/member/info/modifyMemberInfo")
    Observable<Resp<Void>> modifyMemberInfo(@Body RequestBody jsonStr);

    /**
     * 滑动成功获取令牌
     *
     * @POST：代表post请求
     * @Field：替换参数值，是结合post请求的 retrofit+rxjava+Gson
     */
    @POST("app/api/captcha/ypCheckCaptcha")
    @FormUrlEncoded
    Observable<Resp<String>> PostYpCheckCaptcha(@Field("authenticate") String authenticate, @Field("token") String token);

    //    /**
    //     * 发送邮件验证码(需登录)
    //     * @param jsonStr
    //     * @return
    //     */
    //    @GET("app/api/mail/sendmailByLogin")
    //    Call<Resp<Void>> sendmailByLogin(@Body RequestBody jsonStr);

    /**
     * 发送邮件验证码(需登录)
     *
     * @param jsonStr
     * @return
     */
    @POST("app/api/mail/sendmailByLogin")
    Observable<Resp<Void>> sendmailByLogin(@Body RequestBody jsonStr);

    /**
     * 用户工单列表
     * retrofit+rxjava+Gson
     */
    @GET("app/api/demand/demandList/{page}/{pageSize}")
    Observable<Resp<DemandListResq>> demandList(@Path("page") String page1, @Path("pageSize") String pageSize1, @Query("page") String page, @Query("pageSize") String pageSize);

    /**
     * 提交工单
     *
     * @param jsonStr
     * @return
     */
    @POST("app/api/demand/addDemand")
    Observable<Resp<Void>> addDemand(@Body RequestBody jsonStr);

    /**
     * 工单详细信息
     *
     * @return
     */

    @GET("app/api/demand/showDemand/{id}")
    Observable<Resp<ShowDemandResp>> showDemand(@Path("id") String id1, @Query("id") String id);

    /**
     * 工单下继续留言
     *
     * @return
     */
    @POST("app/api/demand/addDemandLog")
    Observable<Resp<Void>> addDemandLog(@Body RequestBody jsonStr);

    /**
     * 获得推荐URL
     *
     * @return
     */
    @GET("app/api/recommend/friendUrl")
    Observable<Resp<String>> friendUrl();

    /**
     * 我的团队
     */
    @POST("app/api/team/mine")
    Observable<Resp<TeamMineResp>> mine();

    /**
     * 返佣记录
     */
    @POST("app/api/team/rebateList")
    Observable<Resp<Page<RebateListResp>>> rebateList(@Body RequestBody jsonStr);

    /**
     * 查询是否满足代理商申请条件
     */
    @POST("app/api/partner/query")
    Observable<Resp<Integer>> querypartner();

    /**
     * 代理商申请信息
     */
    @POST("app/api/partner/partnerInfo")
    Observable<Resp<PartnerInfoResp>> partnerInfo(@Body RequestBody jsonStr);


    /**
     * 申请代理商
     */
    @POST("app/api/partner/apply")
    Observable<Resp<Void>> partnerApply(@Body RequestBody jsonStr);

    /**
     * 查询用户账单记录
     */
    @POST("app/api/asset/findAssetLog")
    Observable<Resp<Page<FindAssetLogResp>>> findAssetLog(@Body RequestBody jsonStr);

    /**
     * 查询团队钱包记录
     */
    @POST("app/api/teamwallet/log/list")
    @FormUrlEncoded
    Observable<Resp<Page<FindAssetLogResp>>> teamwalletLogList(@Field("page") String page, @Field("pageSize") String pageSize, @Field("type") String type);


    /**
     * 资产信息
     *
     * @return
     */
    @GET("app/api/teamwallet/info")
    Observable<Resp<List<TeamwalletInfoResp>>> teamwalletinfo();

//    /**
//     * 提交提币
//     *
//     * @return
//     */
//    @POST("app/api/teamwallet/coinWithdraw")
//    Observable<Resp<List<ConfigListResp>>> coinWithdraw(@Body RequestBody jsonStr);
//
//    /**
//     * 划转至普通钱包
//     *
//     * @return
//     */
//    @POST("app/api/teamwallet/coinTransfer")
//    Observable<Resp<List<ConfigListResp>>> coinTransfer(@Body RequestBody jsonStr);
//
//    /**
//     * 当前用户提币记录
//     */
//    @GET("app/api/teamwallet/findWithdraw")
//    Observable<Resp<Page<FindWithdrawResp>>> findWithdraw(@Query("pageNum") Integer pageNum, @Query("pageSize") Integer pageSize, @Query("withdrawType") Integer withdrawType);

    /**
     * 重新拉取用户redis信息
     */
    @POST("app/api/member/info/freshMemberInfo")
    Observable<Resp<FreshMemberInfoResp>> freshMemberInfo();

    /**
     * 系统基本设置
     *
     * @return
     */
    @GET("app/api/common/commonSetting")
    Observable<Resp<CommonSettingRep>> commonSetting(@Query("id") int id);


    /**
     * 申请解绑
     */
    @POST("app/api/google/addUnbind")
    Observable<Resp<Void>> addUnbind(@Body RequestBody jsonStr);
}
