package com.ximpl.lib.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Date Time Utils
 */
public class XcDateTimeUtils {
	public static final int START_TW_YEAR = 1911;
	
	/**
	 * Determine whether today is in between dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isBetween(DateTime startDate, DateTime endDate){
		return isBetween(startDate, endDate, new DateTime());
	}
	
	/**
	 * Determine whether the date time is in between dates.
	 * @param startDate
	 * @param endDate
	 * @param curDateTime
	 * @return
	 */
	public static boolean isBetween(DateTime startDate, DateTime endDate, DateTime curDateTime){
		final long mills = curDateTime.getMillis();
		return startDate.getMillis() <= mills && mills <= endDate.getMillis();
	}
	

	/**
	 * Calculate yeas from date to now
	 * @param birthDate
	 * @return
	 */
	public static int yearsFrom(DateTime birthDate){
		return yearsBetween(birthDate, new DateTime());
	}
	
	/**
	 * Calculate years
	 * It might be useful to calculate ages
	 * @param toDate
	 * @param toDate
	 * @return
	 */
	public static int yearsBetween(DateTime fromDate, DateTime toDate){
		return Years.yearsBetween(fromDate.toLocalDate(), toDate.toLocalDate()).getYears();
	}
	
	/**
	 * Calculate days from date to now
	 * @param fromDate
	 * @return
	 */
	public static int daysFrom(DateTime fromDate){
		return daysBetween(fromDate, new DateTime());
	}
	
	/**
	 * Calculate total days in between two DateTime
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static int daysBetween(DateTime fromDate, DateTime toDate){
		return Days.daysBetween(fromDate.toLocalDate(), toDate.toLocalDate()).getDays();
	}
	
	/**
	 * Compare date1 & date2 for year, month, day
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(DateTime date1, DateTime date2){
		if (date1 == null || date2 == null)
			return false;
		return date1.toLocalDate().equals(date2.toLocalDate());
	}

	/**
	 * Determine whether the given dates are same on same day.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Long date1, Long date2){
		if (date1 == null || date2 == null)
			return false;
		
		return isSameDate(new DateTime(date1, DateTimeZone.getDefault()), new DateTime(date2, DateTimeZone.getDefault()));
	}

	/**
	 * Compare date1 & date2 for month, day
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(DateTime date1, DateTime date2){
		if (date1 == null || date2 == null)
			return false;
		
		return (date1.getMonthOfYear() == date2.getMonthOfYear()) && (date1.getDayOfMonth() == date2.getDayOfMonth());
	}
	
	/**
	 * Compare the days are same date
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Long date1, Long date2){
		if (date1 == null || date2 == null)
			return false;
		
		return isSameDay(new DateTime(date1, DateTimeZone.UTC), new DateTime(date2, DateTimeZone.UTC));
	}
	
	/**
	 * Compare the days are same.
	 * @param date1
	 * @param date2
	 * @param dateFormat
	 * @return
	 */
	public static boolean isSameDay(Date date1, String date2, String dateFormat){
		if (date1 == null || XcStringUtils.isNullOrEmpty(date2))
			return false;
		
		return new SimpleDateFormat(dateFormat).format(date1).equals(date2);
	}

	/**
	 * Convert DateTime to String as formatted
	 * @param dateTime
	 * @param dateFormat
	 * @return
	 */
	public static String toString(DateTime dateTime, String dateFormat){
		if (dateTime == null)
			return null;
		
		if (dateFormat == null){
			return dateTime.toString();
		}else
			return DateTimeFormat.forPattern(dateFormat).print(dateTime);
		
	}
	
