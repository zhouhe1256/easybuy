
package com.gaiya.easybuy.constant;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.util.SystemUtil;

/**
 * Created by dengt on 15-4-20.
 */
public class ApiUrl {
    public static final String VERSION =
            SystemUtil.getCurrentVersionName(GApplication
                    .getInstance());
    public static final String HOST_URL =
            GApplication.getInstance().getResources()
                    .getString(R.string.host);
//    public static final String HOST_IMG_URL =
//            GApplication.getInstance().getResources()
//                    .getString(R.string.host_img);
//    public static final String HOST_HTML_URL =
//            GApplication.getInstance().getResources()
//                    .getString(R.string.host_html);
    // 192.168.1.54 192.168.1.22:8080
    public static final String OS = SystemUtil.getVersion();

    public static final String REGISTER = "/api/user/register";// 注册用户(POST
                                                               // /api/user/register)
    public static final String USER_LOGIN = "/api/user/logon";// 登录(POST
                                                              // /api/login)
    public static final String USER_INFO = "/api/user/getUserInfo";// 获取当前用户信息(GET
                                                       // /api/user)
    public static final String SEND_CHECK_CODE = "/api/comm/sms";// 发送验证码(POST

    public static final String VERIFY_CHECK_CODE = "/api/user/verify_check_code";// 检查验证码是否正确(POST
                                                                                 // /api/user/verify_check_code)
    public static final String CHANGE_PASSWORD = "/api/user/modifyPassword";// 修改密码(PUT
                                                                             // /api/user/change_password)
public static final String CHANGE_PHONE= "/api/user/changePhone";//变更手机号
    public static final String RESET_PASSWORD = "api/user/findPassword";    // 修改密码(PUT
                                                                           // /api/user/reset_password)
    public static final String CHANGE_USER_INFO = "/api/user/editUserInfo";// 更新用户信息(PUT
                                                              // /api/user)

    public static final String GETUI_PUSH = "api/user/editPushId";// 更新个推ID
    // /api/user)
    public static final String SET_AVATAR = "/api/comm/uploadImage";// 图片上传接口
                                                                   // /api/user/set_avatar)
    public static final String USER_SEARCH = "/api/user/search";// 根据用户手机号搜索用户(POST
                                                                // /api/user/search)
 public static final String COMPANY_INFO = "/api/company/getCompanyInfo";// 获取公司信息
    public static final String CHANGE_COMPANY_INFO = "/api/company/editCompanyInfo";//修改企业信息
    public static final String ADD_CATALOG = "/api/company/addCompanyCatalog";//主营业务新增信息
    public static final String DELETE_CATALOG = "/api/company/delCompanyCatalog";//删除公司主营业务信息
    public static final String COMMIT_COMPANY = "/api/company/companyAudit";// 提交审核
    public static final String GET_COMPANY = "/api/catalog/getCatalogData";// 取主营业务数据
    public static final String GET_COMPANY_IMG = "/api/comm/findImage";//

    public static final String GET_PROPERTY = "/api/comm/property";// config键值获取接口
    public static final String USER_CONTACT_LIST_INVITE = "/api/user/invite";// 获取邀请用户列表(GET
                                                                             // /api/user/invite)

    public static final String MY_MESSAGE = "/api/user/message"; // 我的消息(GET
                                                                 // /api/user/message)

    public static final String FEED_BACK = "/api/comm/feedback"; // 用户反馈(POST
                                                                 // /api/user/feedback)

    public static final String SOFT_UPDATE = "/api/comm/update"; // GET /comm/update  没有额外参数

    public static final String IS_REGISTER_PHONE = "api/user/checkPhoneIsRegister";// 检查手机号码是否注册()
    // 产品列表(GET /api/golf_courses/:id/products)
    public static String productList(Long id) {
        return "/api/golf_courses/" + id + "/products";
    }


}
