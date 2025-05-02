package com.luyu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/actuator")
@RequiredArgsConstructor
public class HealthChecks {

    /**
     * 健康检查
     * @return
     */
    @GetMapping("/health")
    public String home() {
        log.info("运行状况检查，时间：{}", LocalDateTime.now());
        return "OK";
    }
}
