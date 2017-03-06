/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.Date;
import java.util.List;

import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Document;
import com.thirdpillar.codify.loanpath.model.DocumentGroup;
import com.thirdpillar.codify.loanpath.model.User;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;

import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

import org.springframework.beans.factory.annotation.Configurable;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

/**
 * DOCUMENT ME!
 * 
 * @author ENTER YOUR FULL NAME
 * @version 1.0
 * @since 1.0
 */
@Component
@Configurable
public class TaskHandlerService {

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	@Transactional
	public Document getDocument(String documentClassKey,
			String documentCategoryKey, String documentTypeKey) {
		AttributeChoice docClass = (AttributeChoice) com.thirdpillar.foundation.service.LookupService
				.getResult("AttributeChoice.byKey", "key", documentClassKey);
		AttributeChoice docCategory = (AttributeChoice) com.thirdpillar.foundation.service.LookupService
				.getResult("AttributeChoice.byKey", "key", documentCategoryKey);
		AttributeChoice docType = (AttributeChoice) com.thirdpillar.foundation.service.LookupService
				.getResult("AttributeChoice.byKey", "key", documentTypeKey);

		Document doc = new Document();
		doc.setDocumentClass(docClass);
		doc.setDocumentType(docType);
		doc.setDocumentCategory(docCategory);

		return doc;
	}

}
