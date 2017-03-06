/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import com.thirdpillar.codify.loanpath.model.helper.ComplianceHelper;

import com.thirdpillar.foundation.model.Auto;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.Valid;
import javax.validation.constraints.Size;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class RacEvaluation extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 5418621932978769945L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    /**
     * Return Category
     */
    public String getCategory() {
        return "";
    }

    
}
