package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 保存或更新操作
 *
 * @author ray
 */
@Data
@ToString
public class SaveOrUpdateOperationRequest implements Serializable {

    @ApiModelProperty("操作id，更新时必传")
    private String operationId;

    @ApiModelProperty(value = "操作名称", required = true)
    @NotBlank(message = "操作名称不能为空")
    private String operationName;

    @ApiModelProperty(value = "操作code", required = true)
    @NotBlank(message = "操作code不能为空")
    private String operationCode;

    @ApiModelProperty("操作描述")
    private String description;

}
