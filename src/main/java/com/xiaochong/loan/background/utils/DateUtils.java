package com.xiaochong.loan.background.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**  时间工具类
 * Created by zhangyou on 2017/4/13.
 */
public class DateUtils {


    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
  
	public static DateTimeFormatter format_ymdhms = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static DateTimeFormatter format_ymdhmssss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static DateTimeFormatter format_ymds = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static DateTimeFormatter format_ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter format_ymdhms_string = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static DateTimeFormatter format_ymdhms_no_signal = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static SimpleDateFormat yyyyMMdd_format = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat ymdhms_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat ymdhms_formatWithBias= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static SimpleDateFormat ymdhms_formatNoSs = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public static SimpleDateFormat yyyyMMddHHmmss_hanziformat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    public static SimpleDateFormat yyyyMMddHHmmss_hanziformat2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    public static SimpleDateFormat yyyyMMdd_hanziformat = new SimpleDateFormat("yyyy年MM月dd日");

    public static DateTimeFormatter format_ymd_String = DateTimeFormatter.ofPattern("yyMMdd");


    /**
     * 字符串转date
     * @param date 时间戳
     * @param format format
     * @return 时间
     */
    public static Date stringToDate(String date,SimpleDateFormat format){
        try {
            if(StringUtils.isBlank(date)){
                return null;
            }
            return format.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 字符串转date
     * @param date 时间戳
     * @return 时间
     */
    public static Date stringToDateStandard (String date){
        SimpleDateFormat format = ymdhms_format;
        if(StringUtils.isNotBlank(date)){
            if(date.split("-").length==3){
                if(date.split(":").length!=3){
                    format = yyyyMMdd_format;
                }
            }else if(date.split("-").length==2){
                format = new SimpleDateFormat("yyyy-MM");
            }
            try {
                return format.parse(date);
            } catch (ParseException e) {
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }


    /**
     * 获取当前时间
     * @return
     * @throws Exception
     */
    public static LocalDateTime getCurrentDate(){
    	LocalDateTime now = LocalDateTime.now();
    	return now;
    }
    
    /**
     * 获取当前时间字符串，格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateStr(){
    	String nowtime=getCurrentDate().format(format_ymdhms);
    	return nowtime;
    }

    public static String getCurrentDateymd(){
        String nowtime=getCurrentDate().format(format_ymdhms_string);
        return nowtime;
    }

    public static String getCurrentDateyymd(){
        String nowtime=getCurrentDate().format(format_ymd_String);
        return nowtime;
    }

    /**
     * 获取当前时间字符串，格式为yyyyMMdd
     * @return
     */
    public static String getCurrentDateString(){
    	String nowtime=getCurrentDate().format(format_ymds);
    	return nowtime;
    }
    

    /**
     * 获取当前时间字符串，格式为yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateStrymd(){
        String nowtime=getCurrentDate().format(format_ymd);
        return nowtime;
    }

    public static String getCurrentDateStr_MS(){
    	String nowtime=getCurrentDate().format(format_ymdhmssss);
    	return nowtime;
    }
    
    /**
     * 获取当前时间long值
     * @return
     */
    public static long getCurrentDataLong(){
        return new Date().getTime();
    }



    /**
     * 获取当前时间
     * @param format
     * @return
     * @throws Exception
     */
    public static Date getCurrentDate(SimpleDateFormat format){
        String tempDate = format.format(new Date());
        try {
            return format.parse(tempDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * @param date
     * @param newFormat
     * @return
     * @throws Exception
     */
    public static Date getDate(Date date, SimpleDateFormat newFormat) {
        String tempDate = newFormat.format(date);
        try {
            return newFormat.parse(tempDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static String format(Date date, SimpleDateFormat format) {
        if(null == date){
            return "";
        }
        return format.format(date);
    }

    /**
     * 将给定的时间秒转换为中文格式的时分秒
     *
     * @param second
     * @return
     */
    public static String formatSecond_ZH_HMS(Integer second) {
        String result = "0秒";
        if (null != second) {
            int hours = (int) (second / (60 * 60));
            int minutes = (int) (second / 60 - hours * 60);
            int seconds = (int) (second - minutes * 60 - hours * 60 * 60);

            String format;
            Object[] array;

            if (hours > 0) {
                format = "%1$,d时%2$,d分%3$,d秒";
                array = new Object[]{hours, minutes, seconds};
            } else if (minutes > 0) {
                format = "%1$,d分%2$,d秒";
                array = new Object[]{minutes, seconds};
            } else {
                format = "%1$,d秒";
                array = new Object[]{seconds};
            }
            result = String.format(format, array);
        }
        return result;
    }

    /**
     * 对日期进行加法操作
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, Integer days) {
        if (null != date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 对日期进行加法操作
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, Integer month) {
        if (null != date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, month);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 对日期进行加法操作
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, Integer hours) {
        if (null != date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, hours);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 对日期的分钟进行加法操作
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, Integer minutes) {
        if (null != date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minutes);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 比较两个日期的大小
     *
     * @param date1
     * @param date2
     * @return 日期相等，返回0；前者大，返回1；后者大，返回-1；
     */
    public static int dateCompare(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        return c1.compareTo(c2);
    }

    /**
     * 判断当前日期是否在指定日期区域内(包含起止日期) [startDate,endDate]
     * <p/>
     * [null,null] --> return 1;
     *
     * @param now
     * @param startDate
     * @param endDate
     * @return -1：超出并小于startDate；0:在范围内；1：超出并大于endDate；
     */
    public static int validateDate(Date now, Date startDate, Date endDate) {
        if (null == startDate && null == endDate) {
            return 0;
        }
        if (null == startDate) {
            //当前时间大于endDate
            if (dateCompare(now, endDate) == 1) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (null == endDate) {
                //当前时间小于startDate
                if (dateCompare(startDate, now) == 1) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                if (dateCompare(startDate, now) == 1) {
                    return -1;
                } else if (dateCompare(now, endDate) == 1) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    /**
     * 计算两个日期先差多少秒
     * @param pre 前一个日期
     * @param after 后一个日期
     * @return
     */
    public static long calTimeDifference(Date pre,Date after){
        return (after.getTime()-pre.getTime())/1000;
    }



    /**
     * 计算两个时间相差的天数
     * @param stardate
     * @param enddate
     * @return
     */
    public static int getHour(Date stardate,Date enddate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff=enddate.getTime()-stardate.getTime();

        long day = diff / nd;

        long hour = diff % nd / nh;

        long min = diff % nd % nh / nm;

        return (int)day;
    }
    
    /**
     * 结束时间与当前时间计算相差的月数
     * @param enddate
     * @return
     */
    public static int getMonthSpace(String enddate, DateTimeFormatter format) {
    	LocalDate formatted = LocalDate.parse(enddate,format);
        LocalDate today = LocalDate.now();
        Period per = Period.between(today,formatted);
        if(per.getYears() == 0 ){
        	return per.getMonths();
        }else if(per.getYears() > 0 ){
        	return (per.getYears() * 12) + per.getMonths();
        }
        
        return (per.getYears() * 12) + per.getMonths();
    }
    
    /**
     * 获取某个时间段之前的时间点
     * @return
     */
    public static String getSomeTimeStapStr(String sign){
    	LocalDateTime beginDateTime =null;
    	if (sign.equals("day")) {//一天前时间
    		beginDateTime =LocalDateTime.now().minusDays(1L);
		}else if(sign.equals("week")){
			beginDateTime =LocalDateTime.now().minusWeeks(1L);
		}else if (sign.equals("month")) {
			beginDateTime =LocalDateTime.now().minusMonths(1L);
		}else if (sign.equals("year")) {
			beginDateTime =LocalDateTime.now().minusYears(1L);
		}
    	String beginTime=beginDateTime.format(format_ymdhms);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			long timestap = sdf.parse(beginTime).getTime();
			beginTime = timestap+"";
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return beginTime;
    }
    
    public static String getSomeTimeDateStr(String sign){
    	String beginTime="";
    	LocalDateTime beginDateTime =null;
    	if (sign.equals("day")) {//一天前时间
    		beginDateTime =LocalDateTime.now().minusDays(1L);
		}else if(sign.equals("week")){
			beginDateTime =LocalDateTime.now().minusWeeks(1L);
		}else if (sign.equals("month")) {
			beginDateTime =LocalDateTime.now().minusMonths(1L);
		}else if (sign.equals("year")) {
			beginDateTime =LocalDateTime.now().minusYears(1L);
		}
    	if (beginDateTime!=null) {
    		beginTime = beginDateTime.format(format_ymdhms);
		}
    	return beginTime;
    }


    /**
     * 获取失效时间点（在什么时间失效）
     * @param minutes       有效分钟数
     * @return
     */
    public static LocalDateTime getInvalidLocalDateTime(Long minutes){
        return LocalDateTime.now().minusMinutes(minutes);
    }


    /**
     * 获取当前年份
     * @return
     */
    public static String getCurrentYear(){
        Calendar instance = Calendar.getInstance();
        return instance.get(Calendar.YEAR)+"";
    }


    /**
     * 获取当前月份
     * @return
     */
    public static String getCurrentMonth(){
        Calendar instance = Calendar.getInstance();
        int month = instance.get(Calendar.MONTH);
        String result;
        if (month < 10){
            result = "0" + month;
        }else {
            result = month+"";
        }
        return result;
    }

    /** 获取无符号的当前时间
     * @return
     */
    public static String getCurrentDateStrNoSignal(){
        return getCurrentDate().format(format_ymdhms_no_signal);
    }


    /**
     * 获取前几个小时的时间
     * @param hours
     * @return
     */
    public static String getNextHourDateStrNoSignal(long hours){
        return LocalDateTime.now().plusHours(hours).format(format_ymdhms_no_signal);
    }


    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }


    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 小时取整
     * @param date
     * @param hour
     * @return
     */
    public static Date integralHour(Date date,Integer hour) {
        if(date==null||hour==null){
            return null;
        }
        long l = date.getTime()- 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        try {
            Date parse = sdf.parse(sdf.format(new Date(l)));
            return addHours(parse, hour);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 分钟取整
     * 以十分钟为单位 ，去除尾端，加上 参数  46->40+minutes*10
     * @param date
     * @param minutes
     * @return
     */
    public static Date integral10Min(Date date,Integer minutes) {
        if(date==null||minutes==null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.MINUTE);//获取分钟
        if(i%10==0){
            minutes--;
        }
        minutes=minutes*10-i%10;
        long l = date.getTime()- 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        try {
            Date parse = sdf.parse(sdf.format(new Date(l)));
            return addMinutes(parse, minutes);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 天数取整
     * @param date
     * @param day
     * @return
     */
    public static Date integralDay(Date date,Integer day) {
        if(date==null||day==null){
            return null;
        }
        long l = date.getTime()- 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            Date parse = sdf.parse(sdf.format(new Date(l)));
            return addDay(parse, day);
        } catch (ParseException e) {
            return null;
        }
    }





    public static void main(String[]args)throws Exception{
//        Date pre = new Date();
//
//        Thread.sleep(5000);
//        Date after = new Date();
//
//        System.out.println(calTimeDifference(pre,after));
        //System.out.println(getMonthSpace(,new Date()));
        //System.out.println(getMonthSpace("2014-01-30 00:00:00") );

        Date date = integral10Min(new Date(), 3);
        System.out.println(date);

    }
}
