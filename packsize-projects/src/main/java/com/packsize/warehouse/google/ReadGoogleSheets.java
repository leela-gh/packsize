package com.packsize.warehouse.google;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.google.api.services.sheets.v4.model.ValueRange;
import com.packsize.warehouse.WarehouseDetails;
import com.packsize.warehouse.templates.IQFusionChecklistItem;
import com.packsize.warehouse.templates.IQFusionTemplate;
import com.packsize.warehouse.timecard.TimeCardDay;
import com.packsize.warehouse.timecard.TimeCardDetails;
import com.packsize.warehouse.timecard.TimeCardUser;

public class ReadGoogleSheets {
	
	  private static final Logger logger = LogManager.getLogger();
	  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	  private static final String TOKENS_DIRECTORY_PATH = "C:\\Users\\leela.yallabandi\\OneDrive - Packsize International\\Desktop\\DataWareHouse_Prj\\TOKENS_DIRECTORY_PATH";
	  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy").withLocale(Locale.US);
	  private static List<List<Object>> filteredValues = new ArrayList<List<Object>>();
	  private static List<List<Object>> filteredValuesWarehouseDetails = new ArrayList<List<Object>>();
	  private static Map<Integer, Integer> subCheckListSizeByParentID = new HashMap<Integer, Integer>();
	  /**
	   * Global instance of the scopes required by this quickstart.
	   * If modifying these scopes, delete your previously saved tokens/ folder.
	   */
	  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	  private static final String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";

