package net.bestjoy.cloud.security.core.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.bean.QueryCondition;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.User;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ray
 */
@Data
@ToString
public class QueryUserDTO implements QueryCondition<User> {

    private String username;


    @Override
    public QueryWrapper<User> buildQueryCondition() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().orderByDesc(User::getCreateTime);
        queryWrapper.lambda().eq(User::getSystemId, SecurityContext.getSystemId());

        if (StringUtils.isNotBlank(username)) {
            queryWrapper.lambda().eq(User::getUsername, username);
        }


        return queryWrapper;
    }
}
