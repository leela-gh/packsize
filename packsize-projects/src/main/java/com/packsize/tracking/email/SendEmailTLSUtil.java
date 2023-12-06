package com.packsize.tracking.email;

import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.file.UploadedFile;

import com.packsize.tracking.LineItem;
import com.packsize.tracking.TrackingPODetails;

public class SendEmailTLSUtil {
	
	private static final Logger logger = LogManager.getLogger();
	public static boolean sendEmail(TrackingPODetails poDetails, List<LineItem> dispList) {
		logger.info("In sendEmail()");
		 
		boolean success = true;
		 
        final String username = "test505xyz@gmail.com";
        final String password = "dxcoiglblhbonupk";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("test@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(poDetails.getEmail())
            );
            message.setSubject("Purchase Order Details");
                        
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            // Now set the actual message
            messageBodyPart.setText(generateBody(poDetails, dispList));
            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            
         // Part two is attachment
            if(poDetails.isSearchPanel()) {
            	generateAttachmentsForSearchPanel(poDetails,multipart);
            }else if(poDetails.isPoPanel()) {
            	generateAttachmentsForPoPanel(dispList,multipart);
            }
            
            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            logger.info("Done");

        } catch (MessagingException e) {
        	success = false;
        	logger.error(e.getMessage());
        }
        return success;
    }
	
	private static String generateBody(TrackingPODetails poDetails, List<LineItem> dispList) {
		StringBuffer result = new StringBuffer();
		result.append("*****PO# "+ poDetails.getId() + " details*****");
		if(poDetails.isSearchPanel()) {
			result.append("\n\n PO not found \n\n Comments : "+poDetails.getPoComments());
		}else {
			result.append("Line, Item, Plant, Stor.Loca, Material, Mat.Short Text, PO Quantity, Received, Qty in UnE, Label, OK, Discp., Reason, Reason Comments\n");
			for(LineItem item: dispList) {
				result.append(item.getLine()+", "+item.getItem()+", "+item.getPlant()+", "+item.getStoreLocation()+", "+item.getMaterial()+", "+item.getMatShortText()+", "+item.getPoQuantity()+", "+item.getReceived()+", "+item.getQtyInUne()+", "+item.getLabel()+", "+item.isOk()+", "+item.isDisc()+", "+item.getReasonSelection()+", "+item.getReasonComment()+"\n");
		    }
		}
		return result.toString();
	}
	
	private static void generateAttachmentsForSearchPanel(TrackingPODetails poDetails, Multipart multipart) {
		logger.info("In generateAttachmentsForSearchPanel()");
		if(poDetails.getFiles() != null) {
        		addMultiPartToEmail(poDetails.getFiles().getFiles(), multipart);
        }
	}
	
	private static void generateAttachmentsForPoPanel(List<LineItem> dispList, Multipart multipart) {
		logger.info("In generateAttachmentsForPoPanel()");
		for(LineItem item: dispList) {
        	if(item.getFiles() != null) {
        		addMultiPartToEmail(item.getFiles().getFiles(),multipart);
        	}
        }
	}
		
	private static void addMultiPartToEmail(List<UploadedFile> files, Multipart multipart) {
		logger.info("In addMultiPartToEmail()");
		try {
			for (UploadedFile file : files) {
				logger.info("File Name :" + file.getFileName());
	            if(file.getFileName() != null) {
	            	byte[] fileByteArray = file.getContent();
	                byte[] fileBase64ByteArray = java.util.Base64.getEncoder().encode(fileByteArray);
	                
	              //manually define headers
	                InternetHeaders fileHeaders = new InternetHeaders();
	                fileHeaders.setHeader("Content-Type", file.getContentType() + "; name=\"" + file.getFileName() + "\"");
	                fileHeaders.setHeader("Content-Transfer-Encoding", "base64");
	                fileHeaders.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
	
	                //build MIME body part
	                MimeBodyPart mbp = new MimeBodyPart(fileHeaders, fileBase64ByteArray);
	                mbp.setFileName(file.getFileName());
	
	                //add it to the multipart
	                multipart.addBodyPart(mbp);
	            }
			 }
		}catch(MessagingException e) {
			logger.error(e.getMessage());
		}
	}
	
	
}
