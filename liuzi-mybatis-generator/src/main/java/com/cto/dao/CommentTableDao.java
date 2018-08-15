package com.cto.dao;

import com.cto.model.CommentTable;

public interface CommentTableDao {
    int deleteByPrimaryKey(Long id);

    int insert(CommentTable record);

    int insertSelective(CommentTable record);

    CommentTable selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentTable record);

    int updateByPrimaryKey(CommentTable record);
}