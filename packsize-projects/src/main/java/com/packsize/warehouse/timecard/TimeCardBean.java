package com.packsize.warehouse.timecard;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class TimeCardBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();
	
	@Autowired
	private TimeCardController timeCardController;
	
	@PostConstruct
	private void init() {
		
		initialSetup();
	}
	
	private void initialSetup() {
		logger.info("In initialSetup()");
		
		timeCardController.initialSetup();
	}
	
	public void updateTotals() {
		logger.info("In updateTotals()");
		
		String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1");
		logger.info("Param Receive "+ param1);
		timeCardController.calculateTotalsByDayIndex(Integer.valueOf(param1));
	}
	
	public void save() {
		logger.info("In save()");
		
		timeCardController.save();
	}

	public TimeCardController getTimeCardController() {
		return timeCardController;
	}

	public void setTimeCardController(TimeCardController timeCardController) {
		this.timeCardController = timeCardController;
	}
}
