package com.packsize.warehouse.timecard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeCardDetails {
	private Map<String, List<LocalDate>> timeCards;
	private List<TimeCardDay> timeCardDayList;
	
	private int sunTotal;
	private int monTotal;
	private int tueTotal;
	private int wedTotal;
	private int thurTotal;
	private int friTotal;
	private int satTotal;
	private int weekTotal;
	
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

	@Override
	public String toString() {
		return "TimeCardDetails [timeCards=" + timeCards + ", timeCardDayList=" + timeCardDayList + ", sunTotal="
				+ sunTotal + ", monTotal=" + monTotal + ", tueTotal=" + tueTotal + ", wedTotal=" + wedTotal
				+ ", thurTotal=" + thurTotal + ", friTotal=" + friTotal + ", satTotal=" + satTotal + ", weekTotal="
				+ weekTotal + "]";
	}
	
	
}
