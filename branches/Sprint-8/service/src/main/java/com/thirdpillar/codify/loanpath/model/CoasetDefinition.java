/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   GAGANDEEP SINGH
 * @version  1.0
 * @since    1.0
 */


public class CoasetDefinition extends BaseDataObject {
	 
	  //~ Static fields/initializers -------------------------------------------------------------------------------------

	    /** Use serialVersionUID for interoperability. */
	    private static final long serialVersionUID = 5418621932978769945L;

	    //~ Methods --------------------------------------------------------------------------------------------------------


	 /*
	 		Returns total number of conditions applicable to a facility
	 */
	 public Integer getCoaNumber() {
		 
	   if(getCoaEvls() !=null && getCoaEvls().size() >0){
		   return getCoaEvls().size();
	   }else{
		   return Integer.valueOf(0);
	   }
  }
	
	 
	 /*
	  		Returns the number of conditions that are evaluated as 'Cleared'
	 */
//	 public Integer getCoaCleared() {
//		 int clearCount = 0;
//		 
//		 if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_CLEARED".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey())){
//					 clearCount++;
//				 }	 
//			 }
//		 }
//		 
//      return clearCount;
//  }
//	 
	 
	 /*
	 	 Returns the number of conditions that are evaluated as 'Incomplete'
	 */
//	 public Integer getCoaIncomplete() {
//		 int incompleteCount = 0;
//		 
//		 if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_INCOMPLETE".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey())){
//					 incompleteCount++;
//				 }	 
//			 }
//		 }
//		 
//      return incompleteCount;
//		
//	}
//	

	 /*
 	 Returns the number of conditions that are evaluated as 'Resubmit'
	 */
//	 public Integer getCoaResubmit() {
//		 int count = 0;
//		 
//		 if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_RESUBMIT".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey())){
//					 count++;
//				 }	 
//			 }
//		 }
//		 
//	  return count;
//		
//	}

 
	 /*
	 	 Returns the number of conditions that are evaluated as 'Waived'
	 */ 
//	public Integer getCoaWaived() {
//		int waivedCount = 0;
//		 
//		if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_WAIVED".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey())){
//					 waivedCount++;
//				 }	 
//			 }
//		}
//		 
//     return waivedCount;
//		
//  }
//	
	
	
	 
	 /*
	 	 Returns the number of conditions that are evaluated as 'Exception-Sales' 
	 */ 
//	public Integer getCoaExceptionSales() {
//		int exceptionCount = 0;
//		 
//		if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_EXCEPTION_SALES".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey()) ){
//					 exceptionCount++;
//				 }	 
//			 }
//		}
//		 
//    return exceptionCount;
//		
//  }
	
	 /*
	 Returns the number of conditions that are evaluated as 'Exception-Operations'
	  */ 
//	public Integer getCoaExceptionOperations() {
//		int exceptionCount = 0;
//		 
//		if(getCoaEvls() !=null && getCoaEvls().size() >0){
//			 for(CoaEvaluation coaEval : getCoaEvls()){
//				 if(coaEval.getCoaEvaluation() !=null && "COA_EVALUATION_EXCEPTION_OPERATIONS".equalsIgnoreCase(coaEval.getCoaEvaluation().getKey())   ){
//					 exceptionCount++;
//				 }	 
//			 }
//		}
//		 
//	return exceptionCount;
//		
//	}


	
	

}