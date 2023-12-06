package com.packsize.warehouse;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.warehouse.templates.IQFusionChecklistItem;

@Component
@Scope(value = "request")
public class WarehouseBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();
	
	@Autowired
	private WarehouseComponent warehouseComponent;
	
	@Autowired
	private IQFusionWarehouseComponent iQFusion;
	
	public void addItem(Integer toChecklist) {
		logger.info("In addItem()");
		
		iQFusion.addItemObj(toChecklist);
	}
	
	public void checklistItemAction(String status, IQFusionChecklistItem item) {
		logger.info("In checklistItemAction()");
		
		iQFusion.checklistItemAction(status, item);
	}
	
	public WarehouseComponent getWarehouseComponent() {
		return warehouseComponent;
	}

	public void setWarehouseComponent(WarehouseComponent warehouseComponent) {
		this.warehouseComponent = warehouseComponent;
	}

	public IQFusionWarehouseComponent getiQFusion() {
		return iQFusion;
	}

	public void setiQFusion(IQFusionWarehouseComponent iQFusion) {
		this.iQFusion = iQFusion;
	}
	
	
}