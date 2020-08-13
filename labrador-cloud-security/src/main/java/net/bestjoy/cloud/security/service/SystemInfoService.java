package net.bestjoy.cloud.security.service;


import net.bestjoy.cloud.security.core.entitiy.SystemInfo;
import net.bestjoy.cloud.security.core.error.SystemExistException;

/**
 * @author ray
 */
public interface SystemInfoService {
    /***
     * 保存系统信息
     * @param systemInfo
     */
    void saveSystemInfo(SystemInfo systemInfo) throws SystemExistException;

    /***
     * 更新系统信息
     *
     * @param systemInfo
     */
    void updateSystemInfo(SystemInfo systemInfo);

    /**
     * 根据系统名查找
     *
     * @param systemName
     * @return
     */
    SystemInfo getSystemInfoByName(String systemName);
}
