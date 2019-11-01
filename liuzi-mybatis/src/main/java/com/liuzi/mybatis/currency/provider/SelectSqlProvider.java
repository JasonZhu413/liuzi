package com.liuzi.mybatis.currency.provider;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.builder.annotation.ProviderContext;

import com.liuzi.mybatis.currency.cond.Condition;
import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.Order;
import com.liuzi.mybatis.currency.data.Sort;
import com.liuzi.mybatis.currency.data.TableData;
import com.liuzi.mybatis.currency.data.TableMataData;
import com.liuzi.mybatis.currency.util.DataUtil;


/**
 * 通用查询实现类
 * @author zsy
 */
public class SelectSqlProvider {
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	public String $selectOneByPk(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
        
        return new SQL(){{
			String tableAs = data.getTableAs();
			
			SELECT(data.getAllColumns());
			FROM(data.getTable());
			AS(tableAs);
			WHERE();
			CUSTOM(tableAs, SQLConsts.POINT);
			EQ(data.getPk(), para.get("pkValue"));
		}}.toString();
	}
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @return
	 */
	public String $selectOneByColumnMap(ProviderContext context, Map<String, Object> para){
		return getSelectColumnMap(context, para);
	}
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @return
	 */
	public String $selectListByColumnMap(ProviderContext context, Map<String, Object> para){
		return getSelectColumnMap(context, para);
	}
	
	@SuppressWarnings("unchecked")
	private String getSelectColumnMap(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		
		Map<String, Object> columns = (Map<String, Object>) para.get("columns");
		
		return new SQL(){{
			String tableAs = data.getTableAs();
			
			SELECT(data.getAllColumns());
			FROM(data.getTable());
			AS(tableAs);
			WHERE();
			int i = 0;
			for(Entry<String, Object> entry : columns.entrySet()){
				if(i > 0){
					AND();
				}
				CUSTOM(tableAs, SQLConsts.POINT);
				EQ(entry.getKey(), entry.getValue());
				i++;
			}
		}}.toString();
	}
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @param clazz 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $selectOneTargetByPk(ProviderContext context, Map<String, Object> para){
		Object pkValue = para.get("pkValue");
		Class<K> target = (Class<K>) para.get("target");
		
		return new SQL(){{
			SELECT_BY_PK(target, pkValue);
		}}.toString();
	}
	
	/**
	 * 根据字段查询单条
	 * @param record 实体数据
	 * @return
	 */
	public <K> String $selectOneTargetByColumns(ProviderContext context, Map<String, Object> para){
		return getSelectTargetColumns(context, para);
	}
	
	/**
	 * 根据字段查询多条
	 * @param record 实体数据
	 * @return
	 */
	public <K> String $selectListTargetByColumns(ProviderContext context, Map<String, Object> para){
		return getSelectTargetColumns(context, para);
	}
	
	@SuppressWarnings("unchecked")
	private <K> String getSelectTargetColumns(ProviderContext context, Map<String, Object> para){
		K target = (K) para.get("target");
		return new SQL(){{
			SELECT(target);
		}}.toString();
	}
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	public <K> String $selectOneTargetByColumnMap(ProviderContext context, Map<String, Object> para){
		return getSelectTargetColumnMap(context, para);
	}
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	public <K> String $selectListTargetByColumnMap(ProviderContext context, Map<String, Object> para){
		return getSelectTargetColumnMap(context, para);
	}
	
	@SuppressWarnings("unchecked")
	private <K> String getSelectTargetColumnMap(ProviderContext context, Map<String, Object> para){
		Map<String, Object> columns = (Map<String, Object>) para.get("columns");
		Class<K> target = (Class<K>) para.get("target");
		
		TableData tableData = DataUtil.getTable(target);
		String as = tableData.getAs();
		
		return new SQL(){{
			SELECT(target);
			WHERE();
			int i = 0;
			for(Entry<String, Object> entry : columns.entrySet()){
				if(i > 0){
					AND();
				}
				CUSTOM(as, SQLConsts.POINT);
				EQ(entry.getKey(), entry.getValue());
				i++;
			}
		}}.toString();
	}
	
	//--------------------------------- Query -----------------------------------------------
	
	public String $selectByQuery(ProviderContext context, Map<String, Object> para){
        return getQuerySql(context, para);
    }
	
	@SuppressWarnings("unchecked")
	private <K> String getQuerySql(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		Query query = (Query) para.get("query");
		
		return new SQL(){{
			Class<?> target = data.getEntity();
			if(para.containsKey("target")){
				target = (Class<K>) para.get("target");
			}
			
			SELECT(target);
			//条件
			List<Condition> conditions = query.getCondition();
			if(conditions != null && !conditions.isEmpty()){
				WHERE();
				for(Condition c : conditions){
					c.condition(this);
				}
			}
			//分组
			List<String> groupBy = (List<String>) query.get(HandlerConsts.$GROUP_BY);
			if(groupBy != null && !groupBy.isEmpty()){
				String[] g = new String[groupBy.size()];
				GROUP_BY(groupBy.toArray(g));
			}
			//排序
			Map<String, String> orderBy = (Map<String, String>) query.get(HandlerConsts.$ORDER_BY);
			if(orderBy != null && !orderBy.isEmpty()){
				Sort s = new Sort();
				for(Entry<String, String> entry : orderBy.entrySet()){
					s.add(entry.getKey(), Order.valueOf(entry.getValue()));
				}
				ORDER_BY(s);
			}
			//分页
			/*Object limit = query.get(HandlerConsts.$LIMIT);
			Object offset = query.get(HandlerConsts.$OFFSET);
			if(limit != null && offset != null){
				LIMIT((int) limit, (int) offset);
			}*/
		}}.toString();
	}
	
}
