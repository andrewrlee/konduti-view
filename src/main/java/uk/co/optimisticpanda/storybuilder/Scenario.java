package uk.co.optimisticpanda.storybuilder;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Scenario {

	private final String scenarioName;
	private final List<Step> steps = Lists.newArrayList();
	private String duration;
	private Statistics statistics;

	public Scenario(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public void addNewStep(Step currentStep) {
		steps.add(currentStep);
	}

	public void addNewNotPerformedStep(String story) {
		steps.add(Step.notPerformedStep(story));
	}

	public void addNewPendingStep(String story) {
		steps.add(Step.notPendingStep(story));
	}

	public void setDuration(long duration) {
		this.duration = String.valueOf(duration / 1000) + "."
				+ (duration % 1000) + "s";
	}

	public Statistics calculateStats() {
		int successes = 0;
		int failures = 0;
		int pending = 0;
		int notRun = 0;
		int total = 0;

		for (Step step : steps) {
			total++;
			switch (step.getResultAsEnum()) {
			case CURRENT:
				break;
			case FAILED:
				failures++;
				break;
			case NOT_PERFORMED:
				notRun++;
				break;
			case SUCCESS:
				successes++;
				break;
			case PENDING:
				pending++;
				break;
			}
		}
		statistics = new Statistics(failures, successes, pending, notRun, total);
		return statistics;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(Scenario.class) //
				.add("scenarioName", scenarioName)//
				.add("steps", steps) // ;
				.add("duration", duration) // ;
				.add("statistics", statistics) // ;
				.toString();
	}
}
