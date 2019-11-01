package com.liuzi.mybatis.server.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.currency.data.Sort;
import com.liuzi.mybatis.server.mapper.BaseDao;
import com.liuzi.mybatis.server.pojo.Page;




public abstract class BaseServiceImpl<T> implements BaseService<T>{
	
	public abstract BaseDao<T> dao();
	
	@Autowired
	private SqlSessionTemplate _template;
	
	@Override
	public <K> int $updateByPk(K record){
		return dao().$updateByPk(record);
	}
	
	@Override
	public <K> int $insert(K record){
		return dao().$insert(record);
	}
	
	@Override
	public int $deleteByPk(Object pkValue){
		return dao().$deleteByPk(pkValue);
	}
	
	@Override
	public T $selectOneByPk(Object pkValue){
		return dao().$selectOneByPk(pkValue);
	}
	
	@Override
	public T $selectOneBySql(SQL sql){
		return dao().$selectOneBySql(sql);
	}
	
	@Override
	public T $selectOneByQuery(Query query){
		return dao().$selectOneByQuery(query);
	}

	@Override
	public <K> List<K> selectList(Query query){
		return dao().selectList(query);
	}
	
	@Override
	public Page<T> $selectPage(Query query){
		return dao().$selectPage(query);
	}
	
	@Override
	public <K> Page<K> selectPage(Query query){
		return selectPage(null, query);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <K> Page<K> selectPage(String mapperId, Query query){
		mapperId = StringUtils.isEmpty(mapperId) ? "selectList" : mapperId;
		
		//创建分页对象
		Page<K> page = null;
		if(query == null || (page = (Page<K>) query.get(HandlerConsts.$PAGE)) == null){
			page = new Page<>();
		}
		
		Class<?> interfaces = dao().getClass().getInterfaces()[0];
		String nameSpace = interfaces.getPackage().getName() + "." + 
				interfaces.getSimpleName() + "." + mapperId;
		
		//查询数据列表
		List<K> data = _template.selectList(nameSpace, (Map<String, Object>) query);
		//查询数据数量
		long totalCount = page.getTotalCount();
		//计算总页数
		int pageSize = page.getPageSize();
		int pageTotal = (int) (totalCount / pageSize);
		if (totalCount % pageSize > 0) {
	    	++ pageTotal;
	    }
		//当前页条数
		int number = data == null ? 0 : data.size();
		
		//组装返回实体
		page.setData(data);//返回数据
		page.setPageTotal(pageTotal);//总页数
		page.setNumber(number);//当前页条数
		return page;
	}
}
