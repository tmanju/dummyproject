package com.thirdpillar.codify.loanpath.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.foundation.model.BaseDataObject;

public class CustomerSite extends BaseDataObject {
	
	public Address getAddress(String use) {
		if (getSiteDetails().getAddresses() != null) {
			for (Address address  : getSiteDetails().getAddresses()) {
				if (address.getUse() != null) {
					List<AttributeChoice> useType = address.getUse();
					if (StringUtils.equals(use,useType.get(0).getKey())) {
						return address;
					}
				}
			}
		}
		// no matched use type
		return null;		
	}
	
	public Address getDefaultAddress() {
		// no specific logic as of yet, reeturning first in the list
		if (getSiteDetails() != null && getSiteDetails().getAddresses() != null && getSiteDetails().getAddresses().size() > 0) {
			return getSiteDetails().getAddresses().get(0);
		} else {
			return null;
		}
	}
	
    public String getCity() {
        return (getDefaultAddress() != null) ? getDefaultAddress().getCity() : null;
    }
    
    public String getState() {
    	
    	State stateProvince = (getDefaultAddress() != null) ? getDefaultAddress().getStateProvince() : null;
    	if (stateProvince != null) {
    		return stateProvince.getName();
    	} else {
    		return null;
    	}
    }
    
    public String getUsage() {
        List<AttributeChoice> usage = (getDefaultAddress() != null) ? getDefaultAddress().getUsage() : null;
    	if(usage != null && !usage.isEmpty()){
    		return usage.get(0).getValue();
    	}
    	return null;
    }
    
	
}
