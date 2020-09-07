package net.bestjoy.cloud.influxdb.config;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class InfluxDBAutoConfiguration {

    @Value("${spring.influx.url:''}")
    private String influxDBUrl;

    @Value("${spring.influx.user:''}")
    private String userName;

    @Value("${spring.influx.password:''}")
    private String password;

    @Value("${spring.influx.database:''}")
    private String database;

    @Value("${spring.profiles.active:''}")
    private String profiles;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxDBUrl, userName, password);
        try {
            /*
             * 异步插入：
             * enableBatch这里第一个是point的个数，第二个是时间，单位毫秒
             * point的个数和时间是联合使用的，如果满100条或者2000毫秒
             * 满足任何一个条件就会发送一次写的请求。
             */
            influxDB.setDatabase(database)
                    .enableBatch(100, 2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            influxDB.setRetentionPolicy("autogen");
        }
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        if (profiles.equals("prod")
                || profiles.equals("test")
                || profiles.equals("uat")) {
            influxDB.setLogLevel(InfluxDB.LogLevel.NONE);
        }
        return influxDB;
    }

}
