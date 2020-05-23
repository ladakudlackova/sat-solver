package utils;

public class RunInfo {

	protected final boolean withWatchedLiterals;
	protected final int variablesCount;
	protected final int clausesCount;
	protected int decisionCount;
	protected int unitPropagationSteps;
	protected long timeElapsed;
	
	protected int runsCount=1;
	
	public RunInfo(boolean withWatchedLiterals, int variablesCount, int clausesCount, 
			int decisionCount, int unitPropagationSteps, long timeElapsed) {
		
		this.withWatchedLiterals = withWatchedLiterals;
		this.variablesCount = variablesCount;
		this.clausesCount = clausesCount;
		this.decisionCount = decisionCount;
		this.unitPropagationSteps = unitPropagationSteps;
		this.timeElapsed = timeElapsed;
	}
	
	protected void sum(RunInfo other) {
		
		decisionCount = decisionCount + other.decisionCount;
		unitPropagationSteps = decisionCount + other.unitPropagationSteps;
		timeElapsed = timeElapsed + other.timeElapsed;
		runsCount++;
	}
	
	protected void setAverageValues() {
		
		decisionCount = decisionCount/runsCount;
		unitPropagationSteps = unitPropagationSteps/runsCount;
		timeElapsed = timeElapsed/runsCount;
		runsCount=1;
	}
	
	public void print() {
		
		System.out.println(timeElapsed + " ms");
		System.out.println("Number of decisions: " + decisionCount);
		System.out.println("Number of unit propagation steps: " + unitPropagationSteps);
		System.out.println();
	}
}
