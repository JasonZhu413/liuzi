package com.cto.dao;

import com.cto.model.Download;

public interface DownloadDao {
    int deleteByPrimaryKey(Long id);

    int insert(Download record);

    int insertSelective(Download record);

    Download selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Download record);

    int updateByPrimaryKey(Download record);
}