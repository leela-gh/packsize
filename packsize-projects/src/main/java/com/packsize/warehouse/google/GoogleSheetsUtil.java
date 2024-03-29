package com.packsize.warehouse.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.packsize.login.Login;
import com.packsize.warehouse.WarehouseDetails;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
import com.packsize.warehouse.templates.IQFusionTemplate;
import com.packsize.warehouse.timecard.TimeCardDetails;
import com.packsize.warehouse.timecard.TimeCardUser;

public class GoogleSheetsUtil {
	
	private static final Logger logger = LogManager.getLogger();
	  
	public static IQFusionTemplate readDataFromSheets(String name, String assetID) {
		logger.info("In readDataFromSheets()");
		
		 try { 
			  	return ReadGoogleSheets.readDataFromSheets(name, assetID); 
			 }catch(IOException | GeneralSecurityException e) 
		  	{ 
				 e.printStackTrace(); return null;
		  	}
	}
	
	public static void writeDataToSheets(Login login, WarehouseDetails warehouseDetails,IQFusionChecklistItem item, IQFusionTemplate iQFusionTemplate) {
		logger.info("In writeDataToSheets()");
		try {
			WriteToGoogleSheets.writeDataToSheets(login, warehouseDetails,item ,iQFusionTemplate);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static List<WarehouseDetails> readWarehouseDetailsFromSheets(String name) {
		logger.info("In readWarehouseDetailsFromSheets()");
		
		 try { 
			  	return ReadGoogleSheets.readWarehouseDetailsFromSheets(name); 
			 }catch(IOException | GeneralSecurityException e) 
		  	{ 
				 e.printStackTrace(); return null;
		  	}
		 
	}
	
	public static void writeWarehouseDetailsToSheets(Login login, WarehouseDetails warehouseDetails) {
		logger.info("In writeWarehouseDetailsToSheets()");
		try {
			WriteToGoogleSheets.writeWarehouseDetailsToSheets(login, warehouseDetails);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateWarehouseDetailsToSheets(Login login, WarehouseDetails warehouseDetails) {
		logger.info("In updateWarehouseDetailsToSheets()");
		try {
			WriteToGoogleSheets.updateWarehouseDetailsToSheets(login, warehouseDetails);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static TimeCardDetails updateTimeCardEntryToSheets(TimeCardDetails timeCardDetails, String action) {
		logger.info("In updateTimeCardEntryToSheets()");
		try {
			return WriteToGoogleSheets.updateTimeCardEntryToSheets(timeCardDetails, action);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean writeTimeCardEntryToSheets(TimeCardDetails timeCardDetails, String action) {
		logger.info("In writeTimeCardEntryToSheets()");
		boolean success = true; 
		try {
			WriteToGoogleSheets.writeTimeCardEntryToSheets(timeCardDetails,action);
		} catch (IOException | GeneralSecurityException e) {
			success = false; 
			e.printStackTrace();
		}
		return success;
	}
	
	public static List<TimeCardDetails> readTimeCardDetailsFromSheets(String name) {
		logger.info("In readTimeCardDetailsFromSheets()");
		
		 try { 
			  	return ReadGoogleSheets.readTimecardDetailsFromSheets(name); 
			 }catch(IOException | GeneralSecurityException e) 
		  	{ 
				 e.printStackTrace(); return null;
		  	}
	}
	
	public static List<TimeCardUser> readTimecardUsersFromSheets() {
		logger.info("In readTimecardUsersFromSheets()");
		
		 try { 
			  	return ReadGoogleSheets.readTimecardUsersFromSheets(); 
			 }catch(IOException | GeneralSecurityException e) 
		  	{ 
				 e.printStackTrace(); return null;
		  	}
	}
	
	public static boolean deleteTimeCardEntryToSheets(TimeCardDetails timeCardDetails) {
		logger.info("In deleteTimeCardEntryToSheets()");
		
		boolean success = true; 
		try {
			WriteToGoogleSheets.deleteTimeCardEntryToSheets(timeCardDetails);
		} catch (IOException | GeneralSecurityException e) {
			success = false; 
			e.printStackTrace();
		}
		return success;
	}
	
	
}
