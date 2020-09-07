package net.bestjoy.cloud.task.standalone;


import net.bestjoy.cloud.task.EntityAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 独立任务模式中聚合实体管理类
 * <p>由于只有一个生产队列，要应对不同的业务实体、不同的业务处理逻辑. StandaloneEntityAdapter 用于对同一个队列聚合管理多个不同的实体适配器
 * </p>
 */
public class StandaloneEntityAdapter implements EntityAdapter<String, BasicTaskEntity, Object> {
    /**
     * extractEntityId使用,按业务实体class的不同，分不同的EntityAdapter
     */
    private Map<Class<?>, EntityAdapter<?, BasicTaskEntity, Object>> entityAdapterMap = new HashMap<>();

    /**
     * extractEntityId使用, 默认实体适配器,如果业务实体class不存在entityAdapterMap中，就使用默认适配器
     * <p>可用于在一个实体适配器中，处理多中实体类型</p>
     */
    private EntityAdapter<?, BasicTaskEntity, Object> defaultEntityAdapter;

    /**
     * getEntity 使用,按业务实体domains的不同，分不同的EntityAdapter
     */
    private Map<String, EntityAdapter<?, BasicTaskEntity, Object>> entityAdapterByDomain = new HashMap<>();

    /**
     * getEntity 使用, 默认实体适配器, domain存在entityAdapterByDomain中，就使用默认适配器
     * <p>可用于在一个实体适配器中，处理多中实体类型</p>
     */
    private EntityAdapter<?, BasicTaskEntity, Object> defaultEntityAdapterByDomain;

    /**
     * @param task 任务实体详情
     * @return
     */
    @Override
    public Object getEntity(BasicTaskEntity task) {
        EntityAdapter<?, BasicTaskEntity, Object> entityAdapter = entityAdapterByDomain.get(task.getBizDomain());

        if (entityAdapter == null) {
            entityAdapter = defaultEntityAdapterByDomain;
        }

        return entityAdapter != null ? entityAdapter.getEntity(task) : null;
    }

    /**
     * @param entity 业务实体
     * @return
     */
    @Override
    public String extractEntityId(Object entity) {
        EntityAdapter<?, BasicTaskEntity, Object> entityAdapter = entityAdapterMap.get(entity.getClass());

        Object key = null;
        if (entityAdapter != null) {
            key = entityAdapter.extractEntityId(entity);
        } else if (defaultEntityAdapter != null) {
            key = defaultEntityAdapter.extractEntityId(entity);
        }

        return key != null ? key.toString() : null;
    }

    /**
     * 添加EntityAdapter
     *
     * @param entityAdapter 实体适配器
     * @param domain        业务域
     * @param clazz         业务实体类型
     * @param <EA>          实体适配器类型
     */
    public <EA extends EntityAdapter> void add(EA entityAdapter, String domain, Class<?> clazz) {
        entityAdapterMap.put(clazz, entityAdapter);
        entityAdapterByDomain.put(domain, entityAdapter);
    }

    /**
     * 移除业务实体类型对应的EntityAdapter
     *
     * @param clazz 业务实体类型
     * @return 被移除的EntityAdapter
     */
    public EntityAdapter<?, BasicTaskEntity, Object> removeByClass(Class<?> clazz) {
        EntityAdapter<?, BasicTaskEntity, Object> entityAdapter = entityAdapterMap.get(clazz);

        if (entityAdapter != null) {
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, EntityAdapter<?, BasicTaskEntity, Object>> entry : entityAdapterByDomain.entrySet()) {
                if (entityAdapter == entry.getValue()) {
                    keys.add(entry.getKey());
                }
            }

            for (String k : keys) {
                entityAdapterByDomain.remove(k);
            }
        }

        return entityAdapter;
    }

    /**
     * 移除domain对应的EntityAdapter
     *
     * @param domain 业务domain
     * @return 被移除的EntityAdapter
     */
    public EntityAdapter<?, BasicTaskEntity, Object> removeByDomain(String domain) {
        EntityAdapter<?, BasicTaskEntity, Object> entityAdapter = entityAdapterByDomain.get(domain);

        if (entityAdapter != null) {
            List<Class<?>> keys = new ArrayList<>();
            for (Map.Entry<Class<?>, EntityAdapter<?, BasicTaskEntity, Object>> entry : entityAdapterMap.entrySet()) {
                if (entityAdapter == entry.getValue()) {
                    keys.add(entry.getKey());
                }
            }

            for (Class<?> k : keys) {
                entityAdapterByDomain.remove(k);
            }
        }

        return entityAdapter;

    }

    public EntityAdapter<?, BasicTaskEntity, Object> getDefaultEntityAdapter() {
        return defaultEntityAdapter;
    }

    public void setDefaultEntityAdapter(EntityAdapter<?, BasicTaskEntity, Object> defaultEntityAdapter) {
        this.defaultEntityAdapter = defaultEntityAdapter;
    }

    public EntityAdapter<?, BasicTaskEntity, Object> getDefaultEntityAdapterByDomain() {
        return defaultEntityAdapterByDomain;
    }

    public void setDefaultEntityAdapterByDomain(EntityAdapter<?, BasicTaskEntity, Object> defaultEntityAdapterByDomain) {
        this.defaultEntityAdapterByDomain = defaultEntityAdapterByDomain;
    }
}
