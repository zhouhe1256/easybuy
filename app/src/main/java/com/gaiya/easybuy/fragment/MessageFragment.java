package com.gaiya.easybuy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.ViewUtil;
import com.gaiya.easybuy.util.WebAppInterface;

/**
 * Created by dengt on 15-9-24.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {
    private WebViewClient webViewClient;
    private WebChromeClient webChromeClient;
    private WebView webview;
    private String url;
    private String starturl;
    private TextView textView;
    private ImageView backImage;
    private boolean back;
    public  boolean bc = true;
    protected void doInit() {

        initView();
        initData();
        initEvent();
    }
    public void click(){
        webview.reload();
    }
    private void initData() {
        textView.setText("消息");
       // url = ApiUrl.HOST_HTML_URL;
        url= GApplication.getInstance().getPropertyModel().getMaseage_url();
        starturl = url;
        Bundle bundle = getArguments();
        if (bundle != null) {
            String urlBundle = bundle.getString("url");
            if (!urlBundle.isEmpty()) {
                url = urlBundle;
            }
        }
        if (url.startsWith(GApplication.getInstance().getPropertyModel().getMaseage_url())) {
            String token = PreferencesUtils.getString(getActivity(), PreferencesConstant.API_TOKEN,
                    "");
            String userid = PreferencesUtils.getString(getActivity(), PreferencesConstant.USER_ID,
                    "");
            if (!url.contains("?"))
                url += "?";
            if (token != null) {
                url += "&token=" + token;
                url += "&deviceType=1";
            }
            if (userid != null) {
                url += "&userId=" + userid;
            }

        }
    }

    private void initEvent() {
        backImage.setOnClickListener(this);
        webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(title.equals("消息")){
                    bc = true;
                }
                textView.setText(title);
            }

        };
        webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (back) {
                    webview.reload();
                    back = false;
                }
                webview.loadUrl("javascript:init()");//调用js函数
                Logger.i("isjs","true3");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(starturl)) {
                    backImage.setVisibility(View.GONE);
                } else {
                    backImage.setVisibility(View.VISIBLE);
                }
                if (url.startsWith("tel:")) {// 拨打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        };
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath()+"easybuy_2";
        Logger.i("file", "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);
        webview.setWebChromeClient(webChromeClient);
        webview.setWebViewClient(webViewClient);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(getActivity(), webview), "nmd");
        webview.loadUrl(url);
    }

    private void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_home, null);
        textView = ViewUtil.findViewById(rootView, R.id.web_title);
        webview = ViewUtil.findViewById(rootView, R.id.webview);
        backImage = ViewUtil.findViewById(rootView, R.id.left_btn);
        doInit();
        return rootView;
    }

    public void doClick(View v) {
        if (url == null) {
            // finish();
            return;
        }
        url = url.replaceAll("\r|\n", "");
        String weburl = webview.getUrl();
        if (url.equals(weburl)) {
            // finish();
        } else {
            webview.goBack(); // goBack()表示返回WebView的上一页面
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                back();
                break;
        }
    }
    public void back(){
        if (url == null) {
            // finish();
            return;
        }
        url = url.replaceAll("\r|\n", "");
        String weburl = webview.getUrl();
        if (url.equals(weburl)) {
            // finish();
            // backImage.setVisibility(View.GONE);
        } else {
            // backImage.setVisibility(View.VISIBLE);
            webview.goBack(); // goBack()表示返回WebView的上一页面
            back = true;
            if (url.startsWith(starturl))
                backImage.setVisibility(View.GONE);
            bc = true;
        }
    }
    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(starturl)) {
                backImage.setVisibility(View.GONE);
            } else {
                backImage.setVisibility(View.VISIBLE);
                bc = false;
            }
            if (url.startsWith(GApplication.getInstance().getPropertyModel().getMaseage_url())) {
                String token = PreferencesUtils.getString(getActivity(),
                        PreferencesConstant.API_TOKEN,
                        "");
                String userid = PreferencesUtils.getString(getActivity(),
                        PreferencesConstant.USER_ID,
                        "");
                if (!url.contains("?"))
                    url += "?";
                if (token != null) {
                    url += "&token=" + token;
                    url += "&deviceType=1";
                }
                if (userid != null) {
                    url += "&userId=" + userid;
                }
            }

            view.loadUrl(url);
            return true;
        }
    }
}
