package com.packsize.warehouse.timecard;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class TimeCardController implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();
	
	private TimeCardDetails timeCardDetails;
	
	@PostConstruct
	private void init() {
		
		initialSetup();
	}
	
	protected void initialSetup() {
		logger.info("In initialSetup()");
		
		setTimeCardDetails(new TimeCardDetails());
		generateTimeCard();
	}
	
	protected void generateTimeCard() {
		logger.info("In generateTimeCard()");
		
		LocalDate date = LocalDate.now();
		int weekOfYear = date.get(WeekFields.of(Locale.US).weekOfYear());
		getTimeCardDetails().getTimeCards().put("current", getAllDaysOfTheWeek(weekOfYear, Locale.US));
		addDefaultDayFields();
	}
	
	private void addDefaultDayFields() {
		logger.info("In addDefaultDayFields()");
		
		getTimeCardDetails().getTimeCardDayList().add(new TimeCardDay());
		getTimeCardDetails().getTimeCardDayList().add(new TimeCardDay());
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
		
		System.out.println(timeCardDetails.toString());
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
    
    public static void main(String[] args) {
        // Test
		LocalDate date = LocalDate.now();
		int weekOfYear = date.get(WeekFields.of(Locale.US).weekOfYear());
		System.out.println(weekOfYear);
        //getAllDaysOfTheWeek((weekOfYear-1), Locale.US).forEach(System.out::println);
        //getAllDaysOfTheWeek((weekOfYear+1), Locale.US).forEach(System.out::println);
    }

    public TimeCardDetails getTimeCardDetails() {
		return timeCardDetails;
	}

	public void setTimeCardDetails(TimeCardDetails timeCardDetails) {
		this.timeCardDetails = timeCardDetails;
	}
}
