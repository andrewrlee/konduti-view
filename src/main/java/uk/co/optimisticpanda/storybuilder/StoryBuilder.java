package uk.co.optimisticpanda.storybuilder;

import static com.google.common.base.Optional.of;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;

import uk.co.optimisticpanda.reports.IndexReport;
import uk.co.optimisticpanda.reports.StoryReport;
import uk.co.optimisticpanda.storybuilder.Step.Result;
import uk.co.optimisticpanda.storybuilder.section.Catalogue;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StoryBuilder {

	private Logger log = Logger.getLogger(StoryBuilder.class);
	private List<Story> stories = Lists.newArrayList();
	private Story currentStory;
	private Scenario currentScenario;
	private Step currentStep;
	private long currentStepStart;
	private long currentScenarioStart;
	private long currentStoryStart;

	public void startStep(String story) {
		currentStep = new Step(story);
		currentScenario.addNewStep(currentStep);
		currentStepStart = System.currentTimeMillis();
	}

	public void finishStep(String step, Result result) {
		currentStep.setResult(result);
		// We set the current name again here because it has now has its
		// parameters marked out
		currentStep.setStepName(step);
		currentStep.setDuration(System.currentTimeMillis() - currentStepStart);
	}

	public void finishStep(String step, Result result, Throwable cause) {
		finishStep(step, result);
		currentStep.setCause(cause);
	}

	public void newNotPerformedStep(String story) {
		currentScenario.addNewNotPerformedStep(story);
	}

	public void newPendingStep(String story) {
		currentScenario.addNewPendingStep(story);
	}

	public void narrative(Narrative narrative) {
		if (!narrative.isEmpty()) {
			LinkedHashMap<String, String> result = Maps.newLinkedHashMap();
			result.put("As a", narrative.asA());
			result.put("I want to", narrative.iWantTo());
			result.put("In order to", narrative.inOrderTo());
			currentStory.setNarrative(result);
		}
	}

	public void startScenario(String scenarioName) {
		currentScenario = new Scenario(scenarioName);
		currentStory.addScenario(currentScenario);
		currentScenarioStart = System.currentTimeMillis();
	}

	public void endScenario() {
		currentScenario.setDuration(System.currentTimeMillis() - currentScenarioStart);
	}

	public void startStory(String name, String description, Meta meta) {
		currentStory = new Story(name, description, meta);
		if (!isRealStory()) {
			return;
		}
		stories.add(currentStory);
		currentStoryStart = System.currentTimeMillis();
	}

	public void endStory() {
		currentStory.endStory(System.currentTimeMillis() - currentStoryStart);
	}

	public Optional<StoryReport> getStoryReport() {
		return isRealStory() ? of(new StoryReport(currentStory)) : Optional.<StoryReport> absent();
	}

	public Optional<IndexReport> getIndexReport() {
		return isRealStory() ? of(new IndexReport(getCatalogue())) : Optional.<IndexReport> absent();
	}

	public Catalogue<Story> getCatalogue() {
		return Catalogue.create(Iterables.toArray(stories, Story.class));
	}

	private boolean isRealStory() {
		return !currentStory.getName().equalsIgnoreCase("BeforeStories") && !currentStory.getName().equalsIgnoreCase("AfterStories");
	}

	public void addMessage(String message) {
		currentStep.addMessage(message);
	}

	public void addError(String error) {
		currentStep.addError(error);
		
	}

	public void addImage(String path) {
		log.info("Adding image with path to " + path + " to " + currentStep);
		currentStep.addImage(path);
	}

}
