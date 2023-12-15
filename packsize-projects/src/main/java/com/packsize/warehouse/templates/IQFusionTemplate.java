package com.packsize.warehouse.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IQFusionTemplate {
	
	private static final Logger logger = LogManager.getLogger();
	private Map<Integer, Map<Integer, String>> iQCheckList;
	
	private Map<Integer, String> prepToRun;
	private String addItemPrepToRun;
	private List<IQFusionChecklistItem> itemListPrepToRun;
	private long totalHrsPrepToRun; 
	private boolean disableAddItemToPrepToRun;
	
	private Map<Integer, String> imagingThePanel;
	private String addItemImagingThePanel;
	private List<IQFusionChecklistItem> itemListImagingThePanel;
	private long totalHrsImagingThePanel; 
	private boolean disableAddItemToImagingThePanel;
	
	private Map<Integer, String> runningSoftwareInstaller;
	private String addItemrunningSoftwareInstaller;
	private List<IQFusionChecklistItem> itemListRunningSoftwareInstaller;
	private long totalHrsRunningSoftwareInstaller; 
	private boolean disableAddItemToRunningSoftwareInstaller;
	
	private Map<Integer, String> roboSetup;
	private String addItemRoboSetup;
	private List<IQFusionChecklistItem> itemListRoboSetup;
	private long totalHrsRoboSetup; 
	private boolean disableAddItemToRoboSetup;
	
	private Map<Integer, String> plcSettings;
	private String addItemPlcSettings;
	private List<IQFusionChecklistItem> itemListPlcSettings;
	private long totalHrsPlcSettings; 
	private boolean disableAddItemToPlcSettings;
	
	private Map<Integer, String> packnetSetup;
	private String addItemPacknetSetup;
	private List<IQFusionChecklistItem> itemListPacknetSetup;
	private long totalHrsPacknetSetup; 
	private boolean disableAddItemToPacknetSetup;
	
	private Map<Integer, String> calibration;
	private String addItemCalibration;
	private List<IQFusionChecklistItem> itemListCalibration;
	private long totalHrsCalibration; 
	private boolean disableAddItemToCalibration;
	
	private Map<Integer, String> fatTesting;
	private String addItemFatTesting;
	private List<IQFusionChecklistItem> itemListFatTesting;
	private long totalHrsFatTesting; 
	private boolean disableAddItemToFatTesting;
	
	public IQFusionTemplate(){
		setiQCheckList(new HashMap<Integer, Map<Integer,String>>());
		
		setPrepToRun(new HashMap<Integer, String>());
		setItemListPrepToRun(new ArrayList<IQFusionChecklistItem>());
		
		setImagingThePanel(new HashMap<Integer, String>());
		setItemListImagingThePanel(new ArrayList<IQFusionChecklistItem>());
		
		setRunningSoftwareInstaller(new HashMap<Integer, String>());
		setItemListRunningSoftwareInstaller(new ArrayList<IQFusionChecklistItem>());
		
		setRoboSetup(new HashMap<Integer, String>());
		setItemListRoboSetup(new ArrayList<IQFusionChecklistItem>());
		
		setPlcSettings(new HashMap<Integer, String>());
		setItemListPlcSettings(new ArrayList<IQFusionChecklistItem>());
		
		setPacknetSetup(new HashMap<Integer, String>());
		setItemListPacknetSetup(new ArrayList<IQFusionChecklistItem>());
		
		setCalibration(new HashMap<Integer, String>());
		setItemListCalibration(new ArrayList<IQFusionChecklistItem>());
		
		setFatTesting(new HashMap<Integer, String>());
		setItemListFatTesting(new ArrayList<IQFusionChecklistItem>());
				
		loadIQTemplateChecklist();
	}
	
	private void loadIQTemplateChecklist() {
		logger.info("In loadIQTemplateChecklist()");
		
		prepToRun.clear();
		prepToRun.put(1, "1.Mark machine as \"being preconfigured\" as the status under the asset details within Salesforce");
		prepToRun.put(2, "2.Check saleforce for button and arm side configuration");
		prepToRun.put(3, "3.Remove hood stabilization arm");
		prepToRun.put(4, "4.Install gear side body panel");
		prepToRun.put(5, "5.Route Control panel cables(2) through panel arm");
		prepToRun.put(6, "6.Mount panel arm");
		prepToRun.put(7, "7.Install elbow caps ");
		prepToRun.put(8, "8.Attach (4) cables to button panel");
		prepToRun.put(9, "9.Install (2) M6x16mm Button Head Screws, M6 Washer (on Locknut side), and M6 Nylon Locknut; into the (2) mounting holes for the Pretective Screen that is moounted behind the Button Panel. [ One of these Bolts goes into the hole above the Button Panel. The other Bolt is inserted into the upper, unused Rear Side Screen panel hole.] ");
		prepToRun.put(10, "10.Mount and make operator panel connections ");
		prepToRun.put(11, "11.Install Ethernet Cable for internet access");
		iQCheckList.put(1, prepToRun);
		loadIQTemplateChecklistObjects(itemListPrepToRun, 1);
		
		imagingThePanel.clear();
		imagingThePanel.put(1, "1.Insert USB with disk image");
		imagingThePanel.put(2, "2.Restart Panel and click \"Delete\" button to enter BIOS menu");
		imagingThePanel.put(3, "3.Scroll to Boot -> Hard Drive BBS priorities -> Change boot option 1 to USB -> Save & Exit");
		imagingThePanel.put(4, "4.If prompted to \"click any key to boot from USB\" click \"Enter\" ");
		imagingThePanel.put(5, "5.Once you enter Boot Setup, select 'Restore (Normal)' then select 'Next'");
		imagingThePanel.put(6, "6.In 'Select Where The Backup File Is Located', click on the folder with a blue circle under Windows Drives");
		imagingThePanel.put(7, "7.Select appropiate image for panel then click next.");
		imagingThePanel.put(8, "8.Select all drives to be restored by checking the box next to HD and click next");
		imagingThePanel.put(9, "9.Select HD 0 - GPT to be restored to.");
		imagingThePanel.put(10, "10.On 'Restore Options' screen, select 'Scale to Fit' and 'Log Results to File', then click next.");
		imagingThePanel.put(11, "11.On 'Summary' screen, click START");
		imagingThePanel.put(12, "12.Once the restoration has completed succesfully, close out the screen");
		imagingThePanel.put(13, "13.Panel will restart. Click \"Delete\" button to enter BIOS menu");
		imagingThePanel.put(14, "14.Scroll to Boot -> Hard Drive BBS priorities -> Change boot option 1 to native harddrive -> Save & Exit");
		imagingThePanel.put(15, "15.@ LANDesk Deployment screen, click on 'Restore (Normal)' and hit NEXT");
		imagingThePanel.put(16, "16.In 'Select Where The Backup File Is Located:', click on 'Drive D (New Folder)'");
		imagingThePanel.put(17, "17.Click on ' SPC-1881WP-BTO ', then click on ' SPC-1881WP-BTO ', hit NEXT");
		imagingThePanel.put(18, "18.On 'Select the Drive or Partition(s) to Restore' screen, click on ENTIRE HARD DRIVE, then hit NEXT");
		imagingThePanel.put(19, "19.On 'Select the Drive You Want to Restore to' screen, click on 'HD0', then hit NEXT");
		imagingThePanel.put(20, "20.A warning will pop up, that 'All Data in Following Partitions in HD0 will be Lost'. It should ONLY display Drives C: and E:! If there are more, you done messed up, A-A-Ron! Click on 'YES' if all is good");
		imagingThePanel.put(21, "21.On 'Restore Options' screen, select 'Scale to Fit' and 'Log Results to File', then hit NEXT");
		imagingThePanel.put(22, "22.On 'Summary' screen, hit START");
		imagingThePanel.put(23, "23.Once the restoration has completed succesfully, close out the screen");
		imagingThePanel.put(24, "24.Complete basic windows set up process.");
		imagingThePanel.put(25, "25.Skip Customer number page.");
		imagingThePanel.put(26, "26.How many other paneled machines does your location have? (enter in notes)");
		imagingThePanel.put(27, "27.If RTO name PC if RTS use packsize customer number TODO");
		imagingThePanel.put(28, "28.Select NABU and let PC finish updating");
		iQCheckList.put(2, imagingThePanel);
		loadIQTemplateChecklistObjects(itemListImagingThePanel, 2);
		
		runningSoftwareInstaller.clear();
		runningSoftwareInstaller.put(1, "1.Before running installers make sure you are logged into 'Support' account");
		runningSoftwareInstaller.put(2, "2.Open File Explorer, go to USB and Run \"Software Installer Launcher\" off of USB device (Make sure ethernet is connected to pull latest software)");
		runningSoftwareInstaller.put(3, "3.In Salesforce Account");
		runningSoftwareInstaller.put(4, "4.Select \"Semi-Auto\" then click \"Get Installers\"");
		runningSoftwareInstaller.put(5, "5.Select Packnet.Server 4.3.2012.1 or latest version");
		runningSoftwareInstaller.put(6, "6.Select Packnet.Report 1.2 or latest version");
		runningSoftwareInstaller.put(7, "7.Select Packnet.DIM only if customer is receiving DISTO or Zebra Scanner");
		runningSoftwareInstaller.put(8, "8.Do Not Select Packnet.Cube (SIE will install if necessary)");
		runningSoftwareInstaller.put(9, "9.Do Not Select SLD (SIE will install if necessary)");
		runningSoftwareInstaller.put(10, "10.Do Not Select Cubiscan (SIE will install if necessary)");
		runningSoftwareInstaller.put(11, "11.Select Beyond Trust 1.0.0.0 or latest version");
		runningSoftwareInstaller.put(12, "12.Select Backup Tool 1.0.27 or latest version");
		runningSoftwareInstaller.put(13, "13.Select Restore Tool 1.0.12 or latest version");
		runningSoftwareInstaller.put(14, "14.Select Drop File Manager 1.0.8.4 or latest version");
		runningSoftwareInstaller.put(15, "15.Select Config Backup Tool 1.1.0 or latest version");
		runningSoftwareInstaller.put(16, "16.Slect VNC Viewer latest version");
		runningSoftwareInstaller.put(17, "17.Select Pack Assist 1.0.0.9 or latest version");
		runningSoftwareInstaller.put(18, "18.Do Not Select CubeWebAPI (SIE will install if necessary)");
		runningSoftwareInstaller.put(19, "click \"Download and Run Installers\"");
		runningSoftwareInstaller.put(20, "20.Follow Packnet.Server prompts entering the correct customer number then click \"Run\"");
		runningSoftwareInstaller.put(21, "21.After Running the software installer, run Packsize.SophosPatch.22.4.13.2.msi");
		runningSoftwareInstaller.put(22, "22.To confirm successful installation, navigate to Settings > Apps & features and search for Packsize Sophos Patch 2022 22.4.13.2.");
		iQCheckList.put(3, runningSoftwareInstaller);
		loadIQTemplateChecklistObjects(itemListRunningSoftwareInstaller, 3);
		
		roboSetup.clear();
		roboSetup.put(1, "1.Open ROBO 3T 1.2.1 and check license agreement then\"Finish\"");
		roboSetup.put(2, "2.Choose \"Create\" then Name it \"ServerDB\"");
		roboSetup.put(3, "3.Go to Authentification tab and check \"Perform Authentification\" checkbox");
		roboSetup.put(4, "4.User Name - admin    Password - M4ch1n31Q");
		roboSetup.put(5, "5.Click \"Test\" and confirm connection, then Save");
		iQCheckList.put(4, roboSetup);
		loadIQTemplateChecklistObjects(itemListRoboSetup, 4);
		
		plcSettings.clear();
		plcSettings.put(1, "1.Search \"Control Panel\" in search bar and open control panel");
		plcSettings.put(2, "2.Filter \"Sort By\" to Category then go to \"Network and Internet\"");
		plcSettings.put(3, "3.Click \"change adaptor settings\" -> ethernet 1 or 2 (see note) -> properties -> highlight \"Internet Protocol Version 4\" -> Properties");
		plcSettings.put(4, "4.Select \"Use the following IP address\" ");
		plcSettings.put(5, "5.IP Address - 10.241.241.240   Subnet Mask - 255.255.255.0");
		plcSettings.put(6, "6.Click OK");
		iQCheckList.put(5, plcSettings);
		loadIQTemplateChecklistObjects(itemListPlcSettings, 5);
		
		packnetSetup.clear();
		packnetSetup.put(1, "1.Open Salesforce and search machine asset, under \" notes and attachments\" download config file to USB");
		packnetSetup.put(2, "2.Copy config file and go to Local Disc -> Program File -> Packsize Packnet.Server -> Data -> Physical Machine Settings -> Paste Config File");
		packnetSetup.put(3, "3.Local Disc -> Program File -> Packsize Packnet.Server -> Data -> Packaging Designs -> IQ -> Copy files -> click back arrow -> Paste Files");
		packnetSetup.put(4, "4.Local Disc -> Program File -> Packsize Packnet.Server -> Data -> Packaging Designs -> IQ -> Calibration Designs -> Copy Calibration 01 & 09 -> click back arrow TWICE -> Paste Files");
		packnetSetup.put(5, "5.Restart \"Packsize Packnet.Server\" through \"Local Services\"");
		packnetSetup.put(6, "6.Click \"Configuration\" tab ");
		packnetSetup.put(7, "7.Under \"Z-Folds\" tab add Test36 and other customer sizes if RTO");
		packnetSetup.put(8, "8.Under \"Machine\" tab and Add New Machine -> Name, Description - IQ Fusion 2 ST or MT or IQ Fusion 3, IP Address - 10.241.241.245, Number of Tracks (Depends on machine / Customer if RTO), Physical Machine Settings File - Config from Salesforce, Click Save");
		packnetSetup.put(9, "9.Under \"Machine Groups\" tab add new -> Name - MG1, Workflow Path - CreatePackageOnMachineGroup, Check Machine associated");
		packnetSetup.put(10, "10.Under \"Production Groups\" tab -> Name - PG1, Production Mode - Order, Check test36 box under z-folds, Check MG1, Click Save");
		iQCheckList.put(6, packnetSetup);
		loadIQTemplateChecklistObjects(itemListPacknetSetup, 6);
		
		calibration.clear();
		calibration.put(1, "1.Ensure all e-stops function and reset correctly   and the machione will initilize");
		calibration.put(2, "2.Use service aplication to test and ensure solenoids for the Crosshead crease rings are functioning propperly and have no air leaks");
		calibration.put(3, "3.Use service aplication to test and ensure solenoids for the Crosshead knife is functioning properly and have no air leaks");
		calibration.put(4, "4.Use service aplication to test and ensure solenoids for Crosshead to longhead pin is functioning properly and have no air leaks");
		calibration.put(5, "5.Use service aplication to test and ensure solenoids for all track brakes are functioning properly and have no air leaks");
		calibration.put(6, "6.Use service aplication to test and ensure solenoids for all track pressure rollers are functioning properly and have no air leaks");
		calibration.put(7, "7.While the Fusion is at idle, and before inserting corrugate for testing, set both track Brakes (while in the UP position) using the Fusion Brake Height Gauge.");
		calibration.put(8, "8.Load each track with corrugate and make test boxes, 1 each of two sizes for each track (4 boxes total). Label each box with Machine Number, Date, Track number, box size, and \"Test #1\"");
		iQCheckList.put(7, calibration);
		loadIQTemplateChecklistObjects(itemListCalibration, 7);
	
		//TODO add FAT testing items
	}
	
	private void loadIQTemplateChecklistObjects(List<IQFusionChecklistItem> checkList, Integer index) {
		logger.info("In loadIQTemplateChecklistObjects() for "+ index);
		checkList.clear();
		for (Map.Entry<Integer, String> entry : iQCheckList.get(index).entrySet()) {
			IQFusionChecklistItem item = new IQFusionChecklistItem();
			item.setParentId(index);
			item.setId(entry.getKey());
			item.setName(entry.getValue());
			if(entry.getKey() == 1) {
				item.setEnable(true);
			}else {
				item.setEnable(false);
			}
			checkList.add(item);
		}
	}
	
	public Map<Integer, String> getPrepToRun() {
		return prepToRun;
	}

	public void setPrepToRun(Map<Integer, String> prepToRun) {
		this.prepToRun = prepToRun;
	}

	public String getAddItemPrepToRun() {
		return addItemPrepToRun;
	}

	public void setAddItemPrepToRun(String addItemPrepToRun) {
		this.addItemPrepToRun = addItemPrepToRun;
	}
	
	public Map<Integer, Map<Integer, String>> getiQCheckList() {
		return iQCheckList;
	}

	public void setiQCheckList(Map<Integer, Map<Integer, String>> iQCheckList) {
		this.iQCheckList = iQCheckList;
	}

	public Map<Integer, String> getImagingThePanel() {
		return imagingThePanel;
	}

	public void setImagingThePanel(Map<Integer, String> imagingThePanel) {
		this.imagingThePanel = imagingThePanel;
	}

	public String getAddItemImagingThePanel() {
		return addItemImagingThePanel;
	}

	public void setAddItemImagingThePanel(String addItemImagingThePanel) {
		this.addItemImagingThePanel = addItemImagingThePanel;
	}

	public List<IQFusionChecklistItem> getItemListImagingThePanel() {
		return itemListImagingThePanel;
	}

	public void setItemListImagingThePanel(List<IQFusionChecklistItem> itemListImagingThePanel) {
		this.itemListImagingThePanel = itemListImagingThePanel;
	}

	public List<IQFusionChecklistItem> getItemListPrepToRun() {
		return itemListPrepToRun;
	}

	public void setItemListPrepToRun(List<IQFusionChecklistItem> itemListPrepToRun) {
		this.itemListPrepToRun = itemListPrepToRun;
	}
	
	public long getTotalHrsPrepToRun() {
		return totalHrsPrepToRun;
	}

	public void setTotalHrsPrepToRun(long totalHrsPrepToRun) {
		this.totalHrsPrepToRun = totalHrsPrepToRun;
	}

	public boolean isDisableAddItemToPrepToRun() {
		return disableAddItemToPrepToRun;
	}

	public void setDisableAddItemToPrepToRun(boolean disableAddItemToPrepToRun) {
		this.disableAddItemToPrepToRun = disableAddItemToPrepToRun;
	}
	
	public long getTotalHrsImagingThePanel() {
		return totalHrsImagingThePanel;
	}

	public void setTotalHrsImagingThePanel(long totalHrsImagingThePanel) {
		this.totalHrsImagingThePanel = totalHrsImagingThePanel;
	}

	public boolean isDisableAddItemToImagingThePanel() {
		return disableAddItemToImagingThePanel;
	}

	public void setDisableAddItemToImagingThePanel(boolean disableAddItemToImagingThePanel) {
		this.disableAddItemToImagingThePanel = disableAddItemToImagingThePanel;
	}
	
	public Map<Integer, String> getRunningSoftwareInstaller() {
		return runningSoftwareInstaller;
	}

	public void setRunningSoftwareInstaller(Map<Integer, String> runningSoftwareInstaller) {
		this.runningSoftwareInstaller = runningSoftwareInstaller;
	}

	public String getAddItemrunningSoftwareInstaller() {
		return addItemrunningSoftwareInstaller;
	}

	public void setAddItemrunningSoftwareInstaller(String addItemrunningSoftwareInstaller) {
		this.addItemrunningSoftwareInstaller = addItemrunningSoftwareInstaller;
	}

	public List<IQFusionChecklistItem> getItemListRunningSoftwareInstaller() {
		return itemListRunningSoftwareInstaller;
	}

	public void setItemListRunningSoftwareInstaller(List<IQFusionChecklistItem> itemListRunningSoftwareInstaller) {
		this.itemListRunningSoftwareInstaller = itemListRunningSoftwareInstaller;
	}

	public long getTotalHrsRunningSoftwareInstaller() {
		return totalHrsRunningSoftwareInstaller;
	}

	public void setTotalHrsRunningSoftwareInstaller(long totalHrsRunningSoftwareInstaller) {
		this.totalHrsRunningSoftwareInstaller = totalHrsRunningSoftwareInstaller;
	}

	public boolean isDisableAddItemToRunningSoftwareInstaller() {
		return disableAddItemToRunningSoftwareInstaller;
	}

	public void setDisableAddItemToRunningSoftwareInstaller(boolean disableAddItemToRunningSoftwareInstaller) {
		this.disableAddItemToRunningSoftwareInstaller = disableAddItemToRunningSoftwareInstaller;
	}

	public Map<Integer, String> getRoboSetup() {
		return roboSetup;
	}

	public void setRoboSetup(Map<Integer, String> roboSetup) {
		this.roboSetup = roboSetup;
	}

	public String getAddItemRoboSetup() {
		return addItemRoboSetup;
	}

	public void setAddItemRoboSetup(String addItemRoboSetup) {
		this.addItemRoboSetup = addItemRoboSetup;
	}

	public List<IQFusionChecklistItem> getItemListRoboSetup() {
		return itemListRoboSetup;
	}

	public void setItemListRoboSetup(List<IQFusionChecklistItem> itemListRoboSetup) {
		this.itemListRoboSetup = itemListRoboSetup;
	}

	public long getTotalHrsRoboSetup() {
		return totalHrsRoboSetup;
	}

	public void setTotalHrsRoboSetup(long totalHrsRoboSetup) {
		this.totalHrsRoboSetup = totalHrsRoboSetup;
	}

	public boolean isDisableAddItemToRoboSetup() {
		return disableAddItemToRoboSetup;
	}

	public void setDisableAddItemToRoboSetup(boolean disableAddItemToRoboSetup) {
		this.disableAddItemToRoboSetup = disableAddItemToRoboSetup;
	}

	public Map<Integer, String> getPlcSettings() {
		return plcSettings;
	}

	public void setPlcSettings(Map<Integer, String> plcSettings) {
		this.plcSettings = plcSettings;
	}

	public String getAddItemPlcSettings() {
		return addItemPlcSettings;
	}

	public void setAddItemPlcSettings(String addItemPlcSettings) {
		this.addItemPlcSettings = addItemPlcSettings;
	}

	public List<IQFusionChecklistItem> getItemListPlcSettings() {
		return itemListPlcSettings;
	}

	public void setItemListPlcSettings(List<IQFusionChecklistItem> itemListPlcSettings) {
		this.itemListPlcSettings = itemListPlcSettings;
	}

	public long getTotalHrsPlcSettings() {
		return totalHrsPlcSettings;
	}

	public void setTotalHrsPlcSettings(long totalHrsPlcSettings) {
		this.totalHrsPlcSettings = totalHrsPlcSettings;
	}

	public boolean isDisableAddItemToPlcSettings() {
		return disableAddItemToPlcSettings;
	}

	public void setDisableAddItemToPlcSettings(boolean disableAddItemToPlcSettings) {
		this.disableAddItemToPlcSettings = disableAddItemToPlcSettings;
	}

	public Map<Integer, String> getPacknetSetup() {
		return packnetSetup;
	}

	public void setPacknetSetup(Map<Integer, String> packnetSetup) {
		this.packnetSetup = packnetSetup;
	}

	public String getAddItemPacknetSetup() {
		return addItemPacknetSetup;
	}

	public void setAddItemPacknetSetup(String addItemPacknetSetup) {
		this.addItemPacknetSetup = addItemPacknetSetup;
	}

	public List<IQFusionChecklistItem> getItemListPacknetSetup() {
		return itemListPacknetSetup;
	}

	public void setItemListPacknetSetup(List<IQFusionChecklistItem> itemListPacknetSetup) {
		this.itemListPacknetSetup = itemListPacknetSetup;
	}

	public long getTotalHrsPacknetSetup() {
		return totalHrsPacknetSetup;
	}

	public void setTotalHrsPacknetSetup(long totalHrsPacknetSetup) {
		this.totalHrsPacknetSetup = totalHrsPacknetSetup;
	}

	public boolean isDisableAddItemToPacknetSetup() {
		return disableAddItemToPacknetSetup;
	}

	public void setDisableAddItemToPacknetSetup(boolean disableAddItemToPacknetSetup) {
		this.disableAddItemToPacknetSetup = disableAddItemToPacknetSetup;
	}

	public Map<Integer, String> getCalibration() {
		return calibration;
	}

	public void setCalibration(Map<Integer, String> calibration) {
		this.calibration = calibration;
	}

	public String getAddItemCalibration() {
		return addItemCalibration;
	}

	public void setAddItemCalibration(String addItemCalibration) {
		this.addItemCalibration = addItemCalibration;
	}

	public List<IQFusionChecklistItem> getItemListCalibration() {
		return itemListCalibration;
	}

	public void setItemListCalibration(List<IQFusionChecklistItem> itemListCalibration) {
		this.itemListCalibration = itemListCalibration;
	}

	public long getTotalHrsCalibration() {
		return totalHrsCalibration;
	}

	public void setTotalHrsCalibration(long totalHrsCalibration) {
		this.totalHrsCalibration = totalHrsCalibration;
	}

	public boolean isDisableAddItemToCalibration() {
		return disableAddItemToCalibration;
	}

	public void setDisableAddItemToCalibration(boolean disableAddItemToCalibration) {
		this.disableAddItemToCalibration = disableAddItemToCalibration;
	}

	public Map<Integer, String> getFatTesting() {
		return fatTesting;
	}

	public void setFatTesting(Map<Integer, String> fatTesting) {
		this.fatTesting = fatTesting;
	}

	public String getAddItemFatTesting() {
		return addItemFatTesting;
	}

	public void setAddItemFatTesting(String addItemFatTesting) {
		this.addItemFatTesting = addItemFatTesting;
	}

	public List<IQFusionChecklistItem> getItemListFatTesting() {
		return itemListFatTesting;
	}

	public void setItemListFatTesting(List<IQFusionChecklistItem> itemListFatTesting) {
		this.itemListFatTesting = itemListFatTesting;
	}

	public long getTotalHrsFatTesting() {
		return totalHrsFatTesting;
	}

	public void setTotalHrsFatTesting(long totalHrsFatTesting) {
		this.totalHrsFatTesting = totalHrsFatTesting;
	}

	public boolean isDisableAddItemToFatTesting() {
		return disableAddItemToFatTesting;
	}

	public void setDisableAddItemToFatTesting(boolean disableAddItemToFatTesting) {
		this.disableAddItemToFatTesting = disableAddItemToFatTesting;
	}
}
