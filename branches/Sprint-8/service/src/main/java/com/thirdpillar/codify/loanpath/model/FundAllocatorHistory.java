package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.CapitalProvider;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component
@Configurable
public class FundAllocatorHistory extends BaseDataObject {

	public String getDbid() {
		if (this.getFundAllocator() != null) {
			return this.getFundAllocator().getId() + "";
		} else {
			return "";
		}
	}
}
