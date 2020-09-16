package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 保存或更新菜单请求
 *
 * @author ray
 */
@Data
@ToString
public class SaveOrUpdateMenuRequest implements Serializable {
    @ApiModelProperty("菜单id，更新时必传")
    private String menuId;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;

    @NotBlank(message = "菜单code不能为空")
    @ApiModelProperty(value = "菜单code", required = true)
    private String menuCode;

    @ApiModelProperty("菜单描述")
    private String description;

    @ApiModelProperty("菜单类型")
    private MenuTypeEnum menuType;

    @ApiModelProperty("菜单地址")
    private String menuUrl;

    @ApiModelProperty("父菜单id")
    private String parentMenuId;

    @ApiModelProperty("菜单状态")
    private MenuStatusEnum menuStatus;
}
