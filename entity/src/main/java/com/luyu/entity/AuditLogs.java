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
 * 系统操作审计日志
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
@TableName("audit_logs")
public class AuditLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 操作用户
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 操作类型
     */
    @TableField("action")
    private String action;

    /**
     * 目标表名
     */
    @TableField("target_table")
    private String targetTable;

    /**
     * 目标记录ID
     */
    @TableField("target_id")
    private String targetId;

    /**
     * 变更详情（JSON格式）
     */
    @TableField("change_details")
    private String changeDetails;

    /**
     * 操作时间
     */
    @TableField("action_date")
    private LocalDateTime actionDate;
}
