package net.bestjoy.cloud.core.test.bean;

import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.annotation.NotNull;

/**
 * @author ray
 */
@Data
@ToString
public class TestDto {

    @NotNull
    private Integer appId;

    private String appName;
}
