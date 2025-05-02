package com.luyu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存流水记录表（支撑AI分析）
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
@TableName("inventory_transactions")
public class InventoryTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID
     */
    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Long transactionId;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 操作类型
     */
    @TableField("transaction_type")
    private String transactionType;

    /**
     * 变动数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 关联业务ID（如采购单/处方单）
     */
    @TableField("related_id")
    private Long relatedId;

    /**
     * 操作时间
     */
    @TableField("transaction_time")
    private LocalDateTime transactionTime;

    /**
     * 操作人
     */
    @TableField("operator_id")
    private Integer operatorId;

    /**
     * 备注说明
     */
    @TableField("notes")
    private String notes;


}
