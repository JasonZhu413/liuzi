package com.liuzi.elasticsearch.old;


import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.springframework.util.StringUtils;


/**
 * 排序
 * @author zsy
 */
public class EsSort {
	
	//@GeoPointField
	
	public static SortBuilder<FieldSortBuilder> defaultSort(){
		return SortBuilders.fieldSort("id").order(SortOrder.DESC);
	}
	
	/**
	 * 多字段排序
	 * return SortBuilder
	 */
	public static SortBuilder<?> field(String name, SortOrder sortOrder){
		if(StringUtils.isEmpty(name)){
			return defaultSort();
		}
		sortOrder = sortOrder == null ? SortOrder.ASC : sortOrder;
		return SortBuilders.fieldSort(name).order(sortOrder);
	}
	
	/**
	 * 地理位置排序
	 * return SortBuilder
	 */
	public static SortBuilder<?> geo(String position, GeoPoint... geoPoints){
		return geo(position, null, null, geoPoints);
	}
	
	public static SortBuilder<?> geo(String position, DistanceUnit unit, GeoPoint... geoPoints){
		return geo(position, unit, null, geoPoints);
	}
	
	public static SortBuilder<?> geo(String position, SortOrder sortOrder, GeoPoint... geoPoints){
		return geo(position, null, sortOrder, geoPoints);
	}
	
	public static SortBuilder<?> geo(String position, double lat, double lon){
		return geo(position, null, null, lat, lon);
	}
	
	public static SortBuilder<?> geo(String position, DistanceUnit unit, 
			double lat, double lon){
		return geo(position, unit, null, lat, lon);
	}
	
	public static SortBuilder<?> geo(String position, SortOrder sortOrder, 
			double lat, double lon){
		return geo(position, null, sortOrder, lat, lon);
	}
	
	public static SortBuilder<?> geo(String position, DistanceUnit unit, SortOrder sortOrder, 
			double lat, double lon){
		if(lat <= 0 || lon <= 0){
			return defaultSort();
		}
		return geo(position, unit, sortOrder, new GeoPoint(lat, lon));
	}
	
	public static SortBuilder<?> geo(String position, DistanceUnit unit, SortOrder sortOrder, 
			GeoPoint... geoPoints){
		
		if(geoPoints == null || geoPoints.length == 0){
			return defaultSort();
		}
		
		unit = unit == null ? DistanceUnit.METERS : unit;
		sortOrder = sortOrder == null ? SortOrder.ASC : sortOrder;
		
		return SortBuilders.geoDistanceSort(position, geoPoints)
				.unit(unit)
				.order(sortOrder);
	}
	
	/**
	 * 分数排序
	 * @return SortBuilder
	 */
	public static SortBuilder<?> score(){
		return score(null);
	}
	
	/**
	 * 分数排序
	 * @param sortOrder
	 * @return SortBuilder
	 */
	public static SortBuilder<?> score(SortOrder sortOrder){
		sortOrder = sortOrder == null ? SortOrder.ASC : sortOrder;
		return SortBuilders.scoreSort().order(sortOrder);
	}
	
	/**
	 * 自定义脚本排序
	 * @param script
	 * @param scriptSortType
	 * @return
	 */
	public static SortBuilder<?> getBuilder(Script script, ScriptSortType scriptSortType){
		return script(script, scriptSortType, null);
	}
	
	/**
	 * 自定义脚本排序
	 * @param script
	 * @param scriptSortType
	 * @param sortOrder
	 * @return
	 */
	public static SortBuilder<?> script(Script script, ScriptSortType scriptSortType, SortOrder sortOrder){
		if(script == null){
			return defaultSort();
		}
		sortOrder = sortOrder == null ? SortOrder.ASC : sortOrder;
		return SortBuilders.scriptSort(script, scriptSortType).order(sortOrder);
	}
	
}
