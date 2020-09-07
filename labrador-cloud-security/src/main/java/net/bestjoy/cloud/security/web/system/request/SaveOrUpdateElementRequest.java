package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.ElementTypeEnum;

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

    @ApiModelProperty("页面元素名称")
    private String elementName;

    @ApiModelProperty("页面元素描述")
    private String description;

    @ApiModelProperty("页面元素类型")
    private ElementTypeEnum elementType;

    @ApiModelProperty("扩展")
    private String extInfo;
}
