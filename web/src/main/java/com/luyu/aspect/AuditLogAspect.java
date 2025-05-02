package com.luyu.aspect;

import com.luyu.constant.Table;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.service.AOP.AsyncAuditLogAspectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.luyu.utils.SomeService;


@Aspect
@Component
@Slf4j
@Order(2)
@RequiredArgsConstructor
public class AuditLogAspect {
    private final AsyncAuditLogAspectService asyncAuditLogAspectService;
    /**
     * 切入点
     */
    @Pointcut("execution(* com.luyu.service.*.*(..)) && @annotation(com.luyu.context.AuditLog)")
    public void autoFillPointCut(){}
    // 拦截带有 @AuditLog 注解的方法
    @Around("@annotation(auditLogAnnotation)")
    public Object logAuditAction(ProceedingJoinPoint joinPoint, AuditLog auditLogAnnotation) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return joinPoint.proceed();
        }
        Object targetEntity = args[0];
        log.info("审计日志 | 目标实体: {}", targetEntity);
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            // 记录日志
            int userId = SomeService.getUserId();
            String targetIdString = null;
            if (Table.MEDICINE_BATCHES.equals(auditLogAnnotation.targetTable())) {
                String targetId = BaseContext.getTargetIdString();
                if (targetId != null) {
                    targetIdString = targetId;
                }
            } else {
                Long targetId = BaseContext.getTargetId();
                if (targetId != null) {
                    targetIdString = targetId.toString();
                }
            }
            // 确保无论如何都会记录日志
            asyncAuditLogAspectService.saveAuditLogAsync(auditLogAnnotation,
                    targetEntity,
                    userId,
                    targetIdString
            );
            return result;
        } catch (Exception e) {
            log.error("审计日志 | 方法执行异常", e);
            throw e;
        }
    }
}
