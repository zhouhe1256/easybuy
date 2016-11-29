
package com.gaiya.easybuy.activity;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.gaiya.easybuy.view.DeleteInfoDialog;

import org.json.JSONObject;

/**
 * Created by dengt on 14-12-29.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener, ICallback,
        TextWatcher, DeleteInfoDialog.DeleteInfoDialogResult {
    private Activity context;

    private EditText feedbackContent;
    private EditText feedbackcontact;
    private TextView teSize;
    private LinearLayout commitBtn;
    private LinearLayout cancleBtn;
    private GApplication gApplication;

    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        context = this;
        setTitle("意见反馈");

        feedbackContent = ViewUtil.findViewById(context, R.id.feedback_content);
        feedbackcontact = ViewUtil.findViewById(context, R.id.feedback_contact);
        commitBtn = ViewUtil.findViewById(context, R.id.feedback_commit_btn);
        cancleBtn = ViewUtil.findViewById(context, R.id.feedback_cacle_btn);
        teSize = ViewUtil.findViewById(context, R.id.feed_textsize);

    }

    private void initData() {

    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        // feedbackcontact.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        feedbackContent.addTextChangedListener(this);
    }

    private void commit() {
        String content = feedbackContent.getText().toString().trim();
        if (content.isEmpty()) {
            ToastUtil.showMessage("尚未填写反馈内容");
            return;
        }
        UserModel.feedBack(feedbackcontact.getText().toString().trim(),
                content).done(this);
    }

    @Override
    protected boolean isDisplayTitle() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");
        Logger.e("login", jsonObject.toString());
        if (code.equals("000000")) {
            String token = jsonObject.optString("newToken");
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            ToastUtil.showMessage("感谢参与！");
            gApplication.updateApiToken();
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
            case R.id.feedback_commit_btn:
                commit();
                break;
            case R.id.feedback_cacle_btn:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog, GApplication.getInstance().getPropertyModel().getTel()
                                .trim(), "呼叫", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String nickname = feedbackContent.getText().toString();
        int length = nickname.length();
        teSize.setText(length + "/100");
        if (length > 100) {
            teSize.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + GApplication.getInstance().getPropertyModel().getTel().trim()));
            this.startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(FeedbackActivity.this);
    }
}
