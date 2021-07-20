package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysLogUpload;

/**
 * 日志上传查看Service接口
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
public interface ISysLogUploadService 
{
    /**
     * 查询日志上传查看
     * 
     * @param id 日志上传查看ID
     * @return 日志上传查看
     */
    public SysLogUpload selectSysLogUploadById(Long id);

    /**
     * 查询日志上传查看列表
     * 
     * @param sysLogUpload 日志上传查看
     * @return 日志上传查看集合
     */
    public List<SysLogUpload> selectSysLogUploadList(SysLogUpload sysLogUpload);

    /**
     * 新增日志上传查看
     * 
     * @param sysLogUpload 日志上传查看
     * @return 结果
     */
    public int insertSysLogUpload(SysLogUpload sysLogUpload);

    /**
     * 修改日志上传查看
     * 
     * @param sysLogUpload 日志上传查看
     * @return 结果
     */
    public int updateSysLogUpload(SysLogUpload sysLogUpload);

    /**
     * 批量删除日志上传查看
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysLogUploadByIds(String ids);

    /**
     * 删除日志上传查看信息
     * 
     * @param id 日志上传查看ID
     * @return 结果
     */
    public int deleteSysLogUploadById(Long id);
}
