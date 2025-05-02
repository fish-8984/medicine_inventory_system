package com.luyu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luyu.entity.Roles;
import com.luyu.entity.Users;
import com.luyu.result.Result;
import com.luyu.service.IRolesService;
import com.luyu.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限配置表
 * @author 
 * @since 2025-02-25
 */

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolesController {
    private final IRolesService rolesService;
    private final IUsersService usersService;

    /**
     * 保存角色
     * @param roles
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Roles:create')")
    @PostMapping
    public Result<String> save(@RequestBody Roles roles){
        log.info("保存角色:{}",roles);
        if (roles == null) {
            return Result.error("保存失败!");
        }
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Roles::getName,roles.getName());
        Roles one = rolesService.getOne(queryWrapper);
        if(one != null){
            return Result.error("角色名已存在!");
        }
        roles.setRoleId(null);
        if (!rolesService.save(roles)) {
            return Result.error("保存失败!");
        }
        return Result.success("保存成功");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Roles:delete')")
    @DeleteMapping
    public Result<String> delete(Long id){
        log.info("删除角色:{}",id);
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getRoleId,id);
        Users one = usersService.getOne(queryWrapper);
        if(one!= null){
            return Result.error("该角色已被用户使用,无法删除!");
        }
        if (!rolesService.removeById(id)) {
            return Result.error("删除失败!");
        }
        return Result.success("删除成功");
    }

    /**
     * 修改角色
     * @param roles
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Roles:update')")
    @PutMapping
    public Result<String> update(@RequestBody Roles roles){
        log.info("修改角色:{}",roles);
        if (roles == null || roles.getRoleId() == null) {
            return Result.error("修改失败!");
        }
        LambdaUpdateWrapper<Roles> updateWrapper = new LambdaUpdateWrapper<Roles>()
                .eq(Roles::getRoleId,roles.getRoleId())
                .set(roles.getName() != null, Roles::getName,roles.getName())
                .set(roles.getPermissions() != null, Roles::getPermissions,roles.getPermissions())
                .set(roles.getDescription() != null, Roles::getDescription,roles.getDescription());
        if (!rolesService.update(updateWrapper)) {
            return Result.error("修改失败!");
        }
        return Result.success("修改成功");
    }

    /**
     * 查询角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Roles:read')")
    @GetMapping("/{id}")
    public Result<Roles> getById(@PathVariable Long id){
        log.info("查询角色:{}",id);
        if (id == null) {
            return Result.error("查询失败!");
        }
        Roles roles = rolesService.getById(id);
        if (roles != null) {
            roles.setPermissions(null);
        }
        return Result.success(roles);
    }

    /**
     * 查询所有角色
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Roles:read')")
    @GetMapping
    public Result<List<Roles>> list(){
        List<Roles> list = rolesService.list();
        if (list != null) {
            for (Roles roles : list) {
                roles.setPermissions(null);
            }
        }
        return Result.success(list);
    }
}
