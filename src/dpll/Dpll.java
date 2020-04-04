package dpll;

import java.io.File;
import java.nio.file.Paths;

import tseitin.Assignment;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

public class Dpll {

	private static final String DIMACS_EXT =".cnf";
	private static final String SMT_LIB =".sat";
	
	private static final File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "input","toy_5.sat").toFile();
	
	public static void main(String[] args) {
		
		//args[0]
		DimacsCNF dimacsCNF = createDimacsCNF(DATA_INPUT_FOLDER.getPath().toString());
		if (dimacsCNF==null)
			return;	
	}
	
	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT)) 
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;			
	}
	
	public static Assignment[] dpll(DimacsCNF dimacsCNF) {
		return null;
		
	}
	
	
}
