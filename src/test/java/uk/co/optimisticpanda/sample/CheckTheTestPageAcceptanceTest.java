package uk.co.optimisticpanda.sample;

import java.util.Arrays;
import java.util.List;

import uk.co.optimisticpanda.base.AbstractLocalAcceptanceTestCase;
import uk.co.optimisticpanda.steps.SampleTestSteps;
import uk.co.optimisticpanda.storybuilder.DebugReporter;

public class CheckTheTestPageAcceptanceTest extends
		AbstractLocalAcceptanceTestCase {

	@Override
	public List<Object> registerSteps(DebugReporter reporter) {
		return Arrays.<Object> asList(new SampleTestSteps(reporter,
				getScreenshotCapturer(), getWebDriver()));
	}

}
