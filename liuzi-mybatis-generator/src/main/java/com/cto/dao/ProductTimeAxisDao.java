package com.cto.dao;

import com.cto.model.ProductTimeAxis;

public interface ProductTimeAxisDao {
    int deleteByPrimaryKey(Long id);

    int insert(ProductTimeAxis record);

    int insertSelective(ProductTimeAxis record);

    ProductTimeAxis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductTimeAxis record);

    int updateByPrimaryKey(ProductTimeAxis record);
}