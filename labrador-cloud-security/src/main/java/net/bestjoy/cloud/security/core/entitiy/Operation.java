package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 操作
 * @author ray
 */
@Data
@ToString
@TableName("sys_operation")
public class Operation extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;

    /***
     * 操作id
     */
    private String operationId;

    /***
     * 操作名称
     */
    private String operationName;

    /***
     * 操作code，唯一
     */
    private String operationCode;

    /**
     * 操作描述
     */
    private String description;
}
