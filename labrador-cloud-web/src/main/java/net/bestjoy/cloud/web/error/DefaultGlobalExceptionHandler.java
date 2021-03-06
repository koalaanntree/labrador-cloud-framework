package net.bestjoy.cloud.web.error;

import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.core.error.Errors;
import net.bestjoy.cloud.core.error.SysException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/***
 * 统一异常处理
 *
 * 自定义需要继承
 * @author ray
 */
@Primary
@ControllerAdvice
@ConditionalOnMissingBean(name = "exceptionHandler")
@RestController
public class DefaultGlobalExceptionHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        //todo 如果发生异常，默认跳转的页面
        return "/error";
    }

    @ExceptionHandler(SysException.class)
    public Object systemErrorHandler(SysException sysException) {
        sysException.printStackTrace();
        return Result.fail(sysException.getError());
    }

    @ExceptionHandler(BusinessException.class)
    public Object bizErrorHandler(BusinessException businessException) {
        businessException.printStackTrace();
        return Result.fail(businessException.getError());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object methodNotSupportedHandler(HttpRequestMethodNotSupportedException exception) {
        exception.printStackTrace();
        return Result.fail(Errors.Sys.METHOD_NOT_ALLOWED_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object notValidParameterHandler(MethodArgumentNotValidException exception) {
        //todo  参数打印
        exception.printStackTrace();
        return Result.fail(Errors.Biz.ILLEGAL_ARGUMENT_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public Object throwableHandler(Throwable cause) {
        cause.printStackTrace();
        return Result.fail(Errors.Sys.SYS_ERROR.getCode(), cause.toString());
    }

}
