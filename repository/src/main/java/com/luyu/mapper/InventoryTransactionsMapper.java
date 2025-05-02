package com.luyu.mapper;

import com.luyu.entity.InventoryTransactions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存流水记录表（支撑AI分析） Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Mapper
public interface InventoryTransactionsMapper extends BaseMapper<InventoryTransactions> {

}
