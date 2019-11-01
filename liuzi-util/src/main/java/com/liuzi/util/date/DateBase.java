package com.liuzi.util.date;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;

import com.liuzi.util.common.Log;

/**
 * @Title:        DateUtil
 * @Description:  TODO
 * @author        ZhuShiyao
 * @Date          2017年3月30日 下午11:29:10
 * @version       1.0
 * 
 */
public class DateBase {
	
	/**
	 * 字符转date
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static Date stringToDate(String time, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			Log.error(e, "String to Date error");
		}
		return null;
	}
	
	/**
	 * date转字符
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date time, String pattern){
		return new SimpleDateFormat(pattern).format(time);
	}
	
	/**
	 * 字符转localDateTime
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime stringToLocalDateTime(String time, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(time, formatter);
	}
	
	/**
	 * localDateTime转字符
	 * @param localDateTime
	 * @param pattern
	 * @return
	 */
	public static String localDateTimeToString(LocalDateTime localDateTime, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return formatter.format(localDateTime);
	}
    
	/**
	 * localDateTime转date
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime){
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}
	
    /**
     * date转localDateTime
     * @param date
     * @return
     */
	public static LocalDateTime dateToLocalDateTime(Date date){
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, zone);
	}
	/**
	 * localDateTime转时间戳
	 * @param localDateTime
	 * @return
	 */
	public static long localDateTimeToTimestamp(LocalDateTime localDateTime){
		ZoneId zone = ZoneId.systemDefault();
	    Instant instant = localDateTime.atZone(zone).toInstant();
	    return instant.toEpochMilli();
	}
	/**
	 * 时间戳转localDateTime
	 * @param timestamp
	 * @return
	 */
	public static LocalDateTime timestampToLocalDateTime(long timestamp) {
	    Instant instant = Instant.ofEpochMilli(timestamp);
	    ZoneId zone = ZoneId.systemDefault();
	    return LocalDateTime.ofInstant(instant, zone);
	}
	/**
	 * 日期格式化
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date pattern(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	    String formatDate = sdf.format(date);
	    try {
			return sdf.parse(formatDate);
		} catch (ParseException e) {
			Log.error(e, "Date pattern error");
		}
	    return null;
	}
	/**
	 * 日期格式化
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String pattern(String date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	    try {
			Date formatDate = sdf.parse(date);
			return sdf.format(formatDate);
		} catch (ParseException e) {
			Log.error(e, "Date pattern error");
		}
	    return null;
	}
	/**
	 * 日期格式化
	 * @param localDateTime
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime pattern(LocalDateTime localDateTime, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String time = formatter.format(localDateTime);
		return LocalDateTime.parse(time, formatter);
	}
	/**
	 * 字符串转LocalDate
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static LocalDate stringToLocalDate(String date, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(date, formatter);
	}
	/**
	 * LocalDate转字符串
	 * @param localDate
	 * @param pattern
	 * @return
	 */
	public static String localDateToString(LocalDate localDate, String pattern){
		DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
		return localDate.format(df);
	}
	
	/**
	 * 获取当前时间
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.now().format(formatter);
	}
	/**
     * 获取本月第一天
     * @return
     */
    public static LocalDate getFirstDayOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }
    /**
     * 获取月份第一天
     * @param date
     * @param pattern
     * @return
     */
    public static LocalDate getFirstDayOfMonth(String date, String pattern) {
    	LocalDate localDate = stringToLocalDate(date, pattern);
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }
    /**
     * 获取本月最后一天
     */
    public static LocalDate getLastDayOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }
    /**
     * 获取月份最后一天
     * @param date
     * @param pattern
     * @return
     */
    public static LocalDate getLastDayOfMonth(String date, String pattern) {
    	LocalDate localDate = stringToLocalDate(date, pattern);
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }
    /**
	 * 获得一年后的日期格式字符串
	 */
	public static String getOneYearLaterDate(String date, String pattern){
		LocalDate fromDate = stringToLocalDate(date, pattern);
		LocalDate toDate = fromDate.plus(1, ChronoUnit.YEARS);
		return localDateToString(toDate, pattern);
	}
	
	/**
	 * 根据出生日期字符串计算年龄
	 * @param birth
	 * @param pattern
	 * @return
	 */
	public static int getAgeByBirth(String birth, String pattern){
		LocalDate birthDate = stringToLocalDate(birth, pattern);
		LocalDate currentDate = LocalDate.now();
		if(birthDate != null){
			//判断birthDay是否合法
			if(currentDate.isBefore(birthDate)){
				return -1;
			}
			return birthDate.until(currentDate).getYears();
		}
		return -1;
	}
	/**
	 * 获取上一天
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getPrevDate(String date, String pattern){
		return plusDate(date, pattern, DatePlus.Day, -1);
	}
	/**
	 * 获取下一天
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getNextDate(String date, String pattern){
		return plusDate(date, pattern, DatePlus.Day, 1);
	}
	/**
	 * 获取上一个月的这一天
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getPrevMonthDate(String date, String pattern){
		return plusDate(date, pattern, DatePlus.Month, -1);
	}
	/**
	 * 获取下一个月的这一天
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getNextMonthDate(String date, String pattern){
		return plusDate(date, pattern, DatePlus.Month, 1);
	}
	/**
	 * 日期操作
	 * @param date
	 * @param pattern
	 * @param pattern
	 * @param plus 增减日期数量
	 * @return
	 */
	public static String plusDate(String date, String pattern, DatePlus datePlus, int plus){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
		LocalDateTime nextDateTime = localDateTime;
		switch(datePlus){
			case Year:
				nextDateTime = localDateTime.plusYears(plus);
				break;
			case Month:
				nextDateTime = localDateTime.plusMonths(plus);
				break;
			case Week:
				nextDateTime = localDateTime.plusWeeks(plus);
				break;
			case Day:
				nextDateTime = localDateTime.plusDays(plus);
				break;
			case Hour:
				nextDateTime = localDateTime.plusHours(plus);
				break;
			case Minute:
				nextDateTime = localDateTime.plusMinutes(plus);
				break;
			case Second:
				nextDateTime = localDateTime.plusSeconds(plus);
				break;
			default:
				nextDateTime = localDateTime;
				break;
		}
		return nextDateTime.format(formatter);
	}
	/**
	 * 今年第几周
	 * @return
	 */
	public static int weekIsNumOfYear(String date, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDate = LocalDate.parse(date, formatter);
		WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 7);
	    return localDate.get(weekFields.weekOfYear());
	}
	/**
	 * 相差几天
	 * @return
	 */
	public static long differOfDays(String dateStart, String dateEnd, String pattern){
		/*DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDateStart = LocalDate.parse(dateStart, formatter);
		LocalDate localDateEnd = LocalDate.parse(dateEnd, formatter);
		return localDateEnd.toEpochDay() - localDateStart.toEpochDay();*/
		return differ(dateStart, dateEnd, pattern, ChronoUnit.DAYS);
	}
	/**
	 * 相差几周
	 * @return
	 */
	public static long differOfWeeks(String dateStart, String dateEnd, String pattern){
		return differ(dateStart, dateEnd, pattern, ChronoUnit.WEEKS);
	}
	/**
	 * 相差几个月
	 * @return
	 */
	public static long differOfMonths(String dateStart, String dateEnd, String pattern){
		return differ(dateStart, dateEnd, pattern, ChronoUnit.MONTHS);
	}
	/**
	 * 相差几年
	 * @return
	 */
	public static long differOfYears(String dateStart, String dateEnd, String pattern){
		return differ(dateStart, dateEnd, pattern, ChronoUnit.YEARS);
	}
	/**
	 * 相差多少，自定义
	 * @return
	 */
	public static long differ(String dateStart, String dateEnd, String pattern, ChronoUnit unit){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDateStart = LocalDate.parse(dateStart, formatter);
		LocalDate localDateEnd = LocalDate.parse(dateEnd, formatter);
		return localDateEnd.until(localDateStart, unit);
	}
	/**
	 * 时间修饰
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateDecorate(String date, String pattern){
		Date begin = stringToDate(date, pattern);
		return dateDecorate(begin, pattern);
	}
	/**
	 * 时间修饰
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateDecorate(Date date, String pattern){
		long now = System.currentTimeMillis();
	    long deffer = now - date.getTime();

	    deffer /= 1000L;
	    if (deffer < 60L){
	    	return "刚刚";
	    }
	    
	    if (deffer < 3600L) {
	    	return (deffer / 60L) + "分钟前";
	    }
	    
	    long hour = deffer / 3600L;
	    if (hour < 24L) {
	    	return hour + "小时前";
	    }

	    long day = hour / 24L;
	    if (day < 30L){
	    	return day + "天前";
	    }
	    	
	    if (day < 90L){
	    	return "一个月前";
	    }
	    	
	    if (day < 100L) {
	      return "三个月前";
	    }
	    
	    return dateToString(date, pattern);
	}

	public static void main(String[] arf){
		
	    System.out.println(DateBase.pattern("10-21 2722", "MM-dd mmss"));
	}
	
	
}
