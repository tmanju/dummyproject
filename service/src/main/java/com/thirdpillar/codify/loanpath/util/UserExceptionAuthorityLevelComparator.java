package com.thirdpillar.codify.loanpath.util;

import java.util.Comparator;

import com.thirdpillar.codify.loanpath.model.Approver;

/**
 * Sorts the Approvers based on their RacExceptionAuthority in Ascending order.
 * 
 */
public class UserExceptionAuthorityLevelComparator implements
		Comparator<Approver> {

	public int compare(Approver approver1, Approver approver2) {
		int returnValue = 0;
		int approver1Authority = 0;
		int approver2Authority = 0;

		if (approver1 != null && approver1.getUser() != null
				&& approver1.getUser().getRacExceptionAuthority() != null) {
			if (approver1.getUser().getRacExceptionAuthority().getValue() != null
					&& !("".equals(approver1.getUser().getRacExceptionAuthority()
							.getValue()))) {
				approver1Authority = Integer.parseInt(approver1.getUser()
						.getRacExceptionAuthority().getValue().split(" ")[1]);
			}
		}

		if (approver2 != null && approver2.getUser() != null
				&& approver2.getUser().getRacExceptionAuthority() != null) {
			if (approver2.getUser().getRacExceptionAuthority().getValue() != null
					&& !("".equals(approver2.getUser().getRacExceptionAuthority()
							.getValue()))) {
				approver2Authority = Integer.parseInt(approver2.getUser()
						.getRacExceptionAuthority().getValue().split(" ")[1]);
			}
		}

		if (approver1Authority < approver2Authority) {
			returnValue = -1;
		} else if (approver1Authority > approver2Authority) {
			returnValue = 1;
		} else if (approver1Authority == approver2Authority) {
			returnValue = 0;
		}

		return returnValue;
	}

}
