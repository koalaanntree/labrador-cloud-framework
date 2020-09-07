package net.bestjoy.cloud.web.starter;

import net.bestjoy.cloud.web.annotation.EnableSwaggerDoc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 默认web启动类。自动完成所有web默认配置
 * TODO 持续更新配置
 *
 * @author ray
 */
@EnableTransactionManagement
@EnableSwaggerDoc
public class DefaultWebBootStarter extends RootBootStarter {

}
