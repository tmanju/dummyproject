/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.codify.constants.CodifyConstants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.LookupService;

/**
 * Model class for Document
 * 
 * @author richa.khurana
 * @version 1.0
 * @since 1.0
 */
public class Document extends BaseDataObject{

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	/** Use serialVersionUID for interoperability. */
	private static final long serialVersionUID = 2880314593607352570L;

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Get the AutoDocId from table.
	 */
	public String getAutoDocId() {
		String[] param = new String[3];
		param[0] = "documentCategoryDetail_key";
		param[1] = "documentClassDetail_key";
		param[2] = "documentTypeDetail_key";

		Object[] values = new Object[3];

		if ((getDocumentCategory() != null) && (getDocumentClass() != null)
				&& (getDocumentType() != null)) {
			values[0] = getDocumentCategory().getKey();
			values[1] = getDocumentClass().getKey();
			values[2] = getDocumentType().getKey();

			DocumentCatalogItem documentCatalogItem = (DocumentCatalogItem) (LookupService.getResult("DocumentCatalogItem.getAutoDocId", param,values));

			if (documentCatalogItem != null) {
				return documentCatalogItem.getAutoDocId();
			}
		}

		return "";
	}

	/**
	 * Associated Customer Name with document
	 * 
	 * @return the customerName
	 */
	public String getComments() {

		if ((getCommentGroup() != null)
				&& !(getCommentGroup().getComments().isEmpty())
				&& (getCommentGroup().getComments().size() > 0)) {
			return "Y";
		}

		return "";
	}

	/**
	 * Associated Customer Name with document
	 * 
	 * @return the customerName
	 */
	public String getCustomerName() {
		return "";
	}


	/**
	 * Associated Request Number with document
	 * 
	 * @return the requestNumber
	 */
	public String getRequestNumber() {
		return "";
	}

	public String getRelatedTo() {

		StringBuilder sb = new StringBuilder();
		if (getDocumentRelatedTo()!=null && getDocumentRelatedTo().getKey() != null) {
			if(Constants.DOCUMENT_RELATED_TO_PARTY_KEY.equals(getDocumentRelatedTo().getKey())){
				if (getAssociatedCustomers() != null) {
					for(FacilityCustomerRole customer : getAssociatedCustomers()){
						sb.append(customer.getCustomer());
						sb.append(",");
					}
					if(sb.length() > 1){
						sb.delete(sb.length()-1, sb.length());
					}
				}
			}
		}


		return sb.toString();

	}

	public String getIconName() {

		String icon = null;
		if (getUploadedStreamSize() != null) {

			if (StringUtils.isNotEmpty(getContentType())) {
				icon = CodifyConstants.CODIFY_APP_CONTENT_TYPE_ICONS_CONFIG
						.getString(
								"content_type." + getContentType() + ".icon",
								null);
			}

			if (icon == null && StringUtils.isNotEmpty(getDocumentName())) {
				String fileName = getDocumentName();
				String ext = FilenameUtils.getExtension(fileName);
				if (ext != null) {
					ext = ext.toLowerCase();
				}
				icon = CodifyConstants.CODIFY_APP_CONTENT_TYPE_ICONS_CONFIG
						.getString("fileExt." + ext + ".icon", null);
			}

			if (icon == null) {
				icon = CodifyConstants.CODIFY_APP_CONTENT_TYPE_ICONS_CONFIG
						.getString("default.icon", null);
			}
		}

		return icon;
	}

	public boolean isDocumentClass(String... keys) {
		if ((getDocumentClass() != null)
				&& ArrayUtils.contains(keys, getDocumentClass().getKey())) {
			return true;
		}
			return false;
	}

	public boolean isDocumentCategory(String... keys) {
		if ((getDocumentCategory() != null)
				&& ArrayUtils.contains(keys, getDocumentCategory().getKey())) {
			return true;
		}
			return false;
	}

	public boolean isDocumentType(String... keys) {
		if ((getDocumentType() != null)
				&& ArrayUtils.contains(keys, getDocumentType().getKey())) {
			return true;
		}
			return false;
	}

