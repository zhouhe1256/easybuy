
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.remote.Http;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-14.
 */
public class PropertyModel implements Serializable {
    private String home_url;
    private String project_url;
    private String desc;
    private String maseage_url;
    private String tel;
    private String image_url;
    private String api_url;
    private String protocol_url;

    public String getProtocol_url() {
        return protocol_url;
    }

    public void setProtocol_url(String protocol_url) {
        this.protocol_url = protocol_url;
    }

    public String getHome_url() {
        return home_url;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProject_url() {
        return project_url;
    }

    public void setProject_url(String project_url) {
        this.project_url = project_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMaseage_url() {
        return maseage_url;
    }

    public void setMaseage_url(String maseage_url) {
        this.maseage_url = maseage_url;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public static IPromise getProperty() {
        return Http.instance().post(ApiUrl.GET_PROPERTY).isCache(false).
                run();
    }
}
