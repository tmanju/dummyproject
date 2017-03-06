package com.thirdpillar.codify.loanpath.service;

import com.thirdpillar.codify.loanpath.model.PermissionSet;
import com.thirdpillar.foundation.model.IPermissionSet;
import com.thirdpillar.foundation.permissions.PermissionSetFinder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

public class PermissionSetFinderService implements PermissionSetFinder {

	EntityService<PermissionSet> entityService = new EntityService<PermissionSet>();
	
	public IPermissionSet findPermissionSetByName(String name) {
		return (IPermissionSet) LookupService.getResult("PermissionSet.byName", "name", name);
	}

	public Integer getVersion(IPermissionSet permissionSet) {
		return ((PermissionSet) permissionSet).getVersion();
	}
	
	public IPermissionSet findPermissionSetById(Long id) {
		return (IPermissionSet) entityService.get(PermissionSet.class, id);
	}	

}
