package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.entity.AuditLogs;
import com.luyu.result.Result;
import com.luyu.service.IAuditLogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


/**
 * 系统操作审计日志
 * @author 
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/audit-logs")
@Slf4j
@RequiredArgsConstructor
public class AuditLogsController {

    private final IAuditLogsService auditLogsService;

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('AuditLogs:read')")
    public Result<Page<AuditLogs>> page(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(defaultValue = "", required = false) Long userId,
                                        @RequestParam(defaultValue = "", required = false) String action,
                                        @RequestParam(defaultValue = "", required = false) LocalDate actionDate
                                        ) {
        log.info("查询所有操作审计日志");
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<AuditLogs> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<AuditLogs> queryWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq(AuditLogs::getUserId, userId);
        }
        if (action != null && !action.isEmpty()) {
            queryWrapper.like(AuditLogs::getAction, action);
        }
        if (actionDate != null) {
            queryWrapper.like(AuditLogs::getActionDate, actionDate);
        }
        queryWrapper.orderByDesc(AuditLogs::getActionDate);
        auditLogsService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }
}
