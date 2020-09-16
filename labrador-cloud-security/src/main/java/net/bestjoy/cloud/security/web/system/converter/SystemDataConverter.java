package net.bestjoy.cloud.security.web.system.converter;

import net.bestjoy.cloud.core.config.MappingConverterConfig;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.web.system.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/***
 * vo converter
 * @author ray
 */
@Mapper(config = MappingConverterConfig.class)
public interface SystemDataConverter {
    /**
     * 单例
     */
    SystemDataConverter INSTATNCE = Mappers.getMapper(SystemDataConverter.class);


    RoleVO roleToVO(Role role);

    PermissionVO permissionToVO(Permission permission);

    ElementVO elementToVO(Element element);

    MenuVO menuToVO(Menu menu);

    OperationVO operationToVO(Operation operation);
}

