
package com.gaiya.easybuy.activity;

import android.view.View;
import android.webkit.WebView;

import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.util.ViewUtil;

/**
 * Created by dengt on 15-10-14.
 */
public class RegisterNoteActivity extends BaseActivity implements View.OnClickListener {
    private WebView note;

    @Override
    protected void doInit() {
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        setTitle("注册协议");
        note = ViewUtil.findViewById(this, R.id.register_note_text);
    }

    private void initData() {
        Logger.i("urlff",GApplication.getInstance().getPropertyModel().getProtocol_url());
        note.loadUrl(GApplication.getInstance().getPropertyModel().getProtocol_url());
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
        return R.layout.activity_registernote;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                ViewUtil.finish(this);
                break;
        }
    }
}
