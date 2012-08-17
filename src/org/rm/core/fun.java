package org.rm.core;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.UUID;
/*Title:这里为所有工程用到的静态函数库 */
public class fun {
	public fun() {
	}

	/**
	 * 得到唯一的GUUID
	 * 
	 * @return
	 */
	public static String GetUUID() {
		return fun.Replace(UUID.randomUUID().toString().toUpperCase(), "-", "");
	}

	/**
	 * 
	 * @param datetime
	 *            Date
	 * @return String 返回日期 ，格式：yyyy-MM-dd
	 */
	public static String GetFormatDate(Date datetime) {
		String str_date = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		str_date = sf.format(datetime);
		return str_date;
	}

	/**
	 * 
	 * @return String 返回日期
	 */
	public static String GetFormatDate() {
		Date curDate = new Date();
		return GetFormatDate(curDate);
	}

	/**
	 * 
	 * @return String 返回日期 ，格式：yyyy-MM-dd-HH-mm-ss 说明：这里入主要是给上传附件用，生存没有重复的ID
	 */
	public static String GetFormatTimeFile() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		return sf.format(cal.getTime());
	}

	/**
	 * 
	 * @return String 返回日期 ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String GetCurFormatTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf.setTimeZone(cal.getTimeZone());
		return sf.format(cal.getTime());
	}
	/**
	 * 
	 * @return String 返回日期 ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String GetCurFormatDay() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		sf.setTimeZone(cal.getTimeZone());
		return sf.format(cal.getTime());
	}
	/**
	 * 
	 * @return int 返回年份
	 */
	public static int GetYear(Date curDate) {
		int i_year = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		i_year = cal.get(Calendar.YEAR);
		return i_year;
	}

 
 

	/**
	 * 
	 * @param source
	 *            String 源字符串
	 * @param oldString
	 *            String 将要被替换的字符
	 * @param newString
	 *            String 替换后的字符
	 * @return String 字符替换
	 */
	public static String Replace(String source, String oldString, String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length(); // 源字符串长度
		int lengthOfOld = oldString.length(); // 老字符串长度
		int posStart = 0; // 开始搜索位置
		int pos; // 搜索到老字符串的位置
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}

		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 将字符串格式化成 HTML 代码输出 只转换特殊字符，适合于 HTML 中的表单区域
	 * 
	 * @param str
	 *            要格式化的字符串
	 * @return 格式化后的字符串
	 */
	public static String toHtmlInput(String str) {
		if (str == null)
			return null;
		String html = new String(str);
		html = Replace(html, "&", "&amp;");
		html = Replace(html, "<", "&lt;");
		html = Replace(html, ">", "&gt;");
		return html;
	}

	/**
	 * 将字符串格式化成 HTML 代码输出 格式化后的字符串
	 */
	public static String toHtml(String str) {
		if (str == null)
			return null;
		String html = new String(str);
		html = toHtmlInput(html);
		html = Replace(html, "\r\n", "\n");
		html = Replace(html, "\n", "<br>\n");
		html = Replace(html, "\t", "    ");
		html = Replace(html, "  ", " &nbsp;");
		return html;
	}

	/**
	 * 将普通字符串格式化成数据库认可的字符串格式
	 * 
	 * @param str
	 *            要格式化的字符串
	 * @return 合法的数据库字符串
	 */
	public static String toSql(String str) {
		if (str == null)
			return null;
		String sql = new String(str);
		return Replace(sql, "'", "''");
	}

