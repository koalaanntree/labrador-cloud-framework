package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

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

    @ApiModelProperty("操作名称")
    private String operationName;

    @ApiModelProperty("操作描述")
    private String description;

}
