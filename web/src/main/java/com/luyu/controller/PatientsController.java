package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.Patients;
import com.luyu.result.Result;
import com.luyu.service.IPatientsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 患者信息表
 * @author
 * @since 2025-02-25
 */
@RestController
@Slf4j
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientsController {
    private final IPatientsService patientsService;

    /**
     * 保存患者信息
     * @param patients
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Patients:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.PATIENTS)
    @PostMapping
    public Result<String> save(@RequestBody Patients patients){
        log.info("保存患者信息{}", patients);
        if (patients == null) {
            return Result.error("保存失败");
        }
        patients.setCreatedAt(LocalDateTime.now());
        patients.setPatientId(null);
        if (!patientsService.save(patients)) {
            return Result.error("保存失败");
        }
        BaseContext.setTargetId(patients.getPatientId());
        return Result.success("保存成功");
    }

    /**
     * 更新患者信息
     */
    @PreAuthorize("hasAnyAuthority('Patients:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PATIENTS)
    @PutMapping
    public Result<String> update(@RequestBody Patients patients){
        log.info("更新患者信息{}", patients);
        if (patients == null || patients.getPatientId() == null) {
            return Result.error("更新失败");
        }
        LambdaUpdateWrapper<Patients> updateWrapper = new LambdaUpdateWrapper<Patients>()
                .eq(Patients::getPatientId, patients.getPatientId())
                .set(patients.getName() != null, Patients::getName, patients.getName())
                .set(patients.getGender() != null, Patients::getGender, patients.getGender())
                .set(patients.getBirthdate() != null, Patients::getBirthdate, patients.getBirthdate())
                .set(patients.getContactPhone() != null, Patients::getContactPhone, patients.getContactPhone())
                .set(Patients::getCreatedAt, LocalDateTime.now());
        BaseContext.setTargetId(patients.getPatientId());
        if (!patientsService.update(updateWrapper)) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    /**
     * 根据id查询患者信息
     * @param patientId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Patients:read')")
    @GetMapping("/{patientId}")
    public Result<Patients> getById(@PathVariable("patientId") Long patientId){
        log.info("根据id查询患者信息，id={}", patientId);
        if (patientId == null) {
            return Result.error("查询失败");
        }
        Patients byId = patientsService.getById(patientId);
        return Result.success(byId);
    }
    /**
     * 根据name查询患者信息
     * @param patientName
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Patients:read')")
    @GetMapping("/name/{patientName}")
    public Result<List<Patients>> getNames(@PathVariable("patientName") String patientName){
        log.info("根据name查询患者信息，name={}", patientName);
        if (patientName == null) {
            return Result.error("查询失败");
        }
        LambdaQueryWrapper<Patients> queryWrapper = new LambdaQueryWrapper<Patients>()
                .like(Patients::getName, patientName);
        List<Patients> list = patientsService.list(queryWrapper);
        return Result.success(list);
    }


    /**
     * 查询所有患者信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Patients:read')")
    @GetMapping("/page")
    public Result<Page<Patients>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "", required = false) Integer gender,
            @RequestParam(defaultValue = "", required = false) LocalDate birthdate,
            @RequestParam(defaultValue = "", required = false) String contactPhone) {

        log.info("查询所有患者信息，分页参数：page={}, pageSize={}, name={}, gender={}, birthdate={}, contactPhone={}",
                page, pageSize, name, gender, birthdate, contactPhone);
        // 参数校验
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);

        // 构建查询条件
        LambdaQueryWrapper<Patients> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Patients::getName, name)
                .eq(gender != null, Patients::getGender, gender)
                .eq(birthdate != null, Patients::getBirthdate, birthdate)
                .like(StringUtils.isNotBlank(contactPhone), Patients::getContactPhone, contactPhone)
                .orderByDesc(Patients::getCreatedAt);

        // 直接使用数据库分页查询
        Page<Patients> pageInfo = new Page<>(page, pageSize);
        patientsService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }
}

