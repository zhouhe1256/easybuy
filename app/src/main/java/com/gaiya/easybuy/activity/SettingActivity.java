
package com.gaiya.easybuy.activity;

import android.content.Intent;
import android.view.View;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.uptutil.DownloadManager;
import com.gaiya.easybuy.util.ClearWebview;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.ViewUtil;

/**
 * Created by dengt on 15-9-29.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void doInit() {
        initView();
        initData();
        initEvent();

    }

    private void initView() {

        setTitle("设置");

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
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_btn:
                ViewUtil.finish(this);
                break;
            case R.id.update:
                DownloadManager downManger = new DownloadManager(this, true);
                downManger.checkDownload();
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.logout_btn:
                PreferencesUtils.putString(GApplication.getInstance(),
                        PreferencesConstant.API_TOKEN, "");
                intent = new Intent(this, LoginActivity.class);
                ViewUtil.startTopActivity(this, intent);
                GApplication.getInstance().exit();
                ClearWebview.clearWebViewCache(this, "easybuy");
                ClearWebview.clearWebViewCache(this,"easybuy_1");
                ClearWebview.clearWebViewCache(this,"easybuy_2");
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(SettingActivity.this);
    }
}
