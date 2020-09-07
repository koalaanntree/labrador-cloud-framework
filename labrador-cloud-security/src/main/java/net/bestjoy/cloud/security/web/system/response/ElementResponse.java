package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.entitiy.Element;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/***
 * 页面元素返回结果
 * @author ray
 */
@Data
@ToString
public class ElementResponse implements Serializable {
    @ApiModelProperty("页面元素id")
    private String elementId;

    @ApiModelProperty("页面元素名称")
    private String elementName;

    @ApiModelProperty("页面元素描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public static ElementResponse convert(Element element) {
        if (element == null) {
            return null;
        }

        ElementResponse elementResponse = new ElementResponse();
        BeanUtils.copyProperties(element, elementResponse);
        return elementResponse;
    }
}
