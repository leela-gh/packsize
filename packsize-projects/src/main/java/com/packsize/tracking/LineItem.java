package com.packsize.tracking;

import org.primefaces.model.file.UploadedFiles;

public class LineItem {
	
	private String line;
	private String item;
	private String plant;
	private String storeLocation;
	private String material;
	private String matShortText;
	private String poQuantity;
	private String received;
	private String qtyInUne;
	
	private String label;
	private boolean ok;
	private boolean disc;
	private String reasonSelection;
	private String reasonComment;
	private UploadedFiles files;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public boolean isDisc() {
		return disc;
	}

	public void setDisc(boolean disc) {
		this.disc = disc;
	}

	public String getReasonSelection() {
		return reasonSelection;
	}

	public void setReasonSelection(String reasonSelection) {
		this.reasonSelection = reasonSelection;
	}

	public String getReasonComment() {
		return reasonComment;
	}

	public void setReasonComment(String reasonComment) {
		this.reasonComment = reasonComment;
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMatShortText() {
		return matShortText;
	}

	public void setMatShortText(String matShortText) {
		this.matShortText = matShortText;
	}

	public String getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(String poQuantity) {
		this.poQuantity = poQuantity;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getQtyInUne() {
		return qtyInUne;
	}

	public void setQtyInUne(String qtyInUne) {
		this.qtyInUne = qtyInUne;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

}
