package com.packsize.warehouse;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;
import com.packsize.warehouse.google.GoogleSheetsUtil;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
import com.packsize.warehouse.templates.IQFusionTemplate;

@Component
@Scope(value = "session")
public class IQFusionWarehouseComponent {
	
	private IQFusionTemplate iQFusionTemplate;
	private Date start;
	private Date pause;
	private Date complete;
	
	@PostConstruct
    private void init() {
		initialSetup();
	}
	
	private void initialSetup() {
		PackSizeLogger.info("In initialSetup()");
		
		setiQFusionTemplate(new IQFusionTemplate());
	}
	
	public void checklistItemAction(String status, IQFusionChecklistItem item) {
		PackSizeLogger.info("In checklistItemAction()");
		
		switch(status) {
		case "start": itemStartTimer(item); break;
		case "pause": itemPauseTimer(item); break;
		case "complete": itemCompleteTimer(item); break;
		default: break;
		}
	}
	
	public void itemStartTimer(IQFusionChecklistItem item) {
		PackSizeLogger.info("In itemStartTimer()");
		
		setStart(new Date());
		item.setContinueItem(true);
	}
	
	public void itemPauseTimer(IQFusionChecklistItem item) {
		PackSizeLogger.info("In itemPauseTimer()");
		
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
		PackSizeLogger.info("In itemCompleteTimer()");
		
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
		writeDataToGoogleSheets(item);
		 
		totalHoursByKey(item);
		
		List<IQFusionChecklistItem> returnedList = returnCheckListByKey(item);
		if(item.getId() < returnedList.size()) {
		    completeIQCheckListItem(returnedList, item.getId()); 
		}else { 
		    item.setEnable(false);
			disableAddItemByKey(item);
			writeDataToGoogleSheets(item);
		}
	}
	
	public void writeDataToGoogleSheets(IQFusionChecklistItem item) {
		
		switch(item.getParentId()) {
		case 1 : GoogleSheetsUtil.writeDataToSheets("test", 123, "TestPre", 20); break;
		default : break;
		}
	}
	
	public void completeIQCheckListItem(List<IQFusionChecklistItem> returnedList, Integer id) {
		PackSizeLogger.info("In completeIQCheckListItem()");
		
		for(IQFusionChecklistItem item : returnedList) {
			if(item.getId() == (id+1)) {
				item.setEnable(true);
			}else {
				item.setEnable(false);
			}
		}
	}
	
	private List<IQFusionChecklistItem> returnCheckListByKey(IQFusionChecklistItem item) {
		PackSizeLogger.info("In returnCheckListByKey() with obj");
		
		switch(item.getParentId()) {
		case 1 : return iQFusionTemplate.getItemListPrepToRun(); 
		case 2 : return iQFusionTemplate.getItemListImagingThePanel(); 
		default : return null; 
		}
	}
	
	private List<IQFusionChecklistItem> returnCheckListByKey(Integer parentID) {
		PackSizeLogger.info("In returnCheckListByKey() with " + parentID);
		
		switch(parentID) {
		case 1 : return iQFusionTemplate.getItemListPrepToRun(); 
		case 2 : return iQFusionTemplate.getItemListImagingThePanel(); 
		default : return null; 
		}
	}
	
	private void disableAddItemByKey(IQFusionChecklistItem item) {
		PackSizeLogger.info("In disableAddItemByKey()");
		
		switch(item.getParentId()) {
		case 1 : iQFusionTemplate.setDisableAddItemToPrepToRun(true); break; 
		case 2 : iQFusionTemplate.setDisableAddItemToImagingThePanel(true); break;
		default : break; 
		}
	}
	
	private void totalHoursByKey(IQFusionChecklistItem item) {
		PackSizeLogger.info("In totalHoursByKey()");
		
		switch(item.getParentId()) {
		case 1 : iQFusionTemplate.setTotalHrsPrepToRun(iQFusionTemplate.getTotalHrsPrepToRun() + item.getHours()); break; 
		case 2 : iQFusionTemplate.setTotalHrsImagingThePanel(iQFusionTemplate.getTotalHrsImagingThePanel() + item.getHours()); break;
		default : break; 
		}
	}
	
	public void addItemObj(Integer parentID) {
		PackSizeLogger.info("In addItemObj()");
		
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
		PackSizeLogger.info("In newItemChecklist()");
		
		newItem.setParentId(parentID);
		newItem.setId(index);
		newItem.setName(index.toString().concat(".").concat(newItemName));
	    newItem.setEnable(false);
	}
	
	public void addItemToPrepToRun(Integer toChecklist) {
		PackSizeLogger.info("In addItemToPrepToRun()");
		
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
}