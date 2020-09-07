package net.bestjoy.cloud.web.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/***
 * 基本的web启动类
 *
 * @author ray
 */
@Slf4j
@Configuration
@SpringBootApplication
public class RootBootStarter extends SpringApplication {

    @Override
    public ConfigurableApplicationContext run(String... args) {


        return super.run(args);
    }
}
