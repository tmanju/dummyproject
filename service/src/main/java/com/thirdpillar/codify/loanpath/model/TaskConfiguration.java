



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.model.helper.TaskConfigurationHelper;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.DocumentId;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.validation.Unique;
import org.hibernate.search.annotations.Store;
import com.thirdpillar.foundation.model.ITaskConfiguration;
import com.thirdpillar.foundation.model.Metafied;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CascadeType;
import javax.persistence.Version;
import org.hibernate.annotations.OptimisticLock;
import javax.validation.constraints.Size;
import org.hibernate.annotations.NamedQuery;
import javax.persistence.FetchType;
import org.hibernate.annotations.Cache;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;
import org.hibernate.annotations.Index;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import org.hibernate.envers.Audited;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import javax.persistence.Id;
import org.hibernate.annotations.NamedQueries;
import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import javax.persistence.Entity;


public  class TaskConfiguration extends BaseDataObject{

}