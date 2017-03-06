package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

public class ScrFactorSeed extends BaseDataObject {

	public String getDerCodMsg() {
		if (null != getRecommendationReason()) {
			return getCode() + " - "
					+ getRecommendationReason().getRecomReason();
		} else {
			return "";
		}
	}

	public boolean isValidRecomReason() {
		if ("NO ADVERSE ACTION".equals(getMessage())) {
			return true;
		}
		if (getRecommendationReason() == null) {
			return false;
		}
		return true;
	}

}
