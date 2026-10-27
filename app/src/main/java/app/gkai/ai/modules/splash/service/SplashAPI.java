/**
 * This file is part of the zngin software package
 * @copyright (c) zngin <https://www.zngin.com>
 * @author:zhengbangxiong
 * @date: 2019-08-02
 * @version：1.02
 */
package app.gkai.ai.modules.splash.service;

import app.gkai.ai.modules.splash.beans.rep.CommonSettingRep;
import app.gkai.ai.modules.userbase.beans.resp.Resp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 *
 */
public interface SplashAPI {


 /**
  * 系统基本设置
  *
  * @return
  */
 // TODO: 2021/7/30 修改配置   这里改成传sname
 @GET("app/api/common/commonSetting")
 Observable<Resp<CommonSettingRep>> commonSetting(@Query("sname") String sname);



}