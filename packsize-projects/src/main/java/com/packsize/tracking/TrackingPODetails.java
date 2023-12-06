package com.packsize.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.file.UploadedFiles;

public class TrackingPODetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private boolean searchPanel;
	private boolean poPanel;
	private String poComments;
	private UploadedFiles files;
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	
	public TrackingPODetails() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSearchPanel() {
		return searchPanel;
	}

	public void setSearchPanel(boolean searchPanel) {
		this.searchPanel = searchPanel;
	}

	public boolean isPoPanel() {
		return poPanel;
	}

	public void setPoPanel(boolean poPanel) {
		this.poPanel = poPanel;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public String getPoComments() {
		return poComments;
	}

	public void setPoComments(String poComments) {
		this.poComments = poComments;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

}
