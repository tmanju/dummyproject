package com.thirdpillar.codify.loanpath.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.User;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.codify.loanpath.model.Country;
import com.thirdpillar.foundation.i18n.DateFormatter;
import com.thirdpillar.foundation.service.LookupService;

import org.apache.log4j.Logger;

/**
 * This class includes reusable function
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class Utility {

	private static final Logger LOG = Logger.getLogger(Utility.class);

	/**
	 * This method is used to get number of days between two dates. Null check
	 * is also implemented in this method.
	 * 
	 * @param firstDate
	 *            is Date type.
	 * @param secondDate
	 *            is Date type.
	 * @return int >= 0 as number of days otherwise -1(in case of any null
	 *         parameter);
	 */
	public static int daysDifferenceInDates(Date firstDate, Date secondDate) {
		int diffDays = 0;
		if (firstDate != null && secondDate != null) {
			diffDays = Days.daysBetween(new DateMidnight(firstDate),
					new DateMidnight(secondDate)).getDays();
			diffDays = (diffDays < 0) ? -(diffDays) : diffDays;
		} else {
			diffDays = -1;
		}
		return diffDays;

	}

	/**
	 * This method is used to get difference of months between two dates. Null
	 * check is also implemented in this method.
	 * 
	 * @param firstDate
	 *            is Date type.
	 * @param secondDate
	 *            is Date type.
	 * 
	 * @return int >= 0 as number of months otherwise -1(in case of any null
	 *         parameter);
	 */
	public static int monthsDifferenceInDates(Date startDate, Date endDate) {
		int diffMonth = 0, diffYear = 0;
		if (startDate != null && endDate != null) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);

			diffYear = endCalendar.get(Calendar.YEAR)
					- startCalendar.get(Calendar.YEAR);
			diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
					- startCalendar.get(Calendar.MONTH);
		} else {
			diffMonth = -1;
		}
		return diffMonth;
	}

	/**
	 * This method is used to check if a String isNumeric and can be safely
	 * casted to an Integer.
	 * 
	 * @param Strimh
	 *            to check
	 * 
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			Log.error("Error Occured at numeric check", e);
			return false;
		} catch (NullPointerException e) {
			Log.error("Error Occured at null pointer check", e);
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * Simple & Straight forward method used to get maximum out of 2 BigDecimal
	 * Paased.
	 * 
	 * @param1 First BigDecimal
	 * @param2 Second BigDecimal
	 * 
	 * @return BigDeimal
	 * 
	 * @author Bharat
	 */

	public static BigDecimal getMaxBigDecimal(BigDecimal one, BigDecimal two) {
		if (one != null && two != null) {
			return one.max(two);
		}
		return new BigDecimal(-1);
	}

	/**
	 * This method is used to Calculate the actual and exact difference between
	 * two dates without ignoring the leap year and daylight savings etc.
	 * 
	 * if any date is null, -1 will be returned.
	 * 
	 * @param Cal1
	 *            and Cal2
	 * 
	 * @return difference in days.
	 * 
	 *         By Bharat Bajaj
	 */
	public static int daysDifferenceBetween(Calendar day1, Calendar day2) {
		if (day1 != null && day2 != null) {
			Calendar dayOne = (Calendar) day1.clone(), dayTwo = (Calendar) day2
					.clone();

			if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
				return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR)
						- dayTwo.get(Calendar.DAY_OF_YEAR));
			} else {
				if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
					// If Second date is greater than first date..
					return -1;
				}
				int extraDays = 0;

				while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
					dayOne.add(Calendar.YEAR, -1);
					// getActualMaximum() important for leap years
					extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR)
						+ dayOne.get(Calendar.DAY_OF_YEAR);
			}
		} else {
			return -1;
		}
	}

	/**
	 * Method to get the User by username
	 * 
	 * @param String
	 *            userName to get
	 * 
	 * @return object of User
	 */
	public static User getUserByUserName(String userName) {
		return (User) LookupService.getResult("User.byUsername", "username",
				userName);
	}

	public static Date getDateFromString(String dateInString) {
		if (dateInString != null && !StringUtils.isBlank(dateInString)) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try {
				return formatter.parse(dateInString);
			} catch (ParseException e) {
				Log.error("Error occured while parsing date", e);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Utility method that will remove duplicates from the list. Nothing will be
	 * returned, the same list will have duplicates removed..
	 * 
	 * this method makes use of HashSet, so make sure if your List has object
	 * other than non primitives/Wrapper than those Class should override hash
	 * code & equals method..
	 * 
	 * @param list
	 *            to remove duplicates in..
	 */
	public static void removeDuplicates(List<?> lst) {
		if (lst != null && !lst.isEmpty()) {
			Set hs = new HashSet();
			hs.addAll(lst);
			lst.clear();
			lst.addAll(hs);
		}
	}

	/**
	 * This is a Custom method written specifically for particular Use Case to
	 * calculate years out of days.
	 * 
	 * Note:- Days equals or greater than 335 will be treated as 1 complete
	 * Year. Days Less than 335 will be treated as 0 years , even 334..!!
	 * 
	 * @param days
	 * 
	 * @return Int year.
	 * 
	 */
	public static int getYearfromDays(int intDays) {
		int roundOfYears = 0;
		if (intDays > -1) {
			BigDecimal days = new BigDecimal(intDays);
			BigDecimal years = days.divide(new BigDecimal(365), 4,
					RoundingMode.HALF_DOWN);
			roundOfYears = years.intValue();
			BigDecimal decimal = years.subtract(new BigDecimal(roundOfYears));

			// 0.9178 corresponds to 335 days out of 365
			BigDecimal roundOffVal = new BigDecimal("0.9178");

			if (decimal.compareTo(roundOffVal) >= 0) {
				roundOfYears++;
			} else {
				// nothing to do.
			}
		}
		return roundOfYears;
	}

	/**
	 * This is a Custom method written specifically for particular Use Case to
	 * calculate years out of two dates. This may not give the exact years
	 * between 2 dates..
	 * 
	 * Added for cstm Scoring attr : bap_yrs_in_business
	 * 
	 * @param1 date1
	 * @param2 date2
	 * 
	 * @return int year.
	 */
	public static int getYears(Date appReceivedDate, Date bizStartDate) {
		LOG.info("Inside DateDiff");
		int bapYrsInBus = 0;
		int monthDiff = 0;
		int appReceiveYear = 0;
		int bizStartYear = 0;
		int appReceiveMonth = 0;
		int bizStartMonth = 0;
		int appReceiveDay = 0;
		int bizStartDay = 0;
		int p1, p2 = 0;
		int monthSpan, yearSpan, daySpan = 0;

		// Comparing two dates
		if (appReceivedDate.compareTo(bizStartDate) == 1) {
			// Application received date is greater then business start date...
			// working further
			Calendar appReceivedCal = Calendar.getInstance();
			appReceivedCal.setTime(appReceivedDate);
			Calendar bizStartCal = Calendar.getInstance();
			bizStartCal.setTime(bizStartDate);

			appReceiveYear = appReceivedCal.get(Calendar.YEAR);
			bizStartYear = bizStartCal.get(Calendar.YEAR);
			appReceiveMonth = appReceivedCal.get(Calendar.MONTH) + 1;
			bizStartMonth = bizStartCal.get(Calendar.MONTH) + 1;
			appReceiveDay = appReceivedCal.get(Calendar.DAY_OF_MONTH);
			bizStartDay = bizStartCal.get(Calendar.DAY_OF_MONTH);
			p1 = 12 - bizStartMonth;
			p2 = appReceiveMonth;
			monthSpan = p1 + p2;
			yearSpan = appReceiveYear - bizStartYear - 1;
			daySpan = appReceiveDay - bizStartDay;
			if (daySpan < 0) {
				monthDiff = (yearSpan * 12) + monthSpan - 1;
				bapYrsInBus = monthDiff / 12;
			} else {
				monthDiff = (yearSpan * 12) + monthSpan;
				bapYrsInBus = monthDiff / 12;
			}
		} else {
			// Business start date is either equal or greater then application
			// received date
			monthDiff = 0;
			bapYrsInBus = 0;
		}
		return bapYrsInBus;
	}

	public static String dateFormatter(Date date, String dateFormate) {
		if (date != null && dateFormate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
			return dateFormat.format(date);
		}
		return "";
	}

	public AttributeChoice getAttributeChoiceByKey(String param) {
		return (AttributeChoice) LookupService.getResult(
				"AttributeChoice.byKey", "key", param);
	}

	public Country getDefaultCountry() {
		return (Country) LookupService.getResult("Country.byIsoCode",
				"isoCode", "US");
	}

	public WorkflowStatus getWorkflowStatusKey(String param) {
		return (WorkflowStatus) LookupService.getResult(
				"WorkflowStatus.byStatusKey", "statusKey", param);

	}

	/**
	 * This method is used to get number of years between two dates. Null check
	 * is also implemented in this method.
	 * 
	 * @param firstDate
	 *            is Date type.
	 * @param secondDate
	 *            is Date type.
	 * @return int >= 0 as number of days otherwise -1(in case of any null
	 *         parameter);
	 */
	public static long yesrsDifferenceInDates(Date firstDate, Date secondDate) {
		long noOfYears = -1;
		if (firstDate != null && secondDate != null) {
			Calendar firstCal = Calendar.getInstance();
			// set first date
			firstCal.setTime(firstDate);
			Calendar secondCal = Calendar.getInstance();
			// set second date
			secondCal.setTime(secondDate);
			// Convert date into millisecond
			long firstMilliSecond = firstCal.getTimeInMillis();
			long secondMilliSecond = secondCal.getTimeInMillis();
			// Calculate difference in millisecond
			long differMilliSecond = firstMilliSecond - secondMilliSecond;
			// Convert millisecond into second
			long differSecond = differMilliSecond / 1000;
			// Convert second into minutes
			long differMinutes = differSecond / 60;
			// Convert minutes into hours
			long differHours = differMinutes / 60;
			// Convert hours into days
			long differDays = differHours / 24;
			// Convert days into month
			long differMonth = differDays / 30;
			// Convert month into year
			noOfYears = differMonth / 12;
		}
		return noOfYears;
	}

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "26-07-2015 10:20:56";
		Date date;
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, Calendar.JUNE);
			System.out.println(cal.getTime());
			
			cal.add(Calendar.MONTH, 12);
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
			
			System.out.println(cal.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("Exception occured while parsing date.", e);
		}

	}
	
	public static Date addYearsToDate(Date date, int years){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, years*12);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
		return cal.getTime();
	}
	
	public static Date addMonthsToDate(Date date, int months){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
		return cal.getTime();
	}

	public static Date addDaysToDate(Date date, int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	/**
	 * This method is used to get number of days between two dates. Null check
	 * is also implemented in this method.
	 * 
	 * @param firstDate
	 *            is Date type.
	 * @param secondDate
	 *            is Date type.
	 * @return int > 0 as number of days if first date is greater than
	 *         secondDate and otherwise -1(in case of any null parameter);
	 */
	public static int calculateDaysDifferenceInDates(Date firstDate,
			Date secondDate) {
		int diffDays = Days.daysBetween(new DateMidnight(firstDate),
				new DateMidnight(secondDate)).getDays();

		return diffDays;

	}

	/**
	 * This method adds two BigDecimal numbers
	 * 
	 * @param augend
	 * @param addend
	 * @param scale
	 * @return
	 */
	public static BigDecimal add(BigDecimal augend, BigDecimal addend, int scale) {
		MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		BigDecimal result = BigDecimal.ZERO;
		if (augend != null && addend != null) {
			augend = augend.setScale(scale, context.getRoundingMode());
			addend = addend.setScale(scale, context.getRoundingMode());
			result = augend.add(addend, context);
		}
		return result;
	}

	/**
	 * This method finds difference between two BigDecimal numbers
	 * 
	 * @param lhs
	 * @param subtrahend
	 * @param scale
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal lhs, BigDecimal subtrahend,
			int scale) {
		MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		BigDecimal result = BigDecimal.ZERO;
		if (lhs != null && subtrahend != null) {
			lhs = lhs.setScale(scale, context.getRoundingMode());
			subtrahend = subtrahend.setScale(scale, context.getRoundingMode());
			result = lhs.subtract(subtrahend, context);
		}
		return result;
	}

	public static BigDecimal calculatePercent(BigDecimal amt,
			BigDecimal percent, int scale) {
		MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		BigDecimal result = BigDecimal.ZERO;
		amt = amt.setScale(scale, context.getRoundingMode());
		percent = percent.setScale(scale, context.getRoundingMode());
		BigDecimal calc = percent.multiply(amt, context);
		result = calc.divide(new BigDecimal("100"), context);
		return result;
	}
	
	public static BigDecimal multiply(BigDecimal arg1,
			BigDecimal arg2, int scale) {
		MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		arg1 = arg1.setScale(scale, context.getRoundingMode());
		arg2 = arg2.setScale(scale, context.getRoundingMode());
		BigDecimal calc = arg1.multiply(arg2, context);
		return calc;
	}
	
	public static BigDecimal divide(BigDecimal arg1,
			BigDecimal arg2, int scale) {
		MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		arg1 = arg1.setScale(scale, context.getRoundingMode());
		arg2 = arg2.setScale(scale, context.getRoundingMode());
		BigDecimal calc = arg1.divide(arg2, context);
		return calc;
	}
	
	public static BigDecimal formatBigDecimal(BigDecimal amt) {
		String s[] = new java.text.DecimalFormat("#0.0000").format(amt).split("\\.");
		if(s.length>1 && s[1].indexOf("0000") != -1){
			return new BigDecimal(new java.text.DecimalFormat("#0.00").format(amt));
		}
		return new BigDecimal(new java.text.DecimalFormat("#0.000").format(amt));
	}
	
	public static BigDecimal getMax(BigDecimal b1, BigDecimal b2){
		if(b1 != null && b2 != null){
			if(b1.compareTo(b2) >= 0){
				return b1;
			}
			return b2;
		}
		return null;
	}
	
	public static Long convertDateToTimezone(Date date, String timeZone){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
		DateFormatter dateFormatter = new DateFormatter(TimeZone.getTimeZone(timeZone));
		//String dateFormat = dateFormatter.format(date, "MM/dd/yyyy HH:mm:ss Z");
		LocalDateTime dateTime = new LocalDateTime(date,DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZone)));
		DateTime d = dateTime.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZone)));
		return d.getMillis();
		//dateTime = dateTime.parse();
		/*try {
			return sdf.parse(dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		//return null;
	}
}
