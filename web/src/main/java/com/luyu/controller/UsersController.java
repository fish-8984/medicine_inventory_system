package com.luyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyu.constant.*;
import com.luyu.context.AuditLog;
import com.luyu.context.BaseContext;
import com.luyu.dto.UserPasswordDTO;
import com.luyu.entity.Departments;
import com.luyu.entity.Roles;
import com.luyu.entity.Users;
import com.luyu.properties.JwtProperties;
import com.luyu.result.Result;
import com.luyu.security.AuthorityCacheService;
import com.luyu.service.IDepartmentsService;
import com.luyu.service.IRolesService;
import com.luyu.service.IUsersService;
import com.luyu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 系统用户表
 * @author 
 * @since 2025-02-25
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final IUsersService iusersService;
    private final IDepartmentsService idepartmentsService;
    private final IRolesService iRolesService;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthorityCacheService authorityCacheService;
    /**
     * 添加员工
     * @param users
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Users:create')")
    @Transactional
    @AuditLog(action = OperationType.INSERT, targetTable = Table.USERS)
    @PostMapping("/register")
    public Result<String> register(@RequestBody Users users){
        log.info("用户信息：{}", users);
        if (users == null) {
            return Result.error("用户信息不能为空!");
        }
        LambdaQueryWrapper<Users> userLqw = new LambdaQueryWrapper<>();
        userLqw.eq(Users::getUsername, users.getUsername());
        Users one = iusersService.getOne(userLqw);
        if (one != null) {
            return Result.error("用户名已存在!");
        }
        LambdaQueryWrapper<Departments> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Departments::getName, users.getDepartment());
        Departments departments = idepartmentsService.getOne(lqw);
        if (departments == null) {
            return Result.error("部门不存在！");
        }
        LambdaQueryWrapper<Roles> rolesLqw = new LambdaQueryWrapper<>();
        rolesLqw.eq(Roles::getRoleId, users.getRoleId());
        Roles roles = iRolesService.getOne(rolesLqw);
        if (roles == null) {
            return Result.error("角色不存在！");
        }
        String password = BCrypt.hashpw(InitialPassword.INITIAL_PASSWORD, BCrypt.gensalt());
        users.setPasswordHash(password);
        users.setCreatedAt(LocalDateTime.now());
        users.setRoleId(roles.getRoleId());
        users.setStatus(StatusConstant.ENABLE);
        users.setDepartmentId(departments.getDepartmentId());
        users.setUserId(null);
        if (!iusersService.save(users)) {
            return Result.error("添加失败!");
        }
        BaseContext.setTargetId(users.getUserId());
        return Result.success("添加成功");
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('Users:delete')")
    @AuditLog(action = OperationType.DELETE, targetTable = Table.USERS)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        if (id == null) {
            return Result.error("删除失败！");
        }
        LambdaUpdateWrapper<Users> lwq = new LambdaUpdateWrapper<>();
        lwq.eq(Users::getUserId, id);
        lwq.set(Users::getStatus, StatusConstant.DISABLE);
        BaseContext.setTargetId(id);
        if (!iusersService.update(lwq)) {
            return Result.error("删除失败！");
        }
        return Result.success("删除成功！");
    }

    /**
     * 修改员工
     * @param users
     * @return
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('Users:update')")
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.USERS)
    @PutMapping
    public Result<String> update(@RequestBody Users users){
        log.info("用户信息：{}", users);
        if (users == null) {
            return Result.error("用户信息不能为空！");
        }
        LambdaQueryWrapper<Departments> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Departments::getName, users.getDepartment());
        Departments departments = idepartmentsService.getOne(lqw);
        if (departments == null) {
            return Result.error("部门不存在！");
        }
        LambdaUpdateWrapper<Users> lwq = new LambdaUpdateWrapper<Users>()
        .eq(Users::getUserId, users.getUserId())
        .set(users.getDepartment() != null, Users::getDepartment, users.getDepartment())
        .set(Users::getDepartmentId, departments.getDepartmentId());
        BaseContext.setTargetId(users.getUserId());
        if (!iusersService.update(lwq)) {
            return Result.success("修改失败！");
        }
        return Result.success("修改成功！");
    }

    /**
     * 修改员工状态
     * @param status
     * @param id
     * @return
     */
    @Transactional
    @PutMapping("/updateStatus/{status}/{id}")
    @PreAuthorize("hasAnyAuthority('Users:update')")
    public Result<String> updateStatus(@PathVariable Integer status, @PathVariable Long id) {
        if (status == null) {
            return Result.error("修改失败！");
        }
        LambdaUpdateWrapper<Users> lwq = new LambdaUpdateWrapper<>();
        lwq.eq(Users::getUserId, id);
        lwq.set(Users::getStatus, status);
        if (!iusersService.update(lwq)) {
            return Result.error("修改失败！");
        }
        return Result.success("修改成功！");
    }


    /**
     * 修改密码
     * @param userPasswordDTO
     * @return
     */
    @Transactional
    @AuditLog(action = OperationType.UPDATE, targetTable = Table.USERS)
    @PutMapping("/updatePassword")
    public Result<String> updatePassword(@RequestBody UserPasswordDTO userPasswordDTO, HttpServletRequest request) {
        Users users = userPasswordDTO.getUsers();
        String oldPassword = userPasswordDTO.getOldPassword();
        if (users == null) {
            return Result.error("修改失败!");
        }
        if (oldPassword == null) {
            return Result.error("原密码不能为空！");
        }
        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Users::getUserId, users.getUserId());
        Users one = iusersService.getOne(lqw);
        boolean checkpw = BCrypt.checkpw(oldPassword, one.getPasswordHash());
        if (!checkpw) {
            return Result.error("原密码错误！");
        }
        if (oldPassword.equals(users.getPasswordHash())) {
            return Result.error("新密码不能与原密码相同！");
        }
        String password = BCrypt.hashpw(users.getPasswordHash(), BCrypt.gensalt());
        one.setPasswordHash(password);
        BaseContext.setTargetId(users.getUserId());
        if (!iusersService.updateById(one)) {
            return Result.error("修改失败！");
        }

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
        log.info("用户[{}]修改密码成功", username);
        return Result.success("修改成功！");
    }

    /**
     * 查询单个员工
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    @PreAuthorize("hasAnyAuthority('Users:read')")
    public Result<Users> selectById(@PathVariable Long id){
        if (id == null) {
            return Result.error("查询失败！");
        }
        Users users = iusersService.getById(id);
        if (users == null) {
            return Result.error("查询失败!");
        }
        users.setPasswordHash("******");
        return Result.success(users);
    }

    /**
     * 查询单个员工
     * @param name
     * @return
     */
    @GetMapping("/selectByName/{name}")
    @PreAuthorize("hasAnyAuthority('Users:read')")
    public Result<Users> selectByName(@PathVariable String name){
        if (name == null) {
            return Result.error("查询失败！");
        }
        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Users::getUsername, name);
        Users users = iusersService.getOne(lqw);
        if (users == null) {
            return Result.error("查询失败!");
        }
        users.setPasswordHash("******");
        return Result.success(users);
    }

    /**
     * 查询所有员工
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('Users:read')")
    public Result<Page<Users>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
                                    ){
        log.info("page = {}, pageSize = {}", page, pageSize);
        page = Math.max(page, 1);
        pageSize = Math.min(Math.max(pageSize, 1), 100);
        Page<Users> pageInfo = new Page<>(page, pageSize);
        iusersService.page(pageInfo);
        for (Users users : pageInfo.getRecords()) {
            users.setPasswordHash("******");
        }
        return Result.success(pageInfo);
    }
}
