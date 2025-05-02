package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.StatusConstant;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.Departments;
import com.luyu.entity.Staff;
import com.luyu.result.Result;
import com.luyu.service.IDepartmentsService;
import com.luyu.service.IStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医护人员信息表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
    private final IStaffService staffService;
    private final IDepartmentsService departmentsService;

    /**
     * 保存医护人员信息
     * @param staff
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.STAFF)
    @PostMapping
    public Result<String> save(@RequestBody Staff staff){
        log.info("保存医护人员信息:{}", staff);
        if (staff == null) {
            return Result.error("保存失败!");
        }
        LambdaQueryWrapper<Departments> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Departments::getName, staff.getDepartment());
        Departments byId = departmentsService.getOne(lqw);
        if (byId == null) {
            return Result.error("不存在该科室!");
        }
        staff.setCreatedAt(LocalDateTime.now());
        staff.setStaffId(null);
        staff.setIsActive(StatusConstant.ENABLE);
        if (!staffService.save(staff)) {
            return Result.error("保存失败!");
        }
        BaseContext.setTargetId(staff.getStaffId());
        log.info("保存医护人员id:{}", staff.getStaffId());
        return Result.success("保存成功");
    }

    /**
     * 删除医护人员信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.STAFF)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Integer id ){
        if (id == null) {
            return Result.error("删除失败!");
        }
        Staff staff = staffService.getById(id);
        log.info("删除医护人员信息:{}", staff);
        LambdaUpdateWrapper<Staff> luw = new LambdaUpdateWrapper<>();
        luw.eq(Staff::getStaffId, staff.getStaffId());
        luw.set(Staff::getIsActive, StatusConstant.DISABLE);
        BaseContext.setTargetId(staff.getStaffId());
        if (!staffService.update(luw)) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    /**
     * 更新医护人员信息
     * @param staff
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.STAFF)
    @PutMapping
    public Result<String> update(@RequestBody Staff staff){
        log.info("更新医护人员信息:{}", staff);
        if (staff == null || staff.getStaffId() == null) {
            return Result.error("更新失败!");
        }
        if (staff.getDepartment() != null) {
            LambdaQueryWrapper<Departments> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Departments::getName, staff.getDepartment());
            Departments byId = departmentsService.getOne(lqw);
            if (byId == null) {
                return Result.error("不存在该科室!");
            }
        }
        BaseContext.setTargetId(staff.getStaffId());
        log.info("更新医护人员id:{}", BaseContext.getTargetId());
        LambdaUpdateWrapper<Staff> luw = new LambdaUpdateWrapper<Staff>()
                .eq(Staff::getStaffId, staff.getStaffId())
                .set(staff.getName() != null, Staff::getName, staff.getName())
                .set(staff.getTitle() != null, Staff::getTitle, staff.getTitle())
                .set(staff.getDepartment() != null, Staff::getDepartment, staff.getDepartment())
                .set(staff.getIsActive() != null, Staff::getIsActive, staff.getIsActive());
        if (!staffService.update(luw)) {
            return Result.error("更新失败!");
        }
        return Result.success("更新成功");
    }

    /**
     * 查询医护人员信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:read')")
    @GetMapping
    public Result<Staff> get(@RequestParam("id") Integer id){
        log.info("查询医护人员信息:{}", id);
        if (id == null) {
            return Result.error("查询失败!");
        }
        return Result.success(staffService.getById(id));
    }

    /**
     * 查询医护人员信息
     * @param name
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:read')")
    @GetMapping("/name")
    public Result<Staff> getName(@RequestParam("name") String name){
        log.info("查询医护人员信息:{}", name);
        if (name == null) {
            return Result.error("查询失败!");
        }
        LambdaQueryWrapper<Staff> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Staff::getName, name);
        return Result.success(staffService.getOne(lqw));
    }

    /**
     * 查询所有医护人员信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Staff:read')")
    @GetMapping("/page")
    public Result<Page<Staff>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "", required = false) String title,
            @RequestParam(defaultValue = "", required = false) String department,
            @RequestParam(defaultValue = "", required = false) Integer isActive
    ){
        log.info("查询所有医护人员信息:page={},pageSize={},name={},title{},department={},isActive={}", page, pageSize, name, title, department, isActive);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<Staff> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Staff> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(name), Staff::getName, name)
                .like(StringUtils.isNotBlank(title), Staff::getTitle, title)
                .like(StringUtils.isNotBlank(department), Staff::getDepartment, department)
                .eq(isActive != null, Staff::getIsActive, isActive);
        lqw.orderByDesc(Staff::getCreatedAt);
        staffService.page(pageInfo, lqw);
        return Result.success(pageInfo);
    }
}
