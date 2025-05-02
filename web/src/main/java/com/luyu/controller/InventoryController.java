package com.luyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.entity.Inventory;
import com.luyu.result.Result;
import com.luyu.service.IInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 实时库存状态表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryService inventoryService;

    /**
     * 查询所有库存记录
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('Inventory:read')")
    public Result<Page<Inventory>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String batchNo,
            @RequestParam(defaultValue = "", required = false) Integer id
    ){
        log.info("查询所有库存记录:page={},pageSize={},batchNo={},id={}", page, pageSize, batchNo, id);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);

        Page<Inventory> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(batchNo)) {
            queryWrapper.like(Inventory::getBatchNo, batchNo);
        }
        if (id != null) {
            queryWrapper.orderByDesc(Inventory::getLastRestocked);
        }
        queryWrapper.orderByDesc(Inventory::getLastUsed);
        inventoryService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }


    /**
     * 查询库存总数
     * @return
     */
    @GetMapping("/count")
    public Result<Integer> count() {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Inventory::getCurrentQuantity);

        Integer total = inventoryService.list(queryWrapper)
                .stream()
                .mapToInt(Inventory::getCurrentQuantity)
                .sum();

        return Result.success(total);
    }
}
