package dpll;

import java.util.List;

import utils.DimacsFileUtils;

public class Solver {

	private static final String DIMACS_EXT =".cnf";
	private static final String SMT_LIB =".sat";
	
	public static void dpll(String inputFileName) {
		List<Integer[]> clauses = DimacsFileUtils.processDimacsFile(inputFileName);
	}
}
