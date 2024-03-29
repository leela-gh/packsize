package com.packsize.warehouse.google;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.packsize.login.Login;
import com.packsize.warehouse.WarehouseDetails;
import com.packsize.warehouse.templates.DefaultHoursTemplate;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
import com.packsize.warehouse.templates.IQFusionTemplate;
import com.packsize.warehouse.timecard.TimeCardDetails;

public class WriteToGoogleSheets {
	
	private static final Logger logger = LogManager.getLogger();
    private static final String APPLICATION_NAME = "Write to Google Spreadsheet";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "C:\\Users\\leela.yallabandi\\OneDrive - Packsize International\\Desktop\\DataWareHouse_Prj\\TOKENS_DIRECTORY_PATH_WRITE";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE, SheetsScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    	logger.info("In getCredentials()");
    	// Load client secrets.
        InputStream in = WriteToGoogleSheets.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void writeDataToSheets(Login login, WarehouseDetails warehouseDetails,IQFusionChecklistItem item, IQFusionTemplate iQFusionTemplate) throws IOException, GeneralSecurityException {
    	logger.info("In writeDataToSheets()");
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1nGZPIsqYNKUdVQc8O-BzujVqDozfO_7CGrOIsR-o7sc";
        final String range = "Sheet1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                				   .setApplicationName(APPLICATION_NAME)
                				   .build();
        ValueRange response = service.spreadsheets().values()
					                 .get(spreadsheetId, range)
					                 .execute();
        List<List<Object>> values = response.getValues();
        
        if(!values.isEmpty()) {
        	
        	String rowIndex = "A".concat(String.valueOf(values.size() + 1));
        	float totalParentHrs = returnTotalHrsForSubCheckList(item, iQFusionTemplate);
        	float defaultParentHrs = (float) DefaultHoursTemplate.iQParentListDefaultHrs.get(item.getParentId());
        	String percentInString = String.valueOf(((totalParentHrs / defaultParentHrs) * 100)).concat("%");
        	ValueRange body = new ValueRange().setValues(Arrays.asList(
        			Arrays.asList(login.getUser(), 
        						  warehouseDetails.getAssetID(), 
        						  warehouseDetails.getMachineType(), 
        						  item.getParentId(), 
        						  item.getId(), 
        						  item.getName(), 
        						  item.getHours(), 
        						  item.isEnable(), 
        						  item.isContinueItem(), 
        						  totalParentHrs, 
        						  disableAddItemForSubCheckList(item, iQFusionTemplate),
        						  defaultParentHrs,
        						  percentInString)));
        	
          	UpdateValuesResponse result = service.spreadsheets().values()
    								      	     .update(spreadsheetId, rowIndex, body)
    								      	     .setValueInputOption("RAW")
    								      	     .execute();
        }
    }
    
    public static void writeWarehouseDetailsToSheets(Login login, WarehouseDetails warehouseDetails) throws IOException, GeneralSecurityException {
    	logger.info("In writeWarehouseDetailsToSheets()");
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1bbs9YjxG_y9QgErZGawJilHEQ3sHp2dMXdTGRbvOYrE";
        final String range = "Sheet1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                				   .setApplicationName(APPLICATION_NAME)
                				   .build();
        ValueRange response = service.spreadsheets().values()
					                 .get(spreadsheetId, range)
					                 .execute();
        List<List<Object>> values = response.getValues();
        
        if(!values.isEmpty()) {
        	String rowIndex = "A".concat(String.valueOf(values.size() + 1));
        	ValueRange body = new ValueRange().setValues(Arrays.asList(
        			Arrays.asList(login.getUser(), warehouseDetails.getAssetID(), warehouseDetails.getMachineType(), 
        					warehouseDetails.getStatus())));
        	
          	UpdateValuesResponse result = service.spreadsheets().values()
    								      	     .update(spreadsheetId, rowIndex, body)
    								      	     .setValueInputOption("RAW")
    								      	     .execute();
        }
    }
    
