/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.LookupService;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class PromotionPolicy extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -6604576372288529264L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getDescriptionDer() {
        return getDescription();
    }

    public String getNameDer() {
        return getName();
    }

    public String getOuDer() {
        return getOu().getValue();
    }

    public String getSbuDer() {
        return getSbu().getValue();
    }
}
