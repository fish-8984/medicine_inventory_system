package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.constant.Thresholds;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.Inventory;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.StockAlerts;
import com.luyu.result.Result;
import com.luyu.service.IInventoryService;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IStockAlertsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存预警规则配置表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/stock-alerts")
@RequiredArgsConstructor
public class StockAlertsController {
    private final IStockAlertsService stockAlertsService;
    private final IMedicineBatchesService medicineBatchesService;
    private final IInventoryService inventoryService;

    /**
     * 保存库存预警规则
     * @param stockAlerts
     * @return
     */
    @PreAuthorize("hasAnyAuthority('StockAlerts:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.stockAlerts)
    @PostMapping
    public Result<String> save(@RequestBody StockAlerts stockAlerts) {
        log.info("保存库存预警规则:{}", stockAlerts);
        if (stockAlerts == null) {
            return Result.error("保存失败!");
        }
        LambdaQueryWrapper<StockAlerts> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StockAlerts::getMedicineId, stockAlerts.getMedicineId());
        StockAlerts one = stockAlertsService.getOne(lqw);
        if (one != null) {
            return Result.error("该药品已存在库存预警规则!");
        }
        LambdaQueryWrapper<MedicineBatches> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(MedicineBatches::getMedicineId, stockAlerts.getMedicineId());
        List<MedicineBatches> list = medicineBatchesService.list(lqw1);
        if (list == null) {
            return Result.error("该药品没有库存批次!");
        }
        if (stockAlerts.getMinQuantity() <= 0) {
            return Result.error("最小库存量必须大于0!");
        }
        stockAlerts.setIsEnabled(true);
        stockAlerts.setAlertId(null);
        if (!stockAlertsService.save(stockAlerts)) {
            return Result.error("保存失败!");
        }
        for (MedicineBatches medicineBatches : list) {
            LambdaUpdateWrapper<Inventory> luw = new LambdaUpdateWrapper<>();
            luw.eq(Inventory::getBatchNo, medicineBatches.getBatchNo());
            luw.set(Inventory::getMinStock, stockAlerts.getMinQuantity());
            BaseContext.setTargetId(stockAlerts.getAlertId());
            inventoryService.update(luw);
        }
        return Result.success("保存成功");
    }

