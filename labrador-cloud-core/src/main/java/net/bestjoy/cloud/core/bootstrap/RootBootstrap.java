package net.bestjoy.cloud.core.bootstrap;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.util.Dates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

/***
 * 启动基类
 * @author ray
 */
@Slf4j
public class RootBootstrap {


    /**
     * 启动类
     *
     * @param primarySource
     * @param args
     * @return
     */
    protected static SpringApplication run(Class<?> primarySource, String... args) {
        SpringApplication application = new SpringApplication(primarySource);

        ApplicationListener<?>[] applicationListeners = new ApplicationListener[]{
                //添加启动成功事件
                new ApplicationStartedEventListener(),
                //添加启动失败事件
                new ApplicationStartFailEventListener()
        };

        application.addListeners(applicationListeners);

        application.setBannerMode(Banner.Mode.CONSOLE);

        application.run(args);

        return application;
    }

    /**
     * 应用启动成功事件
     */
    private static class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
        @Override
        public void onApplicationEvent(ApplicationStartedEvent event) {
            Environment environment = event.getApplicationContext().getEnvironment();

            System.out.println(
                    "---------------------------------------\n\t"
                            + "\n '" + environment.getProperty("spring.application.name") + "' is started. power by labrador!\n\t"
                            + "\n app version:" + environment.getProperty("spring.application.version", "undefined")
                            + ",started time:" + Dates.formatNow() + "\n\t"
                            + "\n activated profiles: " + StringUtils.join(environment.getActiveProfiles(), ",") + "\n\t"
                            + "\n---------------------------------------");

        }
    }

    /**
     * 应用启动失败事件
     */
    private static class ApplicationStartFailEventListener implements ApplicationListener<ApplicationFailedEvent> {
        @Override
        public void onApplicationEvent(ApplicationFailedEvent event) {
            event.getException().printStackTrace();
        }
    }
}
