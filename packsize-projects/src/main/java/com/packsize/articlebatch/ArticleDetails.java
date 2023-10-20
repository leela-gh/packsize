package com.packsize.articlebatch;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

public class ArticleDetails {
	
	private String machine;
	private boolean labelRequired = false;
	private String printHeaders;
	private String printData;
	private UploadedFile uploadFile;
	private StreamedContent file;
	private String maxZfold = "0";
	private boolean updateDims;
	private String addToL = "0";
	private String addToW = "0";
	private String addToH = "0";
	private String addFT = "0";
	private List<Design> emDesigns = new ArrayList<Design>();
	private List<Design> iqDesigns = new ArrayList<Design>();
	private List<Design> x4Designs = new ArrayList<Design>();
	private List<Design> designs;
	private boolean corrugateQualityRequired = false;
	private String corrugateQuality;
	
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public boolean isLabelRequired() {
		return labelRequired;
	}
	public void setLabelRequired(boolean labelRequired) {
		this.labelRequired = labelRequired;
	}
	public String getPrintHeaders() {
		return printHeaders;
	}
	public void setPrintHeaders(String printHeaders) {
		this.printHeaders = printHeaders;
	}
	public String getPrintData() {
		return printData;
	}
	public void setPrintData(String printData) {
		this.printData = printData;
	}
	public List<Design> getEmDesigns() {
		return emDesigns;
	}
	public void setEmDesigns(List<Design> emDesigns) {
		this.emDesigns = emDesigns;
	}
	public List<Design> getIqDesigns() {
		return iqDesigns;
	}
	public void setIqDesigns(List<Design> iqDesigns) {
		this.iqDesigns = iqDesigns;
	}
	public List<Design> getDesigns() {
		return designs;
	}
	public void setDesigns(List<Design> designs) {
		this.designs = designs;
	}
	public UploadedFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(UploadedFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public StreamedContent getFile() {
		return file;
	}
	public void setFile(StreamedContent file) {
		this.file = file;
	}
	public boolean isCorrugateQualityRequired() {
		return corrugateQualityRequired;
	}
	public void setCorrugateQualityRequired(boolean corrugateQualityRequired) {
		this.corrugateQualityRequired = corrugateQualityRequired;
	}
	public String getMaxZfold() {
		return maxZfold;
	}
	public void setMaxZfold(String maxZfold) {
		this.maxZfold = maxZfold;
	}
	public List<Design> getX4Designs() {
		return x4Designs;
	}
	public void setX4Designs(List<Design> x4Designs) {
		this.x4Designs = x4Designs;
	}
	public String getAddToL() {
		return addToL;
	}
	public void setAddToL(String addToL) {
		this.addToL = addToL;
	}
	public String getAddToW() {
		return addToW;
	}
	public void setAddToW(String addToW) {
		this.addToW = addToW;
	}
	public String getAddToH() {
		return addToH;
	}
	public void setAddToH(String addToH) {
		this.addToH = addToH;
	}
	public String getAddFT() {
		return addFT;
	}
	public void setAddFT(String addFT) {
		this.addFT = addFT;
	}
	public boolean isUpdateDims() {
		return updateDims;
	}
	public void setUpdateDims(boolean updateDims) {
		this.updateDims = updateDims;
	}
	public String getCorrugateQuality() {
		return corrugateQuality;
	}
	public void setCorrugateQuality(String corrugateQuality) {
		this.corrugateQuality = corrugateQuality;
	}
	

}
