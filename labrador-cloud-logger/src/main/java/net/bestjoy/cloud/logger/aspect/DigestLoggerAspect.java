package net.bestjoy.cloud.logger.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.core.error.Errors;
import net.bestjoy.cloud.core.error.SysException;
import net.bestjoy.cloud.core.util.ParamsUtil;
import net.bestjoy.cloud.logger.Loggers;
import net.bestjoy.cloud.logger.context.LoggerContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;

/***
 * 摘要日志切面
 *
 * @author ray
 */
@Aspect
@Slf4j
public class DigestLoggerAspect {

    private long begin = 0;

    /***
     * 对所有的controller层进行拦截
     *
     * todo  去掉 error handle
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void loggerRestController() {

    }

    @Pointcut("@within(org.springframework.stereotype.Controller) && @within(org.springframework.web.bind.annotation.ResponseBody)")
    public void loggerController() {

    }

    @Pointcut("execution(* net.bestjoy.cloud.web.error.DefaultGlobalExceptionHandler.*(..))")
    public void excludeController() {

    }

    //todo 添加注解支持

    @SneakyThrows
    @Around("!excludeController() && (loggerRestController() || loggerController())")
    public Object around(ProceedingJoinPoint joinPoint) {
        begin = System.currentTimeMillis();
        //记录请求参数 todo 敏感参数encoding
        LoggerContext.setArgs(paramsToString(joinPoint));
        //todo 记录业务描述，注解支持，如果没有添加注解，为方法名
        LoggerContext.setBizDescription(joinPoint.getSignature().getName());

        return joinPoint.proceed();
    }

    @AfterReturning(value = "!excludeController() && (loggerRestController() || loggerController())", returning = "rtv")
    public void after(JoinPoint joinPoint, Object rtv) {
        try {
            //记录方法耗时
            LoggerContext.setTimeCost(System.currentTimeMillis() - begin);

            if (rtv instanceof Result) {
                Result result = (Result) rtv;
                //记录结果码
                LoggerContext.setResultCode(result.getCode());
            }

            //记录返回结果
            LoggerContext.setReturnResult(JSONObject.toJSONString(rtv));
            //输出日志文件
            Loggers.sendBizDigestLog();
        } catch (Exception e) {
            log.warn("记录日志出现异常");
            throw e;
        } finally {
            LoggerContext.clear();
        }
    }

    @AfterThrowing(value = "!excludeController() && (loggerRestController() || loggerController())", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        try {
            //记录方法耗时
            LoggerContext.setTimeCost(System.currentTimeMillis() - begin);

            String resultCode = Errors.Sys.SYS_ERROR.getCode();
            String result = "";
            if (exception instanceof BusinessException) {
                BusinessException bizException = (BusinessException) exception;
                resultCode = bizException.getError().getCode();
                result = JSONObject.toJSONString(Result.fail(bizException.getError()));
            } else if (exception instanceof SysException) {
                SysException sysException = (SysException) exception;
                resultCode = sysException.getError().getCode();
                result = JSONObject.toJSONString(Result.fail(sysException.getError()));
            } else {
                result = JSONObject.toJSONString(Result.fail(resultCode, exception.toString()));
            }

            //记录返回码
            LoggerContext.setResultCode(resultCode);
            //记录返回结果
            LoggerContext.setReturnResult(result);

            //输出日志文件
            Loggers.sendBizDigestLog();
        } catch (Exception e) {
            log.warn("记录日志出现异常");
            throw e;
        } finally {
            LoggerContext.clear();
        }
    }

    /***
     * 请求参数
     * @param proceedingJoinPoint
     * @return
     */
    private String paramsToString(ProceedingJoinPoint proceedingJoinPoint) {
        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();

        return ParamsUtil.paramsToString(codeSignature.getParameterNames(), proceedingJoinPoint.getArgs());
    }
}
