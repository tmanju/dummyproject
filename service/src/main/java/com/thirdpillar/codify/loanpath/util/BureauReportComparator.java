package com.thirdpillar.codify.loanpath.util;

import java.util.Comparator;
import java.util.Date;

import com.thirdpillar.codify.loanpath.model.ApprovalLevel;
import com.thirdpillar.codify.loanpath.model.BureauReport;

/**
 * Comparator to sort the BureauReports Based on Report Pull Time.
 */

public class BureauReportComparator implements Comparator<BureauReport> {

	public int compare(BureauReport report1, BureauReport report2) {
		long bureauReportPullTime1 = 0;
		long bureauReportPullTime2 = 0;

		int result = 0;

		if (report1 != null && report1.getPullEndDttm() != null) {
			bureauReportPullTime1 = report1.getPullEndDttm().getTime();
		}

		if (report2 != null && report2.getPullEndDttm() != null) {
			bureauReportPullTime2 = report2.getPullEndDttm().getTime();
		}

		if (bureauReportPullTime1 < bureauReportPullTime2) {
			result = -1;
		} else if (bureauReportPullTime1 > bureauReportPullTime2) {
			result = 1;

		} else if (bureauReportPullTime1 == bureauReportPullTime2) {
			result = 0;
		}

		return result;
	}
}
