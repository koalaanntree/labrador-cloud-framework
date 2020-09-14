package net.bestjoy.cloud.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.PageBean;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.dto.QueryUserDTO;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.UserStateEnum;
import net.bestjoy.cloud.security.persistent.repository.UserMapper;
import net.bestjoy.cloud.security.persistent.repository.UserRoleMapper;
import net.bestjoy.cloud.security.service.UserCacheIncreasingIdProvider;
import net.bestjoy.cloud.security.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/***
 *
 * @author ray
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCacheIncreasingIdProvider userCacheIncreasingIdProvider;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            log.warn("db query user info => userId is empty");
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveUser(User user) {
        user.setUserId(userCacheIncreasingIdProvider.generateCacheIncreasingId());
        user.setCreateTime(new Date());
        if (user.getUserState() == null) {
            user.setUserState(UserStateEnum.NORMAL);
        }
        user.setUpdateTime(user.getCreateTime());
        user.setSystemId(SecurityContext.getSystemId());
        userMapper.insert(user);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(new Date());

        userMapper.updateById(user);
    }

    @Override
    public List<Role> getUserRoles(String userId) {
        return userRoleMapper.selectUerRoleList(userId);
    }

    @Override
    public void addUserRole(UserRole userRole) {
        userRole.setCreateTime(new Date());
        userRole.setUpdateTime(userRole.getCreateTime());
        userRole.setSystemId(SecurityContext.getSystemId());
        userRoleMapper.insert(userRole);
    }

    @Override
    public void deleteUserRole(String userId, String roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("role_id", roleId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());
        userRoleMapper.delete(queryWrapper);
    }

    @Override
    public List<String> getUserPermissionIdList(String userId) {
        return userRoleMapper.selectUserPermissionIdList(userId);
    }

    @Override
    public List<Permission> getUserPermissionList(String userId) {
        return userRoleMapper.selectUserPermissionList(userId);
    }

    @Override
    public IPage<User> pageQueryUser(
            PageBean<User> pageBean, QueryUserDTO queryUserDTO) {

        return userMapper.selectPage(pageBean.getPage(), queryUserDTO.buildQueryCondition());
    }
}
