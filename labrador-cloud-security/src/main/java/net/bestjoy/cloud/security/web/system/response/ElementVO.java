package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/***
 * 页面元素返回结果
 * @author ray
 */
@Data
@ToString
public class ElementVO implements Serializable {
    @ApiModelProperty("页面元素id")
    private String elementId;

    @ApiModelProperty("页面元素名称")
    private String elementName;

    @ApiModelProperty("页面元素描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
