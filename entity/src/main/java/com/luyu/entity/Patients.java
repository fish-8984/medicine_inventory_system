package com.luyu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 患者信息表
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
@TableName("patients")
public class Patients implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 患者唯一标识
     */
    @TableId(value = "patient_id", type = IdType.AUTO)
    private Integer patientId;

    /**
     * 患者姓名
     */
    @TableField("name")
    private String name;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 出生日期
     */
    @TableField("birthdate")
    private LocalDate birthdate;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 建档时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;


}
