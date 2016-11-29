
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.json.annotation.JSONCollection;
import com.gaiya.android.remote.Http;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-10-8.
 */
public class CompanyModel implements Serializable {
    /*
     * result":{" "foundingDate":"", "companySize":"", "quoted":false,
     * "registeredCapital":"", "corporation":"", "discount":0,
     * "updatedAt":1444288893000, "area":"", "address":"",
     * "createdAt":1444288893000, , }}
     */
    private String name;
    private String contactNumber;
    private String website;
    private String businessLicenceImageId;
    private String status;// status`
                          // enum('ACTIVE','INACTIVE','PENDING','CLOSED','DRAFT')
                          // DEFAULT 'DRAFT' COMMENT 'label: 状态, ACTIVE: 正常,
                          // INACTIVE: 冻结, PENDING: 待审核, CLOSED: 已关闭, DRAFT:
                          // 草稿',
    private String id;
    private String linkman;
    private String description;
    private String email;
    private String type;// SUPPLIER: 供应商, AGENT: 代理商, BUYER: 采购商'
    private String imageUrl;

    @JSONCollection(type = CataLogModel.class)
    private List<CataLogModel> catalogList;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessLicenceImageId() {
        return businessLicenceImageId;
    }

    public void setBusinessLicenceImageId(String businessLicenceImageId) {
        this.businessLicenceImageId = businessLicenceImageId;
    }

    public List<CataLogModel> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<CataLogModel> catalogList) {
        this.catalogList = catalogList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static IPromise get() {
        return Http.instance().post(ApiUrl.COMPANY_INFO).
                run();
    }

    public static IPromise change(String type, String name, String description, String website,
            String businessLicenceImageId, String contactNumber, String linkman, String email/*
                                                                                              * ,
                                                                                              * String
                                                                                              * id
                                                                                              */) {
        return Http.instance().post(ApiUrl.CHANGE_COMPANY_INFO).
                param("name", name).param("type", type).param("description", description).
                param("website", website).param("businessLicenceImageId", businessLicenceImageId).
                param("contactNumber", contactNumber).param("linkman", linkman).
                param("email", email)/* .param("id", id) */.
                run();
    }

    public static IPromise getCatalog(String id) {
        return Http.instance().post(ApiUrl.GET_COMPANY).
                param("pid", id).
                run();
    }

    public static IPromise commit() {
        return Http.instance().post(ApiUrl.COMMIT_COMPANY).
                run();
    }
}
