package com.luyu.controller;

import com.luyu.constant.JwtClaimsConstant;
import com.luyu.constant.JwtRedis;
import com.luyu.properties.JwtProperties;
import com.luyu.result.Result;
import com.luyu.security.AuthorityCacheService;
import com.luyu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LoginController {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthorityCacheService authorityCacheService;


    /**
     * 退出登录
     * @return
     */
    @PostMapping
    public Result<String> logout(HttpServletRequest request){
        // 从请求头中获取token
        String token = request.getHeader(jwtProperties.getUserTokenName());
        if (token == null || token.isEmpty()) {
            return Result.error("未找到token");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        String username = (String) claims.get(JwtClaimsConstant.USERNAME);
        // 删除Redis中的token
        String redisKey = String.format("%s:%s:%s", JwtRedis.JWT_AUTH, username, token);
        redisTemplate.delete(redisKey);
        // 清除安全上下文
        // 清除缓存
        authorityCacheService.evictAuthorities(username);
        SecurityContextHolder.clearContext();
        log.info("用户[{}]退出成功", username);
        return Result.success("退出成功");
    }
}
