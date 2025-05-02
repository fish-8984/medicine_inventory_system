package com.luyu.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.constant.JwtClaimsConstant;
import com.luyu.constant.JwtRedis;
import com.luyu.entity.CustomUserDetails;
import com.luyu.properties.JwtProperties;
import com.luyu.result.Result;
import com.luyu.security.AuthorityCacheService;
import com.luyu.service.securityService.SecurityUserServiceImpl;
import com.luyu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserInterceptor extends OncePerRequestFilter {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProperties jwtProperties;
    private final SecurityUserServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final AuthorityCacheService authorityCacheService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        return path.equals("/login") || method.equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = parseToken(request);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
                String username = (String) claims.get(JwtClaimsConstant.USERNAME);
                Integer userId = (Integer) claims.get(JwtClaimsConstant.USER_ID);
                log.info("用户 [{}] Token 解析成功", username);

                String redisKey = String.format("%s:%s:%s", JwtRedis.JWT_AUTH, username, token);
                if (Boolean.FALSE.equals(redisTemplate.hasKey(redisKey))) {
                    log.error("Token 不存在或已过期");
                    sendError(response, "Token 无效或已过期");
                    return;
                }

                Long createdAt = (Long) redisTemplate.opsForHash().get(redisKey, JwtRedis.JWT_CREATED_AT);
                if (createdAt == null) {
                    log.warn("无效Token [{}]，缺失时间戳字段", token);
                    sendError(response, "Token 无效");
                    return;
                }

                long maxAgeMillis = 24 * 60 * 60 * 1000; // 24 小时
                long currentTime = System.currentTimeMillis();
                if (currentTime - createdAt > maxAgeMillis) {
                    redisTemplate.delete(redisKey);
                    log.error("Token 超过最大存活时间");
                    sendError(response, "Token 已过期");
                    return;
                }
                // 检查是否需要刷新 Token 有效期
                long ttl = redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
                if (ttl < maxAgeMillis - (currentTime - createdAt)) {
                    redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                    log.info("用户 [{}] Token 有效期已刷新", username);
                }

                Collection<? extends GrantedAuthority> authorities = authorityCacheService.getCachedAuthorities(username);
                if (authorities == null) {
                    CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                    authorities = customUserDetails.getAuthorities();
                    authorityCacheService.cacheAuthorities(username, authorities);
                }

                // 创建 Authentication 对象
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                        null,
                                authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("用户 [{}] 身份已设置到 SecurityContext", username);

            } catch (Exception ex) {
                log.error("JWT 校验失败: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                sendError(response, "Token 验证失败");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        return request.getHeader(jwtProperties.getUserTokenName());
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new Result<>(401, message, null)
                )
        );
    }
}
