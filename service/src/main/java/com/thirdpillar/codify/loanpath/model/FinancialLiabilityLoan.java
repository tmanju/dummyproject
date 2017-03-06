package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
//	        @XStreamLookupCollectionByOGNL(
//	            name = "byExternalIdentifier",
//	            keys = { "externalIdentifier"}
//	        ),
	        @XStreamLookupCollectionByOGNL(
		        	name = "byLoanLender",
		        	keys = { "loanLender"}
		        )
	    }
	)
public class FinancialLiabilityLoan {

}
