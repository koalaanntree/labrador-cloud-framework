package net.bestjoy.cloud.security.core.error;


import net.bestjoy.cloud.core.error.ErrorCodeAndMessage;

/***
 * 用户一般异常，无需特殊处理
 * @author ray
 */
public interface AuthErrors {

    ErrorCodeAndMessage USER_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10001", "user not found.");
    ErrorCodeAndMessage USER_STATE_ERROR = ErrorCodeAndMessage.create("A10002", "user state error.");
    ErrorCodeAndMessage USER_PASSWORD_INCORRECT  = ErrorCodeAndMessage.create("A10003", "user password incorrect");

    ErrorCodeAndMessage ROLE_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10100", "role not found.");
    ErrorCodeAndMessage ROLE_ALREADY_EXIST_ERROR = ErrorCodeAndMessage.create("A10101", "role already exist.");
    ErrorCodeAndMessage MENU_ALREADY_EXIST_ERROR = ErrorCodeAndMessage.create("A10102", "menu already exist.");
    ErrorCodeAndMessage MENU_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10103", "menu not found.");
    ErrorCodeAndMessage PERMISSION_ALREADY_EXIST_ERROR = ErrorCodeAndMessage.create("A10104", "permission already exist.");
    ErrorCodeAndMessage PERMISSION_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10105", "permission not found.");
    ErrorCodeAndMessage PERMISSION_SUBJECT_TYPE_NOT_EXIST_ERROR = ErrorCodeAndMessage.create("A10106", "permission subject not exist.");
    ErrorCodeAndMessage OPERATION_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10107", "operation not found.");
    ErrorCodeAndMessage OPERATION_ALREADY_EXIST_ERROR = ErrorCodeAndMessage.create("A10108", "operation already exist.");
    ErrorCodeAndMessage ELEMENT_NOT_FOUND_ERROR = ErrorCodeAndMessage.create("A10109", "element not found.");
    ErrorCodeAndMessage ELEMENT_ALREADY_EXIST_ERROR = ErrorCodeAndMessage.create("A10110", "element already exist");

}
