package com.cto.dao;

import com.cto.entity.UserExtend;

public interface UserExtendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserExtend record);

    int insertSelective(UserExtend record);

    UserExtend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserExtend record);

    int updateByPrimaryKey(UserExtend record);
}