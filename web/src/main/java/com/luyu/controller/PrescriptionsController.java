package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.PrescriptionStatus;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.dto.PrescriptionsAndPrescriptionMedicinesDTO;
import com.luyu.entity.*;
import com.luyu.result.Result;
import com.luyu.service.*;
import com.luyu.vo.PrescriptionsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.luyu.utils.SomeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 处方单主表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionsController {
    private final IPrescriptionsService prescriptionsService;
    private final IPatientsService patientsService;
    private final IStaffService staffService;
    private final IPrescriptionMedicinesService prescriptionMedicinesService;
    private final IPrescriptionMedicinesUpdater prescriptionMedicinesUpdater;

    /**
     * 保存处方单
     * @param PrescriptionsAndPrescriptionMedicines
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Prescriptions:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.PRESCRIPTIONS)
    @PostMapping
    public Result<String> save(@RequestBody PrescriptionsAndPrescriptionMedicinesDTO PrescriptionsAndPrescriptionMedicines
    ) {
        log.info("处方单主表和处方药品明细表:{}", PrescriptionsAndPrescriptionMedicines);
        List<PrescriptionMedicines> prescriptionMedicines = PrescriptionsAndPrescriptionMedicines.getPrescriptionMedicines();
        Prescriptions prescriptions = PrescriptionsAndPrescriptionMedicines.getPrescriptions();
        if (prescriptions == null) {
            return Result.error("保存失败!");
        }
        Patients patients = patientsService.getById(prescriptions.getPatientId());
        if (patients == null) {
            return Result.error("不存在的患者!");
        }
        Staff staff = staffService.getById(prescriptions.getDoctorId());
        if (staff == null) {
            return Result.error("不存在的医生!");
        }
        prescriptions.setStatus(PrescriptionStatus.PENDING_MEDICATIONS);
        if (!prescriptionsService.save(prescriptions)) {
            return Result.error("保存失败!");
        }
        BaseContext.setTargetId(prescriptions.getPrescriptionId());
        addPrescriptionMedicines(prescriptionMedicines, prescriptions);
        return Result.success("保存成功");
    }


    @AuditLog(action = OperationType.INSERT, targetTable = Table.PRESCRIPTIONS)
    private void addPrescriptionMedicines(List<PrescriptionMedicines> prescriptionMedicines,
                                          Prescriptions prescriptions
    ) {
        for (PrescriptionMedicines prescriptionMedicine : prescriptionMedicines) {
            PrescriptionMedicines build = PrescriptionMedicines.builder()
                    .prescriptionId(prescriptions.getPrescriptionId())
                    .medicineId(prescriptionMedicine.getMedicineId())
                    .batchNo(prescriptionMedicine.getBatchNo())
                    .dosage(prescriptionMedicine.getDosage())
                    .quantityDispensed(prescriptionMedicine.getQuantityDispensed())
                    .build();
            prescriptionMedicinesService.save(build);
        }
    }

    /**
     * 开药
     * @param prescriptionsId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Prescriptions:update')")
    @Transactional
    @PutMapping("/dispensingMedicines/{prescriptionsId}")
    public Result<String> DispensingMedicines(@PathVariable Long prescriptionsId){
        log.info("处方单主表Id={}", prescriptionsId);
        if (prescriptionsId == null)  {
            return Result.error("更新失败!");
        }
        Prescriptions prescriptions = prescriptionsService.getById(prescriptionsId);
        if (prescriptions == null) {
            return Result.error("不存在的处方单!");
        }
        if (PrescriptionStatus.DONE.equals(prescriptions.getStatus())) {
            return Result.error("该处方单已开药，无法开药!");
        }
        if (PrescriptionStatus.ISSUING_MEDICINE.equals(prescriptions.getStatus())) {
            return Result.error("该处方单正在开药，无法开药!");
        }
        LambdaQueryWrapper<PrescriptionMedicines> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrescriptionMedicines::getPrescriptionId, prescriptions.getPrescriptionId());
        List<PrescriptionMedicines> list = prescriptionMedicinesService.list(queryWrapper);
        return prescriptionMedicinesService.updateInventory(prescriptions, list);
    }

    /**
     * 删除处方单
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Prescriptions:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.PRESCRIPTIONS)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id ) {
        if (id == null) {
            return Result.error("删除失败!");
        }
        Prescriptions prescriptions = prescriptionsService.getById(id);
        LambdaQueryWrapper<Prescriptions> queryWrapper = new LambdaQueryWrapper<Prescriptions>();
        queryWrapper.eq(Prescriptions::getPrescriptionId, prescriptions.getPrescriptionId());
        Prescriptions byId = prescriptionsService.getOne(queryWrapper);
        String status = byId.getStatus();
        if (PrescriptionStatus.DONE.equals(status)) {
            return Result.error("该处方单已开药，无法删除!");
        }
        BaseContext.setTargetId(prescriptions.getPrescriptionId());

        LambdaQueryWrapper<PrescriptionMedicines> lqw = new LambdaQueryWrapper<>();
        lqw.eq(PrescriptionMedicines::getPrescriptionId, prescriptions.getPrescriptionId());
        List<PrescriptionMedicines> list = prescriptionMedicinesService.list(lqw);
        for (PrescriptionMedicines prescriptionMedicines : list) {
            prescriptionMedicinesService.removeById(prescriptionMedicines);
        }
        prescriptionsService.removeById(prescriptions.getPrescriptionId());
        return Result.success("删除成功");
    }

    /**
     * 更新处方单
     * @param PrescriptionsAndPrescriptionMedicines
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Prescriptions:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PRESCRIPTIONS)
    @PutMapping
    public Result<String> update(@RequestBody PrescriptionsAndPrescriptionMedicinesDTO PrescriptionsAndPrescriptionMedicines) {
        if (PrescriptionsAndPrescriptionMedicines.getPrescriptions() == null) {
            return Result.error("更新失败!");
        }
        if (PrescriptionsAndPrescriptionMedicines.getPrescriptionMedicines() == null) {
            return Result.error("更新失败!");
        }
        log.info("处方单主表和处方药品明细表:{}", PrescriptionsAndPrescriptionMedicines);
        List<PrescriptionMedicines> prescriptionMedicines = PrescriptionsAndPrescriptionMedicines.getPrescriptionMedicines();
        Prescriptions prescriptions = PrescriptionsAndPrescriptionMedicines.getPrescriptions();
        LambdaQueryWrapper<Prescriptions> queryWrapper = new LambdaQueryWrapper<Prescriptions>();
        queryWrapper.eq(Prescriptions::getPrescriptionId, prescriptions.getPrescriptionId());
        Prescriptions ById = prescriptionsService.getOne(queryWrapper);
        if (ById == null) {
            return Result.error("不存在的处方单!");
        }
        String status = ById.getStatus();
        if (PrescriptionStatus.DONE.equals(status)) {
            return Result.error("该处方单已开药，无法更新!");
        }
        Patients patients = patientsService.getById(prescriptions.getPatientId());
        if (patients == null) {
            return Result.error("不存在的患者!");
        }
        LambdaQueryWrapper<Staff> lqw = new LambdaQueryWrapper<Staff>();
        lqw.eq(Staff::getStaffId, prescriptions.getDoctorId());
        Staff staff = staffService.getOne(lqw);
        if (staff == null) {
            return Result.error("不存在的医生!");
        }
        int i = 0;
        for (PrescriptionMedicines prescriptionMedicine : prescriptionMedicines) {
            if (!prescriptionMedicinesUpdater.updatePrescriptionMedicines(prescriptionMedicine)) {
                return Result.error(prescriptionMedicines.size() - i + "条更新失败!  " + i + "条更新成功!");
            };
            i++;
        }
        LambdaUpdateWrapper<Prescriptions> updateWrapper = new LambdaUpdateWrapper<Prescriptions>()
                .eq(Prescriptions::getPrescriptionId, prescriptions.getPrescriptionId())
                .set(prescriptions.getPatientId() != null, Prescriptions::getPatientId, prescriptions.getPatientId())
                .set(prescriptions.getDoctorId() != null, Prescriptions::getDoctorId, prescriptions.getDoctorId())
                .set(prescriptions.getDispensedBy() != null, Prescriptions::getDispensedBy, prescriptions.getDispensedBy())
                .set(Prescriptions::getDispensedAt, LocalDateTime.now());
        BaseContext.setTargetId(prescriptions.getPrescriptionId());
        if (!prescriptionsService.update(updateWrapper)) {
            return Result.error("更新失败!");
        }
        return Result.success("更新成功");
    }




    /**
     * 查询所有处方单
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Prescriptions:read')")
    @GetMapping("/page")
    public Result<Page<Prescriptions>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String patientId,
            @RequestParam(defaultValue = "", required = false) Long doctorId,
            @RequestParam(defaultValue = "", required = false) String prescriptionDate,
            @RequestParam(defaultValue = "", required = false) String status,
            @RequestParam(defaultValue = "", required = false) Long dispensedBy
    ) {
        log.info("查询所有处方单:page={},pageSize={},patientId={},doctorId={},prescriptionDate={},status={},dispensedBy={}",
                page, pageSize, patientId, doctorId, prescriptionDate, status, dispensedBy);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<Prescriptions> pageInfo = new Page<>(page, pageSize);

        List<Integer> patientIds = new ArrayList<>();
        if (StringUtils.isNotBlank(patientId)) {
            String[] split = patientId.split(",");
            for (String s : split) {
                if (StringUtils.isNumeric(s)) {
                    patientIds.add(Integer.parseInt(s));
                }
            }
        }

        LambdaQueryWrapper<Prescriptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!patientIds.isEmpty(), Prescriptions::getPatientId, patientIds)
                .eq(doctorId != null, Prescriptions::getDoctorId, doctorId)
                .like(prescriptionDate != null, Prescriptions::getPrescriptionDate, prescriptionDate)
                .eq(StringUtils.isNotBlank(status), Prescriptions::getStatus, status)
                .eq(dispensedBy != null, Prescriptions::getDispensedBy, dispensedBy);
        queryWrapper.orderByDesc(Prescriptions::getDispensedAt);
        prescriptionsService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }


    @GetMapping("/count")
    public Result<PrescriptionsVO> count(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        // 处理日期范围（保持原有逻辑）
        LocalDateTime now = LocalDateTime.now();
        if (startDate == null && endDate == null) {
            startDate = now.minusDays(30);
            endDate = now;
        } else if (startDate == null) {
            startDate = endDate.minusDays(30);
        } else if (endDate == null) {
            endDate = startDate.plusDays(30);
        }

        // 分别查询各状态数量
        PrescriptionsVO vo = new PrescriptionsVO();
        vo.setPendingCount(queryCountByStatus(PrescriptionStatus.PENDING_MEDICATIONS, startDate, endDate));
        vo.setProcessingCount(queryCountByStatus(PrescriptionStatus.ISSUING_MEDICINE, startDate, endDate));
        vo.setDoneCount(queryCountByStatus(PrescriptionStatus.DONE, startDate, endDate));

        return Result.success(vo);
    }

    private Integer queryCountByStatus(String status, LocalDateTime start, LocalDateTime end) {
        LambdaQueryWrapper<Prescriptions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Prescriptions::getStatus, status);

        if ("2".equals(status)) {
            wrapper.between(Prescriptions::getDispensedAt, start, end);
        } else {
            wrapper.between(Prescriptions::getPrescriptionDate, start, end);
        }

        return Math.toIntExact(prescriptionsService.count(wrapper));
    }
}
