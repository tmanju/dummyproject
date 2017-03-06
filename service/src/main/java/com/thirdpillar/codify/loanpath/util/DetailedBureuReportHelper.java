package com.thirdpillar.codify.loanpath.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.model.BureauReport;
import com.thirdpillar.codify.loanpath.model.Customer;

public class DetailedBureuReportHelper {

	private static final Log LOG = LogFactory
			.getLog(DetailedBureuReportHelper.class);

	private static byte[] getBinary(String reportHtml, String reportRefId) {
		if (reportHtml == null) {
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		} else {
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				String fileName = "ConvertedHtml_" + reportHtml.length() + "_"
						+ reportRefId + ".html";
				File f = new File(fileName);
				if (!f.exists()) {
					f.createNewFile();
				}
				fw = new FileWriter(f);
				bw = new BufferedWriter(fw);
				bw.write(reportHtml);
				bw.flush();
				fw.flush();
				bw.close();
				fw.close();
				// reading the binary

				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[8192];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				int bytesRead;
				bytesRead = fis.read(buffer);
				while (bytesRead != -1) {
					baos.write(buffer, 0, bytesRead);
				}
				return baos.toByteArray();
			} catch (Exception e) {
				LOG.error("Exception occured at convereted HTML", e);
				return ArrayUtils.EMPTY_BYTE_ARRAY;
			} finally {
				try {
					bw.close();
					fw.close();
				} catch (Exception e) {
					// nothing can be done
					LOG.error("Exception occured", e);
				}
			}
		}
	}

	private static String formatHtml(String html) {
		if (html == null) {
			return null;
		} else {
			html = html.replaceAll("&amp;", "&");
			html = html.replaceAll("&lt;", "<");
			html = html.replaceAll("&gt;", ">");
			html = html.replaceAll("><", ">\n<");
			return html;
		}
	}
}
