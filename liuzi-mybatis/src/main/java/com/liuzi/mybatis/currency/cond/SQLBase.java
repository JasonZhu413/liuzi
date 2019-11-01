package com.liuzi.mybatis.currency.cond;

import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.Order;
import com.liuzi.mybatis.currency.data.Sort;
import com.liuzi.mybatis.currency.util.DataUtil;
import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;



public class SQLBase{
	
	protected StringBuffer sql;
	
	public SQLBase SELECT(){
		this.append(SQLConsts.SELECT);
		return this;
	}
	public SQLBase DELETE(){
		this.append(SQLConsts.DELETE);
		return this;
	}
	public SQLBase INSERT(){
		this.append(SQLConsts.INSERT);
		return this;
	}
	public SQLBase UPDATE(){
		this.append(SQLConsts.UPDATE);
		return this;
	}
	public SQLBase FROM(){
		this.append(SQLConsts.FROM);
		return this;
	}
	public SQLBase FROM(String table){
		this.put(SQLConsts.FROM, table);
		return this;
	}
	public SQLBase TABLE(String table){
		this.append(table);
		return this;
	}
	public SQLBase WHERE(){
		this.append(SQLConsts.WHERE);
		return this;
	}
	public SQLBase AS(){
		this.put(SQLConsts.AS);
		return this;
	}
	public SQLBase AS(String name){
		this.put(SQLConsts.AS, name);
		return this;
	}
	public SQLBase AS(Class<?> clazz){
		this.put(SQLConsts.AS, DataUtil.getTable(clazz).getAs());
		return this;
	}
	public SQLBase DISTINCT(String column){
		this.put(SQLConsts.DISTINCT, SQLConsts.OPEN, column, SQLConsts.CLOSE);
		return this;
	}
	public SQLBase OPEN(){
		this.append(SQLConsts.OPEN);
		return this;
	}
	public SQLBase CLOSE(){
		this.append(SQLConsts.CLOSE);
		return this;
	}
	public SQLBase AND(){
		this.append(SQLConsts.AND);
		return this;
	}
	public SQLBase OR(){
		this.append(SQLConsts.OR);
		return this;
	}
	public SQLBase EQ(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function EQ params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase NEQ(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function NEQ params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.NEQ, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase LIKE(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function LIKE params value is empty");
			return this;
		}
		
		this.put(column, SQLConsts.SPACE, SQLConsts.LIKE, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase GT(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function GT params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.GT, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase LT(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function LT params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.LT, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase GTE(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function GTE params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.GTE, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase LTE(String column, Object value){
		if(StringUtils.isEmpty(value)){
			Log.warn("SQL build warning, function LTE params value is empty");
			return this;
		}
		this.put(column, SQLConsts.SPACE, SQLConsts.LTE, SQLConsts.SPACE);
		this.decorate(value);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase IN(String column, Object... values){
		if(values == null || values.length == 0){
			Log.warn("SQL build warning, function IN params values is empty");
			return this;
		}
		
		this.put(column, SQLConsts.IN, SQLConsts.OPEN);
		for(int i = 0, j = values.length; i < j; i++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values[i]);
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	public SQLBase IN(String column, List<Object> values){
		if(values == null || values.isEmpty()){
			Log.warn("SQL build warning, function IN params values is empty");
			return this;
		}
		
		this.put(column, SQLConsts.IN, SQLConsts.OPEN);
		for(int i = 0, j = values.size(); i < j; i++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values.get(i));
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	public SQLBase NOT_IN(String column, Object... values){
		if(values == null || values.length == 0){
			Log.warn("SQL build warning, function NOT_IN params values is empty");
			return this;
		}
		this.put(column, SQLConsts.NOT_IN, SQLConsts.OPEN);
		for(int i = 0, j = values.length; i < j; i++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values[i]);
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	public SQLBase NOT_IN(String column, List<Object> values){
		if(values == null || values.isEmpty()){
			Log.warn("SQL build warning, function NOT_IN params values is empty");
			return this;
		}
		this.put(column, SQLConsts.NOT_IN, SQLConsts.OPEN);
		for(int i = 0, j = values.size(); i < j; i++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values.get(i));
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	public SQLBase BETWEEN_AND(String column, Object start, Object end){
		if(StringUtils.isEmpty(start) || StringUtils.isEmpty(end)){
			Log.warn("SQL build warning, function BETWEEN_AND params start/end is empty");
			return this;
		}
		this.put(column, SQLConsts.BETWEEN);
		this.decorate(start);
		this.append(SQLConsts.AND);
		this.decorate(end);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase GROUP_BY(String... columns){
		if(columns == null || columns.length == 0){
			Log.warn("SQL build warning, function GROUP_BY params columns is empty");
			return this;
		}
		
		if(columns != null && columns.length > 0){
			this.append(SQLConsts.GROUP_BY);
			int i = 0;
			for(String column : columns){
				if(StringUtils.isEmpty(column)){
					continue;
				}
				
				if(i > 0){
					this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
				}
				this.append(column);
				i++;
			}
		}
		return this;
	}
	public SQLBase GROUP_BY(List<String> columns){
		if(columns == null || columns.size() == 0){
			Log.warn("SQL build warning, function GROUP_BY params columns is empty");
			return this;
		}
		
		if(columns != null && columns.size() > 0){
			this.append(SQLConsts.GROUP_BY);
			int i = 0;
			for(String column : columns){
				if(StringUtils.isEmpty(column)){
					continue;
				}
				
				if(i > 0){
					this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
				}
				this.append(column);
				i++;
			}
		}
		return this;
	}
	public SQLBase ORDER_BY(Sort sort){
		List<Sort> all = null;
		if(sort == null || (all = sort.getAll()) == null || all.isEmpty()){
			Log.warn("SQL build warning, function ORDER_BY params sort is empty");
			return this;
		}
		
		this.append(SQLConsts.ORDER_BY);
		int i = 0;
		for(Sort s : all){
			String ss = null;
			Order so = null;
			if(s == null || StringUtils.isEmpty((ss = s.getSort())) || 
					(so = s.getOrder()) == null){
				continue;
			}
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.put(ss, SQLConsts.SPACE, so.toString());
			i++;
		}
		return this;
	}
	public SQLBase LIMIT(int limit, int offset){
		if(limit < 0 || offset <= 0){
			Log.warn("SQL build warning, function LIMIT params limit/offset is empty");
			return this;
		}
		
		this.append(SQLConsts.LIMIT);
		//this.decorate(limit);
		this.append(limit);
		this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
		//this.decorate(offset);
		this.append(offset);
		this.append(SQLConsts.SPACE);
		return this;
	}
	public SQLBase HAVING(String sql){
		if(StringUtils.isEmpty(sql)){
			Log.warn("SQL build warning, function HAVING params sql is empty");
			return this;
		}
		
		this.put(SQLConsts.HAVING, SQLConsts.OPEN, sql, SQLConsts.CLOSE);
		return this;
	}
	public SQLBase UNIX_TIMESTAMP(String date){
		if(StringUtils.isEmpty(date)){
			Log.warn("SQL build warning, function UNIX_TIMESTAMP params date is empty");
			return this;
		}
		
		this.put(SQLConsts.UNIX_TIMESTAMP, SQLConsts.OPEN);
		this.decorate(date);
		this.put(SQLConsts.CLOSE, SQLConsts.OPEN);
		return this;
	}
	public SQLBase UNIX_TIMESTAMP(Date date){
		if(date == null){
			Log.warn("SQL build warning, function UNIX_TIMESTAMP params date is empty");
			return this;
		}
		
		this.put(SQLConsts.UNIX_TIMESTAMP, SQLConsts.OPEN);
		this.decorate(date);
		this.put(SQLConsts.CLOSE, SQLConsts.OPEN);
		return this;
	}
	public SQLBase INNER_JOIN() {
		this.append(SQLConsts.INNER_JOIN);
		return this;
	}
	public SQLBase INNER_JOIN(String table) {
		this.put(SQLConsts.INNER_JOIN, table);
		return this;
	}
	public SQLBase INNER_JOIN(String table, String as) {
		this.put(SQLConsts.INNER_JOIN, table, SQLConsts.AS, as);
		return this;
	}
	public SQLBase LEFT_JOIN() {
		this.append(SQLConsts.LEFT_JOIN);
		return this;
	}
	public SQLBase LEFT_JOIN(String table) {
		this.put(SQLConsts.LEFT_JOIN, table);
		return this;
	}
	public SQLBase LEFT_JOIN(String table, String as) {
		this.put(SQLConsts.LEFT_JOIN, table, SQLConsts.AS, as);
		return this;
	}
	
	public SQLBase RIGHT_JOIN() {
		this.append(SQLConsts.RIGHT_JOIN);
		return this;
	}
	public SQLBase RIGHT_JOIN(String table) {
		this.put(SQLConsts.RIGHT_JOIN, table);
		return this;
	}
	public SQLBase RIGHT_JOIN(String table, String as) {
		this.put(SQLConsts.RIGHT_JOIN, table, SQLConsts.AS, as);
		return this;
	}
	
	public SQLBase OUTER_JOIN() {
		this.append(SQLConsts.OUTER_JOIN);
		return this;
	}
	public SQLBase OUTER_JOIN(String table) {
		this.put(SQLConsts.OUTER_JOIN, table);
		return this;
	}
	public SQLBase OUTER_JOIN(String table, String as) {
		this.put(SQLConsts.OUTER_JOIN, table, SQLConsts.AS, as);
		return this;
	}
	
	public SQLBase ON() {
		this.append(SQLConsts.ON);
		return this;
	}
	public SQLBase JOIN_ON(String... sqls) {
		this.append(SQLConsts.ON);
		int length;
		if(sqls != null && (length = sqls.length) > 0){
			for(int i = 0; i < length; i ++ ){
				if(i > 0){
					this.append(SQLConsts.AND);
				}
				this.append(sqls[i]);
			}
		}
		return this;
	}
	
	/**
	 * 自定义
	 */
	public SQLBase CUSTOM(Object... sqls){
		if(sqls == null || sqls.length == 0){
			Log.warn("SQL build warning, function CUSTOM params sqls is empty");
			return this;
		}
		
		for(Object s : sqls){
			this.append(s);
		}
		return this;
	}
	/**
	 * 换行
	 */
	public SQLBase ENTER(){
		this.append(SQLConsts.ENTER);
		return this;
	}
	
	protected void put(Object... objs){
		for(Object o : objs){
			this.sql.append(o);
		}
	}
	protected void append(Object obj){
		this.sql.append(obj);
	}
	protected void decorate(Object obj){
		if(obj == null){
			this.sql.append("null");
			return;
		}
		this.sql.append(SQLConsts.DECORATE);
		this.sql.append(obj);
		this.sql.append(SQLConsts.DECORATE);
	}
	
	@Override
	public String toString() {
		return this.sql.toString();
	}
	
	public void print() {
		System.out.println(DateUtil.now("yyyy-MM-dd HH:mm:ss") + " - SQL: " + toString());
	}
}
