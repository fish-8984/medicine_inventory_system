package com.luyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luyu.constant.PrescriptionStatus;
import com.luyu.constant.TransactionType;
import com.luyu.context.BaseContext;
import com.luyu.context.InventoryLog;
import com.luyu.entity.Inventory;
import com.luyu.entity.PrescriptionMedicines;
import com.luyu.entity.Prescriptions;
import com.luyu.mapper.PrescriptionMedicinesMapper;
import com.luyu.result.Result;
import com.luyu.service.IInventoryService;
import com.luyu.service.IPrescriptionMedicinesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luyu.service.IPrescriptionsService;
import com.luyu.utils.SomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 处方药品明细表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionMedicinesServiceImpl extends ServiceImpl<PrescriptionMedicinesMapper, PrescriptionMedicines> implements IPrescriptionMedicinesService {
    private final IInventoryService inventoryService;
    private final IPrescriptionsService prescriptionsService;
    @Override
    @Transactional
    @InventoryLog(type = TransactionType.OUT,
                    collectionParam = "prescriptionMedicines", // 指定集合参数名
                    elementVar = "prescriptionMedicine",       // 元素变量名
                    batchNoArg = "#prescriptionMedicine.batchNo",
                    quantityArg = "#prescriptionMedicine.quantityDispensed",
                    relatedIdArg = "#prescriptionMedicine.prescriptionId",
                    notes = "'发药出库, 处方单ID:' + #prescriptionMedicine.prescriptionId")
    public Result<String> updateInventory(Prescriptions prescriptions, List<PrescriptionMedicines> prescriptionMedicines) {
        log.info("处方单主表和处方药品明细表: prescriptions={}, prescriptionMedicines={}", prescriptions, prescriptionMedicines);
        if (prescriptions == null || prescriptionMedicines == null || prescriptionMedicines.isEmpty()) {
            return Result.error("参数错误!");
        }
        BaseContext.setTargetId(prescriptions.getPrescriptionId());

        // 更新处方单状态为发药中
        updatePrescriptionStatus(prescriptions, PrescriptionStatus.ISSUING_MEDICINE);

        // 更新库存
        for (PrescriptionMedicines prescriptionMedicine : prescriptionMedicines) {
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<Inventory>()
                    .eq(Inventory::getBatchNo, prescriptionMedicine.getBatchNo());
            Inventory one = inventoryService.getOne(queryWrapper);

            if (one == null) {
                return Result.error("库存记录不存在!");
            }

            Integer currentQuantity = one.getCurrentQuantity();
            Integer quantityDispensed = prescriptionMedicine.getQuantityDispensed();

            int newQuantity = currentQuantity - quantityDispensed;
            if (newQuantity < 0) {
                return Result.error(prescriptionMedicine.getBatchNo() + "该批次库存不足，无法发放!");
            }
            one.setCurrentQuantity(newQuantity);
            inventoryService.updateById(one);
        }

        // 更新处方单状态为已完成
        updatePrescriptionStatus(prescriptions, PrescriptionStatus.DONE);

        return Result.success("药品发放成功，库存已更新");
    }

    private void updatePrescriptionStatus(Prescriptions prescriptions, String status) {
        LambdaUpdateWrapper<Prescriptions> updateWrapper = new LambdaUpdateWrapper<Prescriptions>()
                .eq(Prescriptions::getPrescriptionId, prescriptions.getPrescriptionId())
                .set(Prescriptions::getDispensedBy, SomeService.getUserId())
                .set(Prescriptions::getDispensedAt, LocalDateTime.now())
                .set(Prescriptions::getStatus, status);
        prescriptionsService.update(updateWrapper);
    }
}


