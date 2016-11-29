package com.gaiya.easybuy.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.fragment.HomeFragment;
import com.gaiya.easybuy.fragment.MeFragment;
import com.gaiya.easybuy.fragment.MessageFragment;
import com.gaiya.easybuy.fragment.ProjectFragment;
import com.gaiya.easybuy.model.PushModel;
import com.gaiya.easybuy.tabView.TabNavigateLayout;
import com.gaiya.easybuy.util.ClearWebview;

public class TabActivity extends FragmentActivity implements TabNavigateLayout.OnItemSelectedListener {
    private TabNavigateLayout navigateLayout;
    private PushModel pushModel;
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    private MessageFragment messageFragment;
    private ProjectFragment projectFragment;
    public static int PAGE_F = 0;
    private int page = 0;
    private boolean flag;
    private boolean s = false;
    private boolean s1 = false;
    private boolean s2 = false;
    /**
     * 第一次点击不刷新
     */
    private boolean click_1 = true;
    private boolean click_2 = true;
    private boolean click_3 = true;

    private boolean click_s2 = false;
    private boolean click_s3 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GApplication.getInstance().addActivity(this);
        navigateLayout = (TabNavigateLayout) findViewById(R.id.tabNavigateLayout1);
        initTab();
        navigateLayout.setOnItemSelectedListener(this);
        pushModel = (PushModel) getIntent().getSerializableExtra("push");

        if (pushModel != null) {
            pushNoticeIntent(pushModel);
        }else{
            navigateLayout.getChildView(0);
            navigateLayout.setSelectedPos(PAGE_F);
        }
    }



    private void initTab() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (projectFragment == null) {
            projectFragment = new ProjectFragment();
        }
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
        }
        if (meFragment == null) {
            meFragment = new MeFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment, "1")
                .add(R.id.container, projectFragment, "2")
                .add(R.id.container, messageFragment, "3")
                .add(R.id.container, meFragment, "4")
                .commit();

    }
    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(homeFragment)
                .hide(projectFragment)
                .hide(messageFragment)
                .hide(meFragment)
                .commit();
    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(projectFragment)
                .hide(homeFragment)
                .hide(messageFragment)
                .hide(meFragment)
                .commit();
    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(messageFragment)
                .hide(projectFragment)
                .hide(homeFragment)
                .hide(meFragment)
                .commit();
    }
    /**
     * 点击第四个tab
     */
    private void clickTab4Layout() {
        getSupportFragmentManager().beginTransaction()
                .show(meFragment)
                .hide(projectFragment)
                .hide(messageFragment)
                .hide(homeFragment)
                .commit();
    }
    @Override
    public void OnItemSelected(View v, int position) {
        page = position;
        Bundle bundle = new Bundle();
        if (pushModel != null) {
            bundle.putSerializable("url", pushModel.getUrl());
        }
        switch (position) {
            case 0:
                if(click_1) {
                    if (GApplication.selection) {
                        homeFragment.click();
                    }
                }
                GApplication.selection = true;

                ProjectFragment.b = false;
                s = true;
                s1 = false;
                s2 = false;
                if(click_s2&&click_s3){
                    click_1 = s;
                    click_2 = s1;
                    click_3 = s2;
                }else if(click_s2){
                    click_1 = s;
                    click_2 = s1;
                }else if(click_s3){
                    click_1 = s;
                    click_3 = s2;
                }else{
                    click_1 = s;
                }
                clickTab1Layout();
                break;
            case 1:
                if(click_2){
                    projectFragment.click();
                }
                s = false;
                s1 = true;
                s2 = false;

                if (pushModel != null && "1".equals(pushModel.getType()) && flag == true) {
                    flag = false;
                    projectFragment.setArguments(bundle);

                }else{
                    //projectFragment.setArguments(null);
                    // projectFragment.click();
                }
                click_s2 = true;
                if(click_s2&&click_s3){
                    click_1 = s;
                    click_2 = s1;
                    click_3 = s2;
                }else{
                    click_1 = s;
                    click_2 = s1;
                }
                clickTab2Layout();
                break;
            case 2:
                if(click_3){
                    messageFragment.click();
                }
                s = false;
                s1 = false;
                s2 = true;

                ProjectFragment.b = false;
                if (pushModel != null && "2".equals(pushModel.getType()) && flag == true) {
                    flag = false;
                    messageFragment.setArguments(bundle);
                }else{
                    // messageFragment.setArguments(null);
                }
                click_s3 = true;
                if(click_s2&&click_s3){
                    click_1 = s;
                    click_2 = s1;
                    click_3 = s2;
                }else{
                    click_1 = s;
                    click_3 = s2;
                }
                clickTab3Layout();
                break;
            case 3:
                s = false;
                s1 = false;
                s2 = false;

                ProjectFragment.b = false;
                if(click_s2&&click_s3){
                    click_1 = s;
                    click_2 = s1;
                    click_3 = s2;
                }else if(click_s2){
                    click_1 = s;
                    click_2 = s1;
                }else if(click_s3){
                    click_1 = s;
                    click_3 = s2;
                }else{
                    click_1 = s;
                }
                clickTab4Layout();
                break;
        }
    }


    public void doClick(View v) {
        switch (page) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                homeFragment.doClick(v);
                break;
            case 1:
                if (projectFragment == null) {
                    projectFragment = new ProjectFragment();
                }
                projectFragment.doClick(v);
                break;
            case 2:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                }
                messageFragment.doClick(v);
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                meFragment.doClick(v);
                break;
        }
    }
    private void pushNoticeIntent(PushModel pushModel) {

        if ("1".equals(pushModel.getType())) {
            flag = true;
            navigateLayout.setSelectedPos(1);
        } else if ("2".equals(pushModel.getType())) {
            flag = true;
            navigateLayout.setSelectedPos(2);
        } else {
            navigateLayout.setSelectedPos(PAGE_F);
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(s){
            if(homeFragment.bc){
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                        System.exit(0);
                    }
                }
            }else {
                homeFragment.back();
            }

            return true;
        }
        if(s1){
            if(projectFragment.bc){
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                        System.exit(0);
                    }
                }
            }else {
                projectFragment.back();
            }

            return true;
        }
        if(s2){
            if(messageFragment.bc){
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                        System.exit(0);
                    }
                }
            }else {
                messageFragment.back();
            }

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ClearWebview.clearWebViewCache(this,"easybuy");
                ClearWebview.clearWebViewCache(this,"easybuy_1");
                ClearWebview.clearWebViewCache(this,"easybuy_2");
                finish();
                System.exit(0);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
