package com.liuzi.util.common;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @Title:        DateUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:29:10
 * 
 * @version       1.0
 * 
 */
public class DateUtil {
	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	public static String getDate(String interval, Date starttime, String pattern){
	    Calendar temp = Calendar.getInstance(TimeZone.getDefault());
	    temp.setTime(starttime);
	    temp.add(2, Integer.parseInt(interval));
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	    return sdf.format(temp.getTime());
	}

	public static Date str2Date(String str){
	    Date d = null;
	    try {
	    	sdf.applyPattern(DEFAULT_PATTERN);
	    	d = sdf.parse(str);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return d;
	}

	public static Date str2Date(String str, String pattern) {
	    Date d = null;
	    try {
	    	sdf.applyPattern(pattern);
	    	d = sdf.parse(str);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return d;
	}
	
	public static Long str2DateLong(String str){
	    Date date = str2Date(str);
	    Calendar temp = Calendar.getInstance();
	    temp.setTime(date);
	    return Long.valueOf(temp.getTimeInMillis());
	}

	public static Date pattern(Date date){
	    try {
	    	sdf.applyPattern(DEFAULT_PATTERN);
	    	String dd = sdf.format(date);
	    	date = str2Date(dd);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return date;
	}

	public static Date pattern(Date date, String pattern){
	    try {
	    	sdf.applyPattern(pattern);
	    	String dd = sdf.format(date);
	    	date = str2Date(dd, pattern);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return date;
	}

	public static String date2Str(Date date) {
	    if (date == null) return "";
	    
	    sdf.applyPattern(DEFAULT_PATTERN);
	    return sdf.format(date);
	}

	public static String date2Str(Date date, String format) {
		sdf.applyPattern(format);
	    return sdf.format(date);
	}

	public static Date getLastDate(Date date){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    calendar.add(5, -1);

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date getNextDate(Date date){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    calendar.add(5, 1);

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date getNextMonthDate(Date date){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    calendar.add(2, 1);

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date getBeforeDate(Date date, int dates){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    calendar.add(5, -dates);

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date getLastWeekStart(Date date){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    int i = calendar.get(7) - 1;
	    int startnum = 0;
	    if (i == 0)
	      startnum = 13;
	    else {
	      startnum = 7 + i - 1;
	    }
	    calendar.add(5, -startnum);

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date getLastWeekEnd(Date date){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    calendar.setTime(date);
	    int i = calendar.get(7) - 1;
	    int endnum = 0;
	    if (i == 0)
	      endnum = 7;
	    else {
	      endnum = i;
	    }
	    calendar.add(5, -(endnum - 1));

	    return str2Date(date2Str(calendar.getTime()));
	}

	public static Date changeDate(String type, int value){
	    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    if (type.equals("month"))
	      calendar.set(2, calendar.get(2) + value);
	    else if (type.equals("date")) {
	      calendar.set(5, calendar.get(5) + value);
	    }
	    return calendar.getTime();
	}

	public static Date changeDate(Date date, String type, int value){
	    if (date != null)
	    {
	      Calendar calendar = new GregorianCalendar();
	      calendar.setTime(date);

	      if (type.equals("month"))
	        calendar.set(2, calendar.get(2) + value);
	      else if (type.equals("date"))
	        calendar.set(5, calendar.get(5) + value);
	      else if (type.endsWith("year")) {
	        calendar.set(1, calendar.get(1) + value);
	      }
	      return calendar.getTime();
	    }
	    return null;
	}

	public static boolean checkTime(String time1, String time2){
	    Calendar calendar = Calendar.getInstance();
	    Date date1 = calendar.getTime();
	    Date date11 = str2Date(date2Str(date1, "yyyy-MM-dd") + " " + time1);

	    Calendar c = Calendar.getInstance();
	    c.add(5, 1);
	    Date date2 = c.getTime();
	    Date date22 = str2Date(date2Str(date2, "yyyy-MM-dd") + " " + time2);

	    Calendar scalendar = Calendar.getInstance();
	    scalendar.setTime(date11);

	    Calendar ecalendar = Calendar.getInstance();
	    ecalendar.setTime(date22);

	    Calendar calendarnow = Calendar.getInstance();

	    return ((calendarnow.after(scalendar)) && (calendarnow.before(ecalendar)));
	}

	public static boolean checkTime(Date date11, Date date22){
	    Calendar scalendar = Calendar.getInstance();
	    scalendar.setTime(date11);

	    Calendar ecalendar = Calendar.getInstance();
	    ecalendar.setTime(date22);

	    Calendar calendarnow = Calendar.getInstance();

	    return ((calendarnow.after(scalendar)) && (calendarnow.before(ecalendar)));
	}

	public static boolean checkDate(String date1, String date2){
	    Date date11 = str2Date(date1);

	    Date date22 = str2Date(date2);

	    Calendar scalendar = Calendar.getInstance();
	    scalendar.setTime(date11);

	    Calendar ecalendar = Calendar.getInstance();
	    ecalendar.setTime(date22);

	    Calendar calendarnow = Calendar.getInstance();

	    /*System.out.println(date11.toString());
	    System.out.println(date22.toString());
	    System.out.println(scalendar.toString());
	    System.out.println(ecalendar.toString());
	    System.out.println(calendarnow.toString());*/

	    return ((calendarnow.after(scalendar)) && (calendarnow.before(ecalendar)));
	}

	public static Date getIntervalDate(String interval, Date starttime, String pattern){
	    Calendar temp = Calendar.getInstance();
	    temp.setTime(starttime);
	    temp.add(5, Integer.parseInt(interval));
	    sdf.applyPattern(pattern);
	    String shijian = sdf.format(temp.getTime());
	    return str2Date(shijian);
	}

	public static Date formatDate(Date date) {
	    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    System.out.println(bartDateFormat.format(date));
	    SimpleDateFormat bartDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	    Date date1 = null;
	    try {
	      date1 = bartDateFormat1.parse(bartDateFormat.format(date));
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    System.out.println(date.getTime());
	    return date1;
	}

	public static String dateFeedShow(Date d){
	    long l = System.currentTimeMillis() - d.getTime();

	    l /= 1000L;
	    if (l < 60L)
	      return "刚刚";
	    if (l < 3600L) {
	      return (l / 60L) + "分钟";
	    }
	    long hour = l / 3600L;
	    if (hour < 24L) {
	      return hour + "小时前";
	    }

	    long day = hour / 24L;
	    if (day < 30L)
	      return day + "天前";
	    if (day < 90L)
	      return "一个月前";
	    if (day < 100L) {
	      return "三个月前";
	    }
	    return "亲，太久了";
	}

	  public static void main(String[] arf){
	    Date date = new Date();
	    System.out.println(date2Str(date, "yyyy-MM-dd"));
	    System.out.println(date2Str(date));
	  }
}
