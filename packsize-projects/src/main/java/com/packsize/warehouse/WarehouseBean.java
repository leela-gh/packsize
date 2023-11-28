package com.packsize.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;
import com.packsize.warehouse.templates.IQFusionChecklistItem;

@Component
@Scope(value = "session")
public class WarehouseBean {
	
	@Autowired
	private WarehouseComponent warehouseComponent;
	
	@Autowired
	private IQFusionWarehouseComponent iQFusion;
	
	public void addItem(Integer toChecklist) {
		PackSizeLogger.info("In addItem()");
		
		iQFusion.addItemObj(toChecklist);
	}
	
	public void checklistItemAction(String status, IQFusionChecklistItem item) {
		PackSizeLogger.info("In checklistItemAction()");
		
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