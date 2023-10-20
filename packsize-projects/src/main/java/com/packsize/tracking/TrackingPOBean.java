package com.packsize.tracking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;

@Component
@Scope(value = "session")
public class TrackingPOBean implements Serializable{
	
	@Autowired
	private TrackingPOComponent trackingPOComponent;
	
	public void seachPO() {
		PackSizeLogger.info("In seachPO()");
		
		if(StringUtil.isNotBlank(trackingPOComponent.getPoDetails().getId())) {
			trackingPOComponent.retriveData();
		}else {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter PO id.")); 
			PackSizeLogger.error("PO is empty.");
		}
	}
	
	public void reSet() {
		PackSizeLogger.info("In reSet()");
		trackingPOComponent.reSet();
	}
	
	private void sendEmail(List<LineItem> dispList) {
		PackSizeLogger.info("In sendEmail()");
		
		PackSizeLogger.info("Sending Email to :" + trackingPOComponent.getPoDetails().getEmail());
		if(StringUtil.isNotBlank(trackingPOComponent.getPoDetails().getEmail())) {
			if(trackingPOComponent.sendEmail(dispList)) {
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Email Sent.")); 
			}else {
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Not able to send email, Please check with Admin."));
			}
		}else {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Select email.")); 
			PackSizeLogger.error("Select email.");
		}
	}
	
	public void submit() {
		PackSizeLogger.info("In submit()");
		
		if(trackingPOComponent.getPoDetails().isSearchPanel()) {
			sendEmail(null);
		}else if(trackingPOComponent.getPoDetails().isPoPanel()){
			List<LineItem> dispList = trackingPOComponent.getPoDetails().getLineItems().stream().filter(c -> c.isDisc()).collect(Collectors.toList());
			List<LineItem> okList = trackingPOComponent.getPoDetails().getLineItems().stream().filter(c -> c.isOk()).collect(Collectors.toList());
			
			if(!dispList.isEmpty()) {
				sendEmail(dispList);
			}
			
			if(!okList.isEmpty()) {
				exportToText(okList);
			}
		}
		
	}
	
	private void exportToText(List<LineItem> okList) {
		PackSizeLogger.info("In exportToText()");
		
		StringBuffer data = new StringBuffer(); 
		data.append("Line, Item, Plant, Stor.Loca, Material, Mat.Short Text, PO Quantity, Received, Qty in UnE, Label, OK, Discp., Reason, Reason Comments\n");
        try {
        	BufferedWriter f_writer = new BufferedWriter(new FileWriter("C:\\JAVA\\PO_test\\sample.txt"));
            for(LineItem item : okList) {
            	data.append(item.getLine()+", "+item.getItem()+", "+item.getPlant()+", "+item.getStoreLocation()+", "+item.getMaterial()+", "+item.getMatShortText()+", "+item.getPoQuantity()+", "+item.getReceived()+", "+item.getQtyInUne()+", "+item.getLabel()+", "+item.isOk()+", "+item.isDisc()+", "+item.getReasonSelection()+", "+item.getReasonComment()+"\n");
            }
            f_writer.write(data.toString());
            PackSizeLogger.info("File is created successfully with the content.");
            f_writer.close();
        }
        catch (IOException e) {
        	PackSizeLogger.error(e.getMessage());
        }
	}

	public TrackingPOComponent getTrackingPOComponent() {
		return trackingPOComponent;
	}

	public void setTrackingPOComponent(TrackingPOComponent trackingPOComponent) {
		this.trackingPOComponent = trackingPOComponent;
	}
	
	
}