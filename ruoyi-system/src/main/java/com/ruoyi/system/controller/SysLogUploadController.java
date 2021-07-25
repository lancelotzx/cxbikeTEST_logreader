package com.ruoyi.system.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysLogUpload;
import com.ruoyi.system.service.ISysLogUploadService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日志上传查看Controller,add部分和upload部分进行整合，不会手动add，每次用户upload之后都会自动新增一条，
 *
 * @author ruoyi
 * @date 2021-07-20
 */
@Controller
@RequestMapping("/system/upload")
public class SysLogUploadController extends BaseController
{
    private String prefix = "system/upload";

    @Autowired
    private ISysLogUploadService sysLogUploadService;

    @RequiresPermissions("system:upload:view")
    @GetMapping()
    public String upload()
    {
        return prefix + "/upload";
    }

    /**
     * 查询日志上传查看列表
     */
    @RequiresPermissions("system:upload:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLogUpload sysLogUpload)
    {
        startPage();
        List<SysLogUpload> list = sysLogUploadService.selectSysLogUploadList(sysLogUpload);
        return getDataTable(list);
    }

    /**
     * 导出日志上传查看列表
     */
    @RequiresPermissions("system:upload:export")
    @Log(title = "日志上传查看", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLogUpload sysLogUpload)
    {
        List<SysLogUpload> list = sysLogUploadService.selectSysLogUploadList(sysLogUpload);
        ExcelUtil<SysLogUpload> util = new ExcelUtil<SysLogUpload>(SysLogUpload.class);
        return util.exportExcel(list, "日志上传查看数据");
    }

    /**
     * 新增日志上传查看
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存日志上传查看
     */
    @RequiresPermissions("system:upload:add")
    @Log(title = "日志上传查看", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysLogUpload sysLogUpload)
    {
        return toAjax(sysLogUploadService.insertSysLogUpload(sysLogUpload));
    }

    /**
     * 修改日志上传查看
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysLogUpload sysLogUpload = sysLogUploadService.selectSysLogUploadById(id);
        mmap.put("sysLogUpload", sysLogUpload);
        return prefix + "/edit";
    }


    /**
     * 日志解析by wj
     */
    @GetMapping("/edit2/{id}")
    public String edit2(@PathVariable("id") Long id, ModelMap mmap)
    {
       // SysLogUpload sysLogUpload = sysLogUploadService.selectSysLogUploadById(id);
       // mmap.put("sysLogUpload", sysLogUpload);
        return "system/server/server";
    }

    /**
     * 修改保存日志上传查看
     */
    @RequiresPermissions("system:upload:edit")
    @Log(title = "日志上传查看", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysLogUpload sysLogUpload)
    {
        return toAjax(sysLogUploadService.updateSysLogUpload(sysLogUpload));
    }

    /**
     * 删除日志上传查看
     */
    @RequiresPermissions("system:upload:remove")
    @Log(title = "日志上传查看", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysLogUploadService.deleteSysLogUploadByIds(ids));
    }
}
