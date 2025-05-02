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
 * 药品分类表（支持多级分类）
 * </p>
 *
 * @author 
 * @since 2025-02-27
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("medicine_categories")
public class MedicineCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 分类ID
    */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;
    /**
     * 分类名称（如"抗生素类"）
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 父分类ID（顶级分类的父分类ID为1）
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 分类描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序顺序（用于显示分类时的顺序）
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}

