package com.thirdpillar.codify.loanpath.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.DebtorCustomer;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;

@Component
public class DebtorsUpdatorBizOp extends AbstractBusinessOperation{

	private DebtorsHandlerBizOp debtorsHandlerBizOp;
	
	public Object execute(String operation, Object... params) {
		EntityService es = new EntityService();
		List<DebtorCustomer> debtorCustomers = (List<DebtorCustomer>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.DebtorCustomer.bySyncedToAkritiv", new String[]{"syncedToAkritiv"}, new Object[]{false});
		if(debtorCustomers != null && !debtorCustomers.isEmpty()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("selectedEntities", debtorCustomers);
			debtorsHandlerBizOp.execute(debtorCustomers.get(0).getFacility(), operation, map);
		}
		return null;
	}

	
	public DebtorsHandlerBizOp getDebtorsHandlerBizOp() {
		return debtorsHandlerBizOp;
	}

	@Autowired
	public void setDebtorsHandlerBizOp(DebtorsHandlerBizOp debtorsHandlerBizOp) {
		this.debtorsHandlerBizOp = debtorsHandlerBizOp;
	}
}
