/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.util;

import com.thirdpillar.codify.loanpath.model.Document;
import com.thirdpillar.foundation.codify.constants.CodifyConstants;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class PdfUtils {

	private static final Logger LOG = Logger.getLogger(PdfUtils.class);
	
    //~ Instance fields ------------------------------------------------------------------------------------------------

    boolean flatten;
    boolean archive;
    boolean watermark;

    //~ Constructors ---------------------------------------------------------------------------------------------------

    public PdfUtils() {
        this.flatten = true;
        this.archive = true;
        this.watermark = true;
    }

    public PdfUtils(boolean flatten, boolean archive, boolean watermark) {
        this.flatten = flatten;
        this.archive = archive;
        this.watermark = watermark;
    }

    //~ Methods --------------------------------------------------------------------------------------------------------

    public void archive(Document doc) {
        if (archive) {
            // Archive: UploadedStream->ArchiveStream
            doc.setArchiveStream(doc.getUploadedStream());
        }
    }
    
    public void watermark(Document doc) throws Exception {

        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;

        if (watermark) {
            try {

                // Flatten, Watermark: ArchiveStream->UploadedStream
                is = new ByteArrayInputStream(doc.getArchiveStream());
                os = new ByteArrayOutputStream(doc.getUploadedStream().length); // new Byte[]
                doWaterMark(is, os);
                doc.setUploadedStream(os.toByteArray()); // new Byte[]
            } catch (Exception e) {
            	LOG.error("Exception occured while setting watermark.",e);
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
        }
    }


    public void doWaterMark(InputStream is, OutputStream os) throws Exception {
        String passwd = CodifyConstants.CONFIG.getString("document.pdf.encryption.ownerPassword");
        PdfReader reader = new PdfReader(is, passwd.getBytes());

        try {
            int n = reader.getNumberOfPages();

            // Create a stamper that will copy the document to a new file
            PdfStamper stamper = new PdfStamper(reader, os);

            if (flatten) {
                stamper.setFormFlattening(true);
            }

            PdfContentByte underContent;
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);

            for (int i = 1; i <= n; i++) {

                // Watermark under the existing page
                Rectangle pageSize = reader.getPageSizeWithRotation(i);
                underContent = stamper.getUnderContent(i);
                underContent.beginText();
                underContent.setFontAndSize(baseFont, 60);
                underContent.setRGBColorFill(100, 100, 100);

                float textAngle = 45f;
                String watermarkText = CodifyConstants.CONFIG.getString("document.pdf.watermark.text");
                underContent.showTextAligned(PdfContentByte.ALIGN_CENTER, watermarkText, pageSize.getWidth() / 2,
                    pageSize.getHeight() / 2, textAngle);
                underContent.endText();
            }

            stamper.close();
        } catch (Exception de) {
        	LOG.error("Error occured at execute",de);
            throw de;
        }
    }
}

