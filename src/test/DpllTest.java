package test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
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

	
	
	private static final Path SAT_CNF_FOLDER 
		= Paths.get("src", "test", "data", "input", "task_2", "sat");
	private static final Path UNSAT_CNF_FOLDER 
	= Paths.get("src", "test", "data", "input", "task_2","unsat");

	@Test
	@Order(1)
	public void testDpllSolver() {       
		
		testInputFiles(SAT_CNF_FOLDER.toFile(), true);
		testInputFiles(UNSAT_CNF_FOLDER.toFile(), false);
		
	}
	
	private void testInputFiles(File folder, Boolean sat) {
		for (final File fileEntry : folder.listFiles()) {
			try {
				if (fileEntry.isFile()) {
					String inputFileName = fileEntry.getPath();
					checkAssignment(inputFileName, sat);
				}
			} catch (Exception ex) {
				//ex.printStackTrace();
				System.out.println(ex.getMessage());
				fail(ex.getMessage());      				 				
			}
		}
	}
	
	
	
	private void checkAssignment(String inputFileName, Boolean sat) {
		
		DimacsCNF dimacsCNF = DimacsFileUtils.processDimacsFile(inputFileName) ;
		Boolean[] assignment = Dpll.solve(inputFileName);
		if (!sat) {
			assertNull(assignment);  
			return;
		}
		boolean satClauses = true;
		for (ArrayList<Assignment> clause:dimacsCNF.getClauses()) {
			satClauses = false;
			for (Assignment a:clause) {
				if (a.getValue()==assignment[a.getVariable().getIndex()]) {
					satClauses = true;
					break;
				}					
			}
			if (!satClauses)
				break;
		}
		assertTrue(satClauses);
	}
}
