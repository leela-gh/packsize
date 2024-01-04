package com.packsize.warehouse.templates;

import java.util.HashMap;
import java.util.Map;

public class DefaultHoursTemplate {
	public static Map<Integer, Integer> iQParentListDefaultHrs;
		
	static {
		iQParentListDefaultHrs = new HashMap<Integer, Integer>();
				
		iQParentListDefaultHrs.put(1, 5);
		iQParentListDefaultHrs.put(2, 10);
		iQParentListDefaultHrs.put(3, 15);
		iQParentListDefaultHrs.put(4, 20);
		iQParentListDefaultHrs.put(5, 25);
		iQParentListDefaultHrs.put(6, 30);
		iQParentListDefaultHrs.put(7, 35);
	}
}
