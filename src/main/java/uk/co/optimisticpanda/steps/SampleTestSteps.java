package uk.co.optimisticpanda.steps;

import junit.framework.Assert;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebDriver;

import uk.co.optimisticpanda.base.NaturalLanguagePhraseConverter.NaturalLanguagePhrase;
import uk.co.optimisticpanda.pages.TestPage;
import uk.co.optimisticpanda.storybuilder.DebugReporter;
import uk.co.optimisticpanda.utils.ScreenCapturer;

public class SampleTestSteps {

	private final DebugReporter reporter;
	private TestPage testPage;
	private final ScreenCapturer capturer;

	public SampleTestSteps(DebugReporter reporter, ScreenCapturer capturer,
			WebDriver driver) {
		this.reporter = reporter;
		this.capturer = capturer;
		testPage = new TestPage(driver, reporter);
	}

	@Given("I have opened the test page")
	public void openTestPage() {
		testPage.load();
		capturer.captureScreen();
	}

	@When("I search for \"$searchTerm\"")
	public void searchFor(String searchTerm) {
		testPage.search(searchTerm);
		reporter.reportStepMessage("Haven't submitted the form yet!");

	}

	@Then("I \"$phrase\" be able to see \"$searchTerm\" in the searchbox")
	public void checkSearchBoxContents(
			NaturalLanguagePhrase phrase, String searchTerm) {

		boolean actual = searchTerm.equals(testPage.getSearchBoxContents());
		Assert.assertEquals(phrase.resolvedTo(), actual);

		capturer.captureScreen();
		reporter.reportStepMessage("Test Complete!");
		reporter.reportStepError("Should test further though....");
	}

}
