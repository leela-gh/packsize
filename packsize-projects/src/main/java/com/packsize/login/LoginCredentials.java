package com.packsize.login;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.warehouse.google.GoogleSheetsUtil;
import com.packsize.warehouse.timecard.TimeCardUser;

@Component
@Scope(value = "singleton")
public class LoginCredentials {
	
	private static final Logger logger = LogManager.getLogger();
	private static Map<String, TimeCardUser> userMap = new HashMap<String, TimeCardUser>();
	
	@PostConstruct
	public void init() {
		logger.info("In init()");
		
		userMap.clear();
		for(TimeCardUser user : GoogleSheetsUtil.readTimecardUsersFromSheets()) {
			userMap.put(user.getUserName(), user);
		}
	}

	public static Map<String, TimeCardUser> getUserMap() {
		return userMap;
	}
}
