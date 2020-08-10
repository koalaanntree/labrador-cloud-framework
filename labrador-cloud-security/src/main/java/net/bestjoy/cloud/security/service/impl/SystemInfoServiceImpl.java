package net.bestjoy.cloud.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.generator.IDGenerator;
import net.bestjoy.cloud.security.core.entitiy.SystemInfo;
import net.bestjoy.cloud.security.core.error.SystemExistException;
import net.bestjoy.cloud.security.persistent.repository.SystemInfoMapper;
import net.bestjoy.cloud.security.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/***
 * @author ray
 */
@Slf4j
@Service
public class SystemInfoServiceImpl implements SystemInfoService {
    @Autowired
    private SystemInfoMapper systemInfoMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveSystemInfo(SystemInfo systemInfo) throws SystemExistException {

        SystemInfo exist = getSystemInfoByName(systemInfo.getSystemName());

        if (exist != null) {
            throw new SystemExistException(systemInfo.getSystemName());
        }

        systemInfo.setSystemId(IDGenerator.SNOW_FLAKE_STRING.generate());
        systemInfo.setCreateTime(new Date());
        systemInfo.setUpdateTime(systemInfo.getCreateTime());

        systemInfoMapper.insert(systemInfo);
    }

    @Override
    public void updateSystemInfo(SystemInfo systemInfo) {
        systemInfo.setUpdateTime(new Date());

        systemInfoMapper.updateById(systemInfo);
    }

    @Override
    public SystemInfo getSystemInfoByName(String systemName) {
        QueryWrapper<SystemInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_name", systemName);

        return systemInfoMapper.selectOne(queryWrapper);
    }
}
