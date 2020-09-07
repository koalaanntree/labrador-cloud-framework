package net.bestjoy.cloud.core.bean;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author ray
 */
public interface QueryCondition<T> {

    QueryWrapper<T> buildQueryCondition();
}
