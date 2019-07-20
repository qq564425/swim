package com.hdnav.utils;

import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * 描述:公共日期工具类
 */
public class DateUtils {
 
  public static String DATE_FORMAT = "yyyy-MM-dd";
 
  public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
 
  public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";
 
  /**
   * 获取当前日期
   * 
   * 
   * @return
   * 
   */
  public static String getCurrentDate() {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
    datestr = df.format(new Date());
    return datestr;
  }
 
  /**
   * 获取当前日期时间
   * 
   * 
   * @return
   * 
   */
  public static String getCurrentDateTime() {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
    datestr = df.format(new Date());
    return datestr;
  }
 
  /**
   * 获取当前日期时间
   * 
   * 
   * @return
   * 
   */
  public static String getCurrentDateTime(String Dateformat) {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(Dateformat);
    datestr = df.format(new Date());
    return datestr;
  }
 
  public static String dateToDateTime(Date date) {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
    datestr = df.format(date);
    return datestr;
  }
  /**
   * 将字符串日期转换为日期格式
   * 
   * 
   * @param datestr
   * @return
   * 
   */
  public static Date stringToDate(String datestr) {
 
      if(datestr ==null ||datestr.equals("")){
        return null;
      }
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
    try {
      date = df.parse(datestr);
    } catch (ParseException e) {
      date=DateUtils.stringToDate(datestr,"yyyyMMdd");
    }
    return date;
  }
  
  /**
   * 将字符串日期转换为日期格式字符串
   */
  public static String transToDateString(String datestr1, String datestr2) {
      if(datestr1 ==null ||datestr1.equals("")){
        return "";
      }
    Date date = stringToDate(datestr1, DateUtils.DATE_TIME_FORMAT);
    return dateToString(date, datestr2);
  }
 
