package net.bestjoy.cloud.test;


import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bootstrap.RootBootstrap;
import net.bestjoy.cloud.web.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/***
 * 测试应用，spring cloud
 *
 * @author ray
 */
@Slf4j
@EnableSwaggerDoc
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FrameworkTestApplication extends RootBootstrap {

    public static void main(String[] args) {
        run(FrameworkTestApplication.class, args);
    }
}
