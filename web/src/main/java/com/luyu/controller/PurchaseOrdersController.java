package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.OrdersStatus;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.PurchaseOrders;
import com.luyu.entity.Suppliers;
import com.luyu.result.Result;
import com.luyu.service.IPurchaseOrdersService;
import com.luyu.service.ISuppliersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.luyu.utils.SomeService;

import java.time.LocalDate;

/**
 * 采购订单主表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrdersController {
    private final IPurchaseOrdersService purchaseOrdersService;
    private final ISuppliersService suppliersService;

    /**
     * 保存采购订单主表
     * @param purchaseOrders
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.PURCHASE_ORDERS)
    @PostMapping
    public Result<String> save(@RequestBody PurchaseOrders purchaseOrders) {
        if (purchaseOrders == null) {
            return Result.error("保存失败!");
        }
        log.info("保存采购订单:{}", purchaseOrders);
        Suppliers byId = suppliersService.getById(purchaseOrders.getSupplierId());
        if(byId == null) {
            return Result.error("供应商不存在!");
        }
        purchaseOrders.setStatus(OrdersStatus.PENDING);
        purchaseOrders.setPoId(null);
        purchaseOrders.setCreatedBy(SomeService.getUserId());
        if (!purchaseOrdersService.save(purchaseOrders)) {
            return Result.error("保存失败!");
        }
        BaseContext.setTargetId(purchaseOrders.getPoId());
        return Result.success("保存成功");
    }

    /**
     * 更新采购订单状态
     * @param purchaseOrders
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:update')")
    @PutMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody PurchaseOrders purchaseOrders) {
        log.info("修改采购订单:{}", purchaseOrders);
        PurchaseOrders byId = purchaseOrdersService.getById(purchaseOrders.getPoId());
        String status = byId.getStatus();
        if(status.equals(OrdersStatus.CANCELLED)) {
            return Result.error("订单状态不允许修改!");
        }
        LambdaUpdateWrapper<PurchaseOrders> luw = new LambdaUpdateWrapper<PurchaseOrders>()
                .eq(PurchaseOrders::getPoId, purchaseOrders.getPoId())
                .set(PurchaseOrders::getStatus, purchaseOrders.getStatus());
        BaseContext.setTargetId(purchaseOrders.getPoId());
        purchaseOrdersService.update(luw);
        return Result.success("更新成功");
    }


    /**
     * 取消采购订单
     * @param purchaseOrders
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:update')")
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PURCHASE_ORDERS)
    @Transactional
    @PutMapping("/CancelOrder")
    public Result<String> CancelOrder(@RequestBody PurchaseOrders purchaseOrders) {
        log.info("取消采购订单:{}", purchaseOrders);
        PurchaseOrders byId = purchaseOrdersService.getById(purchaseOrders.getPoId());
        String status = byId.getStatus();
        if(!status.equals(OrdersStatus.PENDING)) {
            return Result.error("订单状态不允许取消!");
        }
        LambdaUpdateWrapper<PurchaseOrders> luw = new LambdaUpdateWrapper<PurchaseOrders>()
               .eq(PurchaseOrders::getPoId, purchaseOrders.getPoId())
               .set(PurchaseOrders::getStatus, OrdersStatus.CANCELLED);
        BaseContext.setTargetId(purchaseOrders.getPoId());
        purchaseOrdersService.update(luw);
        return Result.success("取消成功");
    }

    /**
     * 更新采购订单主表
     * @param purchaseOrders
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PURCHASE_ORDERS)
    @PutMapping
    public Result<String> update(@RequestBody PurchaseOrders purchaseOrders) {
        if (purchaseOrders == null) {
            return Result.error("更新失败!");
        }
        log.info("更新采购订单:{}", purchaseOrders);
        PurchaseOrders byId = purchaseOrdersService.getById(purchaseOrders.getPoId());
        String status = byId.getStatus();
        if(!status.equals(OrdersStatus.PENDING)) {
            return Result.error("订单状态不允许修改!");
        }
        LambdaUpdateWrapper<PurchaseOrders> luw = new LambdaUpdateWrapper<PurchaseOrders>()
                .eq(PurchaseOrders::getPoId, purchaseOrders.getPoId())
                .set(purchaseOrders.getSupplierId() != null, PurchaseOrders::getSupplierId, purchaseOrders.getSupplierId())
                .set(purchaseOrders.getOrderDate() != null, PurchaseOrders::getOrderDate, purchaseOrders.getOrderDate())
                .set(purchaseOrders.getExpectedDeliveryDate() != null, PurchaseOrders::getExpectedDeliveryDate, purchaseOrders.getExpectedDeliveryDate())
                .set(purchaseOrders.getTotalAmount() != null, PurchaseOrders::getTotalAmount, purchaseOrders.getTotalAmount())
                .set(purchaseOrders.getStatus() != null, PurchaseOrders::getStatus, purchaseOrders.getStatus())
                .set(PurchaseOrders::getCreatedBy, SomeService.getUserId());
        BaseContext.setTargetId(purchaseOrders.getPoId());
        if (!purchaseOrdersService.update(luw)) {
            return Result.error("更新失败!");
        }
        return Result.success("更新成功");
    }

    /**
     * 根据主键查询采购订单主表
     * @param poId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:read')")
    @GetMapping("/{poId}")
    public Result<PurchaseOrders> getById(@PathVariable("poId") Long poId) {
        log.info("根据主键查询采购订单主表:{}", poId);
        PurchaseOrders purchaseOrders = purchaseOrdersService.getById(poId);
        if (purchaseOrders == null) {
            return Result.error("采购订单不存在!");
        }
        return Result.success(purchaseOrders);
    }

    /**
     * 查询所有采购订单主表
     * @return
     */
    @PreAuthorize("hasAnyAuthority('PurchaseOrders:read')")
    @GetMapping("/page")
    public Result<Page<PurchaseOrders>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String supplierId,
            @RequestParam(defaultValue = "", required = false) LocalDate orderDate,
            @RequestParam(defaultValue = "", required = false) LocalDate expectedDeliveryDate,
            @RequestParam(defaultValue = "", required = false) String status,
            @RequestParam(defaultValue = "", required = false) Integer createdBy
            ) {
        log.info("查询所有采购订单主表: page={}, pageSize={}, supplierId={}, orderDate={}, expectedDeliveryDate={}, status={}, createdBy={}",
                page, pageSize, supplierId, orderDate, expectedDeliveryDate, status, createdBy);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<PurchaseOrders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<PurchaseOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(status), PurchaseOrders::getStatus, status)
                .eq(StringUtils.isNotBlank(supplierId), PurchaseOrders::getSupplierId, supplierId)
                .eq(orderDate != null, PurchaseOrders::getOrderDate, orderDate)
                .eq(expectedDeliveryDate != null, PurchaseOrders::getExpectedDeliveryDate, expectedDeliveryDate)
                .eq(createdBy != null, PurchaseOrders::getCreatedBy, createdBy);
        purchaseOrdersService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }
}
