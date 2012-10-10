package uk.co.optimisticpanda.storybuilder;

import com.google.common.base.Objects;

public class Statistics {

	private int failures;
	private int successes;
	private int pending;
	private int notRun;
	private int total;
	private int failuresPercentage;
	private int successesPercentage;
	private int pendingPercentage;
	private int notRunPercentage;

	public Statistics() {
	}

	public Statistics(int failures, int successes, int pending, int notRun, int total) {
		this.failures = failures;
		this.successes = successes;
		this.pending = pending;
		this.notRun = notRun;
		this.total = total;
		this.failuresPercentage = percentageOfTotal(failures);
		this.successesPercentage = percentageOfTotal(successes);
		this.pendingPercentage = percentageOfTotal(pending);
		this.notRunPercentage =  percentageOfTotal(notRun);
	}

	public void setFailures(int failures) {
		this.failures = failures;
	}

	public void setSuccesses(int successes) {
		this.successes = successes;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}

	public void setNotRun(int notRun) {
		this.notRun = notRun;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void addStats(Statistics stats){
		this.setFailures(failures + stats.failures);
		this.setNotRun(	notRun+ stats.notRun);
		this.setPending(pending+ stats.pending);
		this.setSuccesses(successes+ stats.successes);
		this.setTotal(total+ stats.total);
		this.failuresPercentage = percentageOfTotal(failures);
		this.successesPercentage = percentageOfTotal(successes);
		this.pendingPercentage = percentageOfTotal(pending);
		this.notRunPercentage =  percentageOfTotal(notRun);
	}

	private int percentageOfTotal(int value){
		return (int)(((double) value ) / total * 100);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(Statistics.class) // 
				.add("failures", failures) // 
				.add("successes", successes) // 
				.add("pending", pending) // 
				.add("notRun", notRun) // 
				.add("total", total) // 
				.add("failuresPercentage", failuresPercentage) // 
				.add("successesPercentage", successesPercentage) // 
				.add("pendingPercentage", pendingPercentage) // 
				.add("notRunPercentage", notRunPercentage).toString(); // 
	}
	
}
