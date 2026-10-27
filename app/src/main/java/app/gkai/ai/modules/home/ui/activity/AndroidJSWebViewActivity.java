package app.gkai.ai.modules.home.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.test_webview_demo.utils.X5WebView;
import com.king.zxing.util.CodeUtils;
import com.nestia.biometriclib.BiometricPromptManager;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.wildma.idcardcamera.camera.IDCardCamera;
import com.wildma.idcardcamera.utils.PermissionUtils;
import com.wildma.pictureselector.PictureSelectActivity;
import com.wildma.pictureselector.PictureSelector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.gkai.ai.R;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.constant.AppUrl;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.network.RetrofitUtil;
import app.gkai.ai.core.common.util.BigDecimalCalculation;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.DownloadUtils;
import app.gkai.ai.core.common.util.JsonUtils;
import app.gkai.ai.core.common.util.StatusBarUtil;
import app.gkai.ai.core.common.view.AppUpdateProgressDialog2;
import app.gkai.ai.core.common.view.DownloadApk2;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.home.beans.req.FindAppUpdateRep;
import app.gkai.ai.modules.home.beans.req.FingerStateReq;
import app.gkai.ai.modules.home.service.HomeService;
import app.gkai.ai.modules.mine.beans.req.LoginFingerReq;
import app.gkai.ai.modules.mine.ui.activity.AuthenticationPictureActivity;
import app.gkai.ai.modules.mine.ui.activity.Promote;

/**
 * x5webview
 * zbx
 */
public class AndroidJSWebViewActivity extends BaseActivity {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;

    private static final String mHomeUrl = "file:///android_asset/index.html";
    //        private static final String mHomeUrl = "http://192.168.3.179:8080/";

    private static final String  TAG           = "SdkDemo";
    private static final int     MAX_LENGTH    = 14;
    private              boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable  = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private String mIntentUrl;

    private String      title           = "";
    private Intent      intent;
    private boolean     isToast         = true;//是否弹吐司，为了保证for循环只弹一次
    private String      google_apk_url  = "";
    private String      partner_yqm_url = "";
    private String      permisionIndex  = "";
    private HomeService mHomeService    = new HomeService();
    Button       bt_next;
    LinearLayout llt_progress;
    private String upload_url;
    private Dialog mydialog;
    private boolean waitingForInstallPermission;
    private static final int REQUEST_INSTALL_PERMISSION = 1000;
    private int    cleanHtmlIndex = 1; //第一次进界面清除，之后不清除 ip地址

    private BiometricPromptManager mManager;

    /**
     * 指纹id汇总
     */
    private String fingerId = "";

    private final       int REQUEST_CODE = 10001; //权限
    public static final int CHOOSE_PHOTO = 2;//从相册取照片

    private String statusBarHeight = "0";


    //    private View decorView2;
    //    private ViewGroup.LayoutParams layoutParams;
    //    private double currentHeight;

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setStatusBar(R.color.color_000000, false);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
            //设置状态栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //设置导航键颜色也为透明
            //            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        } else {
            //5.0以下，就单纯的全屏就完了
            //            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //        System.out.println("--------------checkDeviceHasNavigationBar---------------------");
        //        System.out.println(checkDeviceHasNavigationBar(AndroidJSWebViewActivity.this));


