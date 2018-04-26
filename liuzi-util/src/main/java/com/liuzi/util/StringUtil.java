package com.liuzi.util;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * @Title:        StringUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午10:03:51
 * 
 * @version       1.0
 * 
 */
public class StringUtil{
	private static Random randGen = null;
	private static Object initLock = new Object();
	private static char[] numbersAndLetters = null;

  	public static void main(String[] args){
  		System.out.println(getDistance(52.438887999999999D, 143.35484600000001D, 52.438878000000003D, 143.35483600000001D));
  	}

  	/**
  	 * 获取字符长度（中文+2）
  	 * @param value
  	 * @return
  	 */
	public static int getByteLength(String value){
	    int valueLength = 0;
	    String chinese = "[Α-￥]";
	
	    for (int i = 0; i < value.length(); ++i){
	    	String temp = value.substring(i, i + 1);
	
	    	if (temp.matches(chinese)){
	    		valueLength += 2;
	    	}else {
	    		++valueLength;
	    	}
	    }
	    
	    return valueLength;
	}

	/**
	 * 
	 * @param content
	 * @param len
	 * @return
	 */
	public static String[] split(String content, int len){
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		
		int len2 = content.length();
		
		if (len2 <= len) {
			return new String[] { content };
		}
		
		int i = len2 / len + 1;
		
		System.out.println("i:" + i);
		
		String[] strA = new String[i];
		int j = 0;
		int begin = 0;
		int end = 0;
		while (j < i) {
			begin = j * len;
			end = (j + 1) * len;
			if (end > len2)
				end = len2;
			strA[j] = content.substring(begin, end);

			++j;
		}
		return strA;
	}

	/**
	 * 部分匹配是否为email
	 * @param email
	 * @return
	 */
	public static boolean emailFormat(String email){
	    String patt = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    Pattern pattern = Pattern.compile(patt);
	    Matcher mat = pattern.matcher(email);
	    return mat.find();
	}

	/**
	 * 全部匹配是否为email
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
	    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    Pattern regex = Pattern.compile(check);
	    Matcher matcher = regex.matcher(email);
	    return matcher.matches();
	}

	/**
	 * 判断是否为字母或数字
	 * @param s
	 * @return
	 */
	public static boolean isLetterorDigit(String s){
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		
	    for (int i = 0; i < s.length(); ++i) {
	    	if (!(Character.isLetterOrDigit(s.charAt(i)))){
	    		return false;
	    	}
	    }

	    return true;
	}

	/**
	 * 返回小于256字符
	 * @param str
	 * @return
	 */
	public static String checkStr(String str){
		if (str == null){
			return "";
		}
		
		if (str.length() > 256){
			return str.substring(256);
		}
		
		return str;
	}

	/**
	 * 验证字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean validateInt(String str){
	    if ((str == null) || (str.trim().equals(""))) {
	    	return false;
	    }

	    int i = 0; 
	    
	    for (int l = str.length(); i < l; ++i) {
	    	char c = str.charAt(i);
	    	if ((c < '0') || (c > '9')) {
	    		return false;
	    	}
	    }
	    
	    return true;
	}

	/**
	 * 二进制转字符串
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b){
	    String hs = "";
	    String stmp = "";
	    for (int n = 0; n < b.length; ++n) {
	    	stmp = Integer.toHexString(b[n] & 0xFF);
	    	if (stmp.length() == 1){
	    		hs = hs + "0" + stmp;
	    	}else{
	    		hs = hs + stmp;
	    	}
	    	if (n < b.length - 1){
	    		hs = hs + ":";  
	    	}
	    }
	    return hs.toUpperCase();
	}

	/**
	 * 二进制转字符串
	 * @param b
	 * @return
	 */
	public static String byte2string(byte[] b){
	    String hs = "";
	    String stmp = "";
	    for (int n = 0; n < b.length; ++n) {
	    	stmp = Integer.toHexString(b[n] & 0xFF);
	      	if (stmp.length() == 1){
	      		hs = hs + "0" + stmp;
	    	}else {
	    		hs = hs + stmp;
	    	}
	    }
	    return hs.toUpperCase();
	}

	/**
	 * 字符串转二进制
	 * @param hs
	 * @return
	 */
	public static byte[] string2byte(String hs) {
		byte[] b = new byte[hs.length() / 2];
		for (int i = 0; i < hs.length(); i += 2) {
			String sub = hs.substring(i, i + 2);
			byte bb = new Integer(Integer.parseInt(sub, 16)).byteValue();
			b[(i / 2)] = bb;
		}
		return b;
	}

	/**
	 * 判断是否为空
	 * @param param
	 * @return
	 */
	public static boolean empty(String param){
		return ((param == null) || (param.trim().length() < 1));
	}

