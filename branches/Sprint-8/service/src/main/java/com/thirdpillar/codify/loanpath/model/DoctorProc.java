



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byExternalRefId",
	            keys = { "externalRefId" }
	        ) }
	)
public  class DoctorProc {

}