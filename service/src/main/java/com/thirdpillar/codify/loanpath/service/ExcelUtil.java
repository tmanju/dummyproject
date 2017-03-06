package com.thirdpillar.codify.loanpath.service;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.Collateral;
import com.thirdpillar.codify.loanpath.model.Document;
import com.thirdpillar.codify.loanpath.model.FinancialAssessmentExpense;
import com.thirdpillar.codify.loanpath.model.FinancialAssessmentRevenue;
import com.thirdpillar.codify.loanpath.model.FinancialCRE;
import com.thirdpillar.codify.loanpath.util.PdfUtils;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;

@Component
@Configurable
public class ExcelUtil {
	private static final Logger LOG = Logger.getLogger(ExcelUtil.class);
	
	Document document;

	public void ExportFinancialCSV(BaseDataObject entity,Map<Long, BaseDataObject> map){
		int rownum = 0;
		Collateral collateral = (Collateral) entity;
		if(collateral.getCashFlows() != null){
			if(collateral.getCashFlows().getFinancial() != null){
				FinancialCRE financial = collateral.getCashFlows().getFinancial();
				try {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet worksheet = workbook.createSheet("Financial Details");
					Row row;
					for(FinancialAssessmentRevenue finRevenue : financial.getFinAssttRevDer()){
						row = worksheet.createRow(rownum++);
						if(row.getRowNum()==0){
							row.createCell(0).setCellValue("Revenue");
							row.createCell(1).setCellValue("Year 1 Amount");
							row.createCell(2).setCellValue("Year 1 P.U");
						    row.createCell(3).setCellValue("Year 2 Amount");
						    row.createCell(4).setCellValue("Year 2 P.U");
						    row.createCell(5).setCellValue("Year 3 Amount");
						    row.createCell(6).setCellValue("Year 3 P.U");
						    row.createCell(7).setCellValue("Appraisal Amount");
						    row.createCell(8).setCellValue("Appraisal P.U");
						    row.createCell(9).setCellValue("Underwriting Amount");
						    row.createCell(10).setCellValue("Underwriting P.U");
						    row.createCell(11).setCellValue("Comments");
						    row = worksheet.createRow(rownum++);
						}
						if(finRevenue.getRevenueLegend() != null){
							row.createCell(0).setCellValue((String)finRevenue.getRevenueLegend().getValue());
						}if(finRevenue.getYearOne() != null){
							row.createCell(1).setCellValue((String)finRevenue.getYearOne().toString());
							row.createCell(2).setCellValue((String)finRevenue.getYearOnePu().toString());
						}if(finRevenue.getYearTwo() != null){
							row.createCell(3).setCellValue((String)finRevenue.getYearTwo().toString());
							row.createCell(4).setCellValue((String)finRevenue.getYearTwoPu().toString());
						}if(finRevenue.getYearThree() != null){
							row.createCell(5).setCellValue((String)finRevenue.getYearThree().toString());
							row.createCell(6).setCellValue((String)finRevenue.getYearThreePu().toString());
						}if(finRevenue.getYearAppraisal() != null){
							row.createCell(7).setCellValue((String)finRevenue.getYearAppraisal().toString());
							row.createCell(8).setCellValue((String)finRevenue.getYearAppraisalPu().toString());
						}if(finRevenue.getUnderWriting() != null){
							row.createCell(9).setCellValue((String)finRevenue.getUnderWriting().toString());
							row.createCell(10).setCellValue((String)finRevenue.getUnderWritingPu().toString());
						}if(finRevenue.getComment() != null){
							row.createCell(11).setCellValue((String)finRevenue.getComment());
						}
					}
					//Add Total Amount Year Wise
					row = worksheet.createRow(rownum++);
					row.createCell(0).setCellValue("Estimated Gross Income (EGI)");
					if(financial.getGrossIncomeYearOne() != null && financial.getGrossIncomeYearOne().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(1).setCellValue((String)financial.getGrossIncomeYearOne().toString());
					}if(financial.getGrossIncomeYearTwo() != null && financial.getGrossIncomeYearTwo().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(3).setCellValue((String)financial.getGrossIncomeYearTwo().toString());
					}if(financial.getGrossIncomeYearThree() != null && financial.getGrossIncomeYearThree().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(5).setCellValue((String)financial.getGrossIncomeYearThree().toString());
					}if(financial.getGrossIncomeAppraisal() != null && financial.getGrossIncomeAppraisal().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(7).setCellValue((String)financial.getGrossIncomeAppraisal().toString());
					}if(financial.getGrossIncomeUnderWriting() != null && financial.getGrossIncomeUnderWriting().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(9).setCellValue((String)financial.getGrossIncomeUnderWriting().toString());
					}
					row = worksheet.createRow(rownum++);
					//Expense Lines
					row = worksheet.createRow(rownum++);
					row.createCell(0).setCellValue("Expenses");
					
					for(FinancialAssessmentExpense finExpense : financial.getFinAssttExpDer()){
						row = worksheet.createRow(rownum++);
						if(finExpense.getExpenseLegend() != null){
							row.createCell(0).setCellValue((String)finExpense.getExpenseLegend().getValue());
						}if(finExpense.getYearOne() != null){
							row.createCell(1).setCellValue((String)finExpense.getYearOne().toString());
							row.createCell(2).setCellValue((String)finExpense.getYearOnePu().toString());
						}if(finExpense.getYearTwo() != null){
							row.createCell(3).setCellValue((String)finExpense.getYearTwo().toString());
							row.createCell(4).setCellValue((String)finExpense.getYearTwoPu().toString());
						}if(finExpense.getYearThree() != null){
							row.createCell(5).setCellValue((String)finExpense.getYearThree().toString());
							row.createCell(6).setCellValue((String)finExpense.getYearThreePu().toString());
						}if(finExpense.getYearAppraisal() != null){
							row.createCell(7).setCellValue((String)finExpense.getYearAppraisal().toString());
							row.createCell(8).setCellValue((String)finExpense.getYearAppraisalPu().toString());
						}if(finExpense.getUnderWriting() != null){
							row.createCell(9).setCellValue((String)finExpense.getUnderWriting().toString());
							row.createCell(10).setCellValue((String)finExpense.getUnderWritingPu().toString());
						}if(finExpense.getComment() != null){
							row.createCell(11).setCellValue((String)finExpense.getComment());
						}
					}
					//Add Total Expense Amount Year Wise
					row = worksheet.createRow(rownum++);
					row.createCell(0).setCellValue("Total Expenses (T.E.)");
					if(financial.getGrossExpenseYearOne() != null && financial.getGrossExpenseYearOne().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(1).setCellValue((String)financial.getGrossExpenseYearOne().toString());
					}if(financial.getGrossExpenseYearTwo() != null && financial.getGrossExpenseYearTwo().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(3).setCellValue((String)financial.getGrossExpenseYearTwo().toString());
					}if(financial.getGrossExpenseYearThree() != null && financial.getGrossExpenseYearThree().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(5).setCellValue((String)financial.getGrossExpenseYearThree().toString());
					}if(financial.getGrossExpenseAppraisal() != null && financial.getGrossExpenseAppraisal().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(7).setCellValue((String)financial.getGrossExpenseAppraisal().toString());
					}if(financial.getGrossExpenseUnderWriting() != null && financial.getGrossExpenseUnderWriting().compareTo(BigDecimal.ZERO) > 0){
						row.createCell(9).setCellValue((String)financial.getGrossExpenseUnderWriting().toString());
					}
					//Add Total NOI Amount Year Wise
					row = worksheet.createRow(rownum++);
					row = worksheet.createRow(rownum++);
					row.createCell(0).setCellValue("NOI");
					if(financial.getGrossNoiYearOne() != null){
						row.createCell(1).setCellValue((String)financial.getGrossNoiYearOne().toString());
					}if(financial.getGrossNoiYearTwo() != null){
						row.createCell(3).setCellValue((String)financial.getGrossNoiYearTwo().toString());
					}if(financial.getGrossNoiYearThree() != null){
						row.createCell(5).setCellValue((String)financial.getGrossNoiYearThree().toString());
					}if(financial.getGrossNoiAppraisal() != null){
						row.createCell(7).setCellValue((String)financial.getGrossNoiAppraisal().toString());
					}if(financial.getGrossNoiUnderWriting() != null){
						row.createCell(9).setCellValue((String)financial.getGrossNoiUnderWriting().toString());
					}
					workbook.write(stream);
					byte[] bb = stream.toByteArray();
					financial.getDownloadFinancial().setDocumentName("Financial_Detail.xlsx");
					financial.getDownloadFinancial().setUploadedStream(bb);
				}catch (Exception e) {
					LOG.error("Exception occured while exporting ExportFinancialCSV.",e);
				}
			}
		}
	}

}
