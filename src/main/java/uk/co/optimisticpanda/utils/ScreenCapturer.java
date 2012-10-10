package uk.co.optimisticpanda.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

public class ScreenCapturer {

	private final static Logger log = Logger.getLogger(ScreenCapturer.class);
	private final ReporterWebDriver driver;
	private DebugReporter reporter;
	
	public ScreenCapturer(WebDriver driver){
		if(! (driver instanceof ReporterWebDriver)){
			throw new IllegalArgumentException("Can only take screenshots with a ReporterWebDriver");
		}
		this.driver = (ReporterWebDriver) driver;
	}
	
	public void setDebugReporter(DebugReporter reporter){
		this.reporter = reporter;
	}
	
	public void onError(ScreenShotIfExceptionCallback callback){
		try{
			callback.execute();
		}catch(Exception e){
			log.error(e.getMessage(), e);
			captureScreen();
			throw new RuntimeException(e);
		}
	}

	public void captureScreen() {
		if(driver.isTakesScreenshot()){
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			log.info("screenshot image saved here:" + scrFile.getAbsolutePath());
			reporter.reportStepImage(scrFile.getAbsolutePath());
		}else{
			reporter.reportStepPageDump(driver.getPageSource());
		}
	}
	
	public static interface ScreenShotIfExceptionCallback{
		public void execute() throws Exception; 
		
	}
	
}
