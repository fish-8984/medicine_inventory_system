package com.luyu.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SomeService {
    public static int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Integer) authentication.getPrincipal();
    }
}