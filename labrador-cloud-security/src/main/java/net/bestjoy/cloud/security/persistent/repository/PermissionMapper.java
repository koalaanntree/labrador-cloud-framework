package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bestjoy.cloud.security.core.entitiy.Permission;
import org.springframework.stereotype.Repository;

/**
 * @author ray
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
}
