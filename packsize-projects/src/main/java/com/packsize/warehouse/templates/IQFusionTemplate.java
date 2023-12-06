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
	
	public IQFusionTemplate(){
		setiQCheckList(new HashMap<Integer, Map<Integer,String>>());
		setPrepToRun(new HashMap<Integer, String>());
		setItemListPrepToRun(new ArrayList<IQFusionChecklistItem>());
		setImagingThePanel(new HashMap<Integer, String>());
		setItemListImagingThePanel(new ArrayList<IQFusionChecklistItem>());
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
}
