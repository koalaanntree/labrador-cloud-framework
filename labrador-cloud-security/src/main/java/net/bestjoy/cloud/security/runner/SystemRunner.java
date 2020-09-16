package net.bestjoy.cloud.security.runner;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.config.AuthorizationProperties;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.SystemStateEnum;
import net.bestjoy.cloud.security.core.enums.UserTypeEnum;
import net.bestjoy.cloud.security.service.PermissionService;
import net.bestjoy.cloud.security.service.SystemInfoService;
import net.bestjoy.cloud.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

/***
 * 系统启动运行类
 * @author ray
 */
@Component
@Order
@Slf4j
public class SystemRunner implements ApplicationRunner {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String ADMIN_DEFAULT_PWD = "bt#123";

    @Autowired
    private AuthorizationProperties authorizationProperties;
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SystemInfo systemInfo =
                systemInfoService.getSystemInfoByName(authorizationProperties.getSystemName());

        if (systemInfo == null) {
            systemInfo = new SystemInfo();
            systemInfo.setSystemName(authorizationProperties.getSystemName());
            systemInfo.setCurrentVersion(authorizationProperties.getSystemVersion());
            systemInfo.setDescription(authorizationProperties.getSystemDescription());
            systemInfo.setSystemHomepage(authorizationProperties.getSystemHomepage());
            systemInfo.setLastPublishTime(new Date());
            systemInfo.setSystemState(SystemStateEnum.RUNNING);

            systemInfoService.saveSystemInfo(systemInfo);
        } else {
            systemInfo.setSystemHomepage(authorizationProperties.getSystemHomepage());
            systemInfo.setSystemState(SystemStateEnum.RUNNING);
            systemInfo.setLastPublishTime(new Date());
            systemInfo.setCurrentVersion(authorizationProperties.getSystemVersion());
            systemInfo.setDescription(authorizationProperties.getSystemDescription());

            systemInfoService.updateSystemInfo(systemInfo);
        }

        log.info("...refresh system info:{}", systemInfo.toString());
        SecurityContext.setSystemId(systemInfo.getSystemId());

        //设置管理员
        User user = userService.getUserByUsername(ADMIN_USERNAME);
        if (user == null) {
            user = new User();
            user.setUsername(ADMIN_USERNAME);
            user.setPassword(passwordEncoder.encode(ADMIN_DEFAULT_PWD));
            user.setUserType(UserTypeEnum.ADMIN);
            userService.saveUser(user);

            Role adminRole = permissionService.getRoleByCode(ADMIN_ROLE);

            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRoleName(ADMIN_ROLE);
                adminRole.setDescription("管理员角色");
                permissionService.addRole(adminRole);
            }

            UserRole userRole = new UserRole(user.getUserId(), adminRole.getRoleId());
            userService.addUserRole(userRole);

            log.info("add admin user done.");
        } else {
            log.info("admin user exist.");
        }
    }
}
