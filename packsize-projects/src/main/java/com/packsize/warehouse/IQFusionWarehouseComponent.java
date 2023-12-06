package com.packsize.warehouse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.login.Login;
import com.packsize.warehouse.google.GoogleSheetsUtil;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
import com.packsize.warehouse.templates.IQFusionTemplate;

@Component
@Scope(value = "session")
public class IQFusionWarehouseComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private WarehouseComponent warehouseComponent;
	
	@Autowired
	private Login login;
	
	private IQFusionTemplate iQFusionTemplate;
	private Date start;
	private Date pause;
	private Date complete;
	
	@PostConstruct
    private void init() {
		initialSetup();
	}
	
	private void initialSetup() {
		logger.info("In initialSetup()");
		
		if(warehouseComponent.getWarehouseDetailsList().isEmpty()) {
			setiQFusionTemplate(new IQFusionTemplate());
		}else {
			setiQFusionTemplate(GoogleSheetsUtil.readDataFromSheets(warehouseComponent.getWarehouseDetails().getName(), String.valueOf(warehouseComponent.getWarehouseDetails().getAssetID())));
		}
	}
	
	public void checklistItemAction(String status, IQFusionChecklistItem item) {
		logger.info("In checklistItemAction()");
		
		switch(status) {
		case "start": itemStartTimer(item); break;
		case "pause": itemPauseTimer(item); break;
		case "complete": itemCompleteTimer(item); break;
		default: break;
		}
	}
	
	public void itemStartTimer(IQFusionChecklistItem item) {
		logger.info("In itemStartTimer()");
		
		setStart(new Date());
		item.setContinueItem(true);
	}
	
	public void itemPauseTimer(IQFusionChecklistItem item) {
		logger.info("In itemPauseTimer()");
		
		setPause(new Date());
		item.setContinueItem(false);
		
		long duration  = getPause().getTime() - getStart().getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		
		if(item.getHours() != 0) {
			long existingVal = item.getHours();
			item.setHours(existingVal + diffInSeconds);
		}else {
			item.setHours(diffInSeconds);
		}
	}
	
	public void itemCompleteTimer(IQFusionChecklistItem item) {
		logger.info("In itemCompleteTimer()");
		
		setComplete(new Date());
		
		long duration  = getComplete().getTime() - getStart().getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		
		if(item.isContinueItem()) {
			if(item.getHours() != 0) {
				long existingVal = item.getHours();
				item.setHours(existingVal + diffInSeconds);
			}else {
				item.setHours(diffInSeconds);
			}
		}
		//TODO remove this test call
		//writeDataToGoogleSheets(item);
		totalHoursByKey(item); 
		List<IQFusionChecklistItem> returnedList = returnCheckListByKey(item);
		if(item.getId() < returnedList.size()) {
		    completeIQCheckListItem(returnedList, item.getId()); 
		}else { 
		    item.setEnable(false);
			disableAddItemByKey(item);
		}
		writeDataToGoogleSheets(item);
	}
	
	public void writeDataToGoogleSheets(IQFusionChecklistItem item) {
		logger.info("In writeDataToGoogleSheets()");
		
		GoogleSheetsUtil.writeDataToSheets(login, warehouseComponent.getWarehouseDetails(), item, iQFusionTemplate);
	}
	
	public void completeIQCheckListItem(List<IQFusionChecklistItem> returnedList, Integer id) {
		logger.info("In completeIQCheckListItem()");
		
		for(IQFusionChecklistItem item : returnedList) {
			if(item.getId() == (id+1)) {
				item.setEnable(true);
			}else {
				item.setEnable(false);
			}
		}
	}
	
	private List<IQFusionChecklistItem> returnCheckListByKey(IQFusionChecklistItem item) {
		logger.info("In returnCheckListByKey() with obj");
		
		switch(item.getParentId()) {
		case 1 : return iQFusionTemplate.getItemListPrepToRun(); 
		case 2 : return iQFusionTemplate.getItemListImagingThePanel(); 
		default : return null; 
		}
	}
	
	private List<IQFusionChecklistItem> returnCheckListByKey(Integer parentID) {
		logger.info("In returnCheckListByKey() with " + parentID);
		
		switch(parentID) {
		case 1 : return iQFusionTemplate.getItemListPrepToRun(); 
		case 2 : return iQFusionTemplate.getItemListImagingThePanel(); 
		default : return null; 
		}
	}
	
	private void disableAddItemByKey(IQFusionChecklistItem item) {
		logger.info("In disableAddItemByKey()");
		
		switch(item.getParentId()) {
		case 1 : iQFusionTemplate.setDisableAddItemToPrepToRun(true); break; 
		case 2 : iQFusionTemplate.setDisableAddItemToImagingThePanel(true); break;
		default : break; 
		}
	}
	
	private void totalHoursByKey(IQFusionChecklistItem item) {
		logger.info("In totalHoursByKey()");
		
		switch(item.getParentId()) {
		case 1 : iQFusionTemplate.setTotalHrsPrepToRun(iQFusionTemplate.getTotalHrsPrepToRun() + item.getHours()); break; 
		case 2 : iQFusionTemplate.setTotalHrsImagingThePanel(iQFusionTemplate.getTotalHrsImagingThePanel() + item.getHours()); break;
		default : break; 
		}
	}
	
	public void addItemObj(Integer parentID) {
		logger.info("In addItemObj()");
		
		IQFusionChecklistItem newItem = new IQFusionChecklistItem();
		List<IQFusionChecklistItem> returnedList = returnCheckListByKey(parentID);
		Integer index = returnedList.size()+1;
		
		switch(parentID) {
			case 1 : newItemChecklist(parentID, newItem, index, iQFusionTemplate.getAddItemPrepToRun()); break;
			case 2 : newItemChecklist(parentID, newItem, index, iQFusionTemplate.getAddItemImagingThePanel()); break;
			default : break;
		}
		returnedList.add(newItem);
	}
	
	private void newItemChecklist(Integer parentID, IQFusionChecklistItem newItem, Integer index, String newItemName) {
		logger.info("In newItemChecklist()");
		
		newItem.setParentId(parentID);
		newItem.setId(index);
		newItem.setName(index.toString().concat(".").concat(newItemName));
	    newItem.setEnable(false);
	}
	
	public void addItemToPrepToRun(Integer toChecklist) {
		logger.info("In addItemToPrepToRun()");
		
		Integer index = iQFusionTemplate.getiQCheckList().get(toChecklist).size()+1;
		
		iQFusionTemplate.getiQCheckList().get(toChecklist).put(index, index.toString().concat(".").concat(iQFusionTemplate.getAddItemPrepToRun()));
		iQFusionTemplate.setPrepToRun(iQFusionTemplate.getiQCheckList().get(toChecklist));
	}
	
	public IQFusionTemplate getiQFusionTemplate() {
		return iQFusionTemplate;
	}

	public void setiQFusionTemplate(IQFusionTemplate iQFusionTemplate) {
		this.iQFusionTemplate = iQFusionTemplate;
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getPause() {
		return pause;
	}

	public void setPause(Date pause) {
		this.pause = pause;
	}

	public Date getComplete() {
		return complete;
	}

	public void setComplete(Date complete) {
		this.complete = complete;
	}
	
	public WarehouseComponent getWarehouseComponent() {
		return warehouseComponent;
	}

	public void setWarehouseComponent(WarehouseComponent warehouseComponent) {
		this.warehouseComponent = warehouseComponent;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
}
