package uk.co.optimisticpanda.storybuilder;

import static uk.co.optimisticpanda.storybuilder.Info.Type.ERROR;
import static uk.co.optimisticpanda.storybuilder.Info.Type.IMAGE;
import static uk.co.optimisticpanda.storybuilder.Info.Type.MESSAGE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class Step {

	public static enum Result {
		SUCCESS("resultSuccess"), FAILED("resultFailure"), CURRENT("resultCurrent"), NOT_PERFORMED("resultNotPerformed"), PENDING("resultPending");
		private final String style;

		private Result(String hexColourCode) {
			this.style = hexColourCode;
		}

		public String getColour() {
			return style;
		}

		public Map<String, String> asMap() {
			return ImmutableMap.of("name",this.name(),"style",style);
		}
	}

	private String stepName;
	private Map<String, String> result;
	private String duration;
	private String cause;
	private List<Info> messages = Lists.newArrayList();
	
	public Step(String stepName) {
		this.stepName = stepName;
		this.result = Result.CURRENT.asMap();
	}

	public static Step notPerformedStep(String stepName) {
		Step step = new Step(stepName);
		step.result = Result.NOT_PERFORMED.asMap();
		return step;
	}

	public static Step notPendingStep(String stepName) {
		Step step = new Step(stepName);
		step.result = Result.PENDING.asMap();
		return step;
	}

	public Result getResultAsEnum() {
		return Result.valueOf(result.get("name"));
	}

	public void setResult(Result result) {
		this.result = result.asMap();
	}

	public void setStepName(String story) {
		this.stepName = story;
	}

	public void addMessage(String message){
		messages.add(new Info(MESSAGE, message));
	}
	
	public void addImage(String path){
		messages.add(new Info(IMAGE, path));
	}
	
	public void addError(String error){
		messages.add(new Info(ERROR, error));
	}
	
	public void setDuration(long duration) {
		this.duration = String.valueOf(duration / 1000)+ "." + (duration % 1000) +"s";
	}

	public void setCause(Throwable cause) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		cause.printStackTrace(pw);
		this.cause = writer.toString();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(Step.class) //
				.add("stepName" , stepName) //
				.add("result" , result) //
				.add("duration" , duration) //
				.add("cause" , cause).toString(); //
	}
}
