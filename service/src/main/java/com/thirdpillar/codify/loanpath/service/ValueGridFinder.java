/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import com.thirdpillar.codify.loanpath.model.ValueGrid;

import com.thirdpillar.foundation.fe.GridFinder;
import com.thirdpillar.foundation.model.Grid;
import com.thirdpillar.foundation.service.EntityService;

import com.ibm.icu.util.Calendar;

import java.util.Date;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class ValueGridFinder implements GridFinder {

    //~ Methods --------------------------------------------------------------------------------------------------------

    public Grid findGrid(String gridName, Date date) {
        EntityService es = new EntityService();
        String[] queryParams = new String[] { "name", "currentDate" };
        Object[] queryParamValues = new Object[] { gridName, date };
        List<ValueGrid> valueGrids = es.findByNamedQueryAndNamedParam(
                "com.thirdpillar.codify.loanpath.model.ValueGrid.byNameAndEffectiveDate", queryParams,
                queryParamValues);

        if ((valueGrids != null) && (!valueGrids.isEmpty())) {
            return (valueGrids.get(0)).getGrid();
        }

        return null;
    }

    public Grid findGridByName(String name) {
        Date todaysDate = Calendar.getInstance().getTime();

        return findGrid(name, todaysDate);
    }

    public Grid findGridByName(String name, Date date) {

        if (date == null) {

            // user current date
            date = new Date();
        }

        return findGrid(name, date);
    }
}
