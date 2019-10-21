package net.bestjoy.cloud.test;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/***
 * 测试应用，spring cloud
 *
 * @author ray
 */
@Slf4j
@EnableTransactionManagement
@SpringBootApplication
public class FrameworkTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkTestApplication.class);
        log.info("framework test service application running...");
    }
}
