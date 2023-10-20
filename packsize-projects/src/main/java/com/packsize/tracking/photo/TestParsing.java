package com.packsize.tracking.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.packsize.PackSizeLogger;

public class TestParsing {

	private static Map<String,Integer> uniqueMap = new HashedMap<String, Integer>();
	public static void main(String[] args) {
		parseFile();
	}
	
	private static void parseFile() {
		try {
				int index = 0;
				uniqueMap.clear();
				DataFormatter formatter = new DataFormatter();
	            FileInputStream fis=new FileInputStream(new File("C:\\JAVA\\sush\\sample.xlsx"));
	            XSSFWorkbook input = new XSSFWorkbook(fis);
	            XSSFSheet sheet = input.getSheetAt(0);
	            Iterator<Row> rowIterator = sheet.iterator();
	            
	            XSSFSheet newSheet = input.createSheet("Unique Data");
	            
	            
                while (rowIterator.hasNext()) {
                	Row datarow = rowIterator.next();
	                
	                if(datarow.getRowNum() == 0){ 
	                  Row newRow = newSheet.createRow(index);
	                  for(int i = 0; i<datarow.getLastCellNum(); i++) {
	                	  newRow.createCell(i).setCellValue(formatter.formatCellValue(datarow.getCell(i)));
	  				  }
	                  index++;
	                }else { 
	                	String updatedValue = formatter.formatCellValue(datarow.getCell(0)) 
        						.replaceAll("\\d+(?:\\.\\d+)+", "")
        						.replaceAll("\\(.*\\)", "") 
        						.replaceAll("\\[.*\\]", "") 
        						.replaceAll("\\s+"," ")
        						.trim(); 
	                	System.out.println("Formated Value :" + updatedValue + " for cell " + (datarow.getRowNum() + 1));
	                	if(uniqueMap.containsKey(updatedValue)) {
	                		System.out.println("Already Exists.");
	                	}else {
	                		System.out.println("Adding..");
	                		Row newRow = newSheet.createRow(index);
	                		uniqueMap.put(updatedValue, index);
	                		
	                		for(int i = 0; i<datarow.getLastCellNum(); i++) {
		                		if(i == 0) {
		                			newRow.createCell(i).setCellValue(updatedValue);
		                		}else {
		                			newRow.createCell(i).setCellValue(formatter.formatCellValue(datarow.getCell(i)));
		                		}
			                }
	                		
	                		index++;
	                	}
	                }
	                
	             }
	            FileOutputStream fos=new FileOutputStream("C:\\JAVA\\sush\\sample1.xlsx");
	            input.write(fos);
	            fos.close();
	            input.close();
	           } catch (Exception e) {
	        	PackSizeLogger.error(e.getMessage());
	        }
	}

}
