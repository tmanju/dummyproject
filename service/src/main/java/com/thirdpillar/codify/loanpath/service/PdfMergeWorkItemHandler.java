/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import com.thirdpillar.codify.loanpath.model.*;
import com.thirdpillar.foundation.auth.Authorization;
import com.thirdpillar.foundation.model.DocumentAware;
import com.thirdpillar.foundation.model.IDocument;
import com.thirdpillar.foundation.model.IDocumentGroup;
import com.thirdpillar.foundation.model.RevInfo;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.util.pdf.PDFTemplate;
import com.thirdpillar.foundation.util.pdf.PDFWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.process.instance.WorkItemHandler;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PdfMergeWorkItemHandler implements WorkItemHandler, ApplicationContextAware {

    private ApplicationContext applicationContext;
    protected final Log log = LogFactory.getLog(getClass());

	//~ Methods --------------------------------------------------------------------------------------------------------

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        manager.abortWorkItem(workItem.getId());
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        // results map
        HashMap<String, Object> results = new HashMap<String, Object>();

        // parameters
        DocumentAware entity = (DocumentAware) workItem.getParameter("entity");
        Document doc = (Document) workItem.getParameter("document");
        String docTemplate = null;

		if(doc != null) {
			// Document was passed in
        	docTemplate = doc.getTemplateName();
		} else {
			// class, cat, type were passed in. Must create document.
            docTemplate = (String) workItem.getParameter("docTemplate");
            String docClass = (String) workItem.getParameter("docClass");
            String docCategory = (String) workItem.getParameter("docCategory");
            String docType = (String) workItem.getParameter("docType");
    		doc = getDocument(docClass, docCategory, docType, docTemplate);	

            IDocumentGroup dg = ((DocumentAware) entity).getDocumentGroup();
            
    		if (dg == null) {
    		  dg = new DocumentGroup();
    		  entity.setDocumentGroup((IDocumentGroup)dg);
    		}    		
    		dg.addToGeneratedDocuments((IDocument) doc);
    		doc.setGeneratedDocumentGroup((DocumentGroup)dg);    	
		}

       	mergeDocument(doc, workItem.getParameters());
        
        results.put("entity", entity);

        // complete the work item
        manager.completeWorkItem(workItem.getId(), results);
    }
    
    public void mergeDocument(Document doc, Map<String,Object> namedContext) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
	    	PDFTemplate pdfTemplate = (PDFTemplate) applicationContext.getBean(doc.getTemplateName());
	    	
	    	PDFWriter writer = new PDFWriter(os, pdfTemplate);
	    	writer.generatePDF(namedContext);			
			doc.setUploadedStream(os.toByteArray());
			doc.setGeneratedFlag(true);

		} catch (Exception e) {
			log.error("Error occured while merging document.",e);
		} finally {
			IOUtils.closeQuietly(os);
		}	
    	
    }
    
    public Document getDocument(String docClassKey, String docCategoryKey, String docTypeKey, String docTemplate) {
		AttributeChoice docClass = (AttributeChoice)com.thirdpillar.foundation.service.LookupService.getResult("AttributeChoice.byKey", "key", docClassKey);
		AttributeChoice docCategory = (AttributeChoice)com.thirdpillar.foundation.service.LookupService.getResult("AttributeChoice.byKey", "key", docCategoryKey);
		AttributeChoice docType = (AttributeChoice)com.thirdpillar.foundation.service.LookupService.getResult("AttributeChoice.byKey", "key", docTypeKey);
        AttributeChoice docStatus = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key",
                "DOCUMENT_STATUS_PENDING_REVIEW");

		Document doc = new Document();
		doc.setDocumentClass(docClass);
		doc.setDocumentCategory(docCategory);
		doc.setDocumentType(docType);
		doc.setDocumentName((docType.getValue()) + ".pdf");
        doc.setGeneratedFlag(false);
        doc.setDocumentStatus(docStatus);
        doc.setTemplateName(docTemplate);
        return doc;
    }

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
    
    
}
