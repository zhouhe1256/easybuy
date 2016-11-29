
package com.gaiya.easybuy.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.TimeCount;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;

import org.json.JSONObject;

/**
 * Created by dengt on 15-9-25.
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener,
        TimeCount.TimeUpdate {
    private EditText userPhone;
    private EditText userCode;
    private Button registerBtn;
    private TimeCount time;
    private TextView userCodeBtn;
    private TextView smsCode;
    @Override
    protected void doInit() {
        initView();
        initDate();
        initEvent();
    }

    private void initView() {

        userPhone = ViewUtil.findViewById(this, R.id.register_user_phone);
        userCode = ViewUtil.findViewById(this, R.id.register_user_code);
        registerBtn = ViewUtil.findViewById(this, R.id.next_btn);
        userCodeBtn = ViewUtil.findViewById(this, R.id.register_get_code_btn);
        smsCode = ViewUtil.findViewById(this,R.id.sms_code);
    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userCode.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        userCodeBtn.setOnClickListener(this);
    }

    private void initDate() {
        setTitle("找回密码");
        time = new TimeCount(60000, 1000, ForgetPwdActivity.this);
    }

    private void sendCheckCode() {
        String phone = userPhone.getText().toString().trim();
        // ValidationUtil.matches(phone, "^1[3|4|5|7|8|][0-9]{9}$",
        // "请填写正确的手机号码");
        if (phone.length() > 0) {
            userCodeBtn.setClickable(false);
            // userCodeBtn.setBackgroundResource(R.color.gray);
            UserModel.sendCheckCode(phone, "2").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    String code = jsonObject.optString("status");
                    Logger.e("setnewpwdcode", jsonObject.toString());
                    if (code.equals("000000")) {
                        time.start();
                        userCodeBtn.setBackground(getResources().getDrawable(R.drawable.solid_bg));
                        smsCode.setVisibility(View.VISIBLE);
                    } else {
                        time.cancel();
                        userCodeBtn.setText("获取验证码");
                        userCodeBtn.setBackground(getResources().getDrawable(R.drawable.yellow_big_selector));
                        userCodeBtn.setClickable(true);
                        ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        smsCode.setVisibility(View.GONE);
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    userCodeBtn.setClickable(true);
                }
            });
        }
    }

    @Override
    protected boolean isDisplayTitle() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_findpwd;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_code_btn:
                // 获取验证码
                sendCheckCode();
                break;
            case R.id.next_btn:
                // 注册
                // register();
                String code = userCode.getText().toString().trim();
                String userphone = userPhone.getText().toString();
                if (code == null || code.isEmpty()) {
                } else {
                    Intent intent = new Intent(this, ForgetInputPwdActivity.class);
                    intent.putExtra("mobile", userphone);
                    intent.putExtra("code", code);
                    ViewUtil.startActivity(this, intent);
                }
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        userCodeBtn.setText((millisUntilFinished / 1000) + "秒后重发");
        userCodeBtn.setClickable(false);
    }

    @Override
    public void onFinish() {
        userCodeBtn.setText("获取验证码");
        userCodeBtn.setClickable(true);
        userCodeBtn.setBackground(getResources().getDrawable(R.drawable.yellow_big_selector));
        smsCode.setVisibility(View.GONE);
    }

}
