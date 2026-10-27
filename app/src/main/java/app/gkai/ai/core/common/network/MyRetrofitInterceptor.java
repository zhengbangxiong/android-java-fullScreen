package app.gkai.ai.core.common.network;


import java.io.IOException;

import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.util.StringUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyRetrofitInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path= originalRequest.url().url().getPath();
//        if (!"/login".contains(path)) {
//            return chain.proceed(originalRequest);
//        }
        Request newRequest;

        if(Session.LANGUAGE.equals("") || Session.LANGUAGE.equals("null")){
            Session.LANGUAGE = "EN";
        }

        if(StringUtils.isEmpty(Session.token)){

            newRequest = originalRequest.newBuilder()
                    .header("X-Member-Lan", Session.LANGUAGE.replace("CN", "cn").replace("TW", "hk").replace("EN", "en"))
                    .header("Terminal-Code", Session.IMEI)
                    .header("Terminal-Type", "android")
//                    .header("companyId", AppUrl.companyId)
                    .build();
        }else{
            newRequest = originalRequest.newBuilder()
                    .header("token", Session.token)
                    .header("X-Member-Lan", Session.LANGUAGE.replace("CN", "cn").replace("TW", "hk").replace("EN", "en"))
                    .header("Terminal-Code", Session.IMEI)
                    .header("Terminal-Type", "android")
//                    .header("companyId", AppUrl.companyId)
                    .build();
        }
        return chain.proceed(newRequest);
    }
}