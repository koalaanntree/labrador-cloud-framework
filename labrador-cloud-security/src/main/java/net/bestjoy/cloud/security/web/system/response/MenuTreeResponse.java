package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/***
 * 菜单列表，树形结构
 * @author ray
 */
@Data
@ToString
public class MenuTreeResponse extends MenuResponse implements Serializable {
    @ApiModelProperty("是否顶级目录")
    private boolean isTopMenu = false;

    @ApiModelProperty("是否有子目录")
    private boolean hasSubMenu = false;

    @ApiModelProperty("子目录")
    private List<MenuTreeResponse> subMenus;
}
