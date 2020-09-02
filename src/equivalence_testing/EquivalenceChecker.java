package equivalence_testing;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Paths;
import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Solver;
import simple_nnf_tree.Translation;
import tseitin.TseitinVariableToken;

public class EquivalenceChecker {

	// String inputFileName0;
	// String inputFileName1;
	 
	public static void main(String[] args) {
		File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "output", "task_1").toFile();
		args=new String[2];
		args[0]=DATA_INPUT_FOLDER+"\\toy_50.cnf";
		args[1]=DATA_INPUT_FOLDER+"\\toy_501.cnf";
		DimacsCNF phi = DimacsFileUtils.processDimacsFile(args[0], false);
		DimacsCNF psi = DimacsFileUtils.processDimacsFile(args[1], false); 
		checkEquivalenceSingleFormula(phi, psi);
    
	}
	
/*	public EquivalenceChecker(String inputFileName0, String inputFileName1) {
		this.inputFileName0=inputFileName0;
		this.inputFileName1=inputFileName1;
	}
*/	
	public static void checkEquivalenceSingleFormula(DimacsCNF phi, DimacsCNF psi) {
		
		
		boolean[] implications=new boolean[] {
				ImplicationChecker.checkImplicationSingleFormula(phi, psi),
				ImplicationChecker.checkImplicationSingleFormula(psi, phi)
		};
	}
	
	
}
