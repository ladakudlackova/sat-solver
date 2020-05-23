package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Report {

	private static final String[] ALGS 		= new String[]{"DPLL:", "DPLL with watched literals:"};
	private static final String AVG_VALUES 	= "Average values (";
	private static final String RUNS 		= " runs):";
	private static final String VARIABLES	= "variables:";	
	private static final String CLAUSES 	= "clauses:";
	private static final String TIME 		= "time [ms]:";
	private static final String DECISIONS 	= "decisions:";
	private static final String UP_STEPS 	= "UP steps:";
	
	
	
	@SuppressWarnings("unchecked")
	private Map<Integer,RunInfo>[] totalRunInfo = new HashMap[2]; 

	
	public Report() {
		
		totalRunInfo[0]=new HashMap<Integer, RunInfo>();	// DPLL
		totalRunInfo[1]=new HashMap<Integer, RunInfo>();	// DPLL with watched literals
	}
	
	protected void addRunInfo(RunInfo other) {
		
		int variablesCount = other.variablesCount;
		int index = (other.withWatchedLiterals)? 1 : 0;
		RunInfo runInfo = totalRunInfo[index].get(variablesCount);
		if (runInfo==null)
			totalRunInfo[index].put(variablesCount, other);
		else
			runInfo.sum(other);
	}
	
	protected void print(String outputFileName, int inputCount) {
		
		File outputFile = new File(outputFileName);
		try {
			FileWriter output = new FileWriter(outputFile);
			inputCount=inputCount/totalRunInfo[0].size();
			output.write(AVG_VALUES+inputCount+RUNS+System.lineSeparator());
			printTotalRunInfo(output, 0);
			printTotalRunInfo(output, 1);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void printTotalRunInfo(FileWriter output, int index) throws IOException {
		
		printHeader(output, index);
		StringBuilder info = new StringBuilder();
		Set<Integer> keys = totalRunInfo[index].keySet();
		Iterator<Integer> orderedKeys = keys.stream().sorted().iterator();
		while (orderedKeys.hasNext()) {
			Integer key = orderedKeys.next();
			RunInfo runInfo = totalRunInfo[index].get(key);
			runInfo.setAverageValues();
			append(info, runInfo.variablesCount);
			append(info, runInfo.clausesCount);
			append(info, runInfo.timeElapsed);
			append(info, runInfo.decisionCount);
			append(info, runInfo.unitPropagationSteps);
			appendSeparator(info, 1);
		}
		output.write(info.toString());
	}
	
	private void printHeader(FileWriter output, int index) throws IOException {
		
		StringBuilder header = new StringBuilder();
		appendSeparator(header, 2);
		header.append(ALGS[index]);
		appendSeparator(header, 2);
		append(header, VARIABLES);
		append(header, CLAUSES);
		append(header, TIME);
		append(header, DECISIONS);
		append(header, UP_STEPS);
		appendSeparator(header, 1);
		
		output.write(header.toString());
	}
	
	private void append(StringBuilder line, String word) {		
		line.append(String.format("%-20s", word));
	}
	
	private void append(StringBuilder line, long number) {		
		line.append(String.format("%-20s", number));
	}
	
	private void appendSeparator(StringBuilder line, int count) {
		
		for (int i=0;i<count;i++)
			line.append(System.lineSeparator());
	}
}