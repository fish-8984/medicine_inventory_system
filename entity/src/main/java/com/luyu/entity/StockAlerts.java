package com.luyu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Map;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存预警规则配置表
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
@TableName("stock_alerts")
public class StockAlerts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警规则ID
     */
    @TableId(value = "alert_id", type = IdType.AUTO)
    private Long alertId;

    /**
     * 关联药品ID
     */
    @TableField("medicine_id")
    private Long medicineId;

    /**
     * 最低库存阈值
     */
    @TableField("min_quantity")
    private Integer minQuantity;

    /**
     * 预警级别
     */
    @TableField("alert_level")
    private String alertLevel;

    /**
     * 通知方式（JSON数组）
     */
    @TableField("notification_methods")
    private String notificationMethods;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;


}
