package app.gkai.ai.modules.mine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.modules.mine.beans.resp.AuthindentityResp;
import app.gkai.ai.R;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @title 实名认证
 */
public class AuthenticationAuditActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AuthenticationAuditActivity";

    @BindView(R.id.llt_left)
    LinearLayout llt_left;
    @BindView(R.id.tv_title)
    TextView     tv_title;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.tv_familyName)
    TextView tv_familyName;
    @BindView(R.id.tv_idNumber)
    TextView tv_idNumber;
    @BindView(R.id.tv_rejectReason)
    TextView tv_rejectReason;
    @BindView(R.id.img_picture)
    ImageView img_picture;
    @BindView(R.id.img_top_picture)
    ImageView img_top_picture;
    @BindView(R.id.tv_top_text)
    TextView tv_top_text;

    private AuthindentityResp authindentityResp;
    private Bundle            bundle;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_authentication_three);
        setStatusBar(R.color.color_1E86FC, false);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);

        initValue();

    }

    public void initValue(){
        bundle = getIntent().getBundleExtra("bundle");
        if ("1".equals(bundle.getString("channel"))) {
            tv_title.setText(getResources().getString(R.string.unbindGoogle));
        }else{
            tv_title.setText(getResources().getString(R.string.my_authentication));
        }


        try{
            if(null != bundle.getSerializable("authindentityResp")){
                authindentityResp = (AuthindentityResp)bundle.getSerializable("authindentityResp");
                setAudit(authindentityResp);
            }else{
                tv_rejectReason.setVisibility(View.GONE);
                tv_familyName.setText(getResources().getString(R.string.info_name) +bundle.getString("familyName"));
                tv_idNumber.setText(getResources().getString(R.string.info_idnumber) + bundle.getString("idNumber"));
                if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review));
                } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review_tw));
                } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review_en));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void releaceSource() {

    }

    @OnClick({R.id.llt_left, R.id.bt_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llt_left:
                this.finish();
                break;
            case R.id.bt_next:
                Intent intent = new Intent(this, AuthenticationActivity.class);
//                Bundle bundle = new Bundle();
                bundle.putSerializable("authindentityResp", authindentityResp);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    public void setAudit(AuthindentityResp authindentityResp){
//        this.authindentityResp = authindentityResp;
        try{
            tv_familyName.setText(getResources().getString(R.string.info_name) + authindentityResp.getFamilyName());
            tv_idNumber.setText(getResources().getString(R.string.info_idnumber) + authindentityResp.getIdNumber());
            String status = "" + authindentityResp.getIdStatus();
            if("0".equals(status)){
//                img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review));
                if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review));
                } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review_tw));
                } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authencation_review_en));
                }
            }else if("1".equals(status)){
                Session.isRealname = "1";
                ClientConfigDao.SetConfigString(this, Constants.isRealname, Session.isRealname);

                img_top_picture.setImageDrawable(getResources().getDrawable(R.drawable.google_yes));
                tv_top_text.setTextColor(getResources().getColor(R.color.color_1E86FC));

//                img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_through));
                if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_through));
                } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_through_tw));
                } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_through_en));
                }
            }else if("2".equals(status)){
//                img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_error));
                if (Constants.LANGUAGE_CN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_error));
                } else if (Constants.LANGUAGE_TW.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_error_tw));
                } else if (Constants.LANGUAGE_EN.equals(Session.LANGUAGE)) {
                    img_picture.setImageDrawable(getResources().getDrawable(R.drawable.authentication_error_en));
                }
                tv_rejectReason.setText(getResources().getString(R.string.info_refuse) + authindentityResp.getRejectReason());
                bt_next.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showDialog() {
        showLoadingDialog(getResources().getString(R.string.loading));
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
