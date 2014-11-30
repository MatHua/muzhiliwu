package com.muzhiliwu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间基本操作
 * 
 * 
 */
public class DateUtils {
	public static final DateFormat dformat_full = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat dformat_day = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final DateFormat dformat_day_2 = new SimpleDateFormat(
			"yyyy年MM月dd日");

	/**
	 * 时间字段常量，表示“秒”
	 */
	public final static int SECOND = 0;

	/**
	 * 时间字段常量，表示“分”
	 */
	public final static int MINUTE = 1;

	/**
	 * 时间字段常量，表示“时”
	 */
	public final static int HOUR = 2;

	/**
	 * 时间字段常量，表示“天”
	 */
	public final static int DAY = 3;

	private DateUtils() {
	}

	/**
	 * 获取现在的时间，精确到秒
	 * 
	 * @return
	 */
	public static String now() {
		return dformat_full.format(new Date());
	}

	/**
	 * 获取今天的日期 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String today() {
		return dformat_day.format(new Date());
	}

	/**
	 * 获取yyyy年MM月dd日
	 * 
	 * @return
	 */
	public static String today2() {
		return dformat_day_2.format(new Date());
	}

	/**
	 * 
	 * @param dateString
	 * @param beforeDays
	 * @return
	 * @throws java.text.ParseException
	 * @throws java.text.ParseException
	 */
	public Date getDate(String dateString, int beforeDays)
			throws java.text.ParseException, java.text.ParseException {
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
		Date inputDate = dformat_day.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear - beforeDays);
		return cal.getTime();
	}

	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static Date StringToDate(String dateStr, DateFormat dd) {
		// DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			try {
				date = dd.parse(dateStr);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算两个时间的差距
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long diff(Date begin, Date end, int type) {
		switch (type) {
		case SECOND:
			return (end.getTime() - begin.getTime()) / 1000;

		case MINUTE:
			return (end.getTime() - begin.getTime()) / (1000 * 60);
		case HOUR:
			return (end.getTime() - begin.getTime()) / (1000 * 60 * 60);
		case DAY:
			return (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
		}
		return 0;

	}

	public static void main(String[] args) {
		// Calendar cal = Calendar.getInstance();
		// int month = cal.get(Calendar.MONTH) + 1;
		// int year = cal.get(Calendar.YEAR);
		// System.out.println(year);
		// System.out.println(month);
		// System.out.println(new Date());
		System.out.println(StringToDate("19931012111", dformat_day_2));
	}

}