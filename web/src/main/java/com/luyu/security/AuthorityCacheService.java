package com.luyu.security;

import com.luyu.constant.JwtRedis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AuthorityCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthorityCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 将权限缓存到Redis，设置过期时间
    public void cacheAuthorities(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> authorityStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String key = JwtRedis.JWT_SECURITY_CONTEXT + username;
        redisTemplate.opsForValue().set(key, authorityStrings, 10, TimeUnit.MINUTES); // 10分钟过期
    }

    // 从Redis获取缓存的权限
    public Collection<? extends GrantedAuthority> getCachedAuthorities(String username) {
        String key = JwtRedis.JWT_SECURITY_CONTEXT + username;
        List<String> authorityStrings = (List<String>) redisTemplate.opsForValue().get(key);

        if (authorityStrings != null) {
            return authorityStrings.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return null;
    }

    // 清除权限缓存
    public void evictAuthorities(String username) {
        String key = JwtRedis.JWT_SECURITY_CONTEXT + username;
        redisTemplate.delete(key);
    }
}