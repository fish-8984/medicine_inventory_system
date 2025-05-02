package com.luyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luyu.constant.OperationType;
import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.PrescriptionMedicines;
import com.luyu.service.IPrescriptionMedicinesService;
import com.luyu.service.IPrescriptionMedicinesUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IPrescriptionMedicinesUpdaterImpl implements IPrescriptionMedicinesUpdater {
    private final IPrescriptionMedicinesService prescriptionMedicinesService;
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.PRESCRIPTION_MEDICINES)
    public boolean updatePrescriptionMedicines(PrescriptionMedicines prescriptionMedicines){
        log.info("更新处方药品明细表:{}", prescriptionMedicines);
        LambdaUpdateWrapper<PrescriptionMedicines> updateWrapper = new LambdaUpdateWrapper<PrescriptionMedicines>()
                .eq(PrescriptionMedicines::getRecordId, prescriptionMedicines.getRecordId())
                .set(prescriptionMedicines.getPrescriptionId() != null, PrescriptionMedicines::getPrescriptionId, prescriptionMedicines.getPrescriptionId())
                .set(prescriptionMedicines.getMedicineId() != null, PrescriptionMedicines::getMedicineId, prescriptionMedicines.getMedicineId())
                .set(prescriptionMedicines.getBatchNo() != null, PrescriptionMedicines::getBatchNo, prescriptionMedicines.getBatchNo())
                .set(prescriptionMedicines.getDosage() != null, PrescriptionMedicines::getDosage, prescriptionMedicines.getDosage())
                .set(prescriptionMedicines.getQuantityDispensed() != null, PrescriptionMedicines::getQuantityDispensed, prescriptionMedicines.getQuantityDispensed());
        BaseContext.setTargetId(prescriptionMedicines.getRecordId());
        return prescriptionMedicinesService.update(updateWrapper);
    }
}
