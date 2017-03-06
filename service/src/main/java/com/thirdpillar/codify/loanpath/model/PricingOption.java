package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.model.Asset;
import com.thirdpillar.codify.loanpath.model.LeaseExtension;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.h2.constant.SysProperties;

@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byExternalRefId",
	            keys = { "externalRefId" }
	        ) }
	)
/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class PricingOption extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 5201583945100397076L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public BigDecimal getAnnualizedCostPct() {
        return null;
    }

    public BigDecimal getCPIBrickedPct() {
        return null;
    }

    public BigDecimal getCPISyndicatedPct() {
        return null;
    }

    public BigDecimal getExtensionRateFactorRatio() {

        if ( (this.getStructure().getLeaseExtension() != null) && (this.getStructure().getLeaseExtension().getExtensionRateFactor() != null) &&
                (this.getStructure() != null) && (this.getStructure().getActualRateFactor() != null) &&
                (this.getStructure().getActualRateFactor().compareTo(BigDecimal.ZERO) != 0)) {
            return this.getStructure().getLeaseExtension().getExtensionRateFactor().divide(this.getStructure().getActualRateFactor(),2, RoundingMode.HALF_UP);
        }

        return null;
    }

    public BigDecimal getGainPct() {
        return null;
    }

    public BigDecimal getGrossRevenuePct() {
        return null;
    }

    public BigDecimal getInterimRentPct() {
        return null;
    }

    public BigDecimal getROAPreTaxPct() {
        return null;
    }

    public BigDecimal getROEAfterTaxPct() {
        return null;
    }

    public BigDecimal getROEPreTaxPct() {
        return null;
    }

    public BigDecimal getShippingCost() {

        if (getExtOption() != null) {
            BigDecimal netRevenueAmt = getExtOption().getNetRevenueAmt();
            BigDecimal quoteAmt = getExtOption().getSubTotalAmt();

            if ((netRevenueAmt != null) && (quoteAmt != null)) {
                return netRevenueAmt.subtract(quoteAmt);
            }
        }

        return null;
    }

    public BigDecimal getSyndicationOffsetPct() {
        return null;
    }

    public BigDecimal getTotalExpensesPct() {
        return null;
    }
    
    public BigDecimal getTotalAmt(){
    	BigDecimal total = BigDecimal.ZERO;
    	if(getAssets() != null){
    		for(Asset asset : getAssets()){
    			if(asset.getAssetAmt() !=null){
    				total = total.add(asset.getAssetAmt());
    			}
    		}
    	}
    	return total;
    }
    
    public BigDecimal getTotalPaymentAmt(){
    	BigDecimal total = BigDecimal.ZERO;
    	if(getAssets() != null){
    		for(Asset asset : getAssets()){
    			if(asset.getPaymentAmt() !=null){
    				total = total.add(asset.getPaymentAmt());
    			}
    		}
    	}
    	return total;
    }
    public Asset getQuoteAsset() {

        if ((getAssets() != null) && (getAssets().size() > 0)) {
            return getAssets().get(0);
        }

        return null;
    }

    public void setQuoteAsset(Asset asset) {
        addToAssets(asset);
        asset.setPricingOption(this);
    }
    public BigDecimal getTotalEquipmentCost(){
    	BigDecimal totalEquipmentCost = BigDecimal.ZERO;
    	if(getAssets() != null){
    		for(Asset asset : getAssets()){
    			if(asset.getAssetAmt() !=null){
    				totalEquipmentCost = totalEquipmentCost.add(asset.getAssetAmt());
    			}
    		}
    	}
    	return totalEquipmentCost;
    }
    public BigDecimal getTotalResidualAmount(){
    	BigDecimal aggregatedResidualAmt = BigDecimal.ZERO;

    	if(getAssets() != null){
    		for(Asset asset : getAssets()){
        		aggregatedResidualAmt = aggregatedResidualAmt.add(asset.getPricedResidualAmount());
    		}
    	}
    	return aggregatedResidualAmt;
    }
    
    
    public BigDecimal getAggregatedResidualRate(){
    	BigDecimal totalResidualAmt = getTotalResidualAmount();
    	BigDecimal totalEquipmentCost = getTotalEquipmentCost();
    	BigDecimal aggregatedResidualRate = BigDecimal.ZERO;
    	BigDecimal hundred = new BigDecimal(100);

    		if(totalResidualAmt.compareTo(BigDecimal.ZERO) > 0 && totalEquipmentCost.compareTo(BigDecimal.ZERO) > 0){
    			aggregatedResidualRate = totalResidualAmt.divide(totalEquipmentCost,4,RoundingMode.HALF_EVEN);
    			aggregatedResidualRate = aggregatedResidualRate.multiply(hundred);
    		}
    	return aggregatedResidualRate;
    }
    
    public Boolean isTracLease(){
    	if(this.getStructure() != null && this.getStructure().getFinancialProductType() != null){
    		if(StringUtils.equals("FINANCIAL_PRODUCT_TYPE_TRAC", this.getStructure().getFinancialProductType().getKey())){
    			return Boolean.TRUE;
    		}
    	}
    	return Boolean.FALSE;
    }
    
    public Boolean isFmv(){
    	if(this.getStructure() != null && this.getStructure().getPurchaseOption() != null){
    		if(StringUtils.equals("PURCHASE_OPTION_FAIR_MARKET_VALUE", this.getStructure().getPurchaseOption().getKey())){
    			return Boolean.TRUE;
    		}
    	}
    	return Boolean.FALSE;
    }
    public Boolean isTenPctPut(){
    	if(this.getStructure() != null &&  this.getStructure().getPurchaseOption() != null){
    		if(StringUtils.equals("PURCHASE_OPTION_10_PUT",  this.getStructure().getPurchaseOption().getKey())){
    			return Boolean.TRUE;
    		}
    	}
    	return Boolean.FALSE;
    }
    public Boolean isTenPctOption(){
    	if(this.getStructure() != null &&  this.getStructure().getPurchaseOption() != null){
    		if(StringUtils.equals("PURCHASE_OPTION_10_OPTION",  this.getStructure().getPurchaseOption().getKey())){
    			return Boolean.TRUE;
    		}
    	}
    	return Boolean.FALSE;
    }
    
    public Boolean isCmmncmntDtAftrSrvicDt(){
    	if(this.getStructure() != null && this.getStructure().getPaymentStartDate() != null){
    		Date commencenmentDate = this.getStructure().getPaymentStartDate();
    		for(Asset asset : getAssets()){
        		if(commencenmentDate.after(asset.getInServiceDate()) || commencenmentDate.equals(asset.getInServiceDate())){
        			return Boolean.TRUE;
        		}
        	}
    	}
    	return Boolean.FALSE;
    }
   
}