package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bestjoy.cloud.security.core.entitiy.Operation;
import org.springframework.stereotype.Repository;

/**
 * @author ray
 */
@Repository
public interface OperationMapper extends BaseMapper<Operation> {
}
