package com.thirdpillar.codify.loanpath.util;

import java.util.Comparator;
import java.util.Date;

import com.thirdpillar.codify.loanpath.model.Compliance;

/**
 * Comparator to sort the Compliances Based on ComplianceCheckDttm.
 */

public class ComplianceComparator implements Comparator<Compliance> {

	public int compare(Compliance compliance1, Compliance compliance2) {
		long complianceTime1 = 0;
		long complianceTime2 = 0;

		int result = 0;

		if (compliance1 != null && compliance1.getComplianceCheckDttm() != null) {
			complianceTime1 = compliance1.getComplianceCheckDttm().getTime();
		}

		if (compliance2 != null && compliance2.getComplianceCheckDttm() != null) {
			complianceTime2 = compliance2.getComplianceCheckDttm().getTime();
		}

		if (complianceTime1 < complianceTime2) {
			result = -1;
		} else if (complianceTime1 > complianceTime2) {
			result = 1;

		} else if (complianceTime1 == complianceTime2) {
			result = 0;
		}

		return result;
	}
}
