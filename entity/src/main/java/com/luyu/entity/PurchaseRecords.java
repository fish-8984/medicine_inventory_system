package com.luyu.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购明细记录表
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
@TableName("purchase_records")
public class PurchaseRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 关联订单ID
     */
    @TableField("po_id")
    private Long poId;

    /**
     * 药品ID
     */
    @TableField("medicine_id")
    private Long medicineId;

    /**
     * 入库批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 采购数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 采购单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;


}
