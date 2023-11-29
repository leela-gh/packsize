package com.packsize.warehouse.google;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.packsize.PackSizeLogger;

public class GoogleSheetsUtil {
	  
	public static void readDataFromSheets(String name, long assetID,String parentItemName, long totalHrsPrepToRun) {
		PackSizeLogger.info("In readDataFromSheets()");
		try {
			ReadGoogleSheets.readDataFromSheets(assetID);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDataToSheets(String name, long assetID,String parentItemName, long totalHrsPrepToRun) {
		PackSizeLogger.info("In writeDataToSheets()");
		try {
			WriteToGoogleSheets.writeDataToSheets(name, assetID, parentItemName, totalHrsPrepToRun);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
}
