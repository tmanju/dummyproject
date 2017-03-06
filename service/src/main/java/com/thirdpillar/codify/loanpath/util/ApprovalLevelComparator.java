package com.thirdpillar.codify.loanpath.util;

import java.util.Comparator;

import com.thirdpillar.codify.loanpath.model.ApprovalLevel;

/**
 * Comparator to sort the ApprovalLevels Alphabetically.  
 */

public class ApprovalLevelComparator implements Comparator<ApprovalLevel> {

	public int compare(ApprovalLevel appLevel1, ApprovalLevel appLevel2) {
		String level1 = "";
		String level2 = "";
		int result = 0;
 
		if (appLevel1 != null && appLevel1.getUppercaseName() != null && !("".equals(appLevel1.getUppercaseName()))) {
			level1 = appLevel1.getName();
		}
 
		if (appLevel2 != null && appLevel2.getUppercaseName() != null && !("".equals(appLevel2.getUppercaseName()))) {
			level2 = appLevel2.getName();
		}

		result=level1.compareTo(level2);
		
		return result;
	}
}
