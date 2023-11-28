package com.packsize.warehouse.google;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleSheetsUtil {
	  
	public static void readDataFromSheets(String name, Integer assetID,String parentItemName, long totalHrsPrepToRun) {
		try {
			ReadGoogleSheets.readDataFromSheets(assetID);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDataToSheets(String name, Integer assetID,String parentItemName, long totalHrsPrepToRun) {
		try {
			WriteToGoogleSheets.writeDataToSheets(name, assetID, parentItemName, totalHrsPrepToRun);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
}
