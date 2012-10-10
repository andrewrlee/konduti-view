package uk.co.optimisticpanda.storybuilder;

public interface DebugReporter {

	public abstract void reportStepMessage(String message);

	public abstract void reportStepImage(String path);
	
	public abstract void reportStepPageDump(String dump);

	public abstract void reportStepError(String error);

}