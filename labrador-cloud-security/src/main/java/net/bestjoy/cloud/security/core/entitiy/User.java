package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.UserStateEnum;
import net.bestjoy.cloud.security.core.enums.UserTypeEnum;

/***
 * 用户表
 *
 * 登录账号
 * @author ray
 */
@Data
@ToString
@TableName("sys_user")
public class User extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;

    /**
     * 用户id
     */
    private String userId;

    /***
     * 用户账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /***
     * 用户类型
     */
    @EnumValue
    private UserTypeEnum userType;

    /**
     * 用户状态
     */
    @EnumValue
    private UserStateEnum userState;

    /**
     * 密码
     */
    private String password;
    //todo ext

    /***
     * 头像
     */
    private String avatar;

    /***
     * 账号签名
     */
    private String signature;
}
