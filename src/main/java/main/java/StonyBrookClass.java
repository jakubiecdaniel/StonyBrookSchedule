package main.java;

import java.util.TimeZone;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

public class StonyBrookClass {
	private static String endString ="";
	public static String GetendString() {
		return endString;
	}
	
	
	
	static String weekStart;
	public static void setEndString() {
		//yyyymmdd
		String[] w = weekStart.split("/");
		String y = w[2];
		int endMonth = Integer.valueOf(w[0]) + 4;
		int endDay = Integer.valueOf(w[1]);
		String m = endMonth < 10 ? "0"+Integer.toString(endMonth) : Integer.toString(endMonth);
		String d = endDay < 10 ? "0"+Integer.toString(endDay) : Integer.toString(endDay);
		endString+= y+ m + d;
		
	}
	
	
	int dayNum;
	private boolean[] day = new boolean[7];
	private String classAndSection;
	private String description;
	private String timeSpan;
	private String location;
	
	
	private EventDateTime start = new EventDateTime();
	private EventDateTime end = new EventDateTime();
	
	
	
	public EventDateTime getStart() {
		return start;
	}
	public EventDateTime getEnd() {
		return end;
	}
	public String getClassAndSection() {
		return classAndSection;
	}
	public String getDescription() {
		return description;
	}
	public String getLocation() {
		return location;
	}
	public String getDay() {
		return day[0] ? "Monday" : day[1] ? "Tuesday" : day[2] ? "Wednesday" : day[3] ? "Thursday" : day[4] ? "Friday" : day[5] ? "Saturday" : day[6] ? "Sunday" : null;
	}
	public StonyBrookClass(String classAndSection,String description,String timeSpan,String location,int day) {
		this.classAndSection = classAndSection;
		this.description = description;
		this.timeSpan = timeSpan;
		this.location = location;
		this.day[day] = true;	
		dayNum =day;
		
		
		// 2:30PM - 3:50PM
		//1/28/2019 week start
		/* 1996-12-19T16:39:57-05:00
		 * */
		
		String[] hourArray = timeSpan.split(" ");
		String startHourMinutes = hourArray[0];
		String endHourMinutes = hourArray[2];
		String startTimeString = "";
		String endTimeString = null;
		if(startHourMinutes.contains("PM")) {
			int hour = Integer.valueOf(startHourMinutes.split(":")[0]);
			hour+=12;
			int minutes = Integer.valueOf(startHourMinutes.split(":")[1].replace("PM", ""));
			String s_minutes;
			if(minutes < 10) {
				s_minutes = "0" + Integer.toString(minutes);
			}
			else {
				s_minutes = Integer.toString(minutes);
			}
			startTimeString = Integer.toString(hour)+":"+s_minutes+":00-05:00";
		}
		else if(startHourMinutes.contains("AM")) {
			int hour = Integer.valueOf(startHourMinutes.split(":")[0]);
			int minutes = Integer.valueOf(startHourMinutes.split(":")[1].replace("AM", ""));
			String s_minutes;
			if(minutes < 10) {
				s_minutes = "0" + Integer.toString(minutes);
			}
			else {
				s_minutes = Integer.toString(minutes);
			}
			startTimeString = Integer.toString(hour)+":"+s_minutes+":00-05:00";
		}
		
		
	
		if(endHourMinutes.contains("PM")) {
			int hour = Integer.valueOf(endHourMinutes.split(":")[0]);
			hour+=12;
			int minutes = Integer.valueOf(endHourMinutes.split(":")[1].replace("PM", ""));
			String s_minutes;
			if(minutes < 10) {
				s_minutes = "0" + Integer.toString(minutes);
			}
			else {
				s_minutes = Integer.toString(minutes);
			}
			
			endTimeString = Integer.toString(hour)+":"+s_minutes+":00-05:00";
		}
		else if(endHourMinutes.contains("AM")) {
			int hour = Integer.valueOf(endHourMinutes.split(":")[0]);
			int minutes = Integer.valueOf(endHourMinutes.split(":")[1].replace("AM", ""));
			String s_minutes;
			if(minutes < 10) {
				s_minutes = "0" + Integer.toString(minutes);
			}
			else {
				s_minutes = Integer.toString(minutes);
			}
			
			endTimeString = Integer.toString(hour)+":"+minutes+":00-05:00";
		}
		
		String timeString[] = timeSpan.split(" ");
		String[] weekArray = weekStart.split("/");
		int year = Integer.valueOf(weekArray[2]);
		
		int month = Integer.valueOf(weekArray[0]);
		String s_month;
		if(month < 10) {
			s_month = "0"+Integer.toString(month);
		}
		else {
			s_month = Integer.toString(month);
		}
		
		
		dayNum = dayNum + Integer.valueOf(weekArray[1]);
		
		String date = Integer.toString(year)+"-"+s_month+"-"+Integer.toString(dayNum)+"T"+startTimeString;
		DateTime startDateTime = new DateTime(date);
		
		start.setTimeZone("America/New_York");
		System.out.println(date);
		start.setDateTime(startDateTime);
		
		
		date = Integer.toString(year)+"-"+s_month+"-"+Integer.toString(dayNum)+"T"+endTimeString;
		DateTime endDateTime = new DateTime(date);
		
		
		end.setTimeZone("America/New_York");
		end.setDateTime(endDateTime);
		
		
		
	}
	public String toString() {
		return classAndSection +  " " + description + " " + timeSpan + " " + location + " " + getDay();
	}
	
	
	
}
