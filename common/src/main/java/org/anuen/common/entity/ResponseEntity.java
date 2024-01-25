package org.anuen.common.entity;

import lombok.Data;
import org.anuen.common.enums.ResponseStatus;

/**
 * 响应实体
 *
 * @param <T> 泛型安全检查
 */
@Data
public class ResponseEntity<T> {
    /**
     * 消息
     */
    private String message;

    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 响应内容
     */
    private T data;

    public ResponseEntity(String message, Integer code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    /**
     * 成功响应模版
     *
     * @param <T> all
     * @return responseEntity
     */
    public static <T> ResponseEntity<T> success() {
        return new ResponseEntity<>(
                ResponseStatus.SUCCESS.getMessage(),
                ResponseStatus.SUCCESS.getCode(),
                null);
    }

    public static <T> ResponseEntity<T> success(T data) {
        return success(data, ResponseStatus.SUCCESS.getMessage());
    }

    public static <T> ResponseEntity<T> success(T data, String message) {
        return new ResponseEntity<>(
                message,
                ResponseStatus.SUCCESS.getCode(),
                data);
    }

    /**
     * 失败响应模版
     *
     * @param <T> all
     * @return responseEntity
     */
    public static <T> ResponseEntity<T> fail() {
        return new ResponseEntity<>(
                ResponseStatus.FAIL.getMessage(),
                ResponseStatus.FAIL.getCode(),
                null);
    }

    public static <T> ResponseEntity<T> fail(T data) {
        return fail(data, ResponseStatus.FAIL.getMessage());
    }

    public static <T> ResponseEntity<T> fail(String message) {
        return fail(null, message);
    }

    public static <T> ResponseEntity<T> fail(ResponseStatus statusEnum) {
        return fail(statusEnum.getMessage(), statusEnum);
    }

    public static <T> ResponseEntity<T> fail(String message, ResponseStatus statusEnum) {
        return new ResponseEntity<>(
                message,
                statusEnum.getCode(),
                null);
    }

    public static <T> ResponseEntity<T> fail(T data, String message) {
        return new ResponseEntity<>(
                message,
                ResponseStatus.FAIL.getCode(),
                data);
    }

    /**
     * 纯自定义响应
     *
     * @param data       响应内容
     * @param message    响应消息
     * @param statusEnum 响应状态枚举
     * @param <T>        all
     * @return responseEntity
     */
    public static <T> ResponseEntity<T> getResponse(
            T data,
            String message,
            ResponseStatus statusEnum) {
        return new ResponseEntity<>(message, statusEnum.getCode(), data);
    }

}
