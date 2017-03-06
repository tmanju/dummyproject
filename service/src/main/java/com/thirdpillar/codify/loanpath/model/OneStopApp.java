package com.thirdpillar.codify.loanpath.model;



import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.foundation.model.BaseDataObject;

public class OneStopApp extends BaseDataObject{

	public List<Collateral> getAllCustomerCollateral(){
		List<Collateral> collateralList = new ArrayList<Collateral>();
		if(getNonIndividualCustomer() != null){
			for(Collateral selectedCollateral : getNonIndividualCustomer().getCollaterals()){
				collateralList.add(selectedCollateral);
			}
		}
		return collateralList;
	}
}
