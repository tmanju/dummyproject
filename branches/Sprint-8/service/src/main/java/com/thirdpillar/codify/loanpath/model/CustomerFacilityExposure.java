



package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.LookupService;


public  class CustomerFacilityExposure extends BaseDataObject {
		
	public BigDecimal getTotalRequestedExposure() {
		BigDecimal total = BigDecimal.ZERO;
		for (FacilityExposure facilityExposure : getFacilityExposures()) {
			if (facilityExposure.getRequestedExposure() != null) {
				total = total.add(facilityExposure.getRequestedExposure());
			}
		}
		return total;
	}

	public BigDecimal getTotalOutstandingExposure() {
		BigDecimal total = BigDecimal.ZERO;
		for (FacilityExposure facilityExposure : getFacilityExposures()) {
			if (facilityExposure.getOutstandingExposure() != null) {
				total = total.add(facilityExposure.getOutstandingExposure());
			}
		}
		return total;
	}
	
	private static final Logger LOGGER = Logger.getLogger(CustomerFacilityExposure.class);

    //~ Methods --------------------------------------------------------------------------------------------------------

    public BigDecimal getAdjustedSfCredit() {
        BigDecimal total = getTotalAdjustedAmountOfFacilityExposures(getSoftFacExpsrs());

        return total;
    }

    public List<FacilityExposure> getDirectExposures() {
        long t1 = System.currentTimeMillis();
        LOGGER.info("..... Entering Direct FacilityExposures Values.. start time : " + t1);

        Exposure exposure = getExposure();
        Request request = exposure.getRequest();
        List<BaseDataObject> baseDataObjectList = null;
        Customer customer = null;
        FacilityExposure facilityExposure = null;
        List<FacilityExposure> facilityExposures = new ArrayList<FacilityExposure>();

        if ((request != null) && (this.getRelParty() != null) && (this.getRelParty().getAssignedRolesKey() != null)) {
            LOGGER.info("...... Calculating ExposureValues for application No : " + request.getRefNumber());
            customer = this.getRelParty().getCustomer();

            if ((request.isSmallTicket() && this.getRelParty().isPrimaryBorrower()) ||
                    (request.isStandardTicket() &&
                        (this.getRelParty().isPrimaryBorrower() || this.getRelParty().isBorrower()))) {
                baseDataObjectList = LookupService.getResults("Facility.byRelationshipParty",
                        new String[] { "customerId", "requestId" }, new Object[] { customer.getId(), request.getId() });

                for (Facility activeFacility : request.getAllActiveFacilities()) {
                    facilityExposures.add(exposure.getFacilityExposureByFacility(activeFacility, true, request));
                }

                for (BaseDataObject baseDataObj : baseDataObjectList) {
                    Facility facility = (Facility) baseDataObj;

                    for (RelationshipParty facRelParty : facility.getRequest().getRelationship().getRelatedParties()) {

                        if ((facRelParty.getCustomer().getId() == this.getRelParty().getCustomer().getId()) &&
                                (facRelParty.getAssignedRoles() != null) &&
                                (facRelParty.isPrimaryBorrower() || facRelParty.isBorrower())) {
                            facilityExposures.add(exposure.getFacilityExposureByFacility(facility, true, request));

                            break;
                        }
                    }
                }

                if ((customer.getPartyExposure() != null) &&
                        (customer.getPartyExposure().getPartyFacilities() != null)) {
                    LOGGER.info("...... Calculating ICV ExposureValues for Customer Ref No : " +
                        customer.getRefNumber());

                    for (PartyFacility partyFacility : customer.getPartyExposure().getPartyFacilities()) {
                        LOGGER.info("...... Loop ICV ExposureValues for Customer ");
                        facilityExposure = new FacilityExposure();
                        facilityExposure.setAccountNumber("" + partyFacility.getObligationNumber());
                        facilityExposure.setExistingExposure(partyFacility.getExistingDirectExposure());
                        facilityExposure.setOutstandingExposure(partyFacility.getOutstanding());
                        facilityExposure.setProdCode(partyFacility.getProductTypeCode());
                        facilityExposures.add(facilityExposure);
                    }
                } else {
                    exposure.setNoLoanExposFromIMPACS(Constants.NO_LOAN_EXPOSURE_FROM_AFS_IMPACS);
                }
            }
        }

        return facilityExposures;
    }

