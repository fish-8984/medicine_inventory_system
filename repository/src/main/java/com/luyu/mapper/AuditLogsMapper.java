package com.luyu.mapper;

import com.luyu.entity.AuditLogs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统操作审计日志 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Mapper
public interface AuditLogsMapper extends BaseMapper<AuditLogs> {

}
