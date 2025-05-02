package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.entity.InventoryTransactions;
import com.luyu.result.Result;
import com.luyu.service.IInventoryTransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 库存流水记录表（支撑AI分析）
 * @author 
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/inventory-transactions")
@Slf4j
@RequiredArgsConstructor
public class InventoryTransactionsController {
    private final IInventoryTransactionsService inventoryTransactionsService;

    /**
     * 查询所有库存流水记录
     * @param page
     * @param pageSize
     * @param transactionType
     * @return
     */
    @PreAuthorize("hasAnyAuthority('InventoryTransactions:read')")
    @Transactional(readOnly = true)
    @GetMapping("/page")
    public Result<Page<InventoryTransactions>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String transactionType,
            @RequestParam(defaultValue = "", required = false) LocalDate startDate,
            @RequestParam(defaultValue = "", required = false) LocalDate endDate
    ) {
        log.info("查询所有库存流水记录 | page:{}, pageSize={}, transactionType={}, startDate={}, endDate={}", page, pageSize, transactionType, startDate, endDate);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
                Page<InventoryTransactions> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<InventoryTransactions> queryWrapper = new LambdaQueryWrapper<InventoryTransactions>()
                .eq(StringUtils.isNotBlank(transactionType), InventoryTransactions::getTransactionType, transactionType)
                .ge(startDate != null, InventoryTransactions::getTransactionTime, startDate)
                .le(endDate != null, InventoryTransactions::getTransactionTime, endDate);
        queryWrapper.orderByDesc(InventoryTransactions::getTransactionTime);
        inventoryTransactionsService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('InventoryTransactions:read')")
    @Transactional(readOnly = true)
    public Result<Map<String, Integer>> getInventorySummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {
        log.info("库存统计 | startTime={}, endTime={}", startTime, endTime);

        // 构建查询条件（直接使用时间参数，无需要转换）
        QueryWrapper<InventoryTransactions> wrapper = new QueryWrapper<>();
        wrapper.select("transaction_type", "SUM(quantity) as total")
                .groupBy("transaction_type");

        if (startTime != null) {
            wrapper.ge("transaction_time", startTime);
        }
        if (endTime != null) {
            wrapper.le("transaction_time", endTime);
        }

        // 保持后续处理逻辑不变
        List<Map<String, Object>> result = inventoryTransactionsService.listMaps(wrapper);

        Map<String, Integer> summary = result.stream()
                .collect(Collectors.toMap(
                        map -> {
                            String type = ((String) map.get("transaction_type")).toLowerCase();
                            if ("in".equals(type)) {
                                return "stockIn";
                            } else if ("out".equals(type)) {
                                return "stockOut";
                            } else {
                                return type;
                            }
                        },
                        map -> ((Number) map.get("total")).intValue()
                ));

        // 补充默认值
        summary.putIfAbsent("stockIn", 0);
        summary.putIfAbsent("stockOut", 0);

        return Result.success(summary);
    }
}
