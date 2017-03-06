package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

public class ContactInfo extends BaseDataObject {

	public Contact getPartyContact() {

		if ((getContacts() != null) && (getContacts().size() > 0)) {
			return getContacts().get(0);
		}

		return null;
	}

	public void setPartyContact(Contact contact) {
		addToContacts(contact);
	}

}
