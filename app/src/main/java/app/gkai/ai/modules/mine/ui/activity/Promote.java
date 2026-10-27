package app.gkai.ai.modules.mine.ui.activity;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.zxing.util.CodeUtils;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.StringUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;
import com.wildma.idcardcamera.utils.PermissionUtils;
import app.gkai.ai.R;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.home.service.HomeService;

import java.lang.ref.WeakReference;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @title 邀请注册
 */
public class Promote extends BaseActivity implements View.OnClickListener {

    private final String TAG = "Promote";

    @BindView(R.id.llt_left)
    LinearLayout llt_left;
    @BindView(R.id.tv_title)
    TextView     tv_title;
    @BindView(R.id.bt_next)
    Button       bt_next;
    @BindView(R.id.tv_copy)
    TextView     tv_copy;
    @BindView(R.id.tv_yqm)
    TextView     tv_yqm;
    @BindView(R.id.tv_mail)
    TextView     tv_mail;
    @BindView(R.id.img_head)
    ImageView    img_head;
    @BindView(R.id.img_ewm)
    ImageView    img_ewm;

    private HomeService                           mHomeService = new HomeService();
    private WeakReference<AuthenticationActivity> mActivity    = new WeakReference(this);

    private boolean isToast = true;//是否弹吐司，为了保证for循环只弹一次
    private Bundle  bundle;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_promote);
        setStatusBar(R.color.white, true);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);

        initValue();

        showDialog();
        mHomeService.friendUrl(this);

    }

    public void initValue() {
        tv_title.setText(getResources().getString(R.string.invite_frient));
        bundle = getIntent().getBundleExtra("bundle");

        try {
            tv_yqm.setText(bundle.getString("invitationCode"));

            if (!StringUtils.isEmpty(bundle.getString("headImg"))) {
                StringUtils.SetImageHead(bundle.getString("headImg"), img_head, this);
            }

            Session.nickName = bundle.getString("nickName");
            if (!StringUtils.isEmpty(Session.nickName)) {
                tv_mail.setText(Session.nickName);
            } else {
                tv_mail.setText(Session.mail.substring(0, Session.mail.indexOf("@")));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        showLoadingDialog(getResources().getString(R.string.loading));
    }

    private void catchScreen() {
        // 1. 初始化布局：
        View decorView = findViewById(R.id.llt_center);
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        //        decorView.measure(View.MeasureSpec.makeMeasureSpec(decorView.getWidth(), View.MeasureSpec.EXACTLY),
        //                View.MeasureSpec.makeMeasureSpec(decorView.getHeight(), View.MeasureSpec.EXACTLY));
        //        decorView.layout((int) decorView.getX(), (int) decorView.getY(), (int) decorView.getX() + decorView.getMeasuredWidth(), (int) decorView.getY() + decorView.getMeasuredHeight());
        //        createPicture(decorView);
        shareSingleImage(viewConversionBitmap(decorView));
    }

    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    //分享单张图片
    public void shareSingleImage(Bitmap bitmap) {
        //由文件得到uri
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.partner_share)));
    }

    /**
     * 获取推荐URL,并且跳转到推广界面
     *
     * @param url
     */
    public void getFriendUrl(String url) {
        try {

            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.splash_app);
            //生成二维码
            img_ewm.setImageBitmap(CodeUtils.createQRCode(url, 600, logo));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFinishToLogin(){
        setResult(2020);
        this.finish();
    }

    @Override
    protected void releaceSource() {

    }

    @OnClick({R.id.llt_left, R.id.bt_next, R.id.tv_copy})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llt_left:
                this.finish();
                break;
            case R.id.bt_next:
                /*动态请求需要的权限*/
                boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, IDCardCamera.PERMISSION_CODE_FIRST,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                if (checkPermissionFirst) {
                    catchScreen();
                }
                break;
            case R.id.tv_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tv_yqm.getText().toString());
                MyDirectToast.show(this, MyDirectToast.success, getResources().getString(R.string.copy_success));
                break;
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Session.LANGUAGE = ClientConfigDao.GetConfigString(this, Constants.LANGUAGE, "");
        if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.TRADITIONAL_CHINESE);
        } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
            changeAppLanguage(Locale.US);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
            catchScreen();
        } else {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "有权限不允许");
            //            finish();
        }
    }

}
