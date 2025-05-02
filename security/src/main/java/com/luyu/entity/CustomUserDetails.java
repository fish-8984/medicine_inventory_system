package com.luyu.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
    //--- 自定义方法：获取用户ID ---
    @Getter
    private Integer id;         // 用户ID（自定义字段）
    private String username; // 用户名
    private String password; // 加密后的密码
    private boolean enabled; // 是否启用
    private Collection<? extends GrantedAuthority> authorities; // 权限列表

//     构造方法
    public CustomUserDetails(Integer id, String username, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    //--- 必须实现的3个核心方法 ---
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

     @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
