package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.IdentityTypeEnum;

/***
 * 用户信息
 * 限特定权限获取
 * @author ray
 */
@Data
@ToString
@TableName("sys_user_info")
public class UserInfo extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;

    /***
     * 用户id
     */
    private String userId;

    /**
     * 实名
     */
    private String realName;

    /***
     * 证件号
     */
    private String identityCard;

    /***
     * 证件类型
     */
    @EnumValue
    private IdentityTypeEnum identityType;

    /***
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

}
