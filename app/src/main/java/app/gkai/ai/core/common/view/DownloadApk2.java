package app.gkai.ai.core.common.view;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import app.gkai.ai.modules.home.ui.activity.AndroidJSWebViewActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 访问网络下载apk
 */

public class DownloadApk2 implements Runnable {

    private AppUpdateProgressDialog2 dialog;

    InputStream is;

    FileOutputStream fos;
    private String                   TAG;
    private AndroidJSWebViewActivity mContext;
    private Handler                  myHandler;
    private String           upload_url;
    private String versionname;

    private int progressNetwork = 0;
    private long contentLengthNetwork = 0;

    private int fileSize;

    public DownloadApk2(AndroidJSWebViewActivity context, AppUpdateProgressDialog2 dialog, String upload_url, String versionname, int fileSize, Handler myHandler) {
            this.mContext = context;
        this.dialog = dialog;
        this.myHandler = myHandler;
        this.upload_url = upload_url;
        this.versionname = versionname;
        this.fileSize = fileSize;
    }

    @Override
    public void run() {

        OkHttpClient client = new OkHttpClient();
        File apkFile = getApkFile();

//        String url = "https://cloud-store-one.oss-cn-hongkong.aliyuncs.com/apk/Google_Authenticator_v5.10.apk";

        Request request = new Request.Builder().get().url(upload_url).build();

        try {

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                Log.d(TAG, "开始下载apk");

                //获取内容总长度

                long contentLength = response.body().contentLength();
                contentLengthNetwork = contentLength;


                //设置最大值
//                dialog.setMax(100);

                if (apkFile.exists()){
                    if(contentLength > 0 && contentLength == apkFile.length()){
                        Message msg =myHandler.obtainMessage();
                        msg.what = 100;
                        msg.obj = 100;
                        myHandler.sendMessage(msg);
                        mContext.installAPK(apkFile);
                        return;
                    }
                }
//                System.out.println(upload_url +"cache------------------2222222222222---------------------"+ apkFile);
//                try{
//                    apkFile.createNewFile();
//                }catch (Exception e){
//                    System.out.println(e);
//                }
                //                    apkFile.delete();
                File parentDirectory = apkFile.getParentFile();
                if (parentDirectory == null
                        || (!parentDirectory.exists() && !parentDirectory.mkdirs())) {
                    throw new IOException("Unable to create APK download directory");
                }
                if (!apkFile.exists() && !apkFile.createNewFile()) {
                    throw new IOException("Unable to create APK file");
                }
//                System.out.println("cache---------------------------------------" + apkFile);


                fos = new FileOutputStream(apkFile);

                //获得输入流

                is = response.body().byteStream();

                //定义缓冲区大小

                byte[] bys = new byte[1024];

                int progress = 0;
                progressNetwork = 0;

                int len = -1;
                long count = 0;

                while ((len = is.read(bys)) != -1) {

                    try {

//                        Thread.sleep(1);

                        fos.write(bys, 0, len);
                        count += len;
                        progress = (int) (((double) count / contentLength) * 100);
                        progressNetwork = progress;
//                        System.out.println("--------progressNetwork-------" + progressNetwork);

                        fos.flush();

//                        progress += len;
                        Message msg =myHandler.obtainMessage();
                                                msg.what = 100;
                                                msg.obj = progress;
                                                myHandler.sendMessage(msg);

                        //设置进度

//                        dialog.setProgress(progress);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //下载完成,提示用户安装
                Message msg =myHandler.obtainMessage();
                msg.what = 200;
                msg.obj = apkFile;
                myHandler.sendMessage(msg);
//                installApk(apkFile);

            }

        } catch (IOException e) {

            e.printStackTrace();
//            myHandler.sendEmptyMessage(101);
            try{
                if (apkFile.exists()){
//                    BigDecimal a = new BigDecimal(fileSize);
//                    BigDecimal b = new BigDecimal(1024);
//                    BigDecimal result = a.divide(b, 0, RoundingMode.CEILING);
//
//                    System.out.println(Math.ceil(result.doubleValue()) + "------------------contentLength----------------------");

                    BigDecimal a2 = new BigDecimal(apkFile.length());
                    BigDecimal b2 = new BigDecimal(1024);
                    BigDecimal result2 = a2.divide(b2, 0, RoundingMode.CEILING);



                    if(fileSize == result2.doubleValue()){
//                        System.out.println("--------result2------");
//                        System.out.println("--------progressNetwork-------" + progressNetwork);
                        Message msg = myHandler.obtainMessage();
                        msg.what = 100;
                        msg.obj = 100;
                        myHandler.sendMessage(msg);
                        mContext.installAPK(apkFile);
//                        return;
                    }else{
                        Message msg =myHandler.obtainMessage();
                        msg.what = 1002;
                        msg.obj = 1002;
                        myHandler.sendMessage(msg);
                    }

//                    System.out.println(fileSize + "--------result-------" + result2);

                }
            }catch (Exception e2){
                System.out.println(e2);
            }


        } finally {

            //关闭io流

            if (is != null) {

                try {

                    is.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                is = null;

            }
            if (fos != null) {

                try {

                    fos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                fos = null;

            }

        }

//        dialog.dismiss();

    }

    /**
     * App-specific storage works with scoped storage and needs no storage permission.
     * It is also covered by the app FileProvider when the package installer reads the APK.
     */
    private File getApkFile() {
        File downloadDirectory = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDirectory == null) {
            downloadDirectory = new File(mContext.getFilesDir(), "downloads");
        }

        String safeVersionName = versionname == null
                ? ""
                : versionname.replaceAll("[^a-zA-Z0-9._-]", "_");
        return new File(downloadDirectory, "android_gkai" + safeVersionName + ".apk");
    }

}
