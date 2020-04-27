package dpll;

import java.util.Collection;

import dimacs.Clauses;
import dimacs.DimacsCNF;
import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

//	TODO: 	equality on clauses; stack instead of recursion
//			organize project 

public class Solver {

	private static final String DIMACS_EXT = ".cnf";
	private static final String SMT_LIB = ".sat";
	private static final String SAT = "SAT";
	private static final String UNSAT = "UNSAT";
	
	public static void main(String[] args) {
		solve(args[0]);
	}

	public static Boolean[] solve(String inputFileName) {
		long start = System.currentTimeMillis();
		DimacsCNF dimacsCNF = createDimacsCNF(inputFileName);
		if (dimacsCNF != null) {
			Clauses clauses = dimacsCNF.getClauses();
			
			//ArrayList<ArrayList<Assignment>>[] variableClausesEdges = dimacsCNF.createVariableClausesEdges();
			Dpll dpll = new Dpll(dimacsCNF.getVariables(), clauses);
			Boolean[] assignment = dpll.solveXXX();
			long finish = System.currentTimeMillis();
			printResult(assignment, dimacsCNF, finish - start,
					dpll.getDecisionCount(), dpll.getUnitPropagationSteps());
			return assignment;
		}
		return null;
	}
	
	private static DimacsCNF createDimacsCNF(String inputFileName) {
		
		if (inputFileName.endsWith(DIMACS_EXT))
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;
	}
	
	private static void printResult(Boolean[] assignment, DimacsCNF dimacsCNF, 
			long timeElapsed, int decisionCount, int unitPropagationSteps) {
		
		if (assignment==null)
			System.out.println(UNSAT);
		else {
			System.out.println(SAT);
			Collection<SimpleNNFVariableToken> nnfVars = dimacsCNF.getNNFVars();
			if (nnfVars==null)
				for (int i=1;i<assignment.length;i++) {
					if (assignment[i])
						System.out.print(i+" ");
					else 
						System.out.print("-"+i+" ");
				}
					
			else {
				for (SimpleNNFVariableToken nnfVar:nnfVars) {
					TseitinVariableToken var = nnfVar.getTseitinVar();
					int index = var.getIndex();
					if (assignment[index])
						System.out.print(var.getToken()+" ");
					else 
						System.out.print("-"+var.getToken()+" ");
				}
			}
			System.out.println();
		}
		System.out.println(timeElapsed+" ms");
		System.out.println("Number of decisions: "+decisionCount);
		System.out.println("Number of unit propagation steps: "+unitPropagationSteps);
	}

}
