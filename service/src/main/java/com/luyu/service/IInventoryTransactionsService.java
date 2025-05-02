package com.luyu.service;

import com.luyu.entity.Inventory;
import com.luyu.entity.InventoryTransactions;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luyu.entity.MedicineBatches;

/**
 * <p>
 * 库存流水记录表（支撑AI分析） 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
public interface IInventoryTransactionsService extends IService<InventoryTransactions> {

    Inventory addInventory(MedicineBatches medicineBatches);

    void insert(Inventory inventory);
}
