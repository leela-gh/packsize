package com.packsize.tracking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.tracking.email.SendEmailTLSUtil;

@Component
@Scope(value = "session")
public class TrackingPOComponent {
	
	private TrackingPODetails poDetails;
	private Map<String, String> reasonsDropDown;
	private Map<String, String> emailsDropDown;
	
	@PostConstruct
	void init() {
		  initialSetup(); 
		  reasons();
		  emails();
	}
	
	private void reasons() {
	  reasonsDropDown = new HashMap<>(); 
	  reasonsDropDown.put("Part", "Part");
	  reasonsDropDown.put("Qty", "Qty");
	  reasonsDropDown.put("Quality", "Quality");
	  reasonsDropDown.put("Unit of Meas,", "Unit of Meas,");
	  reasonsDropDown.put("Others", "Others");
	}
	
	private void emails() {
		emailsDropDown = new HashMap<>(); 
		emailsDropDown.put("Leela Yallabandi", "leela.yallabandi@packsize.com");
		emailsDropDown.put("Brian Powell", "brian.powell@packsize.com");
		emailsDropDown.put("Donnie Sinkhorn", "donnie.sinkhorn@packsize.com");
	}
		
	private void initialSetup() {
		  setPoDetails(new TrackingPODetails());
		  poDetails.setSearchPanel(false);
		  poDetails.setPoPanel(false);
		  dataSetup();
	}
	
	protected void reSet() {
		initialSetup();
	}
	
	protected void retriveData() {
		if(poDetails.getId().equalsIgnoreCase("1234")) {
			poDetails.setSearchPanel(false);
			poDetails.setPoPanel(true);
		}else {
			poDetails.setSearchPanel(true);
			poDetails.setPoPanel(false);
		}
	}
	
	private void dataSetup() {
		poDetails.getLineItems().clear();
		
		for(int i=1;i<=5;i++) {
			LineItem item = new LineItem();
			item.setLine(String.valueOf(i));
			item.setItem(String.valueOf(i * 10));
			item.setPlant(String.valueOf(i + 1000));
			item.setStoreLocation(String.valueOf(i + 1000));
			item.setMaterial(String.valueOf(i + 301542));
			item.setMatShortText("GL2 St Strip Brush Retrofit Kit");
			item.setPoQuantity(String.valueOf(i * 5));
			item.setReceived(String.valueOf(i));
			item.setQtyInUne(String.valueOf((i * 5) - i));
			
			poDetails.getLineItems().add(item);
		}
	}
	
	protected boolean sendEmail(List<LineItem> dispList) {
		return SendEmailTLSUtil.sendEmail(getPoDetails(), dispList);
	}
	
	public TrackingPODetails getPoDetails() {
		return poDetails;
	}
	public void setPoDetails(TrackingPODetails poDetails) {
		this.poDetails = poDetails;
	}

	public Map<String, String> getReasonsDropDown() {
		return reasonsDropDown;
	}

	public void setReasonsDropDown(Map<String, String> reasonsDropDown) {
		this.reasonsDropDown = reasonsDropDown;
	}
	
	public Map<String, String> getEmailsDropDown() {
		return emailsDropDown;
	}

	public void setEmailsDropDown(Map<String, String> emailsDropDown) {
		this.emailsDropDown = emailsDropDown;
	}
}