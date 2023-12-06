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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;
import com.packsize.login.Login;
import com.packsize.warehouse.google.GoogleSheetsUtil;

@Component
@Scope(value = "session")
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
	
	private void initialSetup() {
		logger.info("In initialSetup()");
		
		setWarehouseDetailsList(GoogleSheetsUtil.readWarehouseDetailsFromSheets(login.getUser()));
		setWarehouseDetails(new WarehouseDetails());
	}
	
	public void reSet() {
		logger.info("In reSet()");
		initialSetup();
		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Page reset completed."));
	}
	
	public void submit() {
		logger.info("In submit()");
		
		GoogleSheetsUtil.writeWarehouseDetailsToSheets(login, warehouseDetails);
		navigateToCheckListPage();
	}
	
	public void navigateToCheckListPage() {
		logger.info("In navigateToCheckListPage()");
		try {
			if(!warehouseDetailsList.isEmpty()) {
				setWarehouseDetails(warehouseDetailsList.get(0));
			}
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect(context.getRequestContextPath() + "/warehousepages/warehouseChecklist.xhtml");
		} catch (IOException e) {
			PackSizeLogger.error(e.getMessage());
		}
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
