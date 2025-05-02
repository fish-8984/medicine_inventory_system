package com.luyu.service.AOP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.entity.AuditLogs;
import com.luyu.service.IAuditLogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncAuditLogAspectService {

    private final IAuditLogsService iAuditLogService;
    private final ObjectMapper objectMapper;
    @Async
    @Transactional
    public void saveAuditLogAsync(AuditLog auditLogAnnotation,
                                  Object targetEntity,
                                  Integer userId,
                                  String targetId) {
        AuditLogs build = AuditLogs.builder()
                .userId(userId)
                .action(auditLogAnnotation.action())
                .targetTable(auditLogAnnotation.targetTable())
                .targetId(targetId)
                .changeDetails(buildChangeDetails(targetEntity))
                .build();
        BaseContext.removeTargetId();
        BaseContext.removeTargetIdString();
        iAuditLogService.save(build);
    }
    private String buildChangeDetails(Object newEntity) {
        try {
            Map<String, Object> changes = new HashMap<>();
            if (newEntity == null) {
                changes.put("new", null); // 空值处理
            } else if (newEntity instanceof Boolean || newEntity instanceof Character ||
                    newEntity instanceof Number || newEntity instanceof String) {
                Map<String, Object> wrapper = new HashMap<>();
                wrapper.put("value", newEntity); // 基本类型包装
                changes.put("new", wrapper);
            } else if (newEntity instanceof Map) {
                changes.put("new", newEntity); // 复用Map
            } else {
                changes.put("new", objectMapper.convertValue(newEntity, Map.class)); // 正常转换
            }
            return objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            return "{}"; // 异常时返回空JSON
        }
    }
}
