package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 日志上传查看对象 sys_log_upload
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
public class SysLogUpload extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "")
    private String logName;

    /**  */
    private Long id;

    /**  */
    @Excel(name = "")
    private String logUrl;

    public void setLogName(String logName) 
    {
        this.logName = logName;
    }

    public String getLogName() 
    {
        return logName;
    }
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogUrl(String logUrl) 
    {
        this.logUrl = logUrl;
    }

    public String getLogUrl() 
    {
        return logUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logName", getLogName())
            .append("id", getId())
            .append("logUrl", getLogUrl())
            .toString();
    }
}
