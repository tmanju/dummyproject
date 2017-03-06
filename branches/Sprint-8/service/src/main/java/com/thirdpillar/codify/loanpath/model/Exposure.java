package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.LookupService;

public class Exposure {

	public static final String TOTAL_BORROWER_REQUESTED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_REQUESTED_EXPOSURE";
	public static final String TOTAL_CONTINGENT_REQUESTED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_REQUESTED_EXPOSURE";
	public static final String TOTAL_CALCULATED_REQUESTED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_REQUESTED_EXPOSURE";
	
	public static final String EXPOSURE_TYPE_TOTAL_DIRECT_EXPOSURE = "EXPOSURE_SUMMARY_TYPE_TOTAL_DIRECT_FACILITY_EXPOSURE";
	public static final String EXPOSURE_TYPE_TOTAL_INDIRECT_EXPOSURE = "EXPOSURE_SUMMARY_TYPE_TOTAL_INDIRECT_EXPOSURE";
	public static final String EXPOSURE_TYPE_TOTAL_DIRECT_INDIRECT_EXPOSURE = "EXPOSURE_SUMMARY_TYPE_TOTAL_DIRECT_AND_INDIRECT";


	public static final String TOTAL_BORROWER_OUTSTANDING_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_OUTSTANDING_EXPOSURE";
	public static final String TOTAL_CONTINGENT_OUTSTANDING_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_OUTSTANDING_EXPOSURE";
	public static final String TOTAL_CALCULATED_OUTSTANDING_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_OUTSTANDING_EXPOSURE";


	public static final String TOTAL_BORROWER_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_EXPOSURE";
	public static final String TOTAL_CONTINGENT_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_EXPOSURE";
	public static final String TOTAL_CALCULATED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_EXPOSURE";


	public static final String TOTAL_BORROWER_REQUESTED_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_REQUESTED_UNSECURED_EXPOSURE";
	public static final String TOTAL_CONTINGENT_REQUESTED_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_REQUESTED_UNSECURED_EXPOSURE";
	public static final String TOTAL_CALCULATED_REQUESTED_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_REQUESTED_UNSECURED_EXPOSURE";


	public static final String TOTAL_BORROWER_OUTSTANDING_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_OUTSTANDING_UNSECURED_EXPOSURE";
	public static final String TOTAL_CONTINGENT_OUTSTANDING_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_OUTSTANDING_UNSECURED_EXPOSURE";
	public static final String TOTAL_CALCULATED_OUTSTANDING_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_OUTSTANDING_UNSECURED_EXPOSURE";


	public static final String TOTAL_BORROWER_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_BORROWER_REQUESTED_UNSECURED_EXPOSURE";
	public static final String TOTAL_CONTINGENT_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CONTINGENT_REQUESTED_UNSECURED_EXPOSURE";
	public static final String TOTAL_CALCULATED_UNSECURED_EXPOSURE = "EXPOSURE_TYPE_TOTAL_CALCULATED_UNSECURED_EXPOSURE";



	public BigDecimal getExposureAmount(String exposureType) {
		ExposureSummary exposureSummary = getExposureSummary(exposureType);
		if (exposureSummary != null) {
			return exposureSummary.getExposureValue();
		} else {
			return null;
		}
	}

	public ExposureSummary getExposureSummary(String exposureType) {
		for (ExposureSummary exposureSummary : getExposureSummaries()) {
			if (exposureSummary.getExposureType() != null
					&& StringUtils.equals(exposureSummary.getExposureType()
							.getKey(), exposureType)) {
				return exposureSummary;
			}
		}

		return null;
	}

	public BigDecimal getTotalBorrowerRequestedExposure() {
		return getExposureAmount(TOTAL_BORROWER_REQUESTED_EXPOSURE);
	}

	public BigDecimal getTotalContingentRequestedExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_REQUESTED_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedRequestedExposure() {
		return getExposureAmount(TOTAL_CALCULATED_REQUESTED_EXPOSURE);
	}

	public BigDecimal getTotalRelationshipRequestedExposure() {
		return getExposureAmount(EXPOSURE_TYPE_TOTAL_DIRECT_INDIRECT_EXPOSURE);
	}

	public BigDecimal getTotalBorrowerOutstandingExposure() {
		return getExposureAmount(TOTAL_BORROWER_OUTSTANDING_EXPOSURE);
	}

	public BigDecimal getTotalContingentOutstandingExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_OUTSTANDING_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedOutstandingExposure() {
		return getExposureAmount(TOTAL_CALCULATED_OUTSTANDING_EXPOSURE);
	}

	public BigDecimal getTotalRelationshipOutstandingExposure() {
		return add(getTotalCalculatedOutstandingExposure(),
				getTotalOtherOutstandingExposure());
	}

