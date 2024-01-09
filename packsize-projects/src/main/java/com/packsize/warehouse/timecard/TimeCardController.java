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
		
		generateCurrentWeekID();
		setTimeCardDetailsList(GoogleSheetsUtil.readTimeCardDetailsFromSheets(login.getUser())); 
		iterateAndRetriveWeek();
		
		if(getTimeCardDetailsCurrent() == null) {
			setTimeCardDetailsCurrent(new TimeCardDetails());
			generateTimeCardCurrent();
		}
		
		if(getTimeCardDetailsPrev() ==  null) {
			setTimeCardDetailsPrev(new TimeCardDetails());
			generateTimeCardPrev();
		}
		
		if(getTimeCardDetailsNext() ==  null) {
			setTimeCardDetailsNext(new TimeCardDetails());
			generateTimeCardNext();
		}
		
		setTimeCardDetails(getTimeCardDetailsCurrent());
	}
	
	private void generateCurrentWeekID() {
		logger.info("In generateCurrentWeekID()");
		
		LocalDate date = LocalDate.now();
		int currentweekID = date.get(WeekFields.of(Locale.US).weekOfYear());
		setCurrentWeekID(currentweekID);
	}
	
	protected void generateTimeCardPrev() {
		logger.info("In generateTimeCardPrev()");
		
		int prevWeekID = getCurrentWeekID() -1;
		getTimeCardDetailsPrev().setWeekID(prevWeekID);
		getTimeCardDetailsPrev().getTimeCards().put(String.valueOf(prevWeekID), getAllDaysOfTheWeek(prevWeekID, Locale.US));
		assignDateHeadersPrev(prevWeekID);
		addDefaultDayFields("prev");
	}
	
	protected void generateTimeCardNext() {
		logger.info("In generateTimeCardNext()");
		
		int nextWeekID = getCurrentWeekID() +1;
		getTimeCardDetailsNext().setWeekID(nextWeekID);
		getTimeCardDetailsNext().getTimeCards().put(String.valueOf(nextWeekID), getAllDaysOfTheWeek(nextWeekID, Locale.US));
		assignDateHeadersNext(nextWeekID);
		addDefaultDayFields("next");
	}
	
	protected void generateTimeCardCurrent() {
		logger.info("In generateTimeCard()");
		
		int currentweekID = getCurrentWeekID();
		getTimeCardDetailsCurrent().setWeekID(currentweekID);
		getTimeCardDetailsCurrent().getTimeCards().put(String.valueOf(currentweekID), getAllDaysOfTheWeek(currentweekID, Locale.US));
		assignDateHeaders(currentweekID);
		addDefaultDayFields("current");
	}
	
	private void assignDateHeaders(int weekID) {
		logger.info("In assignDateHeaders()");
			
		
		getTimeCardDetailsCurrent().setSunday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(0).format(formatter));
		getTimeCardDetailsCurrent().setMonday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(1).format(formatter));
		getTimeCardDetailsCurrent().setTuesday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(2).format(formatter));
		getTimeCardDetailsCurrent().setWednesday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(3).format(formatter));
		getTimeCardDetailsCurrent().setThursday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(4).format(formatter));
		getTimeCardDetailsCurrent().setFriday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(5).format(formatter));
		getTimeCardDetailsCurrent().setSaturday(getTimeCardDetailsCurrent().getTimeCards().get(String.valueOf(weekID)).get(6).format(formatter));
	}
	
	private void assignDateHeadersPrev(int weekID) {
		getTimeCardDetailsPrev().setSunday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(0).format(formatter));
		getTimeCardDetailsPrev().setMonday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(1).format(formatter));
		getTimeCardDetailsPrev().setTuesday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(2).format(formatter));
		getTimeCardDetailsPrev().setWednesday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(3).format(formatter));
		getTimeCardDetailsPrev().setThursday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(4).format(formatter));
		getTimeCardDetailsPrev().setFriday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(5).format(formatter));
		getTimeCardDetailsPrev().setSaturday(getTimeCardDetailsPrev().getTimeCards().get(String.valueOf(weekID)).get(6).format(formatter));
	}
	
	private void assignDateHeadersNext(int weekID) {
		getTimeCardDetailsNext().setSunday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(0).format(formatter));
		getTimeCardDetailsNext().setMonday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(1).format(formatter));
		getTimeCardDetailsNext().setTuesday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(2).format(formatter));
		getTimeCardDetailsNext().setWednesday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(3).format(formatter));
		getTimeCardDetailsNext().setThursday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(4).format(formatter));
		getTimeCardDetailsNext().setFriday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(5).format(formatter));
		getTimeCardDetailsNext().setSaturday(getTimeCardDetailsNext().getTimeCards().get(String.valueOf(weekID)).get(6).format(formatter));
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
	
	private void iterateAndRetriveWeek() {
		logger.info("In iterateAndRetriveWeek() ");
		
		LocalDate date = LocalDate.now();
		int currentweekID = date.get(WeekFields.of(Locale.US).weekOfYear());
		String userKeyPrev = login.getUser().concat(String.valueOf(currentweekID -1));
		String userKey = login.getUser().concat(String.valueOf(currentweekID));
		String userKeyNext = login.getUser().concat(String.valueOf(currentweekID +1));
		for(TimeCardDetails timeCard : timeCardDetailsList) {
			if(timeCard.getUserKey().equalsIgnoreCase(userKey)) {
				setTimeCardDetailsCurrent(timeCard);
				assignDateHeaders(timeCard.getWeekID());
			}else if(timeCard.getUserKey().equalsIgnoreCase(userKeyPrev)) {
				setTimeCardDetailsPrev(timeCard);
				assignDateHeadersPrev(timeCard.getWeekID());
			}else if(timeCard.getUserKey().equalsIgnoreCase(userKeyNext)) {
				setTimeCardDetailsNext(timeCard);
				assignDateHeadersPrev(timeCard.getWeekID());
			}
		}
	}
	
	private void addDefaultDayFields(String week) {
		logger.info("In addDefaultDayFields()");
		
		if(week.equalsIgnoreCase("current")) {
			getTimeCardDetailsCurrent().getTimeCardDayList().clear();
			getTimeCardDetailsCurrent().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsCurrent().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsCurrent().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsCurrent().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsCurrent().getTimeCardDayList().add(new TimeCardDay());
		}else if(week.equalsIgnoreCase("prev")) {
			getTimeCardDetailsPrev().getTimeCardDayList().clear();
			getTimeCardDetailsPrev().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsPrev().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsPrev().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsPrev().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsPrev().getTimeCardDayList().add(new TimeCardDay());
		}else if(week.equalsIgnoreCase("next")) {
			getTimeCardDetailsNext().getTimeCardDayList().clear();
			getTimeCardDetailsNext().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsNext().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsNext().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsNext().getTimeCardDayList().add(new TimeCardDay());
			getTimeCardDetailsNext().getTimeCardDayList().add(new TimeCardDay());
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
	
	protected void save() {
		logger.info("In save() ");
		
		getTimeCardDetails().setUserKey(login.getUser().concat(String.valueOf(getTimeCardDetails().getWeekID())));
		getTimeCardDetails().setUser(login.getUser());
		GoogleSheetsUtil.deleteTimeCardEntryToSheets(getTimeCardDetails());
		GoogleSheetsUtil.writeTimeCardEntryToSheets(getTimeCardDetails());
	}
	
	protected void submitForApprove() {
		logger.info("In submitForApprove() ");
		
		getTimeCardDetails().setUserKey(login.getUser().concat(String.valueOf(getTimeCardDetails().getWeekID())));
		setTimeCardDetails(GoogleSheetsUtil.updateTimeCardEntryToSheets(getTimeCardDetails(), "submitForApproval"));
	}
	
	protected void approve(TimeCardDetails timeCardDetails) {
		logger.info("In approve() ");
		
		GoogleSheetsUtil.updateTimeCardEntryToSheets(timeCardDetails, "approve");
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
