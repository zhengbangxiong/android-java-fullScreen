package app.gkai.ai.modules.mine.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wildma.idcardcamera.camera.IDCardCamera;
import com.wildma.idcardcamera.utils.PermissionUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelectActivity;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import app.gkai.ai.R;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.util.BitmapLoader;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.mine.service.AliyunOss;
import app.gkai.ai.modules.mine.service.MemberAuthService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @title 上传身份证
 */
public class AuthenticationPictureActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AuthenticationPictureActivity";

    @BindView(R.id.llt_left)
    LinearLayout llt_left;
    @BindView(R.id.tv_title)
    TextView     tv_title;
    @BindView(R.id.rt_bg_one)
    LinearLayout rt_bg_one;
    @BindView(R.id.img_front)
    ImageView    img_front;
    @BindView(R.id.rt_bg_two)
    LinearLayout rt_bg_two;
    @BindView(R.id.img_cont)
    ImageView    img_cont;
    @BindView(R.id.rt_bg_three)
    LinearLayout rt_bg_three;
    @BindView(R.id.img_sc)
    ImageView    img_sc;
    @BindView(R.id.bt_next)
    Button       bt_next;

    private boolean isToast = true;//是否弹吐司，为了保证for循环只弹一次
    private File    file;
    private Bundle  bundle;

    /**
     * 本地保存压缩path
     */
    private String sc_path    = "";
    private String front_path = "";
    private String cont_path  = "";

    /**
     * 阿里云返回path
     */
    private String idFrontImg    = "";
    private String idBackImg     = "";
    private String idHandheldImg = "";

    private String familyName = "";
    private String idNumber   = "";

    private MemberAuthService mMemberAuthService = new MemberAuthService();

    private WeakReference<AuthenticationPictureActivity> mActivity = new WeakReference(this);
    private AliyunOss                                    service   = new AliyunOss();

    private final int REQUEST_CODE = 10001; //权限

    private int camera_index = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_authentication_two);
        setStatusBar(R.color.color_1E86FC, false);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);

        initValue();

        //        Build.VERSION_CODES.M
        //        Build.VERSION_CODES.R;

    }

    public void initValue() {
        bundle = getIntent().getBundleExtra("bundle");
        if ("1".equals(bundle.getString("channel"))) {
            tv_title.setText(getResources().getString(R.string.unbindGoogle));
        } else {
            tv_title.setText(getResources().getString(R.string.authentication_upload_idnumber));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
        }


        try {

            if (null != bundle.getString("familyName")) {

                familyName = bundle.getString("familyName");
                idNumber = bundle.getString("idNumber");

                idFrontImg = bundle.getString("idFrontImg");
                idBackImg = bundle.getString("idBackImg");
                idHandheldImg = bundle.getString("idHandheldImg");

                Glide.with(this).load(idFrontImg).into(img_front);
                Glide.with(this).load(idBackImg).into(img_cont);
                Glide.with(this).load(idHandheldImg).into(img_sc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void releaceSource() {

    }

    @OnClick({R.id.llt_left, R.id.rt_bg_one, R.id.rt_bg_two, R.id.rt_bg_three, R.id.bt_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llt_left:
                this.finish();
                break;
            case R.id.rt_bg_one:
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
            case R.id.rt_bg_two:
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                break;
            case R.id.rt_bg_three:
                camera_index = 1;
                /*动态请求需要的权限*/
                boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, IDCardCamera.PERMISSION_CODE_FIRST,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
                if (checkPermissionFirst) {
                    initCamera();

                }
                break;
            case R.id.bt_next:
                if (StringUtils.isEmpty(idFrontImg)) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.authentication_idnumber_front));
                    return;
                } else if (StringUtils.isEmpty(idBackImg)) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.authentication_idnumber_cont));
                    return;
                } else if (StringUtils.isEmpty(idHandheldImg)) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.authentication_idnumber_sc));
                    return;
                }

                bundle.putString("idFrontImg", idFrontImg);
                bundle.putString("idBackImg", idBackImg);
                bundle.putString("idHandheldImg", idHandheldImg);

                Intent intent = new Intent(this, AuthenticationActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);


                break;
        }
    }

    public void initCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //确保有相机来处理Intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                System.out.println("--------photoFile1-------");

                File photoFile = saveFileName();
                System.out.println("--------photoFile-------" + photoFile.getName());
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
    public void selectPicture(boolean cropEnabled, int cropWidth, int cropHeight, int ratioWidth, int ratioHeight) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                showDialog();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap bitmap = BitmapLoader.getBitmapFromFile(currentPath, 1080, 1080);
                    sc_path = BitmapLoader.saveBitmapToLocal(bitmap, "" + System.currentTimeMillis());
                } else {
                    PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                    Bitmap bitmap = BitmapLoader.getBitmapFromFile(pictureBean.getPath(), 1080, 1080);
                    sc_path = BitmapLoader.saveBitmapToLocal(bitmap, "" + System.currentTimeMillis());
                }

                service.aliOssPolicy(AuthenticationPictureActivity.this, PictureSelector.SELECT_REQUEST_CODE, new File(sc_path));

            } else {
                //                System.out.println("-------currentPath---------" + currentPath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap bitmap = BitmapLoader.getBitmapFromFile(currentPath, 1080, 1080);
                    sc_path = BitmapLoader.saveBitmapToLocal(bitmap, "" + System.currentTimeMillis());
                    if(!StringUtils.isEmpty(sc_path)) {
                        service.aliOssPolicy(AuthenticationPictureActivity.this, PictureSelector.SELECT_REQUEST_CODE, new File(sc_path));
                    }else{
                        MyDirectToast.show(AuthenticationPictureActivity.this, MyDirectToast.waring, getResources().getString(R.string.picture_address_null));
                    }
                }

            }
        }


        if (resultCode == IDCardCamera.RESULT_CODE) {
            showDialog();
            //获取图片路径，显示图片
            String imagePath = IDCardCamera.getImagePath(data);
            Bitmap bitmap = BitmapLoader.getBitmapFromFile(imagePath, 720, 720);
            String path = BitmapLoader.saveBitmapToLocal(bitmap, "" + System.currentTimeMillis());
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    //                    img_front.setImageBitmap(BitmapFactory.decodeFile(path));
                    front_path = path;
                    service.aliOssPolicy(AuthenticationPictureActivity.this, IDCardCamera.TYPE_IDCARD_FRONT, new File(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    //                    img_cont.setImageBitmap(BitmapFactory.decodeFile(path));
                    cont_path = path;
                    service.aliOssPolicy(AuthenticationPictureActivity.this, IDCardCamera.TYPE_IDCARD_BACK, new File(path));
                }


            }
        }

        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
                MyDirectToast.show(AuthenticationPictureActivity.this, MyDirectToast.waring, getResources().getString(R.string.ccqx_error));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 图片上传返回阿里云path
     *
     * @param path
     * @param resultCode
     */
    public void getPath(String path, int resultCode) {
        /*结果回调*/
        if (resultCode == PictureSelector.SELECT_REQUEST_CODE) {
            //            img_sc.setImageBitmap(BitmapFactory.decodeFile(sc_path));

            idHandheldImg = path;
            mSafeHandler.obtainMessage(0, sc_path).sendToTarget();

        } else if (resultCode == IDCardCamera.TYPE_IDCARD_FRONT) {
            service.AliyunOssPath2(mActivity.get(), new File(front_path), path);

        } else if (resultCode == IDCardCamera.TYPE_IDCARD_BACK) {
            mSafeHandler.obtainMessage(2, cont_path).sendToTarget();
            idBackImg = path;
        }

    }

    public void getOCRResult(String ocrResult, String path) {
//        {"status":200,"statusCode":"SUCCESS","message":"成功","data":{"birthDate":"19690303","gender":"男",
//                "address":"吉林省延吉市公园街园辉委五组","nationality":"朝鲜","name":"李基福","IDNumber":"222401196903034219",
//                "idnumber":"222401196903034219"},"success":true,"fail":false}

//        familyName = bundle.getString("familyName");
//        idNumber = bundle.getString("idNumber");
        try {
            JSONObject object = new JSONObject(ocrResult);
            JSONObject jsonObject = new JSONObject(object.getString("data"));
            bundle.putString("familyName", jsonObject.getString("name"));
            bundle.putString("idNumber", jsonObject.getString("IDNumber"));
        }catch (Exception e){
            e.printStackTrace();
        }

        mSafeHandler.obtainMessage(1, front_path).sendToTarget();
        idFrontImg = path;
    }

    /**
     * 图片上传失败
     */
    public void getPictureError() {
        mSafeHandler.obtainMessage(4, "").sendToTarget();
    }


    private SafeHandler mSafeHandler = new SafeHandler(AuthenticationPictureActivity.this);

    /**
     * 根据提示实现的Handler
     */
    private static class SafeHandler extends Handler {

        private WeakReference<AuthenticationPictureActivity> mWeakReference;

        public SafeHandler(AuthenticationPictureActivity activity) {
            super();
            mWeakReference = new WeakReference<AuthenticationPictureActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            AuthenticationPictureActivity activity = mWeakReference.get();
            if (activity != null) {
                activity.dismissDialog();
                switch (msg.what) {
                    case 0:
                        Glide.with(activity).load(msg.obj.toString()).into(activity.img_sc);
                        break;
                    case 1:
                        Glide.with(activity).load(msg.obj.toString()).into(activity.img_front);

                        break;
                    case 2:
                        Glide.with(activity).load(msg.obj.toString()).into(activity.img_cont);
                        break;
                    case 4:
                        MyDirectToast.show(activity, MyDirectToast.waring, activity.getResources().getString(R.string.picture_upload_error));
                        break;
                }
            }
        }
    }

    public void showDialog() {
        showLoadingDialog(getResources().getString(R.string.loading));
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
            if (camera_index == 1) {
                initCamera();
            }
        } else {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "有权限不允许");
            //            finish();
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

}
