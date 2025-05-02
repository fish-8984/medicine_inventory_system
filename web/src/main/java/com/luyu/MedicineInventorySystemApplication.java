package com.luyu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.luyu.mapper")
@EnableTransactionManagement       //开启注解方式的事务管理
@Slf4j
@EnableCaching         //开发缓存注解功能
@EnableScheduling      //开启任务调度
@EnableAsync           //开启异步任务
@EnableAspectJAutoProxy(exposeProxy = true) //开启AOP
public class MedicineInventorySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicineInventorySystemApplication.class, args);
        log.info("项目启动成功");
    }
}


