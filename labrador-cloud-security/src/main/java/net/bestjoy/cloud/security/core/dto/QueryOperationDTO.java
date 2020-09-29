package net.bestjoy.cloud.security.core.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.bean.QueryCondition;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.Operation;
import org.springframework.util.StringUtils;

/**
 * 查询操作dto
 *
 * @author ray
 */
@Data
@ToString
public class QueryOperationDTO implements QueryCondition<Operation> {
    @ApiModelProperty("操作ID")
    private String operationId;

    @ApiModelProperty("操作名称，模糊查询")
    private String operationName;

    @ApiModelProperty("操作码，模糊查询")
    private String operationCode;

    @Override
    public QueryWrapper<Operation> buildQueryCondition() {
        QueryWrapper<Operation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Operation::getCreateTime);
        queryWrapper.lambda().eq(Operation::getSystemId, SecurityContext.getSystemId());

        if (!StringUtils.isEmpty(operationId)) {
            queryWrapper.lambda().eq(Operation::getOperationId, operationId);
        }

        if (!StringUtils.isEmpty(operationName)) {
            queryWrapper.lambda().like(Operation::getOperationName, operationName);
        }

        if (!StringUtils.isEmpty(operationCode)) {
            queryWrapper.lambda().like(Operation::getOperationCode, operationCode);
        }
        return queryWrapper;
    }
}
