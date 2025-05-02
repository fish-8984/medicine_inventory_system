package com.luyu.service.securityService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.entity.Roles;
import com.luyu.entity.Users;
import com.luyu.entity.CustomUserDetails;
import com.luyu.service.IRolesService;
import com.luyu.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements UserDetailsService {
    private final IUsersService usersService;
    private final ObjectMapper objectMapper;
    private final IRolesService roleService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始查询用户信息: {}", username);

        LambdaQueryWrapper<Users> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(Users::getUsername, username);
        Users user = usersService.getOne(objectLambdaQueryWrapper);
        log.info("用户信息: {}", user);
        if (user == null) {
            return null;
        }
        // 获取角色和权限
        Integer roleId = user.getRoleId();
        Roles roles = roleService.getById(roleId);// 获取角色

        List<GrantedAuthority> authorities = new ArrayList<>();
        // 添加权限（从 JSON 解析）
        List<String> permissions = parsePermissions(roles.getPermissions());
        permissions.forEach(perm ->
                authorities.add(new SimpleGrantedAuthority(perm))
        );
        log.info("用户权限: {}", authorities);
        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPasswordHash(),  // 确保数据库中的密码是加密后的值
                authorities);
    }

    // 解析 JSON 格式的权限
    private List<String> parsePermissions(String permissionsJson) {
        try {
            log.info("开始解析权限JSON: {}", permissionsJson);
            List<String> permissions = objectMapper.readValue(permissionsJson, new TypeReference<>() {
            });
            log.info("解析结果: {}", permissions);
            return permissions;
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
