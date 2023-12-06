package com.packsize.articlebatch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session")
public class ArticleComponent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger();
	private ArticleDetails articleDetails;
	
	private String[] printHeadersArr;
    private String[] printDataArr;
    
    private ArrayList<String> dupList = new ArrayList<String>();
    private StringBuffer dupRows = new StringBuffer();
    
    private Map<String,Integer> corrugateQualityMap = new HashedMap<String, Integer>();
    
    private static int partNumIndex,descriptionIndex,testIndex, fluteIndex, fefcoIndex, lengthIndex, widthIndex, depthIndex, styleIndex = 0;
    
	@PostConstruct
    private void init() {
		initialSetup();
	}
	
	private void initialSetup() {
		logger.info("In initialSetup()");
		setArticleDetails(new ArticleDetails());
		getArticleDetails().setEmDesigns(loadEMDesignXValues());
		getArticleDetails().setIqDesigns(loadIQDesignXValues());
		getArticleDetails().setX4Designs(loadX4DesignXValues());
		articleDetails.setFile(null);
		articleDetails.setUploadFile(null);
		articleDetails.setCorrugateQuality(null);
	}
	
	public void reSet() {
		initialSetup();
		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Page reset completed."));
	}
	
	public void validations() {
		logger.info("In validations()");
		if((articleDetails.isLabelRequired() && generatePrintData()) || !articleDetails.isLabelRequired()) {
			if(articleDetails.getUploadFile() != null && articleDetails.getUploadFile().getFileName().contains(".xlsx")) {
				logger.info(articleDetails.getUploadFile().getFileName());
				parseExcelData();
			}else {
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please choose valid file to upload.")); 
				logger.error("Please upload File.");
			}
		}
		
	}
	
	/**
	 * This method is used to parse input file and select required excel cell data and gather required information for final out put CSV file
	 */
	private void parseExcelData()
    {
		logger.info("In parseExcelData()");
		boolean isStyleBlank = false;
		boolean isValid = true;
		String articleName;
		String description;

		dupList.clear();
		dupRows.setLength(0);
		try {
	        /*  *********************PENDING ITEMS*******************
	            input validation pending, 
	            generate print headers and respective values, 'X' values from user input
	            ************************Input File Headers Order******************************** 
	            Sort, Part Number, Description, Test, Flute, FEFCO,	Length (in), Width (in), Depth (in), Annual Quantity, Sq. Ft., Blank Width (in), Total Sq. Ft., % Total Volume, Style
	        */
				// formatter for all your data to string from date/number etc
	            DataFormatter formatter = new DataFormatter();
				InputStream fis = articleDetails.getUploadFile().getInputStream();
	            XSSFWorkbook input = new XSSFWorkbook(fis);
	            XSSFSheet sheet = input.getSheetAt(0);
	            input.close();
	            
	            Iterator<Row> corrugateQuality = sheet.iterator();
	            if(articleDetails.isCorrugateQualityRequired()) {
	            	corrugateQuality(corrugateQuality, formatter);
	            }
	            
	            Map < Integer, String[] > articleInfo = new TreeMap < Integer, String[] >();
	            int index = 1;
	            
	            Iterator<Row> rowIterator = sheet.iterator();
	            while (rowIterator.hasNext()) {
	    
	                Row row = rowIterator.next();
	                if(row.getRowNum() ==0){
	
	                	for(int i = 0; i<=row.getPhysicalNumberOfCells(); i++) {
	                		getHeaderIndex(formatter, row, i);
	                	}
	                	
	                	System.out.println("Part Number " + partNumIndex);
	                	System.out.println("Description " + descriptionIndex);
	                	System.out.println("Test " + testIndex);
	                	System.out.println("Flute " + fluteIndex);
	                	System.out.println("FEFCO " + fefcoIndex);
	                	System.out.println("Length (in) " + lengthIndex);
	                	System.out.println("Width (in) " + widthIndex);
	                	System.out.println("Depth (in) " + depthIndex);
	                	System.out.println("Style " + styleIndex);
            	
	                	if(partNumIndex == 0 || descriptionIndex == 0 || testIndex == 0 || fluteIndex == 0 || fefcoIndex == 0 || lengthIndex == 0 || widthIndex == 0 || depthIndex == 0 || styleIndex == 0){
	                    	   FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Excel columns validations Fail. Make sure all required columns exists."));   
	                    	   logger.info("Excel columns validations Fail");
	                    	   isValid = false;
	                    	   break;
	                       }else{
	                    	   logger.info("Excel columns validations Pass");
		                   }
	                }else{
	                	if(!formatter.formatCellValue(row.getCell(styleIndex)).isBlank()) {
	                		articleName = fillInBlankArticleName(row,formatter).replaceAll("\\,+"," ").replaceAll("\\s+"," ").trim();
	                		description = formatter.formatCellValue(row.getCell(descriptionIndex)).replaceAll("\\,+"," ").replaceAll("\\s+"," ").trim();
	                		articleInfo.put( index++, generateRowDataStructure(articleDetails.isLabelRequired(),printHeadersArr,new String[] { articleName, 
	                																						description, 
		                                                                                                    "Article",
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "","","","","","","","","","",
		                                                                                                    "",
		                                                                                                    ""
		                                                                                                    }));
		
		                    articleInfo.put( index++, generateRowDataStructure(articleDetails.isLabelRequired(),printHeadersArr,new String[] { articleName,                        							//ArticleName
		                    																				description,                                  					//Description
		                                                                                                    "Carton",                                                                           			//Type
		                                                                                                    "1",                                                                                			//Quantity
		                                                                                                    selectDesignIDFromStyle(articleDetails.getMachine(),formatter.formatCellValue(row.getCell(styleIndex))),//DesignId
		                                                                                                    formatter.formatCellValue(row.getCell(lengthIndex)),                                  					//Length
		                                                                                                    formatter.formatCellValue(row.getCell(widthIndex)),                                  					//Width
		                                                                                                    formatter.formatCellValue(row.getCell(depthIndex)),                                  					//Height
		                                                                                                    "",                                                                                 			//Rotation
		                                                                                                    "","","","","","","","","","",                                                      			//DimensionX1 TO X10
		                                                                                                    (articleDetails.isCorrugateQualityRequired() ? corrugateQualityMap.get(formatter.formatCellValue(row.getCell(testIndex)).strip().concat(formatter.formatCellValue(row.getCell(fluteIndex)).strip())).toString() : "1"), //CorrugateQuality                                                                             //CorrugateQuality
		                                                                                                    ""                                                                                 				//CorrugateThickness
		                                                                                                    }));                                                                               				//LabelTemplate
		                if(articleDetails.isLabelRequired())
		                    articleInfo.put( index++, generatePrintLabelData(generateRowDataStructure(articleDetails.isLabelRequired(),printHeadersArr,new String[] { articleName, 
		                    																				description, 
		                                                                                                    "Label",
		                                                                                                    "1", 
		                                                                                                    "", 
		                                                                                                    "",
		                                                                                                    "",
		                                                                                                    "", 
		                                                                                                    "", 
		                                                                                                    "","","","","","","","","","",
		                                                                                                    "", 
		                                                                                                    ""
		                                                                                                    }),formatter, row));
	                	}else {
	                		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage. SEVERITY_ERROR, "Error", "'Style' column values are empty on row " + (row.getRowNum() + 1) + ". Style column values should not be empty.")); 
	                		isStyleBlank = true;
	                	}
	                }
	            }
	            if(!isStyleBlank && isValid) {
	            	addDesignDimensions(articleDetails.getMachine(), articleInfo);	            
		            generateCSV(articleInfo);
		            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "CSV file generated, to download click 'Download' button."));  
		            if(!dupRows.isEmpty()) {
		            	FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Input file contains duplicate data at 'Sort' index values " + dupRows.toString()));  
		            }
		            
	            }
	        } catch (Exception e) {
	        	FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not able to generate CSV file.")); 
	        	logger.error(e.getMessage());
	        }
		
    }
	
	private void getHeaderIndex(DataFormatter formatter, Row row, int cellIndex) {
		switch(formatter.formatCellValue(row.getCell(cellIndex))) {
			case "Part Number" : partNumIndex = cellIndex; break;
			case "Description" : descriptionIndex = cellIndex; break;
			case "Test" : testIndex = cellIndex; break;
			case "Flute" : fluteIndex = cellIndex; break;
			case "FEFCO" : fefcoIndex = cellIndex; break;
			case "Length (in)" : lengthIndex = cellIndex; break;
			case "Width (in)" : widthIndex = cellIndex; break;
			case "Depth (in)" : depthIndex = cellIndex; break;
			case "Style" : styleIndex = cellIndex; break;
			default: break;
		}
	}
	
	private void corrugateQuality(Iterator<Row> rowIterator,DataFormatter formatter) {
		logger.info("In corrugateQuality");
		
		corrugateQualityMap.clear();
		int index = 0;
		while (rowIterator.hasNext()) {
		    Row row = rowIterator.next();
            if(row.getRowNum() !=0){
            	String key = formatter.formatCellValue(row.getCell(testIndex)).strip().concat(formatter.formatCellValue(row.getCell(fluteIndex)).strip());
            	if(corrugateQualityMap.containsKey(key)) {
            		
            	}else {
            		index++;
            		corrugateQualityMap.put(key, index);
            	}
            }else if(row.getRowNum() ==0){
        		for(int i = 0; i<=row.getPhysicalNumberOfCells(); i++) {
            		getHeaderIndex(formatter, row, i);
            	}
            }
        }
		
		if(!corrugateQualityMap.isEmpty())
			articleDetails.setCorrugateQuality(corrugateQualityMap.toString());
		
		logger.info("Identified Test and Flute's are :" +corrugateQualityMap.toString());
	}
	
	private void identifyDuplicateArticles(Row row, DataFormatter formatter) {
		String rowVal = formatter.formatCellValue(row.getCell(styleIndex)).strip()+formatter.formatCellValue(row.getCell(fluteIndex)).strip()+formatter.formatCellValue(row.getCell(lengthIndex)).strip()+formatter.formatCellValue(row.getCell(widthIndex)).strip()+formatter.formatCellValue(row.getCell(depthIndex)).strip();
		if(dupList.contains(rowVal)) {
			dupRows.append(formatter.formatCellValue(row.getCell(0)).strip()+" ");
		}else {
			dupList.add(rowVal);
		}
	}
	
	private String fillInBlankArticleName(Row row, DataFormatter formatter) {
		identifyDuplicateArticles(row,formatter);
		
		if(StringUtil.isNotBlank(formatter.formatCellValue(row.getCell(partNumIndex)))) {
			return formatter.formatCellValue(row.getCell(partNumIndex));
		}else if(StringUtil.isNotBlank(formatter.formatCellValue(row.getCell(descriptionIndex)))){
			return formatter.formatCellValue(row.getCell(descriptionIndex));
		}else { //Style-flute-LxWxH
			return formatter.formatCellValue(row.getCell(styleIndex))+"-"+ formatter.formatCellValue(row.getCell(fluteIndex))+"-"+ formatter.formatCellValue(row.getCell(lengthIndex)) + "x" + formatter.formatCellValue(row.getCell(widthIndex)) + "x" + formatter.formatCellValue(row.getCell(depthIndex));
		}
	}
	
	private void addDesignDimensions(String machine, Map < Integer, String[] > articleInfo) {
		switch(machine){
			case "EM" : addDesignDimensionsForEM(articleInfo); break;
			case "IQ" : addDesignDimensionsForIQ(articleInfo); break;
			case "X4" : addDesignDimensionsForX4(articleInfo); break;
			default: break;
		}
	}
	
	private boolean generatePrintData() {
		boolean success = true;
		printHeadersArr = articleDetails.getPrintHeaders().split(",");
		printDataArr = articleDetails.getPrintData().split(",");
		
		if(printHeadersArr.length != printDataArr.length) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Print Headers ',' seperated values must be equal to Print Data ',' seperated values.")); 
		}else {
			for(int i=0 ;i <printHeadersArr.length; i++) {
				if(!printHeadersArr[i].equalsIgnoreCase("LabelTemplate"))
						printHeadersArr[i] = "PrintData".concat(printHeadersArr[i]);
			}
		}
		return success;
	}
	
	/**
	 * PrintData variables and LabelTemplate values setup.
	 * @param rowData
	 * @param formatter
	 * @param row
	 * @return
	 */
    private String[] generatePrintLabelData(String[] rowData, DataFormatter formatter, Row row){
        for(int i=0; i<printHeadersArr.length; i++){
           switch(printDataArr[i]){
                case "ArticleName" : rowData[(rowData.length - printHeadersArr.length) + i] =   formatter.formatCellValue(row.getCell(partNumIndex)); break;
                case "Design" : rowData[(rowData.length - printHeadersArr.length) + i] =   selectDesignIDFromStyle(articleDetails.getMachine(),formatter.formatCellValue(row.getCell(styleIndex))); break;//DesignId
                case "Length" : rowData[(rowData.length - printHeadersArr.length) + i] =   formatter.formatCellValue(row.getCell(lengthIndex)); break;                                  //Length
                case "Width"  : rowData[(rowData.length - printHeadersArr.length) + i] =   formatter.formatCellValue(row.getCell(widthIndex)); break;                                  //Width
                case "Height" : rowData[(rowData.length - printHeadersArr.length) + i] =   formatter.formatCellValue(row.getCell(depthIndex)); break;                                  //Height
                case "Extra1" : rowData[(rowData.length - printHeadersArr.length) + i] =   formatter.formatCellValue(row.getCell(15)); break;
                default :  rowData[(rowData.length - printHeadersArr.length) + i] =   printDataArr[i]; break;
            }
        }
        return rowData;
    }
	
    /**
     * Generate space holder for header information and header values
     * @param printLabel
     * @param printHeaders
     * @param currentRowData
     * @return
     */
    private String[] generateRowDataStructure(boolean printLabel, String[] printHeaders, String[] currentRowData){
        String[] returnArray = currentRowData;
        
        if(returnArray[2].equalsIgnoreCase("Carton") && returnArray[4].equalsIgnoreCase("1101001") && returnArray[7].isBlank())
        	returnArray[7] = "0";
	        
        if(printLabel)
            returnArray = generateNewRowDataStructure(printHeaders, currentRowData); 
        
		if(returnArray[2].equalsIgnoreCase("Carton") && articleDetails.isUpdateDims()) 
			returnArray = addInchesToDims(returnArray,articleDetails);
		
		//swapping dim's based on design width logic
		/*
		 * if(returnArray[2].equalsIgnoreCase("Carton") &&
		 * StringUtil.isNotBlank(articleDetails.getMaxZfold()) &&
		 * (Integer.parseInt(articleDetails.getMaxZfold()) > 0 )) returnArray =
		 * swapDims(returnArray,93.8,0.28);
		 */
		 
            
        return returnArray;
    }

    private static String[] generateNewRowDataStructure(String[] printHeaders, String[] currentRowData){
        
        String[] newData = new String[currentRowData.length + printHeaders.length];
        
        for(int i=0; i<currentRowData.length; i++){
            newData[i] = currentRowData[i];
        }

        for(int i=0; i<printHeaders.length; i++){
            newData[currentRowData.length + i] = "";
        }

        return newData;
    }

    /**
     * Adds new header variables to existing header row. 
     * @param articleInfo
     */
    private void generatePrintHeaders(Map < Integer, String[] > articleInfo ){
        String[] headers= articleInfo.get(0);
        String[] newHeaders = new String[headers.length + printHeadersArr.length];
        
        for(int i=0; i<headers.length; i++){
            newHeaders[i] = headers[i];
        }

        for(int i=0; i<printHeadersArr.length; i++){
            newHeaders[headers.length + i] = printHeadersArr[i];
        }
        articleInfo.put(0, newHeaders);
    }

    /**
     * Final action method that is responsible to generate CSV file with necessary values.
     * @param articleInfo
     */
    private void generateCSV(Map < Integer, String[] > articleInfo){
        try { 
            
            articleInfo.put( 0, new String[] { "ArticleName", "Description", "Type", "Quantity", "DesignId", "Length", "Width", "Height", "Rotation", "DimensionX1","DimensionX2","DimensionX3","DimensionX4","DimensionX5","DimensionX6","DimensionX7","DimensionX8","DimensionX9","DimensionX10", "CorrugateQuality", "CorrugateThickness"});

            if(articleDetails.isLabelRequired())
                generatePrintHeaders(articleInfo);
            logger.info(articleInfo.keySet().toString());
            Set < Integer > rowIDS = articleInfo.keySet();
           
            // For storing data into CSV files
            StringBuffer data = new StringBuffer();
            for (Integer row = 0; row <  rowIDS.size(); row++) {
                String [] cellArr = articleInfo.get(row);
                for (Integer cell = 0; cell < cellArr.length; cell++){
                    if(cell != (cellArr.length-1)){
                        data.append(cellArr[cell] + ","); 
                    }else{
                        data.append(cellArr[cell]); 
                    }
                }
                // appending new line after each row
                data.append('\n');
            }

            articleDetails.setFile(DefaultStreamedContent.builder()
    		        .name("articleList.csv")
    		        .contentType("application/vnd.ms-excel ")
    		        .stream(() -> new ByteArrayInputStream(data.toString().getBytes(StandardCharsets.UTF_8)))
    		        .build());
            
            logger.info("Excel File has been created successfully."); 
        } catch (Exception e) {
        	logger.error(e.toString());
        }
        
    }
    
    /**
     * This method is used to swap dim based on design if design is not able to produced 
     * @param row
     * @param maxWidth
     * @return
     */
    private static String[] swapDims(String[] row, ArticleDetails articleDetails) {
    	logger.info("In swapDims()");
    	
    			Double l = Double.parseDouble(row[5]);
    			Double w = Double.parseDouble(row[6]);
    			Double h = Double.parseDouble(row[7]);
    			
    			Double templ = 0.0;
    			Double tempw = 0.0;
    			Double temph = 0.0;
    			
    			Double maxzfold = Double.parseDouble(articleDetails.getMaxZfold());
    			
    			if(row[4] == "4100001") {//FPF 2H+L+4FT
    				if((2*h) + l + (4*0.28) > maxzfold) {
    					templ = l;
    					tempw = w;
    					row[5] = tempw.toString();
    					row[6] = templ.toString();
    					logger.info("Swapped for row "+ row[0]);
    				}
    			}else if(row[4] == "2031102") {//FOL FGF H+2W+8FT
    				if(h + (2*w) + (8*0.28) > maxzfold) {//
    					tempw = w;
    					temph = h;
    					row[6] = temph.toString();
    					row[7] = tempw.toString();
    					logger.info("Swapped for row "+ row[0]);
    				}
    			}
    	return row;
   }
    
    private static String[] addInchesToDims(String[] row, ArticleDetails articleDetails) {
    	if(!row[5].isBlank() && !row[6].isBlank() && !row[7].isBlank()) {
    		Double l = Double.parseDouble(row[5]);
    		Double w = Double.parseDouble(row[6]);
    		Double h = Double.parseDouble(row[7]);
    		
    		Double addToL = Double.parseDouble(articleDetails.getAddToL());
    		Double addToW = Double.parseDouble(articleDetails.getAddToW());
    		Double addToH = Double.parseDouble(articleDetails.getAddToH());
    		
    		Double templ = l + addToL;
    		Double tempw = w + addToW;
    		Double temph = h + addToH;
    		    			
    		row[5] = templ.toString();
    		row[6] = tempw.toString();
    		row[7] = temph.toString();
    	}else {
    		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "Article not updated with extra DIM's, Data may contain empty string in Length or Width or Height for Article "+row[0])); 
    	}
    	
    	return row;
   }
    
    private static String selectDesignIDFromStyle(String machine, String style){
        String designID = "";
        switch(machine){
            case "EM" : designID = generateEMDesignId(style); break;
            case "IQ" : designID = generateIQDesignId(style); break;
            case "X4" : designID = generateX4DesignId(style); break;
            default: break;
        }
        return designID;
    }
    
    public void updateDesignValues() {
    	if(articleDetails.getMachine() != null)
	    	switch (articleDetails.getMachine()) {
			case "EM":	retriveEMDesigns(); break;
			case "IQ":	retriveIQDesigns(); break;
			case "X4":	retriveX4Designs(); break;
			default:
				break;
			}
    }
    
    private void retriveEMDesigns() {
    	articleDetails.setDesigns(articleDetails.getEmDesigns().stream().collect(Collectors.toList()));
    }
    
    private void retriveIQDesigns() {
    	articleDetails.setDesigns(articleDetails.getIqDesigns().stream().collect(Collectors.toList()));
    }
    
    private void retriveX4Designs() {
    	articleDetails.setDesigns(articleDetails.getX4Designs().stream().collect(Collectors.toList()));
    }
    
    /**
     * Add dimension X values for allowed EM machine designs.
     * @param articleInfo
     */
    private void addDesignDimensionsForEM(Map < Integer, String[] > articleInfo){
        for(String[] str : articleInfo.values()){
            if(str[2].equalsIgnoreCase("Carton")){
                switch(str[4]){
                    case "2030100": str[9] = articleDetails.getEmDesigns().get(0).getdX1(); break;//X1
                    case "2000100": str[9] = articleDetails.getEmDesigns().get(1).getdX1(); break;//X1
                    case "4010000": str[9] = articleDetails.getEmDesigns().get(2).getdX1();; break;//X1
                    case "4120000": str[9] = articleDetails.getEmDesigns().get(3).getdX1(); break;//X1
                    case "4030000": str[9] = articleDetails.getEmDesigns().get(4).getdX1();; break;//X1
                    case "1101000": str[9] = articleDetails.getEmDesigns().get(5).getdX1(); str[10] = articleDetails.getEmDesigns().get(5).getdX2(); str[11] = articleDetails.getEmDesigns().get(5).getdX3(); str[12] = articleDetails.getEmDesigns().get(5).getdX4(); str[13] = articleDetails.getEmDesigns().get(5).getdX5(); str[14] = articleDetails.getEmDesigns().get(5).getdX6(); str[15] = articleDetails.getEmDesigns().get(5).getdX7(); str[16] = articleDetails.getEmDesigns().get(5).getdX8(); str[17] = articleDetails.getEmDesigns().get(5).getdX9(); str[18] = articleDetails.getEmDesigns().get(5).getdX10(); break;//X1,X2,X3,X4,X5,X6,X7,X8,X9,X10
                    case "2020100": str[9] = articleDetails.getEmDesigns().get(6).getdX1(); str[10] = articleDetails.getEmDesigns().get(6).getdX2(); break;//X1,X2
                    case "4210001": str[9] = articleDetails.getEmDesigns().get(7).getdX1(); str[10] = articleDetails.getEmDesigns().get(7).getdX2(); break;//X1,X2
                    case "2010100": str[9] = articleDetails.getEmDesigns().get(8).getdX1(); break;//X1
                    case "3310000": str[9] = articleDetails.getEmDesigns().get(9).getdX1(); break;//X1
                    case "3310001": str[9] = articleDetails.getEmDesigns().get(10).getdX1(); break;//X1
                    default: break;
                }
            }
        }
    }
    
    private static String generateEMDesignId(String style){

        String designID = "";
        switch(style){
            case "FOL": designID = "2030100"; break;//X1
            case "FFFPF": designID = "4100001"; break;
            case "HSC": designID = "2000100"; break;//X1
            case "OPF": designID = "4010000"; break;//X1
            case "OPF DF": designID = "4120000"; break;//X1
            case "OPF IA": designID = "4030000"; break;//X1
            case "PAD": designID = "1101001"; break;
            case "PAD/CREASE": designID = "1101000"; break;//X1,X2,X3,X4,X5,X6,X7,X8,X9,X10
            case "POL": designID = "2020100"; break;//X1,X2
            case "RE FF FL": designID = "4430001"; break;
            case "RETT": designID = "4210001"; break;//X1,X2
            case "RSC": designID = "2010100"; break;//X1
            case "RT INS ED": designID = "9141000"; break;
            case "TRAY": designID = "3001200"; break;
            case "TRAY FL": designID = "3310000"; break;//X1
            case "TRAY FL TOP": designID = "3310001"; break;//X1
            case "TRAY TOP": designID = "3001202"; break;
            case "CSS OPF": designID = "4020000"; break;
            default: FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Not able to find EM style code "+ style)); break;
        }

        return designID;
    }
    
    /**
     * Creates space holder for EM machine dimension values
     * @return
     */
	private List<Design> loadEMDesignXValues() {
		articleDetails.getEmDesigns().clear();
		
		Design d1 = new Design();
		d1.setDesignName("FOL");
		d1.setDesignID("2030100");
		d1.setdX1("0");
		articleDetails.getEmDesigns().add(d1);
		
		Design d2 = new Design();
		d2.setDesignName("HSC");
		d2.setDesignID("2000100");
		d2.setdX1("0");
		articleDetails.getEmDesigns().add(d2);
		
		Design d3 = new Design();
		d3.setDesignName("OPF");
		d3.setDesignID("4010000");
		d3.setdX1("0");
		articleDetails.getEmDesigns().add(d3);
		
		Design d4 = new Design();
		d4.setDesignName("OPF DF");
		d4.setDesignID("4120000");
		d4.setdX1("0");
		articleDetails.getEmDesigns().add(d4);
		
		Design d5 = new Design();
		d5.setDesignName("OPF IA");
		d5.setDesignID("4030000");
		d5.setdX1("0");
		articleDetails.getEmDesigns().add(d5);
		
		Design d6 = new Design();
		d6.setDesignName("PAD/CREASE");
		d6.setDesignID("1101000");
		d6.setdX1("0");d6.setdX2("0");d6.setdX3("0");d6.setdX4("0");d6.setdX5("0");d6.setdX6("0");d6.setdX7("0");d6.setdX8("0");d6.setdX9("0");d6.setdX10("0");
		articleDetails.getEmDesigns().add(d6);
		
		Design d7 = new Design();
		d7.setDesignID("2020100");
		d7.setDesignName("POL");
		d7.setdX1("0");d7.setdX2("0");
		articleDetails.getEmDesigns().add(d7);
		
		Design d8 = new Design();
		d8.setDesignID("4210001");
		d8.setDesignName("RETT");
		d8.setdX1("0");d8.setdX2("0");
		articleDetails.getEmDesigns().add(d8);
		
		Design d9 = new Design();
		d9.setDesignID("2010100");
		d9.setDesignName("RSC");
		d9.setdX1("0");
		articleDetails.getEmDesigns().add(d9);
		
		Design d10 = new Design();
		d10.setDesignID("3310000");
		d10.setDesignName("TRAY FL");
		d10.setdX1("0");
		articleDetails.getEmDesigns().add(d10);
		
		Design d11 = new Design();
		d11.setDesignID("3310001");
		d11.setDesignName("TRAY FL TOP");
		d11.setdX1("0");
		articleDetails.getEmDesigns().add(d11);
		
		return articleDetails.getEmDesigns();
	}
	
	/**
     * Add dimension X values for allowed IQ machine designs.
     * @param articleInfo
     */
    private void addDesignDimensionsForIQ(Map < Integer, String[] > articleInfo){
        for(String[] str : articleInfo.values()){
            if(str[2].equalsIgnoreCase("Carton")){
                switch(str[4]){
                    case "2030100": str[9] = articleDetails.getIqDesigns().get(0).getdX1(); break;//X1
                    case "2031102": str[9] = articleDetails.getIqDesigns().get(1).getdX1(); break;//X1
                    case "1101000": str[9] = articleDetails.getIqDesigns().get(2).getdX1(); str[10] = articleDetails.getIqDesigns().get(1).getdX2(); str[11] = articleDetails.getIqDesigns().get(1).getdX3(); str[12] = articleDetails.getIqDesigns().get(1).getdX4(); str[13] = articleDetails.getIqDesigns().get(1).getdX5(); str[14] = articleDetails.getIqDesigns().get(1).getdX6(); str[15] = articleDetails.getIqDesigns().get(1).getdX7(); str[16] = articleDetails.getIqDesigns().get(1).getdX8(); str[17] = articleDetails.getIqDesigns().get(1).getdX9(); str[18] = articleDetails.getIqDesigns().get(1).getdX10(); break;//X1,X2,X3,X4,X5,X6,X7,X8,X9,X10
                    case "2021102": str[9] = articleDetails.getIqDesigns().get(3).getdX1(); str[10] = articleDetails.getIqDesigns().get(2).getdX2(); break;//X1,X2
                    case "2310100": str[9] = articleDetails.getIqDesigns().get(4).getdX1(); break;//X1
                    case "2011102": str[9] = articleDetails.getIqDesigns().get(5).getdX1();; break;//X1
                    case "4150000": str[9] = articleDetails.getIqDesigns().get(6).getdX1();; break;//X1
                    default: break;
                }
            }
        }
    }
	
	private static String generateIQDesignId(String style){

        String designID = "";
        switch(style){
        	case "FOL": designID = "2030100"; break;//X1
            case "FOL FGF": designID = "2031102"; break;//X1
            case "FPF": designID = "4091000"; break;
            case "FPF CS": designID = "4111000"; break;
            case "PAD/CREASE": designID = "1101000"; break;//X1,X2,X3,X4,X5,X6,X7,X8,X9,X10
            case "POL FGF": designID = "2021102"; break;//X1,X2
            case "RSC CS": designID = "2310100"; break;//X1
            case "RSC": designID = "2011102"; break;//X1
            case "TRAY": designID = "3001201"; break;
            case "TRAY TOP": designID = "3001203"; break;
            case "OPF": designID = "4150000"; break;//X1
            default: FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Not able to find IQ style code "+ style)); break;
        }

        return designID;
    }
	
	/**
     * Creates space holder for EM machine dimension values
     * @return
     */
	private List<Design> loadIQDesignXValues() {
		articleDetails.getIqDesigns().clear();
		
		Design fol = new Design();
		fol.setDesignName("FOL");
		fol.setDesignID("2030100");
		fol.setdX1("0");
		articleDetails.getIqDesigns().add(fol);
		
		Design fol_fgl = new Design();
		fol_fgl.setDesignName("FOL FGF");
		fol_fgl.setDesignID("2031102");
		fol_fgl.setdX1("0");
		articleDetails.getIqDesigns().add(fol_fgl);
		
		Design pad_crease = new Design();
		pad_crease.setDesignName("PAD/CREASE");
		pad_crease.setDesignID("1101000");
		pad_crease.setdX1("0");pad_crease.setdX2("0");pad_crease.setdX3("0");pad_crease.setdX4("0");pad_crease.setdX5("0");pad_crease.setdX6("0");pad_crease.setdX7("0");pad_crease.setdX8("0");pad_crease.setdX9("0");pad_crease.setdX10("0");
		articleDetails.getIqDesigns().add(pad_crease);
		
		Design pol_fgf = new Design();
		pol_fgf.setDesignName("POL FGF");
		pol_fgf.setDesignID("2021102");
		pol_fgf.setdX1("0");pol_fgf.setdX2("0");
		articleDetails.getIqDesigns().add(pol_fgf);
		
		Design rsc_cs = new Design();
		rsc_cs.setDesignName("RSC CS");
		rsc_cs.setDesignID("2310100");
		rsc_cs.setdX1("0");
		articleDetails.getIqDesigns().add(rsc_cs);
		
		Design rsc = new Design();
		rsc.setDesignName("RSC");
		rsc.setDesignID("2011102");
		rsc.setdX1("0");
		articleDetails.getIqDesigns().add(rsc);
		
		Design opf = new Design();
		opf.setDesignName("OPF");
		opf.setDesignID("4150000");
		opf.setdX1("0");
		articleDetails.getIqDesigns().add(opf);
		
		return articleDetails.getIqDesigns();
	}
	
	private static String generateX4DesignId(String style){

        String designID = "";
        switch(style){
            case "RSC": designID = "2010013"; break;
            case "HSC FGF": designID = "2001193"; break;
            case "POL FGF": designID = "2010179"; break;//X1
            default: FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Not able to find EM style code "+ style)); break;
        }

        return designID;
    }
	
	/**
     * Add dimension X values for allowed IQ machine designs.
     * @param articleInfo
     */
    private void addDesignDimensionsForX4(Map < Integer, String[] > articleInfo){
        for(String[] str : articleInfo.values()){
            if(str[2].equalsIgnoreCase("Carton")){
                switch(str[4]){
                    case "2010179": str[9] = articleDetails.getX4Designs().get(0).getdX1();//X1
                    default: break;
                }
            }
        }
    }
    
    /**
     * Creates space holder for EM machine dimension values
     * @return
     */
	private List<Design> loadX4DesignXValues() {
		articleDetails.getX4Designs().clear();
		
		Design pol_fgf = new Design();
		pol_fgf.setDesignName("POL FGF");
		pol_fgf.setDesignID("2010179");
		pol_fgf.setdX1("0");
		articleDetails.getX4Designs().add(pol_fgf);
		
		return articleDetails.getX4Designs();
	}
	
	public ArticleDetails getArticleDetails() {
		return articleDetails;
	}

	public void setArticleDetails(ArticleDetails articleDetails) {
		this.articleDetails = articleDetails;
	}	

}
