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
import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.LookupService;



public class Asset extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 228186268336506544L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public BigDecimal getAssetAmt() {

        if ((getUnitCostAmt() != null) && (getQuantity() != null)) {
            return getUnitCostAmt().multiply(new BigDecimal(getQuantity()));
        }

        return null;
    }
    
    
    public BigDecimal getRateCardResidualRate(){
    	BigDecimal bHundred = new BigDecimal(100);
    	BigDecimal rateCardResidualRate = null; 
    	if(this.getResidualReference() != null && getAssetAmt() != null){
    		BigDecimal residualValue = this.getResidualReference().getResidualValue();
    		if(this.getResidualReference().isResidualTypePercentage()){
    			rateCardResidualRate =  residualValue;
    		}else if(this.getResidualReference().isResidualTypeAmount()){
    			rateCardResidualRate = residualValue.divide(getAssetAmt(), 4, RoundingMode.HALF_EVEN);
    			rateCardResidualRate = rateCardResidualRate.multiply(bHundred);
    		}
    	}
    	return rateCardResidualRate;
    }

    public BigDecimal getRateCardResidualAmt(){
    	BigDecimal bHundred = new BigDecimal(100);
    	BigDecimal rateCardResidualAmt = null; 
    	if(this.getResidualReference() != null && getAssetAmt() != null){
    		BigDecimal residualValue = this.getResidualReference().getResidualValue();
    		if(this.getResidualReference().isResidualTypePercentage()){
    			rateCardResidualAmt = residualValue.multiply(getAssetAmt());
    			rateCardResidualAmt = rateCardResidualAmt.divide(bHundred, 4, RoundingMode.HALF_EVEN);
    		}else if(this.getResidualReference().isResidualTypeAmount()){
    			rateCardResidualAmt =  residualValue;
    		}
    	}
    	return rateCardResidualAmt;
    }
    public BigDecimal getPricedResidualAmount(){
    	// if calculated residual rate is present, use it to compute the amount, if not use the pricing input rate to compute the amount
    	BigDecimal residualAmt = BigDecimal.ZERO;
    	BigDecimal bHundred = new BigDecimal(100);
    	if(getAssetAmt() != null){
        	if(getCalcResidualRate() != null && BigDecimal.ZERO.compareTo(getCalcResidualRate()) < 0 ){
        		residualAmt = getCalcResidualRate().multiply(getAssetAmt());
        		residualAmt = residualAmt.divide(bHundred, 4, RoundingMode.HALF_EVEN);
        	}else if(getPricingInputResidualRate() != null && BigDecimal.ZERO.compareTo(getPricingInputResidualRate()) < 0 ){
        		residualAmt = getPricingInputResidualRate().multiply(getAssetAmt());
        		residualAmt = residualAmt.divide(bHundred, 4, RoundingMode.HALF_EVEN);
        	}
    	}
    	return residualAmt;
    }
    public BigDecimal getPricingInputResidualRate(){
    	// if buy-out option is 10% put or 10% option then use 10 as residual rate,
    	//else
    		// if trac lease(financial product type is TRAC) use tracpct if entered
    		// if buy-out option is fmv and fmvcapPct is entered use fmcCapPct
    		// Otherwise use Ratecard Residual Rate
    	BigDecimal inputResidualRate = null;
    	PricingOption option = getPricingOption();
    	if(option != null){
    		if(option.isTenPctPut() || option.isTenPctOption()){
    			inputResidualRate =  BigDecimal.TEN;
    		}else {
    			if(option.isTracLease() && getTracPct() != null){
        			inputResidualRate =  getTracPct();
        		}else if(option.isFmv() && option.getStructure() != null && option.getStructure().getFmvCapPct() != null){
    				inputResidualRate =  option.getStructure().getFmvCapPct();
    			}else{
        			inputResidualRate =  getRateCardResidualRate();
        		}	
    		}
    		
    		
    	}
    	if(inputResidualRate == null){
    		inputResidualRate = BigDecimal.ZERO;
    	}
    	return inputResidualRate;

    }

    public BigDecimal getPaymentAmt() {
        BigDecimal assetAmt = getAssetAmt();
        BigDecimal lrf = null;

        if ((getPricingOption() != null) && (getPricingOption().getStructure() != null)) {
            lrf = getPricingOption().getStructure().getActualRateFactor();
        }

        if ((assetAmt != null) && (lrf != null)) {
            return assetAmt.multiply(lrf.divide(new BigDecimal(100)));
        }

        return null;
    }

    /**
     * Method to get total Asset cost for a particular asset
     *
     * @return  totalAssetCost
     */
    public BigDecimal getTotalAssetCost() {
        BigDecimal totalAssetCost = BigDecimal.ZERO;

        if ((getAssetCosts() != null) && (getAssetCosts().size() > 0)) {

            for (AssetCost assetCost : getAssetCosts()) {
                totalAssetCost = totalAssetCost.add(assetCost.getAmount());
            }
        }

        return totalAssetCost;
    }


    /**
     * Method to get Total Fee amount
     *
     * @return  totalFeeAmount
     */
    public BigDecimal getTotalFeeAmt() {
        BigDecimal totalFeeAmount = BigDecimal.ZERO;

        if ((getFees() != null) && (getFees().size() > 0)) {

            for (Fee fee : getFees()) {
                totalFeeAmount = totalFeeAmount.add(fee.getFeeAmt());
            }
        }

        return totalFeeAmount;
    }

    /**
     * Method to get Total Sales Tax from AssetTaxes
     *
     * @return  totalSalesTaxAmount
     */
    public BigDecimal getTotalSalesTax() {
        BigDecimal totalSalesTaxAmount = BigDecimal.ZERO;

        if ((getAssetTaxes() != null) && (getAssetTaxes().size() > 0)) {

            for (AssetTaxes assetTaxes : getAssetTaxes()) {
                totalSalesTaxAmount = totalSalesTaxAmount.add(assetTaxes.getSalesTaxAmount());
            }
        }

        return totalSalesTaxAmount;
    }


    /**
     * Method to get Total Sales Tax for Tied Items(Attached Assets)
     *
     * @return  totalSalesTaxAmt
     */
    public BigDecimal getTotalSalesTaxTiedItems() {
        BigDecimal totalSalesTaxAmt = BigDecimal.ZERO;

        if ((getAttachedAssets() != null) && (getAttachedAssets().size() > 0)) {

            for (Asset asset : getAttachedAssets()) {
                totalSalesTaxAmt = totalSalesTaxAmt.add(asset.getSalesTaxAmt());
            }
        }

        return totalSalesTaxAmt;
    }


    /**
     * Method to get Total Shipping Tax Amount from AssetTaxes
     *
     * @return  totalShippingTaxAmount
     */
    public BigDecimal getTotalShippingTaxAmt() {
        BigDecimal totalShippingTaxAmount = BigDecimal.ZERO;

        if ((getAssetTaxes() != null) && (getAssetTaxes().size() > 0)) {

            for (AssetTaxes assetTaxes : getAssetTaxes()) {
                totalShippingTaxAmount = totalShippingTaxAmount.add(assetTaxes.getShippingTaxAmount());
            }
        }

        return totalShippingTaxAmount;
    }

    /**
     * Method to get Total Unit Cost for Tied Items
     *
     * @return  totalTiedItemsCost
     */
    public BigDecimal getTotalTiedItemsCost() {
        BigDecimal totalTiedItemsCost = BigDecimal.ZERO;

        if ((getAttachedAssets() != null) && (getAttachedAssets().size() > 0)) {

            for (Asset asset : getAttachedAssets()) {
                totalTiedItemsCost = totalTiedItemsCost.add(asset.getAssetCost().getAmount());
            }
        }

        return totalTiedItemsCost;
    }

    /**
     * Method to get Total Transit Tax amount for AssetTaxes
     *
     * @return  totalTransitTaxAmount
     */
    public BigDecimal getTotalTransitTaxAmt() {
        BigDecimal totalTransitTaxAmount = BigDecimal.ZERO;

        if ((getAssetTaxes() != null) && (getAssetTaxes().size() > 0)) {

            for (AssetTaxes assetTaxes : getAssetTaxes()) {
                totalTransitTaxAmount = totalTransitTaxAmount.add(assetTaxes.getTransitTaxAmount());
            }
        }

        return totalTransitTaxAmount;
    }
    
    /**
     * Method to get Total AssetCost amount
     *
     * @return  totalAssetCostAmount
     */
    public BigDecimal getAssetCostAmt() {
        BigDecimal totalAssetCostAmount = BigDecimal.ZERO;

        if ((getAssetCosts() != null) && (getAssetCosts().size() > 0)) {
            for (AssetCost assetCost : getAssetCosts()) {
            	if(assetCost.getAmount() != null){
            		totalAssetCostAmount = totalAssetCostAmount.add(assetCost.getAmount());
            	}	
            }
        }
        return totalAssetCostAmount;
    }
    
    /**
     * Method to get Total of total Cost Assets and all Asset Costs
     *
     * @return  totalAssetCostAndAssetAmountCost
     */
    
    public BigDecimal getTotalAssetCostAndAssetAmountCost(){
		 BigDecimal totalAssetCostAndAssetAmountCost = BigDecimal.ZERO;
	   	 if(getAssetAmt() != null){
	   		totalAssetCostAndAssetAmountCost = totalAssetCostAndAssetAmountCost.add(getAssetAmt());
	   	 } 
	   	 totalAssetCostAndAssetAmountCost = totalAssetCostAndAssetAmountCost.add(getAssetCostAmt());
	   	 return totalAssetCostAndAssetAmountCost;
	   }
    
    /**
     * Method to get Total Other Cost
     *
     * @return  totalOtherCost
     */
    public BigDecimal getSoftCost() {
        BigDecimal totalOtherCost = BigDecimal.ZERO;
        if (getAssetCosts() != null) {
            for (AssetCost assetCost : getAssetCosts()) {
            	totalOtherCost = totalOtherCost.add(assetCost.getAmount());
            }
        }

        return totalOtherCost;
    }
    	
}
