
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
import com.gaiya.easybuy.tabView.TabNavigateLayout.OnItemSelectedListener;

public class MainActivity extends FragmentActivity implements OnItemSelectedListener {
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    private MessageFragment messageFragment;
    private ProjectFragment projectFragment;
    private int page = 0;
    public static int PAGE_F = 0;
    private PushModel pushModel;
    private TabNavigateLayout navigateLayout;
    private String url;
    private String action;
    private boolean flag;
    private boolean f = false;
    private boolean f_2 = false;
    private boolean s = true;
    private boolean s1 = false;
    private boolean s2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GApplication.getInstance().addActivity(this);
        navigateLayout = (TabNavigateLayout) findViewById(R.id.tabNavigateLayout1);
        navigateLayout.setOnItemSelectedListener(this);
        pushModel = (PushModel) getIntent().getSerializableExtra("push");
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment).commit();
        if (pushModel != null) {
            pushNoticeIntent(pushModel);
        }else{
            navigateLayout.getChildView(0);
            navigateLayout.setSelectedPos(PAGE_F);
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

    @Override
    public void OnItemSelected(View v, int position) {
        page = position;
        Bundle bundle = new Bundle();
        if (pushModel != null) {
            bundle.putSerializable("url", pushModel.getUrl());
        }
        switch (position) {
            case 0:
                if(s){
                    homeFragment.click();
                }

                f_2 = false;
                f = false;
                ProjectFragment.b = false;
                s = true;
                s1 = false;
                s2 = false;
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, homeFragment).commit();

                break;
            case 1:
                if(s1){
                    if(f){
                        projectFragment.click();
                    }
                }

                s = false;
                s1 = true;
                s2 = false;
                f_2 = false;
                if (projectFragment == null) {
                    projectFragment = new ProjectFragment();
                }

                if (pushModel != null && "1".equals(pushModel.getType()) && flag == true) {
                    flag = false;
                    projectFragment.setArguments(bundle);

                }else{
                    //projectFragment.setArguments(null);
                   // projectFragment.click();
                }

                f = true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, projectFragment).commit();


                break;
            case 2:
                if(s2){
                    if(f_2){
                        messageFragment.click();
                    }
                }
                s = false;
                s1 = false;
                s2 = true;
                f = false;
                ProjectFragment.b = false;
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                }
                //messageFragment.click();
                if (pushModel != null && "2".equals(pushModel.getType()) && flag == true) {
                    flag = false;
                    messageFragment.setArguments(bundle);
                }else{
                   // messageFragment.setArguments(null);
                }
                f_2 = true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, messageFragment).commit();


                break;
            case 3:
                s = false;
                s1 = false;
                s2 = false;
                f_2 = false;
                f = false;
                ProjectFragment.b = false;
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, meFragment)
                        .commit();

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
                    finish();
                    System.exit(0);
                }
            }

        return super.onKeyDown(keyCode, event);
    }
}
