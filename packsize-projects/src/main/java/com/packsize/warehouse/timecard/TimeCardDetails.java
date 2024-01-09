package com.packsize.warehouse.timecard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeCardDetails {
	private String user;
	private Map<String, List<LocalDate>> timeCards;
	private List<TimeCardDay> timeCardDayList;
	private boolean submitForApproval;
	private boolean approved;
	
	private String userKey;
	private int weekID;
	private int sunTotal;
	private int monTotal;
	private int tueTotal;
	private int wedTotal;
	private int thurTotal;
	private int friTotal;
	private int satTotal;
	private int weekTotal;
	
	private String sunday;
	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	
	public TimeCardDetails() {
		setTimeCards(new HashMap<String, List<LocalDate>>());
		setTimeCardDayList(new ArrayList<TimeCardDay>());
	}
	
	public int getSunTotal() {
		return sunTotal;
	}

	public void setSunTotal(int sunTotal) {
		this.sunTotal = sunTotal;
	}

	public int getMonTotal() {
		return monTotal;
	}

	public void setMonTotal(int monTotal) {
		this.monTotal = monTotal;
	}

	public int getTueTotal() {
		return tueTotal;
	}

	public void setTueTotal(int tueTotal) {
		this.tueTotal = tueTotal;
	}

	public int getWedTotal() {
		return wedTotal;
	}

	public void setWedTotal(int wedTotal) {
		this.wedTotal = wedTotal;
	}

	public int getThurTotal() {
		return thurTotal;
	}

	public void setThurTotal(int thurTotal) {
		this.thurTotal = thurTotal;
	}

	public int getFriTotal() {
		return friTotal;
	}

	public void setFriTotal(int friTotal) {
		this.friTotal = friTotal;
	}

	public int getSatTotal() {
		return satTotal;
	}

	public void setSatTotal(int satTotal) {
		this.satTotal = satTotal;
	}

	public Map<String, List<LocalDate>> getTimeCards() {
		return timeCards;
	}
	public void setTimeCards(Map<String, List<LocalDate>> timeCards) {
		this.timeCards = timeCards;
	}

	public List<TimeCardDay> getTimeCardDayList() {
		return timeCardDayList;
	}

	public void setTimeCardDayList(List<TimeCardDay> timeCardDayList) {
		this.timeCardDayList = timeCardDayList;
	}

	public int getWeekTotal() {
		return weekTotal;
	}

	public void setWeekTotal(int weekTotal) {
		this.weekTotal = weekTotal;
	}

	public int getWeekID() {
		return weekID;
	}

	public void setWeekID(int weekID) {
		this.weekID = weekID;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public boolean isSubmitForApproval() {
		return submitForApproval;
	}

	public void setSubmitForApproval(boolean submitForApproval) {
		this.submitForApproval = submitForApproval;
	}

	@Override
	public String toString() {
		return "TimeCardDetails{}" + timeCards +"{}" + timeCardDayList +"{}"
				+ userKey +"{}" + weekID +"{}" + sunTotal +"{}" + monTotal +"{}"
				+ tueTotal +"{}" + wedTotal +"{}" + thurTotal +"{}" + friTotal
				+ "{}" + satTotal +"{}" + weekTotal +"{}";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	
}
