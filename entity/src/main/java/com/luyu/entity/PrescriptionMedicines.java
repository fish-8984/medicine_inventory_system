package com.luyu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 处方药品明细表
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("prescription_medicines")
public class PrescriptionMedicines implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 处方单ID
     */
    @TableField("prescription_id")
    private Long prescriptionId;

    /**
     * 药品ID
     */
    @TableField("medicine_id")
    private Long medicineId;

    /**
     * 发放批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 用法用量（如"每次1片，每日3次"）
     */
    @TableField("dosage")
    private String dosage;

    /**
     * 发放数量
     */
    @TableField("quantity_dispensed")
    private Integer quantityDispensed;


}
