package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import dpll.Solver;

public class Report {

	private Path inputFolderPath = Paths.get("src", "test", "data", "input",  "task_3");
	private Path outputFilePath = Paths.get("src", "test", "data", "output", "task_3", "report.txt");
	
	private Map<Integer,RunInfo> totalRunInfo = new HashMap<>();
	
	
	public static void main(String[] args) {
		
		Report report = new Report(args);
		report.runAll();
		report.print();
	}
	
	
	public Report(String[] args) {
		
		if (args!=null) {
			if (args.length>0) {
				inputFolderPath =  Paths.get(args[0]);
				if (args.length>1)
					outputFilePath =  Paths.get(args[1]);
			}
		}
	}
	
	private void runAll() {
		
		File inputFolder = inputFolderPath.toFile();
		for (final File inputFile : inputFolder.listFiles()) {
			run(inputFile, true);
			run(inputFile, false);
		}
	}
	
	private void run(File inputFile, Boolean withWatchedLiterals) {
		
		Solver.solve(inputFile.getPath(), withWatchedLiterals);
		addRunInfo(Solver.getRunInfo());
	}
	
	private void addRunInfo(RunInfo other) {
		
		int variablesCount = other.variablesCount;
		RunInfo runInfo = totalRunInfo.get(variablesCount);
		if (runInfo==null)
			totalRunInfo.put(variablesCount, other);
		else
			runInfo.sum(other);
	}
	
	private void print() {
		
		File outputFile = new File(outputFilePath.toString());
		try {
			FileWriter output = new FileWriter(outputFile);
			for (RunInfo runInfo:totalRunInfo.values()) {
				runInfo.setAverageValues();
				// ...
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
