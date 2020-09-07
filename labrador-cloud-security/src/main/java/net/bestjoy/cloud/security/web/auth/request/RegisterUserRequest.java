package net.bestjoy.cloud.security.web.auth.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.UserTypeEnum;

import java.io.Serializable;

/***
 * 用户注册请求
 * @author ray
 */
@Data
@ToString
public class RegisterUserRequest implements Serializable {
    @ApiModelProperty(value = "账号名", required = true)
    private String username;

    @ApiModelProperty(value = "已对比确认的密码", required = true)
    private String confirmedPassword;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("账号类型")
    private UserTypeEnum userType;
}
