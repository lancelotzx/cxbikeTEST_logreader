package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysLogUploadMapper;
import com.ruoyi.system.domain.SysLogUpload;
import com.ruoyi.system.service.ISysLogUploadService;
import com.ruoyi.common.core.text.Convert;

/**
 * 日志上传查看Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
@Service
public class SysLogUploadServiceImpl implements ISysLogUploadService 
{
    @Autowired
    private SysLogUploadMapper sysLogUploadMapper;

    /**
     * 查询日志上传查看
     * 
     * @param id 日志上传查看ID
     * @return 日志上传查看
     */
    @Override
    public SysLogUpload selectSysLogUploadById(Long id)
    {
        return sysLogUploadMapper.selectSysLogUploadById(id);
    }

    /**
     * 查询日志上传查看列表
     * 
     * @param sysLogUpload 日志上传查看
     * @return 日志上传查看
     */
    @Override
    public List<SysLogUpload> selectSysLogUploadList(SysLogUpload sysLogUpload)
    {
        return sysLogUploadMapper.selectSysLogUploadList(sysLogUpload);
    }

    /**
     * 新增日志上传查看
     * 
     * @param sysLogUpload 日志上传查看
     * @return 结果
     */
    @Override
    public int insertSysLogUpload(SysLogUpload sysLogUpload)
    {
        return sysLogUploadMapper.insertSysLogUpload(sysLogUpload);
    }

    /**
     * 修改日志上传查看
     * 
     * @param sysLogUpload 日志上传查看
     * @return 结果
     */
    @Override
    public int updateSysLogUpload(SysLogUpload sysLogUpload)
    {
        return sysLogUploadMapper.updateSysLogUpload(sysLogUpload);
    }

    /**
     * 删除日志上传查看对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysLogUploadByIds(String ids)
    {
        return sysLogUploadMapper.deleteSysLogUploadByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除日志上传查看信息
     * 
     * @param id 日志上传查看ID
     * @return 结果
     */
    @Override
    public int deleteSysLogUploadById(Long id)
    {
        return sysLogUploadMapper.deleteSysLogUploadById(id);
    }
}
