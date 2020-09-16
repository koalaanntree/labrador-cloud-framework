package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.ElementTypeEnum;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 新增或者保存页面元素请求
 * @author ray
 */
@Data
@ToString
public class SaveOrUpdateElementRequest implements Serializable {

    @ApiModelProperty("页面元素id，更新时必传")
    private String elementId;

    @NotBlank(message = "页面元素名称不能为空")
    @ApiModelProperty(value = "页面元素名称", required = true)
    private String elementName;

    @NotBlank(message = "页面元素code不能为空")
    @ApiModelProperty(value = "页面元素code", required = true)
    private String elementCode;

    @ApiModelProperty("页面元素描述")
    private String description;

    @ApiModelProperty("页面元素类型")
    @NotBlank(message = "页面元素类型不能为空")
    private ElementTypeEnum elementType;

    @ApiModelProperty("扩展")
    private String extInfo;
}
