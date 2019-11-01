package com.liuzi.elasticsearch.data;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;






import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.StringUtils;




import com.liuzi.util.common.Log;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 检索条件（日期范围）
 * @author zsy
 */
@Data
public class EsScript{
	
	private Map<String, Object> params;
	private float boost = 0F;
	private String script;
	
	public static EsScriptBuilder where(){
		return new EsScript.EsScriptBuilder();
	}
	
	@SuppressWarnings("unused")
	private static class EsScriptBuilder{
		
		private static final AtomicInteger AI = new AtomicInteger();
		
		private EsScript dateRang;
		private Map<String, Object> params = new HashMap<>();
		private String paramName;
		private float boost;
		private String script;
		private String doc;
		
		public EsScriptBuilder(){
			dateRang = new EsScript();
			script = "";
		}
		
		/**
		 * 字段
		 */
		public EsScriptBuilder doc(String doc){
			this.doc = doc;
			script += "doc['" + doc + "']";
			return this;
		}
		
		/**
		 * 字段值
		 */
		public EsScriptBuilder val(){
			script += ".value";
			return this;
		}
		/**
		 * 字段长度
		 */
		public EsScriptBuilder len(){
			script += ".length()";
			return this;
		}
		/**
		 * 时间毫秒
		 */
		public EsScriptBuilder millis(){
			script += ".getMillis()";
			return this;
		}
		
		/**
		 * 相等
		 */
		public EsScriptBuilder is(Object val){
			return _base(val, "=");
		}
		
		/**
		 * 不相等
		 */
		public EsScriptBuilder not(Object val){
			return _base(val, "!=");
		}
		
		/**
		 * 大于
		 */
		public EsScriptBuilder gt(Object val){
			return _base(val, ">");
		}
		
		/**
		 * 大于等于
		 */
		public EsScriptBuilder gte(Object val){
			return _base(val, ">=");
		}
		
		/**
		 * 小于
		 */
		public EsScriptBuilder lt(Object val){
			return _base(val, "<");
		}
		
		/**
		 * 小于等于
		 */
		public EsScriptBuilder lte(Object val){
			return _base(val, "<=");
		}
		
		public EsScriptBuilder end(){
			script += ";";
			return this;
		}
		
		public EsScriptBuilder add(){
			script += "+";
			return this;
		}
		
		public EsScriptBuilder subtract(){
			script += "-";
			return this;
		}
		
		public EsScriptBuilder multiply(){
			script += "*";
			return this;
		}
		
		public EsScriptBuilder divide(){
			script += "/";
			return this;
		}
		
		public EsScriptBuilder incr(){
			script += "++";
			return this;
		}
		
		public EsScriptBuilder decr(){
			script += "--";
			return this;
		}
		
		public EsScriptBuilder script(String script){
			this.script += script;
			return this;
		}
		
		private EsScriptBuilder _base(Object val, String flag){
			if(StringUtils.isEmpty(val)){
				throw new IllegalArgumentException("date is null");
			}
			try {
				if(val instanceof Date){
					val = ((Date) val).getTime();
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("date rang error: " + e.getMessage());
			}
			
			this.paramName = "param_" + AI.getAndIncrement();
			params.put(paramName, val);
			
			script += flag + "params." + this.paramName;
			return this;
		}
		
		public EsScriptBuilder boost(float boost){
			this.boost = boost;
			return this;
		}
		
		public EsScript build(){
			dateRang.setParams(params);
			dateRang.setBoost(boost);
			dateRang.setScript(script);
			
			Log.info("\n|---------- ES script -------------------------------------" + 
					"\n| [script]\n|  {}\n| [params]\n|  {}\n| [boost]\n|  " + boost +
					"\n|---------- /ES script ----------------------------------",
					script.replaceAll(";", ";\n|  "),
					params.toString().replaceAll(",", ",\n|  ")
					.replace("{", "{\n|   ").replace("}", "\n|  }"));
			return dateRang;
		}
	} 
	
	public static void main(String[] args) {
		EsScript dr = EsScript.where()
			.doc("title").is("你好").end()
			.doc("updateTime").val().millis().gt("2019-07-19 12:29:10 000").end()
			.doc("createTime").gt("2019-01-01 12:22:12 001").end()
			.doc("createTime").lt(new Date(1544108941002L)).build();
		
		EsConditions c = EsConditions.AND.script(dr);
		System.out.println(c);
	}
}
