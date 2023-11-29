package com.packsize.warehouse;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.poi.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;

@Component
@Scope(value = "session")
public class WarehouseComponent {
	
	private WarehouseDetails warehouseDetails;
	
	@PostConstruct
    private void init() {
		initialSetup();
	}
	
	private void initialSetup() {
		PackSizeLogger.info("In initialSetup()");
		
		setWarehouseDetails(new WarehouseDetails());
	}
	
	public void reSet() {
		PackSizeLogger.info("In reSet()");
		initialSetup();
		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Page reset completed."));
	}
	
	public void submit() {
		PackSizeLogger.info("In submit()");
		
		System.out.println(warehouseDetails.getName());
		validateUser();
	}
	
	private void validateUser() {
		PackSizeLogger.info("In validateUser()");
		
		if(StringUtil.isNotBlank(warehouseDetails.getName())) {
			try {
				ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
				context.redirect(context.getRequestContextPath() + "/warehousepages/warehouseChecklist.xhtml");
			} catch (IOException e) {
				PackSizeLogger.error(e.getMessage());
			}
		}else {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not a valid User.")); 
			PackSizeLogger.error("Not a valid User "+ warehouseDetails.getName());
		}
	}
	
	public WarehouseDetails getWarehouseDetails() {
		return warehouseDetails;
	}

	public void setWarehouseDetails(WarehouseDetails warehouseDetails) {
		this.warehouseDetails = warehouseDetails;
	}
}
