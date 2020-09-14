package net.bestjoy.cloud.security.converter;

import net.bestjoy.cloud.core.config.MappingConverterConfig;
import net.bestjoy.cloud.security.core.AppUserDetails;
import net.bestjoy.cloud.security.core.UserSession;
import net.bestjoy.cloud.security.core.dto.UserDTO;
import net.bestjoy.cloud.security.core.entitiy.User;
import net.bestjoy.cloud.security.web.auth.response.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/***
 * 用户对象装换
 * @author ray
 */
@Mapper(config = MappingConverterConfig.class)
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    AppUserDetails userToDetail(User user);

    UserSession detailToSession(AppUserDetails appUserDetails);

    LoginResponse userToLoginResponse(AppUserDetails appUserDetails);

    UserDTO userToDTO(User user);
}
