package uk.co.optimisticpanda.pages;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

public class TestPage {

	private final WebDriver driver;
	private final DebugReporter reporter;

	public TestPage(WebDriver driver, DebugReporter reporter) {
		this.driver = driver;
		this.reporter = reporter;
	}


	public void load() {
		File file = new File(".", "src/test/resources/test.html");
		driver.get("file:" + file.getAbsolutePath());
	}


	public void search(String text) {
		driver.findElement(By.id("searchInput")).sendKeys(text);
	}


	public String getSearchBoxContents() {
		String text = driver.findElement(By.id("searchInput")).getText();
		reporter.reportStepMessage("Search box contains:" + text);
		return text;
	}

}
