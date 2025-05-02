package com.luyu.service.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.entity.Inventory;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.Medicines;
import com.luyu.properties.EmailProperties;
import com.luyu.service.IInventoryService;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IMedicinesService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.luyu.service.EmailService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryMonitoring {
    private final IInventoryService inventoryService;
    private final IMedicineBatchesService medicineBatchesService;
    private final IMedicinesService medicinesService;
    private final EmailService emailService;
    private final EmailProperties emailProperties;

    // 库存核实（每天凌晨1点执行）
    @Scheduled(cron = "0 0 1 * * ?")
    public void dailyStockCheck() {
        int page = 0;
        int pageSize = 100;
        // 药品批次集合
        List<String> batchNoList = new ArrayList<>();
        // 当前库存
        List<Integer> currentQuantityList = new ArrayList<>();
        // 当前库存阈值
        List<Integer> minStockList = new ArrayList<>();
        Page<Inventory> pageResult = new Page<>(page, pageSize);
        inventoryService.page(pageResult);
        // 分页查询库存
        do {
            pageResult.getRecords().forEach(inventory -> {
                if (inventory.getCurrentQuantity() < inventory.getMinStock()) {
                    batchNoList.add(inventory.getBatchNo());
                    currentQuantityList.add(inventory.getCurrentQuantity());
                    minStockList.add(inventory.getMinStock());
                }
            });
            page++;
            // 重新执行分页查询，获取新的结果
            inventoryService.page(pageResult);
        } while (pageResult.getRecords().size() == pageSize);


        // 药品id号集合
        List<Long> medicineIdList = new ArrayList<>();
        if (!batchNoList.isEmpty()) {
            LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(MedicineBatches::getBatchNo, batchNoList);  // 使用in条件一次性查询所有批次号
            List<MedicineBatches> medicineBatchesList = medicineBatchesService.list(queryWrapper);
            for (MedicineBatches medicineBatches : medicineBatchesList) {
                medicineIdList.add(medicineBatches.getMedicineId());
            }
        }
        // 药品名称集合
        List<String> medicineName = new ArrayList<>();
        if (!medicineIdList.isEmpty()) {
            LambdaQueryWrapper<Medicines> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Medicines::getMedicineId, medicineIdList);
            List<Medicines> medicinesList = medicinesService.list(queryWrapper);
            for (Medicines medicines : medicinesList) {
                medicineName.add(medicines.getName());
            }
        }
// 库存不足预警
        if (!batchNoList.isEmpty()) {
            emailService.sendInventoryAlert(emailProperties.to, batchNoList, currentQuantityList, minStockList, medicineIdList, medicineName);
        }
    }
}
