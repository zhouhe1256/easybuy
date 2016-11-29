
package com.gaiya.easybuy.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class HomeFragment extends Fragment implements View.OnClickListener {
    private WebViewClient webViewClient;
    private WebChromeClient webChromeClient;
    private WebView webview;
    public String url;
   // private ArrayList<String> urls = new ArrayList<String>();
    public String starturl;
    private TextView textView;
    private ImageView backImage;
    private LinearLayout linear;
    private boolean back;
    private boolean b;
    public boolean bc = true;
    private int h = 0;
    LinearLayout.LayoutParams layoutParams;
    Handler handler = new Handler();
    protected void doInit() {

        initView();
        initData();
        initEvent();
    }
    public void click(){
        webview.reload();
    }
    private void initData() {
        textView.setText("首页");
       // url = ApiUrl.HOST_HTML_URL;
        url= GApplication.getInstance().getPropertyModel().getHome_url();

        starturl = url;
        Bundle bundle = getArguments();
        if (bundle != null) {
            String urlBundle = bundle.getString("url");
            if (!urlBundle.isEmpty()) {
                url = urlBundle;
            }
        }
        if (url.startsWith(GApplication.getInstance().getPropertyModel().getHome_url())) {
            String token = PreferencesUtils.getString(getActivity(), PreferencesConstant.API_TOKEN,
                    "");
            String userid = PreferencesUtils.getString(getActivity(), PreferencesConstant.USER_ID,
                    "");
            if (!url.contains("?"))
                url += "?";
            if (token != null) {
                url += "&token=" + token;
                url += "&deviceType=1";
                Logger.i("tokens2",token+"");
            }
            if (userid != null) {
                url += "&userId=" + userid;
            }

        }
        Logger.i("shouyeff",url+"");
    }

    private void initEvent() {
        Logger.i("fff","1");
        backImage.setOnClickListener(this);
        webChromeClient = new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(title.equals("首页")){
                    bc = true;
                    backImage.setVisibility(View.GONE);
                }

                textView.setText(title);
            }

        };
        webViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (back) {
                    //webview.reload();
                    back = false;
                }

                /*String dd = "function showAlert() {alert(678);if(window.XXJSBridge) {return; }console.log(02020);}";
                webview.loadUrl("javascript:"+dd);//注入js函数，*/
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl("javascript:init()");//调用js函数
                    }
                }, 1000);
                Logger.i("isjs","true");
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
        };
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath()+"easybuy";
        Logger.i("file", "cacheDirPath="+cacheDirPath);
        //设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);
        webview.setWebChromeClient(webChromeClient);
        webview.setWebViewClient(webViewClient);
        webview.getSettings().setJavaScriptEnabled(true);
        // 用JavaScript调用Android函数：
        // 先建立桥梁类，将要调用的Android代码写入桥梁类的public函数
        // 绑定桥梁类和WebView中运行的JavaScript代码
        // 将一个对象起一个别名传入，在JS代码中用这个别名代替这个对象
        webview.addJavascriptInterface(new WebAppInterface(getActivity(), webview), "nmd");
        //urls.add(url);
        Logger.i("jas",url);
        webview.loadUrl(url);

        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        final int height = wm.getDefaultDisplay().getHeight();
        linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                linear.getWindowVisibleDisplayFrame(r);
                int screenHeight = linear.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                //Logger.e("Keyboard Size", "Size:"  +     heightDifference);
                if(heightDifference<100){
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.FILL_PARENT);
                    webview.setLayoutParams(layoutParams);
                }else{
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            height-heightDifference-30);
                    webview.setLayoutParams(layoutParams);
                }

            }
        });
    }

    private void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;
       /* getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/
        rootView = inflater.inflate(R.layout.fragment_home, null);
        textView = ViewUtil.findViewById(rootView, R.id.web_title);
        webview = ViewUtil.findViewById(rootView, R.id.webview);
        backImage = ViewUtil.findViewById(rootView, R.id.left_btn);
        linear = ViewUtil.findViewById(rootView,R.id.linear);
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
            // Logger.i("sizess","urls.size()="+urls.size());

            if(textView.getText().equals("项目列表")){
                webview.loadUrl(url);
            }else {
                webview.goBack();
            }
            b = true;
            back = true;
            if (url.startsWith(starturl)){
                //urls.clear();
                //backImage.setVisibility(View.GONE);
                b = false;
               // bc = true;
            }
        }

        //urls.remove(urls.size()-1);
    }
    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.i("usv",url+"d");
            if (url.startsWith(starturl)) {
                backImage.setVisibility(View.GONE);
               // urls.clear();
            } else {
                backImage.setVisibility(View.VISIBLE);
                bc = false;
                /*if(!b){
                    urls.add(url);
                }*/
            }
            if (url.startsWith(GApplication.getInstance().getPropertyModel().getHome_url())) {
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
