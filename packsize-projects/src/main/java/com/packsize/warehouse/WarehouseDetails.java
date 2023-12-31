package com.packsize.warehouse;

import org.primefaces.model.StreamedContent;

public class WarehouseDetails {
	
	private Long assetID;
	private String machineType;
	private String name;
	private String status = "In Progress";
	private Long totalHrs;
	private StreamedContent file;
	
	public Long getAssetID() {
		return assetID;
	}
	public void setAssetID(Long assetID) {
		this.assetID = assetID;
	}
	public String getMachineType() {
		return machineType;
	}
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getTotalHrs() {
		return totalHrs;
	}
	public void setTotalHrs(Long totalHrs) {
		this.totalHrs = totalHrs;
	}
	public StreamedContent getFile() {
		return file;
	}
	public void setFile(StreamedContent file) {
		this.file = file;
	}
}