  /**
   * 将字符串日期转换为日期格式
   * 自定義格式
   * 
   * @param datestr
   * @return
   * 
   */
  public static Date stringToDate(String datestr, String dateformat) {
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat(dateformat);
    try {
      date = df.parse(datestr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }
 
 
 
 
  /**
   * 将日期格式日期转换为字符串格式
   * 
   * 
   * @param date
   * @return
   * 
   */
  public static String dateToString(Date date) {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
    datestr = df.format(date);
    return datestr;
  }
 
  /**
   * 将日期格式日期转换为字符串格式 自定義格式
   * 
   * @param date
   * @param dateformat
   * @return
   */
  public static String dateToString(Date date, String dateformat) {
    String datestr = null;
    SimpleDateFormat df = new SimpleDateFormat(dateformat);
    datestr = df.format(date);
    return datestr;
  }
 
  /**
   * 获取日期的DAY值
   * 
   * 
   * @param date
   *      输入日期
   * @return
   * 
   */
  public static int getDayOfDate(Date date) {
    int d = 0;
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    d = cd.get(Calendar.DAY_OF_MONTH);
    return d;
  }
 
  /**
   * 获取日期的MONTH值
   * 
   * 
   * @param date
   *      输入日期
   * @return
   * 
   */
  public static int getMonthOfDate(Date date) {
    int m = 0;
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    m = cd.get(Calendar.MONTH) + 1;
    return m;
  }
 
  /**
   * 获取日期的YEAR值
   * 
   * 
   * @param date
   *      输入日期
   * @return
   * 
   */
  public static int getYearOfDate(Date date) {
    int y = 0;
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    y = cd.get(Calendar.YEAR);
    return y;
  }
 
  /**
   * 获取星期几
   * 
   * 
   * @param date
   *      输入日期
   * @return
   * 
   */
  public static int getWeekOfDate(Date date) {
    int wd = 0;
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    wd = cd.get(Calendar.DAY_OF_WEEK) - 1;
    return wd;
  }
 
  /**
   * 获取输入日期的当月第一天
   * 
   * 
   * @param date
   *      输入日期
   * @return
   * 
   */
  public static Date getFirstDayOfMonth(Date date) {
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    cd.set(Calendar.DAY_OF_MONTH, 1);
    return cd.getTime();
  }
 
  /**
   * 获得输入日期的当月最后一天
   * 
   * @param date
   */
  public static Date getLastDayOfMonth(Date date) {
    return DateUtils.addDay(DateUtils.getFirstDayOfMonth(DateUtils.addMonth(date, 1)), -1);
  }
 
  /**
   * 判断是否是闰年
   * 
   * 
   * @param date
   *      输入日期
   * @return 是true 否false
   * 
   */
  public static boolean isLeapYEAR(Date date) {
 
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    int year = cd.get(Calendar.YEAR);
 
    if (year % 4 == 0 && year % 100 != 0 | year % 400 == 0) {
      return true;
    } else {
      return false;
    }
  }
 
  /**
   * 根据整型数表示的年月日，生成日期类型格式
   * 
   * @param year
   *      年
   * @param month
   *      月
   * @param day
   *      日
   * @return
   * 
   */
  public static Date getDateByYMD(int year, int month, int day) {
    Calendar cd = Calendar.getInstance();
    cd.set(year, month-1, day);
    return cd.getTime();
  }
 
  /**
   * 获取年周期对应日
   * 
   * @param date
   *      输入日期
   * @param iyear
   *      年数  負數表示之前
   * @return
   * 
   */
  public static Date getYearCycleOfDate(Date date, int iyear) {
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
 
    cd.add(Calendar.YEAR, iyear);
 
    return cd.getTime();
  }
 
  /**
   * 获取月周期对应日
   * 
   * @param date
   *      输入日期
   * @param i
   * @return
   * 
   */
  public static Date getMonthCycleOfDate(Date date, int i) {
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
 
    cd.add(Calendar.MONTH, i);
 
    return cd.getTime();
  }
 
  /**
   * 计算 fromDate 到 toDate 相差多少年
   * 
   * @param fromDate
   * @param toDate
   * @return 年数
   * 
   */
  public static int getYearByMinusDate(Date fromDate, Date toDate) {
    Calendar df=Calendar.getInstance();
    df.setTime(fromDate);
 
    Calendar dt=Calendar.getInstance();
    dt.setTime(toDate);
 
    return dt.get(Calendar.YEAR)-df.get(Calendar.YEAR);
  }
 
  /**
   * 计算 fromDate 到 toDate 相差多少个月
   * 
   * @param fromDate
   * @param toDate
   * @return 月数
   * 
   */
  public static int getMonthByMinusDate(Date fromDate, Date toDate) {
    Calendar df=Calendar.getInstance();
    df.setTime(fromDate);
 
    Calendar dt=Calendar.getInstance();
    dt.setTime(toDate);
 
    return dt.get(Calendar.YEAR)*12+dt.get(Calendar.MONTH)-
        (df.get(Calendar.YEAR)*12+df.get(Calendar.MONTH));
  }
 
  /**
   * 计算 fromDate 到 toDate 相差多少天
   * 
   * @param fromDate
   * @param toDate
   * @return 天数
   * 
   */
  public static long getDayByMinusDate(Object fromDate, Object toDate) {
 
    Date f=DateUtils.chgObject(fromDate);
 
    Date t=DateUtils.chgObject(toDate);
 
    long fd=f.getTime();
    long td=t.getTime();
 
    return (td-fd)/(24L * 60L * 60L * 1000L);
  }
 
  /**
   * 计算年龄
   * 
   * @param birthday
   *      生日日期
   * @param calcDate
   *      要计算的日期点
   * @return
   * 
   */
  public static int calcAge(Date birthday, Date calcDate) {
 
    int cYear=DateUtils.getYearOfDate(calcDate);
    int cMonth=DateUtils.getMonthOfDate(calcDate);
    int cDay=DateUtils.getDayOfDate(calcDate);   
    int bYear=DateUtils.getYearOfDate(birthday);
    int bMonth=DateUtils.getMonthOfDate(birthday);
    int bDay=DateUtils.getDayOfDate(birthday);
 
    if(cMonth>bMonth||(cMonth==bMonth&&cDay>bDay)){
      return cYear-bYear;
    }else{
      return cYear-1-bYear;
    }
  }
 
  /**
   * 从身份证中获取出生日期
   * 
   * @param idno
   *      身份证号码
   * @return
   * 
   */
  public static String getBirthDayFromIDCard(String idno) {
    Calendar cd = Calendar.getInstance();
    if (idno.length() == 15) {
      cd.set(Calendar.YEAR, Integer.valueOf("19" + idno.substring(6, 8))
          .intValue());
      cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(8, 10))
          .intValue() - 1);
      cd.set(Calendar.DAY_OF_MONTH,
          Integer.valueOf(idno.substring(10, 12)).intValue());
    } else if (idno.length() == 18) {
      cd.set(Calendar.YEAR, Integer.valueOf(idno.substring(6, 10))
          .intValue());
      cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(10, 12))
          .intValue() - 1);
      cd.set(Calendar.DAY_OF_MONTH,
          Integer.valueOf(idno.substring(12, 14)).intValue());
    }
    return DateUtils.dateToString(cd.getTime());
  }
 
  /**
   * 在输入日期上增加（+）或减去（-）天数
   * 
   * @param date
   *      输入日期
   * @param imonth
   *      要增加或减少的天数
   */
  public static Date addDay(Date date, int iday) {
    Calendar cd = Calendar.getInstance();
 
    cd.setTime(date);
 
    cd.add(Calendar.DAY_OF_MONTH, iday);
 
    return cd.getTime();
  }
 
  /**
   * 在输入日期上增加（+）或减去（-）月份
   * 
   * @param date
   *      输入日期
   * @param imonth
   *      要增加或减少的月分数
   */
  public static Date addMonth(Date date, int imonth) {
    Calendar cd = Calendar.getInstance();
 
    cd.setTime(date);
 
    cd.add(Calendar.MONTH, imonth);
 
    return cd.getTime();
  }
 
  /**
   * 在输入日期上增加（+）或减去（-）年份
   * 
   * @param date
   *      输入日期
   * @param imonth
   *      要增加或减少的年数
   */
  public static Date addYear(Date date, int iyear) {
    Calendar cd = Calendar.getInstance();
 
    cd.setTime(date);
 
    cd.add(Calendar.YEAR, iyear);
 
    return cd.getTime();
  }
 
  /**
   * 將OBJECT類型轉換為Date
   * @param date
   * @return
   */
  public static Date chgObject(Object date){
 
    if(date!=null&&date instanceof Date){
      return (Date)date;
    }
 
    if(date!=null&&date instanceof String){
      return DateUtils.stringToDate((String)date);
    }
 
    return null;
 
  }
 
  public static long getAgeByBirthday(String date){
 
    Date birthday = stringToDate(date, "yyyy-MM-dd");
    long sec = new Date().getTime() - birthday.getTime();
 
    long age = sec/(1000*60*60*24)/365;
 
    return age;
  }
  
  /**
   * element ui日期组件日期转换
   * @param date
   * @return
   */
  public static String dateConversion(String date) {
	  if(date == null || "".equals(date)) {
		  return null;
	  }
	  boolean status = date.contains("Z");  
	  if(status) {
		  date = date.replace("Z", " UTC");//注意是空格+UTC
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
		  Date d = null;
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToString(d);
	  }else {
		  return date;
	  }	  
  }

  /**
   * 按传入的格式，将日期类型格式化成字符串
   *
   * @param date
   *            日期
   * @param format
   *            格式
   * @return 格式化后的字符串
   */
  public static String date2String(Date date, String format) {
    String result = null;
    requireNonNull(date, "date is null");
    requireNonNull(format, "format is null");

    result = DateTimeFormat.forPattern(format).print(date.getTime());
    return result;
  }

  /**
   * 日期类型格式化成字符串
   *
   * @param date
   *            日期
   * @return yyyy-MM-dd HH:mm:ss格式的字符串
   */
  public static String date2String(Date date) {
    return date2String(date, DATE_TIME_FORMAT);
  }
  public static String dateString(String date) {
    requireNonNull(date, "date is null");
    return  DateTimeFormat.forPattern("yyyyMMdd").print(stringToDate(date).getTime());
  }
  
  /**
   * 获取范围日期 内的所有日期值
   * @param beginDate
   * @param endDate
   * @return List<String>
   * @throws ParseException
   */
  public static List<String> getRangeDate(String beginDate,String endDate) {
	 List<String> dateList = new ArrayList<String>();
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 Calendar cal = Calendar.getInstance();
	 try {
		 cal.setTime(sdf.parse(beginDate));	  
		 for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
		   dateList.add(sdf.format(d));
		 }
	  } catch (Exception e) {
		// TODO: handle exception
	  }
	 return dateList;
  }
  public static long get_D_Plaus_1(Calendar c) {
	   c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
	   return c.getTimeInMillis();
  }
  
  public static List<String> getRangeDateMonth(String beginDate,String endDate){
	  List<String> dateList = new ArrayList<String>();
	  try{
			 
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
          String nowdate=format.format(new Date());//当前月份

          Date d1 = new SimpleDateFormat("yyyy-MM").parse(beginDate);//定义起始日期

          Date d2 = new SimpleDateFormat("yyyy-MM").parse(endDate);//定义结束日期  可以去当前月也可以手动写日期。

          Calendar dd = Calendar.getInstance();//定义日期实例

          dd.setTime(d1);//设置日期起始时间

          while (dd.getTime().before(d2)) {//判断是否到结束日期

              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

              String str = sdf.format(dd.getTime());

              dateList.add(str);
              
              dd.add(Calendar.MONTH, 1);//进行当前日期月份加1

          }
          dateList.add(endDate);

      }catch (Exception e){
          System.out.println("异常"+e.getMessage());
      }finally {
		return dateList;
	  }	
  }
}
