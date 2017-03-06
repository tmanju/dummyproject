/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class Compliance extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 5418621932978769945L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getDbaName() {
        PartyDba partyDba = null;

        if ((getPartyDetail().getDbas() != null) && (getPartyDetail().getDbas().size() > 0)) {
            partyDba = getPartyDetail().getDbas().get(0);
        }

        return (partyDba != null) ? partyDba.getDbaName() : "";
    }

    /*
     * return Tax ID Number of the party
     */
    public String getTaxId() {
        String taxIdNumber = getPartyDetail().getTaxIDNumber();

        return (taxIdNumber != null) ? taxIdNumber : "";
    }
    
    
}
