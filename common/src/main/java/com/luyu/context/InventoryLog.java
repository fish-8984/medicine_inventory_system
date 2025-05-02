package com.luyu.context;

import com.luyu.constant.TransactionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InventoryLog {
    TransactionType type();  // 操作类型 IN/OUT/ADJUST
    String batchNoArg() default "";   // 方法参数中批次号的SpEL表达式
    String quantityArg() default "";   // 方法参数中数量的SpEL表达式
    String relatedIdArg() default "";  // 方法参数中关联业务ID的SpEL表达式
    String notes() default "";         // 备注信息的SpEL表达式（可选）
    String collectionParam() default ""; // 指定集合参数名称
    String elementVar() default "item";  // 指定元素变量名
}
