
package com.gaiya.easybuy.activity;

import android.view.View;
import android.widget.TextView;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.util.SystemUtil;
import com.gaiya.easybuy.util.ViewUtil;

/**
 * Created by dengt on 15-5-26.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private TextView version;

    @Override
    protected void doInit() {
        initView();
        initData();
        initEvent();

    }

    private void initView() {

        setTitle("关于柠檬豆");
        version = ViewUtil.findViewById(this, R.id.app_version);
    }

    private void initData() {
        version.setText(SystemUtil.getCurrentVersionName(GApplication.getInstance()));
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
        return R.layout.activity_about;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                ViewUtil.finish(this);
                break;
        }
    }
}