        Intent intent = getIntent();
        if (intent != null) {
            try {
                //				mIntentUrl = new URL(intent.getData().toString());
                mIntentUrl = intent.getStringExtra("data");
                title = intent.getStringExtra("title");
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

        //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //                    // 先判断有没有权限
        //                    if (Environment.isExternalStorageManager()) {
        //                    } else {
        //                        intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        //                        intent.setData(Uri.parse("package:" + getPackageName()));
        //                        startActivityForResult(intent, REQUEST_CODE);
        //                    }
        //                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //                    // 先判断有没有权限
        //                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
        //                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        //                    } else {
        //                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        //                    }
        //                } else {
        //                }


        //        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|
        //                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /*
         * getWindow().addFlags(
         * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */
        setContentView(R.layout.activity_webview);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        AndroidBug5497Workaround.assistActivity(mViewParent, AndroidJSWebViewActivity.this);


        initBtnListenser();

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

        Session.LANGUAGE = ClientConfigDao.GetConfigString(this, Constants.LANGUAGE, "");
        getVersionName(this);
        mHomeService.findAppUpdate2(this);

        try {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
//                statusBarHeight = "" + new BigDecimalCalculation("" + getResources().getDimensionPixelSize(resourceId)).div("2").result();
                int statusBarHeightPx = getResources().getDimensionPixelSize(resourceId);

                // 转换为 H5 用的逻辑像素
                float density = getResources().getDisplayMetrics().density;
                statusBarHeight = "" + new BigDecimalCalculation("" + statusBarHeightPx).div("" + density).result();
            }else {
                statusBarHeight = "44";
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            if (23 <= Build.VERSION.SDK_INT) {
                mManager = BiometricPromptManager.from(this);
                getFingerprintInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void changGoForwardButton(WebView view) {

    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(com.example.test_webview_demo.R.id.progressBar1);// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(com.example.test_webview_demo.R.drawable.color_progressbar));
    }

    private void init() {

        //        System.out.println("ANDROID" + getPhoneNumber(AndroidJSWebViewActivity.this) + "','" + getVersionName(AndroidJSWebViewActivity.this) + "','" + Build.BRAND + " " + Build.MODEL + "','" + Build.VERSION.RELEASE + "')");

        mWebView = new X5WebView(this, null);
        mWebView.setBackgroundColor(Color.TRANSPARENT);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用系统浏览器打开链接
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //安卓调用js方法。注意需要在 onPageFinished 回调里调用
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                        //                        System.out.println("ANDROID," + getPhoneNumber(AndroidJSWebViewActivity.this) + "','" + getVersionName(AndroidJSWebViewActivity.this) + "','" + Build.BRAND + " " + Build.MODEL + "','" + Build.VERSION.RELEASE );
                        //                        tbsWebView.loadUrl("javascript:callJsFunction('soloname')");
                        mWebView.loadUrl("javascript:jsFunctionArg('ANDROID')"); //告诉js是Android设备
                        mWebView.loadUrl("javascript:setJsLanguage('ANDROID','" + Session.LANGUAGE + "','" + getPhoneNumber(AndroidJSWebViewActivity.this) + "')"); //告诉js是Android设备，传语言，传设备号
                        mWebView.loadUrl("javascript:jsFunctionArg2('ANDROID','" + cleanHtmlIndex + "')"); //只启用一次
                        mWebView.loadUrl("javascript:androidToJSversion('ANDROID','" + getVersionName(AndroidJSWebViewActivity.this) + "')"); //android app版本号
                        mWebView.loadUrl("javascript:androidToJs('ANDROID','" + getPhoneNumber(AndroidJSWebViewActivity.this) + "')"); //android手机设备号
                        mWebView.loadUrl("javascript:androidToJsLoginVersion('ANDROID','" + getPhoneNumber(AndroidJSWebViewActivity.this) + "','" + getVersionName(AndroidJSWebViewActivity.this) + "','" + Build.BRAND + " " + Build.MODEL + "','" + Build.VERSION.RELEASE + "')");
                        mWebView.loadUrl("javascript:androidToStatusBarHeight('ANDROID','" + statusBarHeight + "')"); //android

                        //                        mWebView.loadUrl("javascript:(function() { " +
                        //                                "var inputs = document.getElementsByTagName('input'); " +
                        //                                "if (inputs.length > 0) { inputs[0].focus(); } " +
                        //                                "})();");
                    }
                });


                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
                /* mWebView.showLog("test Log"); */
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("onReceivedError", "onReceivedError:" + error.getDescription() + " url:" + request.getUrl());

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(AndroidJSWebViewActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //                                        Toast.makeText(
                                        //                                                AndroidJSWebViewActivity.this,
                                        //                                                "fake message: i'll download...",
                                        //                                                Toast.LENGTH_LONG).show();
                                        //                                        System.out.println(arg0 + "==0==" + arg1 + "==1==" + arg2  + "--2--" +  arg3 + "==3==" + arg4);

                                        if (arg0 != null && (arg0.startsWith("http://") || arg0.startsWith("https://"))) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(arg0));
                                            startActivity(intent);
                                        }
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                AndroidJSWebViewActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                AndroidJSWebViewActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });


//        mWebView.setOnLongClickListener(v -> false);
        mWebView.setLongClickable(true);
//        mWebView.setOnTouchListener((v, event) -> false);



        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //		 webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setAllowContentAccess(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);

