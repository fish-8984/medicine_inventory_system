package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.dto.SuppliersDTO;
import com.luyu.entity.MedicineBatches;
import com.luyu.entity.PurchaseOrders;
import com.luyu.entity.Suppliers;
import com.luyu.result.Result;
import com.luyu.service.IMedicineBatchesService;
import com.luyu.service.IPurchaseOrdersService;
import com.luyu.service.ISuppliersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商信息表
 * @author 
 * @since 2025-02-25
 */
@RestController
@Slf4j
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SuppliersController {
    private final ISuppliersService suppliersService;
    private final IMedicineBatchesService medicineBatchesService;
    private final IPurchaseOrdersService purchaseOrdersService;

    /**
     * 保存供应商信息
     * @param suppliers
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.SUPPLIERS)
    @PostMapping
    public Result<String> save(@RequestBody Suppliers suppliers){
        log.info("保存供应商信息:{}", suppliers);
        if (suppliers == null) {
            return Result.error("保存失败!");
        }
        suppliers.setSupplierId(null);
        if (!suppliersService.save(suppliers)) {
            return Result.error("保存失败!");
        }
        BaseContext.setTargetId(suppliers.getSupplierId());
        return Result.success("保存成功");
    }

    /**
     * 删除供应商信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:delete')")
    @Transactional
    @AuditLog(action = OperationType.DELETE, targetTable = Table.SUPPLIERS)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id ){
        if (id == null) {
            return Result.error("删除失败!");
        }
        Suppliers suppliers = suppliersService.getById(id);
        log.info("删除供应商信息:{}", suppliers);
        LambdaQueryWrapper<MedicineBatches> queryWrapper = new LambdaQueryWrapper<MedicineBatches>()
                .eq(MedicineBatches::getSupplierId, suppliers.getSupplierId());
        MedicineBatches byId = medicineBatchesService.getOne(queryWrapper);
        if (byId != null) {
            return Result.error("该供应商下有关联药品，不能删除!");
        }
        LambdaQueryWrapper<PurchaseOrders> queryWrapper1 = new LambdaQueryWrapper<PurchaseOrders>()
               .eq(PurchaseOrders::getSupplierId, suppliers.getSupplierId());
        PurchaseOrders byId1 = purchaseOrdersService.getOne(queryWrapper1);
        if (byId1 != null) {
            return Result.error("该供应商下有关联采购单，不能删除!");
        }
        BaseContext.setTargetId(suppliers.getSupplierId());
        if (!suppliersService.removeById(suppliers.getSupplierId())) {
            return Result.error("删除失败!");
        }
        return Result.success("删除成功");
    }

    /**
     * 更新供应商信息
     * @param suppliers
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:update')")
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.SUPPLIERS)
    @PutMapping
    public Result<String> update(@RequestBody Suppliers suppliers){
        log.info("更新供应商信息:{}", suppliers);
        if (suppliers == null || suppliers.getSupplierId() == null) {
            return Result.error("更新失败");
        }
        LambdaUpdateWrapper<Suppliers> updateWrapper = new LambdaUpdateWrapper<Suppliers>()
                .eq(Suppliers::getSupplierId, suppliers.getSupplierId())
                .set(suppliers.getName() != null, Suppliers::getName, suppliers.getName())
                .set(suppliers.getContactPerson() != null, Suppliers::getContactPerson, suppliers.getContactPerson())
                .set(suppliers.getPhone() != null, Suppliers::getPhone, suppliers.getPhone())
                .set(suppliers.getAddress() != null, Suppliers::getAddress, suppliers.getAddress())
                .set(suppliers.getContractEndDate() != null, Suppliers::getContractEndDate, suppliers.getContractEndDate());
        suppliersService.update(updateWrapper);
        BaseContext.setTargetId(suppliers.getSupplierId());
        if (!suppliersService.updateById(suppliers)) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    /**
     * 根据id查询供应商信息
     * @param supplierId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:read')")
    @GetMapping("/{supplierId}")
    public Result<Suppliers> getById(@PathVariable("supplierId") Long supplierId){
        log.info("根据id查询供应商信息，id={}", supplierId);
        if (supplierId == null) {
            return Result.error("查询失败");
        }
        Suppliers byId = suppliersService.getById(supplierId);
        return Result.success(byId);
    }

    /**
     * 根据ids查询供应商信息
     * @param supplierId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:read')")
    @GetMapping("/supplierIds")
    public Result<List<SuppliersDTO>> getByIds(@RequestParam String supplierId){
        log.info("根据ids查询供应商信息，ids={}", supplierId);
        List<Integer> supplierIds = new ArrayList<>();
        if (StringUtils.isNotBlank(supplierId)) {
            String[] split = supplierId.split(",");
            for (String s : split) {
                if (StringUtils.isNumeric(s)) {
                    supplierIds.add(Integer.parseInt(s));
                }
            }
        }
        LambdaQueryWrapper<Suppliers> queryWrapper = new LambdaQueryWrapper<Suppliers>()
                .select(Suppliers::getSupplierId, Suppliers::getName)
                .in(Suppliers::getSupplierId, supplierIds);
        List<Suppliers> suppliers = suppliersService.list(queryWrapper);
        List<SuppliersDTO> suppliersDTOS = new ArrayList<>();
        for (Suppliers supplier : suppliers) {
            SuppliersDTO suppliersDTO = new SuppliersDTO();
            suppliersDTO.setSupplierId(supplier.getSupplierId());
            suppliersDTO.setName(supplier.getName());
            suppliersDTOS.add(suppliersDTO);
        }
        return Result.success(suppliersDTOS);
    }

    /**
     * 查询供应商信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Suppliers:read')")
    @GetMapping("/page")
    public Result<Page<Suppliers>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "", required = false) String phone,
            @RequestParam(defaultValue = "", required = false) LocalDate contractEndDate
    ){
        log.info("查询供应商信息:page={},pageSize={},name={},phone{},contractEndDate={}", page, pageSize, name, phone, contractEndDate);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<Suppliers> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Suppliers> queryWrapper = new LambdaQueryWrapper<Suppliers>()
                .like(StringUtils.isNotBlank(name), Suppliers::getName, name)
                .like(StringUtils.isNotBlank(phone), Suppliers::getPhone, phone)
                .eq(contractEndDate != null, Suppliers::getContractEndDate, contractEndDate);
        queryWrapper.orderByDesc(Suppliers::getContractEndDate);
        suppliersService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }
}
