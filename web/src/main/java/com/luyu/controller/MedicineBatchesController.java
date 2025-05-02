package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.*;
import com.luyu.result.Result;
import com.luyu.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 药品批次管理表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/medicine-batches")
@RequiredArgsConstructor
public class MedicineBatchesController {
    private final IMedicineBatchesService medicineBatchesService;
    private final IMedicinesService medicinesService;
    private final ISuppliersService suppliersService;
    private final IInventoryTransactionsService inventoryTransactionsService;


    /**
     * 保存药品批次
     * @param medicineBatches
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineBatches:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.MEDICINE_BATCHES)
    @PostMapping
    public Result<String> save(@RequestBody MedicineBatches medicineBatches)  {
        if (medicineBatches == null) {
            return Result.error("保存失败");
        }
        log.info("添加药品批次 | 批次号:{}", medicineBatches.getBatchNo());
        Medicines medicines = medicinesService.getById(medicineBatches.getMedicineId());
        if (medicines == null) {
            return Result.error("药品不存在!");
        }
        Suppliers suppliers = suppliersService.getById(medicineBatches.getSupplierId());
        if (suppliers == null) {
            return Result.error("供应商不存在!");
        }
        BaseContext.setTargetIdString(medicineBatches.getBatchNo());
        medicineBatches.setCreatedAt(LocalDateTime.now());
        if (!medicineBatchesService.save(medicineBatches)) {
            return Result.error("保存失败!");
        }
        log.info("添加库存记录 | 批次号:{}", BaseContext.getTargetIdString());
        Inventory inventory = inventoryTransactionsService.addInventory(medicineBatches);
        inventoryTransactionsService.insert(inventory);
        return Result.success("保存成功");
    }


    /**
     * 查询单个药品批次
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineBatches:read')")
    @GetMapping("/{id}")
    public Result<List<MedicineBatches>> selectById(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error("查询失败");
        }
        log.info("查询单个药品批次 | id:{}", id);
        LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<MedicineBatches>()
                .eq(MedicineBatches::getMedicineId, id);
        List<MedicineBatches> list = medicineBatchesService.list(queryWrapper);
        return Result.success(list);
    }


    /**
     * 查询所有药品批次
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineBatches:read')")
    @GetMapping
    public Result<Page<MedicineBatches>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String batchNo
    ) {
        log.info("查询所有药品批次 | page:{}, pageSize={}, batchNo={}", page, pageSize, batchNo);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<MedicineBatches> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<MedicineBatches> lqw = new LambdaQueryWrapper<>();
        lqw.like(batchNo != null, MedicineBatches::getBatchNo, batchNo);
        lqw.orderByDesc(MedicineBatches::getCreatedAt);
        medicineBatchesService.page(pageInfo, lqw);
        return Result.success(pageInfo);
    }
}
