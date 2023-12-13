package com.packsize.warehouse;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.packsize.login.Login;
import com.packsize.warehouse.google.GoogleSheetsUtil;
import com.packsize.warehouse.templates.IQFusionTemplate;

@Component
@SessionScope
public class WarehouseComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private Login login;
	
	private WarehouseDetails warehouseDetails;
	private List<WarehouseDetails> warehouseDetailsList;
	
	@PostConstruct
    private void init() {
		initialSetup();
	}
	
	protected void initialSetup() {
		logger.info("In initialSetup()");
		
		setWarehouseDetailsList(GoogleSheetsUtil.readWarehouseDetailsFromSheets(login.getUser(), false));
		setWarehouseDetails(new WarehouseDetails());
		calculateTotals();
	}
	
	private void calculateTotals() {
		logger.info("In calculateTotals()");
		
		for(WarehouseDetails obj : warehouseDetailsList) {
			IQFusionTemplate template = GoogleSheetsUtil.readDataFromSheets(obj.getName(), obj.getAssetID().toString());
			obj.setTotalHrs(template.getTotalHrsPrepToRun() + template.getTotalHrsImagingThePanel());
		}
	}
	
	public void reSet() {
		logger.info("In reSet()");
		setWarehouseDetails(new WarehouseDetails());
		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Page reset completed."));
	}
	
	public void submit() {
		logger.info("In submit()");
		
		GoogleSheetsUtil.writeWarehouseDetailsToSheets(login, warehouseDetails);
		navigateToCheckListPage();
	}
	
	private void navigateToCheckListPage() {
		logger.info("In navigateToCheckListPage()");
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect(context.getRequestContextPath() + "/warehousepages/warehouseChecklist.xhtml");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void callFromAssetsTable(WarehouseDetails asset) {
		logger.info("In callFromAssetsTable()");
		
		setWarehouseDetails(asset);
		navigateToCheckListPage();	
	}
	
	public void assetComplete(WarehouseDetails asset, boolean complete) {
		logger.info("In assetComplete()");
		
		GoogleSheetsUtil.updateWarehouseDetailsToSheets(login, asset);
		setWarehouseDetailsList(GoogleSheetsUtil.readWarehouseDetailsFromSheets(login.getUser(), complete));
	}
	
	public WarehouseDetails getWarehouseDetails() {
		return warehouseDetails;
	}

	public void setWarehouseDetails(WarehouseDetails warehouseDetails) {
		this.warehouseDetails = warehouseDetails;
	}

	public List<WarehouseDetails> getWarehouseDetailsList() {
		return warehouseDetailsList;
	}

	public void setWarehouseDetailsList(List<WarehouseDetails> warehouseDetailsList) {
		this.warehouseDetailsList = warehouseDetailsList;
	}
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
}
