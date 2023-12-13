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
	
	public static List<WarehouseDetails> readWarehouseDetailsFromSheets(String name, boolean complete) {
		logger.info("In readWarehouseDetailsFromSheets()");
		
		 try { 
			  	return ReadGoogleSheets.readWarehouseDetailsFromSheets(name, complete); 
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
	
	
}
