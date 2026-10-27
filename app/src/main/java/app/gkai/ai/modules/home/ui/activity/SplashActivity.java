package app.gkai.ai.modules.home.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import app.gkai.ai.core.common.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 直接跳转主界面
        startActivity(new Intent(SplashActivity.this, AndroidJSWebViewActivity.class));

        // 结束自己
        finish();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void releaceSource() {

    }
}
