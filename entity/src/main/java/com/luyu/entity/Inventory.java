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
 * 实时库存状态表
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
@TableName("inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存记录ID
     */
    @TableId(value = "inventory_id", type = IdType.AUTO)
    private Long inventoryId;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 当前库存数量
     */
    @TableField("current_quantity")
    private Integer currentQuantity;

    /**
     * 安全库存阈值
     */
    @TableField("min_stock")
    private Integer minStock;

    /**
     * 最近补货时间
     */
    @TableField("last_restocked")
    private LocalDateTime lastRestocked;

    /**
     * 最近领用时间
     */
    @TableField("last_used")
    private LocalDateTime lastUsed;


}
