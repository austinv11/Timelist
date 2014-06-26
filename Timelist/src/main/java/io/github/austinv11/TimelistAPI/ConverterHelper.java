package io.github.austinv11.TimelistAPI;

public class ConverterHelper {
	private static double weeksPerMonth = 4.34812141;
	public static String[] removeElements(String args[], int elementsToRemove){
		String newArray[] = new String[args.length - elementsToRemove];
		int j = 0;
		for (int i = 0; i < args.length; i++){
			if (i >= elementsToRemove){
				newArray[j] = args[i];
				j++;
			}
		}
		return newArray;
	}
	public static int getTotalTimes(String...args){
		int total = 0;
		for (int i = 0; i < args.length; i++){
			if (args[i].toLowerCase().contains("y")){
				total = total + convertYears(args[i]);
			}else if (args[i].toLowerCase().contains("mon")){
				total = total + convertMonths(args[i]);
			}else if (args[i].toLowerCase().contains("w")){
				total = total + convertWeeks(args[i]);
			}else if (args[i].toLowerCase().contains("d")){
				total = total + convertDays(args[i]);
			}else if (args[i].toLowerCase().contains("h")){
				total = total + convertHours(args[i]);
			}else if (args[i].toLowerCase().contains("min")){
				total = total + convertMinutes(args[i]);
			}
		}
		return total;
	}
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