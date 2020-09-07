package net.bestjoy.cloud.task.standalone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 独立任务实体,Mapper扫描配置
 */
@Configuration
@MapperScan(basePackages = "com.ptc.board.taskqueue.standalone.dao")
public class DALConfig {
}