    public BigDecimal getExistingExposureByFacilityType(String facilityType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityType() != null) &&
                StringUtils.equals(facilityExposure.getFacilityType().getKey(), facilityType)) &&
                (facilityExposure.getExistingExposure() != null)) {
                    total = total.add((facilityExposure.getExistingExposure() == null)
                            ? BigDecimal.ZERO : facilityExposure.getExistingExposure());
                }
            }

        return total;
    }

    public BigDecimal getExistingExposurePerFacility(Facility facility) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityNumber() != null) &&
                    StringUtils.endsWithIgnoreCase(String.valueOf(facilityExposure.getFacilityNumber()),
                        facility.getRefNumber())) && (facilityExposure.getExistingExposure() != null)) {
                    total = facilityExposure.getExistingExposure();

                    break;
                }
            }

        return total;
    }

    public BigDecimal getExistingIncrementalSfCredit() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalRequestedSfCredit());
        total = total.add(getTotalExistingSfCredit());
        total = total.add(getTotalAdjustedSfCredit());

        return total;
    }

    public BigDecimal getExistingSfCredit() {
        BigDecimal total = getTotalExistingExposureOfFacilityExposures(getSoftFacExpsrs());

        return total;
    }

    public BigDecimal getExposureAdjustmentsByFacilityType(String facilityType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if ((facilityExposure.getFacilityType() != null) &&
                    StringUtils.equals(facilityExposure.getFacilityType().getKey(), facilityType)) {

              
                total = total.add((facilityExposure.getAdjustedExposure() == null)
                        ? BigDecimal.ZERO : facilityExposure.getAdjustedExposure());
               
            }
        }

        return total;
    }

    public BigDecimal getExposureAdjustmentsPerFacility(Facility facility) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityNumber() != null) &&
                    StringUtils.endsWithIgnoreCase(String.valueOf(facilityExposure.getFacilityNumber()),
                        facility.getRefNumber())) && (facilityExposure.getAdjustedExposure() != null)) {
                    total = facilityExposure.getAdjustedExposure();

                    break;
                }
            }

        return total;
    }

    public List<FacilityExposure> getInDirExposures() {
        List<FacilityExposure> facilityExposures = new ArrayList<FacilityExposure>();
        List<BaseDataObject> baseDataObjectList = null;
        Exposure exposure = getExposure();
        Request request = exposure.getRequest();

        if (request != null) {
            LOGGER.info("###Calculating InDirect ExposureValues for application No : " + request.getRefNumber());

            if ((this.getRelParty() != null) && this.getRelParty().isGuarantor()) {
                LOGGER.info("###Calculating InDirect CustomerFacilityExposure for application No : " + request.getId());
                baseDataObjectList = LookupService.getResults("Facility.byRelationshipParty",
                        new String[] { "customerId", "requestId" },
                        new Object[] { this.getRelParty().getCustomer().getId(), request.getId() });

                for (BaseDataObject baseDataObj : baseDataObjectList) {
                    Facility facility = (Facility) baseDataObj;

                    for (RelationshipParty facRelParty : facility.getRequest().getRelationship().getRelatedParties()) {

                        if ((facRelParty.getCustomer().getId() == this.getRelParty().getCustomer().getId()) &&
                                facRelParty.isGuarantor()) {
                            facilityExposures.add(exposure.getFacilityExposureByFacility(facility, false, request));

                            break;
                        }
                    }
                }

                baseDataObjectList = null;
            }
        }

        return facilityExposures;
    }

    public BigDecimal getOutstandingOfFacilityExposures(List<FacilityExposure> facilityExposures) {
        BigDecimal outstandingAmount = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : facilityExposures) {

            if (facilityExposure.getOutstandingExposure() != null) {
                outstandingAmount = outstandingAmount.add(facilityExposure.getOutstandingExposure());
            }
        }

        return outstandingAmount;
    }

    public BigDecimal getOutstandingSfCredit() {
        BigDecimal total = getOutstandingOfFacilityExposures(getSoftFacExpsrs());

        return total;
    }

    public BigDecimal getPipelineAmountByFacilityType(String facilityType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityType() != null) &&
               StringUtils.equals(facilityExposure.getFacilityType().getKey(), facilityType)) 
               && (facilityExposure.getType() != null) && (Constants.FACILITY_EXPOSURE_TYPE_PIPELINE.equalsIgnoreCase(facilityExposure.getType()))) {
                        total = total.add((facilityExposure.getRequestedExposure() == null)
                                ? BigDecimal.ZERO : facilityExposure.getRequestedExposure());
                    }
        		}

        return total;
    }

    public Long getRelatedCustomerId() {
        return this.getRelParty().getCustomer().getId();
    }

    public BigDecimal getRequestedExposureByFacilityType(String facilityType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityType() != null) &&
                    StringUtils.equals(facilityExposure.getFacilityType().getKey(), facilityType)) 
                && (facilityExposure.getRequestedExposure() != null)) {
                    total = total.add((facilityExposure.getRequestedExposure() == null)
                            ? BigDecimal.ZERO : facilityExposure.getRequestedExposure());
                }
        }

        return total;
    }

    public BigDecimal getRequestedExposurePerFacility(Facility facility) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if (((facilityExposure.getFacilityNumber() != null) &&
                    StringUtils.endsWithIgnoreCase(String.valueOf(facilityExposure.getFacilityNumber()),
                        facility.getRefNumber())) && (facilityExposure.getRequestedExposure() != null)) {
                    total = facilityExposure.getRequestedExposure();

                    break;
                }
        }

        return total;
    }

    public BigDecimal getRequestedPlusExistingExposureByFacilityType(String facilityType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if ((facilityExposure.getFacilityType() != null) &&
                    StringUtils.equals(facilityExposure.getFacilityType().getKey(), facilityType)) {
                total = total.add((facilityExposure.getRequestedExposure() == null)
                        ? BigDecimal.ZERO : facilityExposure.getRequestedExposure());
                total = total.add((facilityExposure.getExistingExposure() == null)
                        ? BigDecimal.ZERO : facilityExposure.getExistingExposure());
            }
        }

        return total;
    }

    public BigDecimal getRequestedSfCredit() {
        BigDecimal total = getTotalExistingExposureOfFacilityExposures(getSoftFacExpsrs());

        return total;
    }

    public BigDecimal getSubTotalOfAdjustedSfCredit() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add((getSoftCreditExistingAdjustment() == null) ? BigDecimal.ZERO
                                                                      : getSoftCreditExistingAdjustment());

        return total;
    }

    public BigDecimal getTotalAdjustedAmountOfFacilityExposures(List<FacilityExposure> facilityExposures) {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : facilityExposures) {
            total = total.add((facilityExposure.getAdjustedExposure() == null)
                    ? BigDecimal.ZERO : facilityExposure.getAdjustedExposure());
        }

        return total;
    }

    public BigDecimal getTotalAdjustedSfCredit() {
        BigDecimal total = getAdjustedSfCredit();
        total = total.add(getSubTotalOfAdjustedSfCredit());

        return total;
    }

    public BigDecimal getTotalDirectExistIncremental() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalExistingIncrementalExposure());
        total = total.add((getExistingDirectAdjustment() == null) ? BigDecimal.ZERO : getExistingDirectAdjustment());

        return total;
    }

    public BigDecimal getTotalDirectExistingAdjustment() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalExistingExposure());
        total = total.add((getExistingDirectAdjustment() == null) ? BigDecimal.ZERO : getExistingDirectAdjustment());

        return total;
    }

    public BigDecimal getTotalDirectExposureAdjustments() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalExposureAdjustments());

        return total;
    }

    /**
     * Method for "Total - Direct (application + pipeline + existing)" panel Start here.
     */
    public BigDecimal getTotalDirectOutstanding() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalOutstandingExposure());
        total = total.add((getOutstandingAdjustment() == null) ? BigDecimal.ZERO : getOutstandingAdjustment());

        return total;
    }

    public BigDecimal getTotalDirectRequestedExposure() {
        BigDecimal total = getTotalRequestedExposure();

        return total;
    }

    public BigDecimal getTotalExistIncremenSfCredit() {
        BigDecimal total = getExistingIncrementalSfCredit();
        total = total.add(getSubTotalOfAdjustedSfCredit());

        return total;
    }

    public BigDecimal getTotalExistingExposure() {
        BigDecimal total = getTotalExistingExposureOfFacilityExposures(getFacilityExposures());

        return total;
    }

    public BigDecimal getTotalExistingExposureOfFacilityExposures(List<FacilityExposure> facilityExposures) {
        BigDecimal existingExposure = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : facilityExposures) {

            if (facilityExposure.getExistingExposure() != null) {
                existingExposure = existingExposure.add(facilityExposure.getExistingExposure());
            }
        }

        return existingExposure;
    }

    public BigDecimal getTotalExistingIncrementalExposure() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getTotalRequestedExposure());
        total = total.add(getTotalExistingExposure());
        total = total.add(getTotalExposureAdjustments());

        return total;
    }

    public BigDecimal getTotalExistingSfCredit() {
        BigDecimal total = getExistingSfCredit();

        return total;
    }

    public BigDecimal getTotalExposureAdjustments() {
        BigDecimal exposureAdjustment = getTotalAdjustedAmountOfFacilityExposures(getFacilityExposures());

        return exposureAdjustment;
    }


    public BigDecimal getTotalOutstandingSfCredit() {
        BigDecimal total = getOutstandingSfCredit();
        total = total.add((getSoftCreditOutstandingAdjustment() == null) ? BigDecimal.ZERO
                                                                         : getSoftCreditOutstandingAdjustment());

        return total;
    }

    public BigDecimal getTotalPipelineAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (FacilityExposure facilityExposure : getFacilityExposures()) {

            if ((facilityExposure.getType() != null) 
                && (Constants.FACILITY_EXPOSURE_TYPE_PIPELINE.equalsIgnoreCase(facilityExposure.getType()))) {
                    total = total.add((facilityExposure.getRequestedExposure() == null)
                            ? BigDecimal.ZERO : facilityExposure.getRequestedExposure());
                }
        }

        return total;
    }


    /**
     * End here.
     */
    /**
     * Method for Show Summary panel related to Soft-credit data-table.
     */
    public BigDecimal getTotalRequestedSfCredit() {
        BigDecimal total = getRequestedSfCredit();

        return total;
    }
    

    public DebtorCustomer getDebtorCustomerDer(){
    	
    	Map<String, Object> holder = ContextHolder.getContext().getNamedContext();
    	FacilityExposure facilityExposure = (FacilityExposure)holder.get("root_facilityExposures");
    	DebtorCustomer debtor1 = null;
		if(facilityExposure != null && this.getCustomerRole() != null && this.getCustomerRole().getId() != null){
			Facility facility = facilityExposure.getFacility();
			for(DebtorCustomer debtor : facility.getDebtors()){
				if(this.getCustomerRole().getCustomer().getId().equals(debtor.getFacilityCustomerRole().getCustomer().getId())){
					debtor1 = debtor;
					break;
				}
			}
		}
		return debtor1;
	}
    
    public List<FacilityExposure> getFacilityExposuresDer() {
    	FacilityCustomerRole customerRole = this.getCustomerRole();
    	List<FacilityExposure> exposures = new ArrayList<FacilityExposure>();
    	List<FacilityExposure> facilityExposures = getExposure().findFacilityExposures(customerRole.getCustomer(), false);
    	if (facilityExposures != null) {
	    	for (FacilityExposure facilityExposure : facilityExposures) {
	    		for(DebtorCustomer d : facilityExposure.getFacility().getDebtors()){
	    			if(customerRole.getCustomer().getId().equals(d.getFacilityCustomerRole().getCustomer().getId())){
	    				facilityExposure.setDebtorCustomerDer(d);
	    				break;
	    			}
	    		}
	    		exposures.add(facilityExposure);
	    	}
    	}
		return exposures;
	}

}