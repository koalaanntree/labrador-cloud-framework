package net.bestjoy.cloud.web.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

import javax.validation.constraints.NotNull;

/***
 * swagger配置项
 * @author ray
 */
@Data
@ToString
@ConfigurationProperties(prefix = "bt.swagger")
public class SwaggerProperties {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String version;

    private String termsOfServiceUrl;

    private Contact contact;
}
