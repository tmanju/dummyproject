/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class RentRoll extends BaseDataObject {
	 //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;
    
    public BigDecimal getTotalRent(){
    	BigDecimal total = BigDecimal.ZERO;
		for(MultiFamily mfamily : getMultiFamilies()){
			if(mfamily.getTotalUnits() != null && mfamily.getAvgRent() != null ){
				total = total.add(mfamily.getTotalUnits().multiply(mfamily.getAvgRent()));
			}
		}
		for(NonMultiFamily nfamily : getNonMtiFmlies()){
			if(nfamily.getNonMultiFamilySize() != null && nfamily.getRent() != null ){
				total = total.add(nfamily.getNonMultiFamilySize().multiply(nfamily.getRent()));
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
    	
    }
}
