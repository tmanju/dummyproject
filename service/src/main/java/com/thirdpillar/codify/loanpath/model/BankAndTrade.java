package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	    	@XStreamLookupCollectionByOGNL(
	                name = "byServicingIdentifier",
	                keys = { "servicingIdentifier" }
	            )
	    }
	)
public class BankAndTrade {

}
