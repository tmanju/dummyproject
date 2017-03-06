package com.thirdpillar.codify.loanpath.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.thirdpillar.foundation.test.RuleInitializationTest;

public class RuleTest extends RuleInitializationTest{
	
	private static final Log LOG = LogFactory.getLog(RuleTest.class);

	@Test
	public void testRuleSetCompilation() {
		compileRuleSet("rules/rule-set.xml");
	}
	
	@Test
	public void testDefaultRuleSetCompilation() {
		compileRuleSet("rules/default-rule-set.xml");
	}
	
	@Test
	public void testAuthRuleSetCompilation() {
		compileRuleSet("rules/auth-rule-set.xml");
	}
		
	
}
