



package com.thirdpillar.codify.loanpath.model;

import org.hibernate.search.annotations.Field;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.validation.Unique;
import org.hibernate.search.annotations.Store;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;
import com.thirdpillar.codify.loanpath.model.helper.LicenseInformationHelper;
import javax.validation.constraints.Size;
import javax.persistence.NamedQuery;
import javax.persistence.FetchType;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;
import org.hibernate.annotations.Index;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;
import org.hibernate.envers.Audited;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import javax.persistence.Entity;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	            name = "byExternalRefId",
	            keys = { "externalRefId" }
	        )
	    }
	)
public  class LicenseInformation {

}