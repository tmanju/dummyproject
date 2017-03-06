



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;
import com.thirdpillar.foundation.validation.Unique;
import com.thirdpillar.foundation.model.Metafied;
import org.hibernate.annotations.Cache;
import javax.persistence.Table;
import com.thirdpillar.codify.loanpath.model.helper.NotificationConfigurationHelper;
import javax.persistence.Column;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.search.Filter;
import javax.persistence.Embedded;
import org.hibernate.envers.Audited;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Id;
import javax.persistence.Version;
import org.hibernate.annotations.OptimisticLock;
import javax.validation.constraints.Size;
import javax.persistence.Entity;


public  class NotificationConfiguration extends BaseDataObject {

}