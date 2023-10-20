package com.packsize.tracking.photo;

import java.io.Serializable;

import org.primefaces.event.CaptureEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session")
public class PhotoCamView implements Serializable {

    private String filename;

    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    public String getFilename() {
        return filename;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();
        
        System.out.println("Data is :" + data);

		/*
		 * ExternalContext externalContext =
		 * FacesContext.getCurrentInstance().getExternalContext(); String newFileName =
		 * externalContext.getRealPath("") + File.separator + "resources" +
		 * File.separator + "demo" + File.separator + "images" + File.separator +
		 * "photocam" + File.separator + filename + ".jpeg";
		 * 
		 * FileImageOutputStream imageOutput; try { imageOutput = new
		 * FileImageOutputStream(new File(newFileName)); imageOutput.write(data, 0,
		 * data.length); imageOutput.close(); } catch (IOException e) { throw new
		 * FacesException("Error in writing captured image.", e); }
		 */
    }
}