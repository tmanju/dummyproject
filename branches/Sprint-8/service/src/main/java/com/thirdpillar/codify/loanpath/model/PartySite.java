package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

public class PartySite extends BaseDataObject {

	public ContactInfo getPartyContactInfo() {

		if ((getContactInfo() != null) && (getContactInfo().size() > 0)) {
			return getContactInfo().get(0);
		}

		return null;
	}

	public void setPartyContactInfo(ContactInfo contact) {
		addToContactInfo(contact);
	}

}
