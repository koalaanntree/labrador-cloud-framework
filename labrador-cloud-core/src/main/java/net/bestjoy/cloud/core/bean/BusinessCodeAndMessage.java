package net.bestjoy.cloud.core.bean;

/**
 * 信息抽象接口
 * 所有用户返回信息均应该继承改接口
 */
public interface BusinessCodeAndMessage {
    /**
     * 获取异常编码
     */
    Integer getCode();

    /**
     * 获取异常信息
     */
    String getMessage();
}
