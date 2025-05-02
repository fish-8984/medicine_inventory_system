package com.luyu.config;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.constant.JwtClaimsConstant;
import com.luyu.constant.JwtRedis;
import com.luyu.entity.CustomUserDetails;
import com.luyu.entity.Users;
import com.luyu.interceptor.JsonAuthenticationFilter;
import com.luyu.interceptor.JwtTokenUserInterceptor;
import com.luyu.properties.CrossDomain;
import com.luyu.properties.JwtProperties;
import com.luyu.result.Result;
import com.luyu.service.IUsersService;
import com.luyu.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final CrossDomain crossDomain;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authManager,
            ObjectMapper objectMapper,
            JwtProperties jwtProperties,
            IUsersService iusersService,
            JwtTokenUserInterceptor jwtTokenUserInterceptor,
            RedisTemplate<String, Object> redisTemplate
    ) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/login","/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAt(jsonAuthFilter(objectMapper, jwtProperties, iusersService, redisTemplate, authManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenUserInterceptor , UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(0);
                            response.getWriter().write("{\"code\":401,\"msg\":\"未授权\"}");
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
    // 自定义 JSON 登录过滤器
    private JsonAuthenticationFilter jsonAuthFilter(
            ObjectMapper objectMapper,
            JwtProperties jwtProperties,
            IUsersService usersService,
            RedisTemplate<String, Object> redisTemplate,
            AuthenticationManager authManager
    ) throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter(authManager, objectMapper);
        filter.setFilterProcessesUrl("/login");

        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {

            CustomUserDetails one = (CustomUserDetails) authentication.getPrincipal();
            LambdaUpdateWrapper<Users> luw = new LambdaUpdateWrapper<>();
            luw.eq(Users::getUsername, one.getUsername());
            luw.set(Users::getLastLogin, LocalDateTime.now());
            usersService.update(luw);

            // 生成 claims
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, one.getId());
            claims.put(JwtClaimsConstant.USERNAME, one.getUsername());

            // 创建 JWT
            String token = JwtUtil.createJWT(
                    jwtProperties.getUserSecretKey(),
                    jwtProperties.getUserTtl(),
                    claims);

            String redisKey = String.format("%s:%s:%s", JwtRedis.JWT_AUTH, one.getUsername(), token);
            redisTemplate.opsForHash().put(redisKey, JwtRedis.JWT_USER_ID, one.getId());
            redisTemplate.opsForHash().put(redisKey, JwtRedis.JWT_CREATED_AT, System.currentTimeMillis());
            redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);

            log.info("sessionId: {}", request.getSession().getId());
            log.info("token: {}", token);

        // 3. 返回 JSON 响应
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(Result.success(token))
        );
    });

    // 设置失败处理器
    filter.setAuthenticationFailureHandler((request, response, exception) -> {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(
                Result.error("登录失败")
            )
        );
        log.error("登录失败", exception);
    });
        return filter;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(crossDomain.getAllowedOrigins());
        configuration.setAllowedMethods(crossDomain.getAllowedMethods());
        configuration.setAllowedHeaders(crossDomain.getAllowedHeaders());
        configuration.setExposedHeaders(crossDomain.getExposedHeaders());
        configuration.setAllowCredentials(crossDomain.isAllowCredentials());
        configuration.setMaxAge(crossDomain.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}