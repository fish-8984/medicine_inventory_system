package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.OrdersStatus;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.Medicines;
import com.luyu.entity.PurchaseOrders;
import com.luyu.entity.PurchaseRecords;
import com.luyu.result.Result;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IMedicinesService;
import com.luyu.service.IPurchaseOrdersService;
import com.luyu.service.IPurchaseRecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 采购明细记录表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/purchase-records")
@RequiredArgsConstructor
public class PurchaseRecordsController {
    private final IPurchaseRecordsService purchaseRecordsService;
    private final IPurchaseOrdersService purchaseOrdersService;
    private final IMedicinesService medicinesService;
    private final IMedicineBatchesService medicineBatchesService;

    /**
     * 保存采购明细记录表
     * @param purchaseRecords
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseRecords:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.PURCHASE_RECORDS)
    @PostMapping
    public Result<String> save(@RequestBody PurchaseRecords purchaseRecords) {
        if (purchaseRecords == null) {
            return Result.error("保存失败!");
        }
        log.info("保存采购明细记录表:{}", purchaseRecords);
        String msg = verify(purchaseRecords);
        if (msg != null) {
            return Result.error(msg);
        }
        purchaseRecords.setRecordId(null);
        if (!purchaseRecordsService.save(purchaseRecords)) {
            return Result.error("保存失败!");
        }
        BaseContext.setTargetId(purchaseRecords.getRecordId());
        return Result.success("保存成功");
    }


    /**
     * 更新采购明细记录表
     * @param purchaseRecords
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseRecords:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PURCHASE_RECORDS)
    @PutMapping
    public Result<String> update(@RequestBody PurchaseRecords purchaseRecords) {
        if (purchaseRecords == null) {
            return Result.error("更新失败!");
        }
        log.info("更新采购明细记录表:{}", purchaseRecords);
        String msg = verify(purchaseRecords);
        if (msg != null) {
            return Result.error(msg);
        }
        PurchaseOrders byId = purchaseOrdersService.getById(purchaseRecords.getPoId());
        String status = byId.getStatus();
        if(!status.equals(OrdersStatus.PENDING)) {
            return Result.error("订单状态不允许修改!");
        }
        LambdaUpdateWrapper<PurchaseRecords> updateWrapper = new LambdaUpdateWrapper<PurchaseRecords>()
                .eq(PurchaseRecords::getRecordId, purchaseRecords.getRecordId())
                .set(purchaseRecords.getPoId() != null, PurchaseRecords::getPoId, purchaseRecords.getPoId())
                .set(purchaseRecords.getMedicineId() != null, PurchaseRecords::getMedicineId, purchaseRecords.getMedicineId())
                .set(purchaseRecords.getBatchNo() != null, PurchaseRecords::getBatchNo, purchaseRecords.getBatchNo())
                .set(purchaseRecords.getQuantity() != null, PurchaseRecords::getQuantity, purchaseRecords.getQuantity())
                .set(purchaseRecords.getUnitPrice() != null, PurchaseRecords::getUnitPrice, purchaseRecords.getUnitPrice());
        BaseContext.setTargetId(purchaseRecords.getRecordId());
        if (!purchaseRecordsService.update(updateWrapper)) {
            return Result.error("更新失败!");
        }
        return Result.success("更新成功");
    }

    /**
     * 根据主键查询采购明细记录表
     * @param recordId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseRecords:update')")
    @GetMapping("/{recordId}")
    public Result<PurchaseRecords> getById(@PathVariable("recordId") Long recordId) {
        log.info("根据主键查询采购明细记录表:{}", recordId);
        PurchaseRecords purchaseRecords = purchaseRecordsService.getById(recordId);
        if (purchaseRecords == null) {
            return Result.error("采购明细记录表不存在!");
        }
        return Result.success(purchaseRecords);
    }

    /**
     * 验证数据
     * @param purchaseRecords
     * @return
     */
    private  String verify(PurchaseRecords purchaseRecords) {
        PurchaseOrders purchaseOrders = purchaseOrdersService.getById(purchaseRecords.getPoId());
        if (purchaseOrders == null) {
            return "采购订单不存在!";
        }
        Medicines medicines = medicinesService.getById(purchaseRecords.getMedicineId());
        if (medicines == null) {
            return "药品不存在!";
        }
        MedicineBatches medicineBatches = medicineBatchesService.getById(purchaseRecords.getBatchNo());
        if (medicineBatches == null) {
            return "药品批次不存在!";
        }
        return null;
    }

    /**
     * 查询所有采购明细记录表
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseRecords:read')")
    @GetMapping("/page")
    public Result<Page<PurchaseRecords>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String medicineId,
            @RequestParam(defaultValue = "", required = false) String batchNo
    ) {
        log.info("查询所有采购明细记录表: page={}, pageSize={}, medicineId={}, batchNo={}",
                page, pageSize, medicineId, batchNo);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        List<Integer> medicineIds = new ArrayList<>();
        if (StringUtils.isNotBlank(medicineId)) {
            String[] split = medicineId.split(",");
            for (String s : split) {
                if (StringUtils.isNumeric(s)) {
                    medicineIds.add(Integer.parseInt(s));
                }
            }
        }
        Page<PurchaseRecords> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<PurchaseRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!medicineIds.isEmpty(), PurchaseRecords::getMedicineId, medicineIds);
        queryWrapper.like(StringUtils.isNotBlank(batchNo), PurchaseRecords::getBatchNo, batchNo);
        queryWrapper.orderByDesc(PurchaseRecords::getRecordId);
        purchaseRecordsService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }
}
