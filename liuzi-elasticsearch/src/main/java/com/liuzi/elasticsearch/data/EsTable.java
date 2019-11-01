package com.liuzi.elasticsearch.data;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateFormat;
import com.liuzi.util.date.DateUtil;

import lombok.Data;


/**
 * 索引
 * @author zsy
 */
@Data
public class EsTable {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	private String table;
	private String date;
	
	public EsTable(String table){
		this.table = table;
	}
	public EsTable(String table, String date){
		this.table = table;
		this.date = date;
	}
	
	public static EsTable[] create(String table, String... date){
		int length = date.length;
		EsTable[] esTable = new EsTable[length];
		for(int i = 0; i < length; i ++){
			esTable[i] = new EsTable(table, date[i]);
		}
		return esTable;
	}
	
	public static EsTable[] create(String table, Date... date){
		int length = date.length;
		EsTable[] esTable = new EsTable[length];
		for(int i = 0; i < length; i ++){
			esTable[i] = new EsTable(table, DateUtil.dateToString(date[i], DateFormat.yyyyMMdd));
		}
		return esTable;
	}
	
	public static EsTable[] create(String table, String dateStart, String dateEnd){
		List<String> dates = null;
		try {
			Date start = sdf.parse(dateStart);
			Date end = sdf.parse(dateEnd);
			
			Calendar cStart = Calendar.getInstance();
			cStart.setTime(start);
			Calendar cEnd = Calendar.getInstance();
			cEnd.setTime(end);

			dates = new ArrayList<>();
			while (cStart.before(cEnd) || cStart.equals(cEnd)) {
				dates.add(sdf.format(cStart.getTime()));
				cStart.add(Calendar.DAY_OF_YEAR, 1);
			}
			//倒叙
			//Collections.reverse(dates);
		} catch (ParseException e) {
			Log.error(e, "ParseException");
		}
		
		if(dates != null && !dates.isEmpty()){
			return create(table, dates.toArray(new String[dates.size()]));
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(EsTable.create("test", "20190711", "20190717"));
	}
}
