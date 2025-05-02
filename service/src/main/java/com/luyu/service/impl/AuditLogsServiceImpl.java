package com.luyu.service.impl;

import com.luyu.entity.AuditLogs;
import com.luyu.mapper.AuditLogsMapper;
import com.luyu.service.IAuditLogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统操作审计日志 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Service
public class AuditLogsServiceImpl extends ServiceImpl<AuditLogsMapper, AuditLogs> implements IAuditLogsService {

}
