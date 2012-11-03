package com.tearoffcalendar.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Card {
	
	public static final String DATE_FORMAT = "d MMM yyyy";

	public void increment() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--
		date = cal.getTime();
	}
	
	public void decrement() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1); // <--
		date = cal.getTime();
	}
	
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return new String(dateFormat.format(date));
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDateFromString(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}		
	}
	
	public int getDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	private Date date;

}
