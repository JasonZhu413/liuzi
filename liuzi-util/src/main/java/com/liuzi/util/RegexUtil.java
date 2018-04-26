package com.liuzi.util;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title:        RegexUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:35:17
 * 
 * @version       1.0
 * 
 */
public class RegexUtil {
	
	/**
	 * 判断是否为email
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str){
	    String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为ip地址
	 * @param str
	 * @return
	 */
	public static boolean isIP(String str){
	    String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
	    String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为请求地址
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str){
	    String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	    return match(regex, str);
	}

	/**
	 * 判断是否为电话号码（座机）
	 * @param str
	 * @return
	 */
	public static boolean isTelephone(String str){
	    String regex = "^(\\d{3,4}-)?\\d{6,8}$";

	    return match(regex, str);
	}

	/**
	 * 判断是否为密码
	 * @param str
	 * @return
	 */
	public static boolean isPassword(String str){
	    String regex = "[A-Za-z]+[0-9]";
	    return match(regex, str);
	}

	/**
	 * 判断是否为6-18位的数字
	 * @param str
	 * @return
	 */
	public static boolean isPasswLength(String str){
	    String regex = "^\\d{6,18}$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为邮政编码（6位数字）
	 * @param str
	 * @return
	 */
	public static boolean isPostalcode(String str){
	    String regex = "^\\d{6}$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为手机号码
	 * @param str
	 * @return
	 */
	public static boolean isMobileNo(String str){
	    String regex = "^((13[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为手机号码
	 * @param str
	 * @return
	 */
	public static boolean isMobileNoOther(String str) {
	    String regex = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为身份证
	 * @param str
	 * @return
	 */
	public static boolean isIDcard(String str){
	    String regex = "(^\\d{18}$)|(^\\d{15}$)";
	    return match(regex, str);
	}

	/**
	 * 判断是否为小数
	 * @param str
	 * @return
	 */
	public static boolean isDecimal(String str){
	    String regex = "^[0-9]+(.[0-9]{2})?$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为月份
	 * @param str
	 * @return
	 */
	public static boolean isMonth(String str){
	    String regex = "^(0?[[1-9]|1[0-2])$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为日期（日）
	 * @param str
	 * @return
	 */
	public static boolean isDay(String str){
	    String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为日期
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str){
	    String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|"
	    		+ "(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|"
	    		+ "(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)"
	    		+ "(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))"
	    		+ " (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
	    String regex = "^[0-9]*$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为正整数
	 * @param str
	 * @return
	 */
	public static boolean isIntNumber(String str){
	    String regex = "^\\+?[1-9][0-9]*$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为大写字母
	 * @param str
	 * @return
	 */
	public static boolean isUpChar(String str){
	    String regex = "^[A-Z]+$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为小写字母
	 * @param str
	 * @return
	 */
	public static boolean isLowChar(String str){
	    String regex = "^[a-z]+$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为字母
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str){
	    String regex = "^[A-Za-z]+$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
	    String regex = "^[一-龥],{0,}$";
	    return match(regex, str);
	}

	/**
	 * 判断是否为特殊字符
	 * @param qString
	 * @param regx
	 * @return
	 */
	public static boolean checkString(String qString){
	    String regx = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";
	    return hasCrossScriptRisk(qString, regx);
	}
	
	/**
	 * 获取字符串长度
	 * @param str
	 * @return
	 */
	public static int getStrLength(String str){
	    if (StringUtil.empty(str)) {
	    	return 0;
	    }
	    str = str.replaceAll("[^\\x00-\\xff]", "**");
	    return str.length();
	}

	/**
	 * 判断是否大于8个字符（不确定）
	 * @param str
	 * @return
	 */
	public static boolean isLength(String str){
	    String regex = "^.{8,}$";
	    return match(regex, str);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkHtmlTag(String str){
	    String regex = "^[a-zA-Z0-9],{0,}$";
	    return match(regex, str);
	}

	
	public static boolean isYeah(String str){
	    String regx = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
	    return match(regx, str);
	}
	
	private static boolean match(String regex, String str){
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	}
	
	public static boolean hasCrossScriptRisk(String qString, String regx){
		if (qString != null) {
	    	qString = qString.trim();
	    	Pattern p = Pattern.compile(regx, 2);
	    	Matcher m = p.matcher(qString);
	    	return m.find();
	    }
	    return false;
	}
	
	public static void main(String[] args){
	    String str = "18123222333";
	    System.out.println(isMobileNo(str));
	    
	}

}
