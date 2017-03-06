package com.thirdpillar.codify.loanpath.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
/*import org.jgroups.tests.perf.Data;*/
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.*;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

/**
 * 
 * @author rahul.dhiman
 *
 */

public class CalculateDailyInterestBizOp extends AbstractBusinessOperation{
	
	private static final Log LOG = LogFactory.getLog(CalculateDailyInterestBizOp.class);
	
	private int scale = 3;
	
	 public Object execute(String operation, Object... params) {
		 LOG.info("****************Processing Interest Started");
		 EntityService es = new EntityService();
		 MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		 List<FacAllocator> facilityAllocators = (List<FacAllocator>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FacAllocator.byWfStatus", "statusKey", Constants.DEALS_STATUS_ACCEPTED_KEY);
		 LOG.info("****************No of Fac Allocators processing="+facilityAllocators.size());
		 for(FacAllocator facAllocator : facilityAllocators){
			 LOG.info("****************Processing facAllocator="+facAllocator);
			 int numDays = 1;
			 if(facAllocator.getIntCalcDt() !=  null){
				 Calendar currDate = Calendar.getInstance();
				 Calendar calcDt = Calendar.getInstance();
				 calcDt.setTime(facAllocator.getIntCalcDt());
				 /**
				  * If interest for day is already calculated do skip
				  */
				 if(currDate.get(Calendar.MONTH) == calcDt.get(Calendar.MONTH) && currDate.get(Calendar.DATE) == calcDt.get(Calendar.DATE) && currDate.get(Calendar.YEAR) == calcDt.get(Calendar.YEAR)){
					 LOG.info("****************Processing already completed for facAllocator="+facAllocator+" hence skipping.");
					 continue;
				 }else{
					 numDays = Days.daysBetween(new LocalDate(calcDt.getTime()), new LocalDate(currDate.getTime())).getDays();
				 }
			 }
			 
			 for(int i=numDays; i>0; i--){
				 
				 Calendar startDt = Calendar.getInstance();
				 startDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH)-i);
				 startDt.set(Calendar.HOUR_OF_DAY, 0);
				 startDt.set(Calendar.MINUTE, 0);
				 startDt.set(Calendar.SECOND, 0);
				 
				 Calendar endDt = Calendar.getInstance();
				 endDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH));
				 endDt.set(Calendar.MONTH, startDt.get(Calendar.MONTH));
				 endDt.set(Calendar.YEAR, startDt.get(Calendar.YEAR));
				 endDt.set(Calendar.HOUR_OF_DAY, 23);
				 endDt.set(Calendar.MINUTE, 59);
				 endDt.set(Calendar.SECOND, 59);
				 
				 FacAllocatorHistory facAllocatorHistory = null;
				 BigDecimal cpBalance = BigDecimal.ZERO;
				 List<FacAllocatorHistory> facAllocatorHistories = (List<FacAllocatorHistory>) es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FacAllocatorHistory.byCreatedAt", new String[]{"startDttm","id"}, new Object[]{endDt.getTime(), facAllocator.getId()});
				 if(facAllocatorHistories.isEmpty()){
					 cpBalance = facAllocator.getCpBalance();
				 }else{
					 facAllocatorHistory = facAllocatorHistories.get(0);
					 cpBalance = facAllocatorHistory.getCpBalance();
				 }
				 BigDecimal interestRate = facAllocator.getPlacementRate();
				 BigDecimal principle = BigDecimal.ZERO;
				 BigDecimal interestAccural = BigDecimal.ZERO;
 				 
