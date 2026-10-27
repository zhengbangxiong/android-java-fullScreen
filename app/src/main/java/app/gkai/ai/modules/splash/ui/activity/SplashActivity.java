package app.gkai.ai.modules.splash.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;
import java.util.Locale;

import app.gkai.ai.R;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.modules.home.ui.activity.AndroidJSWebViewActivity;
import butterknife.ButterKnife;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {

    private String    TAG = "SplashActivity";
    private ImageView img_gif;
    private Thread    myThread;
    private TextView  tv_right;

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash_loading);



//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        OkHttpClient client = new OkHttpClient();
//
//                        Request request = new Request.Builder()
//                                .url("https://api.opensea.io/v2/orders/ethereum/seaport/listings?asset_contract_address=0xd33657710bEC49876C33e2204184C4D38b873f96&limit=50&token_ids=2950")
//                                .get()
//                                .addHeader("Accept", "application/json")
//                                .addHeader("X-API-KEY", "2f6f419a083c46de9d83ce3dbe7db601")
//                                .build();
//
//                        Response response = client.newCall(request).execute();
//
////                        ResponseHandler<String> responseHandler=new BasicResponseHandler();
////                        String responseBody = httpclient.execute(httppost, responseHandler);
////                        JSONObject response2 = new JSONObject(response.body().string());
//
//                    System.out.println(response.body().string() + "------------------response-------------------------");
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }.start();





        //        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);
        tv_right = (TextView) findViewById(R.id.tv_right);
        ClientConfigDao.SetConfigLong(this, Constants.PRODUCTION_ENVIRONMENT, AppUrl.PRODUCTION_ENVIRONMENT);


        //        System.out.println(Locale.getDefault().getCountry() + "----------language-------------" + language);

        if(StringUtils.isEmpty(ClientConfigDao.GetConfigString(this, "DefaultLanguage", ""))){
            Session.LANGUAGE = Constants.LANGUAGE_TW;
            ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
            ClientConfigDao.SetConfigString(this, "DefaultLanguage", "1");
        }

        Session.LANGUAGE = ClientConfigDao.GetConfigString(this, Constants.LANGUAGE, "");
        if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
            tv_right.setText(getResources().getString(R.string.skip));
        } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.TRADITIONAL_CHINESE);
            tv_right.setText("跳過");
        } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.US);
            tv_right.setText("skip");
        }

        if (StringUtils.isEmpty(Session.LANGUAGE)) {
            //            String language = Locale.getDefault().getLanguage();
            //            String country = Locale.getDefault().getCountry();
            //            if("zh".equals(language)){
            //                if("CN".equals(country)){
            //                    Session.LANGUAGE = Constants.LANGUAGE_CN;
            //                    ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
            //                }else{
            //                    Session.LANGUAGE = Constants.LANGUAGE_TW;
            //                    ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
            //
            //                }
            //            }else{
            //                Session.LANGUAGE = Constants.LANGUAGE_EN;
            //                ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
            //
            //            }

            Session.LANGUAGE = Constants.LANGUAGE_TW;
            ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
        }


        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSafeHandler.removeMessages(0);

                if ("1".equals(ClientConfigDao.GetConfigString(SplashActivity.this, Constants.ISFIRST, ""))) {
                    jumpActivity(AndroidJSWebViewActivity.class);

                } else {
                    jumpActivity(GuideActivity.class);
                }
                setFinish();
            }
        });


        img_gif = (ImageView) findViewById(R.id.img_gif);
        String path = "file:///android_asset/animation/splash_loading";
        Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable instanceof GifDrawable) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.setLoopCount(1);
                    //                    img_gif.setImageDrawable(drawable);
                    img_gif.setImageDrawable(drawable);
                    gifDrawable.start();
                }
            }
        });

        if ("1".equals(ClientConfigDao.GetConfigString(this, Constants.ISFIRST, ""))) {
            goActivity("1");
        } else {
            goActivity("2");
        }
    }

    public void goActivity(String index) {
        Message msg = Message.obtain();
        msg.obj = index;
        msg.what = 0;   //标志消息的标志
        mSafeHandler.sendMessageDelayed(msg, 5800);
    }


    private SafeHandler mSafeHandler = new SafeHandler(this);

    /**
     * 根据提示实现的Handler
     */
    private static class SafeHandler extends Handler {

        private WeakReference<SplashActivity> mWeakReference;

        public SafeHandler(SplashActivity activity) {
            super();
            mWeakReference = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SplashActivity activity = mWeakReference.get();
                    if ("1".equals(msg.obj.toString())) {
                        activity.jumpActivity(AndroidJSWebViewActivity.class);
                    } else {
                        activity.jumpActivity(GuideActivity.class);
                    }

                    activity.setFinish();
                    break;
            }


        }
    }

    public void setFinish() {
        this.finish();
    }

    @Override
    protected void releaceSource() {
        Glide.get(SplashActivity.this).clearMemory();
        mSafeHandler.removeMessages(0);
    }
}
