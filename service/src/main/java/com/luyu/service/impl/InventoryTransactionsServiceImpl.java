package com.luyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.constant.Thresholds;
import com.luyu.constant.TransactionType;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.context.InventoryLog;
import com.luyu.entity.Inventory;
import com.luyu.entity.InventoryTransactions;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.StockAlerts;
import com.luyu.mapper.InventoryTransactionsMapper;
import com.luyu.service.IInventoryService;
import com.luyu.service.IInventoryTransactionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IStockAlertsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 库存流水记录表（支撑AI分析） 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryTransactionsServiceImpl extends ServiceImpl<InventoryTransactionsMapper, InventoryTransactions> implements IInventoryTransactionsService {
    private final IInventoryService inventoryService;
    private final IStockAlertsService stockAlertsService;

    /**
     * 添加库存
     * @param medicineBatches
     */
    @Override
    @InventoryLog(type = TransactionType.IN,
            batchNoArg = "#medicineBatches.batchNo",
            quantityArg = "#medicineBatches.initialQuantity",
            relatedIdArg = "#medicineBatches.medicineId",
            notes = "'采购入库, 供应商ID:' + #medicineBatches.supplierId"
    )
    @Transactional
    public Inventory addInventory(MedicineBatches medicineBatches) {
        // 参数校验
        if (medicineBatches == null || medicineBatches.getBatchNo() == null ||
                medicineBatches.getInitialQuantity() == null || medicineBatches.getMedicineId() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        log.info("添加库存 | 批次号:{}", medicineBatches.getBatchNo());
        // 获取库存预警阈值
        Integer threshold = getStockThreshold(medicineBatches.getMedicineId());

        Inventory build;
        synchronized (this) {
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getBatchNo, medicineBatches.getBatchNo());
            Inventory one = inventoryService.getOne(queryWrapper);
            if (one != null) {
                build = one;
                build.setCurrentQuantity(build.getCurrentQuantity() + medicineBatches.getInitialQuantity());
                inventoryService.updateById(build);
            }
            build = Inventory.builder()
                    .batchNo(medicineBatches.getBatchNo())
                    .currentQuantity(medicineBatches.getInitialQuantity())
                    .minStock(threshold)
                    .lastRestocked(LocalDateTime.now())
                    .lastUsed(LocalDateTime.now())
                    .build();
            inventoryService.save(build);
        }
        return build;
    }

    // 提取获取库存阈值逻辑
    private Integer getStockThreshold(Long medicineId) {
        log.info("获取库存阈值 | 药品ID:{}", medicineId);
        LambdaQueryWrapper<StockAlerts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StockAlerts::getMedicineId, medicineId);
        StockAlerts stockAlert = stockAlertsService.getOne(queryWrapper);
        return stockAlert != null ? stockAlert.getMinQuantity() : Thresholds.MIN_INVENTORY;
    }


    @Override
    @AuditLog(action = OperationType.INSERT, targetTable = Table.INVENTORY)
    public void insert(Inventory inventory) {
        BaseContext.setTargetId(inventory.getInventoryId());
    }
}
