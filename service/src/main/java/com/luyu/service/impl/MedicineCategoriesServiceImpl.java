package com.luyu.service.impl;

import com.luyu.entity.MedicineCategories;
import com.luyu.mapper.MedicineCategoriesMapper;
import com.luyu.service.IMedicineCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 药品分类表（支持多级分类） 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-27
 */
@Service
public class MedicineCategoriesServiceImpl extends ServiceImpl<MedicineCategoriesMapper, MedicineCategories> implements IMedicineCategoriesService {

}
