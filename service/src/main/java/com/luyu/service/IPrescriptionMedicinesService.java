package com.luyu.service;

import com.luyu.entity.PrescriptionMedicines;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luyu.entity.Prescriptions;
import com.luyu.result.Result;

import java.util.List;

/**
 * <p>
 * 处方药品明细表 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
public interface IPrescriptionMedicinesService extends IService<PrescriptionMedicines> {
    Result<String> updateInventory(Prescriptions prescriptions, List<PrescriptionMedicines> prescriptionMedicines);
}
