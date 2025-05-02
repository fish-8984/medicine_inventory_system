package com.luyu.mapper;

import com.luyu.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 实时库存状态表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

}
