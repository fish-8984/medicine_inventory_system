package com.luyu.entity;

import java.math.BigDecimal;

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
 * 药品基础信息表
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
@TableName("medicines")
public class Medicines implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 药品唯一标识
     */
    @TableId(value = "medicine_id", type = IdType.AUTO)
    private Long medicineId;

    /**
     * 药品通用名（如"阿莫西林胶囊"）
     */
    @TableField("name")
    private String name;

    /**
     * 规格（如"0.25g*12粒/盒"）
     */
    @TableField("specification")
    private String specification;

    /**
     * 生产商/品牌（如"辉瑞制药"）
     */
    @TableField("brand")
    private String brand;

    /**
     * 药品分类ID（外键关联分类表）
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 计量单位（盒/支/瓶）
     */
    @TableField("unit")
    private String unit;

    /**
     * 单价（含税）
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 是否为仿制药
     */
    @TableField("is_generic")
    private Boolean isGeneric;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
