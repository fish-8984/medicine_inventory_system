package com.luyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.Departments;
import com.luyu.entity.Staff;
import com.luyu.result.Result;
import com.luyu.service.IDepartmentsService;
import com.luyu.service.IStaffService;
import com.luyu.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 医院科室表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentsController {
    private final IDepartmentsService idepartmentsService;
    private final IUsersService iUsersService;
    private final IStaffService iStaffService;

    /**
     * 添加部门
     * @param departments
     * @return
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('Departments:create')")
    @AuditLog(action = OperationType.INSERT, targetTable = Table.DEPARTMENTS)
    @PostMapping
    public Result<String> save(@RequestBody Departments departments){
        log.info("添加部门{}", departments);
        if (departments == null) {
            return Result.error("添加失败！");
        }
        Staff byId = iStaffService.getById(departments.getManagerId());
        if (byId == null) {
            return Result.error("该负责人不存在！");
        }
        departments.setDepartmentId(null);
        if (!idepartmentsService.save(departments)) {
            return Result.error("添加失败！");
        }
        BaseContext.setTargetId(departments.getDepartmentId());
        return Result.success("保存成功！");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('Departments:delete')")
    @AuditLog(action = OperationType.DELETE, targetTable = Table.DEPARTMENTS)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id){
        Departments departments = idepartmentsService.getById(id);
        log.info("删除部门{}", departments);
        if (departments == null || departments.getDepartmentId() == null) {
            return Result.error("删除失败！");
        }
        LambdaQueryWrapper<Staff> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Staff::getName, departments.getName());
        if (iUsersService.getById(departments.getDepartmentId()) != null
                || iStaffService.getOne(lqw) != null) {
            return Result.error("当前部门不为空！");
        }
        BaseContext.setTargetId(departments.getDepartmentId());
        if (!idepartmentsService.removeById(departments.getDepartmentId())) {
            return Result.error("删除失败！");
        }
        return Result.success("删除成功！");
    }
    /**
     * 修改部门
     * @param departments
     * @return
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('Departments:update')")
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.DEPARTMENTS)
    @PutMapping
    public Result<String> update(@RequestBody Departments departments){
        log.info("修改部门{}", departments);
        if (departments == null) {
            return Result.error("修改失败！");
        }
        LambdaUpdateWrapper<Departments> luw = new LambdaUpdateWrapper<>();
        luw.eq(Departments::getDepartmentId, departments.getDepartmentId());
        if (departments.getLocation() != null) {
            luw.set(Departments::getLocation, departments.getLocation());
        }
        if (departments.getName() != null) {
            luw.set(Departments::getName, departments.getName());
        }
        if (departments.getManagerId() != null) {
            Staff byId = iStaffService.getById(departments.getManagerId());
            if (byId == null) {
                return Result.error("该负责人不存在！");
            }
            luw.set(Departments::getManagerId, departments.getManagerId());
        }
        BaseContext.setTargetId(departments.getDepartmentId());
        if (!idepartmentsService.update(luw)) {
            return Result.error("修改失败！");
        }
        return Result.success("修改成功！");
    }

    /**
     * 分页查询所有部门
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Departments:read')")
    @GetMapping("/page")
    public Result<Page<Departments>> page(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false, defaultValue = "") String name){
        log.info("分页查询所有部门，分页参数：page={}, pageSize={}, name={}", page, pageSize, name);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);

            LambdaQueryWrapper<Departments> lqw = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(name)) {
                lqw.like(Departments::getName, name);
            }
            lqw.orderByDesc(Departments::getDepartmentId);
        Page<Departments> pageInfo = new Page<>(page, pageSize);
        idepartmentsService.page(pageInfo, lqw);
        return Result.success(pageInfo);
    }

    /**
     * 查询所有部门名称
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Departments:read')")
    @GetMapping("/nameList")
    public Result<List<String>> nameList(){
        log.info("查询所有部门");
        List<String> list = idepartmentsService.list().stream().map(Departments::getName).toList();
        return Result.success(list);
    }
}
