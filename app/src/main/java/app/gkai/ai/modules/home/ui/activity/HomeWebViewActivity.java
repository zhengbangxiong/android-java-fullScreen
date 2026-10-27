package app.gkai.ai.modules.home.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.R;
import app.gkai.ai.core.common.view.AlwaysMarqueeTextView;

public class HomeWebViewActivity extends BaseActivity {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;

    private static final String  mHomeUrl      = "http://app.html5.qq.com/navi/index";
    private static final String  TAG           = "SdkDemo";
    private static final int     MAX_LENGTH    = 14;
    private              boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable  = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private String mIntentUrl;

    private LinearLayout          llt_left;
    private AlwaysMarqueeTextView tv_title;
    private String                title = "";


    @Override
    protected void initView() {
        setStatusBar(R.color.white, true);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

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

        /*
         * getWindow().addFlags(
         * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */
        setContentView(R.layout.activity_webview2);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        llt_left = (LinearLayout) findViewById(R.id.llt_left);
        tv_title = (AlwaysMarqueeTextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        try {
            if (15 > title.length()) {
                tv_title.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //		tv_title.setSelected(true);
        llt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeWebViewActivity.this.finish();
            }
        });


        initBtnListenser();

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

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

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
                /* mWebView.showLog("test Log"); */
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            // /////////////////////////////////////////////////////////
            //

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(com.example.test_webview_demo.R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
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
                new AlertDialog.Builder(HomeWebViewActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                HomeWebViewActivity.this,
                                                "fake message: i'll download...",
                                                Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                HomeWebViewActivity.this,
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
                                                HomeWebViewActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

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
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        mWebView.setInitialScale(100);


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
            mWebView.loadUrl(mHomeUrl);
        } else {
            //			mWebView.loadUrl(mIntentUrl.toString());
            //			mWebView.loadData(Html.fromHtml(mIntentUrl).toString(), "text/html", "UTF-8");
            mWebView.loadDataWithBaseURL("", mIntentUrl, "text/html", "utf-8", "");

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(mWebView);
                return true;
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
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
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
}
