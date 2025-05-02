package com.luyu.mapper;

import com.luyu.entity.MedicineCategories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 药品分类表（支持多级分类） Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-27
 */
@Mapper
public interface MedicineCategoriesMapper extends BaseMapper<MedicineCategories> {

}
