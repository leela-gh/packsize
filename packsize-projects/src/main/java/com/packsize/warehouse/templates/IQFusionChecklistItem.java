package com.packsize.warehouse.templates;

public class IQFusionChecklistItem {
	
	private Integer parentId;
	private Integer id;
	private String name;
	private long hours;
	private boolean enable;
	private boolean continueItem;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getHours() {
		return hours;
	}
	public void setHours(long hours) {
		this.hours = hours;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isContinueItem() {
		return continueItem;
	}
	public void setContinueItem(boolean continueItem) {
		this.continueItem = continueItem;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	
	

}
