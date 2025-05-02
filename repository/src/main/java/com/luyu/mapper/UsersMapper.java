package com.luyu.mapper;

import com.luyu.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    @Select("select * from users where username = #{username}")
    Users getById(String username);
}
