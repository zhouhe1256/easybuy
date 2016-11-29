
package com.gaiya.easybuy.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.async.LooperCallbackExecutor;
import com.gaiya.android.cache.ChainedCache;
import com.gaiya.android.cache.DiskCache;
import com.gaiya.android.cache.MemoryCache;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.remote.Http;
import com.gaiya.android.remote.HttpOption;
import com.gaiya.android.remote.IContentDecoder;
import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.constant.ApiUrl;
import com.gaiya.easybuy.model.PropertyModel;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by dengt on 15-4-20.
 */
public class GApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static GApplication gApplication;
    private static volatile boolean httpInited;
    public static  boolean selection = false;
    private static File baseDir;
    private UserModel user;
    private PropertyModel propertyModel;
    private Context Gcontext;
    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    private boolean flag = true;

    public UserModel getUser() {
        if (user == null) {
            String userJson = PreferencesUtils.getString(this,
                    PreferencesConstant.USER_INFO, "");
            if (userJson.isEmpty()) {
                return null;
            }
            user = JSONUtil.load(UserModel.class, userJson);
        }
        return user;
    }

    public PropertyModel getPropertyModel() {
        if (propertyModel == null) {
            String pro = PreferencesUtils.getString(this,
                    PreferencesConstant.PROPERTY_INFO, "");
            if (pro.isEmpty()) {
                return null;
            }
            propertyModel = JSONUtil.load(PropertyModel.class, pro);
        }
        return propertyModel;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    // public void setUser(UserModel user) {
    // this.user = user;
    // PreferencesUtils.putString(this, PreferencesConstant.USER_INFO,
    // JSONUtil.dump(user));
    // }
    public void setPropertyModel(PropertyModel propertyModel) {
        this.propertyModel = propertyModel;
        PreferencesUtils.putString(this, PreferencesConstant.PROPERTY_INFO,
                JSONUtil.dump(propertyModel));
        Http.instance().option(HttpOption.BASE_URL, propertyModel.getApi_url());
       // Http.instance().option(HttpOption.BASE_URL, "http://192.168.23.1:8080");
        Http.imageInstance().baseUrl(propertyModel.getImage_url());
    }

    // public boolean isUserLogin() {
    // return user != null;
    // }

    @Override
    public void onCreate() {
        super.onCreate();
        gApplication = this;
        Logger.setDebug(true);
        initHttp(this);
    }

    public static GApplication getInstance() {
        PushManager.getInstance().initialize(gApplication.getApplicationContext());
        // Logger.e("d",
        // PushManager.getInstance().getClientid(gApplication).toString()) ;
        return gApplication;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }

    // public void closeDialog() {
    // if (mView.getParent() != null) {
    // flag = false;
    // // if (dialogReceiver != null) {
    // // mView.getContext().unregisterReceiver(dialogReceiver);
    // // }
    // wm.removeView(mView);
    // }
    // }
    //
    // DialogReceiver dialogReceiver;

    public synchronized void initHttp(Context context) {
        if (httpInited) {
            return;
        }
        this.Gcontext = context;
        // mView = LayoutInflater.from(context).inflate(
        // R.layout.layout_my_dialog, null);
        // mView = new NetAccessView(context);
        // if (dialogReceiver == null) {
        // dialogReceiver = new DialogReceiver();
        // }

        httpInited = true;

        baseDir = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();
        baseDir = new File(baseDir, "easybuy");
        DiskCache.setBaseDir(baseDir);
        String token = getApiToken();
        String userid = getUserID();
        DiskCache<String, byte[]> apiCache = new DiskCache<String, byte[]>("api", new DiskCache.ByteArraySerialization());
        Http.instance().option(HttpOption.BASE_URL, ApiUrl.HOST_URL).
                option(HttpOption.MIME, "application/json").
                param("token", token).param("deviceType", "1").
                param("userId", userid).
                option(HttpOption.CONNECT_TIMEOUT, 10000).
                option(HttpOption.READ_TIMEOUT, 10000).
                // option(HttpOption.X_Token, token).
                // option(HttpOption.X_Version, ApiUrl.VERSION).
                // option(HttpOption.X_OS, userid).
                setContentDecoder(new IContentDecoder.JSONDecoder()).
                cache(apiCache).fallbackToCache(true).
                always(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        Object object = arguments.get(0);

                        if (object instanceof Throwable) {
                            Throwable t = (Throwable) object;
                            StringWriter writer = new StringWriter();
                            writer.write(t.getMessage() + "\n");
                            t.printStackTrace(new PrintWriter(writer));
                            String s = writer.toString();
                            System.out.println(s);
                            return;
                        }

                        if (!(object instanceof JSONObject)) {
                            return;
                        }
                        JSONObject json = arguments.get(0);
                        if (!json.optBoolean("success")) {
                            String errorMessage = json.optString("message");
                            int code = json.optInt("code");
                            if (code == 13005) {
                            } else {
                                // if (!StringUtils.isEmpty(errorMessage))
                                // DialogUtil.showMessage(errorMessage);
                                // else {
                                // DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                // }
                            }
                        }
                    }
                }).start(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        // if (flag == false) {
                        // flag = true;
                        // IntentFilter homeFilter = new IntentFilter(
                        // Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                        // mView.getContext().registerReceiver(dialogReceiver,
                        // homeFilter);
                        // if (mView.getParent() == null)
                        // wm.addView(mView, para);
                        // }
                    }
                }).complete(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        // if (mView.getParent() != null) {
                        // flag = false;
                        // if (dialogReceiver != null) {
                        // mView.getContext().unregisterReceiver(dialogReceiver);
                        // }
                        // wm.removeView(mView);
                        // }
                    }
                }).
                callbackExecutor(new LooperCallbackExecutor()).
                fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                    }
                });
        ChainedCache<String, byte[]> imageCache = ChainedCache.create(
                400 * 1024 * 1024, new MemoryCache.ByteArraySizer<String>(),
                "images", new DiskCache.ByteArraySerialization<String>()
                );
        Http.imageInstance().cache(imageCache).baseUrl(ApiUrl.HOST_URL).
                aheadReadInCache(true);
    }

    public void updateApiToken() {
        String token = PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN);
        Http.instance().param("token", token)/*
                                              * .option(HttpOption.X_Token,
                                              * token)
                                              */;
    }

    public String getIMEI() {
        try {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        } catch (Exception ex) {
        }
        return null;
    }

    public void updateUserID() {
        String userid = PreferencesUtils.getString(gApplication, PreferencesConstant.USER_ID);
        Http.instance().param("userId", userid)/*
                                                * .option(HttpOption.X_OS,
                                                * userid)
                                                */;
    }

    public boolean isLogin() {
        if (getApiToken() == null || getApiToken() == "")
            return false;
        else if (getApiToken().length() > 1)
            return true;
        else
            return false;
    }

    private boolean isPushID;

    public void setPushID(boolean isPushID) {
        this.isPushID = isPushID;
    }

    public boolean isPushID() {
        return isPushID;
    }

    public String getApiToken() {
        return PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN, "");
    }

    public String getUserID() {
        return PreferencesUtils.getString(gApplication, PreferencesConstant.USER_ID, "");
    }
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void exit(){
        for(Activity activity:activityList ){
            activity.finish();
        }
        System. exit(0);
    }
}
