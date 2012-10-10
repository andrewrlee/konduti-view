package uk.co.optimisticpanda.base;

import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import uk.co.optimisticpanda.utils.ReporterWebDriver;
import uk.co.optimisticpanda.utils.ScreenCapturer;
import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

/**
 * This is a superclass that allows writing directly to the report app data dir.
 * It also specified the junit story runner that generates nice reports.
 */
@RunWith(JUnitReportingRunner.class)
public abstract class AbstractLocalAcceptanceTestCase extends AbstractAcceptanceTestCase {

	private WebDriver driver;
	private ScreenCapturer capturer;
	
	public AbstractLocalAcceptanceTestCase(){
		super();
		this.driver = new ReporterWebDriver( new ChromeDriver());
		this.capturer = new ScreenCapturer(driver);
		init();
		((ReporterWebDriver)driver).setDebugReporter(getDebugReporter());
		capturer.setDebugReporter(getDebugReporter());
	}
	
	@Override
	public String getBaseOutputDirectory() {
		return "site/data/";
	}

	protected WebDriver getWebDriver() {
		return driver;
	}
	
	protected ScreenCapturer getScreenshotCapturer(){
		return capturer;
	}
}