	  /**
	   * Creates an authorized Credential object.
	   *
	   * @param HTTP_TRANSPORT The network HTTP Transport.
	   * @return An authorized Credential object.
	   * @throws IOException If the credentials.json file cannot be found.
	   */
	  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
	      throws IOException {
		  logger.info("getCredentials()");
	    // Load client secrets.
	    InputStream in = ReadGoogleSheets.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

	  /**
	   * Prints the names and majors of students in a sample spreadsheet:
	   * https://docs.google.com/spreadsheets/d/1nGZPIsqYNKUdVQc8O-BzujVqDozfO_7CGrOIsR-o7sc/edit?usp=sharing
	   */
	  public static IQFusionTemplate readDataFromSheets(String user, String assetID) throws IOException, GeneralSecurityException {
		  logger.info("readDataFromSheets() for "+ user +" and asset "+ assetID);
		  
		  // Build a new authorized API client service.
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    final String spreadsheetId = "1nGZPIsqYNKUdVQc8O-BzujVqDozfO_7CGrOIsR-o7sc";
	    final String range = "Sheet1";
	    Sheets service =
	        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
	    ValueRange response = service.spreadsheets().values()
	        .get(spreadsheetId, range)
	        .execute();
	    List<List<Object>> values = response.getValues();
	    filteredValues.clear();
	    
	    if (values == null || values.isEmpty()) {
	    	logger.info("No data found.");
	    } else {
		      for (List row : values) {
		        if(row.get(1).toString().equalsIgnoreCase(assetID)) {
		        	filteredValues.add(row);
		        }
		     }
	    }
	    return retrieveValues(filteredValues, user, assetID);
	  }
	  
	  private static IQFusionTemplate retrieveValues(List<List<Object>> filteredValues, String user, String assetID) {
		  logger.info("retrieveValues() for " + filteredValues);
		  
		  IQFusionTemplate iQFusionTemplate = new IQFusionTemplate();
		  List<IQFusionChecklistItem> returnedList;
		  int index = 0;
		  if(!filteredValues.isEmpty()) {
			  for (List row : filteredValues) {
				  IQFusionChecklistItem item = new IQFusionChecklistItem();
				  
				  item.setParentId(Integer.parseInt(row.get(3).toString()));
				  item.setId(Integer.parseInt(row.get(4).toString()));
				  item.setName((String)row.get(5));
				  item.setHours(Integer.parseInt(row.get(6).toString()));
				  item.setEnable(Boolean.parseBoolean(row.get(7).toString()));
				  item.setContinueItem(Boolean.parseBoolean(row.get(8).toString()));
				  
				  returnedList = retrieveValuesByParentID(iQFusionTemplate,row);
				  if(item.getId() <= returnedList.size()) {
					 returnedList.set((item.getId()-1), item);
				  }else {
					  returnedList.add(item);
				  }
				}
			  savedSubCheckListSizeByParentID(iQFusionTemplate,filteredValues,user,assetID);
			  
		  }
		  return iQFusionTemplate;
		}
	  
	  private static void savedSubCheckListSizeByParentID(IQFusionTemplate iQFusionTemplate, List<List<Object>> filteredValues, String user, String assetID) {
		  logger.info("savedSubCheckListSizeByParentID()");
		  
		  subCheckListSizeByParentID.clear();
		  subCheckListSizeByParentID.put(1, null);
		  subCheckListSizeByParentID.put(2, null);
		  subCheckListSizeByParentID.put(3, null);
		  subCheckListSizeByParentID.put(4, null);
		  subCheckListSizeByParentID.put(5, null);
		  subCheckListSizeByParentID.put(6, null);
		  subCheckListSizeByParentID.put(7, null);
		  subCheckListSizeByParentID.put(8, null);
		  
		  for (Integer key : subCheckListSizeByParentID.keySet()) {
		        int size = 0;
		        for (List row : filteredValues) {
			        if(row.get(0).toString().equalsIgnoreCase(user) && row.get(1).toString().equalsIgnoreCase(assetID) && row.get(3).toString().equalsIgnoreCase(key.toString())) {
			        	size++;
			        }
			        subCheckListSizeByParentID.put(key, size);
			     }
		    }
		  enableStartAndCompleteButtons(iQFusionTemplate,subCheckListSizeByParentID);
	  }
	  
	  private static void enableStartAndCompleteButtons(IQFusionTemplate iQFusionTemplate, Map<Integer, Integer> subCheckListSizeByParentID) {
		  logger.info("enableStartAndCompleteButtons() and size detials : "+ subCheckListSizeByParentID);
		 
		  if(!iQFusionTemplate.isDisableAddItemToPrepToRun() && subCheckListSizeByParentID.get(1) != 0) {
			  iQFusionTemplate.getItemListPrepToRun().get((subCheckListSizeByParentID.get(1)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToImagingThePanel() && subCheckListSizeByParentID.get(2) != 0) {
			  iQFusionTemplate.getItemListImagingThePanel().get((subCheckListSizeByParentID.get(2)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToRunningSoftwareInstaller() && subCheckListSizeByParentID.get(3) != 0) {
			  iQFusionTemplate.getItemListRunningSoftwareInstaller().get((subCheckListSizeByParentID.get(3)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToRoboSetup() && subCheckListSizeByParentID.get(4) != 0) {
			  iQFusionTemplate.getItemListRoboSetup().get((subCheckListSizeByParentID.get(4)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToPlcSettings() && subCheckListSizeByParentID.get(5) != 0) {
			  iQFusionTemplate.getItemListPlcSettings().get((subCheckListSizeByParentID.get(5)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToPacknetSetup() && subCheckListSizeByParentID.get(6) != 0) {
			  iQFusionTemplate.getItemListPacknetSetup().get((subCheckListSizeByParentID.get(6)- 1) + 1).setEnable(true);
		  }
		  if(!iQFusionTemplate.isDisableAddItemToCalibration() && subCheckListSizeByParentID.get(7) != 0) {
			  iQFusionTemplate.getItemListCalibration().get((subCheckListSizeByParentID.get(7)- 1) + 1).setEnable(true);
		  }
	  }
	  
	  private static List<IQFusionChecklistItem> retrieveValuesByParentID(IQFusionTemplate iQFusionTemplate, List row) {
		  
		  List<IQFusionChecklistItem> list = null;
		  switch(row.get(3).toString()) {
				  case "1" : list = iQFusionTemplate.getItemListPrepToRun();
				  					iQFusionTemplate.setTotalHrsPrepToRun(Long.parseLong(row.get(9).toString()));
				  					iQFusionTemplate.setDisableAddItemToPrepToRun(Boolean.parseBoolean(row.get(10).toString()));break;
				  					
				  case "2" : list = iQFusionTemplate.getItemListImagingThePanel();
				  					iQFusionTemplate.setTotalHrsImagingThePanel(Long.parseLong(row.get(9).toString()));
				  					iQFusionTemplate.setDisableAddItemToImagingThePanel(Boolean.parseBoolean(row.get(10).toString()));break;
				  					
				  case "3" : list = iQFusionTemplate.getItemListRunningSoftwareInstaller();
									iQFusionTemplate.setTotalHrsRunningSoftwareInstaller(Long.parseLong(row.get(9).toString()));
									iQFusionTemplate.setDisableAddItemToRunningSoftwareInstaller(Boolean.parseBoolean(row.get(10).toString()));break;
									
				  case "4" : list = iQFusionTemplate.getItemListRoboSetup();
									iQFusionTemplate.setTotalHrsRoboSetup(Long.parseLong(row.get(9).toString()));
									iQFusionTemplate.setDisableAddItemToRoboSetup(Boolean.parseBoolean(row.get(10).toString()));break;
									
				  case "5" : list = iQFusionTemplate.getItemListPlcSettings();
									iQFusionTemplate.setTotalHrsPlcSettings(Long.parseLong(row.get(9).toString()));
									iQFusionTemplate.setDisableAddItemToPlcSettings(Boolean.parseBoolean(row.get(10).toString()));break;
									
				  case "6" : list = iQFusionTemplate.getItemListPacknetSetup();
									iQFusionTemplate.setTotalHrsPacknetSetup(Long.parseLong(row.get(9).toString()));
									iQFusionTemplate.setDisableAddItemToPacknetSetup(Boolean.parseBoolean(row.get(10).toString()));break;
									
				  case "7" : list = iQFusionTemplate.getItemListCalibration();
									iQFusionTemplate.setTotalHrsCalibration(Long.parseLong(row.get(9).toString()));
									iQFusionTemplate.setDisableAddItemToCalibration(Boolean.parseBoolean(row.get(10).toString()));break;
				  default : break;
		  }
		  return list;
		  
	  }
	  
	  public static List<WarehouseDetails> readWarehouseDetailsFromSheets(String user) throws IOException, GeneralSecurityException {
		  logger.info("readWarehouseDetailsFromSheets()");
		  
		  // Build a new authorized API client service.
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    final String spreadsheetId = "1bbs9YjxG_y9QgErZGawJilHEQ3sHp2dMXdTGRbvOYrE";
	    final String range = "Sheet1";
	    Sheets service =
	        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
	    ValueRange response = service.spreadsheets().values()
	        .get(spreadsheetId, range)
	        .execute();
	    List<List<Object>> values = response.getValues();
	    //filteredValuesWarehouseDetails.clear();
	    
	    if (values == null || values.isEmpty()) {
	    	logger.info("No data found.");
	    } else {
	    	logger.info("Rows in sheet :" + values.size());
	    	values.remove(0);
		      //for (List row : values) {
		        //if(row.get(0).toString().equalsIgnoreCase(user)) {
		        	//filteredValuesWarehouseDetails.add(row);
		        //}
		    // }
	    }
	    return retrieveValuesWarehouseDetails(values);
	  }
	  
	  private static List<WarehouseDetails> retrieveValuesWarehouseDetails(List<List<Object>> values) {
		  logger.info("retrieveValuesWarehouseDetails()");
		  
		  List<WarehouseDetails> WarehouseDetailsList = new ArrayList<WarehouseDetails>();
		  		  
		  for (List row : values) {
			  WarehouseDetails warehouseDetails = new WarehouseDetails();
			  
			  warehouseDetails.setName((String)row.get(0));
			  warehouseDetails.setAssetID(Long.parseLong(row.get(1).toString()));
			  warehouseDetails.setMachineType((String)row.get(2));
			  warehouseDetails.setStatus((String)row.get(3));
			  if(row.size() == 5) {
				  warehouseDetails.setTotalHrs(Long.parseLong(row.get(4).toString())); 
			  }
			  WarehouseDetailsList.add(warehouseDetails);
		   }
		  return WarehouseDetailsList;
		}
	  
	  public static List<TimeCardDetails> readTimecardDetailsFromSheets(String user) throws IOException, GeneralSecurityException {
		  logger.info("readTimecardDetailsFromSheets()");
		  
		  // Build a new authorized API client service.
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
	    //filteredValuesWarehouseDetails.clear();
	    
	    if (values == null || values.isEmpty()) {
	    	logger.info("No data found.");
	    } else {
	    	logger.info("Rows in sheet :" + values.size());
	    	values.remove(0);
		      //for (List row : values) {
		        //if(row.get(0).toString().equalsIgnoreCase(user)) {
		        	//filteredValuesWarehouseDetails.add(row);
		        //}
		    // }
	    }
	    return retrieveValuesTimeCardDetails(values);
	  }
	  
	  public static List<TimeCardUser> readTimecardUsersFromSheets() throws IOException, GeneralSecurityException {
		  logger.info("readTimecardUsersFromSheets()");
		  
		  // Build a new authorized API client service.
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    final String spreadsheetId = "11v-TQvrj0VM5oGQ-gE1rw136hJulUVZ3d_mz__xpvwo";
	    final String range = "Sheet1";
	    Sheets service =
	        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
	    ValueRange response = service.spreadsheets().values()
	        .get(spreadsheetId, range)
	        .execute();
	    List<List<Object>> values = response.getValues();
	    //filteredValuesWarehouseDetails.clear();
	    
	    if (values == null || values.isEmpty()) {
	    	logger.info("No data found.");
	    } else {
	    	logger.info("Rows in sheet :" + values.size());
	    	values.remove(0);
		      //for (List row : values) {
		        //if(row.get(0).toString().equalsIgnoreCase(user)) {
		        	//filteredValuesWarehouseDetails.add(row);
		        //}
		    // }
	    }
	    return retrieveValuesTimeCardUsers(values);
	  }
	  
	  private static List<TimeCardUser> retrieveValuesTimeCardUsers(List<List<Object>> values) {
		  logger.info("retrieveValuesTimeCardUsers()");
		  
		  List<TimeCardUser> timeCardUsersList = new ArrayList<TimeCardUser>();
		  for (List row : values) {
			  TimeCardUser timeCardUser = new TimeCardUser();
			  
			  timeCardUser.setUserName((String)row.get(0));
			  timeCardUser.setPsw((String)row.get(1));
			  timeCardUser.setEmail((String)row.get(2));
			  timeCardUser.setType((String)row.get(3));
			  timeCardUser.setRole((String)row.get(4));
			  
			  timeCardUsersList.add(timeCardUser);
		  }
		  return timeCardUsersList;
		}
	  
	  private static List<TimeCardDetails> retrieveValuesTimeCardDetails(List<List<Object>> values) {
		  logger.info("retrieveValuesTimeCardDetails()");
		  
		  List<TimeCardDetails> timeCardDetailsList = new ArrayList<TimeCardDetails>();
		  for (List row : values) {
			  TimeCardDetails timeCardDetails = new TimeCardDetails();
			  System.out.println(row);
			  System.out.println("*********");
			  String timeCardData = (String)row.get(1);
			  String[] split = timeCardData.split("\\{\\}");
			  processTimecardDetails(split, timeCardDetails);
			  
			  logger.info("****Generating Time Card dates to Java Map*****");
			  String splitTimeCardCu = split[1].substring(1,split[1].lastIndexOf("}"));
			  String[] splitTimeCardCuAr = splitTimeCardCu.split("],");
			  for(int i=0;i<splitTimeCardCuAr.length;i++) {
				  processTimeCardMap(splitTimeCardCuAr[i].replaceAll("[\\[\\]]", "").trim(),timeCardDetails);
			  }
			  
			  logger.info("****Generating Time Card Day to Java Object*****");
			  String splitTimeCardDay = split[2].substring(1,split[2].lastIndexOf("]"));
			  String[] splitTimeCardDay2 = splitTimeCardDay.split("next,");
			  for(int i=0;i<splitTimeCardDay2.length;i++) {
				 processTimeCardDays(splitTimeCardDay2[i].replace("next", "").trim(), timeCardDetails.getTimeCardDayList());
			  }
			  
			  timeCardDetails.setUser((String)row.get(2));
			  timeCardDetails.setSubmitForApproval(Boolean.parseBoolean(row.get(3).toString()));
			  timeCardDetails.setApproved(Boolean.parseBoolean(row.get(4).toString()));
			  timeCardDetailsList.add(timeCardDetails);
		}
		  
		  return timeCardDetailsList;
		}
	  
	 private static void processTimecardDetails(String[] split, TimeCardDetails timeCardDetails ) {
		  logger.info("In processTimecardDetails()");
		  
		  timeCardDetails.setUserKey(split[3]);
		  timeCardDetails.setWeekID(Integer.valueOf(split[4]));
		  timeCardDetails.setSunTotal(Integer.valueOf(split[5]));
		  timeCardDetails.setMonTotal(Integer.valueOf(split[6]));
		  timeCardDetails.setTueTotal(Integer.valueOf(split[7]));
		  timeCardDetails.setWedTotal(Integer.valueOf(split[8]));
		  timeCardDetails.setThurTotal(Integer.valueOf(split[9]));
		  timeCardDetails.setFriTotal(Integer.valueOf(split[10]));
		  timeCardDetails.setSatTotal(Integer.valueOf(split[11]));
		  timeCardDetails.setWeekTotal(Integer.valueOf(split[12]));
	  }
	  
	  private static TimeCardDay processTimeCardDays(String day, List<TimeCardDay> timeCardDayList) {
		  logger.info("In processTimeCardDay()");
		  TimeCardDay timeCardDay = new TimeCardDay();
		  
		  Object[] arr = day.replaceAll("[\\[\\]]", "").trim().split(",");
		  timeCardDay.setProjectName((String)arr[0]);
		  timeCardDay.setSun(Integer.parseInt(arr[1].toString()));
		  timeCardDay.setMon(Integer.parseInt(arr[2].toString()));
		  timeCardDay.setTue(Integer.parseInt(arr[3].toString()));
		  timeCardDay.setWed(Integer.parseInt(arr[4].toString()));
		  timeCardDay.setThur(Integer.parseInt(arr[5].toString()));
		  timeCardDay.setFri(Integer.parseInt(arr[6].toString()));
		  timeCardDay.setSat(Integer.parseInt(arr[7].toString()));
		  
		  timeCardDayList.add(timeCardDay);
		  
		  return timeCardDay;
	  }
	  
	  private static void processTimeCardMap(String map, TimeCardDetails timeCardDetails) {
		  logger.info("In processTimeCardMap() : " + map);
		  
		  List<LocalDate> datesList = new ArrayList<LocalDate>();
		  Object[] arr = map.split("=");
		  String key = (String) arr[0];
		  String[] dates = arr[1].toString().split(",");
		  for(String date : dates) {
			  datesList.add(LocalDate.parse(date.trim()));
		  }
		  timeCardDetails.getTimeCards().put(key, datesList);
		  
		  	timeCardDetails.setSunday(timeCardDetails.getTimeCards().get(key).get(0).format(formatter));
			timeCardDetails.setMonday(timeCardDetails.getTimeCards().get(key).get(1).format(formatter));
			timeCardDetails.setTuesday(timeCardDetails.getTimeCards().get(key).get(2).format(formatter));
			timeCardDetails.setWednesday(timeCardDetails.getTimeCards().get(key).get(3).format(formatter));
			timeCardDetails.setThursday(timeCardDetails.getTimeCards().get(key).get(4).format(formatter));
			timeCardDetails.setFriday(timeCardDetails.getTimeCards().get(key).get(5).format(formatter));
			timeCardDetails.setSaturday(timeCardDetails.getTimeCards().get(key).get(6).format(formatter));
	  }
	  
	  public static void main(String... args) throws IOException, GeneralSecurityException {
		  readTimecardDetailsFromSheets("");
	  }
}
