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

import org.mvel2.sh.command.basic.Exit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class RacsetEvaluation extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 5418621932978769945L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getExceptionAuthorityValue() {
        String str = null;
        List<String> ls = new ArrayList<String>();

        for (RacEvaluation rc : getRacEvaluations()) {

            if (rc.getExceptionAuthority() != null) {
                ls.add(rc.getExceptionAuthority().getValue());
            }
        }

        if (!ls.isEmpty()) {
            Collections.sort(ls);
            str = ls.get(ls.size() - 1);
        }

        return str;
        
    }

    /**
     * Return Fail
     */
    public Integer getFail() {
        int failCount = 0;

        for (RacEvaluation rc : getRacEvaluations()) {

            if ((rc.getStatus() != null) && "RAC_STATUS_FAIL".equalsIgnoreCase(rc.getStatus().getKey())) {
                failCount++;
            }
        }

        return failCount;
    }

    /**
     * Return NA
     */
    public Integer getNa() {
    	int naCount = 0;

        for (RacEvaluation rc : getRacEvaluations()) {

            if ((rc.getStatus() != null) && "RAC_STATUS_N/A".equalsIgnoreCase(rc.getStatus().getKey())) {
                naCount++;
            }
        }

        return naCount;
    }

    /**
     * Return Pass
     */
    public Integer getPass() {
        int passCount = 0;

        for (RacEvaluation rc : getRacEvaluations()) {

            if ((rc.getStatus() != null) && "RAC_STATUS_PASS".equalsIgnoreCase(rc.getStatus().getKey())) {
                passCount++;
            }
        }

        return passCount;
    }

    public Integer getRacNumber() {
        return getRacEvaluations().size();
    }

    public String getStatusValue() {
        boolean flag = false;

        for (RacEvaluation rc : getRacEvaluations()) {

            if ( (rc.getStatus() != null) &&
                    ("RAC_STATUS_PASS".equalsIgnoreCase(rc.getStatus().getKey()) ||
                        "RAC_STATUS_FAIL".equalsIgnoreCase(rc.getStatus().getKey()) ||
                        "RAC_STATUS_N/A".equalsIgnoreCase(rc.getStatus().getKey()))) {
                flag = true;
            } else {
                flag = false;

                break;
            }
        }

        if (flag) {
            return "COMPLETE";
        } else {
            return "INCOMPLETE";
        }
      
    }
}
