package com.thirdpillar.codify.selenium;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

@RunWith(BlockJUnit4ClassRunner.class)
public class SeleniumTest extends TestCase {

    protected static final Log LOG = LogFactory.getLog(SeleniumTest.class);
    protected static final Log PERF = LogFactory.getLog("Performance");

    protected static final int DEFAULT_AJAX_TIMEOUT = 25;
	
	protected WebDriver driver;
	protected int waitTime = 2000;
	protected Properties p;
	private boolean captureScreenShotOnFailure;

	public SeleniumTest() {
		super();
	}
	
	@Before
	public void setUp() throws Exception {
		
		try {
			p = new Properties();
			p.load(SeleniumTest.class.getClassLoader().getResourceAsStream("test.properties"));
			String browser = p.getProperty("test.browser");
			
			if ("chrome".equals(browser)) {
				System.setProperty("webdriver.chrome.driver",p.getProperty("webdriver.chrome.driver"));
				driver = new ChromeDriver();
			} else if ("firefox".equals(browser)) {
				driver = new FirefoxDriver();
			} else if ("remote".equals(browser)) {
				driver = remoteWebDriver();
			} else if ("htmlunit".equals(browser)) {
				driver = htmlUnitDriver();
			} else {
				fail("Unsupported test.browser - " + browser);
			}
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	private WebDriver htmlUnitDriver() {
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setJavascriptEnabled(true);
		return new HtmlUnitDriver(cap);		
	}
	

	private WebDriver remoteWebDriver() throws Exception {
        DesiredCapabilities capabillities = DesiredCapabilities.chrome();  
//        capabillities.setCapability("version", "11");  
//        capabillities.setCapability("platform", Platform.WINDOWS);  
        capabillities.setCapability("name", "Testing LP ");  
        capabillities.setCapability("screenshot", false);
        capabillities.setCapability("screenrecorder", false);
//        capabillities.setCapability("extra", "release 1.2.3");
        
        return new RemoteWebDriver(  
           new URL(p.getProperty("webdriver.remote.url")), capabillities);  
		
	}
	
	@After
	public void tearDown() throws Exception {				
		if (driver != null) {
			driver.quit();
		}
	}

	protected void open(String url) {
		LOG.debug("Open " + url);
		driver.get(url);
	}

	protected void sendKeys(By by, String value) {
		LOG.debug("SendKeys " + by.toString() + ":" + value);
		driver.findElement(by).sendKeys(value);
	}

	protected void type(By by, String value) {
		LOG.debug("Type " + by.toString() + ":" + value);
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(value);
	}

	protected void click(By by) {
		click(by,false);
	}
	
	protected void click(By by, boolean waitForCompletion) {
//		highlightElement(by);
		click(by,waitForCompletion,DEFAULT_AJAX_TIMEOUT);
	}
	
	protected void click(By by, boolean waitForCompletion, int ajaxTimeout) {
		LOG.debug("Click " + by.toString());
		driver.findElement(by).click();
		if (waitForCompletion) {
			waitForAjaxComplete(ajaxTimeout);
			//*[@id="j_idt837"]
		} else {
			pause(waitTime);
		}
	}
	
	
	protected void click(WebElement webElement) {
		click(webElement, false);
	}	
	
	protected void click(WebElement webElement, boolean waitForCompletion) {
		click(webElement,waitForCompletion,DEFAULT_AJAX_TIMEOUT);
	}	
	
	protected void click(WebElement webElement, boolean waitForCompletion, int ajaxTimeout) {
		LOG.debug("Click " + webElement.getAttribute("id"));
		webElement.click();
		if (waitForCompletion) {
			waitForAjaxComplete(ajaxTimeout);
			//*[@id="j_idt837"]
		} else {
			pause(waitTime);
		}
	}	

	protected void selectByValue(By by, String value) {
		LOG.debug("Select " + by.toString() + ":" + value);
		Select selectBox = new Select(driver.findElement(by));
		selectBox.selectByValue(value);
	}

	protected void selectByLabel(By by, String label) {
		LOG.debug("Select " + by.toString() + ":" + label);
		Select selectBox = new Select(driver.findElement(by));
		selectBox.selectByVisibleText(label);
	}
	
	protected void selectByIndex(By by, int index) {
		LOG.debug("Select " + by.toString() + ":" + index);
		Select selectBox = new Select(driver.findElement(by));
		selectBox.selectByIndex(index);
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	

	protected WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
	public void waitForTitle(final String title) {
		  (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver d) {
	                return d.getTitle().toLowerCase().startsWith(title);
	            }
	        });
	  }

	protected boolean waitForElement(final By by, int timeOutInSecs) {
		  LOG.debug("Waitfor " + by.toString());
		  WebElement myDynamicElement = (new WebDriverWait(driver, timeOutInSecs))
				  .until(new ExpectedCondition<WebElement>(){
					public WebElement apply(WebDriver d) {
						return d.findElement(by);
					}});
		  return (myDynamicElement != null);
	  }

	/** Sleeps for the specified number of milliseconds */
	  public void pause(int millisecs) {
	    try {
	      Thread.sleep(millisecs);
	      // Sleeper.SYSTEM_SLEEPER.sleep(thinkTime);
	    } catch (InterruptedException e) {}
	  }
	  
	  public void highlightElement(By by) {
		  	WebElement element = driver.findElement(by);
		    for (int i = 0; i < 2; i++) {
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
		                element, "color: yellow; border: 2px solid yellow;");
		        js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
		                element, "");
		    }
	  }
	  
	  /** Take a screenshot of the current page */
	  protected void screenshot(ByteArrayOutputStream os) throws IOException {
		WebDriver augmentedDriver = new Augmenter().augment(driver);

		os.write(((TakesScreenshot) augmentedDriver)
				.getScreenshotAs(OutputType.BYTES));
	  }
	  
	protected boolean isCaptureScreenShotOnFailure() {
		return captureScreenShotOnFailure;
	}

	protected void setCaptureScreenShotOnFailure(
			boolean captureScreetShotOnFailure) {
		this.captureScreenShotOnFailure = captureScreetShotOnFailure;
	}
	
	protected void waitForAjaxComplete(int ajaxTimeout) {
		(new WebDriverWait(driver, ajaxTimeout))
		.until( new Predicate<WebDriver>() {						
			public boolean apply(WebDriver input) {
				return isAjaxComplete(input);
			}
		});		
		pause(waitTime);
	}
	
	protected boolean isAjaxComplete(SearchContext searchContext) {
		try {
			WebElement ajaxWebElement = searchContext.findElement(By.xpath("//body/div[4]"));
			String ajaxWebElementId = ajaxWebElement.getAttribute("id");
			WebElement ajaxStartElement = ajaxWebElement.findElement(By.xpath("./div[@id=\"" + ajaxWebElementId+"_start\"]"));
			
			boolean ajaxComplete = !StringUtils.contains(ajaxStartElement.getAttribute("style"),"display: block;");
			if (ajaxComplete) {
				// verify ajax page submit			
				WebElement processWebElement = searchContext.findElement(By.xpath("//body/div[5]"));
				return !StringUtils.contains(processWebElement.getAttribute("style"),"display: block;");
				
			} else {
				return false;
			}
		} catch (StaleElementReferenceException e) {
			// ignore
			return true;
		}
	}
	  
}