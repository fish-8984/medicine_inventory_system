package com.luyu.service.impl;

import com.luyu.entity.Patients;
import com.luyu.mapper.PatientsMapper;
import com.luyu.service.IPatientsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 患者信息表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-24
 */
@Service
public class PatientsServiceImpl extends ServiceImpl<PatientsMapper, Patients> implements IPatientsService {

}
