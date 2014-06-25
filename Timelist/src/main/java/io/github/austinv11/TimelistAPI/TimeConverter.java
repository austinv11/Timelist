package io.github.austinv11.TimelistAPI;

public class TimeConverter {
	private static double weeksPerMonth = 4.34812141;
	public static int convertYears(String input){
		input = input.replace("y", "").replace("Y", "");
		int years = Integer.parseInt(input);
		int months = yearsToMonths(years);
		int weeks = monthsToWeeks(months);
		int days = weeksToDays(weeks);
		int hours = daysToHours(days);
		int min = hoursToMinutes(hours);
		return min;
	}
	public static int convertMonths(String input){
		input = input.toLowerCase().replace("mon", "");
		int months = Integer.parseInt(input);
		int weeks = monthsToWeeks(months);
		int days = weeksToDays(weeks);
		int hours = daysToHours(days);
		int min = hoursToMinutes(hours);
		return min;
	}
	public static int convertWeeks(String input){
		input = input.toLowerCase().replace("w", "");
		int weeks = Integer.parseInt(input);
		int days = weeksToDays(weeks);
		int hours = daysToHours(days);
		int min = hoursToMinutes(hours);
		return min;
	}
	public static int convertDays(String input){
		input = input.toLowerCase().replace("d", "");
		int days = Integer.parseInt(input);
		int hours = daysToHours(days);
		int min = hoursToMinutes(hours);
		return min;
	}
	public static int convertHours(String input){
		input = input.toLowerCase().replace("h", "");
		int hours = Integer.parseInt(input);
		int min = hoursToMinutes(hours);
		return min;
	}
	public static int convertMinutes(String input){
		input = input.toLowerCase().replace("min", "");
		int min = Integer.parseInt(input);
		return min;
	}
	private static int yearsToMonths(int years){
		int months = years * 12;
		return months;
	}
	private static int monthsToWeeks(int months){
		int weeks = (int) Math.round((double) months * weeksPerMonth);
		return weeks;
	}
	private static int weeksToDays(int weeks){
		int days = weeks * 7;
		return days;
	}
	private static int daysToHours(int days){
		int hours = days * 24;
		return hours;
	}
	private static int hoursToMinutes(int hours){
		int min = hours * 60;
		return min;
	}
}