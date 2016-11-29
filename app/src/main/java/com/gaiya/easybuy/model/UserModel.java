
package com.gaiya.easybuy.model;

import com.gaiya.android.async.IPromise;
import com.gaiya.android.remote.Http;
import com.gaiya.android.remote.IContentDecoder;
import com.gaiya.easybuy.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-4-30.
 */
public class UserModel implements Serializable {
    private String position;
    private String birthday;
    private String phone;
    private String sex;
    private String status;
    private String education;
    private String entryDate;
    private String companyName;
    private String graduateInstitutions;
    private String updatedAt;
    private String id;
    private boolean vip;
    private String imageId;
    private String graduateDate;
    private String email;
    private String nickName;
    private String createdAt;
    private String name;
    private String userId;
    private String companyId;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGraduateInstitutions() {
        return graduateInstitutions;
    }

    public void setGraduateInstitutions(String graduateInstitutions) {
        this.graduateInstitutions = graduateInstitutions;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getGraduateDate() {
        return graduateDate;
    }

    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    private static IContentDecoder<UserModel> decoder = new IContentDecoder.BeanDecoder<UserModel>(
            UserModel.class, "result");

    public static IPromise register(String mobileNo, String password, String code) {
        return Http.instance().post(ApiUrl.REGISTER).
                param("mobile", mobileNo).param("password", password)
                .param("message", code)
                .run();
    }

    public static IPromise verifyCheckCode(String phone, String code, String type) {
        return Http.instance().post(ApiUrl.VERIFY_CHECK_CODE).
                param("phone", phone).param("code", code).param("type", type).run();
    }

    public static IPromise sendCheckCode(String mobile, String authType) {
        return Http.instance().post(ApiUrl.SEND_CHECK_CODE).
                param("mobile", mobile).param("authType", authType).run();
    }

    public static IPromise login(String user, String password, String deviceIdent) {
        return Http.instance().post(ApiUrl.USER_LOGIN).
                param("mobile", user).param("password", password).param("deviceIdent", deviceIdent)
                .run();
    }

    public static IPromise get() {
        return Http.instance().post(ApiUrl.USER_INFO).isCache(true).
                run();
    }

    // 修改密码
    public static IPromise changePassword(String oldPassword, String newPassword) {
        return Http.instance().post(ApiUrl.CHANGE_PASSWORD).
                param("oldPassword", oldPassword).param("newPassword", newPassword).run();
    }

    // 变更手机号
    public static IPromise changephone(String mobile, String message) {
        return Http.instance().post(ApiUrl.CHANGE_PHONE).
                param("mobile", mobile).param("message", message).run();
    }

    // 重置密码
    public static IPromise resetPassword(String mobileNumber, String password, String code) {
        return Http.instance().post(ApiUrl.RESET_PASSWORD).
                param("password", password).param("mobile", mobileNumber).param("message", code)
                .run();
    }

    // 更新用户信息
    public static IPromise updateUserInfo(String name, String nickname
            , String sex, String imageId, String birthDay, String position, String entryDate) {
        return Http.instance().post(ApiUrl.CHANGE_USER_INFO).
                param("name", name).param("nickName", nickname).
                param("sex", sex)
                .param("imageId", imageId).
                param("birthday", birthDay).
                param("position", position).
                param("entryDate", entryDate).
                run();
    }

    // 更新个推信息
    public static IPromise updatepushClientId(String pushClientId) {
        return Http.instance().post(ApiUrl.GETUI_PUSH).
                param("pushClientId", pushClientId).run();
    }

    // 设置用户头像
    public static IPromise setAvatar(byte[] data) {
        return Http.instance().post(ApiUrl.SET_AVATAR).
                data(data).run();
    }

    // 用户反馈(POST /api/user/feedback)
    public static IPromise feedBack(String mobile, String content) {
        return Http.instance().post(ApiUrl.FEED_BACK).
                param("mobile", mobile).param("description", content).run();
    }
}