/**
   *考虑安全问题将传的参数进行转换
   * 如敏感字符> < '
   */
	public static String toParm(String str) {
		return toSql(toHtmlInput(str));
	}

	/**
	 * 判断字符串是否为Null或空字符串
	 * 
	 * @param String
	 *            要判断的字符串
	 * @return String true-是空字符串,false-不是空字符串
	 */
	public static boolean nil(String s) {
		if (s == null || s.equals("")) {
			return true;
		}
		return false;
	}
	 
 
	/**
	 * 字符串数组转换为字符串,使用逗号分隔
	 */
	public static String split(String[] s, String spliter) {
		if (fun.nil(s))
			return "";
		if (s.length == 1)
			return s[0];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++) {
			sb.append(s[i]).append(spliter);
		}
		sb.deleteCharAt(sb.lastIndexOf(spliter));
		return sb.toString();
	}

	/**
	 * 将整型数组合并为用字符分割的字符串
	 * 
	 * @param int[]
	 * @return String
	 */
	public static String intJoin(int[] arr) {
		if (arr == null || arr.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = arr.length; i < len; i++) {
			sb.append(",").append(arr[i]);
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}

	/**
	 * 返回一个整数数组
	 * 
	 * @param s
	 *            String
	 * @param spliter
	 *            分隔符如逗号
	 * @return int[]
	 */
	public static int[] iniSplit(String s, String spliter) {
		if (s == null || s.indexOf(spliter) == -1) {
			return (new int[0]);
		}
		String[] ary = s.split(spliter);
		int[] result = new int[ary.length];
		try {
			for (int i = 0; i < ary.length; i++) {
				result[i] = Integer.parseInt(ary[i]);
			}
		} catch (NumberFormatException ex) {
		}
		return result;
	}

	/**
	 * 返回一个整数数组
	 * 
	 * @param s
	 *            String
	 * @return int[]
	 */
	public static int[] intSplit(String s) {
		if (nil(s))
			return new int[0];
		if (s.indexOf(",") > -1) {
			return fun.iniSplit(s, ",");
		} else {
			int[] i = new int[1];
			i[0] = Integer.parseInt(s);
			return i;
		}
	}

	/**
	 * 如果第一个字符串不为空则返回该字符串,否则返回第二个
	 */
	public static String nil(String s, String _default) {
		if (fun.nil(s))
			return _default;
		else
			return s;
	}

	/**
	 * 判断字符串数组是否为空
	 */
	public static boolean nil(String[] s) {
		return (s == null || s.length == 0);
	}

	/**
	 * 如果数组为空,则返回空数组
	 */
	public static String[] notNil(String[] s) {
		if (s == null || s.length == 0) {
			return new String[0];
		}
		return s;
	}

	/**
	 * 改变字符串编码到gbk
	 */
	public static String toGBK(String src) {
		if (nil(src))
			return "";
		String s = null;
		try {
			s = new String(src.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 改变字符串编码到utf8
	 */
	public static String toUTF8(String src) {
		if (nil(src))
			return "";
		String s = null;
		try {
			s = new String(src.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		 
		}
		return s;
	}

	/**
	 * 转换String到boolean
	 */
	public static boolean parseBoolean(String flag) {
		if (nil(flag))
			return false;
		else if (flag.equals("true") || flag.equals("1") || flag.equals("是") || flag.equals("yes"))
			return true;
		else if (flag.equals("false") || flag.equals("0") || flag.equals("否") || flag.equals("no"))
			return false;
		return false;
	}

	/*
	 * 把字符串转换成数字，_default：缺省值
	 */
	public static int parseInt(String flag, int _default) {
		int returnvalue =_default;
		if (nil(flag)) returnvalue = _default;
		else {
			try {
				returnvalue = Integer.parseInt(flag);
			} catch (Exception e) {
				log.error("fun.parseInt:" + e.toString());
			}
		}
		 
		return returnvalue;
	}
	/*
	 * 把字符串转换成数字，_default：缺省值
	 */
	public static float parseFloat(String flag, float _default) {
		float returnvalue =_default;
		if (nil(flag)) returnvalue = _default;
		else {
			try { 
				returnvalue = Float.parseFloat(flag);
			} catch (Exception e) {
				log.error("fun.parseFloat:" + e.toString());
			}
		}
		 
		return returnvalue;
	}
	/**
	 * 转换String到long
	 */
	public static long parseLong(String flag) {
		if (nil(flag))
			return 0;
		return Long.parseLong(flag);
	}

	/**
	 * 字符填充
	 * 
	 * @param source
	 *            源字符串
	 * @param filler
	 *            填充字符,如0或*等
	 * @param length
	 *            最终填充后字符串的长度
	 * @return 最终填充后字符串
	 */
	public static String fill(String source, String filler, int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length - source.length(); i++) {
			sb.append(filler);
		}
		sb.append(source);
		return sb.toString();
	}

	/**
	 * 从开头到spliter字符,截取字符串数组中的每一项
	 * 
	 * @param arr
	 *            源字符串数组
	 * @param spliter
	 *            切割符
	 * @return 切割后的字符串数组
	 */
	public static String[] subStrBefore(String[] arr, String spliter) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].substring(0, arr[i].indexOf(spliter));
		}
		return arr;
	}

	/**
	 * 
	 * 将字串转成日期，字串格式: yyyy-MM-dd
	 * 
	 * @param string
	 *            String
	 * @return Date
	 */

	public static Date parseDate(String string) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return (Date) formatter.parse(string);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串数组中是否包含指定的字符串。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @param caseSensitive
	 *            是否大小写敏感
	 * @return 包含时返回true，否则返回false
	 */

	public static boolean contains(String[] strings, String string, boolean caseSensitive) {
		for (int i = 0; i < strings.length; i++) {
			if (caseSensitive == true) {
				if (strings[i].equals(string)) {
					return true;
				}
			} else {
				if (strings[i].equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 字符串数组中是否包含指定的字符串。大小写敏感。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @return 包含时返回true，否则返回false
	 */

	public static boolean contains(String[] strings, String string) {
		return contains(strings, string, true);
	}

	/**
	 * 不区分大小写判定字符串数组中是否包含指定的字符串。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @return 包含时返回true，否则返回false
	 */

	public static boolean containsIgnoreCase(String[] strings, String string) {
		return contains(strings, string, false);
	}

	/**
	 * 返回一个整数数组
	 * 
	 * @param s
	 *            String[]
	 * @return int[]
	 */
	public static int[] parseInt(String[] s) {
		if (s == null) {
			return (new int[0]);
		}
		int[] result = new int[s.length];
		try {
			for (int i = 0; i < s.length; i++) {
				result[i] = Integer.parseInt(s[i]);
			}
		} catch (NumberFormatException ex) {
		}
		return result;
	}

	 

	/**
	 * 将整型数组合并为用字符分割的字符串
	 * 
	 * @param int[]
	 * @return String
	 */
	public static String join(int[] arr) {
		if (arr == null || arr.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = arr.length; i < len; i++) {
			sb.append(",").append(arr[i]);
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}

	/**
	 * 将字符串的第一个字母大写
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String firstCharToUpperCase(String s) {
		if (s == null || s.length() < 1) {
			return "";
		}
		char[] arrC = s.toCharArray();
		arrC[0] = Character.toUpperCase(arrC[0]);
		return String.copyValueOf(arrC);
	}

	/**
	 * 根据当前字节长度取出加上当前字节长度不超过最大字节长度的子串
	 * 
	 * @param str
	 * @param currentLen
	 * @param MAX_LEN
	 * @return
	 */
	public static String getSubStr(String str, int currentLen, int MAX_LEN) {
		int i;
		for (i = 0; i < str.length(); i++) {
			if (str.substring(0, i + 1).getBytes().length + currentLen > MAX_LEN) {
				break;
			}
		}
		if (i == 0) {
			return "";
		} else {
			return str.substring(0, i);
		}
	}

	public static String DateToString(java.util.Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	public static boolean isInteger(String value) {
		  try {
		   Integer.parseInt(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
		 }
	 public static boolean isDouble(String value) {
		  try {
		   Double.parseDouble(value);
		   if (value.contains("."))
		    return true;
		   return false;
		  } catch (NumberFormatException e) {
		   return false;
		  }
		 }
	public static boolean isintfloat(String str) {

		if (str.equalsIgnoreCase("java.lang.Integer") || str.equalsIgnoreCase("float") || str.equalsIgnoreCase("int")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDate(String str) {

		if (str.equalsIgnoreCase("java.sql.Date") || str.equalsIgnoreCase("Date") || (str.equalsIgnoreCase("java.util.Date"))) {
			return true;
		} else {
			return false;
		}
	}
}
