package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.dto.MedicineCategoriesDTO;
import com.luyu.entity.MedicineCategories;
import com.luyu.entity.Medicines;
import com.luyu.result.Result;
import com.luyu.service.IMedicineCategoriesService;
import com.luyu.service.IMedicinesService;
import com.luyu.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * 药品分类表（支持多级分类）
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/medicine-categories")
@RequiredArgsConstructor
public class MedicineCategoriesController {
    private final IMedicineCategoriesService medicineCategoriesService;
    private final IMedicinesService medicinesService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    /**
     * 查询所有药品分类信息
     * @return
     */

    /**
     * 保存药品分类信息
     * @param medicineCategories
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.MEDICINE_CATEGORIES)
    @PostMapping
    public Result<String> save(@RequestBody MedicineCategories medicineCategories)  {
        log.info("保存药品分类信息{}", medicineCategories);
        if (medicineCategories == null) {
            return Result.error("保存失败!");
        }
        medicineCategories.setCreatedAt(LocalDateTime.now());
        medicineCategories.setUpdatedAt(LocalDateTime.now());
        medicineCategories.setCategoryId(null);
        if (medicineCategories.getParentId() == null) {
            medicineCategories.setParentId(2);
        }
        if (medicineCategories.getParentId() != 0) {
            MedicineCategories parentCategory = medicineCategoriesService.getById(medicineCategories.getParentId());
            if (parentCategory == null) {
                return Result.error("父分类不存在!");
            }
        }
        if (!medicineCategoriesService.save(medicineCategories)) {
            return Result.error("保存失败!");
        }
        deleteCache();
        BaseContext.setTargetId(medicineCategories.getCategoryId());
        return Result.success("保存成功");
    }

    /**
     * 删除药品分类信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.MEDICINE_CATEGORIES)
    @DeleteMapping("/{medicineCategoryId}")
    public Result<String> delete(@PathVariable("medicineCategoryId") Long id )  {
        if (id == null) {
            return Result.error("删除失败！");
        }
        MedicineCategories medicineCategories = medicineCategoriesService.getById(id);
        log.info("删除药品分类信息{}", medicineCategories);
        if (medicineCategories.getCategoryId() == 2 || medicineCategories.getCategoryId() == 1) {
            return Result.error("基础分类无法删除");
        }
        LambdaQueryWrapper<MedicineCategories> lqw = new LambdaQueryWrapper<MedicineCategories>()
                .eq(MedicineCategories::getParentId, medicineCategories.getCategoryId());
        long count = medicineCategoriesService.count(lqw);
        if (count > 1) {
            return Result.error("该分类下存在子分类，无法删除!");
        }
        LambdaQueryWrapper<Medicines> lm = new LambdaQueryWrapper<Medicines>()
                .eq(Medicines::getCategoryId, medicineCategories.getCategoryId());
        long count1 = medicinesService.count(lm);
        if (count1 > 0) {
            return Result.error("该分类下存在药品，无法删除!");
        }
        medicineCategories.setIsDeleted(true);
        BaseContext.setTargetId(medicineCategories.getCategoryId());
        if (!medicineCategoriesService.updateById(medicineCategories)) {
            return Result.error("删除失败!");
        }
        deleteCache();
        return Result.success("删除成功");
    }

    /**
     * 更新药品分类信息
     * @param medicineCategories
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.MEDICINE_CATEGORIES)
    @PutMapping
    public Result<String> update(@RequestBody MedicineCategories medicineCategories)  {
        log.info("更新药品分类信息{}", medicineCategories);
        if (medicineCategories == null || medicineCategories.getCategoryId() == null) {
            return Result.error("更新失败!");
        }
        if (medicineCategories.getParentId() != null) {
            MedicineCategories parentCategory = medicineCategoriesService.getById(medicineCategories.getParentId());
            if (parentCategory == null) {
                return Result.error("父分类不存在");
            }
        }
        medicineCategories.setUpdatedAt(LocalDateTime.now());
            LambdaUpdateWrapper<MedicineCategories> luw = new LambdaUpdateWrapper<MedicineCategories>()
                    .eq(MedicineCategories::getCategoryId, medicineCategories.getCategoryId())
                    .set(medicineCategories.getCategoryName() != null, MedicineCategories::getCategoryName, medicineCategories.getCategoryName())
                    .set(MedicineCategories::getParentId, medicineCategories.getParentId())
                    .set(medicineCategories.getDescription() != null, MedicineCategories::getDescription, medicineCategories.getDescription())
                    .set(medicineCategories.getSortOrder() != null, MedicineCategories::getSortOrder, medicineCategories.getSortOrder())
                    .set(MedicineCategories::getUpdatedAt, LocalDateTime.now());
        BaseContext.setTargetId(medicineCategories.getCategoryId());
        if (!medicineCategoriesService.update(luw)) {
            return Result.error("更新失败!");
        }
        deleteCache();
        return Result.success("更新成功");
    }

    /**
     * 根据id查询药品分类信息
     * @param medicineCategoryId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:read')")
    @GetMapping("/{medicineCategoryId}")
    public Result<MedicineCategories> getById(@PathVariable("medicineCategoryId") Long medicineCategoryId) {
        log.info("根据id查询药品分类信息，id={}", medicineCategoryId);
        if (medicineCategoryId == null) {
            return Result.error("查询失败！");
        }
        MedicineCategories byId = medicineCategoriesService.getById(medicineCategoryId);
        return Result.success(byId);
    }

    /**
     * 查询所有药品分类名称和id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:read')")
    @GetMapping("/names/all")
    public Result<List<MedicineCategoriesDTO>> getName() {
        LambdaQueryWrapper<MedicineCategories> lqw = new LambdaQueryWrapper<>();
        lqw.select(MedicineCategories::getCategoryId,
                MedicineCategories::getCategoryName,
                MedicineCategories::getParentId,
                MedicineCategories::getSortOrder
        );
        lqw.eq(MedicineCategories::getIsDeleted, false);
        List<MedicineCategories> list = medicineCategoriesService.list(lqw);
        List<MedicineCategoriesDTO> result = new ArrayList<>();
        for (MedicineCategories medicineCategories : list) {
            MedicineCategoriesDTO medicineCategoriesDTO = new MedicineCategoriesDTO();
            BeanUtils.copyProperties(medicineCategories, medicineCategoriesDTO);
            result.add(medicineCategoriesDTO);
        }
        return Result.success(result);
    }



    /**
     * 分页查询所有药品分类信息
     * @param page
     * @param pageSize
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasAnyAuthority('MedicineCategories:read')")
    @GetMapping("/page")
    public Result<Page> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) throws JsonProcessingException {

        log.info("查询所有药品分类信息，分页参数：page={}, pageSize={}", page, pageSize);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        String resultPage = (String) redisTemplate.opsForValue().get(Table.MEDICINE_CATEGORIES + page + "_" + pageSize);
        if (resultPage != null) {
            Page resultPage1 = objectMapper.readValue(resultPage, Page.class);
            return Result.success(resultPage1);
        }
        // 数据库分页查询
        Page<MedicineCategories> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<MedicineCategories> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(MedicineCategories::getCategoryId);
        lqw.eq(MedicineCategories::getIsDeleted, false);
        medicineCategoriesService.page(pageInfo, lqw);

        String string = objectMapper.writeValueAsString(pageInfo);
        redisTemplate.opsForValue().set(Table.MEDICINE_CATEGORIES +":"+ page + "_" + pageSize, string, 10 + RandomUtil.random(1, 10), TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    private void deleteCache() {
        Set<String> keys = redisTemplate.keys(Table.MEDICINE_CATEGORIES + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
