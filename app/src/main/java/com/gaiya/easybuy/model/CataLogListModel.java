
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.json.annotation.JSONCollection;
import com.gaiya.android.remote.Http;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-10-9.
 */
public class CataLogListModel implements Serializable {
    @JSONCollection(type = CataLogModel.class)
    private List<CataLogModel> result;

    public List<CataLogModel> getResult() {
        return result;
    }

    public void setResult(List<CataLogModel> result) {
        this.result = result;
    }

    public static IPromise getCatalog(String id) {
        return Http.instance().post(ApiUrl.GET_COMPANY).
                param("pid", id).
                run();
    }

    public static IPromise addCatalog(long catalogId) {
        return Http.instance().post(ApiUrl.ADD_CATALOG).
                param("catalogId", catalogId).
                run();
    }

    public static IPromise delCatalog(long id) {
        return Http.instance().post(ApiUrl.DELETE_CATALOG).
                param("id", id).
                run();
    }
}
