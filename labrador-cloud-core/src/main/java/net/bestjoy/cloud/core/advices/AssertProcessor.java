package net.bestjoy.cloud.core.advices;

import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import net.bestjoy.cloud.core.annotation.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

/***
 * @author ray
 */
@Component
@AutoService(NotNull.class)
public class AssertProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
