package net.bestjoy.cloud.web.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

import javax.validation.constraints.NotNull;

/***
 * swagger配置项
 * @author ray
 */
@Data
@ToString
@ConfigurationProperties(prefix = "bestjoy.swagger")
public class SwaggerProperties {

    @Value("${bestjoy.swagger.title:${spring.application.name}}")
    private String title;

    private String description;

    @Value("${bestjoy.swagger.version:${spring.application.version}}")
    private String version;

    private String termsOfServiceUrl;

    private Contact contact;
}
