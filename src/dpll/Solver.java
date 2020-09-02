package dpll;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Collection;

import clauses_base.ClausesBase;
import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.Translation;
import utils.RunInfo;
import watched_literals.ClausesWithWatches;

public class Solver {

	private static final String DIMACS_EXT = ".cnf";
	private static final String SMT_LIB = ".sat";
	private static final String SAT = "SAT";
	private static final String UNSAT = "UNSAT";
	private static RunInfo runInfo;

	public static void main(String[] args) {
		solve(args[0], false);
	}

	public static Boolean[] solve(String inputFileName, boolean withWatchedLiterals) {

		long start = System.currentTimeMillis();
		DimacsCNF dimacsCNF = createDimacsCNF(inputFileName, withWatchedLiterals);
		if (dimacsCNF != null) 
		{
			ClausesBase clauses = dimacsCNF.getClauses();
			Dpll dpll = new Dpll(dimacsCNF.getVariables(), (ClausesWithWatches)clauses);
			Boolean[] assignment = dpll.solveClauses();
			long finish = System.currentTimeMillis();
			runInfo=new RunInfo(
					withWatchedLiterals,
					dimacsCNF.getVariables().length-1, dimacsCNF.getClausesCount(),
					dpll.getDecisionCount(), dpll.getUnitPropagationSteps(), finish - start);
			printResult(assignment, dimacsCNF);
			return assignment;
		}
		return null;
	}
	
	public static Boolean[] solve(DimacsCNF dimacsCNF, boolean withWatchedLiterals) {

		ClausesBase clauses = dimacsCNF.getClauses();
		Dpll dpll = new Dpll((ClausesWithWatches)clauses, dimacsCNF.getVariables());
		return dpll.solveClauses();
	}
	

	private static DimacsCNF createDimacsCNF(String inputFileName, boolean watchedLiterals) {

		if (inputFileName.endsWith(DIMACS_EXT))
			return DimacsFileUtils.processDimacsFile(inputFileName, watchedLiterals);
		if (inputFileName.endsWith(SMT_LIB))
			return Translation.formula2dimacsCNF(false, inputFileName, watchedLiterals);
		return null;
	}

	private static void printResult(Boolean[] assignment, DimacsCNF dimacsCNF) {

		if (assignment == null)
			System.out.println(UNSAT);
		else {
			System.out.println(SAT);
			Collection<SimpleNNFVariableToken> nnfVars = dimacsCNF.getNNFVars();
			if (nnfVars == null)
				printAssignment(assignment);
			else
				printNnfVars(assignment, nnfVars);
		}
		runInfo.print();
		
	}

	private static void printAssignment(Boolean[] assignment) {

		for (int i = 1; i < assignment.length; i++) {
			if (assignment[i])
				System.out.print(i + " ");
			else
				System.out.print("-" + i + " ");
		}
		System.out.println();
	}

	private static void printNnfVars(Boolean[] assignment, 
			Collection<SimpleNNFVariableToken> nnfVars) {

		for (SimpleNNFVariableToken nnfVar : nnfVars) {
			TseitinVariableToken var = nnfVar.getTseitinVar();
			int index = var.getIndex();
			if (assignment[index])
				System.out.print(var.getToken() + " ");
			else
				System.out.print("-" + var.getToken() + " ");
		}
		System.out.println();
	}

	public static RunInfo getRunInfo() {
		return runInfo;
	}
}
