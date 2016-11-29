
package com.gaiya.easybuy.constant;

/**
 * -1 系统内部错误 1 参数错误 10000 用户未登陆 10001 用户不存在 10002 账号已被冻结 10003 无效的api token
 * 10004 用户名密码不匹配 10005 原密码错误 10006 账号已被占用 10007 手机号码格式不正确 10008 验证码错误 10009
 * 邀请码不存在 10010 验证码已发送，重新发送需要等待60秒 11001 没有足够的名额完成该交易 12001 赛事不存在 12002 已参与过该赛事
 * 12003 赛事已下线 12004 赛事名额已满 13001 订单已经取消 13002 订单状态错误 13003 处理中的订单不能删除 13004
 * 订单未支付成功 14001 兑换目标不存在 14002 物品转让失败 14003 兑换条件尚未满足 14004 赠送用户已被冻结 14005
 * 参与条件尚未满足 15001 反馈内容不能为空
 */
public enum ErrorCode {

    ERROR_CODE_000000("000000", "数据处理成功"),
    ERROR_CODE_000001("000001", "数据格式异常"),
    ERROR_CODE_000002("000002", "服务端错误"),
    ERROR_CODE_000003("000003", "数据不存在"),
    ERROR_CODE_000004("000004", "手机号未注册"),
    ERROR_CODE_000005("000005", "旧密码不正确"),

    ERROR_CODE_800000("800000", "手机号不符合规则"),

    ERROR_CODE_100000("100000", "手机号码已注册"),
    ERROR_CODE_100001("100001", "未注册的手机号不能修改密码"),
    ERROR_CODE_100002("100002", "用户在其它设备登录，请检查密码是否泄露"),
    ERROR_CODE_100003("100003", "此福包已经被使用"),
    ERROR_CODE_100004("100004", "验证码已过期"),
    ERROR_CODE_100005("100005", "验证码不正确"),
    ERROR_CODE_100006("100006", "用户名或密码错误"),
    ERROR_CODE_100007("100007", "用户绑定的设备超过了指定值"),
    ERROR_CODE_100008("100008", "手机号未曾注册"),
    ERROR_CODE_100009("100009", "令牌不合法，或令牌超时"),
    ERROR_CODE_100010("100010", "请不要在短时间内请求验证码"),
    ERROR_CODE_100011("300000" , "未登录"),
    ERROR_CODE_100012("900000" , "超出限额"),
    ERROR_CODE_100013("200001","您已经是最新版本")	;

    private String code;
    private String codeName;

    private ErrorCode(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public static boolean getrequestSuc(String code) {
        if (getCodeName(code).equals("000000")) {
            return true;
        } else
            return false;
    }

    public static String getCodeName(String code) {
        for (ErrorCode error : ErrorCode.values()) {
            if (error.getCode().equals(code))
                return error.codeName;
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
