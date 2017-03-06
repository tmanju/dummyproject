package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.foundation.model.BaseDataObject;

/**
 * DOCUMENT ME!
 * 
 * @author Sajan Monga
 * @version 1.0
 * @since 1.0
 */

public class ScrRskMod extends BaseDataObject {

	public List<BureauReport> getBureauDer() {
		List<BureauReport> lstBureau = new ArrayList<BureauReport>();
		lstBureau.add(getBureauReport());
		return lstBureau;
	}
}
