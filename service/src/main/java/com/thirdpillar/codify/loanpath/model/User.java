/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import com.thirdpillar.foundation.codify.constants.CodifyConstants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.util.Configuration;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class User extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -6604576372288529264L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
     *
     * @return  <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    public boolean isEnabled() {
    	return "USER_STATUS_ACTIVE".equalsIgnoreCase(getWfStatus().getStatusKey());
    }

    
    /**
     * Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
     * 
     * Set User.lastPasswordResetDate=null to force password change on next login 
     * When Config[security.authentication.expirePasswordInDays] > 0, passwords expire periodically. 
     *     Credentials will have expired when User.lastPasswordResetDate + expirePasswordInDays > today
     *
     * @return  <code>true</code> if the user's credentials are valid (ie non-expired), <code>false</code> if no longer
     *          valid (ie expired)
     */
    public boolean isCredentialsNonExpired() {
    	int expirePasswordInDays = CodifyConstants.CONFIG.getInt("security.authentication.expirePasswordInDays", -1);
    	
    	if(getLastPasswordResetDate() == null) {
    		// force password reset on next login.
    		return false;
    	}
    		
    	if(expirePasswordInDays > 0) {
    		// Password expiry enabled
        	return Days.daysBetween( new DateMidnight(), new DateMidnight(getLastPasswordResetDate())).getDays() > expirePasswordInDays*-1;    		
    	} else {
    		// Never expires 
    		return true;
    	}
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be authenticated.
     * 
     * When User.accountNonExpired == null, account never expires
     *  
     * @return  <code>true</code> if the user's account is valid (ie non-expired), <code>false</code> if no longer valid
     *          (ie expired)
     */
    public boolean isAccountNonExpired() {
   	 	return getAccountExpiryDate() == null || new Date().before(getAccountExpiryDate());
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
     *
     * @return  <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    public boolean isAccountNonLocked() {
    	return !getLocked();
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return  the authorities, sorted by natural key (never <code>null</code>)
     */
    public Collection<GrantedAuthority> getAuthorities() {
        List<Role> roles = this.getRoles();
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        for (Iterator j = roles.iterator(); j.hasNext();) {
            Role role = (Role) j.next();
            if (role.getPermissionSet() != null) {
            	auths.add(new SimpleGrantedAuthority(role.getPermissionSet().getName()));
            }
        }
        
        return auths;
    }

    public boolean hasRole(Role r) {

        for (Role role : getRoles()) {

            if ((role.getStatus() == null) || !StringUtils.equals(role.getStatus().getKey(), "ROLE_STATUS_INACTIVE")) {

                if (role.getId() == r.getId()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasRole(String roleName) {

        for (Role role : getRoles()) {

            if ((role.getStatus() == null) || !StringUtils.equals(role.getStatus().getKey(), "ROLE_STATUS_INACTIVE")) {

                if (StringUtils.equals(role.getName(), roleName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasRoleCategory(String roleCategoryKey) {

        for (Role role : getRoles()) {

            if ((role.getStatus() == null) || !StringUtils.equals(role.getStatus().getKey(), "ROLE_STATUS_INACTIVE")) {

                if (StringUtils.equals(role.getRoleCategory().getKey(), roleCategoryKey)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean hasAnyRoleCategory(String... roles) {
    	
		for (String role : roles) {
			if (hasRoleCategory(role)) {
				return true;
			}
		}
		return false;
    	
    }
    
    public boolean hasRoleCategoryOtherThan(String... roles) {
    	
    	boolean hasRoleCategory = true;

    	 for (Role role : getRoles()) {

    		 // if atleast one role, then, taht rolw shouldn't be the other than the passed in list.
    		 hasRoleCategory = false;
    		 
             if ((role.getStatus() == null) || !StringUtils.equals(role.getStatus().getKey(), "ROLE_STATUS_INACTIVE")) {
     			if(!ArrayUtils.contains(roles, role.getRoleCategory().getKey())) {
    				return true;
    			}
             }
         }
		return hasRoleCategory;
    }
    
    public String getLowerUsername() {
    	if (getUsername() != null) {
    		return getUsername().toLowerCase();
    	} else {
    		return null;
    	}
    }
}
