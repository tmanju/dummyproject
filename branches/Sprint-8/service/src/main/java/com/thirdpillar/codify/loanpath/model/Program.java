/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import com.thirdpillar.codify.loanpath.model.helper.ProgramHelper;
import com.thirdpillar.foundation.model.Auto;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Size;


/**
 * Model Class for Program
 *
 * @author   shivanand.singh
 * @version  1.0
 * @since    1.0
 */
public class Program extends BaseDataObject {

    //~ Methods --------------------------------------------------------------------------------------------------------

    /**
     * Method to get created date
     */
    public Date getProgramCreatedDate() {
        return new Date();
    }
	public String getDescriptionDer() {
	     return getDescription();
	}
	public String getProgramNameDer() {
	     return getProgramName();
	}
	public Date getStartDttmDer() {
	     return getStartDttm();
	}
	public Date getEndDttmDer() {
	     return getEndDttm();
	}
	public Integer getDerivedVersionNumber() {
	     return getVersionNumber();
	}
	

    /**
     * Method to check if current Payment Frequency exists in Payment Frequency in Program
     */
	public Boolean isKeyInProgramPaymentFrequency(String key){
		boolean match = false;
		if(!getPaymentFrequency().isEmpty()){
			for(AttributeChoice attributeChoice: getPaymentFrequency()){
	    		if(key.equals(attributeChoice.getKey())){
	    			match = true;
	    		}
	    	}
		}else{
		  		match = true;
		}
		return match;
	}
	
	/**
     * Method to check if current Purchase Option exists in Purchase Option in Program
     */
	public Boolean isKeyInProgramPurchaseOption(String key){
		boolean match = false;
		if(getStructure() != null && !getStructure().getPurchaseOptionMulSel().isEmpty()){
	  		for(AttributeChoice attributeChoice : getStructure().getPurchaseOptionMulSel()){
	  			if(key.equals(attributeChoice.getKey())){
	    			match = true;
	    		}
	  		}
	  	}else{
	  		match = true;
	  	}
		return match;
	}
	
	/**
     * Method to check if current Asset Subclass exits in Asset Subclass in Program
     */
	public Boolean isKeyInProgramAssetSubclass(String key){
			boolean match = false;
	  		for(AttributeChoice attributeChoice : getAssetSubclass()){
	  			if(key.equals(attributeChoice.getKey())){
	    			match = true;
	    	}
	  	}
	  	return match;
	}
	
	/**
     * Method to check if current Asset Class exits in AssetClass in Program
     */
	public Boolean isKeyInProgramAssetclass(String key){
		boolean match = false;
		if(getAssetClass() != null){
			if(key.equals(getAssetClass().getKey())){
				match = true;
			}
	  	}else{
	  		match = true;
	  	}
		return match;
	}
	
	
	
}
