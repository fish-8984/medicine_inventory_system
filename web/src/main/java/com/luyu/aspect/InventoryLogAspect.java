package com.luyu.aspect;

import com.luyu.context.InventoryLog;
import com.luyu.service.AOP.AsyncInventoryLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class InventoryLogAspect {
    private final AsyncInventoryLogService asyncInventoryLogService;

    private final ParameterNameDiscoverer paramDiscoverer = new DefaultParameterNameDiscoverer();



    @Pointcut("execution(* com.luyu.service.*.*(..)) && @annotation(com.luyu.context.InventoryLog)")
    public void inventoryLogPointcut() {}

    @Around("@annotation(inventoryLog)")
    public Object processInventoryLog(ProceedingJoinPoint pjp, InventoryLog inventoryLog) throws Throwable {
        log.info("开始记录库存流水 | 操作类型: {}", inventoryLog.type());
        Object result = pjp.proceed();

        String collectionParam = inventoryLog.collectionParam();
        if (!collectionParam.isEmpty()) {
            processCollectionLog(pjp, inventoryLog, collectionParam);
        } else {
            processSingleLog(pjp, inventoryLog);
        }
        return result;
    }

    private void processCollectionLog(ProceedingJoinPoint pjp, InventoryLog inventoryLog, String collectionParam) {
        Object[] args = pjp.getArgs();
        Object collection = getObject(pjp, collectionParam, args);

        if (!((Iterable<?>) collection).iterator().hasNext()) {
            log.info("集合参数为空，无需记录流水");
            return;
        }

        String elementVar = inventoryLog.elementVar();
        SecurityContext securityContext = SecurityContextHolder.getContext();

        for (Object element : (Iterable<?>) collection) {
            try {
                EvaluationContext context = createElementContext(pjp, elementVar, element);
                asyncInventoryLogService.asyncSaveInventoryLog(inventoryLog, context, args, securityContext);
            } catch (Exception e) {
                log.error("记录库存流水失败: {}", e.getMessage());
            }
        }
    }

    private Object getObject(ProceedingJoinPoint pjp, String collectionParam, Object[] args) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = paramDiscoverer.getParameterNames(signature.getMethod());

        int paramIndex = -1;
        for (int i = 0; i < paramNames.length; i++) {
            if (collectionParam.equals(paramNames[i])) {
                paramIndex = i;
                break;
            }
        }

        if (paramIndex == -1) {
            throw new IllegalArgumentException("未找到集合参数: " + collectionParam);
        }

        Object collection = args[paramIndex];
        if (!(collection instanceof Iterable)) {
            throw new IllegalArgumentException("参数非集合类型: " + collectionParam);
        }
        return collection;
    }

    private EvaluationContext createElementContext(ProceedingJoinPoint pjp, String elementVar, Object element) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = paramDiscoverer.getParameterNames(signature.getMethod());
        Object[] args = pjp.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        context.setVariable(elementVar, element);
        return context;
    }

    private void processSingleLog(ProceedingJoinPoint pjp, InventoryLog inventoryLog) {
        EvaluationContext context = createEvaluationContext(pjp);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        asyncInventoryLogService.asyncSaveInventoryLog(inventoryLog, context, pjp.getArgs(), securityContext);
    }

    /**
     * 创建SpEL表达式解析上下文
     */
    private EvaluationContext createEvaluationContext(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = paramDiscoverer.getParameterNames(signature.getMethod());
        Object[] args = pjp.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return context;
    }
}