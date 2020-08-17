package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.entitiy.Operation;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/***
 * 操作返回对象
 * @author ray
 */
@Data
@ToString
public class OperationResponse implements Serializable {

    @ApiModelProperty("操作id")
    private String operationId;

    @ApiModelProperty("操作名称")
    private String operationName;

    @ApiModelProperty("操作描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public static OperationResponse convert(Operation operation) {
        if (operation == null) {
            return null;
        }
        OperationResponse response = new OperationResponse();
        BeanUtils.copyProperties(operation, response);
        return response;
    }
}
