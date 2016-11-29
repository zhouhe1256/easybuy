package com.gaiya.easybuy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiya.easybuy.R;

/**
 * Created by dengt on 15-9-24.
 */
public abstract class BaseActivity extends Activity {
    protected ImageView leftBtn;
    protected ImageButton leftCloseBtn;
    protected ImageButton rightBtn;
    protected ImageButton rightShareBtn;
    protected TextView rightText;
    protected TextView titleView;
    protected LinearLayout windowBodyLayout;
    protected LayoutInflater inflater;
    protected RelativeLayout windowHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        inflater = getLayoutInflater();

        initLayout();
        doInit();
    }
    @Override
    protected void onStart() {
        //ProductReceiver.baseActivity = this;
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    public void setRightBtnOnClick(View.OnClickListener click) {
        if (View.VISIBLE == rightBtn.getVisibility()) {
            rightBtn.setOnClickListener(click);
        } else if (View.VISIBLE == rightText.getVisibility()) {
            rightText.setOnClickListener(click);
        }
    }

    public void setRightBtn(String text) {
        rightBtn.setVisibility(View.GONE);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(text);
    }

    public void setRightBtn(int resid) {
        rightText.setVisibility(View.GONE);
        rightBtn.setImageDrawable(getResources().getDrawable(resid));
        rightBtn.setBackground(null);
        rightBtn.setVisibility(View.VISIBLE);
    }

    public void setLeftBtn(int resid) {
        leftBtn.setBackground(null);
        leftBtn.setImageDrawable(getResources().getDrawable(resid));
    }
    private void initLayout() {
//        if (isDisplayActionBar()) {
//            getActionBar().show();
//        } else {
//            getActionBar().hide();
//        }

        leftBtn = (ImageView) findViewById(R.id.left_btn);
        leftCloseBtn = (ImageButton) findViewById(R.id.left_close_btn);
        rightBtn = (ImageButton) findViewById(R.id.right_btn);
        rightShareBtn = (ImageButton) findViewById(R.id.right_share_btn);
        rightText = (TextView) findViewById(R.id.right_text);
        titleView = (TextView) findViewById(R.id.title);
        windowHeadView = (RelativeLayout) findViewById(R.id.window_head);
        windowBodyLayout = (LinearLayout) findViewById(R.id.window_body);

        if (isDisplayTitle()) {
            windowHeadView.setVisibility(View.VISIBLE);
        } else {
            windowHeadView.setVisibility(View.GONE);
        }

        if (isDisplayPlayBar()) {

        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            windowBodyLayout.setLayoutParams(lp);

        }

        if (getContentView() != -1) {
            windowBodyLayout.addView(inflater.inflate(getContentView(), null),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }}
    protected void setTitle(String name) {
        titleView.setText(name);
    }

    protected abstract void doInit();

    protected abstract boolean isDisplayTitle();

    protected boolean isDisplayPlayBar() {
        return false;
    }

    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
