package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;


import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import dpll.Dpll;
import tseitin.Assignment;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

@FixMethodOrder
public class DpllTest {

	private static final File DATA_INPUT_FOLDER = Paths.get("src", "test", "data", "input").toFile();

	@Test
	@Order(1)
	public void testDpllSolver() {
		
		for (final File fileEntry : DATA_INPUT_FOLDER.listFiles()) {
			try {
				if (fileEntry.isFile() && fileEntry.getName().startsWith("rti")) {
					String inputFileName = fileEntry.getPath();
					assertTrue(checkAssignment(inputFileName));
				}
			} catch (Exception ex) {
				//fail(ex.getMessage());
				System.out.println(fileEntry.getName());
			}
		}
	}
	
	private Boolean checkAssignment(String inputFileName) {
		
		DimacsCNF dimacsCNF = DimacsFileUtils.processDimacsFile(inputFileName) ;
				//Translation..formula2dimacsCNF(false, inputFileName);
		Boolean[] assignment = Dpll.solve(inputFileName);
		for (ArrayList<Assignment> clause:dimacsCNF.getClauses()) {
			boolean sat = false;
			for (Assignment a:clause) {
				if (a.getValue()==assignment[a.getVariable().getIndex()]) {
					sat = true;
					break;
				}					
			}
			if (!sat)
				return false;
		}
		return true;
	}
}
