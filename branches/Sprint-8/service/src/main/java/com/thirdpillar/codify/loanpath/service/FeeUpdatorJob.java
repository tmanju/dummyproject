package com.thirdpillar.codify.loanpath.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.Fee;
import com.thirdpillar.codify.loanpath.model.FeePolicy;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.codify.loanpath.model.Structure;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;

public class FeeUpdatorJob extends AbstractBusinessOperation{

	public Object execute(String operation, Object... params) {
		EntityService es = new EntityService();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = dateFormat.format(new Date());
		List<FeePolicy> policies = (List<FeePolicy>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FeePolicy.byFeeChanged", new String[]{"feeChanged"}, new Object[]{true});
		if(policies != null && !policies.isEmpty()){
			String[] statuses = new String[]{"REQUEST_STATUS_PENDING_ANALYSIS","REQUEST_STATUS_PENDING_DECISION","REQUEST_STATUS_PENDING_NOIA","REQUEST_STATUS_PENDING_RENEWAL_SUBMITTED_TO_CREDIT","REQUEST_STATUS_PENDING_APPLICATION_SENT","REQUEST_STATUS_APPROVED_TO_BE_NOTIFIED","REQUEST_STATUS_SAVED"};
			List<String> strs = Arrays.asList(statuses);
			List<Request> requests = (List<Request>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Request.byWfStatus", "statusKeys", strs);
			for(Request rq : requests){
				for(Facility facility : rq.getAllActiveFacilities()){
					/*
					 * Updating Fees
					 */
					if(facility.getPricingOption() != null && facility.getPricingOption().getStructure() != null && facility.getPricingOption().getStructure().getFees() != null){
						for(Fee fee : facility.getPricingOption().getStructure().getFees()){
							try{
								updateFee(policies, fee);
								//es.update(fee);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					/*
					 * Updating Pricing and renewal Fee
					 */
					if(facility.getAdditionalOptions() != null){
						for(PricingOption p : facility.getAdditionalOptions()){
							try{
								p.getStructure().setFacility(facility);
								updatePricing(policies, p.getStructure());
								//es.update(p.getStructure());
							}catch(Exception e){
								e.printStackTrace();
							}
							try{
								updateUnusedLineRate(policies, p.getStructure());
								//es.update(p.getStructure());
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					try{
						System.out.println(facility);
						System.out.println(rq);
						es.update(facility);
						es.flush();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			for(FeePolicy feePolicy : policies){
				feePolicy.setFeeChanged(false);
//				if(feePolicy.getEffectiveDefaultFeePct() != null){
//					feePolicy.setDefaultPct(feePolicy.getEffectiveDefaultFeePct());
//					feePolicy.setEffectiveDefaultFeePct(null);
//				}
				feePolicy.setDefaultPct(feePolicy.getDefaultPct());
			}
			es.saveOrUpdateAll(policies);
			es.flush();
		}
		return null;
	}
	
	public void updateFee(List<FeePolicy> feePolicies, Fee fee){
		for(FeePolicy feePolicy : feePolicies){
//			if(feePolicy.equals(fee.getFeePolicy()) && feePolicy.getEffectiveDefaultFeePct() != null){
//				fee.setFeePct(feePolicy.getEffectiveDefaultFeePct());
//			}
			if(feePolicy.equals(fee.getFeePolicy())){
				fee.setFeePct(feePolicy.getDefaultPct());
			}
		}
	}
	
	public void updatePricing(List<FeePolicy> feePolicies, Structure structure){
		for(FeePolicy feePolicy : feePolicies){
//			if(structure.getPricingIndex() != null && feePolicy.getPricingIndex() != null && structure.getPricingIndex().getKey().equals(feePolicy.getPricingIndex().getKey()) && feePolicy.getEffectiveDefaultFeePct() != null){
//				structure.setIndexValue(feePolicy.getEffectiveDefaultFeePct());
//			}
			if(structure.getPricingIndex() != null && feePolicy.getPricingIndex() != null && structure.getPricingIndex().getKey().equals(feePolicy.getPricingIndex().getKey())){
				structure.setIndexValue(feePolicy.getDefaultPct());
			}
		}
	}
	
	/*public void updateRenewalFee(List<FeePolicy> feePolicies, Structure structure){
		for(FeePolicy feePolicy : feePolicies){
			if(feePolicy.getName().equals("Renewal Fee")){
				structure.setRenewalFee(feePolicy.getEffectiveDefaultFeePct());
			}
			
		}
	}*/
	
	public void updateUnusedLineRate(List<FeePolicy> feePolicies, Structure structure){
		for(FeePolicy feePolicy : feePolicies){
//			if(feePolicy.getName().equals("Unused Line Rate") && structure.getInterestRate() != null && feePolicy.getEffectiveDefaultFeePct() != null){
//				structure.setInterestRate(feePolicy.getEffectiveDefaultFeePct());
//			}
			if(feePolicy.getName().equals("Unused Line Rate") && structure.getInterestRate() != null){
				structure.setInterestRate(feePolicy.getDefaultPct());
			}
		}
	}
}
