package com.cto.dao;

import com.cto.model.DownloadLog;

public interface DownloadLogDao {
    int deleteByPrimaryKey(Long id);

    int insert(DownloadLog record);

    int insertSelective(DownloadLog record);

    DownloadLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DownloadLog record);

    int updateByPrimaryKey(DownloadLog record);
}