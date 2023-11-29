package com.packsize.warehouse.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.packsize.PackSizeLogger;

public class IQFusionTemplate {
	
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
		setItemListPrepToRun(new ArrayList<IQFusionChecklistItem>());
		setItemListImagingThePanel(new ArrayList<IQFusionChecklistItem>());
		loadIQTemplateChecklist();
	}
	
	private void loadIQTemplateChecklist() {
		PackSizeLogger.info("In loadIQTemplateChecklist()");
		
		Map<Integer, String> prepToRunLocal = new HashMap<Integer, String>();
		prepToRunLocal.put(1, "1.Mark machine as \"being preconfigured\" as the status under the asset details within Salesforce");
		prepToRunLocal.put(2, "2.Check saleforce for button and arm side configuration");
		prepToRunLocal.put(3, "3.Remove hood stabilization arm");
		prepToRunLocal.put(4, "4.Install gear side body panel");
		prepToRunLocal.put(5, "5.Route Control panel cables(2) through panel arm");
		prepToRunLocal.put(6, "6.Mount panel arm");
		prepToRunLocal.put(7, "7.Install elbow caps ");
		prepToRunLocal.put(8, "8.Attach (4) cables to button panel");
		prepToRunLocal.put(9, "9.Install (2) M6x16mm Button Head Screws, M6 Washer (on Locknut side), and M6 Nylon Locknut; into the (2) mounting holes for the Pretective Screen that is moounted behind the Button Panel. [ One of these Bolts goes into the hole above the Button Panel. The other Bolt is inserted into the upper, unused Rear Side Screen panel hole.] ");
		prepToRunLocal.put(10, "10.Mount and make operator panel connections ");
		prepToRunLocal.put(11, "11.Install Ethernet Cable for internet access");
		
		iQCheckList.put(1, prepToRunLocal);
		setPrepToRun(iQCheckList.get(1));
		loadIQTemplateChecklistObjects(itemListPrepToRun, 1);
		
		Map<Integer, String> imagingThePanelLocal = new HashMap<Integer, String>();
		imagingThePanelLocal.put(1, "1.Insert USB with disk image");
		imagingThePanelLocal.put(2, "2.Restart Panel and click \"Delete\" button to enter BIOS menu");
		imagingThePanelLocal.put(3, "3.Scroll to Boot -> Hard Drive BBS priorities -> Change boot option 1 to USB -> Save & Exit");
		imagingThePanelLocal.put(4, "4.If prompted to \"click any key to boot from USB\" click \"Enter\" ");
		imagingThePanelLocal.put(5, "5.Once you enter Boot Setup, select 'Restore (Normal)' then select 'Next'");
		imagingThePanelLocal.put(6, "6.In 'Select Where The Backup File Is Located', click on the folder with a blue circle under Windows Drives");
		imagingThePanelLocal.put(7, "7.Select appropiate image for panel then click next.");
		imagingThePanelLocal.put(8, "8.Select all drives to be restored by checking the box next to HD and click next");
		imagingThePanelLocal.put(9, "9.Select HD 0 - GPT to be restored to.");
		imagingThePanelLocal.put(10, "10.On 'Restore Options' screen, select 'Scale to Fit' and 'Log Results to File', then click next.");
		imagingThePanelLocal.put(11, "11.On 'Summary' screen, click START");
		imagingThePanelLocal.put(12, "12.Once the restoration has completed succesfully, close out the screen");
		imagingThePanelLocal.put(13, "13.Panel will restart. Click \"Delete\" button to enter BIOS menu");
		imagingThePanelLocal.put(14, "14.Scroll to Boot -> Hard Drive BBS priorities -> Change boot option 1 to native harddrive -> Save & Exit");
		imagingThePanelLocal.put(15, "15.@ LANDesk Deployment screen, click on 'Restore (Normal)' and hit NEXT");
		imagingThePanelLocal.put(16, "16.In 'Select Where The Backup File Is Located:', click on 'Drive D (New Folder)'");
		imagingThePanelLocal.put(17, "17.Click on ' SPC-1881WP-BTO ', then click on ' SPC-1881WP-BTO ', hit NEXT");
		imagingThePanelLocal.put(18, "18.On 'Select the Drive or Partition(s) to Restore' screen, click on ENTIRE HARD DRIVE, then hit NEXT");
		imagingThePanelLocal.put(19, "19.On 'Select the Drive You Want to Restore to' screen, click on 'HD0', then hit NEXT");
		imagingThePanelLocal.put(20, "20.A warning will pop up, that 'All Data in Following Partitions in HD0 will be Lost'. It should ONLY display Drives C: and E:! If there are more, you done messed up, A-A-Ron! Click on 'YES' if all is good");
		imagingThePanelLocal.put(21, "21.On 'Restore Options' screen, select 'Scale to Fit' and 'Log Results to File', then hit NEXT");
		imagingThePanelLocal.put(22, "22.On 'Summary' screen, hit START");
		imagingThePanelLocal.put(23, "23.Once the restoration has completed succesfully, close out the screen");
		imagingThePanelLocal.put(24, "24.Complete basic windows set up process.");
		imagingThePanelLocal.put(25, "25.Skip Customer number page.");
		imagingThePanelLocal.put(26, "26.How many other paneled machines does your location have? (enter in notes)");
		imagingThePanelLocal.put(27, "27.If RTO name PC if RTS use packsize customer number TODO");
		imagingThePanelLocal.put(28, "28.Select NABU and let PC finish updating");
		
		iQCheckList.put(2, imagingThePanelLocal);
		setImagingThePanel(iQCheckList.get(2));
		loadIQTemplateChecklistObjects(itemListImagingThePanel, 2);
	}
	
	private void loadIQTemplateChecklistObjects(List<IQFusionChecklistItem> checkList, Integer index) {
		
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