	/**
	 * 判断字符串是否为数字或字母
	 * @param str
	 * @return
	 */
	public static boolean isLetterOrDigit(String str){
	    Pattern p = null;
	    Matcher m = null;
	    boolean value = true;
	    try {
	    	p = Pattern.compile("[^0-9A-Za-z]");
	    	m = p.matcher(str);
	    	if (m.find()){
	    		value = false;
	    	}
	    } catch (Exception localException) {
	    	
	    }
	    return value;
	}

	/**
	 * 判断字符串是否为小写
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isLowerLetter(String str){
	    Pattern p = null;
	    Matcher m = null;
	    boolean value = true;
	    try {
	    	p = Pattern.compile("[^a-z]");
	    	m = p.matcher(str);
	    	if (m.find()){
	    		value = false;
	    	}
	    }
	    catch (Exception localException) {
	    }
	    return value;
	}

	/**
	 * 转码
	 * @param str
	 * @param code
	 * @return
	 */
	public static String encode(String str, String code) {
	    try {
	    	return URLEncoder.encode(str, code);
	    } catch (Exception ex) {
	    	ex.fillInStackTrace(); 
	    }
	    return "";
	}

	/**
	 * 转码
	 * @param str
	 * @param code
	 * @return
	 */
	public static String decode(String str, String code){
		try {
			return URLDecoder.decode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace(); 
		}
		return "";
	}

	/**
	 * 字符串去空格（如果为null则返回空字符串）
	 * @param param
	 * @return
	 */
	public static String nvl(String param){
		return ((param != null) ? param.trim() : "");
	}

	/**
	 * 字符串转int 如果错误 返回0
	 * @param param
	 * @return
	 */
	public static int parseInt(String param) {
		int i = 0;
	    try {
	    	i = Integer.parseInt(param);
	    } catch (Exception localException) {
	    	
	    }
	    return i;
	}

	/**
	 * 字符串转long 如果错误 返回0
	 * @param param
	 * @return
	 */
	public static long parseLong(String param) {
	    long l = 0L;
	    try {
	    	l = Long.parseLong(param);
	    } catch (Exception localException) {
	    	
	    }
	    return l;
	}

	/**
	 * 字符串转double
	 * @param param
	 * @return
	 */
	public static double parseDouble(String param) {
		double t = 0.0D;
	    try {
	    	t = Double.parseDouble(param.trim());
	    } catch (Exception e) {
	    	
	    }
	    return t;
	}

	/**
	 * 字符串 1,t,y,T,Y 转换boolean
	 * @param param
	 * @return
	 */
	public static boolean parseBoolean(String param) {
		if (empty(param))
			return false;
		
		switch (param.charAt(0)){
		    case '1':
		    case 'T':
		    case 'Y':
		    case 't':
		    case 'y':
		    return true;
		}

		return false;
	}

	/**
	 * 字符串截取（str按delims截取） 返回数组
	 * @param str
	 * @param delims
	 * @return
	 */
	public static final String[] split(String str, String delims){
		
	    StringTokenizer st = new StringTokenizer(str, delims);
	    
	    ArrayList<String> list = new ArrayList<String>();
	    
	    String token = st.nextToken();
	    
	    for (; st.hasMoreTokens(); list.add(token));
	    
	    return ((String[]) list.toArray(new String[list.size()]));
	}
	
	
	/**
	 * 从0开始截取到length位置 大于length 返回 str...
	 * @param str
	 * @param length
	 * @return
	 */
	public static String substring(String str, int length){
		if (str == null) {
			return null;
		}
		if (str.length() > length) {
			return str.substring(0, length - 2) + "...";
		}
		return str;
	}

	/**
	 * 验证是否为double
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static boolean validateDouble(String str) throws RuntimeException {
	    if (str == null){
	    	return false;
	    }
	    int k = 0;
	    int i = 0; 
	    for (int l = str.length(); i < l; ++i) {
	    	char c = str.charAt(i);
	    	boolean flag = ((c >= '0') && (c <= '9')) || ((i == 0) && (((c == '-') || (c == '+'))));
	    	if (flag){
	    		continue;
	    	}
	    	
	    	flag = (c != '.') || (i >= l - 1) || (k >= 1);
	    	if (flag){
		        return false;
	    	}
	    	
		    ++k;
	    }
	    return true;
	}

	/**
	 * 判断是否为电话号
	 * @param str
	 * @param includeUnicom
	 * @return
	 */
	public static boolean validateMobile(String str, boolean includeUnicom) {
	    if (empty(str)){
	    	return false;
	    }
	    
	    str = str.trim();

	    if ((str.length() != 11) || (!(validateInt(str)))) {
	    	return false;
	    }
	    
	    boolean flag = (includeUnicom) && (((str.startsWith("130")) || (str.startsWith("133"))
	    		|| (str.startsWith("131"))));
	    if (flag) {
	    	return true;
	    }

	    return ((str.startsWith("139")) || (str.startsWith("138")) || (str.startsWith("137")) || 
	    		(str.startsWith("136")) || (str.startsWith("135")));
	}