	/**
	 * Parse date string to DateTime with date format.
	 * Timezone shall be UTC.
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static DateTime parseDate(String dateString, String dateFormat){
		return parseDate(dateString, dateFormat, DateTimeZone.UTC);
	}
	
	/**
	 * Parse date string to DateTime with date format and time zone ID.
	 * If time zone ID is null or not found, Time zone shall be UTC.
	 * @param dateString
	 * @param dateFormat
	 * @param timeZoneText
	 * @return
	 */
	public static DateTime parseDate(String dateString, String dateFormat, String timeZoneText){
		if (XcStringUtils.isNullOrEmpty(dateString))
			return null;
		try{
			final DateTimeZone timeZone = XcStringUtils.isNullOrEmpty(timeZoneText) ? DateTimeZone.UTC : DateTimeZone.forID(timeZoneText); 
			return parseDate(dateString, dateFormat, timeZone);
		}catch(Exception ex){
			return parseDate(dateString, dateFormat, DateTimeZone.UTC);
		}
	}

	/**
	 * Parse Datetime with/without format
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static DateTime parseDate(String dateString, String dateFormat, DateTimeZone timeZone){
		if (XcStringUtils.isNullOrEmpty(dateString))
			return null;
		
		if (timeZone == null)
			timeZone = DateTimeZone.UTC;
				
		if (XcStringUtils.isNullOrEmpty(dateFormat)){
			return ISODateTimeFormat.dateTime().withZone(timeZone).parseDateTime(dateString);
		}else
			return DateTime.parse(dateString, DateTimeFormat.forPattern(dateFormat).withZone(timeZone));
	}
	
	/**
	 * Parse Taiwanese date string
	 * @param twDateString
	 * @return
	 */
	public static DateTime parseTwDate(String twDateString){
		if (XcStringUtils.isNullOrEmpty(twDateString))
			return null;
		
		switch(twDateString.length()) {
		case 6:
			return parseTwDate("00"+twDateString, "yyyyMMdd");
		case 7:
			return parseTwDate("0"+twDateString, "yyyyMMdd");
		case 8:
			return parseTwDate(twDateString, "yyyyMMdd");
		default:
			try {
				String separator = twDateString.substring(4, 1);
				return parseTwDate(twDateString, "yyyy/MM/dd".replaceAll("/", separator));
			}catch(Exception ex) {
				return null;
			}
		}
		
	}
	
	/**
	 * Parse Taiwanese date string 
	 * @param twDateString
	 * @return
	 */
	public static DateTime parseTwDate(String twDateString, String format){
		if (XcStringUtils.isNullOrEmpty(twDateString))
			return null;
		final int yearIndex = format.indexOf("yyyy");
		
		final String yearString = twDateString.substring(yearIndex, 4);
		int year = XcNumberUtils.toInt(yearString) + START_TW_YEAR;
		twDateString = twDateString.replaceFirst(yearString, String.valueOf(year));

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
		return DateTime.parse(twDateString, formatter);
	}
	
	/**
	 * Parse Excel cell value to DateTime
	 * @param cellValue
	 * @return
	 */
	public static DateTime parseExcelDate(String cellValue){
		if (XcStringUtils.isNullOrEmpty(cellValue))
			return null;
		return parseExcelDate(Double.parseDouble(cellValue));
	}
	
	/**
	 * Parse Excel cell value to DateTime
	 * @param cellValue
	 * @return
	 */
	public static DateTime parseExcelDate(double cellValue){
		return new DateTime(DateUtil.getJavaDate(cellValue));
	}
	
	/**
	 * Get Current UTC Time
	 * @return
	 */
	public static DateTime getUtcTime(){
		return DateTime.now(DateTimeZone.UTC);
	}
	
	/**
	 * Convert UTC time to local time
	 * @param utc
	 * @param timeZone
	 * @return
	 */
	public static DateTime toLocalTime(long utc, DateTimeZone timeZone){
		return new LocalDateTime(utc, DateTimeZone.UTC).toDateTime(timeZone);
	}
	
	public static DateTime toLocalTime(Date date, DateTimeZone timeZone){
		return toLocalTime(date.getTime(), timeZone);
	}

}