    /**
     * 删除库存预警规则
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('StockAlerts:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.stockAlerts)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error("删除失败!");
        }
        StockAlerts stockAlerts = stockAlertsService.getById(id);
        log.info("删除库存预警规则:{}", stockAlerts);
        if (stockAlerts == null) {
            return Result.error("规则不存在!");
        }
        if (stockAlerts.getIsEnabled()) {
            return Result.error("已启用的规则无法删除!");
        }
        if (!stockAlertsService.removeById(stockAlerts.getAlertId())) {
            return Result.error("删除失败!");
        }
        BaseContext.setTargetId(stockAlerts.getAlertId());
        updateInventory(stockAlerts);
        return Result.success("删除成功");
    }

    /**
     * 更新库存预警规则
     * @param stockAlerts
     * @return
     */
    @PreAuthorize("hasAnyAuthority('StockAlerts:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.stockAlerts)
    @PutMapping
    public Result<String> update(@RequestBody StockAlerts stockAlerts) {
        log.info("更新库存预警规则:{}", stockAlerts);
        if (stockAlerts == null) {
            return Result.error("更新失败!");
        }
        LambdaQueryWrapper<StockAlerts> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(StockAlerts::getAlertId, stockAlerts.getAlertId());
        StockAlerts one1 = stockAlertsService.getOne(lqw1);
        if (one1.getIsEnabled()) {
            return Result.error("已启用的规则无法更新!");
        }
        LambdaUpdateWrapper<StockAlerts> luw = new LambdaUpdateWrapper<>();
        luw.eq(StockAlerts::getAlertId, stockAlerts.getAlertId());
        if (stockAlerts.getMedicineId() != null) {
            LambdaQueryWrapper<MedicineBatches> medicineBatchesLambdaQueryWrapper = new LambdaQueryWrapper<>();
            medicineBatchesLambdaQueryWrapper.eq(MedicineBatches::getMedicineId, stockAlerts.getMedicineId());
            List<MedicineBatches> list = medicineBatchesService.list(medicineBatchesLambdaQueryWrapper);
            if (list == null) {
                return Result.error("该药品没有库存批次!");
            }
            LambdaQueryWrapper<StockAlerts> lqw = new LambdaQueryWrapper<>();
            lqw.eq(StockAlerts::getMedicineId, stockAlerts.getMedicineId());
            luw.set(StockAlerts::getMedicineId, stockAlerts.getMedicineId());
        }
        if (stockAlerts.getMinQuantity() != null) {
            if (stockAlerts.getMinQuantity() <= 0) {
                return Result.error("最小库存量必须大于0!");
            }
            luw.set(StockAlerts::getMinQuantity, stockAlerts.getMinQuantity());
        }
        if (!stockAlertsService.update(luw)) {
            return Result.error("更新失败!");
        }
        BaseContext.setTargetId(stockAlerts.getAlertId());
        updateInventory(stockAlerts);
        return Result.success("更新成功");
    }

    private void updateInventory(StockAlerts stockAlerts) {
        MedicineBatches byId = medicineBatchesService.getById(stockAlerts.getMedicineId());
        if (byId == null) {
            return;
        }
        LambdaUpdateWrapper<Inventory> luw1 = new LambdaUpdateWrapper<>();
        luw1.eq(Inventory::getBatchNo, byId.getBatchNo());
        luw1.set(Inventory::getMinStock, stockAlerts.getMinQuantity());
        inventoryService.update(luw1);
    }

    /**
     * 启用/禁用库存预警规则
     * @param stockAlerts
     * @return
     */
    @PreAuthorize("hasAnyAuthority('StockAlerts:update')")
    @Transactional
    @PutMapping("/is-enabled")
    public Result<String> updateIsEnabled(@RequestBody StockAlerts stockAlerts) {
        log.info("启用/禁用库存预警规则: {}", stockAlerts);

        // 参数校验
        if (stockAlerts == null || stockAlerts.getAlertId() == null) {
            return Result.error("更新失败，参数无效!");
        }

        try {
            // 更新库存预警规则的状态
            LambdaUpdateWrapper<StockAlerts> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(StockAlerts::getAlertId, stockAlerts.getAlertId())
                    .set(StockAlerts::getIsEnabled, stockAlerts.getIsEnabled());
            stockAlertsService.update(updateWrapper);

            // 根据启用/禁用状态更新库存信息
            if (stockAlerts.getIsEnabled()) {
                updateInventoryForEnabled(stockAlerts);
            } else {
                resetInventoryToDefault();
            }

            log.info("库存预警规则更新成功: {}", stockAlerts);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新库存预警规则失败: {}", stockAlerts, e);
            return Result.error("更新失败，服务器内部错误");
        }
    }

    private void updateInventoryForEnabled(StockAlerts stockAlerts) {
        LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicineBatches::getMedicineId, stockAlerts.getMedicineId());
        List<MedicineBatches> batches = medicineBatchesService.list(queryWrapper);

        for (MedicineBatches batch : batches) {
            LambdaUpdateWrapper<Inventory> inventoryUpdateWrapper = new LambdaUpdateWrapper<>();
            inventoryUpdateWrapper.eq(Inventory::getBatchNo, batch.getBatchNo())
                    .set(Inventory::getMinStock, stockAlerts.getMinQuantity());
            inventoryService.update(inventoryUpdateWrapper);
        }
    }

    private void resetInventoryToDefault() {
        LambdaUpdateWrapper<Inventory> resetWrapper = new LambdaUpdateWrapper<>();
        resetWrapper.set(Inventory::getMinStock, Thresholds.MIN_INVENTORY);
        inventoryService.update(resetWrapper);
    }


    /**
     * 查询所有库存预警规则
     * @return
     */
    @PreAuthorize("hasAnyAuthority('StockAlerts:read')")
    @GetMapping("/page")
    public Result<Page<StockAlerts>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) Integer medicineId,
            @RequestParam(defaultValue = "", required = false) String alertLevel
    ) {
        log.info("查询所有库存预警规则: page={}, pageSize={}, medicineId={}, alertLevel={}", page, pageSize, medicineId, alertLevel);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        LambdaQueryWrapper<StockAlerts> lqw = new LambdaQueryWrapper<>();
        if (medicineId != null) {
            lqw.eq(StockAlerts::getMedicineId, medicineId);
        }
        if (StringUtils.isNotBlank(alertLevel)) {
            lqw.eq(StockAlerts::getAlertLevel, alertLevel);
        }
        Page<StockAlerts> pageInfo = new Page<>(page, pageSize);
        stockAlertsService.page(pageInfo, lqw);
        return Result.success(pageInfo);
    }
}
