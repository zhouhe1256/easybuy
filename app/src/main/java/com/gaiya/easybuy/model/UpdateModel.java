
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.remote.Http;
import com.gaiya.android.remote.IContentDecoder;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-1-4.
 */
public class UpdateModel implements Serializable {

    private double version;
    private String url;
    private String description;
    private double minVersion;

    public double getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(double minVersion) {
        this.minVersion = minVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private static IContentDecoder<UpdateModel> decoder = new IContentDecoder.BeanDecoder<UpdateModel>(
            UpdateModel.class, "version");

    public static IPromise sendVersion() {
        return Http.instance().get(ApiUrl.SOFT_UPDATE).
               run();
    }
}