        webSetting.setGeolocationEnabled(true);
        webSetting.setGeolocationEnabled(true);

        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);

        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);


        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLoadsImagesAutomatically(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(mWebView, true);

        mWebView.setInitialScale(100);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);

        try {
            //下面方法去掉
            IX5WebViewExtension ix5 = mWebView.getX5WebViewExtension();
            if (null != ix5) {
                ix5.setScrollBarFadingEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mWebView.addJavascriptInterface(this, "android");
        //        mWebView.setDownloadListener(new DownloadListener() {
        //            @Override
        //            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
        //                // TODO: 2017-5-6 处理下载事件
        //                /*动态请求需要的权限*/
        //                boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(AndroidJSWebViewActivity.this, IDCardCamera.PERMISSION_CODE_FIRST,
        //                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        //                if (checkPermissionFirst) {
        //                    downloadApk(url);
        //                }
        //            }
        //        });

        //		String abc = "<html>" +
        //				"<head>" +
        //				"    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" +
        //				//            "    <meta name=\"viewport\"" +
        //				//          "    content=\"width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" />" +
        //				"    <title></title>" +
        //				"    <style>" +
        //				"          img{" +
        //				"            max-width: 96%;" +
        //				"        }" +
        //				"        .centerDiv{" +
        //				"            margin: 0 auto;" +
        //				"            max-width: 96%;" +
        //				"            text-align: center;" +
        //				"        }" +
        //				"        .centerDiv p {" +
        //				"            display: inline-block;" +
        //				"            margin: 10px 0 10px 0;" +
        //				"            text-align: left;" +
        //				"        }" +
        //				"    </style>" +
        //				"</head>" +
        //				"<body><div class='centerDiv'>" + mIntentUrl +
        //				"        " +
        //				"  </div>" +
        //				"</body>" +
        //				"</html>";


        //String html = "<style>img{height: 100%;max-width: 100%;}</style> <html><head></head><body><center>"+mIntentUrl+"</center></body></html>";
        //		mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //		mWebView.getSettings().setBuiltInZoomControls(true);
        //		mWebView.getSettings().setUseWideViewPort(true);
        //		mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.loadDataWithBaseURL( "" ,  html, "text/html", "UTF-8", "");


        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            //            System.out.println("------------------" + mHomeUrl);
            mWebView.loadUrl(mHomeUrl);
            //                        mWebView.loadUrl("http://192.168.1.12:8010/");

        } else {
            //            mWebView.loadUrl("hhttp://192.168.1.12:8010/#/");
            mWebView.loadData(Html.fromHtml(mIntentUrl).toString(), "text/html", "UTF-8");
            //            mWebView.loadDataWithBaseURL("", mIntentUrl, "text/html", "utf-8", "");

        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();





    }

    private void initBtnListenser() {


    }

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};


    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //            System.out.println("---------KEYCODE_BACK-----" + mWebView.copyBackForwardList().getCurrentIndex());
            if (mWebView != null) {
                if (mWebView.copyBackForwardList().getCurrentIndex() <= 0) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
                        MyDirectToast.show(this, MyDirectToast.text, getResources().getString(R.string.exis_app));
                        //                        Toast.makeText(this, getResources().getString(R.string.exis_app), Toast.LENGTH_LONG).show();
                        firstTime = secondTime;// 更新firstTime
                        return true;
                    } else {
                        /**
                         * 实现Home键会自动切换回桌面功能
                         */
                        ActivityManager.removeAllActivity();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);

                    }
                } else {
                    mWebView.goBack();
                    if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                        changGoForwardButton(mWebView);
                    return true;
                }

            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
    //                + ",resultCode:" + resultCode);
    //
    //        if (resultCode == RESULT_OK) {
    //            switch (requestCode) {
    //                case 0:
    //                    if (null != uploadFile) {
    //                        Uri result = data == null || resultCode != RESULT_OK ? null
    //                                : data.getData();
    //                        uploadFile.onReceiveValue(result);
    //                        uploadFile = null;
    //                    }
    //                    break;
    //                default:
    //                    break;
    //            }
    //        } else if (resultCode == RESULT_CANCELED) {
    //            if (null != uploadFile) {
    //                uploadFile.onReceiveValue(null);
    //                uploadFile = null;
    //            }
    //
    //        }
    //
    //    }
    //
    //    @Override
    //    protected void onNewIntent(Intent intent) {
    //        if (intent == null || mWebView == null || intent.getData() == null)
    //            return;
    //        mWebView.loadUrl(intent.getData().toString());
    //    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INSTALL_PERMISSION) {
            handleInstallPermissionReturn();
        } else if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
                MyDirectToast.show(AndroidJSWebViewActivity.this, MyDirectToast.waring, getResources().getString(R.string.ccqx_error));
            }
        } else if (requestCode == 2020 && resultCode == 2020) {
            setFinishToLogin();
        } else if (CHOOSE_PHOTO == 2) {//打开相册
            if (resultCode == RESULT_OK) {
                //判断手机的系统的版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Some Android versions/OEM settings pages do not reliably return a result code.
        if (waitingForInstallPermission) {
            handleInstallPermissionReturn();
        }
    }

    private void handleInstallPermissionReturn() {
        if (!waitingForInstallPermission) {
            dismissInstallPermissionDialog();
            return;
        }

        waitingForInstallPermission = false;
        dismissInstallPermissionDialog();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                || getPackageManager().canRequestPackageInstalls()) {
            DialogButtonVisible();
        }
    }

    private void dismissInstallPermissionDialog() {
        if (mydialog != null) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            mydialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        dismissInstallPermissionDialog();
        super.onDestroy();
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    public static final int     MSG_OPEN_TEST_URL = 0;
    public static final int     MSG_INIT_UI       = 1;
    private final       int     mUrlStartNum      = 0;
    private             int     mCurrentUrl       = mUrlStartNum;
    private             Handler mTestHandler      = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void releaceSource() {

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }


    /**
     * 得到当前应用版本名称的方法
     *
     * @param context :上下文
     * @throws Exception
     */
    public String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            Session.version = packInfo.versionName;
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 下载
     */
    public void downloadApk(String url) {
        new DownloadUtils(this, url, "googleKey.apk", getResources().getString(R.string.google_valication));
        MyDirectToast.show(this, MyDirectToast.success, getResources().getString(R.string.google_apk_uploading));
    }

    //分享单张图片
    public void shareSingleImage(String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //由文件得到uri
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.splash_app);
                Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(AndroidJSWebViewActivity.this.getContentResolver(), CodeUtils.createQRCode(url, 600, logo), null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.partner_share)));
            }
        });
    }

    private AppUpdateProgressDialog2 dialog      = null;
    private String                   versionname = "";
    private int                      fileSize;

    public void showUploadDialog(FindAppUpdateRep findAppUpdateRep) {
        //        System.out.println("-------------------------" + findAppUpdateRep);
        try {
            System.out.println("---------------findAppUpdateRep------------------------" + findAppUpdateRep);
            System.out.println(findAppUpdateRep);
            if (Double.valueOf(Session.version) < Double.valueOf(findAppUpdateRep.getVersion())) {


                versionname = findAppUpdateRep.getVersion();
                fileSize = findAppUpdateRep.getFileSize();

                dialog = new AppUpdateProgressDialog2(this, findAppUpdateRep);
                //正在下载时，不可关闭dialog
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            //                    Toast.makeText(HomeMainActivity.this, "正在下载请稍后", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPerssion(Button bt_next, LinearLayout llt_progress, String upload_url) {
        this.bt_next = bt_next;
        this.llt_progress = llt_progress;
        this.upload_url = upload_url;
        permisionIndex = "3";
        /*动态请求需要的权限*/
        boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, IDCardCamera.PERMISSION_CODE_FIRST,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        if (checkPermissionFirst) {
            //            DialogButtonVisible();

            setInstallPermission();
        }
    }

    public void setApkBrowserDownLoad(String upload_url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(upload_url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开浏览器", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 更新下载apk
     */
    public void DialogButtonVisible() {
        bt_next.setVisibility(View.GONE);
        llt_progress.setVisibility(View.VISIBLE);
        new Thread(new DownloadApk2(AndroidJSWebViewActivity.this, dialog, upload_url, versionname, fileSize, myHandler)).start();
    }

    /**
     * 8.0以上系统设置安装未知来源权限
     */
    public void setInstallPermission() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //弹框提示用户手动打开
                showInstallPermissionDialog();
                return;
            }
            DialogButtonVisible();
        } else {
            DialogButtonVisible();
        }
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    int progress = (int) msg.obj;
                    dialog.setProgress(progress);
                    if (100 == progress) {
                        //                        dialog.dismiss();
                        //                        Toast.makeText(HomeMainActivity.this,"下载完成，跳转到安装界面",Toast.LENGTH_SHORT).show();
                        dialog.setButton();
                    }
                    break;
                case 101:
                    MyDirectToast.show(AndroidJSWebViewActivity.this, MyDirectToast.waring, getResources().getString(R.string.network_exception));
                    dialog.setButton();
                    break;
                case 200:
                    installAPK(new File(msg.obj.toString()));
                    break;
                case 1002:
                    MyDirectToast.show(AndroidJSWebViewActivity.this, MyDirectToast.waring, getResources().getString(R.string.uploading_network_text));
                    dialog.setButton2();
                    break;
            }
        }
    };

    /**
     * 对话框
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void showInstallPermissionDialog() {

        mydialog = new Dialog(this, R.style.MyDialog);
        // 设置它的ContentView

        mydialog.setContentView(R.layout.dialog_install_permissions);

        Button confirm = (Button) mydialog.findViewById(R.id.confirm);
        mydialog.setCanceledOnTouchOutside(false);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此方法需要API>=26才能使用
                dismissInstallPermissionDialog();
                toInstallPermissionSettingIntent();

            }
        });

        if (!mydialog.isShowing()) {
            mydialog.show();
        }

    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        waitingForInstallPermission = true;
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, REQUEST_INSTALL_PERMISSION);
    }

    /**
     * 安装Apk
     */
    public void installAPK(File apkFile) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //      安装完成后，启动app（源码中少了这句话）

        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                //                if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                startActivity(intent);
                //                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * js中调用的android方法
     */
    @JavascriptInterface
    public void setCamera(String token) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Session.token = token;
                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.token, token);

                intent = new Intent(AndroidJSWebViewActivity.this, AuthenticationPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("channel", "0");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    @JavascriptInterface
    public void setFinishToLogin() {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:setLogin()"); //告诉js是Android设备
            }
        });
    }

    /**
     * js中调用的android方法
     */
    @JavascriptInterface
    public void setCameraReview(String token, String familyName, String idNumber, String
            idFrontImg, String idBackImg, String idHandheldImg, String mail, String channel) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                Session.token = token;
                //                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.token, token);
                //                Bundle bundle = new Bundle();
                //                bundle.putString("familyName", familyName);
                //                bundle.putString("idNumber", idNumber);
                //                bundle.putString("idFrontImg", idFrontImg);
                //                bundle.putString("idBackImg", idBackImg);
                //                bundle.putString("idHandheldImg", idHandheldImg);
                //                intent = new Intent(AndroidJSWebViewActivity.this, AuthenticationActivity.class);
                //                intent.putExtra("bundle", bundle);
                //                startActivity(intent);

                Session.token = token;
                Session.mail = mail;
                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.token, token);
                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.mail, mail);
                intent = new Intent(AndroidJSWebViewActivity.this, AuthenticationPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("familyName", familyName);
                bundle.putString("idNumber", idNumber);
                bundle.putString("idFrontImg", idFrontImg);
                bundle.putString("idBackImg", idBackImg);
                bundle.putString("idHandheldImg", idHandheldImg);
                bundle.putString("channel", channel);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });
    }

    /**
     * js中调用的android方法
     */
    @JavascriptInterface
    public void appSource(String url) {
        permisionIndex = "1";
        google_apk_url = url;
        boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, IDCardCamera.PERMISSION_CODE_FIRST,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        if (checkPermissionFirst) {
            downloadApk(url);
        }

        //        downloadApk("https://cloud-store-one.oss-cn-hongkong.aliyuncs.com/apk/Google_Authenticator_v5.10.apk");
    }

    @JavascriptInterface
    public void setShare(String url) {
        //        System.out.println("-------------------" + url);
        permisionIndex = "2";
        partner_yqm_url = url;
        /*动态请求需要的权限*/
        boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, IDCardCamera.PERMISSION_CODE_FIRST,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        if (checkPermissionFirst) {
            shareSingleImage(partner_yqm_url);
        }
    }


    @JavascriptInterface
    public void goPromote(String headImg, String invitationCode, String nickName, String
            token, String mail) {
        Intent intent = new Intent(AndroidJSWebViewActivity.this, Promote.class);
        ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.token, token);
        ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.mail, mail);
        Session.token = token;
        Session.mail = mail;
        Bundle bundle = new Bundle();
        bundle.putString("headImg", headImg);
        bundle.putString("invitationCode", invitationCode);
        bundle.putString("nickName", nickName);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, 2020);

    }

    @JavascriptInterface
    public void openBrowser(String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open browser", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理请求权限的响应
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 请求权限结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissions = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isPermissions = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) { //用户选择了"不再询问"
                    if (isToast) {
                        //                        Toast.makeText(this, getResources().getString(R.string.authentication_please_qx), Toast.LENGTH_SHORT).show();
                        MyDirectToast.show(this, MyDirectToast.success, getResources().getString(R.string.authentication_please_qx));
                        isToast = false;
                    }
                }
            }
        }
        isToast = true;
        if (isPermissions) {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "允许所有权限");
            if ("1".equals(permisionIndex)) {
                downloadApk(google_apk_url);
            } else if ("2".equals(permisionIndex)) {
                shareSingleImage(partner_yqm_url);
            } else if ("3".equals(permisionIndex)) {
                setInstallPermission();
            } else if ("10".equals(permisionIndex)) {
                openAlbum();
            }
        } else {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "有权限不允许");
            //            finish();
        }
    }

    /**
     * 1: 白色  white
     * 2：挖矿黑色  color_010D25
     * 3: 灰色 color_f2f2f2
     * 4：蓝色 color_1E86FC
     * 5: 深蓝色 color_1d4793
     * 6: 褐色  color_0B1D2E
     * 7: 淡灰色 color_f0f0f0
     * 8: 淡蓝色 color_388FFC
     *
     * @param toobarColor
     */
    @JavascriptInterface
    public void setHtmlToobarColor(int toobarColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (1 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.white);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, true, false);
                } else if (2 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_010D25);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, false, false);
                } else if (3 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_f2f2f2);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, true, false);
                } else if (4 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_1E86FC);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, false, false);
                } else if (5 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_1d4793);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, false, false);
                } else if (6 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_0B1D2E);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, false, false);
                } else if (7 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_f0f0f0);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, true, false);
                } else if (8 == toobarColor) {
                    StatusBarUtil.setStatusBarColor(AndroidJSWebViewActivity.this, R.color.color_388FFC);
                    StatusBarUtil.setLightStatusBar(AndroidJSWebViewActivity.this, false, false);
                }


            }
        });

    }


    @JavascriptInterface
    public void getPicture() {
        //        initCamera();

        permisionIndex = "10";
        //申请一个运行时权限处理
        //权限WRITE_EXTERNAL_STORAGE表示同时授予程序对SD卡读和写的能力。
        if (ContextCompat.checkSelfPermission(AndroidJSWebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AndroidJSWebViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {//授予了权限后，则调用openAlbum方法来获取图片
            openAlbum();
        }
    }

    @JavascriptInterface
    public void onX5ButtonClicked() {
        AndroidJSWebViewActivity.this.enableX5FullscreenFunc();
    }

    @JavascriptInterface
    public void onCustomButtonClicked() {
        AndroidJSWebViewActivity.this.disableX5FullscreenFunc();
    }

    @JavascriptInterface
    public void onLiteWndButtonClicked() {
        AndroidJSWebViewActivity.this.enableLiteWndFunc();
    }

    @JavascriptInterface
    public void onPageVideoClicked() {
        AndroidJSWebViewActivity.this.enablePageVideoFunc();
    }


    @JavascriptInterface
    public void setAndroidEnvironment(int environment) {
        getUrl(environment);
    }

    @JavascriptInterface
    public void setCleanIndex() {
        cleanHtmlIndex = 2;
    }

    /**
     * 设置多语言
     *
     * @param language
     */
    @JavascriptInterface
    public void setAndroidLanguage(String language) {
        Session.LANGUAGE = language;
        ClientConfigDao.SetConfigString(this, Constants.LANGUAGE, Session.LANGUAGE);
        if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.TRADITIONAL_CHINESE);
        } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.US);
        }

    }

    /**
     * channel: 1 代表google解绑
     */
    @JavascriptInterface
    public void unbindGoogle(String token, String mail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Session.token = token;
                Session.mail = mail;
                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.token, token);
                ClientConfigDao.SetConfigString(AndroidJSWebViewActivity.this, Constants.mail, mail);
                intent = new Intent(AndroidJSWebViewActivity.this, AuthenticationPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("channel", "1");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 指纹识别对话框出现
     * <p>
     * 传1 代表指纹登录
     * 传2 代表购买支付
     * 传3 代表立即挖矿
     * 传4 代表转让
     * 传5 代表提前还款
     * 传6 代表defi理财购买
     */
    @JavascriptInterface
    public void getFingerRequire(String obj) {

        //        StringBuilder stringBuilder = new StringBuilder();
        //        stringBuilder.append("SDK version is "+ Build.VERSION.SDK_INT);
        //        stringBuilder.append("\n");
        //        stringBuilder.append("isHardwareDetected : "+mManager.isHardwareDetected());
        //        stringBuilder.append("\n");
        //        stringBuilder.append("hasEnrolledFingerprints : "+mManager.hasEnrolledFingerprints());
        //        stringBuilder.append("\n");
        //        stringBuilder.append("isKeyguardSecure : "+mManager.isKeyguardSecure());
        //        stringBuilder.append("\n");
        try {
            mManager = BiometricPromptManager.from(this);
            if (mManager.isHardwareDetected()) {
                if (!mManager.hasEnrolledFingerprints() || !mManager.isKeyguardSecure()) {
                    MyDirectToast.show(AndroidJSWebViewActivity.this, MyDirectToast.waring, getResources().getString(R.string.openFingerPrint));
                } else {
                    if (mManager.isBiometricPromptEnable()) {
                        mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                            @Override
                            public void onUsePassword() {
                                //                                Toast.makeText(AndroidJSWebViewActivity.this, "onUsePassword", Toast.LENGTH_SHORT).show();
                                if ("2".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword()");
                                } else if ("3".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword()");
                                } else if ("4".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword2()");
                                } else if ("5".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword()");
                                } else if ("6".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword()");
                                } else if ("7".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword3()");
                                } else if ("8".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword4()");
                                } else if ("9".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSUseFingerAndPassword5()");
                                }
                            }

                            @Override
                            public void onSucceeded() {
                                LoginFingerReq loginFingerReq = new LoginFingerReq();
                                loginFingerReq.setImei("" + Session.IMEI);
                                loginFingerReq.setFingerId("" + fingerId);
                                loginFingerReq.setDeviceNumber("" + Session.IMEI);
                                loginFingerReq.setAppInfo("" + getVersionName(AndroidJSWebViewActivity.this));
                                loginFingerReq.setMobileInfo(Build.BRAND + " " + Build.MODEL);
                                loginFingerReq.setSystemInfo(Build.VERSION.RELEASE);
                                String jsonData = JsonUtils.toJsonStr(loginFingerReq);

                                if ("4".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSLoginFingerJson2('" + jsonData + "')");
                                } else if ("7".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSLoginFingerJson3('" + jsonData + "')");
                                } else if ("8".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSLoginFingerJson4('" + jsonData + "')");
                                } else if ("9".equals(obj)) {
                                    mWebView.loadUrl("javascript:AndroidToJSLoginFingerJson5('" + jsonData + "')");
                                } else {
                                    mWebView.loadUrl("javascript:AndroidToJSLoginFingerJson('" + jsonData + "')");
                                }
                            }

                            @Override
                            public void onFailed() {

                                //                                Toast.makeText(AndroidJSWebViewActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(int code, String reason) {

                                //                                Toast.makeText(AndroidJSWebViewActivity.this, "onError", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {

                                //                                Toast.makeText(AndroidJSWebViewActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyDirectToast.show(AndroidJSWebViewActivity.this, MyDirectToast.waring, getResources().getString(R.string.openFingerPrint));

        }
    }

    /**
     * 判断指纹识别是否可以开启
     */
    @JavascriptInterface
    public void getFingerState() {
        mManager = BiometricPromptManager.from(this);
        FingerStateReq fingerStateReq = new FingerStateReq();
        fingerStateReq.setHardwareDetected("" + mManager.isHardwareDetected());
        fingerStateReq.setHasEnrolledFingerprints("" + mManager.hasEnrolledFingerprints());
        fingerStateReq.setIsKeyguardSecure("" + mManager.isKeyguardSecure());
        fingerStateReq.setImei("" + Session.IMEI);
        fingerStateReq.setFingerId("" + fingerId);
        fingerStateReq.setImei("" + Session.IMEI);
        //        fingerStateReq.setFingerId("" + fingerId);
        fingerStateReq.setDeviceNumber("" + Session.IMEI);
        fingerStateReq.setAppInfo("" + getVersionName(AndroidJSWebViewActivity.this));
        fingerStateReq.setMobileInfo(Build.BRAND + " " + Build.MODEL);
        fingerStateReq.setSystemInfo(Build.VERSION.RELEASE);
        String jsonData = JsonUtils.toJsonStr(fingerStateReq);
        //        System.out.println(jsonData + "----------------jsonData---------------------");

        //        mWebView.loadUrl("javascript:AndroidToJSJson('" + jsonData + "')"); //告诉js是Android设备
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:AndroidToJSJson('" + jsonData + "')");
            }
        });

    }


    @JavascriptInterface
    public void showMsg(String basePicture, String fileName) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:showMsg('" + basePicture + "', '" + fileName + "')"); //告诉js是Android设备
            }
        });
    }

    /**
     * 获取指纹id
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getFingerprintInfo() {
        try {
            @SuppressLint("WrongConstant") FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            Method method = FingerprintManager.class.getDeclaredMethod("getEnrolledFingerprints");
            Object obj = method.invoke(fingerprintManager);
            if (obj != null) {
                Class<?> clazz = Class.forName("android.hardware.fingerprint.Fingerprint");
                Method getFingerId = clazz.getDeclaredMethod("getFingerId");
                for (int i = 0; i < ((List) obj).size(); i++) {
                    Object item = ((List) obj).get(i);
                    if (null == item) {
                        continue;
                    }
                    if (i == ((List) obj).size() - 1) {
                        fingerId += getFingerId.invoke(item);
                    } else {
                        fingerId += getFingerId.invoke(item) + ",";
                    }
                    //                                        Log.d("qvc", "fingerId: " + getFingerId.invoke(item));
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //    @JavascriptInterface
    //    public void getIsFullScreen(String isFullScreen){
    //        System.out.println("-------------------------------" + isFullScreen);
    //        if ("0".equals(isFullScreen)){//设置为非全屏
    ////            WindowManager.LayoutParams lp = getWindow().getAttributes();
    ////            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
    ////            getWindow().setAttributes(lp);
    ////            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    //            AndroidJSWebViewActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //
    //
    //        }else{//设置为全屏
    ////            WindowManager.LayoutParams lp = getWindow().getAttributes();
    ////            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
    ////            getWindow().setAttributes(lp);
    ////            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    //            AndroidJSWebViewActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    //
    //        }
    //    }

    /**
     *
     */
    @JavascriptInterface
    public void setAndroidDownload() {
        try {
            mHomeService.findAppUpdate2(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取T的网络地址
     */
    public void getUrl(int environment) {
        RetrofitUtil.sInstance = null;
        if (2 == environment) {
            AppUrl.PRODUCTION_ENVIRONMENT = 2;
        } else if (0 == environment) {
            AppUrl.PRODUCTION_ENVIRONMENT = 0;
        } else {
            AppUrl.PRODUCTION_ENVIRONMENT = 1;
        }

    }

    public void initCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //确保有相机来处理Intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                File photoFile = saveFileName();

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "app.gkai.ai.fileProvider", photoFile);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, PictureSelector.SELECT_REQUEST_CODE);
                }
            }

        } else {
            PictureSelector
                    .create(this, PictureSelector.SELECT_REQUEST_CODE);
            //                .selectPicture(true);
            selectPicture(true, 0, 0, 0, 0);
        }
    }

    String currentPath = null;

    private File saveFileName() {
        //
        File newFolder = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String name = format.format(date) + ".jpg";

        File ji = null;
        try {
            ji = new File(newFolder + "/" + name);
            ji.createNewFile();
            currentPath = ji.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ji;
    }

    /**
     * 选择图片（指定宽高及宽高比例裁剪）
     *
     * @param cropEnabled 是否裁剪
     * @param cropWidth   裁剪宽
     * @param cropHeight  裁剪高
     * @param ratioWidth  宽比例
     * @param ratioHeight 高比例
     */
    public void selectPicture(boolean cropEnabled, int cropWidth, int cropHeight,
                              int ratioWidth, int ratioHeight) {
        //        Activity activity = new WeakReference(this);
        //        Fragment fragment = this.mFragment.get();
        Intent intent = new Intent(this, app.gkai.ai.core.common.view.PictureSelectActivity.class);
        intent.putExtra(PictureSelectActivity.ENABLE_CROP, cropEnabled);
        intent.putExtra(PictureSelectActivity.CROP_WIDTH, cropWidth);
        intent.putExtra(PictureSelectActivity.CROP_HEIGHT, cropHeight);
        intent.putExtra(PictureSelectActivity.RATIO_WIDTH, ratioWidth);
        intent.putExtra(PictureSelectActivity.RATIO_HEIGHT, ratioHeight);
        //        if (fragment != null) {
        //            fragment.startActivityForResult(intent, mRequestCode);
        //        } else {
        this.startActivityForResult(intent, PictureSelector.SELECT_REQUEST_CODE);
        //        }
    }


    //定义openAlbum()方法来获取图片
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");//构建一个intent对象，并将它的action指定
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册程序，选择照片
        //给第二个参数传入的值变成了CHOOSE_PHOTO
        // 这样当从相册选择完图片回到onActivityResult() 方法时
        // 就会进入CHOOSE_PHOTO 的case 来处理图片。
    }

    //因为Android系统从4.4版本开始，选取相册中的图片不再返回图片真实的Uri了，而是一个封装过的Uri
    // 因此如果是4.4版本以上的手机就需要对这个Uri进行解析才行。
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {//用于解析Android4.4版本以上的封装过的Uri
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //如果Uri的authority是media格式的话，document id 还需要再进行一次解析
                //要通过字符串分割的方式取出后半部分才能得到真正的数字id
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        displayImage(imagePath); // 根据图片路径显示图片
    }

    //获取到图片的真实路径了
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //将图片显示到界面上
    private void displayImage(String imagePath) {
        //        if (imagePath != null) {
        //            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //            picture.setImageBitmap(bitmap);
        //        } else {
        //            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        //        }

        //        System.out.println(convertImageToBase64(imagePath));
        //        mWebView.postDelayed(() -> {
        //            mWebView.evaluateJavascript("javascript:getBase64(" + convertImageToBase64(imagePath) +  ")", null);
        //        }, 1000);
        String fileName = new File(imagePath).getName();
        showMsg(convertImageToBase64(imagePath), fileName);
    }

    public static String convertImageToBase64(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return convertBitmapToBase64(bitmap);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}
