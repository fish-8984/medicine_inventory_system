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
import com.luyu.dto.MedicinesDTO;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.MedicineCategories;
import com.luyu.entity.Medicines;
import com.luyu.result.Result;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IMedicineCategoriesService;
import com.luyu.service.IMedicinesService;
import com.luyu.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 药品基础信息表
 * @author
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicinesController {
    private final IMedicinesService medicinesService;
    private final IMedicineBatchesService medicineBatchesService;
    private final IMedicineCategoriesService medicineCategoriesService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    /**
     * 保存药品信息
     * @param medicines
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.MEDICINES)
    @PostMapping
    public Result<String> save(@RequestBody Medicines medicines) {
        log.info("保存药品信息{}", medicines);
        if (medicines == null) {
            return Result.error("保存失败!");
        }
        if (medicines.getCategoryId() == null) {
            medicines.setCategoryId(2);
        }
        LambdaQueryWrapper<MedicineCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicineCategories::getCategoryId, medicines.getCategoryId());
        long count = medicineCategoriesService.count(queryWrapper);
        if (count == 0) {
            return Result.error("分类不存在!");
        }
        medicines.setCreatedAt(LocalDateTime.now());
        medicines.setUpdatedAt(LocalDateTime.now());
        medicines.setMedicineId(null);
        if (!medicinesService.save(medicines)) {
            return Result.error("保存失败!");
        }
        deleteCache();
        BaseContext.setTargetId(medicines.getMedicineId());
        return Result.success("保存成功");
    }

    /**
     * 删除药品信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.MEDICINES)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id ) {
        if (id == null) {
            return Result.error("删除失败");
        }
        Medicines medicines = medicinesService.getById(id);
        log.info("删除药品信息{}", medicines);
        LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicineBatches::getMedicineId, medicines.getMedicineId());
        long count = medicineBatchesService.count(queryWrapper);
        if (count > 0) {
            return Result.error("该药品已存在批次信息，无法删除");
        }
        BaseContext.setTargetId(medicines.getMedicineId());
        if (!medicinesService.removeById(medicines.getMedicineId())) {
            return Result.error("删除失败");
        }
        deleteCache();
        return Result.success("删除成功");
    }

    /**
     * 更新药品信息
     * @param medicines
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.MEDICINES)
    @PutMapping
    public Result<String> update(@RequestBody Medicines medicines) {
        log.info("更新药品信息{}", medicines);
        if (medicines == null) {
            return Result.error("更新失败");
        }
        medicines.setUpdatedAt(LocalDateTime.now());
        LambdaUpdateWrapper<Medicines> luw = new LambdaUpdateWrapper<Medicines>()
                .eq(Medicines::getMedicineId, medicines.getMedicineId())
                .set(Medicines::getUpdatedAt, LocalDateTime.now())
                .set(medicines.getName() != null, Medicines::getName, medicines.getName())
                .set(medicines.getSpecification() != null, Medicines::getSpecification, medicines.getSpecification())
                .set(medicines.getBrand() != null, Medicines::getBrand, medicines.getBrand())
                .set(medicines.getCategoryId() != null, Medicines::getCategoryId, medicines.getCategoryId())
                .set(medicines.getUnit() != null, Medicines::getUnit, medicines.getUnit())
                .set(medicines.getPrice() != null, Medicines::getPrice, medicines.getPrice())
                .set(medicines.getIsGeneric() != null, Medicines::getIsGeneric, medicines.getIsGeneric());
        BaseContext.setTargetId(medicines.getMedicineId());
        if (!medicinesService.update(luw)) {
            return Result.error("更新失败");
        }
        deleteCache();
        return Result.success("更新成功");
    }
    /**
     * 根据name查询药品信息
     * @param medicineName
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:read')")
    @GetMapping("/name/{medicineName}")
    public Result<List<Integer>> getByName(@PathVariable("medicineName") String medicineName) {
        log.info("根据id查询药品信息，name={}", medicineName);
        if (medicineName == null) {
            return Result.error("查询失败");
        }
        LambdaQueryWrapper<Medicines> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Medicines::getName, medicineName);
        queryWrapper.select(Medicines::getMedicineId);
        List<Medicines> one = medicinesService.list(queryWrapper);
        List<Integer> medicineIds = new ArrayList<>();
        for (Medicines medicines : one) {
            medicineIds.add(Math.toIntExact(medicines.getMedicineId()));
        }
        return Result.success(medicineIds);
    }

    /**
     * 根据ids查询药品信息
     * @param medicineId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:read')")
    @GetMapping("/ids")
    public Result<List<MedicinesDTO>> getByIds(@RequestParam("medicineIds") String medicineId) {
        log.info("根据id查询药品信息，ids={}", medicineId);
        if (medicineId == null) {
            return Result.error("查询失败");
        }
        List<Integer> medicineIds = new ArrayList<>();
        if (StringUtils.isNotBlank(medicineId)) {
            String[] split = medicineId.split(",");
            for (String s : split) {
                if (StringUtils.isNumeric(s)) {
                    medicineIds.add(Integer.parseInt(s));
                }
            }
        }
        LambdaQueryWrapper<Medicines> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Medicines::getMedicineId, medicineIds);
        queryWrapper.select(Medicines::getMedicineId, Medicines::getName);
        List<Medicines> medicines = medicinesService.list(queryWrapper);
        List<MedicinesDTO> medicinesDTOS = new ArrayList<>();
        for (Medicines medicine : medicines) {
            MedicinesDTO medicinesDTO = new MedicinesDTO();
            medicinesDTO.setMedicineId(medicine.getMedicineId());
            medicinesDTO.setName(medicine.getName());
            medicinesDTOS.add(medicinesDTO);
        }
        return Result.success(medicinesDTOS);
    }
    /**
     * 查询所有药品信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Medicines:read')")
    @GetMapping("/page")
    public Result<Page> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "", required = false) String brand,
            @RequestParam(defaultValue = "", required = false) String categoryId,
            @RequestParam(defaultValue = "", required = false) String isGeneric) throws JsonProcessingException {

        log.info("查询药品信息: page={}, pageSize={}, name={}, brand={}, categoryId={}, isGeneric={}",
                page, pageSize, name, brand, categoryId, isGeneric);
        // 参数校验
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);

        String key = Table.MEDICINES + ":" + page + "_" + pageSize + "_" + name + "_" + brand + "_" + categoryId + "_" + isGeneric;
        String medicinesPage = (String) redisTemplate.opsForValue().get(key);
        if (medicinesPage != null) {
            return Result.success(objectMapper.readValue(medicinesPage, Page.class));
        }
        List<Integer> categoryIds = new ArrayList<>();
        if (StringUtils.isNotBlank(categoryId)) {
            String[] split = categoryId.split(",");
            for (String s : split) {
                if (StringUtils.isNumeric(s)) {
                    categoryIds.add(Integer.parseInt(s));
                }
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<Medicines> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(name), Medicines::getName, name)
                .like(StringUtils.isNotBlank(brand), Medicines::getBrand, brand)
                .in(!categoryIds.isEmpty(), Medicines::getCategoryId, categoryIds)
                .like(StringUtils.isNotBlank(isGeneric), Medicines::getIsGeneric, Objects.equals(isGeneric, "true") ? "1": "0")
                .orderByDesc(Medicines::getUpdatedAt);
        // 数据库分页查询
        Page<Medicines> pageInfo = new Page<>(page, pageSize);
        medicinesService.page(pageInfo, lqw);
        String s = objectMapper.writeValueAsString(pageInfo);
        redisTemplate.opsForValue().set(key, s, 10 + RandomUtil.random(1, 10), TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    private void deleteCache() {
        Set<String> keys = redisTemplate.keys(Table.MEDICINES + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
