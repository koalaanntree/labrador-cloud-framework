package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.SystemStateEnum;

import java.util.Date;

/***
 * 系统详情
 *
 * @author ray
 */
@Data
@ToString
@TableName("sys_system_info")
public class SystemInfo extends BaseEntity<Long> {
    /***
     * 系统id
     */
    private String systemId;
    /***
     * 系统名称
     */
    private String systemName;
    /***
     * 系统首页url
     */
    private String systemHomepage;
    /**
     * 系统状态
     */
    @EnumValue
    private SystemStateEnum systemState;
    /***
     * 系统描述
     */
    private String description;
    /**
     * 系统当前版本
     */
    private String currentVersion;
    /**
     * 系统最后发布时间
     */
    private Date lastPublishTime;
}
