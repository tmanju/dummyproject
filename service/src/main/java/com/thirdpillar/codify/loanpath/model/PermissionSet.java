package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.foundation.model.IPermission;
import com.thirdpillar.foundation.model.IPermissionSet;

public class PermissionSet {
	
	
	public IPermission getPermission(String name, String type, String group) {
		List<Permission> permissions = getPermissions();
		// skip name for now
		for (Permission permission : permissions) {
			if (StringUtils.equals(permission.getType(), type)
					&& StringUtils.equals(permission.getName(), name)) {
				return permission;
			}
		}
		return null;
	}

	public List<? extends IPermission> getPermissions(String type, String group) {
		List<Permission> ps = getPermissions();
		List<IPermission> permissions = new ArrayList<IPermission>();
		for (Permission permission : ps) {
			if (StringUtils.equals(permission.getType(), type)) {
				permissions.add(permission);
			}
		}
		return permissions;
	}	
	
	public void addToPermissions(IPermission permissionSet) {
		  this.addToPermissions((Object) permissionSet);
	}
}
