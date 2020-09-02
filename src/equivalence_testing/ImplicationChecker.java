package equivalence_testing;

import java.io.BufferedReader;
import java.io.StringReader;

import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Clause;
import dpll.Clauses;
import dpll.Solver;
import simple_nnf_tree.SimpleNNFOperatorToken.operators;
import simple_nnf_tree.Translation;

public class ImplicationChecker {
	
	public static Boolean checkImplicationSingleFormula(DimacsCNF phi, DimacsCNF psi) {
		
		String nnf = Translation.translate2PhiAndNotPsi(phi, psi);
		//System.out.println(nnf);
		String cnf  = tseitin_to_dimacs.Translation.formula2cnfString(nnf);
		//System.out.println(cnf);
		DimacsCNF dimacsCNF = DimacsFileUtils.createDimacsCNF(
				new BufferedReader(new StringReader(cnf)), true);//Translation.formula2cnf(false, fileEntry.getPath(), outputFileName);
		Boolean[] assignment = Solver.solve(dimacsCNF, true);
		if (assignment != null)
			System.out.println("SAT");
		else
			System.out.println("UNSAT");
		return assignment==null;
	}

	public static Boolean checkImplicationFormulasDisjunction(DimacsCNF phi, DimacsCNF psi) {
		
		String nnfPhi = Translation.translateClauses(
				((Clauses)phi.getClauses()).getAllClauses(), operators.AND, operators.OR);
		
		for (Clause clause:((Clauses)psi.getClauses()).getAllClauses()) {
			Clause clauseNegation = clause.negateLiterals();
			///....................................................................................
			
			String nnfClauseNegation = Translation.translate2NotPhi(psi);
			String nnf = Translation.join(nnfPhi, nnfClauseNegation, operators.AND);
			String cnf  = tseitin_to_dimacs.Translation.formula2cnfString(nnf);
			DimacsCNF dimacsCNF = DimacsFileUtils.createDimacsCNF(
					new BufferedReader(new StringReader(cnf)), true);//Translation.formula2cnf(false, fileEntry.getPath(), outputFileName);
			Boolean[] assignment = Solver.solve(dimacsCNF, true);
			if (assignment == null)
				return true;
		}
		return false;
		
		
	}


}
