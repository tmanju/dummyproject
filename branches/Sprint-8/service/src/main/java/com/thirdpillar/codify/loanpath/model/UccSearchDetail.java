/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class UccSearchDetail extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 6593671576923819043L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getContentType() {
        Document imageDocument = this.getImageDoc();

        if (imageDocument != null) {
            return imageDocument.getContentType();
        }

        return null;
    }

    public String getDocumentName() {
        Document imageDocument = this.getImageDoc();

        if (imageDocument != null) {
            return imageDocument.getDocumentName();
        }

        return null;
    }

    public Document getImageDoc() {
        String orderNum = this.getOrderNum();

        if (orderNum != null) {
            Request request = this.getUccSrchSummary().getUccSrch().getRequest();
            List<Document> uploadedDocuments = request.getDocumentGroup().getUploadedDocuments();

            if ((uploadedDocuments != null) && (!uploadedDocuments.isEmpty())) {

                for (Document uploadedDocument : uploadedDocuments) {

                    // check for the document type to make sure it is ucc document
                    // DOCUMENT_TYPE_UCC_LIEN_SEARCH,DOCUMENT_CATEGORY_UCC,DOCUMENT_CLASS_UPLOADED_DOC
                    if ((uploadedDocument.getDocumentType() != null) &&
                            "DOCUMENT_TYPE_UCC_LIEN_SEARCH".equals(uploadedDocument.getDocumentType().getKey()) &&
                            orderNum.equals(uploadedDocument.getLookupId())) {
                        return uploadedDocument;
                    }
                }
            }
        }

        return null;
    }

    public boolean isImageOnline() {

        if ((this.getImageKey()!=null) && (!("".equals(this.getImageKey())))) {
            return true;
        }

        return false;
    }

    public String getOrderNum() {
        String orderItemNum = this.getOrderItemNum();

        if (orderItemNum != null) {
            String recordNum = this.getUccSrchSummary().getUccSrch().getRecordNo();

            return recordNum + orderItemNum;
        }

        return null;
    }

    public List<UccSearchDetail> getUccSearchDtlsByOrderNum() {
        String orderItemNum = this.getOrderItemNum();
        List<UccSearchDetail> uccSearchDtls = new ArrayList<UccSearchDetail>();

        if (orderItemNum != null) {
            UccSrch uccSrch = this.getUccSrchSummary().getUccSrch();

            for (UccSearchSummary uccSearchSummary : uccSrch.getResults()) {

                for (UccSearchDetail uccSearchDetail : uccSearchSummary.getDetails()) {

                    if ((uccSearchDetail.getOrderItemNum() != null) &&
                            orderItemNum.equals(uccSearchDetail.getOrderItemNum())) {
                        uccSearchDtls.add(uccSearchDetail);
                    }
                }
            }
        }

        return uccSearchDtls;
    }

    public byte[] getUploadedStream() {
        Document imageDocument = this.getImageDoc();

        if (imageDocument != null) {
            return imageDocument.getUploadedStream();
        }

        return ArrayUtils.EMPTY_BYTE_ARRAY ;
    }
}
