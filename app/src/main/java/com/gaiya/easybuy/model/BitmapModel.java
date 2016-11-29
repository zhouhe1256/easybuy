
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.remote.Http;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-9.
 */
public class BitmapModel implements Serializable {
    private String uri;
    private String id;

//    public String getUri() {
//        return uri;
//    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static IPromise imageCom(String businessLicenceImageId) {
        return Http.instance().post(ApiUrl.CHANGE_COMPANY_INFO).
                param("businessLicenceImageId", businessLicenceImageId).
                run();
    }

    public static IPromise imageUser(String imageId) {
        return Http.instance().post(ApiUrl.CHANGE_USER_INFO)
                .param("imageId", imageId).
                run();
    }

    public static IPromise imageGet() {
        return Http.instance().post(ApiUrl.GET_COMPANY_IMG).
               // param("id", id).
                run();
    }
}