	public Document clone(Document clone) {
		if (clone == null) {
			throw new IllegalStateException("Document to clone cannot be null");
		}
		clone.setDocumentName(getDocumentName());
		clone.setDocumentClass(getDocumentClass());
		clone.setDocumentCategory(getDocumentCategory());
		clone.setDocumentType(getDocumentType());

		clone.setDocumentStatus(getDocumentStatus());
		clone.setReadOnly(getReadOnly());
		clone.setEsignatureRqd(getEsignatureRqd());
		clone.setPrintJobDttm(getPrintJobDttm());

		clone.setInitializeDttm(getInitializeDttm());
		clone.setWetSignature(getWetSignature());
		clone.setUploadedStream(getUploadedStream());
		clone.setArchiveStream(getArchiveStream());

		clone.setShared(getShared());
		clone.setDescription(getDescription());
		clone.setDocumentRepoId(getDocumentRepoId());
		clone.setUploadedBy(getUploadedBy());

		clone.setFileSize(getFileSize());
		clone.setVersionNumber(getVersionNumber());
		//clone.setNumIndex(getNumIndex());

		clone.setRequiredFlag(getRequiredFlag());
		clone.setUploadedFlag(getUploadedFlag());
		clone.setGeneratedFlag(getGeneratedFlag());
		clone.setReceivedDttm(getReceivedDttm());

		clone.setUploadDttm(getUploadDttm());
		clone.setUploadedStreamBy(getUploadedStreamBy());
		clone.setTemplateName(getTemplateName());

		clone.setLookupId(getLookupId());
		clone.setGeneratedDocumentGroup(getGeneratedDocumentGroup());
		clone.setUploadedDocumentGroup(getUploadedDocumentGroup());
		clone.setDocumentGroup(getDocumentGroup());

		clone.setRequestLevel(getRequestLevel());
		clone.setAssociatedRequest(getAssociatedRequest());

		clone.setAssociatedCustomers(getAssociatedCustomers());
		
		List<Partner> partnerList = getAssociatedPartners();

		if (partnerList != null && !partnerList.isEmpty()) {
			List<Partner> newPartnerList = new ArrayList<Partner>();
			for (Partner partner : partnerList) {
					newPartnerList.add(partner);
			}
			clone.setAssociatedPartners(newPartnerList);
		}
		
		
		
		
		clone.setWfStatus(getWfStatus());

		clone.setSigningMetadata(getSigningMetadata());

		CommentGroup commentGroup = getCommentGroup();
		CommentGroup newCommentGroup = new CommentGroup();

		if(commentGroup!=null && commentGroup.getCommentGroupName()!=null){
			newCommentGroup.setCommentGroupName(commentGroup.getCommentGroupName());
			newCommentGroup.setEntityId(commentGroup.getEntityId());
			List<Comment> commentList = commentGroup.getComments();
			if (commentList != null && !commentList.isEmpty()) {
				List<Comment> newCommentList = new ArrayList<Comment>();
				for (Comment comment : commentList) {
					Comment newComment = new Comment();
					newComment.setCommentText(comment.getCommentText());
					newComment.setIndexNumber(comment.getIndexNumber());
					newComment.setEntityId(comment.getEntityId());
					newComment.setCommentType(comment.getCommentType());
					newComment.setCommentScope(comment.getCommentScope());
					newCommentList.add(newComment);
				}
	
				newCommentGroup.setComments(newCommentList);
			}
		}	
			clone.setCommentGroup(newCommentGroup);

		List<DocumentSigner> documentSignerList = getDocumentSigners();

		if (documentSignerList != null && !documentSignerList.isEmpty()) {
			List<DocumentSigner> newDocumentSignerList = new ArrayList<DocumentSigner>();
			for (DocumentSigner documentSigner : documentSignerList) {
				DocumentSigner newDocumentSigner = new DocumentSigner();
				newDocumentSigner.setRequest(documentSigner.getRequest());
				newDocumentSigner.setDocument(documentSigner.getDocument());
				newDocumentSigner.setSignerType(documentSigner.getSignerType());
				newDocumentSigner.setSignerId(documentSigner.getSignerId());
				newDocumentSigner.setName(documentSigner.getName());
				newDocumentSigner.setSignerRefId(documentSigner
						.getSignerRefId());
				newDocumentSigner.setDocSignedFlag(documentSigner
						.getDocSignedFlag());
				newDocumentSigner.setSignedDttm(documentSigner.getSignedDttm());
				newDocumentSigner.setName(documentSigner.getName());
				newDocumentSignerList.add(newDocumentSigner);

			}
			clone.setDocumentSigners(newDocumentSignerList);
		}

		List<PageAsImage> pageAsImageList = getDocumentPagesAsImages();

		if (pageAsImageList != null && !pageAsImageList.isEmpty()) {
			List<PageAsImage> newPageAsImageList = new ArrayList<PageAsImage>();
			for (PageAsImage pageAsImage : newPageAsImageList) {
				PageAsImage newPageAsImage = new PageAsImage();
				newPageAsImage.setDocument(pageAsImage.getDocument());
				newPageAsImage.setPage(pageAsImage.getPage());
				newPageAsImage.setImage(pageAsImage.getImage());
				newPageAsImage.setImageWidth(pageAsImage.getImageWidth());
				newPageAsImage.setImageHeight(pageAsImage.getImageHeight());
				newPageAsImageList.add(newPageAsImage);
			}
			clone.setDocumentPagesAsImages(newPageAsImageList);
		}

		List<DocumentPublishHistory> documentPublishHistoryList = getPublishedTo();

		if (documentPublishHistoryList != null
				&& !documentPublishHistoryList.isEmpty()) {
			List<DocumentPublishHistory> newDocumentPublishHistoryList = new ArrayList<DocumentPublishHistory>();
			for (DocumentPublishHistory documentPublishHistory : documentPublishHistoryList) {
				DocumentPublishHistory newDocumentPublishHistory = new DocumentPublishHistory();
				newDocumentPublishHistory.setComments(documentPublishHistory
						.getComments());
				newDocumentPublishHistory.setContact(documentPublishHistory
						.getContact());
				newDocumentPublishHistory
						.setCommentGroup(documentPublishHistory
								.getCommentGroup());
				newDocumentPublishHistoryList.add(newDocumentPublishHistory);
			}
			clone.setPublishedTo(newDocumentPublishHistoryList);
		}

		return clone;
	}

	public Document clone() {
		return clone(new Document());

	}
	
	public List<FacilityCustomerRole> getAllRoles(){
		List<FacilityCustomerRole> list = new ArrayList<FacilityCustomerRole>();
		Facility facility = (Facility)ContextHolder.getContext().getNamedContext().get("root_allFacilities");
		if(facility != null){
			list.addAll(facility.getFacilityCustomerRoles());
		}
		return list;
	}
	
	// Method will be used to add multiple row in generated document
	public void addMultipleEntryWithCustomer(Document document) {
		Request request = (Request) ContextHolder.getContext().getNamedContext().get("root");
		request.getDocumentGroup().getGeneratedDocuments().add(document);
	}
	
	// Method will be used to add multiple row in generated document
	public void addMultipleEntryWithFacility(Document document) {
		Request request = (Request) ContextHolder.getContext().getNamedContext().get("root");
		request.getDocumentGroup().getGeneratedDocuments().add(document);
	}

}