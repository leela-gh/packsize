package com.packsize.warehouse.timecard;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.packsize.login.Login;
import com.packsize.warehouse.google.GoogleSheetsUtil;

@Component
@SessionScope
public class TimeCardController implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy").withLocale(Locale.US);
	
	@Autowired
	private Login login;
	
	private TimeCardDetails timeCardDetails;
	private TimeCardDetails timeCardDetailsCurrent;
	private TimeCardDetails timeCardDetailsPrev;
	private TimeCardDetails timeCardDetailsNext;
	private List<TimeCardDetails> timeCardDetailsList;
	
	private int currentWeekID;
	
	@PostConstruct
	private void init() {
		
		initialSetup();
	}
	
	protected void initialSetup() {
		logger.info("In initialSetup()");
		
		setTimeCardDetailsList(GoogleSheetsUtil.readTimeCardDetailsFromSheets(login.getUser())); 
		generateCurrentWeekID();
		iterateAndRetriveWeeks();
		if(getTimeCardDetailsCurrent() == null) {
			setTimeCardDetailsCurrent(new TimeCardDetails());
			generateTimeCardForWeek(getTimeCardDetailsCurrent(), getCurrentWeekID(), "current");
		}
		if(getTimeCardDetailsPrev() ==  null) {
			setTimeCardDetailsPrev(new TimeCardDetails());
			generateTimeCardForWeek(getTimeCardDetailsPrev(), (getCurrentWeekID()-1), "prev");
		}
		if(getTimeCardDetailsNext() ==  null) {
			setTimeCardDetailsNext(new TimeCardDetails());
			generateTimeCardForWeek(getTimeCardDetailsNext(), (getCurrentWeekID()+1), "next");
		}
		setTimeCardDetails(getTimeCardDetailsCurrent());
	}
	
	private void generateCurrentWeekID() {
		logger.info("In generateCurrentWeekID()");
		
		LocalDate date = LocalDate.now();
		int currentweekID = date.get(WeekFields.of(Locale.US).weekOfYear());
		setCurrentWeekID(currentweekID);
	}
	
	private void generateTimeCardForWeek(TimeCardDetails timeCardDetails, int weekID, String weekStatus) {
		logger.info("In generateTimeCardForWeek()");
				
		timeCardDetails.setWeekID(weekID);
		timeCardDetails.getTimeCards().put(String.valueOf(weekID), getAllDaysOfTheWeek(weekID, Locale.US));
		assignDateHeadersForWeek(timeCardDetails,weekID);
		addDefaultDayFieldsForWeek(timeCardDetails);
	}
	
	private void assignDateHeadersForWeek(TimeCardDetails timeCardDetails, int weekID) {
		logger.info("In assignDateHeadersForWeek()");
			
		timeCardDetails.setSunday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(0).format(formatter));
		timeCardDetails.setMonday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(1).format(formatter));
		timeCardDetails.setTuesday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(2).format(formatter));
		timeCardDetails.setWednesday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(3).format(formatter));
		timeCardDetails.setThursday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(4).format(formatter));
		timeCardDetails.setFriday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(5).format(formatter));
		timeCardDetails.setSaturday(timeCardDetails.getTimeCards().get(String.valueOf(weekID)).get(6).format(formatter));
	}
	
	private void addDefaultDayFieldsForWeek(TimeCardDetails timeCardDetails) {
		logger.info("In addDefaultDayFieldsForWeek()");
		
		timeCardDetails.getTimeCardDayList().clear();
		timeCardDetails.getTimeCardDayList().add(new TimeCardDay());
		timeCardDetails.getTimeCardDayList().add(new TimeCardDay());
		timeCardDetails.getTimeCardDayList().add(new TimeCardDay());
		timeCardDetails.getTimeCardDayList().add(new TimeCardDay());
		timeCardDetails.getTimeCardDayList().add(new TimeCardDay());
	}
	
	protected void navDate(String action, int currentWeekID) {
    	logger.info("In navDate() ");
    	
    	if(getTimeCardDetails().getTimeCards().containsKey(String.valueOf(currentWeekID))) {
    		if(action.equalsIgnoreCase("current")) {
        		setTimeCardDetails(getTimeCardDetailsCurrent());
        	}else if(action.equalsIgnoreCase("prev")) {
        		setTimeCardDetails(getTimeCardDetailsPrev());
        	}else if(action.equalsIgnoreCase("next")) {
        		setTimeCardDetails(getTimeCardDetailsNext());
        	}
        }
    }
	
	private void iterateAndRetriveWeeks() {
		logger.info("In iterateAndRetriveWeeks() ");
		
		LocalDate date = LocalDate.now();
		int currentweekID = date.get(WeekFields.of(Locale.US).weekOfYear());
		String userKeyPrev = login.getUser().concat(String.valueOf(currentweekID -1));
		String userKey = login.getUser().concat(String.valueOf(currentweekID));
		String userKeyNext = login.getUser().concat(String.valueOf(currentweekID +1));
		for(TimeCardDetails timeCard : timeCardDetailsList) {
			if(timeCard.getUserKey().equalsIgnoreCase(userKey)) {
				setTimeCardDetailsCurrent(timeCard);
				assignDateHeadersForWeek(getTimeCardDetailsCurrent(), timeCard.getWeekID());
			}else if(timeCard.getUserKey().equalsIgnoreCase(userKeyPrev)) {
				setTimeCardDetailsPrev(timeCard);
				assignDateHeadersForWeek(getTimeCardDetailsPrev(), timeCard.getWeekID());
			}else if(timeCard.getUserKey().equalsIgnoreCase(userKeyNext)) {
				setTimeCardDetailsNext(timeCard);
				assignDateHeadersForWeek(getTimeCardDetailsNext(), timeCard.getWeekID());
			}
		}
	}
	
	protected void calculateTotalsByDayIndex(int day) {
		logger.info("In calculateTotalsByDayIndex() day "+day);
		
		switch(day) {
		case 0 : timeCardDetails.setSunTotal(calculateTotals(day)); break;
		case 1 : timeCardDetails.setMonTotal(calculateTotals(day)); break;
		case 2 : timeCardDetails.setTueTotal(calculateTotals(day)); break;
		case 3 : timeCardDetails.setWedTotal(calculateTotals(day)); break;
		case 4 : timeCardDetails.setThurTotal(calculateTotals(day)); break;
		case 5 : timeCardDetails.setFriTotal(calculateTotals(day)); break;
		case 6 : timeCardDetails.setSatTotal(calculateTotals(day)); break;
		default : break;
		}
		calculateWeekTotals();
	}
	
	private int calculateTotals(int day) {
		logger.info("In calculateTotals() day "+day);
		
		int total = 0;
		for(TimeCardDay dayObj : timeCardDetails.getTimeCardDayList()) {
			if(day == 0) {
				total = total + dayObj.getSun();
			}else if(day == 1) {
				total = total + dayObj.getMon();
			}else if(day == 2) {
				total = total + dayObj.getTue();
			}else if(day == 3) {
				total = total + dayObj.getWed();
			}else if(day == 4) {
				total = total + dayObj.getThur();
			}else if(day == 5) {
				total = total + dayObj.getFri();
			}else if(day == 6) {
				total = total + dayObj.getSat();
			}
		}
		return total;
	}
	
	private void calculateWeekTotals() {
		logger.info("In calculateWeekTotals() ");
		
		timeCardDetails.setWeekTotal(timeCardDetails.getSunTotal() + timeCardDetails.getMonTotal() + timeCardDetails.getTueTotal() + 
									 timeCardDetails.getWedTotal() + timeCardDetails.getThurTotal() + timeCardDetails.getFriTotal() + 
									 timeCardDetails.getSatTotal());
	}
	
	protected boolean save() {
		logger.info("In save() ");
		
		boolean success = true;
		getTimeCardDetails().setUserKey(login.getUser().concat(String.valueOf(getTimeCardDetails().getWeekID())));
		getTimeCardDetails().setUser(login.getUser());
		if(GoogleSheetsUtil.deleteTimeCardEntryToSheets(getTimeCardDetails())) {
			if(!GoogleSheetsUtil.writeTimeCardEntryToSheets(getTimeCardDetails(),"save")) 
				success = false; 
		}else {
			success = false; 
		}
		return success;
	}
	
	protected boolean submitForApprove() {
		logger.info("In submitForApprove() ");
		
		boolean success = true;
		getTimeCardDetails().setUserKey(login.getUser().concat(String.valueOf(getTimeCardDetails().getWeekID())));
		TimeCardDetails timeCardDetails = GoogleSheetsUtil.updateTimeCardEntryToSheets(getTimeCardDetails(), "submitForApproval");
		if(timeCardDetails != null) {
			setTimeCardDetails(timeCardDetails);
		}else {
			success = false; 
		}
		return success;
	}
	
	protected boolean approve() {
		logger.info("In approve() ");
		
		boolean success = true;
		if(GoogleSheetsUtil.deleteTimeCardEntryToSheets(timeCardDetails)) {
			if(GoogleSheetsUtil.writeTimeCardEntryToSheets(timeCardDetails,"ApproverSave")) {
				setTimeCardDetailsList(GoogleSheetsUtil.readTimeCardDetailsFromSheets(login.getUser())); 
			}else {
				success = false;
			}
		}else {
			success = false;
		}
		return success;
	}
	
	private List<LocalDate> getAllDaysOfTheWeek(int weekNumber, Locale locale) {
        LocalDate firstDayOfWeek = getFirstDayOfWeek(weekNumber, locale);
        return IntStream
                .rangeClosed(0, 6)
                .mapToObj(i -> firstDayOfWeek.plusDays(i))
                .collect(Collectors.toList());
    }
    
    private LocalDate getFirstDayOfWeek(int weekNumber, Locale locale) {
        return LocalDate
                .of(Year.now().getValue(), 2, 1)
                .with(WeekFields.of(locale).getFirstDayOfWeek())
                .with(WeekFields.of(locale).weekOfWeekBasedYear(), weekNumber);
    }
    
    public TimeCardDetails getTimeCardDetails() {
		return timeCardDetails;
	}

	public void setTimeCardDetails(TimeCardDetails timeCardDetails) {
		this.timeCardDetails = timeCardDetails;
	}
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<TimeCardDetails> getTimeCardDetailsList() {
		return timeCardDetailsList;
	}

	public void setTimeCardDetailsList(List<TimeCardDetails> timeCardDetailsList) {
		this.timeCardDetailsList = timeCardDetailsList;
	}

	public TimeCardDetails getTimeCardDetailsPrev() {
		return timeCardDetailsPrev;
	}

	public void setTimeCardDetailsPrev(TimeCardDetails timeCardDetailsPrev) {
		this.timeCardDetailsPrev = timeCardDetailsPrev;
	}

	public TimeCardDetails getTimeCardDetailsNext() {
		return timeCardDetailsNext;
	}

	public void setTimeCardDetailsNext(TimeCardDetails timeCardDetailsNext) {
		this.timeCardDetailsNext = timeCardDetailsNext;
	}

	public TimeCardDetails getTimeCardDetailsCurrent() {
		return timeCardDetailsCurrent;
	}

	public void setTimeCardDetailsCurrent(TimeCardDetails timeCardDetailsCurrent) {
		this.timeCardDetailsCurrent = timeCardDetailsCurrent;
	}

	public int getCurrentWeekID() {
		return currentWeekID;
	}

	public void setCurrentWeekID(int currentWeekID) {
		this.currentWeekID = currentWeekID;
	}
}
