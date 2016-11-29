
package com.gaiya.easybuy.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gaiya.easybuy.R;
import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;

import org.json.JSONObject;

/**
 * Created by dengt on 15-9-28.
 */
public class ForgetInputPwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText userPhone;
    private EditText userCode;
    private Button registerBtn;
    private String mobile;
    private String code;
    private GApplication gApplication;

    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();

        initView();
        initDate();
        initEvent();
    }

    @Override
    protected boolean isDisplayTitle() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_find_input_pwd;
    }

    private void initView() {
        userPhone = ViewUtil.findViewById(this, R.id.input_user_pwd);
        userCode = ViewUtil.findViewById(this, R.id.input_agian_user_pwd);
        registerBtn = ViewUtil.findViewById(this, R.id.commit_btn);
    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private void initDate() {
        setTitle("找回密码");
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        code = intent.getStringExtra("code");
    }

    private void edit() {
        String pwd1 = userPhone.getText().toString();
        String pwd2 = userCode.getText().toString();
        if (pwd1.isEmpty() || pwd2.isEmpty()) {
            ToastUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            ToastUtil.showMessage("两次输入的密码不一样");
            return;
        }
        if (pwd2.length() >= 6 && pwd2.length() <= 20) {
            UserModel.resetPassword(mobile, userCode.getText().toString().trim(), code).done(
                    new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            String code = jsonObject.optString("status");
                            Logger.e("setnewpwd", jsonObject.toString());
                            if (code.equals("000000")) {
                                ToastUtil.showMessage("密码设置成功");
                                String token = jsonObject.optString("newToken");
                                PreferencesUtils.putString(gApplication,
                                        PreferencesConstant.API_TOKEN, token);
                                gApplication.updateApiToken();
                                PreferencesUtils.putString(ForgetInputPwdActivity.this,
                                        PreferencesConstant.USER_PASSWORD, userCode.getText()
                                                .toString().trim());
                                Intent intent = new Intent(ForgetInputPwdActivity.this,
                                        MainActivity.class);
                                ViewUtil.startTopActivity(ForgetInputPwdActivity.this, intent);
                                finish();
                            } else {
                                ToastUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }

                    });
        } else {
            ToastUtil.showMessage("密码长度必须大于6位小于20位");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.commit_btn:
                edit();
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }
}
