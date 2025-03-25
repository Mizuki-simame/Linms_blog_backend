package linms.linmsblog.common;

import lombok.Data;

/**
 * 返回值工具类
 * @param <T>
 */
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResEnum resEnum) {
        this.code = resEnum.getCode();
        this.message = resEnum.getMsg();
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<T>(ResEnum.SUCCESS);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok() {
        return new Result<T>(ResEnum.SUCCESS);
    }

    public Result(T data) {
        this.data = data;
        this.code = (Integer) 200;
        this.message = "请求成功";
    }

    public static <T> Result<T> normalResponse(ResEnum resEnum) {
        Result<T> result = new Result<>();
        result.setCode(resEnum.getCode());
        result.setMessage(resEnum.getMsg());
        return result;
    }

    public Result() {
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Result<T> success() {
        Result<T> r = new Result<>();
        r.setData(data);
        r.setCode(ResEnum.SUCCESS.getCode());
        r.setMessage(ResEnum.SUCCESS.getMsg());
        return r;
    }

    public Result<T> success(String message) {
        Result<T> r = new Result<>();
        r.setData(data);
        r.setCode(ResEnum.SUCCESS.getCode());
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail() {
        return new Result<T>(ResEnum.BAD_REQUEST);
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setCode(ResEnum.BAD_REQUEST.getCode());
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail(ResEnum resEnum) {
        Result<T> r = new Result<T>();
        r.setCode(resEnum.getCode());
        r.setMessage(resEnum.getMsg());
        return r;
    }

    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail(Integer code, String message, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}