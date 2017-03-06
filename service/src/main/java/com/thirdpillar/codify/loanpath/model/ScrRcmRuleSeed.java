package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

/**
 * DOCUMENT ME!
 * 
 * @author Sajan Monga
 * @version 1.0
 * @since 1.0
 */

public class ScrRcmRuleSeed extends BaseDataObject {
	
	private static final Logger logger = Logger.getLogger(ScrRcmRuleSeed.class);
	
	public ScrRuleParam getFirstScrRuleParam() {
		if (this.getScrRuleParams() != null
				&& this.getScrRuleParams().size() == 1) {
			return (ScrRuleParam) this.getScrRuleParams().get(0);
		}
		return null;

	}

	public boolean hasParameterValue() {
		for (ScrRuleParam scrRuleParam : getScrRuleParams()) {
			if (scrRuleParam.getParamValue() != null) {
				return true;
			}
				return false;
		}
		return true;
	}


	public List<RecomReasonSeed> getRecommReasonCodes() {
		List<RecomReasonSeed> lst = new ArrayList<RecomReasonSeed>();

		List<BaseDataObject> recomReasonList = (List<BaseDataObject>) LookupService
				.getResults("RecomReasonSeed.fetchAllRecomReason",
						new String[] {}, new Object[] {});
		logger.info("###############&&&&&&&&&&&&&&&&&&&&--- size "
				+ recomReasonList.size());
		for (BaseDataObject baseDataObject : recomReasonList) {
			lst.add((RecomReasonSeed) baseDataObject);
		}
		logger.info("###############&&&&&&&&&&&&&&&&&&&&--- size "
				+ lst.size() + "     " + lst.get(0).getRecomReason());

		return lst;
	}
	/**
	 * Method to get list of Fimp associated with partnersite
	 * 
	 * @return list of Fimp
	 */
	
}
