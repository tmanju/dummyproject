



package com.thirdpillar.codify.loanpath.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Indexed;
import com.thirdpillar.foundation.model.BaseDataObject;
import org.hibernate.search.annotations.Store;
import com.thirdpillar.foundation.validation.Unique;
import com.thirdpillar.foundation.model.LobContent;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import com.thirdpillar.codify.loanpath.model.helper.PageAsImageHelper;
import javax.persistence.Version;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import javax.persistence.FetchType;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;
import org.hibernate.annotations.Index;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import org.hibernate.envers.Audited;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.persistence.Id;
import org.hibernate.annotations.Cascade;
import javax.persistence.Entity;


public  class PageAsImage {
	
	private static final long serialVersionUID = -4660397505071982358L;

}