package com.cto.dao;

import com.cto.model.Advert;

public interface AdvertDao {
    int deleteByPrimaryKey(Long id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}