    public static void writeTimeCardEntryToSheets(TimeCardDetails timeCardDetails, String action) throws IOException, GeneralSecurityException {
    	logger.info("In writeTimeCardEntryToSheets()");
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1-GG-5_08Sh0sjzjeSzGx6QK22tZWzH5HktKpiAvAShk";
        final String range = "Sheet1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                				   .setApplicationName(APPLICATION_NAME)
                				   .build();
        ValueRange response = service.spreadsheets().values()
					                 .get(spreadsheetId, range)
					                 .execute();
        List<List<Object>> values = response.getValues();
        
        if(!values.isEmpty()) {
        	String rowIndex = "A".concat(String.valueOf(values.size() + 1));
        	ValueRange body = null;
        	if("ApproverSave".equalsIgnoreCase(action)) {
        		 body = new ValueRange().setValues(Arrays.asList(
            			Arrays.asList(timeCardDetails.getUserKey(),timeCardDetails.toString(),timeCardDetails.getUser(),true,true)));
        	}else {
        		 body = new ValueRange().setValues(Arrays.asList(
            			Arrays.asList(timeCardDetails.getUserKey(),timeCardDetails.toString(),timeCardDetails.getUser(),false,false)));
        	}
        	
        	UpdateValuesResponse result = service.spreadsheets().values()
    								      	     .update(spreadsheetId, rowIndex, body)
    								      	     .setValueInputOption("RAW")
    								      	     .execute();
        }
    }
    
    private static long returnTotalHrsForSubCheckList(IQFusionChecklistItem item, IQFusionTemplate iQFusionTemplate) {
		long totalHrs = 0;
		
		switch(item.getParentId()) {
			case 1 : totalHrs = iQFusionTemplate.getTotalHrsPrepToRun(); break;
			case 2 : totalHrs = iQFusionTemplate.getTotalHrsImagingThePanel(); break;
			case 3 : totalHrs = iQFusionTemplate.getTotalHrsRunningSoftwareInstaller(); break;
			case 4 : totalHrs = iQFusionTemplate.getTotalHrsRoboSetup(); break;
			case 5 : totalHrs = iQFusionTemplate.getTotalHrsPlcSettings(); break;
			case 6 : totalHrs = iQFusionTemplate.getTotalHrsPacknetSetup(); break;
			case 7 : totalHrs = iQFusionTemplate.getTotalHrsCalibration(); break;
			default : break;
		}
		
		return totalHrs;
	}
    
    private static boolean disableAddItemForSubCheckList(IQFusionChecklistItem item, IQFusionTemplate iQFusionTemplate) {
		boolean disable = false;
		
		switch(item.getParentId()) {
			case 1 : disable = iQFusionTemplate.isDisableAddItemToPrepToRun(); break;
			case 2 : disable = iQFusionTemplate.isDisableAddItemToImagingThePanel(); break;
			case 3 : disable = iQFusionTemplate.isDisableAddItemToRunningSoftwareInstaller(); break;
			case 4 : disable = iQFusionTemplate.isDisableAddItemToRoboSetup(); break;
			case 5 : disable = iQFusionTemplate.isDisableAddItemToPlcSettings(); break;
			case 6 : disable = iQFusionTemplate.isDisableAddItemToPacknetSetup(); break;
			case 7 : disable = iQFusionTemplate.isDisableAddItemToCalibration(); break;
			default : break;
		}
		return disable;
	}
    
    public static void deleteWarehouseDetailsFromSheets(String user) throws IOException, GeneralSecurityException {
		  logger.info("deleteWarehouseDetailsFromSheets()");
		  
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    final String spreadsheetId = "1bbs9YjxG_y9QgErZGawJilHEQ3sHp2dMXdTGRbvOYrE";
	    final String range = "Sheet1";
	    Sheets service =
	        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
	    
	    DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest() 
	    		   .setRange(
	    		        new DimensionRange()
	    		            .setSheetId(0)
	    		            .setDimension("ROWS")
	    		            .setStartIndex(0)
	    		            .setEndIndex(1)
	    		  );

	    		List<Request> requests = new ArrayList<>();
	    		requests.add(new Request().setDeleteDimension(deleteRequest));

	    		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
	    		service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
	  }
    
