package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class ResidualReference extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 5201583945100397076L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public Boolean isResidualTypePercentage(){
    	if ("RESIDUAL_TYPE_EQUIPMENT_PERCENTAGE".equals(getResidualType().getKey())){
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }

    public Boolean isResidualTypeAmount(){
    	if ("RESIDUAL_TYPE_AMOUNT".equals(getResidualType().getKey())){
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    	
    }
    
}