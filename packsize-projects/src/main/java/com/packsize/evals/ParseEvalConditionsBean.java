package com.packsize.evals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session")
public class ParseEvalConditionsBean {

	private static Map<String, String> conditionsMapStringFormat = new HashMap<String, String>();
	private static Map<String, Element> conditionsMapElements = new HashMap<String, Element>();
	private static List<PipeLineRules> rules = new ArrayList<PipeLineRules>();
	private String designPath;
	private StringWriter details; 
	private String noOfFilesProcessed;

	public StringWriter getDetails() {
		return details;
	}

	public void setDetails(StringWriter details) {
		this.details = details;
	}

	public String getDesignPath() {
		return designPath;
	}

	public void setDesignPath(String designPath) {
		this.designPath = designPath;
	}

	/*
	 * public static void main(String[] args) { getConditions(); //generateJSON(); }
	 */
	public void reset() {
		System.out.println("In reset()");
		setDesignPath(null);
		setDetails(null);
		setNoOfFilesProcessed("0");
	}
	
	public void getConditions() {
		System.out.println("In getConditions()");
		System.out.println(designPath);
		int counter = 0;
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			File folder = new File(designPath);
			
			File[] listOfFiles = folder.listFiles();
			details = new StringWriter();
			for (File file : listOfFiles) {
					File inputFile = new File(file.getAbsoluteFile().toString());
					
					if(FilenameUtils.getExtension(inputFile.getName()).equalsIgnoreCase("ezd")) {
					Document document = saxBuilder.build(inputFile);
					Element conditionElement = document.getRootElement().getChild("design").getChild("conditions");
					conditionsMapStringFormat.clear();
					conditionsMapElements.clear();
					rules.clear();
					
					details.write("\n\n*****************************File : "+ inputFile.getName() +"*****************************");
					if(conditionElement != null) {
						List<Element> conditionList = conditionElement.getChildren();
								
						for (Element condition : conditionList) {
							String mapID = condition.getAttribute("id").getValue();
							conditionsMapStringFormat.put(mapID, returnTagContent(condition));
							conditionsMapElements.put(mapID, condition);
						}
						details.write("\nConditions are : ");
						for (Map.Entry<String,String> entry : conditionsMapStringFormat.entrySet()) 
							details.write("\nCondition " + entry.getKey() +", Value = " + entry.getValue());
						//iterateCondition(conditionList);
					}else {
						details.write("\nNo Condition ");
					}
					counter++;
				}
			}
			setNoOfFilesProcessed(String.valueOf(counter));
			System.out.println(details);
		} 
		catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static void iterateCondition(List<Element> conditionList) {
		for (Element condition : conditionList) {
			 List<Element> conditionChildrens = condition.getChildren(); 
			 for(Element child : conditionChildrens) { 
				  System.out.println("\n**********child name :" + child.getName() + " In condition " + condition.getAttributeValue("id")); 
				  
				  if(child.getName().equalsIgnoreCase("do")) {
			      List<Attribute> childAttributes = child.getAttributes();
			  
					  if(childAttributes.get(0).getName().equalsIgnoreCase("if")) {
						  String ruleCon = ("(").concat(childAttributes.get(0).getValue()).concat(")"); 
						  String[] target = {};
						  if(childAttributes.get(1).getName().equalsIgnoreCase("action") && childAttributes.get(1).getValue().equalsIgnoreCase("condition")) { 
							  Element nestedElement = conditionsMapElements.get(child.getValue());
							  iterateNestedCondition(nestedElement);
							  target = parseMapData(child.getValue()).replaceAll("[^a-zA-Z=0-9]+", "").split("="); 
						  }
						  
						  PipeLineRules rule = new PipeLineRules();
						  rule.setCondition(ruleCon);
						  PipeLineAction action = new PipeLineAction();
						  Configuration configuration = new Configuration();
						  configuration.setTarget(target[0]);
						  configuration.setValueExpression(target[1]);
						  target = parseElseTarget(conditionChildrens).replaceAll("[^a-zA-Z=0-9]+","").split("="); 
						  configuration.setElseTarget(target[0]);
						  configuration.setElseTargetValueExpression(target[1]);
						  action.setConfiguration(configuration);
						  rule.getActions().add(action);
						  rules.add(rule);
					  }
				  } 
			}
		}
		System.out.println("Generated Rules : ");
		for(PipeLineRules rule : rules) {
			System.out.println(rule.toString());
		}
	}
	