    public static void updateWarehouseDetailsToSheets(Login login, WarehouseDetails warehouseDetails) throws IOException, GeneralSecurityException {
    	logger.info("In updateWarehouseDetailsToSheets()");
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1bbs9YjxG_y9QgErZGawJilHEQ3sHp2dMXdTGRbvOYrE";
        final String range = "Sheet1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                				   .setApplicationName(APPLICATION_NAME)
                				   .build();
        ValueRange response = service.spreadsheets().values()
					                 .get(spreadsheetId, range)
					                 .execute();
        List<List<Object>> values = response.getValues();
        
        int i = 1;
        for (List row : values) {
        	if(row.get(0).toString().equalsIgnoreCase(login.getUser()) && row.get(1).toString().equalsIgnoreCase(warehouseDetails.getAssetID().toString())) {
	        	 break;
	        }
        	i++;
	     }
        String rowIndex = "D".concat(String.valueOf(i));
        ValueRange body = new ValueRange().setValues(Arrays.asList(
    			Arrays.asList("Complete",warehouseDetails.getTotalHrs())));
    	
      	UpdateValuesResponse result = service.spreadsheets().values()
								      	     .update(spreadsheetId, rowIndex, body)
								      	     .setValueInputOption("RAW")
								      	     .execute();
      	logger.info("Updated row index " + rowIndex);
    }
    
    public static TimeCardDetails updateTimeCardEntryToSheets(TimeCardDetails timeCardDetails, String action) throws IOException, GeneralSecurityException {
    	logger.info("In updateTimeCardEntryToSheets()");
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1-GG-5_08Sh0sjzjeSzGx6QK22tZWzH5HktKpiAvAShk";
        final String range = "Sheet1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                				   .setApplicationName(APPLICATION_NAME)
                				   .build();
        ValueRange response = service.spreadsheets().values()
					                 .get(spreadsheetId, range)
					                 .execute();
        List<List<Object>> values = response.getValues();
        
        int i = 1;
        for (List row : values) {
        	if(row.get(0).toString().equalsIgnoreCase(timeCardDetails.getUserKey())) {
	        	 break;
	        }
        	i++;
	     }
        
        String rowIndex = "D".concat(String.valueOf(i));
        ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(true)));
    	
      	UpdateValuesResponse result = service.spreadsheets().values()
								      	     .update(spreadsheetId, rowIndex, body)
								      	     .setValueInputOption("RAW")
								      	     .execute();
      	
      	timeCardDetails.setSubmitForApproval(true);
      	logger.info("Updated row index " + rowIndex);
      	return timeCardDetails;
    }
    
    public static void deleteTimeCardEntryToSheets(TimeCardDetails timeCardDetails) throws IOException, GeneralSecurityException {
		  logger.info("deleteTimeCardEntryToSheets()");
		  
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    final String spreadsheetId = "1-GG-5_08Sh0sjzjeSzGx6QK22tZWzH5HktKpiAvAShk";
	    final String range = "Sheet1";
	    Sheets service =
	        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
	    
	    ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
		List<List<Object>> values = response.getValues();
		
		int i = 0;
		boolean found = false;
		for (List row : values) {
			if(row.get(0).toString().equalsIgnoreCase(timeCardDetails.getUserKey())) {
				logger.info(timeCardDetails.getUserKey() + " Found on Index "+ i);
				found = true;
				break;
			}
			i++;
		}
	    if(found) {
			DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest() 
		    		   .setRange(
		    		        new DimensionRange()
		    		            .setSheetId(0)
		    		            .setDimension("ROWS")
		    		            .setStartIndex(i)
		    		            .setEndIndex(i+1)
		    		  );

		    		List<Request> requests = new ArrayList<>();
		    		requests.add(new Request().setDeleteDimension(deleteRequest));

		    		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
		    		service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
		    		
		    		logger.info(timeCardDetails.getUserKey() + " Deleted ");		
		}
	    
	  }
    
    public static void main(String... args) throws IOException, GeneralSecurityException {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy").withLocale(Locale.US);
    	LocalDate today = LocalDate.parse("2019-03-29");
    	
    	System.out.println(today.format(formatter));
    }
}