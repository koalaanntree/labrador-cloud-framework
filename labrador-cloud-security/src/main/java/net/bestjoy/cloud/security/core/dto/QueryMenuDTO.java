package net.bestjoy.cloud.security.core.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.bean.QueryCondition;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.Menu;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import org.springframework.util.StringUtils;

/**
 * 查询menuDTO
 *
 * @author ray
 */
@Data
@ToString
public class QueryMenuDTO implements QueryCondition<Menu> {

    @ApiModelProperty("菜单ID")
    private String menuId;

    @ApiModelProperty("菜单名称，模糊查询")
    private String menuName;

    @ApiModelProperty("菜单码，模糊查询")
    private String menuCode;

    @ApiModelProperty("父菜单ID")
    private String parentMenuId;

    @ApiModelProperty(value = "查询顶级列表", dataType = "boolean")
    private Boolean topQuery;

    @ApiModelProperty("菜单状态")
    private MenuStatusEnum menuStatus;

    @Override
    public QueryWrapper<Menu> buildQueryCondition() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Menu::getCreateTime);
        queryWrapper.lambda().eq(Menu::getSystemId, SecurityContext.getSystemId());

        if (!StringUtils.isEmpty(menuId)) {
            queryWrapper.lambda().eq(Menu::getMenuId, menuId);
        }

        if (!StringUtils.isEmpty(menuName)) {
            queryWrapper.lambda().like(Menu::getMenuName, menuName);
        }

        if (!StringUtils.isEmpty(parentMenuId)) {
            queryWrapper.lambda().eq(Menu::getParentMenuId, parentMenuId);
        }

        if (!StringUtils.isEmpty(menuCode)) {
            queryWrapper.lambda().like(Menu::getMenuCode, menuCode);
        }

        if (menuStatus != null) {
            queryWrapper.lambda().eq(Menu::getMenuStatus, menuStatus);
        }

        if (topQuery) {
            queryWrapper.lambda().isNull(Menu::getParentMenuId);
        }

        return queryWrapper;
    }
}
