/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class BusinessDetail extends BaseDataObject{

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -2918498306453458789L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public boolean isEntitytypeOther() {
        AttributeChoice legalentityType = getLegalEntityType();

        if (legalentityType != null) {

            if (StringUtils.equals("LEGAL_ENTITY_TYPE_OTHER", legalentityType.getKey())) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method validate current management start date against current date.
     *
     * @return  boolean as true if received date is future date otherwise false.
     */
    public boolean isFutureDate() {
        boolean flag = false;

        if ((getCurrentManagementStartDttm() != null) && getCurrentManagementStartDttm().after(new Date())) {
            flag = true;
        }

        return flag;
    }

    public boolean isSoleProprietor() {
        AttributeChoice legalentityType = getLegalEntityType();

        if (legalentityType != null) {

            if (StringUtils.equals("LEGAL_ENTITY_TYPE_SOLE_PROPRIETOR_KEY", legalentityType.getKey())) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Method to check if Business has a NAICS code that is categorized with the given industry Category 
     * i.e. if the NAICS Code has a specific category.
     * 
     * @param String industryCategory to check
     * 
     * @return boolean a true or false
     */
    public boolean hasIndustryCategory(String industryCategory){
    	return false;
    }
    
    /**
     * Method to check if Business has a NAICS code that is categorized with the given industry Group 
     * i.e. if the NAICS Code has a specific group.
     * 
     * @param String industryCategory to check
     * 
     * @return boolean a true or false
     */
    public boolean hasIndustryGroup(String industryGroup){
    	return false;
    }
    /**
     * This method is used to validate all check boxes available on Business details page for Non- Individual party.
     * 
     * @return boolean as true if all check boxes validated to true otherwise false.
     */
    public boolean validateAllCheckBoxesOnPartyScreen(){
    	boolean flag = false;
    	if(this.getFranchiseFlag() || this.getBranchTradeFlag() || this.getRealEstatePropFlag() || this.getOrAnyOwnGuarProtection() ||this.getDoesBussCurrLiens() || this.getDoesBussOrAnyOwnGuarTax()
    			|| this.getBussOrAnyOwnGuarStateTax() || this.getBussExpAnyManagThreeYears() || this.getSalesToCust() || this.getAreAnyAssetsOfBusiness() || this.getIsAnyOwnGuarDesignated()
    			|| this.getIsThisHomeBasedBuss()){
    		flag = true;
    	}
    	return flag;
    }
}
