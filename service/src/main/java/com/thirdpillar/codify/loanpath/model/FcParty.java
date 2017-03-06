package com.thirdpillar.codify.loanpath.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.thirdpillar.codify.loanpath.service.interceptors.OrganizationalEntitySyncInterceptor;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

public class FcParty extends BaseDataObject{
    
    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 6908917000289874265L;
    private static final Logger logger = Logger.getLogger(FcParty.class);

	public boolean isIndividual() {
		return isPartyType("CUSTOMER_TYPE_INDIVIDUAL");
	}

	public boolean isPartyType(String key) {

		if ((getPartyType() != null)
				&& key.equalsIgnoreCase(getPartyType().getKey())) {
			return true;
		}
			return false;
	}

	public FcAddress getDisplayAddress() {
		List<FcAddress> addresses = getAddresses();

		
		try {

			String addressTypeKey = null;

			if (isIndividual()) {
				addressTypeKey = "ADDRESS_TYPE_HOME";
			} else {
				addressTypeKey = "ADDRESS_TYPE_BUSINESS";
			}
				for (FcAddress address : addresses) {
						if ((address.getAddressType() != null)
								&& (StringUtils.equals(addressTypeKey, address
										.getAddressType().getKey()))) {
							return address;
						}
				}
			}catch (NullPointerException ignored) {
				logger.error("Null Pointer Exception",ignored);
			}

		return null;
	}
	
	public boolean validBirthDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  
        Date birthDate = getBirthDate();
        Date today = new Date();
        
        if(birthDate.after(today)){
        	return false;
        }
        if(sdf.format(birthDate).equals(sdf.format(today))){
        	return false;
        }
           return true;
    }

}
