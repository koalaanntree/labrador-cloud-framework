package net.bestjoy.cloud.security.service;

import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import net.bestjoy.cloud.security.service.impl.PermissionElementProvider;
import net.bestjoy.cloud.security.service.impl.PermissionMenuProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static net.bestjoy.cloud.security.core.error.AuthErrors.PERMISSION_SUBJECT_TYPE_NOT_EXIST_ERROR;

/***
 * 权限对象context
 * @author ray
 */
@Component
public class PermissionSubjectContext {
    @Autowired
    private ApplicationContext applicationContext;

    /***
     * 获取权限主体工具类
     * @param permissionTypeEnum
     * @return
     */
    public PermissionSubjectProvider getPermissionSubjectProvider(PermissionTypeEnum permissionTypeEnum) {
        switch (permissionTypeEnum) {
            case MENU:
                return applicationContext.getBean(PermissionMenuProvider.class);
            case ELEMENT:
                return applicationContext.getBean(PermissionElementProvider.class);
            case FILE:
            default:
                throw new BusinessException(PERMISSION_SUBJECT_TYPE_NOT_EXIST_ERROR);
        }
    }
}
