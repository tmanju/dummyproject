



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;


@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byProductType",
	            keys = { "productType.key" }
	        ) }
	)
public  class CustomerExposureLimit {

}