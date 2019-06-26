package net.bestjoy.cloud.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * eureka服务
 */
@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class EurekaApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class);
        log.info("eureka app started...");
    }
}
