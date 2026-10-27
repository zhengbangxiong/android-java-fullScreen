package app.gkai.ai.modules.mine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.verificationcodejavademo.widget.BlockPuzzleDialog;

import java.lang.ref.WeakReference;
import java.util.Locale;

import app.gkai.ai.R;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.constant.Session;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.util.CountDownTimerUtils;
import app.gkai.ai.core.common.util.StringUtils;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.mine.beans.req.AuthIdentityReq;
import app.gkai.ai.modules.mine.beans.req.SendMailReq;
import app.gkai.ai.modules.mine.beans.req.UnbindReq;
import app.gkai.ai.modules.mine.service.MemberAuthService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @title 实名认证
 */
public class AuthenticationActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AuthenticationActivity";

    @BindView(R.id.llt_left)
    LinearLayout llt_left;
    @BindView(R.id.tv_title)
    TextView     tv_title;
    @BindView(R.id.bt_next)
    Button       bt_next;
    @BindView(R.id.et_name)
    EditText     et_name;
    @BindView(R.id.et_id_number)
    EditText     et_id_number;
    @BindView(R.id.llt_channel)
    LinearLayout llt_channel;
    @BindView(R.id.et_email)
    TextView     et_email;
    @BindView(R.id.tv_yzm)
    TextView     tv_yzm;
    @BindView(R.id.et_yzm)
    EditText     et_yzm;


    private Bundle bundle;
    /**
     * 阿里云返回path
     */
    private String idFrontImg    = "";
    private String idBackImg     = "";
    private String idHandheldImg = "";

    private MemberAuthService                     mMemberAuthService = new MemberAuthService();
    private WeakReference<AuthenticationActivity> mActivity          = new WeakReference(this);


    @Override
    protected void initView() {
        setContentView(R.layout.activity_authentication_one);
        setStatusBar(R.color.color_1E86FC, false);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);

        initValue();

    }

    public void initValue() {
        bundle = getIntent().getBundleExtra("bundle");
        if ("1".equals(bundle.getString("channel"))) {
            tv_title.setText(getResources().getString(R.string.unbindGoogle));
        } else {
            tv_title.setText(getResources().getString(R.string.my_authentication));
        }

        idFrontImg = bundle.getString("idFrontImg");
        idBackImg = bundle.getString("idBackImg");
        idHandheldImg = bundle.getString("idHandheldImg");

        if ("1".equals(bundle.getString("channel"))) {
            llt_channel.setVisibility(View.VISIBLE);
            et_email.setText(Session.mail);
        } else {
            llt_channel.setVisibility(View.GONE);
        }

        try {
            if (null != bundle.getString("familyName")) {

                et_name.setText(bundle.getString("familyName"));
                et_id_number.setText(bundle.getString("idNumber"));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void releaceSource() {

    }

    @OnClick({R.id.llt_left, R.id.bt_next, R.id.tv_yzm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llt_left:
                this.finish();
                break;
            case R.id.bt_next:
                String name = et_name.getText().toString().trim();
                String number = et_id_number.getText().toString().trim();
                if (StringUtils.isEmpty(name)) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.authentication_hint_id_name));
                    return;
                } else if (StringUtils.isEmpty(number)) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.authentication_hint_id_number));
                    return;
                }

                bundle.putString("familyName", name);
                bundle.putString("idNumber", number);


                //                Intent intent = new Intent(this, AuthenticationPictureActivity.class);
                //                if(null == bundle){
                //                    bundle = new Bundle();
                //                }
                //
                //                bundle.putString("familyName", name);
                //                bundle.putString("idNumber", number);
                //                intent.putExtra("bundle", bundle);
                //                startActivity(intent);

                if ("1".equals(bundle.getString("channel"))) {
                    Session.mail = ClientConfigDao.GetConfigString(this, Constants.mail, "");
                    if (StringUtils.isEmpty(et_yzm.getText().toString().trim())) {
                        MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(R.string.input_yzm));
                        return;
                    }

                    UnbindReq mUnbindReq = new UnbindReq();
                    mUnbindReq.setEmail(Session.mail);
                    mUnbindReq.setFamilyName(name);
                    mUnbindReq.setIdNumber(number);
                    mUnbindReq.setIdFrontImg(idFrontImg);
                    mUnbindReq.setIdBackImg(idBackImg);
                    mUnbindReq.setIdHandheldImg(idHandheldImg);
                    mUnbindReq.setSmsCode(et_yzm.getText().toString().trim());

                    showDialog();
                    mMemberAuthService.addUnbind(mActivity.get(), mUnbindReq);

                } else {
                    AuthIdentityReq authIdentityReq = new AuthIdentityReq();
                    authIdentityReq.setFamilyName(name);
                    authIdentityReq.setIdNumber(number);
                    authIdentityReq.setIdFrontImg(idFrontImg);
                    authIdentityReq.setIdBackImg(idBackImg);
                    authIdentityReq.setIdHandheldImg(idHandheldImg);

                    showDialog();
                    mMemberAuthService.authIdentity(mActivity.get(), authIdentityReq);
                }

                break;
            case R.id.tv_yzm:
                BlockPuzzleDialog blockPuzzleDialog = new BlockPuzzleDialog(mActivity.get());
                blockPuzzleDialog.setOnResultsListener(new BlockPuzzleDialog.OnResultsListener() {
                    @Override
                    public void onResultsClick(String result) {
                        //todo 二次校验回调结果
//                        System.out.println(result + "-----------------------result---------------------");
                        try {
                            SendMailReq sendMailReq = new SendMailReq();
                            sendMailReq.setName(Session.mail);
                            sendMailReq.setCheckCodeToken(result);
                            mMemberAuthService.sendMail(mActivity.get(), sendMailReq);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                blockPuzzleDialog.show();
                //                mMemberAuthService.yunpian(this, Session.mail);
                break;
        }
    }

    public void setStartActivity() {
        //        jumpActivity(AuthenticationAuditActivity.class);
        Intent intent = new Intent(this, AuthenticationAuditActivity.class);
        bundle.putSerializable("authindentityResp", null);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        ActivityManager.getActivity("AuthenticationActivity").finish();
        ActivityManager.getActivity("AuthenticationPictureActivity").finish();

    }

    /**
     * // 倒计时
     */
    public void setCountDownTimerUtils() {
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tv_yzm, 120000, 1000, this);
        mCountDownTimerUtils.start();
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