					 try{
						 if(interestRate != null && interestRate.compareTo(BigDecimal.ZERO) > 0 && cpBalance != null){
							// Calculate interest by following formula
							 // interestRate/360*capital provider balance
							 
							 if(facAllocator.getMinUtilizationAmt().compareTo(facAllocator.getCpBalance()) >= 0){
								 principle = facAllocator.getMinUtilizationAmt();
							 }else{
								 principle = facAllocator.getCpBalance();
							 }
							 
							interestAccural = calculatePercentAmt(principle, interestRate, scale);
							 
							 LOG.info("****************Computed Accrual interest for facAllocator="+facAllocator+" is "+interestAccural);
						 }
						 
						 /*Calculate principle for unused line rate start*/
						 BigDecimal computedULInt = BigDecimal.ZERO;
						 BigDecimal unusedLineRate = null;
						 BigDecimal unusedPrinciple = BigDecimal.ZERO;
						 if(facAllocator.getFacilityBE().getTotalCpBalance() != null && facAllocator.getUnusedLineRate() != null){
							 BigDecimal commitmentAmt = facAllocator.getFundAmt();
							 unusedPrinciple = Utility.subtract(commitmentAmt, principle, scale);
							 unusedLineRate = facAllocator.getUnusedLineRate();
							 // Calculate interest by following formula
							 // interestRate/360*Unused line principle
							 computedULInt = calculatePercentAmt(unusedPrinciple, unusedLineRate, scale);
							 LOG.info("****************Computed unused line rate for facAllocator="+facAllocator+" is "+computedULInt);
						 }
						 /*Calculate principle for unused line rate end*/
						  
						 //Calculate principle for servicing fee start
						 BigDecimal servingFeeOnCollateral = BigDecimal.ZERO;
						 BigDecimal collateralBase = BigDecimal.ZERO;
						 BigDecimal grossAR = BigDecimal.ZERO;
						 BigDecimal servicingFee = null;
						 if(facAllocator.getCapitalProvider() != null && facAllocator.getCapitalProvider().getCustomerAssociated() != null && facAllocator.getCapitalProvider().getCustomerAssociated().getServicingFee() != null){
							 servicingFee = new BigDecimal(facAllocator.getCapitalProvider().getCustomerAssociated().getServicingFee());
						 }
						 if(facAllocator.getFacilityBE().getGrossAR() != null && servicingFee != null){
							 grossAR = facAllocator.getFacilityBE().getGrossAR();
							 grossAR = grossAR.setScale(scale,context.getRoundingMode());
							 BigDecimal facCommitmentRate = facAllocator.getFundPer();
							 facCommitmentRate = facCommitmentRate.setScale(scale,context.getRoundingMode());
							 /*Collateral Base = facilityCommitment%*GrossAR*/
							 collateralBase = facCommitmentRate.multiply(grossAR,context);
							 collateralBase = collateralBase.divide(new BigDecimal("100"),context);
							 // Calculate servicing fee by following formula
							 // interestRate/360*collateral Base
							 servingFeeOnCollateral = calculateServicingFee(collateralBase, servicingFee, scale);
							
							 LOG.info("****************Computed servicing fee for facAllocator="+facAllocator+" is "+servingFeeOnCollateral);
						 }
						 //Calculate principle for servicing fee end
						 
						 
						 facAllocator.setDailyIntAccrual(interestAccural);
						 facAllocator.setUnusedLineFee(computedULInt);
						 facAllocator.setServicingFeeCollateral(servingFeeOnCollateral);
						 
						 Calendar intCalcDt = Calendar.getInstance();
						 
						 startDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH)+1);
					
						 endDt.set(Calendar.DAY_OF_MONTH, endDt.get(Calendar.DAY_OF_MONTH)+1);
						 
						 facAllocator.setIntCalcDt(startDt.getTime());
						 intCalcDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH));
						 intCalcDt.set(Calendar.MONTH, startDt.get(Calendar.MONTH));
						 intCalcDt.set(Calendar.YEAR, startDt.get(Calendar.YEAR));
						 
						 List<CPInterestRecord> cpInterestRecords = es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.CPInterestRecord.byCustomerBackend", new String[]{"id","intCalcDt","intCalcDtEnd"}, new Object[]{facAllocator.getCapitalProvider().getCustomerAssociated().getId(),startDt.getTime(),endDt.getTime()});
						 CPInterestRecord cpInterestRecord = null;
						 if(cpInterestRecords.isEmpty()){
							 cpInterestRecord = new CPInterestRecord();
							 cpInterestRecord.setDailyIntAccrual(BigDecimal.ZERO);
							 cpInterestRecord.setUnusedLineFee(BigDecimal.ZERO);
							 cpInterestRecord.setServicingFee(BigDecimal.ZERO);
							 cpInterestRecord.setIntCalcDt(intCalcDt.getTime());
						 }else{
							 cpInterestRecord = cpInterestRecords.get(0);
						 }
						 
						 cpInterestRecord.setDailyIntAccrual(Utility.add(cpInterestRecord.getDailyIntAccrual(), interestAccural, scale));
						 cpInterestRecord.setUnusedLineFee(Utility.add(cpInterestRecord.getUnusedLineFee(), computedULInt, scale));
						 cpInterestRecord.setServicingFee(Utility.add(cpInterestRecord.getServicingFee(), servingFeeOnCollateral, scale));
						 
						 cpInterestRecord.setCustomerBackend(facAllocator.getCapitalProvider().getCustomerAssociated());
						 
						 FacAllocatorIntRecord facAllocatorIntRecord = new FacAllocatorIntRecord();
						 facAllocatorIntRecord.setCpBalance(cpBalance);
						 
						 facAllocatorIntRecord.setGrossAR(grossAR);
						 facAllocatorIntRecord.setUnusedLineRate(unusedLineRate);
						 facAllocatorIntRecord.setServicefeeRate(servicingFee);
						 facAllocatorIntRecord.setPlacementRate(interestRate);
						 facAllocatorIntRecord.setFacFIU(facAllocator.getFacilityBE().getFacBalance());
						 
						 facAllocatorIntRecord.setCollateralBase(collateralBase);
						 facAllocatorIntRecord.setEarnedInterest(interestAccural);
						 facAllocatorIntRecord.setServicingFee(servingFeeOnCollateral);
						 facAllocatorIntRecord.setUnUsedLineFee(computedULInt);
						 facAllocatorIntRecord.setFacAllocator(facAllocator);
						 facAllocatorIntRecord.setIntCalcDt(intCalcDt.getTime());
						 facAllocatorIntRecord.setPrinciple(principle);
						 facAllocatorIntRecord.setMinUtilization(facAllocator.getMinUtilizationAmt());
						 facAllocatorIntRecord.setFundingLimit(facAllocator.getFundAmt());
						 facAllocatorIntRecord.setCommitmentPercent(facAllocator.getFundPer());
						 
						 LOG.info("****************Saving data for facAllocator="+facAllocator);
						 es.saveOrUpdate(facAllocatorIntRecord);
						 es.saveOrUpdate(cpInterestRecord);
						 es.update(facAllocator);
						 es.flush();
					 }catch(Exception e){
						 LOG.fatal("****************Failed with a fatal error for facAllocator="+facAllocator,e);
					 }
				 
			 }
		 }
		 
		 /**
		  * Update interest for CP's here
		  */
		 updateCustomerBackend();
		 LOG.info("****************Processing Interest Completed*************************");
		 return null;
	 }
	 
	 public void updateCustomerBackend(){
		 EntityService es = new EntityService();
		 LOG.info("****************Processing CP aggregation of interest and fee started*************************");
		 List<CustomerBackend> customerBackendObjs = (List<CustomerBackend>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.CustomerBackend.fetchAllCustomersByINX","isInxCp",false);
		 LOG.info("****************# of CP's to be processed="+customerBackendObjs.size());
		 for(CustomerBackend customerBackend : customerBackendObjs){
			 try{
				 LOG.info("****************Processing CP="+customerBackend);
				 int numDays = 1;
				 if(customerBackend.getIntCalcDt() !=  null){
					 Calendar currDate = Calendar.getInstance();
					 Calendar calcDt = Calendar.getInstance();
					 calcDt.setTime(customerBackend.getIntCalcDt());
					 /**
					  * If interest for day is already calculated do skip
					  */
					 if(currDate.get(Calendar.MONTH) == calcDt.get(Calendar.MONTH) && currDate.get(Calendar.DATE) == calcDt.get(Calendar.DATE) && currDate.get(Calendar.YEAR) == calcDt.get(Calendar.YEAR)){
						 LOG.info("****************Processing already completed for customer backend="+customerBackend+" hence skipping.");
						 continue;
					 }else{
						 numDays = Days.daysBetween(new LocalDate(calcDt.getTime()), new LocalDate(currDate.getTime())).getDays();
					 }
				 }
				 
				 for(int i=numDays; i>0; i--){
					 
					 Calendar startDt = Calendar.getInstance();
					 startDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH)-i+1);
					 startDt.set(Calendar.HOUR_OF_DAY, 0);
					 startDt.set(Calendar.MINUTE, 0);
					 startDt.set(Calendar.SECOND, 0);
					 
					 Calendar endDt = Calendar.getInstance();
					 endDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH));
					 endDt.set(Calendar.MONTH, startDt.get(Calendar.MONTH));
					 endDt.set(Calendar.YEAR, startDt.get(Calendar.YEAR));
					 endDt.set(Calendar.HOUR_OF_DAY, 23);
					 endDt.set(Calendar.MINUTE, 59);
					 endDt.set(Calendar.SECOND, 59);
					 
					 List<CPInterestRecord> cpInterestRecords = es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.CPInterestRecord.byCustomerBackend", new String[]{"id","intCalcDt","intCalcDtEnd"}, new Object[]{customerBackend.getId(),startDt.getTime(),endDt.getTime()});
					 CPInterestRecord cpInterestRecord = null;
					 if(!cpInterestRecords.isEmpty()){
						 cpInterestRecord = cpInterestRecords.get(0);
					 }else{
						 continue;
					 }
					 
					 
					 /**
					  * Get total daily interest accrual,unused Line Fee and servicing fee on Collateral for this CP for all facilities
					  */
					 BigDecimal totalDailyAccrual = BigDecimal.ZERO;
					 BigDecimal unusedLineFee = BigDecimal.ZERO;
					 BigDecimal servicingFeeOnCollateral = BigDecimal.ZERO;
					 
					 if(cpInterestRecord.getDailyIntAccrual() != null){
						 totalDailyAccrual = cpInterestRecord.getDailyIntAccrual();
					 }if(cpInterestRecord.getUnusedLineFee() != null){
						 unusedLineFee = cpInterestRecord.getUnusedLineFee();
					 }if(cpInterestRecord.getServicingFee() != null){
						 servicingFeeOnCollateral = cpInterestRecord.getServicingFee();
					 }
					 
					 //Calculate MTD
					 if(customerBackend.getInterestEarnedMTD() == null){
						 customerBackend.setInterestEarnedMTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getServicingFeeCaMTD() == null){
						 customerBackend.setServicingFeeCaMTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getUnusedLineFeeMTD() == null){
						 customerBackend.setUnusedLineFeeMTD(BigDecimal.ZERO);
					 }
					 
					 BigDecimal intEarnedMTD = Utility.add(customerBackend.getInterestEarnedMTD(), totalDailyAccrual, scale);
					 BigDecimal servicingFeeEarnedMTD = Utility.add(customerBackend.getServicingFeeCaMTD(), servicingFeeOnCollateral, scale);
					 BigDecimal unusedLineFeeEarnedMTD = Utility.add(customerBackend.getUnusedLineFeeMTD(), unusedLineFee, scale);
					 
					 //Start of month
					 if(startDt.get(Calendar.DAY_OF_MONTH) == 1){
						 customerBackend.setInterestEarnedMTD(BigDecimal.ZERO);
						 customerBackend.setServicingFeeCaMTD(BigDecimal.ZERO);
						 customerBackend.setUnusedLineFeeMTD(BigDecimal.ZERO);
						 
						 customerBackend.setInterestEarnedMTD(BigDecimal.ZERO);
						 customerBackend.setServicingFeeCpMTD(BigDecimal.ZERO);
						 customerBackend.setUnusedFeePaidMTD(BigDecimal.ZERO);
					 }else{
						 customerBackend.setInterestEarnedMTD(intEarnedMTD);
						 customerBackend.setServicingFeeCaMTD(servicingFeeEarnedMTD);
						 customerBackend.setUnusedLineFeeMTD(unusedLineFeeEarnedMTD);
					 }
					 
					 //Set YTD
					 if(customerBackend.getInterestEarnedYTD() == null){
						 customerBackend.setInterestEarnedYTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getServicingFeeCaYTD() == null){
						 customerBackend.setServicingFeeCaYTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getUnusedLineFeeYTD() == null){
						 customerBackend.setUnusedLineFeeYTD(BigDecimal.ZERO);
					 }
					 
					 BigDecimal intEarnedYTD = Utility.add(customerBackend.getInterestEarnedYTD(), totalDailyAccrual, scale);
					 BigDecimal servicingFeeEarnedYTD = Utility.add(customerBackend.getServicingFeeCaYTD(), servicingFeeOnCollateral, scale);
					 BigDecimal unusedLineFeeEarnedYTD = Utility.add(customerBackend.getUnusedLineFeeYTD(), unusedLineFee, scale);
					
					 //Begining of new year
					 if(startDt.get(Calendar.MONTH) == Calendar.JANUARY && startDt.get(Calendar.DAY_OF_MONTH) == 1){
						 customerBackend.setInterestEarnedYTD(BigDecimal.ZERO);
						 customerBackend.setServicingFeeCaYTD(BigDecimal.ZERO);
						 customerBackend.setUnusedLineFeeYTD(BigDecimal.ZERO);
						 
						 customerBackend.setInterestEarnedYTD(BigDecimal.ZERO);
						 customerBackend.setServicingFeeCpYTD(BigDecimal.ZERO);
						 customerBackend.setUnusedFeePaidYTD(BigDecimal.ZERO);
					 }else{
						 customerBackend.setInterestEarnedYTD(intEarnedYTD);
						 customerBackend.setServicingFeeCaYTD(servicingFeeEarnedYTD);
						 customerBackend.setUnusedLineFeeYTD(unusedLineFeeEarnedYTD);
					 }
					
					 //Set LTD 
					 if(customerBackend.getInterestEarnedLTD() == null){
						 customerBackend.setInterestEarnedLTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getServicingFeeCaLTD() == null){
						 customerBackend.setServicingFeeCaLTD(BigDecimal.ZERO);
					 }
					 if(customerBackend.getUnusedLineFeeLTD() == null){
						 customerBackend.setUnusedLineFeeLTD(BigDecimal.ZERO);
					 }
					 
					 BigDecimal intEarnedLTD = Utility.add(customerBackend.getInterestEarnedLTD(), totalDailyAccrual, scale);
					 BigDecimal servicingFeeEarnedLTD = Utility.add(customerBackend.getServicingFeeCaLTD(), servicingFeeOnCollateral, scale);
					 BigDecimal unusedLineFeeEarnedLTD = Utility.add(customerBackend.getUnusedLineFeeLTD(), unusedLineFee, scale);
					 
					 customerBackend.setInterestEarnedLTD(intEarnedLTD);
					 customerBackend.setServicingFeeCaLTD(servicingFeeEarnedLTD);
					 customerBackend.setUnusedLineFeeLTD(unusedLineFeeEarnedLTD);
					 
					 
					 
			
					 Calendar intCalcDt = Calendar.getInstance();
					 intCalcDt.set(Calendar.DAY_OF_MONTH, startDt.get(Calendar.DAY_OF_MONTH));
					 intCalcDt.set(Calendar.MONTH, startDt.get(Calendar.MONTH));
					 intCalcDt.set(Calendar.YEAR, startDt.get(Calendar.YEAR));
					 
					 
					 
					 
					 
					 
					 customerBackend.setIntCalcDt(intCalcDt.getTime());
					 
					 //Add record to history table
					 cpInterestRecord.setDailyIntAccrual(totalDailyAccrual);
					 cpInterestRecord.setServicingFee(servicingFeeOnCollateral);
					 cpInterestRecord.setUnusedLineFee(unusedLineFee);
					 
					 cpInterestRecord.setEarnedIntMTD(customerBackend.getInterestEarnedMTD());
					 cpInterestRecord.setEarnedIntYTD(customerBackend.getInterestEarnedYTD());
					 cpInterestRecord.setEarnedIntLTD(customerBackend.getInterestEarnedLTD());
					 
					 cpInterestRecord.setServicingFeeMTD(customerBackend.getServicingFeeCaMTD());
					 cpInterestRecord.setServicingFeeYTD(customerBackend.getServicingFeeCaYTD());
					 cpInterestRecord.setServicingFeeLTD(customerBackend.getServicingFeeCaLTD());
					 
					 cpInterestRecord.setUnusedLineFeeMTD(customerBackend.getUnusedLineFeeMTD());
					 cpInterestRecord.setUnusedLineFeeYTD(customerBackend.getUnusedLineFeeYTD());
					 cpInterestRecord.setUnusedLineFeeLTD(customerBackend.getUnusedLineFeeLTD());
					 cpInterestRecord.setIntCalcDt(intCalcDt.getTime());
					 
					 
					 cpInterestRecord.setCustomerBackend(customerBackend);
					 
					 LOG.info("****************Saving CP="+customerBackend);
					 es.saveOrUpdate(cpInterestRecord);
					 es.saveOrUpdate(customerBackend);
					 es.flush();
				 }
				 
				 	 
			 }catch(Exception e){
				 LOG.fatal("*******************Exception occured while processing cp**********************",e);
			 }
		 }
		 LOG.info("****************Processing CP aggregation of interest and fee completed*************************");
	 }
	 
	 private BigDecimal calculateServicingFee(BigDecimal amt, BigDecimal percent, int scale){
		 MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		 BigDecimal result = BigDecimal.ZERO;
		 amt.setScale(scale,context.getRoundingMode());
		 percent.setScale(scale,context.getRoundingMode());
		 BigDecimal calc = percent.multiply(amt,context);
		 result = calc.divide(new BigDecimal("100"),context);
		 return result;
	 }
	 
	 private BigDecimal calculatePercentAmt(BigDecimal amt, BigDecimal percent, int scale){
		 MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		 BigDecimal result = BigDecimal.ZERO;
		 amt.setScale(scale,context.getRoundingMode());
		 percent.setScale(scale,context.getRoundingMode());
		 BigDecimal calc = percent.divide(new BigDecimal("360"),context);
		 calc = calc.multiply(amt,context);
		 result = calc.divide(new BigDecimal("100"),context);
		 return result;
	 }
	 
}
