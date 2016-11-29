
package com.gaiya.easybuy.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiya.android.remote.Http;
import com.gaiya.easybuy.R;
import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.util.DialogUtil;
import com.gaiya.android.util.Logger;
import com.gaiya.android.util.ValidationUtil;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.constant.ApiUrl;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.TimeCount;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dengt on 15-9-29.
 */
public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener , TimeCount.TimeUpdate, ICallback{
    private Activity context;
    private GApplication gApplication;

    private EditText userPhone;
    private EditText userCode;
    private TextView userCodeBtn;
    private TimeCount time;
    private Button cancleBtn;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                sendCheckCode();
            }
        }
    };
    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();

        initView();
        initData();
        initEvent();

    }

    private void initView() {
        context = this;
        setTitle("更换手机");

        userPhone = ViewUtil.findViewById(context, R.id.register_user_phone);
        userCode = ViewUtil.findViewById(context, R.id.register_user_pwd);
        userCodeBtn = ViewUtil.findViewById(context, R.id.register_get_code_btn);
        cancleBtn = ViewUtil.findViewById(context, R.id.register_btn);

    }

    private void initData() {
        time = new TimeCount(60000, 1000, ChangePhoneActivity.this);
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
        return R.layout.activity_change_phone;
    }
    private void isRegisterPhone(){
        String phone = userPhone.getText().toString().trim();
        try {
            ValidationUtil.matches(phone, "^1[3|4|5|7|8|][0-9]{9}$",
                    "请填写正确的手机号码");
        } catch (ValidationUtil.ValidationException e) {
            ToastUtil.showMessage("请填写正确的手机号码");
            return;
        }
        Http.instance().post(ApiUrl.IS_REGISTER_PHONE).param("mobile",phone).run().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                try {
                    String status = jsonObject.getString("status");
                    if(status.equals("000000")){
                        Message msg = handler.obtainMessage();
                        msg.what=1;
                        handler.sendMessage(msg);
                    }else {
                        ToastUtil.showMessage(ErrorCode.getCodeName(status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Logger.i("isphone",jsonObject.toString());

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ToastUtil.showMessage("网络异常");
            }
        });
    }
    private void sendCheckCode() {
        String phone = userPhone.getText().toString().trim();
        try {
            ValidationUtil.matches(phone, "^1[3|4|5|7|8|][0-9]{9}$",
                    "请填写正确的手机号码");
        } catch (ValidationUtil.ValidationException e) {
            ToastUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0) {
            userCodeBtn.setClickable(false);
            // userCodeBtn.setBackgroundResource(R.color.gray);
            UserModel.sendCheckCode(phone, "1").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    String code = jsonObject.optString("status");
                    if (code.equals("000000")) {
                        time.start();
                    } else {
                        time.cancel();
                        userCodeBtn.setText("获取验证码");
                        userCodeBtn.setClickable(true);
                        ToastUtil.showMessage(ErrorCode.getCodeName(code));
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
    private void change(){
        String phone = userPhone.getText().toString().trim();
        String code = userCode.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.hintMessage(this, "请输入手机号码");
            return;
        }
        try {
            ValidationUtil.matches(phone, "^1[3|4|5|7|8|][0-9]{9}$",
                    "请填写正确的手机号码");
        } catch (ValidationUtil.ValidationException e) {
            ToastUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (code.length() == 0) {
            DialogUtil.hintMessage(this, "请输入验证码");
            return;
        }

            if (phone.length() > 0  && code.length() > 0)
                UserModel.changephone(phone, code).done(this)
                        .fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                // ToastUtil.showMessage(getString(R.string.empty_net_text));
                            }
                        });

    }
    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");
        Logger.e("changephone", jsonObject.toString());
        if (code.equals("000000")) {
            UserModel userModel = JSONUtil
                    .load(UserModel.class, jsonObject.optJSONObject("result"));
            String token = jsonObject.optString("newToken");
            String userid = userModel.getUserId();
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_ID, userid);
            gApplication.updateApiToken();
            gApplication.updateUserID();
            ToastUtil.showMessage("变更手机号成功");
            finish();
        } else {
            ToastUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                ViewUtil.finish(this);
                break;
            case R.id.register_btn:
                change();
                break;
            case R.id.register_get_code_btn:
                // 获取验证码
                //sendCheckCode();
                isRegisterPhone();
                break;
        }
    }
    @Override
    public void onTick(long millisUntilFinished) {
        userCodeBtn.setText((millisUntilFinished / 1000) + "秒后重发");
        userCodeBtn.setClickable(false);
       // userCodeBtn.setBackgroundResource(R.color.gray);
    }

    @Override
    public void onFinish() {
        userCodeBtn.setText("获取验证码");
        userCodeBtn.setClickable(true);
       // userCodeBtn.setBackgroundResource(R.color.gray);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(ChangePhoneActivity.this);
    }
}
