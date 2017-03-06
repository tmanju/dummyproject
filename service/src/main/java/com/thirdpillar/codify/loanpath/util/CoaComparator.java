
package com.thirdpillar.codify.loanpath.util;


import com.thirdpillar.codify.loanpath.model.CoaEvaluation;
import com.thirdpillar.foundation.rules.RuleConstants;
import com.thirdpillar.foundation.rules.RuleService;
import com.thirdpillar.foundation.service.interceptors.BasicInterceptor;
import com.thirdpillar.foundation.service.interceptors.InterceptorContext;


import org.springframework.beans.factory.annotation.Autowired;


import java.util.Comparator;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Util class to compare two Coas based on their CoaId
 *
 * @author   Sailesh Kushwaha
 * @version  1.0
 * @since    1.0
 */

public class CoaComparator implements Comparator<CoaEvaluation> {
	

	public int compare(CoaEvaluation firstCoaId, CoaEvaluation secondCoaId) {
		int result = 0;
		if(firstCoaId.getCoaDefinition() !=null && secondCoaId.getCoaDefinition() !=null){
			BigDecimal coaDec1 = new BigDecimal(firstCoaId.getCoaDefinition().getCoaId());
			BigDecimal coaDec2 = new BigDecimal(secondCoaId.getCoaDefinition().getCoaId());
			result = coaDec1.compareTo(coaDec2);
			return result;
		}else{
			return result;
		}
	}
}
