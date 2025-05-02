package com.luyu.dto;

import com.luyu.entity.PrescriptionMedicines;
import com.luyu.entity.Prescriptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionsAndPrescriptionMedicinesDTO {
    private List<PrescriptionMedicines> prescriptionMedicines;

    private Prescriptions prescriptions;
}
