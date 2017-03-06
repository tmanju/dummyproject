



package com.thirdpillar.codify.loanpath.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;


public  class DocumentGroup {
	
	public Document getUploadDocument(String documentClassKey, String documentCategoryKey, String documentTypeKey) {
		return getDocuments(getUploadedDocuments(),documentClassKey,documentCategoryKey,documentTypeKey);
	}
	
	public Document getGeneratedDocument(String documentClassKey, String documentCategoryKey, String documentTypeKey) {
		return getDocuments(getGeneratedDocuments(),documentClassKey,documentCategoryKey,documentTypeKey);
	}
	
	public Document getDocuments(List<Document> documents, String documentClassKey, String documentCategoryKey, String documentTypeKey) {
		if (documents != null) {
			for (Document document : documents) {
				if (document.getDocumentClass() == null
						|| document.getDocumentCategory() == null
						|| document.getDocumentType() == null) {
					// no check needed
					continue;
				}
				
				if (StringUtils.equals(document.getDocumentClass().getKey(), documentClassKey)
						&& StringUtils.equals(document.getDocumentCategory().getKey(), documentCategoryKey)
						&& StringUtils.equals(document.getDocumentType().getKey(), documentTypeKey)) {
					return document;
				}
			}
			
		}
		
		return null;
		
	}
	
	
	public Document getUploadDocument(Document documentCheck) {
		if (documentCheck != null) {
			for (Document document : getUploadedDocuments()) {
				String documentKeyExpression = getDocumentTypeKeyExpression(document)
						+ getDocumentClassKeyExpression(document)
						+ getDocumentCategoryKeyExpression(document);
				String documentCheckKeyExpression = getDocumentTypeKeyExpression(documentCheck)
						+ getDocumentClassKeyExpression(documentCheck)
						+ getDocumentCategoryKeyExpression(documentCheck);

				if (StringUtils.equals(documentKeyExpression,
						documentCheckKeyExpression)) {
					return document;
				}
			}

		}

		return null;
	}

	private String getDocumentTypeKeyExpression(Document document) {
		return document.getDocumentType() == null ? "" : document
				.getDocumentType().getKey();
	}

	private String getDocumentClassKeyExpression(Document document) {
		return document.getDocumentClass() == null ? "" : document
				.getDocumentClass().getKey();
	}

	private String getDocumentCategoryKeyExpression(Document document) {
		return document.getDocumentCategory() == null ? "" : document
				.getDocumentCategory().getKey();
	}
	

}