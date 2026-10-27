package app.gkai.ai.core.common.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.constant.Session;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

    public volatile static RetrofitUtil sInstance;
    private                Retrofit     mRetrofit;

    private Map<String, Object> apiMap = new ConcurrentHashMap<>();

    private HttpAPI mHttpAPI;

    private RetrofitUtil() {
        GsonBuilder builder = new GsonBuilder();
        //        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        //            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //                return new Date(json.getAsJsonPrimitive().getAsLong());
        //            }
        //        });
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = builder.create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppUrl.getEnvironment())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpMethod())
                .build();
        mHttpAPI = mRetrofit.create(HttpAPI.class);
    }

    public static RetrofitUtil getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitUtil();
                }
            }
        }
        return sInstance;
    }

    private static OkHttpClient httpMethod() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    //                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("OKHttp-----", message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("OKHttp-----", message);
                }
            }
        });
        Log.e("OKHttp-----token:", Session.token);
        //这里可以builder(). 添加更多的内容 具体看需求
        OkHttpClient mClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new MyRetrofitInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .build();
        //这行必须加 不然默认不打印
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return mClient;
    }

    public HttpAPI getHttpAPI() {
        return mHttpAPI;
    }

    public <T> T getHttpAPI(Class<T> classz) {
        String key = classz.getPackage() + "@" + classz.getSimpleName();
        if (apiMap.containsKey(key)) {
            return (T) apiMap.get(key);
        }
        T t = mRetrofit.create(classz);
        apiMap.put(key, t);
        return t;
    }
}