	public BigDecimal getTotalBorrowerExposure() {
		return getExposureAmount(TOTAL_BORROWER_EXPOSURE);
	}

	public BigDecimal getTotalContingentExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedExposure() {
		return getExposureAmount(TOTAL_CALCULATED_EXPOSURE);
	}

	public BigDecimal getTotalOtherExposure() {
		return add(getTotalOtherRequestedExposure(),
				getTotalOtherOutstandingExposure());
	}

	public BigDecimal getTotalRelationshipExposure() {
		return add(getTotalCalculatedExposure(), getTotalOtherExposure());
	}

	public BigDecimal getTotalBorrowerRequestedUnsecuredExposure() {
		return getExposureAmount(TOTAL_BORROWER_REQUESTED_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalContingentRequestedUnsecuredExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_REQUESTED_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedRequestedUnsecuredExposure() {
		return getExposureAmount(TOTAL_CALCULATED_REQUESTED_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalRelationshipRequestedUnsecuredExposure() {
		return add(getTotalCalculatedRequestedUnsecuredExposure(),
				getTotalOtherRequestedUnsecuredExposure());
	}

	public BigDecimal getTotalBorrowerOutstandingUnsecuredExposure() {
		return getExposureAmount(TOTAL_BORROWER_OUTSTANDING_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalContingentOutstandingUnsecuredExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_OUTSTANDING_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedOutstandingUnsecuredExposure() {
		return getExposureAmount(TOTAL_CALCULATED_OUTSTANDING_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalRelationshipOutstandingUnsecuredExposure() {
		return add(getTotalCalculatedOutstandingUnsecuredExposure(),
				getTotalOtherOutstandingUnsecuredExposure());
	}

	public BigDecimal getTotalBorrowerUnsecuredExposure() {
		return getExposureAmount(TOTAL_BORROWER_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalContingentUnsecuredExposure() {
		return getExposureAmount(TOTAL_CONTINGENT_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalCalculatedUnsecuredExposure() {
		return getExposureAmount(TOTAL_CALCULATED_UNSECURED_EXPOSURE);
	}

	public BigDecimal getTotalOtherUnsecuredExposure() {
		return add(getTotalOtherRequestedUnsecuredExposure(),
				getTotalOtherOutstandingUnsecuredExposure());
	}

	public BigDecimal getTotalRelationshipUnsecuredExposure() {
		return add(getTotalCalculatedUnsecuredExposure(),
				getTotalOtherUnsecuredExposure());
	}

	private BigDecimal add(BigDecimal... amounts) {
		BigDecimal total = BigDecimal.ZERO;
		for (int i = 0; i < amounts.length; i++) {
			if (amounts[i] != null) {
				total = total.add(amounts[i]);
			}
		}
		return total;
	}

	public FacilityDelinquencyInfo getFacilityDelinquencyInfo(Facility facility) {
		if (getDelinquencies() != null) {
			for (FacilityDelinquencyInfo facilityDelinquencyInfo : getDelinquencies()) {
				if (facilityDelinquencyInfo.getFacility() != null
						&& facilityDelinquencyInfo.getFacility().getId() == facility
								.getId()) {
					return facilityDelinquencyInfo;
				}
			}
		}
		return null;
	}

	public FacilityExposure findFacilityExposure(Facility facility) {
		if (getFacilityExposures() != null) {
			for (FacilityExposure exposure : getFacilityExposures()) {
				if (exposure.getFacility() != null
						&& exposure.getFacility().getId() == facility.getId()) {
					return exposure;
				}
			}
		}
		return null;
	}

	public FacilityExposure findFacilityExposure(Customer counterParty,
			String productType) {
		if (getFacilityExposures() != null) {
			for (FacilityExposure exposure : getFacilityExposures()) {
				if (exposure.getCounterParty() != null
						&& exposure.getCounterParty().getId() == counterParty
								.getId()) {
					if (exposure.getFacilityType() != null
							&& StringUtils.equals(exposure.getFacilityType()
									.getKey(), productType)) {
						return exposure;
					}
				}
			}
		}
		return null;

	}

	public List<FacilityExposure> findFacilityExposures(Customer customer,
			boolean accountParty) {
		List<FacilityExposure> exposures = new ArrayList<FacilityExposure>();
		if (getFacilityExposures() != null) {
			for (FacilityExposure facilityExposure : getFacilityExposures()) {
				if (accountParty) {
					if (facilityExposure.getAccountCustomers() != null
							&& facilityExposure.getAccountCustomers().contains(
									customer)) {
						exposures.add(facilityExposure);
					}
				} else {
					if (facilityExposure.getRelatedCustomers() != null
							&& facilityExposure.getRelatedCustomers().contains(
									customer)) {
						exposures.add(facilityExposure);
					}

				}
			}
		}
		return exposures;
	}

	public FacilityExposure getFacilityExposureByFacility(Facility facility,
			boolean existingFacility, Request request) {
		FacilityExposure facilityExposure = new FacilityExposure();
		facilityExposure.setFacilityNumber(Long.parseLong(facility
				.getRefNumber()));

		if (request.getId() != facility.getRequest().getId()) {
			facilityExposure.setType(checkPipeLine(facility));
		}

		facilityExposure.setFacilityType(facility.getFacilityType());
		facilityExposure.setRequestedExposure(facility.getRequestedLoanAmt());

		String accountNumber = getFacAccountNumber(facility);

		if (accountNumber == null) {
			accountNumber = "";
		}

		if (Constants.APPLICATION_TYPE_SMALL_TICKET_KEY
				.equalsIgnoreCase(request.getRequestType().getKey())) {
			Customer customer = getPrimaryBorrowerFromFacility(facility);

			if (customer != null) {
				facilityExposure.setBorrower(customer);
				facilityExposure.setBorrowerName(customer.getCustomerName());
				facilityExposure
						.setTaxPayerIdNumber(customer.getFederalTaxId());
			}
		}

		facilityExposure.setAccountNumber(accountNumber);
		facilityExposure.setExistingFacility(existingFacility);
		facilityExposure.setOutstandingExposure(BigDecimal.ZERO);
		facilityExposure
				.setExistingExposure(getFacApproveExposureAmt(facility));

		if (facility.getFacilityProductCode() != null) {
			facilityExposure.setProdCode(facility.getFacilityProductCode());
		}

		return facilityExposure;
	}
	
	public Customer getPrimaryBorrowerFromFacility(Facility facility) {
        Customer customer = null;

        for (RelationshipParty facRelParty : facility.getRequest().getRelationship().getRelatedParties()) {

            if ((facRelParty.getAssignedRoles() != null) &&
                    facRelParty.hasAssignedRole(Constants.PARTY_ROLE_TYPE_PRIMARY_BORROWER_KEY_VAL)) {
                customer = facRelParty.getCustomer();

                break;
            }
        }

        return customer;
    }
	
	public BigDecimal getFacApproveExposureAmt(Facility fac) {
        BigDecimal expAmt = BigDecimal.ZERO;

        if ("FACILITY_STATUS_APPROVED_DOCUMENTATION".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                "FACILITY_STATUS_QC_PENDING".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                "FACILITY_STATUS_SUBMITTED_TO_BOOKING".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                "FACILITY_STATUS_VALIDATION_FAILED".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                "FACILITY_STATUS_VALIDATION_PASSED".equalsIgnoreCase(fac.getWfStatus().getStatusKey())) {
            expAmt = expAmt.add(fac.getApprovedLoanAmt());
        }

        return expAmt;
    }
	
	public String getFacAccountNumber(Facility facility) {
        String accNumber = "";

        if (facility != null) {

            if (facility.getWithCheckAccess() &&
            		"BUSINESS_CREDIT_ACCOUNT".equals(facility.getFacilityType().getKey())) {
                accNumber = String.valueOf(facility.getObligationNumber());
            }
        } else if (facility.getWithCheckAccess() &&
        		"BUSINESS_CHECKING_PLUS".equals(facility.getFacilityType().getKey())) {
            accNumber = String.valueOf(facility.getLinkToCheckingAccount());
        }

        return (accNumber == null) ? "" : accNumber;
    }
	
	public String checkPipeLine(Facility fac) {
        String pipeLine = null;

        if (fac != null) {

            if ("FACILITY_STATUS_APPROVED_DOCUMENTATION".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                    "FACILITY_STATUS_QC_PENDING".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                    "FACILITY_STATUS_SUBMITTED_TO_BOOKING".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                    "FACILITY_STATUS_VALIDATION_FAILED".equalsIgnoreCase(fac.getWfStatus().getStatusKey()) ||
                    "FACILITY_STATUS_VALIDATION_PASSED".equalsIgnoreCase(fac.getWfStatus().getStatusKey())) {
                pipeLine = Constants.FACILITY_EXPOSURE_TYPE_PIPELINE;
            }
        }

        return pipeLine;
    }
	
	 public Request getRequest() {
	        List<BaseDataObject> baseDataObjectList = LookupService.getResults("Request.byExposureId",
	                new String[] { "exposureId" }, new Object[] { this.getId() });

	        if (!baseDataObjectList.isEmpty()) {
	            return (Request) baseDataObjectList.get(0);
	        }

	        return null;
	    }

}