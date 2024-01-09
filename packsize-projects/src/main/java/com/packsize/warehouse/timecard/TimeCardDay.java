package com.packsize.warehouse.timecard;

public class TimeCardDay {
	
	private String projectName;
	private int sun;
	private int mon;
	private int tue;
	private int wed;
	private int thur;
	private int fri;
	private int sat;
	
	
	public int getSun() {
		return sun;
	}
	public void setSun(int sun) {
		this.sun = sun;
	}
	public int getMon() {
		return mon;
	}
	public void setMon(int mon) {
		this.mon = mon;
	}
	public int getTue() {
		return tue;
	}
	public void setTue(int tue) {
		this.tue = tue;
	}
	public int getWed() {
		return wed;
	}
	public void setWed(int wed) {
		this.wed = wed;
	}
	public int getThur() {
		return thur;
	}
	public void setThur(int thur) {
		this.thur = thur;
	}
	public int getFri() {
		return fri;
	}
	public void setFri(int fri) {
		this.fri = fri;
	}
	public int getSat() {
		return sat;
	}
	public void setSat(int sat) {
		this.sat = sat;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@Override
	public String toString() {
		return "[" + projectName + "," + sun + "," + mon + "," + tue + ","
				+ wed + "," + thur + "," + fri + "," + sat + "]next";
	}
	
	
	
}
