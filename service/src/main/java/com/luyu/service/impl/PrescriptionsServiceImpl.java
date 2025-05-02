package com.luyu.service.impl;

import com.luyu.entity.Prescriptions;
import com.luyu.mapper.PrescriptionsMapper;
import com.luyu.service.IPrescriptionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 处方单主表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Service
@RequiredArgsConstructor
public class PrescriptionsServiceImpl extends ServiceImpl<PrescriptionsMapper, Prescriptions> implements IPrescriptionsService {
}
