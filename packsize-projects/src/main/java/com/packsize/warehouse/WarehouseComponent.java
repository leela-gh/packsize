package com.packsize.warehouse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.packsize.login.Login;
import com.packsize.warehouse.google.GoogleSheetsUtil;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
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
		
		setWarehouseDetailsList(GoogleSheetsUtil.readWarehouseDetailsFromSheets(login.getUser()));
		setWarehouseDetails(new WarehouseDetails());
		calculateTotals();
	}
	
	private void calculateTotals() {
		logger.info("In calculateTotals()");
		
		for(WarehouseDetails obj : warehouseDetailsList) {
			IQFusionTemplate template = GoogleSheetsUtil.readDataFromSheets(obj.getName(), obj.getAssetID().toString());
			obj.setTotalHrs(template.getTotalHrsPrepToRun() + template.getTotalHrsImagingThePanel() + template.getTotalHrsRunningSoftwareInstaller() + template.getTotalHrsRoboSetup()
							+ template.getTotalHrsPlcSettings() + template.getTotalHrsPacknetSetup() + template.getTotalHrsCalibration());
		}
	}
	
	private void generateCSV(WarehouseDetails asset){
		logger.info("In generateCSV"); 
        try { 
        	 Map < Integer, String[] > checkListInfoMap = new TreeMap < Integer, String[] >();
        	 checkListInfoMap.put( 0, new String[] {"user","assetID","MachineType","parentId","name","hours","totalHrsPrepToRun","Default Assigned Hrs"});
        	 StringBuffer data = new StringBuffer();
        	         	 
        	 IQFusionTemplate template = GoogleSheetsUtil.readDataFromSheets(asset.getName(), asset.getAssetID().toString());
        	 addDataFromSheets("Prep To Run", checkListInfoMap, asset, template.getItemListPrepToRun(), template.getTotalHrsPrepToRun(),data);
        	 checkListInfoMap.clear();
        	 addDataFromSheets("Imaging The Panel", checkListInfoMap, asset, template.getItemListImagingThePanel(), template.getTotalHrsImagingThePanel(),data);
            
            warehouseDetails.setFile(DefaultStreamedContent.builder()
    		        .name("checkListDetails.csv")
    		        .contentType("application/vnd.ms-excel ")
    		        .stream(() -> new ByteArrayInputStream(data.toString().getBytes(StandardCharsets.UTF_8)))
    		        .build());
            
            logger.info("Excel File has been created successfully.");
            
            } catch (Exception e) {
        	logger.error(e.toString());
        }
        
    }
	
	private void addDataFromSheets(String title, Map < Integer, String[] > checkListInfoMap, WarehouseDetails asset, List<IQFusionChecklistItem> items, long totalHrs, StringBuffer data) {
		logger.info("In addDataFromSheets() for "+ title); 
		
		int index = 1;
		for(IQFusionChecklistItem item : items) {
			checkListInfoMap.put( index, new String[] {asset.getName(),asset.getAssetID().toString(),asset.getMachineType(),item.getParentId().toString(),item.getName(),String.valueOf(item.getHours()),String.valueOf(totalHrs),"Default Assigned Hrs"});
			index++;
   	 	}
   	 
	       logger.info(checkListInfoMap.keySet().toString());
	       Set < Integer > rowIDS = checkListInfoMap.keySet();
	       data.append("*************"+ title +"****************").append('\n'); 
	       for (Integer row = 0; row <  rowIDS.size(); row++) {
	    	   if(checkListInfoMap.get(row) != null) {
	    		   String [] cellArr = checkListInfoMap.get(row);
		           for (Integer cell = 0; cell < cellArr.length; cell++){
		               if(cell != (cellArr.length-1)){
		                   data.append(cellArr[cell].replace(",", ";") + ","); 
		               }else{
		                   data.append(cellArr[cell].replace(",", ";")); 
		               }
		           }
		           data.append('\n');
	    	   }
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
	
	public void assetComplete(WarehouseDetails asset) {
		logger.info("In assetComplete()");
		
		GoogleSheetsUtil.updateWarehouseDetailsToSheets(login, asset);
		setWarehouseDetailsList(GoogleSheetsUtil.readWarehouseDetailsFromSheets(login.getUser()));
	}
	
	public void download(WarehouseDetails asset) {
		logger.info("In download()");
		
		generateCSV(asset);
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
