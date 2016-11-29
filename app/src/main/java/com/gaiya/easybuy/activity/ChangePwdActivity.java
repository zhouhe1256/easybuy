
package com.gaiya.easybuy.activity;

import android.view.View;
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
 * Created by dengt on 15-9-29.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText oldPwd;
    private EditText newPwd;
    private EditText surePwd;
    private GApplication gApplication;

    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        setTitle("修改密码");
        oldPwd = ViewUtil.findViewById(this, R.id.login_pwd);
        newPwd = ViewUtil.findViewById(this, R.id.set_pwd);
        surePwd = ViewUtil.findViewById(this, R.id.again_pwd);
    }

    private void edit() {
        String pwd1 = newPwd.getText().toString();
        String pwd2 = surePwd.getText().toString();
        if (pwd1.isEmpty() || pwd2.isEmpty() || oldPwd.getText().toString().isEmpty()) {
            ToastUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            ToastUtil.showMessage("两次输入的密码不一样");
            return;
        }
        if (pwd2.length() >= 6 && pwd2.length() <= 20) {
            UserModel.changePassword(oldPwd.getText().toString(),
                    newPwd.getText().toString()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    String code = jsonObject.optString("status");
                    Logger.e("setnewpwd", jsonObject.toString());
                    if (code.equals("000000")) {
                        ToastUtil.showMessage("修改成功");
                        String token = jsonObject.optString("newToken");
                        PreferencesUtils.putString(gApplication,
                                PreferencesConstant.API_TOKEN, token);
                        gApplication.updateApiToken();
                        PreferencesUtils.putString(ChangePwdActivity.this,
                                PreferencesConstant.USER_PASSWORD, newPwd.getText().toString());
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

    private void initData() {

    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
    }

    @Override
    protected boolean isDisplayTitle() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                ViewUtil.finish(this);
                break;
            case R.id.register_btn:
                edit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(ChangePwdActivity.this);
    }
}
