package com.liuzi.mybatis.currency.type;


public enum SqlType {
	
	/**
	 * 新增
	 */
	$insert,
	/**
	 * 批量新增
	 */
	$insertArray,
	/**
	 * 批量新增
	 */
	$insertList,
	
	/**
	 * 删除
	 */
	$delete,
	/**
	 * 更新
	 */
	$update,
	/**
	 * 查询一条
	 */
	$selectOne,
	/**
	 * 查询多条
	 */
	$selectList,
	/**
	 * 查询kv
	 */
	$selectMap,
	
	
	$selectOneByPk,
}
