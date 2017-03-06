/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;
import com.thirdpillar.foundation.service.LookupService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
@XStreamLookupCollectionByOGNL.List(
    { @XStreamLookupCollectionByOGNL(
            name = "byExternalRefId",
            keys = { "externalRefId" }
        ) }
)
public class RelationshipParty extends BaseDataObject {
	
	public static final String PARTY_ROLE_TYPE_ACCOUNT = "PARTY_ROLE_TYPE_ACCOUNT";

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 765091612762133922L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getDerivedOrr() {
    	// TODO
        return null;
    }
    
    public String getAccountName() {
    	if (this.getPartyRole() != null && StringUtils.equals(this.getPartyRole().getKey(), PARTY_ROLE_TYPE_ACCOUNT ) && this.getCustomer() != null) {
    		return this.getCustomer().getHelper().getDisplayValue();
    	} 
    	return null;
    		 
    }
    
    public String getParentRelPartyVal() {
    	
    	if (this.getParentRelationshipParty()!=null) {
    		return this.getParentRelationshipParty().getHelper().getDisplayValue();
    	} 
    	return null;
    		 
    }
    
    public boolean isPrimaryBorrower() {
        return hasBorrowerRole(Constants.PARTY_ROLE_TYPE_PRIMARY_BORROWER_KEY);
    }  
    
    public boolean isGuarantor() {
        return hasRelatedRole("PARTY_ROLE_TYPE_GUARANTOR");
    }
    
    /**
     * This method is used to check the role based on the passed parameter .
     *
     * @param   role
     *
     * @return  boolean as true if match found otherwise false.
     */
    public boolean hasRelatedRole(String role) {

        if (this.getRelatedRoles() != null) {

            for (AttributeChoice relatedRole : this.getRelatedRoles()) {

                if (StringUtils.equals(relatedRole.getKey(), role)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean hasBorrowerRole(String role) {
        return getBorrowerRole() != null && StringUtils.equals(getBorrowerRole().getKey(), role);
    }
    
    /**
     * This method validate that passed string value is matched with legal entity type or not.
     *
     * @param   legalEntityString  is of String type
     *
     * @return  boolean as true if match found otherwise false.
     */
    public boolean checkForLegalEntityType(String legalEntityString) {
        boolean flag = false;

        if ((legalEntityString != null) && (getCustomer() != null)) {
            BusinessDetail businessDetail = getCustomer().getBusinessDetail();
            AttributeChoice legalEntityTypeAttr =
                ((businessDetail != null) && (businessDetail.getLegalEntityType() != null))
                ? (AttributeChoice) businessDetail.getLegalEntityType() : null;

            if ((legalEntityTypeAttr != null) && legalEntityTypeAttr.getKey().contains(legalEntityString)) {
                flag = true;
            }
        }

        return flag;
    }
    
    /**
     * This method is nothing but is used to freeze the UI. This is used to join the roles with commas separated.
     *
     * @return  a string with commas seprated value.
     */
    public String getAssignedRoles() {
        List<String> roles = new ArrayList<String>();

        if (getBorrowerRole() != null) {
            roles.add(getBorrowerRole().getValue());
        } else if (this.getSelectedRole() != null) {
            roles.add(getSelectedRole().getValue());
        } else {

            if (this.getRelatedRoles() != null) {

                for (AttributeChoice relatedRole : this.getRelatedRoles()) {
                    roles.add(relatedRole.getValue());
                }
            }
        }

        return StringUtils.join(roles, ", ");
    }
    
    /**
     * This method is the union of the role from two different attributes.
     *
     * @return  list of AttributeChoice with Borrower from Borrower_Role + Related_Roles.
     */
    public List<AttributeChoice> getListAllRelavantRoles() {
        Attribute borrowerAtt = (Attribute) LookupService.getResult("Attribute.byKey", "key", "BORROWER_ROLE");
        List<AttributeChoice> borrowerChoices = new ArrayList<AttributeChoice>(borrowerAtt.getAttributeChoices());
        AttributeChoice borrowerRole = null;
        List<AttributeChoice> allRolesList = new ArrayList<AttributeChoice>();

        for (AttributeChoice allChoices : borrowerChoices) {

            if ((allChoices.getKey()).equals(Constants.PARTY_ROLE_TYPE_BORROWER_KEY)) {
                borrowerRole = allChoices;
            }
        }

        allRolesList.add(borrowerRole);

        Attribute relatedAtt = (Attribute) LookupService.getResult("Attribute.byKey", "key", "RELATED_ROLE");
        List<AttributeChoice> relatedChoices = new ArrayList<AttributeChoice>(relatedAtt.getAttributeChoices());

        for (AttributeChoice allChoices : relatedChoices) {

            if ((allChoices.getGroup()).equals(Constants.APPLICATION_TYPE_STANDARD_TICKET_KEY) ||
                    ("APPLICATION_TYPE_SMALL_TICKET,APPLICATION_TYPE_STANDARD").equals(allChoices.getGroup())) {
                allRolesList.add(allChoices);
            }
        }

        return allRolesList;
    }
    
    public boolean isBorrower() {
        return hasBorrowerRole(Constants.PARTY_ROLE_TYPE_BORROWER_KEY);
    }  
    
    /**
     * This method is nothing but is used to freeze the UI. This is used to join the roles with commas separated.
     *
     * @return  a string with commas seprated value.
     */
    public String getAssignedRolesKey() {
        List<String> roles = new ArrayList<String>();

        if (getBorrowerRole() != null) {
            roles.add(getBorrowerRole().getKey());
        } else if (this.getSelectedRole() != null) {
            roles.add(getSelectedRole().getKey());
        } else {

            if (this.getRelatedRoles() != null) {

                for (AttributeChoice relatedRole : this.getRelatedRoles()) {
                    roles.add(relatedRole.getKey());
                }
            }
        }

        return StringUtils.join(roles, ", ");
    }
    
    public boolean hasAssignedRole(String role) {
        return getAssignedRoles() != null && (StringUtils.equals(getAssignedRoles(), role) || getAssignedRoles().contains(role));
    }
    
}
