package uk.co.optimisticpanda.base;

import static org.jbehave.core.reporters.Format.CONSOLE;

import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

import com.google.common.util.concurrent.MoreExecutors;

public abstract class AbstractAcceptanceTestCase extends JUnitStory {

	private ParameterConverters parameterConverters;
	private NaturalLanguagePhraseConverter naturalPhrases;
	private DebugReporter debugReporter;

	public AbstractAcceptanceTestCase() {
		this.naturalPhrases = NaturalLanguagePhraseConverter.createWithDefaultWords();
		this.parameterConverters = new ParameterConverters().addConverters(naturalPhrases);
		LoggingSetup.setup();
	}

	/** This must be called after all step dependencies have been resolved */
	public void init() {
		CombinedStoryReporter storyReporter = new CombinedStoryReporter(getBaseOutputDirectory());
		debugReporter = storyReporter.getDebugReporter();
		Class<?> embeddableClass = this.getClass();
		Configuration configuration = new MostUsefulConfiguration().useStoryLoader(new LoadFromClasspath(embeddableClass))
		// remove the need for stories to end with "acceptance_test" before the
		// .story suffix
				.useStoryPathResolver(new UnderscoredCamelCaseResolver().removeFromClassName("AcceptanceTest"))
				// Add our story reporter - to generate the report
				.useStoryReporterBuilder(new StoryReporterBuilder().withReporters(storyReporter).withFormats(CONSOLE)).useParameterConverters(parameterConverters.addConverters());

		useConfiguration(configuration);
		List<Object> steps = registerSteps(debugReporter);
		useStepsFactory(new InstanceStepsFactory(configuration, steps));
		configuredEmbedder().useExecutorService(MoreExecutors.sameThreadExecutor());
	}

	/** Use this to register additionalParameterConverters. */
	public ParameterConverters getParameterConverters() {
		return parameterConverters;
	}

	/** Use this to register additional positive/negative phrases */
	public NaturalLanguagePhraseConverter getNaturalPhraseConverter() {
		return naturalPhrases;
	}
	
	/** Use this to register additional positive/negative phrases */
	public DebugReporter getDebugReporter() {
		return debugReporter;
	}

	/**
	 * The file path to the base output directory (This directory must exist!)
	 */
	public abstract String getBaseOutputDirectory();

	/**
	 * Use this hook to register steps. NOTE that this is called on construction
	 * so cannot rely on subclass instance variables
	 * 
	 * @param debugReporter
	 *            a reporter that can be used by steps to record extra messages
	 */
	public abstract List<Object> registerSteps(DebugReporter debugReporter);

}
