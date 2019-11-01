package com.liuzi.mybatis.currency.data;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.core.ResolvableType;

import lombok.Getter;

import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.provider.BaseSqlProvider;
import com.liuzi.mybatis.currency.util.DataUtil;
import com.liuzi.mybatis.server.mapper.BaseDao;


@Getter
public class TableMataData {
	/**
	 * 默认主键
	 */
	private static final String DEFAULT_PK = "id";
	/**
	 * 默认主键
	 */
	private static final String ALL_COLUMNS = "*";
	/**
	 * 缓存类属性对象
	 */
	private static final WeakHashMap<Class<?>, TableMataData> TABLE_CACHE = new WeakHashMap<>(64) ;
	
	/**
	 * 实体类
	 */
	private Class<?> entity;
	/**
	 * 表名
	 */
	private String table;
	/**
	 * 表别名
	 */
	private String tableAs;
	/**
	 * 表字段
	 */
	private List<ColumnData> columns;
	/**
	 * 主键名
	 */
	private String pk = DEFAULT_PK;
	/**
	 * 所有字段
	 */
	private String allColumns;
	
	public TableMataData(Class<?> entity){
		//实体
	    this.entity = entity;
	    //获取表信息
	    TableData tableData = DataUtil.getTable(entity);
	    //表名
	    this.table = tableData.getName();
	    //表别名
	    this.tableAs = tableData.getAs();
	    
	    this.columns = new ArrayList<>();
	    StringBuffer sbf = new StringBuffer();
	    
	    //获取所有字段
	    List<Field> fields = DataUtil.getFields(entity);
	    for(int i = 0, j = fields.size(); i < j; i ++){
	    	//字段
	    	ColumnData columnData = DataUtil.getColumn(fields.get(i));
	    	columns.add(columnData);
	    	
	    	String column = columnData.getColumn();
	    	String as = columnData.getAs();
	    	
	    	//基础所有查询字段
	    	if(i > 0){
				sbf.append(SQLConsts.PAUSE).append(SQLConsts.SPACE);
			}
			sbf.append(tableAs).append(SQLConsts.POINT).append(column);
			sbf.append(SQLConsts.AS).append(as);
			
			//主键
	    	if(columnData.isPk()){
	    		this.pk = column;
	    	}
	    }
	    
	    allColumns = sbf.length() > 0 ? sbf.toString() : ALL_COLUMNS;
	}
	
	/**
	 * 获取对象属性
	 * @param entity
	 * @return
	 */
	public static TableMataData getData(ProviderContext context){
        return getData(getEntityClass(context));
	}
	
	/**
	 * 获取实体类
	 * @param context ProviderContext
	 * @return
	 */
	private static Class<?> getEntityClass(ProviderContext context) {
        Class<?> mapperType = context.getMapperType();
        for (Type parent : mapperType.getGenericInterfaces()) {
            ResolvableType parentType = ResolvableType.forType(parent);
            if (parentType.getRawClass() == BaseDao.class) {
                return parentType.getGeneric(0).getRawClass();
            }
        }
        return null;
    }
	
	/**
	 * 获取对象属性
	 * @param entity
	 * @return
	 */
	public static TableMataData getData(Class<?> entity){
		TableMataData data = TABLE_CACHE.get(entity);
        if (data == null) {
        	data = new TableMataData(entity);
            TABLE_CACHE.put(entity, data);
        }
        return data;
	}
	
}
