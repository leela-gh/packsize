package com.packsize.warehouse.timecard;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.packsize.login.Login;

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
	
	public void navDate(String action, int currentWeekID) {
		logger.info("In navDate()");
		
		timeCardController.navDate(action, currentWeekID);
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
	
	public void submitForApprove() {
		logger.info("In submitForApprove()");
		
		timeCardController.submitForApprove();
	}
	
	public void approverEditUpdateTotals() {
		logger.info("In approverEditUpdateTotals()");
		
		String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1");
		logger.info("Param Receive "+ param1);
		Object param2 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param2");
		timeCardController.setTimeCardDetails((TimeCardDetails) param2);
		System.out.println(timeCardController.getTimeCardDetails().getUser());
		//timeCardController.calculateTotalsByDayIndex(Integer.valueOf(param1));
	}
	
	public void approverEditUpdateTotalsTest() {
		logger.info("In approverEditUpdateTotals()");
		
		String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1");
		logger.info("Param Receive "+ param1);
		String param2 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param2");
		logger.info("Param Receive "+ param2);
		//timeCardController.setTimeCardDetails((TimeCardDetails) param2);
		//System.out.println(timeCardController.getTimeCardDetails().getUser());
		//timeCardController.calculateTotalsByDayIndex(Integer.valueOf(param1));
	}
	
	public void approve() {
		logger.info("In approve()");
		
		timeCardController.approve();
	}
	
	public void editAndApprove(TimeCardDetails timeCardDetails) {
		logger.info("In editAndApprove()");
		
		timeCardController.setTimeCardDetails(timeCardDetails);
		System.out.println(timeCardController.getTimeCardDetails().toString());
		
	}
	
	public TimeCardController getTimeCardController() {
		return timeCardController;
	}

	public void setTimeCardController(TimeCardController timeCardController) {
		this.timeCardController = timeCardController;
	}
}
