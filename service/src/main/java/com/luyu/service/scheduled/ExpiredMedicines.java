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
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.luyu.service.EmailService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpiredMedicines {
    private final IInventoryService inventoryService;
    private final IMedicineBatchesService medicineBatchesService;
    private final IMedicinesService medicinesService;
    private final EmailService emailService;
    private final EmailProperties emailProperties;


    // 新增定时任务方法（每天凌晨1点执行）
    @Scheduled(cron = "0 0 1 * * ?")
    public void dailyMedicationCheck() {
        int page = 0;
        int pageSize = 100;
        // 库存不为0 药品批次集合
        List<String> batchNoList = new ArrayList<>();
        Page<Inventory> pageResult = new Page<>(page, pageSize);
        inventoryService.page(pageResult);
        // 分页查询库存
        do {
            pageResult.getRecords().forEach(inventory -> {
                if (inventory.getCurrentQuantity() != 0) {
                    batchNoList.add(inventory.getBatchNo());
                }
            });
            page++;
            // 下一页
            inventoryService.page(pageResult);
        } while (pageResult.getRecords().size() == pageSize);

        // 药品批次号
        List<String> RemainingDatesBatchNo = new ArrayList<>();
        // 过期药品id
        List<Long> medicineIdList = new ArrayList<>();
        // 剩余日期 或 已过期
        List<String> RemainingDates = new ArrayList<>();
        if (!batchNoList.isEmpty()) {
            LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(MedicineBatches::getBatchNo, batchNoList);
            List<MedicineBatches> medicineBatchesList = medicineBatchesService.list(queryWrapper);

            for (MedicineBatches medicineBatches : medicineBatchesList) {
                medicineIdList.add(medicineBatches.getMedicineId());
                RemainingDatesBatchNo.add(medicineBatches.getBatchNo());

                LocalDate endDate = medicineBatches.getExpiryDate();
                LocalDate startDate = LocalDate.now();
                long days = ChronoUnit.DAYS.between(startDate, endDate);

                if (days >= 0) {
                    // 未过期
                    if (days < 30) {
                        RemainingDates.add("剩余" + days + "天");
                    }
                } else {
                    // 已过期
                    RemainingDates.add("已过期" + Math.abs(days) + "天");
                }
            }
        }

        List<String> medicineName = new ArrayList<>();
        // 查询药品信息
        if (!medicineIdList.isEmpty()) {
            LambdaQueryWrapper<Medicines> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Medicines::getMedicineId, medicineIdList);
            List<Medicines> list = medicinesService.list(queryWrapper);
            for (Medicines medicines : list) {
                medicineName.add(medicines.getName());
            }
        }

// 药品效期预警
        if (!RemainingDatesBatchNo.isEmpty()) {
            emailService.sendMedicationAlert(emailProperties.to, RemainingDatesBatchNo, medicineIdList, medicineName, RemainingDates);
        }
    }
}
