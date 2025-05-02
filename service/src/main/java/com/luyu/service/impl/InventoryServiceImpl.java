package com.luyu.service.impl;

import com.luyu.entity.Inventory;
import com.luyu.mapper.InventoryMapper;
import com.luyu.service.IInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实时库存状态表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {
}
