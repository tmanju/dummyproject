package com.thirdpillar.codify.loanpath.service;

import java.util.Date;

import com.thirdpillar.codify.loanpath.model.Document;
import com.thirdpillar.codify.loanpath.model.User;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;

public class DocumentCheckoutBizOp extends AbstractBusinessOperation {
	
	@Override
	public Object execute(BaseDataObject entity, String operation,
			Object... params) {
		
		Document doc = (Document) entity;
		doc.getCheckout().setCheckedOutByUser((User)ContextHolder.getContext().getUser());
		doc.getCheckout().setCheckedOutAt(new Date());
		return null;
	}

}
