package com.luyu.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购订单主表
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
@TableName("purchase_orders")
public class PurchaseOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购订单ID
     */
    @TableId(value = "po_id", type = IdType.AUTO)
    private Long poId;

    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Integer supplierId;

    /**
     * 下单日期
     */
    @TableField("order_date")
    private LocalDate orderDate;

    /**
     * 预计到货日期
     */
    @TableField("expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @TableField("status")
    private String status;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Integer createdBy;


}