	/**
	 * 判断是否为电话号
	 * @param str
	 * @return
	 */
	public static boolean validateMobile(String str){
		return validateMobile(str, false);
	}

	/**
	 * gb2312转ios
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String gbToIso(String s) throws UnsupportedEncodingException {
		return covertCode(s, "GB2312", "ISO8859-1");
	}

	/**
	 * 字符转化（字符）
	 * @param s
	 * @param code1
	 * @param code2
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String covertCode(String s, String code1, String code2) throws UnsupportedEncodingException{
		if (s == null){
			return null;
		}
		
		if (s.trim().equals("")) {
			return "";
		}
		
		return new String(s.getBytes(code1), code2);
	}

	/**
	 * 字符转换 Bytes("GBK"), "ISO8859-1"
	 * @param s0
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String transCode(String s0) throws UnsupportedEncodingException {
		if (empty(s0)) {
			return null;
		}
		
		s0 = s0.trim();
		
		return new String(s0.getBytes("GBK"), "ISO8859-1");
	}

	/**
	 * GBToUTF8
	 * @param s
	 * @return
	 */
	public static String GBToUTF8(String s){
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");
			for (int i = 2; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[(i + 1)] & 0xFF);
				for (int j = str.length(); j < 2; ++j) {
					out.append("0");
				}
				out.append(str);
				String str1 = Integer.toHexString(bytes[i] & 0xFF);
				for (int j = str1.length(); j < 2; ++j) {
					out.append("0");
				}

				out.append(str1);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); 
		}
		return null;
	}

	/**
	 * 数组替换
	 * @param obj
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String[] replaceAll(String[] obj, String oldString, String newString){
		if (obj == null) {
			return null;
		}
		int length = obj.length;
		String[] returnStr = new String[length];
		
		for (int i = 0; i < length; ++i) {
			returnStr[i] = replaceAll(obj[i], oldString, newString);
		}
		return returnStr;
	}

	/**
	 * 字符串替换
	 * @param s0
	 * @param oldstr
	 * @param newstr
	 * @return
	 */
	public static String replaceAll(String s0, String oldstr, String newstr) {
		if (empty(s0)){
			return null;
		}
		StringBuffer sb = new StringBuffer(s0);
		int begin = 0;

		begin = s0.indexOf(oldstr);
		while (begin > -1) {
			sb = sb.replace(begin, begin + oldstr.length(), newstr);
			s0 = sb.toString();
			begin = s0.indexOf(oldstr, begin + newstr.length());
		}
		return s0;
	}

	/**
	 * 字符串转html
	 * @param str
	 * @return
	 */
	public static String toHtml(String str) {
	    String html = str;
	    
	    if ((str == null) || (str.length() == 0)) {
	    	return str;
	    }
	    
	    html = replaceAll(html, "&", "&amp;");
	    html = replaceAll(html, "<", "&lt;");
	    html = replaceAll(html, ">", "&gt;");
	    html = replaceAll(html, "\r\n", "\n");
	    html = replaceAll(html, "\n", "<br>\n");
	    html = replaceAll(html, "\t", "         ");
	    html = replaceAll(html, " ", "&nbsp;");
	    
	    return html;
	}

	/**
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String replaceIgnoreCase(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String escapeHTMLTags(String input){
		if ((input == null) || (input.length() == 0)) {
			return input;
		}

		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); ++i) {
			ch = input.charAt(i);
			if (ch == '<')
				buf.append("&lt;");
			else if (ch == '>')
				buf.append("&gt;");
			else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * 指定长度随机字符串（0-9 a-z 0-9 A-Z）
	 * @param length
	 * @return
	 */
	public static final String randomString(int length){
		if (length < 1) {
			return null;
		}

		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();

					numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
							.toCharArray();
				}
			}
		}

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; ++i) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		
		return new String(randBuffer);
	}

	/**
	 * 指定长度随机字符串（0-9）
	 * @param length
	 * @return
	 */
	public static final String randomNum(int length) {
		if (length < 1) {
			return null;
		}

		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();

					numbersAndLetters = "0123456789".toCharArray();
				}
			}
		}

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; ++i) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}

  	/**
  	 * 
  	 * @param content
  	 * @param i
  	 * @return
  	 */
  	public static String getSubstring(String content, int i) {
  		int varsize = 10;
    	String strContent = content;
    	if (strContent.length() < varsize + 1) {
    		return strContent;
    	}
    	int max = (int) Math.ceil(strContent.length() / varsize);//Math.ceil(x) 返回大于x中最小的整数
    	if (i < max - 1) {
    		return strContent.substring(i * varsize, (i + 1) * varsize);
    	}
    	return strContent.substring(i * varsize);
  	}

  	/**
  	 * 获取当前时间（传入规则）
  	 * @param pattern
  	 * @return
  	 */
  	public static String now(String pattern){
  		return dateToString(new java.util.Date(), pattern);
  	}

  	/**
  	 * 获取时间
  	 * @param date
  	 * @param pattern
  	 * @return
  	 */
  	public static String dateToString(java.util.Date date, String pattern) {
  		if (date == null) {
  			return "";
  		}
  		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
  		return sdf.format(date);
  	}

  	/**
  	 * 当前时间yyyyMMddHHmmssSSS
  	 * @return
  	 */
  	public static synchronized String getNow(){
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  		return sdf.format(new java.util.Date());
  	}

  	/**
  	 * sql时间
  	 * @param strDate
  	 * @param pattern
  	 * @return
  	 * @throws ParseException
  	 */
  	public static java.sql.Date stringToDate(String strDate, String pattern) throws ParseException {
  		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
  		java.util.Date date = simpleDateFormat.parse(strDate);
  		long lngTime = date.getTime();
  		return new java.sql.Date(lngTime);
  	}

  	/**
  	 * 字符串转date
  	 * @param strDate
  	 * @param pattern
  	 * @return
  	 * @throws ParseException
  	 */
  	public static java.util.Date stringToUtilDate(String strDate, String pattern) throws ParseException{
  		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
  		return simpleDateFormat.parse(strDate);
  	}

  	/**
  	 * 项目路径
  	 * @param request
  	 * @return
  	 */
  	public static String root(HttpServletRequest request) {
  		return request.getContextPath();
  	}

  	/**
  	 * 转html
  	 * @param s
  	 * @return
  	 */
  	public static String formatHTMLOutput(String s) {
  		if (s == null) {
  			return null;
  		}
  		if (s.trim().equals("")) {
  			return "";
  		}

  		String formatStr = replaceAll(s, " ", "&nbsp;");
  		formatStr = replaceAll(formatStr, "\n", "<br />");

  		return formatStr;
  	}

  	/**
  	 * num四舍五入，保留x位小数
  	 * @param num
  	 * @param x
  	 * @return
  	 */
  	public static double myround(double num, int x){
  		BigDecimal b = new BigDecimal(num);
  		return b.setScale(x, BigDecimal.ROUND_HALF_UP).doubleValue();
  	}

  	/**
  	 * long转int
  	 * @param param
  	 * @return
  	 */
  	public static int parseLongInt(Long param) {
  		int i = 0;
  		try {
  			i = Integer.parseInt(String.valueOf(param));
  		} catch (Exception localException) {
  		}
  		return i;
  	}

  	/**
  	 * 返回字符串非null字符串
  	 * @param obj
  	 * @return
  	 */
  	public static String returnString(Object obj){
  		if (obj == null) {
  			return "";
  		}
  		return obj.toString();
  	}

  	/**
  	 * html字符替换
  	 * @param value
  	 * @return
  	 */
	public static String htmlEncode(String value){
		String[][] re = { { "<", "&lt;" }, 
				{ ">", "&gt;" }, 
				{ "\"", "&quot;" }, 
				{ "\\′", "&acute;" }, 
				{ "&", "&amp;" } };

		for (int i = 0; i < 4; ++i) {
			value = value.replaceAll(re[i][0], re[i][1]);
		}
		return value;
	}

  	/**
  	 * 判断是否为sql注入
  	 * @param str
  	 * @return
  	 */
  	public static boolean sql_inj(String str){
  		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
  		String[] inj_stra = inj_str.split("|");
  		
  		for (int i = 0; i < inj_stra.length; ++i){
  			if (str.indexOf(inj_stra[i]) >= 0){
  				return true;
  			}
  		}
  		return false;
  	}

  	private static double rad(double d){
  		return (d * 3.141592653589793D / 180.0D);
  	}

  	/**
  	 * 两经纬度之间距离
  	 * @param lat1
  	 * @param lng1
  	 * @param lat2
  	 * @param lng2
  	 * @return
  	 */
  	public static double getDistance(double lat1, double lng1, double lat2, double lng2){
  		double radLat1 = rad(lat1);
  		double radLat2 = rad(lat2);
  		double a = radLat1 - radLat2;
  		double b = rad(lng1) - rad(lng2);

  		double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) + 
  				Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2.0D), 2.0D)));
  		s *= 6378137.0D;
  		s = Math.round(s * 10000.0D) / 10000L;
  		return s;
  	}
}
