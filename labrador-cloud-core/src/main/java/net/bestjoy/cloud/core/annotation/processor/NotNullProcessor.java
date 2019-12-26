package net.bestjoy.cloud.core.annotation.processor;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/***
 * notNull注解解析类
 * @author ray
 */
@SupportedAnnotationTypes(value = {"net.bestjoy.cloud.core.annotation.NotNull"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class NotNullProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("process");

        return true;
    }
}
