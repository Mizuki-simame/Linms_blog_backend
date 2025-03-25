package linms.linmsblog.common;

/**
 * 返回信息枚举
 */
public enum ResEnum {

    SUCCESS(200, "请求成功"),
    BAD_REQUEST(400, "服务内部错误"),
    UNAUTHORIZED(401, "未授权"),
    NOT_FOUND(404, "未发现"),
    USER_NOT_EXIST(10401, "用户不存在"),
    USER_IS_EXIST(10402, "用户已存在"),
    USER_IS_LOGIN(10403, "用户已登录"),
    PASSWORD_NOT_RIGHT(10403, "密码错误"),
    PAGE_ERROR(10404, "分页错误"),
    PARAMETER_ERROR(10405, "参数错误")
    ;

    private final Integer code;

    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean isSuccess(int code){
        if(ResEnum.SUCCESS.code == code) {
            return true;
        }else{
            return false;
        }
    }

}