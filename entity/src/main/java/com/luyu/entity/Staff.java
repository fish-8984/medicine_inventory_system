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
 * 医护人员信息表
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
@TableName("staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    @TableId(value = "staff_id", type = IdType.AUTO)
    private Integer staffId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 职称（如主任医师）
     */
    @TableField("title")
    private String title;

    /**
     * 所属科室
     */
    @TableField("department")
    private String department;

    /**
     * 是否在职
     */
    @TableField("is_active")
    private Integer isActive;

    /**
     * 入职时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;


}
