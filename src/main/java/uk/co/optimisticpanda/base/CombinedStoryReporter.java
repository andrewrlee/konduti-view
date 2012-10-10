package uk.co.optimisticpanda.base;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.OutcomesTable;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.reporters.StoryReporter;

import uk.co.optimisticpanda.reports.IndexReport;
import uk.co.optimisticpanda.reports.StoryReport;
import uk.co.optimisticpanda.storybuilder.DebugReporter;
import uk.co.optimisticpanda.storybuilder.Step.Result;
import uk.co.optimisticpanda.storybuilder.StoryBuilder;

import com.google.common.base.Optional;

public class CombinedStoryReporter implements StoryReporter, DebugReporter {

	private final FileWriter writer;
	private static StoryBuilder storyBuilder = new StoryBuilder();

	public CombinedStoryReporter(String baseOutputDirectory) {
		this.writer = new FileWriter(baseOutputDirectory);
	}

	@Override
	public void beforeStory(Story story, boolean givenStory) {
		storyBuilder.startStory(story.getName(), story.getDescription().asString(), story.getMeta());
	}

	@Override
	public void afterStory(boolean givenStory) {
		storyBuilder.endStory();
		Optional<StoryReport> storyReport = storyBuilder.getStoryReport();
		if (storyReport.isPresent()) {
			for (String childDirectory : storyReport.get().getIndexes()) {
				writer.writeOut(childDirectory, storyReport.get().getName() + ".json", storyReport.get().toJson());
			}
		}
		Optional<IndexReport> indexReport = storyBuilder.getIndexReport();
		if (indexReport.isPresent()) {
			writer.writeOut("index.json", indexReport.get().toJson());
		}
	}

	@Override
	public void beforeScenario(String scenarioTitle) {
		storyBuilder.startScenario(scenarioTitle);
	}

	@Override
	public void afterScenario() {
		storyBuilder.endScenario();
	}

	@Override
	public void narrative(Narrative narrative) {
		storyBuilder.narrative(narrative);
	}

	@Override
	public void scenarioNotAllowed(Scenario scenario, String filter) {
	}

	@Override
	public void scenarioMeta(Meta meta) {
	}

	@Override
	public void givenStories(GivenStories givenStories) {
	}

	@Override
	public void givenStories(List<String> storyPaths) {
	}

	@Override
	public void beforeExamples(List<String> steps, ExamplesTable table) {
	}

	@Override
	public void example(Map<String, String> tableRow) {
	}

	@Override
	public void storyNotAllowed(Story story, String filter) {
	}

	@Override
	public void storyCancelled(Story story, StoryDuration storyDuration) {
	}

	@Override
	public void afterExamples() {
	}

	@Override
	public void ignorable(String step) {
	}

	@Override
	public void pending(String step) {
		storyBuilder.newPendingStep(step);
	}

	@Override
	public void beforeStep(String step) {
		storyBuilder.startStep(step);
	}

	@Override
	public void successful(String step) {
		storyBuilder.finishStep(step, Result.SUCCESS);
	}

	@Override
	public void notPerformed(String step) {
		storyBuilder.newNotPerformedStep(step);
	}

	@Override
	public void failed(String step, Throwable cause) {
		storyBuilder.finishStep(step, Result.FAILED, cause);
	}

	@Override
	public void failedOutcomes(String step, OutcomesTable table) {
	}

	@Override
	public void restarted(String step, Throwable cause) {
	}

	@Override
	public void dryRun() {
	}

	@Override
	public void pendingMethods(List<String> methods) {
	}

	public static void resetStoryBuilder() {
		storyBuilder = new StoryBuilder();
	}

	public DebugReporter getDebugReporter() {
		return this;
	}

	@Override
	public void reportStepMessage(String message) {
		storyBuilder.addMessage(message);
	}

	@Override
	public void reportStepImage(String path) {
		Optional<StoryReport> storyReport = storyBuilder.getStoryReport();
		if (!storyReport.isPresent()) {
			throw new IllegalStateException("no story to attach screenshot: " + path + " to.");
		}
		File imageFile = new File(path);
		String result = writer.copy("images", imageFile);
		storyBuilder.addImage(result);
	}

	@Override
	public void reportStepError(String error) {
		storyBuilder.addError(error);
	}

	@Override
	public void reportStepPageDump(String dump) {
		Optional<StoryReport> storyReport = storyBuilder.getStoryReport();
		if (!storyReport.isPresent()) {
			throw new IllegalStateException("no story to attach dump to: " + dump);
		}
		
		String fileName = "dump" + System.currentTimeMillis() + ".html";
		writer.writeOut("images", fileName, dump);
		storyBuilder.addImage("images/" + fileName);
	}
	
}