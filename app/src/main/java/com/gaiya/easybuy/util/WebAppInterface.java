
package com.gaiya.easybuy.util;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.gaiya.easybuy.application.GApplication;

/**
 * Created by dengt on 14-12-15.
 */
public class WebAppInterface {
    private GApplication gApplication;
    private Context mContext;
    private WebView mWebview;

    /**
     * 初始化，用于JS调用android函数
     * 
     * @param c context
     * @param webview webview
     */
    public WebAppInterface(Context c, WebView webview) {
        mContext = c;

        gApplication = GApplication.getInstance();
        mWebview = webview;

    }

    /**
     * Show a toast from the web page
     */
    // 如果target 大于等于API 17，则需要加上如下注解
    @JavascriptInterface
    public void showToast(String toast) {
        // Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }

    /**
     * js调用修改token
     */
    @JavascriptInterface
    public void updateToken(String token) {
        PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
        gApplication.updateApiToken();
    }

    @JavascriptInterface
    public String getToken() {
        return PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN, "");
    }

    @JavascriptInterface
    public String getUserId() {
        return PreferencesUtils.getString(gApplication, PreferencesConstant.USER_ID, "");
    }
}
