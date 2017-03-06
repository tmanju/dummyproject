package com.thirdpillar.codify.selenium;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.openqa.selenium.By;

public class ApplicationTest extends CodifyWebappTest {

	String[] appNums = new String[] {
			"103837", "101428", "101443", "101349", "101354", "101380", "101480", "101492", "101413", "101501", // Dev
//			"105481", "105472", "104963", "104946", "104929", "104589", "104293", "104286", "104067", "103244"  // Prod
	};


	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Loanpath Application tests
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void testBrowseApplication() throws Exception {
		
		open(p.getProperty("test.url"));

		loginSaml("Ping101", "p2wd4001");
		//loginInternal("demouser1", "password");
		int failed = 0;

		for (int i = 0; i < 2400; i++) {
			String appNum = null;
			try {
				
				appNum = appNums[RandomUtils.nextInt(10)];
				System.out.println("testBrowseApplication: " + appNum);
				long start = System.currentTimeMillis();
				browseApplication(appNum, "Edit");		
				long end = System.currentTimeMillis();
				System.out.println("Application,"+ appNum +","+ (end-start));
				
			} catch (Exception e) {
				failed++;
				LOG.debug("testBrowseApplication failed: " + appNum, e);
				System.out.println("Application,"+ appNum +",0");
				continue;
			}
		}			
		
		if(failed > 0)
			System.out.println("Failure count!: " + failed);
		
	}
	
	@Test
	public void paginate() throws Exception {
		
		open("https://pieduat.thirdpillar.com/loanpath-piedmont-web/search/customer/main/results.seam");
		loginSaml("Ping101", "p2wd4001");
		
		for (int i = 0; i < 300; i++) {
			nextPage("entityListForm:results_table");
		}

		for (int i = 0; i < 300; i++) {
			prevPage("entityListForm:results_table");
		}

	}
	
	public void testSubmittedToCreditTOPendingAnalysis() throws Exception {
		open(p.getProperty("test.url"));

		loginInternal("demouser1", "password");

		openEntity("104256", "Edit");
		applyApplicationChanges();
		
		dropdown("entityForm:tasksResults:entityTaskFilter", 3);
		
		//closeEntity("Edit");
		
		
		//clickTab("Tasks");
		
		pause(10000);
		
	}

	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Loanpath navigations/operations
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	 * Start page: Application List page
	 * End page: Application List page
	 */
	private void browseApplication(String name, String oper) {
		
		// Filter
		//"entityListForm:results_table:j_idt120_filter"
		filterTable("entityListForm:results","j_idt120",name);

		// Open Application
		openEntity(name, oper);
		
		// Click through tabs
		click(By.linkText("Credit Decision"),true);
		click(By.linkText("Sources of Funds"),false);
		click(By.linkText("Credit Decision"),false);
		click(By.linkText("Exposure"),true);
		click(By.linkText("RAC"),true);
		click(By.linkText("COA"),true);
		click(By.linkText("Bureaus"),true);
		click(By.linkText("Decision"),true);
		
		// Documents
		click(By.linkText("Documents"),true);
		//downloadDocuments(oper);
		
		// Select document name
		/*
		String[] columnValues = findColumnValues("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_uploadedDocuments","j_idt2576");
		if (columnValues.length > 0) {
			int randomRowIndex = RandomUtils.nextInt(100)%columnValues.length;
			LOG.debug("Selected random row - " + columnValues[randomRowIndex]);
			selectRowByColLink("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_uploadedDocuments","j_idt2576",columnValues[randomRowIndex],"j_idt2576");
			doneSubpage(oper);
		}*/
		
		click(By.linkText("UCC"),true);

		// Versions
		click(By.linkText("Versions"),true);
//		String[] columnValues = findColumnValues("entityForm:tab_creditRequest_tabGroup:request_taggedVersions","v_tagName");
//		if (columnValues.length > 0) {
//			int randomRowIndex = RandomUtils.nextInt(100)%columnValues.length;
//			LOG.debug("Selected random row - " + columnValues[randomRowIndex]);
//			selectRowByColLink("entityForm:tab_creditRequest_tabGroup:request_taggedVersions","v_tagName",columnValues[randomRowIndex],"v_tagName");
//			doneEntity();
//		}
		
		click(By.linkText("Application"),true);
		
		// Browse Product
		browseProduct(0, oper);
		
		// Close App
		escapeEntity(oper);
		
	}
	

	/*
	 * Start page: Application crud page
	 * End page: Application crud page
	 */
	private void browseProduct(int index, String oper) {
		openProduct(index, oper);
		doneSubpage(oper);		
	}
	
	protected void openProduct(int index, String oper) {
		String action = StringUtils.lowerCase(oper);
		click(By.id("entityForm:tab_creditRequest_tabGroup:Request_allFacilities_table:"+ index + ":Request_allFacilities_" + action + "Action"), true);
	}


	private void downloadDocuments(String oper) {
		int num = findNumberOfRows("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_generatedDocuments");
		LOG.debug("Generated Docs: " + num);
		for (int i = 0; i < num; i++) {
			if(RandomUtils.nextInt(100) < 30)
				downloadDocument("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_generatedDocuments_table", i, "generatedDocument");
		}

		LOG.debug("Uploaded Docs: " + num);
		num = findNumberOfRows("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_uploadedDocuments");
		for (int i = 0; i < num; i++) {
			if(RandomUtils.nextInt(100) < 30)
				downloadDocument("entityForm:tab_creditRequest_tabGroup:Request_documentGroup_uploadedDocuments_table", i, "document");
		}
	}

	private void downloadDocument(String tableId, int index, String property) {
		String id = tableId + ":"+ index + ":" + property + "_uploadedStream:output";
		LOG.debug(id);
		if(waitForElement(By.id(id), 1)) {
			click(By.id(id));			
		}
		
		// TODO: block until the document gets downloaded
	}

	protected void openDocument(int index, String oper) {
		String action = StringUtils.lowerCase(oper);
	}

	protected void applyApplicationChanges() {
		dropdown("entityForm:tab_creditRequest_tabGroup:request_creditAnalyst:input", 7);
		date("entityForm:tab_creditRequest_tabGroup:request_interviewDttm:input", "Sep 28, 2012 10:00");
		dropdown("entityForm:tab_creditRequest_tabGroup:request_complexity:input", 1);
		checkbox("entityForm:tab_creditRequest_tabGroup:request_turnTimeExclude:input", true);
		textarea("entityForm:tab_creditRequest_tabGroup:request_appDescription:input", "description");
		quickSearch("entityForm:tab_creditRequest_tabGroup:request_relationship_searchBox:input", "Mayer Eric Test-Relationship");
		
		// Product
		applyChangesProduct(0);
		
	}
	
	protected void applyChangesProduct(int index) {
		openProduct(index, "Edit");
		dropdown("entityForm:tab_creditRequest_Application_FacilityMain_tabGroup:facility_facilityCategory:input", 2);
		dropdown("entityForm:tab_creditRequest_Application_FacilityMain_tabGroup:facility_purposeCode:input", 2);
		dropdown("entityForm:tab_creditRequest_Application_FacilityMain_tabGroup:facility_pricingOption_structure_facilityRR:input", 2);
		dropdown("entityForm:tab_creditRequest_Application_FacilityMain_tabGroup:facility_pricingOption_structure_source:input", 2);
		doneSubpage("Edit");
	}

}