	private static void iterateNestedCondition(Element condition) {
		System.out.println("iterateCondition");
		System.out.println("In data" + condition.getAttributes());
		 for(Element childData : condition.getChildren()) { 
			  if(childData.getName().equalsIgnoreCase("do")) {
				  List<Attribute> childAttributes = childData.getAttributes();
				  
				  if(childAttributes.get(0).getName().equalsIgnoreCase("if")) {
					  String parseCondition = ("(").concat(childAttributes.get(0).getValue()).concat(")"); 
					  String target[] = {};
					  if(childAttributes.get(1).getName().equalsIgnoreCase("action") && childAttributes.get(1).getValue().equalsIgnoreCase("condition")){ 
						  target = parseMapData(childData.getValue()).split("/>"); 
						  System.out.println("Parse condition" + parseCondition);
						  System.out.println("Target" + target[0] + ", " + target[1]);
					  }
				  }
			  }
		  }
	}
	
	private static String parseElseTarget(List<Element> conditionChildrens) {
		System.out.println("parseElseTarget");
		String result = null; 
		for(Element child : conditionChildrens) {
			 if(child.getName().equalsIgnoreCase("design")) {
				 result = child.getName().concat(child.getAttributes().get(0).getName().concat("=").concat(child.getAttributes().get(0).getValue()));
			 }
		 }
		return result;
	}

	private static String parseMapData(String key) {
		return conditionsMapStringFormat.get(key);
	}

	private static String returnTagContent(Element condition) {
		StringBuffer sb = null;
		XMLOutputter outp = new XMLOutputter();
		StringWriter sw = new StringWriter();
		try {
			outp.setFormat(Format.getCompactFormat());
			outp.output(condition.getContent(), sw);
			sb = sw.getBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static void generateJSON() {
		
		JSONArray ruleArray = new JSONArray();
		for(PipeLineRules rule: rules){
			JSONObject ruleObject = new JSONObject();
			
			ruleObject.put("ruleId", rule.getRuleId());
			ruleObject.put("condition", rule.getCondition());
			ruleObject.put("name", rule.getName());
			ruleObject.put("description", rule.getDescription());
			ruleObject.put("isEnabled", rule.isEnabled());
			
			JSONArray actionsArray = new JSONArray();
			
			JSONObject actionObject = new JSONObject();
			
			for(PipeLineAction action : rule.getActions()) {
				actionObject.put("id", action.getId());
				actionObject.put("validationErrors", action.getValidationErrors());
				JSONObject configurationObject = new JSONObject();
				configurationObject.put("Target", action.getConfiguration().getTarget());
				configurationObject.put("ValueExpression", action.getConfiguration().getValueExpression());
				actionObject.put("configuration", configurationObject);
				actionObject.put("type", action.getType());
				actionsArray.add(actionObject);
			}
					    
			ruleObject.put("actions", actionsArray);
			ruleObject.put("notes", rule.getNotes());
			ruleObject.put("isValid", rule.isValid());
			ruleObject.put("validationErrors", rule.getValidationErrors());
			ruleObject.put("type", rule.getCondition());
			ruleArray.add(ruleObject);
		}
		
		
	    
	    //System.out.println(ruleArray.toJSONString());
	}

	public String getNoOfFilesProcessed() {
		return noOfFilesProcessed;
	}

	public void setNoOfFilesProcessed(String noOfFilesProcessed) {
		this.noOfFilesProcessed = noOfFilesProcessed;
	}

}