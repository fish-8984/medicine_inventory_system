package com.luyu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 药品批次管理表
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
@TableName("medicine_batches")
public class MedicineBatches implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 批次号（如"20230801A"）
     */
    @TableId(value = "batch_no", type = IdType.AUTO)
    private String batchNo;

    /**
     * 关联药品ID
     */
    @TableField("medicine_id")
    private Long medicineId;

    /**
     * 有效期至
     */
    @TableField("expiry_date")
    private LocalDate expiryDate;

    /**
     * 生产日期
     */
    @TableField("production_date")
    private LocalDate productionDate;

    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Integer supplierId;

    /**
     * 批次总数量
     */
    @TableField("initial_quantity")
    private Integer initialQuantity;

    /**
     * 库存位置（如"门诊药房1区"）
     */
    @TableField("storage_location")
    private String storageLocation;

    /**
     * 入库时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;


}
