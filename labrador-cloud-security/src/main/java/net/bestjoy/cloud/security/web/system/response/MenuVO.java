package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单返回结果
 *
 * @author ray
 */
@Data
@ToString
public class MenuVO implements Serializable {
    @ApiModelProperty("菜单id")
    private String menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单url")
    private String menuUrl;

    @ApiModelProperty("菜单类型")
    private MenuTypeEnum menuType;

    @ApiModelProperty("菜单描述")
    private String description;

    @ApiModelProperty("菜单状态")
    private MenuStatusEnum menuStatus;

    @ApiModelProperty("父菜单id")
    private String parentMenuId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
