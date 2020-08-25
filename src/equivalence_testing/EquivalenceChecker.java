package equivalence_testing;

import java.io.File;
import java.nio.file.Paths;

import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Clauses;
import simple_nnf_tree.Translation;

public class EquivalenceChecker {

	public static void main(String[] args) {
		File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "output", "task_1").toFile();
		checkEquivalence(DATA_INPUT_FOLDER+"\\toy_6.cnf", DATA_INPUT_FOLDER+"\\toy_7.cnf");

	}
	
	public static void checkEquivalence(String inputFileName0, String inputFileName1) {
		
		boolean[] implications=new boolean[2];
		DimacsCNF phi = DimacsFileUtils.processDimacsFile(inputFileName0, false);
		DimacsCNF psi = DimacsFileUtils.processDimacsFile(inputFileName1, false);   // watched lit!!!!!!
		String nnf=Translation.translate2PhiAndNotPsi(phi, psi);
		System.out.println(nnf);
	}
	
	private static void checkImplication(String inputFileName0, String inputFileName1) {
		
		boolean[] implications=new boolean[2];
		DimacsCNF cnf0 = DimacsFileUtils.processDimacsFile(inputFileName0);
		DimacsCNF cnf1 = DimacsFileUtils.processDimacsFile(inputFileName1);
	}
	

}
