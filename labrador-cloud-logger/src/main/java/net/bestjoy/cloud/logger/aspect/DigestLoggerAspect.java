package net.bestjoy.cloud.logger.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.util.ParamsUtil;
import net.bestjoy.cloud.logger.context.LoggerContext;
import net.bestjoy.cloud.logger.util.LoggerUtils;
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
    /***
     * 对所有的controller层进行拦截
     */
    @Pointcut("execution(public * net..*Controller.*(..))")
    public void loggerControllerNet() {
    }

    @Pointcut("execution(public * com..*Controller.*(..))")
    public void loggerControllerCom() {
    }

    //todo 添加注解支持

    @SneakyThrows
    @Around("loggerControllerNet() || loggerControllerCom()")
    public Object around(ProceedingJoinPoint joinPoint) {
        long begin = System.currentTimeMillis();
        //记录请求参数
        LoggerContext.setArgs(paramsToString(joinPoint));
        //todo 记录业务描述，注解支持，如果没有添加注解，为方法名
        LoggerContext.setBizDescription(joinPoint.getSignature().getDeclaringTypeName());
        //记录方法耗时
        LoggerContext.setTimeCost(System.currentTimeMillis() - begin);
        return joinPoint.proceed();
    }

    @AfterReturning(value = "loggerControllerNet() || loggerControllerCom()", returning = "rtv")
    public void after(JoinPoint joinPoint, Object rtv) {
        try {
            if (rtv instanceof Result) {
                Result result = (Result) rtv;
                //记录结果码
                LoggerContext.setResultCode(result.getCode());
            }

            //记录返回结果
            LoggerContext.setReturnResult(JSONObject.toJSONString(rtv));
            //记录返回结果类型
            LoggerUtils.sendBizDigestLogger("");
        } catch (Exception e) {
            log.warn("记录日志出现异常");
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
