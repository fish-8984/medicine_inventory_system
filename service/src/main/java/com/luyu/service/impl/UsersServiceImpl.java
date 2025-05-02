package com.luyu.service.impl;

import com.luyu.entity.Users;
import com.luyu.mapper.UsersMapper;
import com.luyu.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
}
