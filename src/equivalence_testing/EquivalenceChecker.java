package equivalence_testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Paths;

import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Clauses;
import dpll.Solver;
import simple_nnf_tree.Translation;

public class EquivalenceChecker {

	public static void main(String[] args) {
		File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "output", "task_1").toFile();
		checkEquivalence(DATA_INPUT_FOLDER+"\\nested_5.cnf", DATA_INPUT_FOLDER+"\\nested_8.cnf");

	}
	
	public static void checkEquivalence(String inputFileName0, String inputFileName1) {
		
		DimacsCNF phi = DimacsFileUtils.processDimacsFile(inputFileName0, false);
		DimacsCNF psi = DimacsFileUtils.processDimacsFile(inputFileName1, false);   
		boolean[] implications=new boolean[] {
				checkImplication(phi, psi),
				checkImplication(psi, phi)
		};
		System.out.println(implications);
	}
	
	private static boolean checkImplication(DimacsCNF phi, DimacsCNF psi) {
		
		String nnf=Translation.translate2PhiAndNotPsi(phi, psi);
		DimacsCNF dimacsCNF = tseitin_to_dimacs.Translation.formula2dimacsCNF(new StringReader(nnf));
		Boolean[] result = Solver.solve(dimacsCNF, true);
		return result!=null;
	}
	

}
