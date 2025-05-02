package com.luyu.service.AOP;

import com.luyu.context.InventoryLog;
import com.luyu.entity.InventoryTransactions;
import com.luyu.service.IInventoryTransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.luyu.utils.SomeService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncInventoryLogService {
    private final IInventoryTransactionsService transactionService;
    private final SpelExpressionParser spelParser = new SpelExpressionParser();
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void asyncSaveInventoryLog(InventoryLog inventoryLog,
                                      EvaluationContext evacontext,
                                      Object[] methodArgs,
                                      SecurityContext context) {
        // 设置安全上下文到当前线程
        SecurityContextHolder.setContext(context);
        try {
            // 解析关键字段
            String batchNo = parseSpel(inventoryLog.batchNoArg(), evacontext, String.class);
            Integer quantity = parseSpel(inventoryLog.quantityArg(), evacontext, Integer.class);
            Long relatedId = parseSpel(inventoryLog.relatedIdArg(), evacontext, Long.class);
            String notes = parseSpel(inventoryLog.notes(), evacontext, String.class);

            // 构建并保存实体
            InventoryTransactions transaction = buildTransactionEntity(
                    inventoryLog, batchNo, quantity, relatedId, notes
            );

            transactionService.save(transaction);
        } catch (Exception e) {
            log.error("库存流水记录失败 | 操作类型: {} | 参数: {} | 错误: {}",
                    inventoryLog.type(),
                    methodArgs,
                    e.getMessage());
        }
    }

    /**
     * 构建流水记录实体
     */
    private InventoryTransactions buildTransactionEntity(InventoryLog inventoryLog,
                                                       String batchNo,
                                                       Integer quantity,
                                                       Long relatedId,
                                                       String notes) {
        return InventoryTransactions.builder()
                .batchNo(batchNo)
                .transactionType(inventoryLog.type().toString())
                .quantity(quantity)
                .relatedId(relatedId)
                .notes(notes)
                .operatorId(SomeService.getUserId())
                .transactionTime(LocalDateTime.now())
                .build();
    }

    /**
     * 安全解析SpEL表达式
     */
    private <T> T parseSpel(String expr, EvaluationContext context, Class<T> clazz) {
        try {
            return spelParser.parseExpression(expr).getValue(context, clazz);
        } catch (Exception e) {
            log.error("SpEL解析失败 | 表达式: {} | 错误: {}", expr, e.getMessage());
            return null;
        }
    }
}
