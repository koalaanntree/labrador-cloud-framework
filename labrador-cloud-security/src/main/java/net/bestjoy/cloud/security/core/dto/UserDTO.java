package net.bestjoy.cloud.security.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.UserStateEnum;
import net.bestjoy.cloud.security.core.enums.UserTypeEnum;

import java.util.Date;

/**
 * @author ray
 */
@Data
@ToString
public class UserDTO {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户类型")
    private UserTypeEnum userType;

    @ApiModelProperty("用户状态")
    private UserStateEnum userState;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("账号签名")
    private String signature;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date updateTime;
}
