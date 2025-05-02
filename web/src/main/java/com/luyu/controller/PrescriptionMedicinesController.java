package com.luyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luyu.constant.PrescriptionStatus;
import com.luyu.entity.Medicines;
import com.luyu.entity.PrescriptionMedicines;
import com.luyu.entity.Prescriptions;
import com.luyu.result.Result;
import com.luyu.service.IMedicinesService;
import com.luyu.service.IPrescriptionMedicinesService;
import com.luyu.service.IPrescriptionsService;
import com.luyu.vo.PrescriptionMedicinesVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处方药品明细表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/prescription-medicines")
@RequiredArgsConstructor
public class PrescriptionMedicinesController {
    private final IPrescriptionMedicinesService prescriptionMedicinesService;
    private final IMedicinesService medicinesService;
    private final IPrescriptionsService prescriptionsService;
    /**
     * 根据处方单Id查询处方药品明细表
     * @param Id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PrescriptionMedicines:read')")
    @GetMapping("/{id}")
    public Result<List<PrescriptionMedicines>> selectId(@PathVariable("id") Integer Id) {
        if (Id == null) {
            return Result.error("查询失败");
        }
        LambdaQueryWrapper<PrescriptionMedicines> queryWrapper = new LambdaQueryWrapper<PrescriptionMedicines>();
        queryWrapper.eq(PrescriptionMedicines::getPrescriptionId, Id);
        List<PrescriptionMedicines> prescriptionMedicines = prescriptionMedicinesService.list(queryWrapper);
        return Result.success(prescriptionMedicines);
    }

    /**
     * 计算处方药品总价
     * @param
     * @return
     */
    @GetMapping("/total-price")
    public Result<PrescriptionMedicinesVO> calculateTotalIncome(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("计算已开药处方收入: startDate={}, endDate={}", startDate, endDate);

        // 处理日期范围并获取新值
        LocalDateTime[] processedDates = processDateRange(startDate, endDate);
        LocalDateTime processedStart = processedDates[0];
        LocalDateTime processedEnd = processedDates[1];

        // 查询已完成状态的处方
        List<Prescriptions> donePrescriptions = prescriptionsService.list(
                new LambdaQueryWrapper<Prescriptions>()
                        .eq(Prescriptions::getStatus, PrescriptionStatus.DONE)
                        .between(Prescriptions::getDispensedAt, processedStart, processedEnd)
                        .select(Prescriptions::getPrescriptionId)
        );

        // 处理空处方情况
        if (donePrescriptions.isEmpty()) {
            log.info("未找到已完成处方");
            return Result.success(new PrescriptionMedicinesVO(BigDecimal.ZERO));
        }

        List<Long> prescriptionIds = donePrescriptions.stream()
                .map(Prescriptions::getPrescriptionId)
                .filter(Objects::nonNull) // 过滤空ID
                .collect(Collectors.toList());

        // 再次验证ID列表
        if (prescriptionIds.isEmpty()) {
            log.warn("处方ID列表为空");
            return Result.success(new PrescriptionMedicinesVO(BigDecimal.ZERO));
        }

        // 安全查询药品明细
        List<PrescriptionMedicines> medicines = prescriptionMedicinesService.list(
                new LambdaQueryWrapper<PrescriptionMedicines>()
                        .in(PrescriptionMedicines::getPrescriptionId, prescriptionIds)
                        .select(PrescriptionMedicines::getMedicineId, PrescriptionMedicines::getQuantityDispensed)
        );

        // 计算总收入
        BigDecimal totalIncome = calculateTotalIncome(medicines);

        return Result.success(new PrescriptionMedicinesVO(totalIncome));
    }

    private LocalDateTime[] processDateRange(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
        if (start == null && end == null) {
            return new LocalDateTime[]{now.minusDays(30), now};
        } else if (start == null) {
            return new LocalDateTime[]{end.minusDays(30), end};
        } else if (end == null) {
            return new LocalDateTime[]{start, start.plusDays(30)};
        }
        return new LocalDateTime[]{start, end};
    }

    private BigDecimal calculateTotalIncome(List<PrescriptionMedicines> medicines) {
        if (medicines.isEmpty()) return BigDecimal.ZERO;

        Set<Long> medicineIds = medicines.stream()
                .map(PrescriptionMedicines::getMedicineId)
                .collect(Collectors.toSet());

        Map<Long, BigDecimal> priceMap = medicinesService.listByIds(medicineIds)
                .stream()
                .collect(Collectors.toMap(
                        Medicines::getMedicineId,
                        m -> m.getPrice() != null ? m.getPrice() : BigDecimal.ZERO
                ));

        return medicines.stream()
                .map(p -> priceMap.getOrDefault(p.getMedicineId(), BigDecimal.ZERO)
                        .multiply(BigDecimal.valueOf(p.getQuantityDispensed())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
