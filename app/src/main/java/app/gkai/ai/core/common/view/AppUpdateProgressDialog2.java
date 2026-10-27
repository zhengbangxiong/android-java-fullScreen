package app.gkai.ai.core.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.gkai.ai.R;
import app.gkai.ai.modules.home.beans.req.FindAppUpdateRep;
import app.gkai.ai.modules.home.ui.activity.AndroidJSWebViewActivity;


/**
 * Created by zbx on 2018/4/10.
 */

public class AppUpdateProgressDialog2 extends Dialog {

    private NumberProgressBar numberProgressBar;
    private FindAppUpdateRep  findAppUpdateRep;
    private TextView          tv_version, tv_description;
    private LinearLayout             llt_progress;
    private Button                   bt_next;
    private AndroidJSWebViewActivity context;
    private TextView problem_bg;
    private TextView       problem_text;
    private RelativeLayout llt_upload;

    public AppUpdateProgressDialog2(AndroidJSWebViewActivity context, FindAppUpdateRep findAppUpdateRep) {
        super(context, R.style.Custom_Progress);
        this.findAppUpdateRep = findAppUpdateRep;
        this.context = context;
        initLayout();
    }

    public AppUpdateProgressDialog2(Context context, int theme) {
        super(context, R.style.Custom_Progress);
        initLayout();
    }

    private void initLayout() {
        this.setContentView(R.layout.update_progress_layout);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.number_progress);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_description = (TextView) findViewById(R.id.tv_description);

        llt_progress = (LinearLayout) findViewById(R.id.llt_progress);
        problem_bg = (TextView)findViewById(R.id.problem_bg);

        llt_upload = (RelativeLayout) findViewById(R.id.llt_upload);

        llt_upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                problem_bg.setVisibility(View.GONE);
            }
        });

        problem_bg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                problem_bg.setVisibility(View.GONE);
            }
        });

        problem_text = (TextView)findViewById(R.id.problem_text);

        problem_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                problem_bg.setVisibility(View.VISIBLE);
            }
        });



        bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                llt_progress.setVisibility(View.VISIBLE);
//                bt_next.setVisibility(View.GONE);
                if(1 == findAppUpdateRep.getIsUpdate()){
                    context.setApkBrowserDownLoad(findAppUpdateRep.getUrl());
                }else {
                    context.getPerssion(bt_next, llt_progress, findAppUpdateRep.getUrl());
                }
            }
        });

        tv_version.setText("V" + findAppUpdateRep.getVersion());
        try{
            tv_description.setText(findAppUpdateRep.getDes().replace("," , "\n"));
        }catch (Exception e){
            e.printStackTrace();
        }


        this.setCanceledOnTouchOutside(false);//点击dialog背景部分不消失
//        this.setCancelable(false);//dialog出现时，点击back键不消失
    }

    public void setProgress(int mProgress) {
        numberProgressBar.setProgress(mProgress);
    }

    public void setButton() {
        llt_progress.setVisibility(View.GONE);
        bt_next.setVisibility(View.VISIBLE);
        bt_next.setText(R.string.uploading_text);
        problem_text.setVisibility(View.VISIBLE);
    }

    public void setButton2() {
        llt_progress.setVisibility(View.GONE);
        bt_next.setVisibility(View.VISIBLE);
        bt_next.setText(R.string.uploading_text);
        problem_text.setVisibility(View.GONE);
    }
}
