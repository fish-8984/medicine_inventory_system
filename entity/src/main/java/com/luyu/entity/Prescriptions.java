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
 * 处方单主表
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
@TableName("prescriptions")
public class Prescriptions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处方单ID
     */
    @TableId(value = "prescription_id", type = IdType.AUTO)
    private Long prescriptionId;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 开方医生ID
     */
    @TableField("doctor_id")
    private Integer doctorId;

    /**
     * 开方时间
     */
    @TableField("prescription_date")
    private LocalDateTime prescriptionDate;

    /**
     * 执行状态
     */
    @TableField("status")
    private String status;

    /**
     * 发药人（关联users.user_id）
     */
    @TableField("dispensed_by")
    private Integer dispensedBy;

    /**
     * 发药时间
     */
    @TableField("dispensed_at")
    private LocalDateTime dispensedAt;


}
