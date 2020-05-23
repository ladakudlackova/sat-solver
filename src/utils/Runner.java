package utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import dpll.Solver;

public class Runner {

	private Path inputFolderPath = Paths.get("src", "test", "data", "input",  "task_3");
	private Path outputFilePath = Paths.get("src", "test", "data", "output", "task_3", "report.txt");
	
	private Report report = new Report();
	private int inputCount;
	
	public static void main(String[] args) {
		
		Runner runner = new Runner(args);
		runner.runAll();
		runner.printReport();
	}
	
	public Runner(String[] args) {
		
		if (args!=null) {
			if (args.length>0) {
				inputFolderPath =  Paths.get(args[0]);
				if (args.length>1)
					outputFilePath =  Paths.get(args[1]);
			}
		}
	}
	
	
	protected void runAll() {
		
		File inputFolder = inputFolderPath.toFile();
		for (final File inputFile : inputFolder.listFiles()) {
			inputCount++;
			run(inputFile, true);
			run(inputFile, false);
		}
	}
	
	private void run(File inputFile, Boolean withWatchedLiterals) {
		
		Solver.solve(inputFile.getPath(), withWatchedLiterals);
		report.addRunInfo(Solver.getRunInfo());
	}
	
	private void printReport() {
		report.print(outputFilePath.toString(), inputCount);
	}
}
