
package com.gaiya.easybuy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.util.DialogUtil;
import com.gaiya.android.util.Logger;
import com.gaiya.android.view.ImageViewAdapter;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.PropertyModel;
import com.gaiya.easybuy.model.PushModel;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.ClearWebview;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.SystemUtil;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;
import com.gaiya.easybuy.view.CircleImageView;
import com.gaiya.easybuy.view.LoadingDialog;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by dengt on 15-9-25.
 */
public class LoginActivity extends Activity implements View.OnClickListener, ICallback {
    private Button loginbtn;
    private CircleImageView topView;
    private TextView newlogin;
    private TextView forgetbtn;
    private EditText loginUser;
    private EditText loginpwd;
    private GApplication gApplication;
    private ImageView imageView;
    private boolean pwdFlag = false;
    private PushModel pushModel;
    private LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        ClearWebview.clearWebViewCache(this, "easybuy");
        ClearWebview.clearWebViewCache(this,"easybuy_1");
        ClearWebview.clearWebViewCache(this,"easybuy_2");

        loginbtn = ViewUtil.findViewById(this, R.id.login_btn);
        topView = ViewUtil.findViewById(this, R.id.user_img);
        newlogin = ViewUtil.findViewById(this, R.id.new_login);
        forgetbtn = ViewUtil.findViewById(this, R.id.forget_secrete);
        loginUser = ViewUtil.findViewById(this, R.id.login_user);
        loginpwd = ViewUtil.findViewById(this, R.id.login_pwd);
        imageView = ViewUtil.findViewById(this, R.id.pwd_visiable);
    }

    private void initEvent() {
        loginbtn.setOnClickListener(this);
        newlogin.setOnClickListener(this);
        forgetbtn.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private void initData() {
        PreferencesUtils.putString(gApplication, PreferencesConstant.USER_ID, "");
        PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, "");
        gApplication.updateUserID();
        gApplication.updateApiToken();
        PropertyModel.getProperty().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String code = jsonObject.optString("status");

                Logger.e("PropertyModel", jsonObject.toString());
                if (code.equals("000000")) {
                    PropertyModel propertyModel = JSONUtil
                            .load(PropertyModel.class, jsonObject.optJSONObject("result"));
                    gApplication.setPropertyModel(propertyModel);
                    // propertyModel.setHome_url("http://192.168.23.1:8080/mobile/index.html");
                }else{
                    ToastUtil.showMessage(ErrorCode.getCodeName(code));
                }
            }
        });

        String user_name = PreferencesUtils.getString(this, PreferencesConstant.USER_NAME,
                "");
        String pass_word = PreferencesUtils.getString(this,
                PreferencesConstant.USER_PASSWORD, "");
        loginUser.setText(user_name);
        loginpwd.setText(pass_word);
        String userImg = PreferencesUtils.getString(this, PreferencesConstant.USER_IMAGEURL,
                "");
        ImageViewAdapter.adapt(topView, userImg, R.drawable.login_default,
                R.drawable.login_default, true);

        pushModel = (PushModel) getIntent().getSerializableExtra("push");
    }

    private void login() {
        String user = loginUser.getText().toString();
        String password = loginpwd.getText().toString();
        if (user.isEmpty() || password.isEmpty()) {
            DialogUtil.hintMessage(this, "用户名或密码不能为空");
            return;
        }
        String uuid = UUID.randomUUID().toString();
        String imei = gApplication.getIMEI();
        String deviceIdent = uuid + "_" + imei + "_" + SystemUtil.getUser_Agent();
        UserModel.login(user, password, deviceIdent).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                progress.dismiss();
                ToastUtil.showMessage("网络异常");
            }
        });
        // Intent intent = new Intent(this, MainActivity.class);
        // ViewUtil.startActivity(this, intent);
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");

        Logger.e("login", jsonObject.toString());
        if (code.equals("000000")) {
            UserModel userModel = JSONUtil
                    .load(UserModel.class, jsonObject.optJSONObject("result"));
            String token = jsonObject.optString("newToken");
            // String token = userModel.getApiToken();
            String userid = userModel.getUserId();
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_ID, userid);
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_NAME, loginUser
                    .getText().toString().trim());
            // PreferencesUtils.putString(gApplication,
            // PreferencesConstant.USER_IMGID,userModel.getImageId());
            // PreferencesUtils.putString(gApplication,
            // PreferencesConstant.NICK_NAME,userModel.getNickName());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_PASSWORD, loginpwd
                    .getText().toString().trim());
            gApplication.updateApiToken();
            gApplication.updateUserID();
            progress.dismiss();
            ToastUtil.showMessage("登录成功");
            Intent intent = new Intent(this, TabActivity.class);
            // pushModel = JSONUtil.load(PushModel.class,
            // "{\"type\":\"1\",\"url\":\"http://www.qq.com\",\"content\":\"腾讯\"}");
            if (pushModel != null) {
                intent.putExtra("push", pushModel);
            }
            ViewUtil.startActivity(this, intent);
            UserModel.updatepushClientId(PushManager.getInstance().getClientid(this)).done(
                    new ICallback() {
                        @Override
                        public void call(Arguments arguments) {

                        }
                    });

            finish();
        } else {
            ToastUtil.showMessage(ErrorCode.getCodeName(code));
            progress.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_btn:
                if(progress == null){
                    progress = new LoadingDialog(this);
                }
                progress.show();
                login();
                break;
            case R.id.new_login:
                intent = new Intent(this, RegisterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.forget_secrete:
                intent = new Intent(this, ForgetPwdActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.pwd_visiable:
                if (pwdFlag == false) {
                    pwdFlag = true;
                    loginpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pwdFlag = false;
                    loginpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
