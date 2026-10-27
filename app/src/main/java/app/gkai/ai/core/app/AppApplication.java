package app.gkai.ai.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

// import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.util.NavigationBarUtil;

/**
 */
public class AppApplication extends MultiDexApplication {
    private static Context mContext;
    private  static Handler mHandler;
    private static long mMainThreadId;

    public static Context getmContext(){
        return mContext;
    }
    public static Handler getmHandler(){
        return mHandler;
    }
    public static Long getMainThread(){
        return mMainThreadId;
    }

    @Override
    public void onCreate() {

        //上下文路径
        mContext = getApplicationContext();
        //全局handler
        mHandler = new Handler();
        //获取主线程id
        mMainThreadId = android.os.Process.myTid();

        super.onCreate();

        // Apply the same bottom-only behavior to library activities as well.
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                NavigationBarUtil.showGestureBarOnContent(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                NavigationBarUtil.showGestureBarOnContent(activity);
            }

            @Override public void onActivityStarted(Activity activity) { }
            @Override public void onActivityPaused(Activity activity) { }
            @Override public void onActivityStopped(Activity activity) { }
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
            @Override public void onActivityDestroyed(Activity activity) { }
        });


        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        // CrashReport.initCrashReport(getApplicationContext(), AppUrl.BUGLY_ID, false);


//        try{
//            Resources res = super.getResources();
//            Configuration config = new Configuration();
//            config.setToDefaults();
//            res.updateConfiguration(config, res.getDisplayMetrics());
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
