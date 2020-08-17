package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bestjoy.cloud.security.core.entitiy.User;
import org.springframework.stereotype.Repository;

/***
 * @author ray
 */
@Repository("userMapper")
public interface UserMapper extends BaseMapper<User> {
    
}
