package com.thirdpillar.codify.selenium;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CodifyWebappTest extends SeleniumTest {

	public CodifyWebappTest() {
		super();
	}

	protected void loginSaml(String username, String password) {
		// Login
		type(By.id("USER"), username);
		type(By.id("PASSWORD"), password);
		click(By.name("signon"));
		
	}

	protected void loginInternal(String username, String password) {
		// Login		
		type(By.id("USname_textbox"), username);
		type(By.id("pwd_textbox"), password);
		click(By.name("j_idt10"));
		
	}

	protected void openEntity(String name, String oper) {
		viewEntity(name);
		if("Edit".equals(oper)) {
			editEntity();
		}		
		//selectRow("entityListForm:results", "j_idt122", name, oper);
		
	}

	protected void closeEntity(String oper) {
		
		if("View".equals(oper)) {
			doneEntity();
		} else if("Edit".equals(oper)) {
			saveAndCloseEntity();
			doneEntity();
		}
		
	}
	
	protected void escapeEntity(String oper) {
		
		if ("View".equals(oper)) {
			waitForElement(By.id("entityForm:doneEntityBtn"), 10);
		} else if ("Edit".equals(oper)) {
			waitForElement(By.id("entityForm:cancelEntityBtn"), 10);
		}
		goHome();
	}

	protected void openOwnedEntity(String oper) {
		throw new UnsupportedOperationException("openOwnedEntity not supported");		
	}

	protected void closeOwnedEntity(String oper) {
		throw new UnsupportedOperationException("closeOwnedEntity not supported");		
	}

	protected void openRefEntity(String oper) {
		throw new UnsupportedOperationException("openRefEntity not supported");		
	}

	protected void closeRefEntity(String oper) {
		throw new UnsupportedOperationException("closeRefEntity not supported");		
	}

	protected void viewEntity(String name) {
		click(By.linkText(name),true);
	}
	
	protected void filterTable(String tableId, String columnId, String value) {
		String filterId = tableId + "_table:" + columnId + "_filter";
		type(By.xpath("//*[@id=\"" + filterId + "\"]"), value);
		waitForAjaxComplete(DEFAULT_AJAX_TIMEOUT);
		pause(5000);
	}
	
	protected void selectRowByColLink(String tableId, String lookupColumnId, String lookupColumnValue, String selectColumnId) {
		WebElement rowElement = findRow(tableId, lookupColumnId, lookupColumnValue);
		assertNotNull("Unable to find the row", rowElement);
		// Select column
		WebElement colElement = findColumnById(rowElement,selectColumnId);
		assertNotNull("Unable to find column with id " + selectColumnId, colElement);
		click(colElement);		
	}
	
	protected void selectRow(String tableId, String lookupColumnId, String lookupColumnValue, String actionName ) {
		WebElement rowElement = findRow(tableId, lookupColumnId, lookupColumnValue);
		assertNotNull("Unable to find the row", rowElement);
		WebElement actionColElement = findActionsColumn(rowElement);
		assertNotNull("Unable to find actions column on id - ", actionColElement);
		clickOnActionColumn(actionColElement,actionName);
	}
	
	protected String[] findColumnValues(String tableId, String columnId) {
		WebElement tableElement = driver.findElement(By.xpath("//*[@id=\"" + tableId + "\"]"));
		List<WebElement> rowElements = tableElement.findElements(By.xpath(".//table/tbody/tr"));
		List<String> columnValues = new ArrayList<String>();
		for (WebElement rowElement : rowElements) {
			// if row element matches return the rowElement
			WebElement tdElement = rowElement.findElement(By.xpath("./td"));
			if (StringUtils.isNotEmpty(tdElement.getAttribute("colspan"))) {
				// not a regular row
				continue;
			}
			List<WebElement> columnElements = rowElement.findElements(By.xpath(".//td/div[contains(./*/@id,\":" + columnId +"\")]"));
			
			for (WebElement columnElement : columnElements) {
				WebElement childElement = columnElement.findElement(By.xpath("./*[contains(@id,\":" + columnId +"\")]"));
				String id = childElement.getAttribute("id");
				List<WebElement> valueElements = childElement.findElements(By.xpath(".//*[@id='" + id + ":output']"));
				if (valueElements.size() > 0) {
					columnValues.add(valueElements.get(0).getText());
					break;
				} else {
					columnValues.add(childElement.getText().trim());
					break;
				}
			}
		}
		return columnValues.toArray(new String[]{});
	}
	
	protected WebElement findRow(String tableId, String columnId, String value) {
		// 
		WebElement tableElement = driver.findElement(By.xpath("//*[@id=\"" + tableId + "\"]"));
		List<WebElement> rowElements = tableElement.findElements(By.xpath(".//table/tbody/tr"));
		for (WebElement rowElement : rowElements) {
			// if row element matches return the rowElement
			WebElement tdElement = rowElement.findElement(By.xpath("./td"));
			if (StringUtils.isNotEmpty(tdElement.getAttribute("colspan"))) {
				// not a regular row
				continue;
			}			
			List<WebElement> columnElements = rowElement.findElements(By.xpath(".//td/div[contains(./*/@id,\":" + columnId +"\")]"));
			for (WebElement columnElement : columnElements) {
				WebElement childElement = columnElement.findElement(By.xpath("./*[contains(@id,\":" + columnId +"\")]"));
				String id = childElement.getAttribute("id");
				List<WebElement> valueElements = childElement.findElements(By.xpath(".//*[@id=\"" + id + ":output\"]"));
				if (valueElements.size() > 0) {
					if (value.equals(valueElements.get(0).getText())) {
						return rowElement;
					}
				} else {
					if (value.equals(childElement.getText().trim())) {
						return rowElement;
					}
				}
			}
		}

		return null;
	}
	
	protected int findNumberOfRows(String tableId) {
		// 
		WebElement tableElement = driver.findElement(By.xpath("//*[@id=\"" + tableId + "\"]"));
		List<WebElement> rowElements = tableElement.findElements(By.xpath(".//table/tbody/tr"));
		int rows = 0;
		for (WebElement rowElement : rowElements) {
			// if row element matches return the rowElement
			WebElement tdElement = rowElement.findElement(By.xpath("./td"));
			if (StringUtils.isNotEmpty(tdElement.getAttribute("colspan"))) {
				// not a regular row
				continue;
			}	
			rows++;
		}
		return rows;
	}
	
	protected WebElement findColumnById(WebElement rowElement, String columnId) {
		//entityForm:tab_creditRequest_tabGroup:Request_documentGroup_uploadedDocuments_table:0:j_idt1830
		List<WebElement> elements = rowElement.findElements(By.xpath(".//*[contains(@id,\":" + columnId + "\")]"));
		if (elements.size() > 0) {
			return elements.get(0);			
		} else {
			return null;
		}
	}
	
	protected WebElement findActionsColumn(WebElement rowElement) {		
		List<WebElement> elements = rowElement.findElements(By.xpath(".//div[contains(@class,\"action-column\")]"));
		if (elements.size() > 0) {
			return elements.get(0);			
		} else {
			return null;
		}
	}
	
	protected void clickOnActionColumn(WebElement actionColElement, String actionName) {
		WebElement actionElement = actionColElement.findElement(By.xpath(".//a[@title=\""+ actionName+"\"]"));
		assertNotNull(actionName + " not found on " + actionColElement.getAttribute("id"), actionElement);
		click(actionElement);
	}

	private void saveAndCloseEntity() {
		click(By.id("entityForm:saveAndCloseEntityBtn"), true);
	}

	protected void saveEntity() {
		click(By.id("entityForm:saveEntityBtn"), true);
	}

	protected void editEntity() {
		click(By.id("entityForm:editEntityBtn"), true);
	}

	protected void doneEntity() {
		click(By.id("entityForm:doneEntityBtn"),true);
	}

	protected void doneSubpage(String oper) {
		click(By.id("entityForm:subpage" + oper + "DoneBtn"), true);
	}

	protected void clickTab(String tabName) {
		click(By.linkText(tabName));
	}
	
	protected void clickTab(String tabGroupId, String tabName) {
		click(By.linkText(tabName));
	}
	

	protected void clickTabs(String...tabNames) {
		for (String tabName : tabNames) {
			clickTab(tabName);
		}
	}
	

	protected void firstPage(String tableId) {
		click(By.cssSelector("span.ui-icon.ui-icon-seek-first"), true, 10);
	}
	protected void prevPage(String tableId) {
		click(By.cssSelector("span.ui-icon.ui-icon-seek-prev"), true, 10);
	}
	protected void nextPage(String tableId) {
		click(By.cssSelector("span.ui-icon.ui-icon-seek-next"), true, 10);
	}
	protected void lastPage(String tableId) {
		click(By.cssSelector("span.ui-icon.ui-icon-seek-end"), true, 10);
	}
	
	
	
	
	
	protected void goHome() {
		open(p.getProperty("test.url"));
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//         Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected void textarea(String id, String value) {
		type(By.id(id), value);
	}
	
	protected void date(String id, String value) {
		type(By.id(id+"_input"), value+"\t");
		
	}

	// Pass the id of the div with ui-selectonemenu style
	protected void dropdown(String id, int index) {

		String xpathlabel = "//*[@id=\""+id+"\"]/a/label";
		String xpathoption = "//*[@id=\""+id+"_panel\"]/ul/li[" + index + "]";

		click(By.xpath(xpathlabel));
		click(By.xpath(xpathoption));
		
	}
	
	protected void checkbox(String id, boolean value) {
		// TODO: Check or uncheck based on it actual toggle state
		String xpath = value ? "//*[@id=\""+id+"\"]/div[2]" : "//*[@id=\""+id+"\"]/div[2]/span"; 
		click(By.xpath(xpath));		
	}
	
	
	protected void quickSearch(String id, String value) {
		String xpathpanel = "//*[@id=\""+id+"_panel\"]/ul/li[1]";
		
		type(By.id(id + "_input"), value);
		click(By.xpath(xpathpanel));	
		
	}
	
}