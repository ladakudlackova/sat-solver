package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import org.junit.jupiter.api.Test;

import dimacs.DimacsCNF;
import dpll.Solver;
import tseitin.Assignment;
import utils.DimacsFileUtils;


public class DpllTest {

	private static final Path SAT_FOLDER 
		= Paths.get("src", "test", "data", "input", "task_1");
	
	private static final Path SAT_CNF_FOLDER 
		= Paths.get("src", "test", "data", "input", "task_2", "sat");
	
	private static final Path UNSAT_CNF_FOLDER 
	= Paths.get("src", "test", "data", "input", "task_2","unsat");
	
	@Test
	public void testSolver() {       
		
		testSolverInputFiles(SAT_FOLDER.toFile(), true);
		testSolverInputFiles(SAT_CNF_FOLDER.toFile(), true);
		testSolverInputFiles(UNSAT_CNF_FOLDER.toFile(), false);
		
	}
	
	private void testSolverInputFiles(File folder, Boolean sat) {
		for (final File fileEntry : folder.listFiles()) {
			try {
				if (fileEntry.isFile()) {
					String inputFileName = fileEntry.getPath();
					Boolean[] assignment = Solver.solve(inputFileName);
					if (sat)
						assertNotNull(assignment);
					else
						assertNull(assignment);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				fail(ex.getMessage());      				 				
			}
		}
	}
	

	@Test
	public void testDpll() {       
		
		testDpllInputFiles(SAT_CNF_FOLDER.toFile(), true);
		testDpllInputFiles(UNSAT_CNF_FOLDER.toFile(), false);
		
	}
	
	private void testDpllInputFiles(File folder, Boolean sat) {
		for (final File fileEntry : folder.listFiles()) {
			try {
				if (fileEntry.isFile()) {
					String inputFileName = fileEntry.getPath();
					//checkAssignment(inputFileName, sat);
				}
			} catch (Exception ex) {
				fail(ex.getMessage());      				 				
			}
		}
	}
	
	
	
/*	private void checkAssignment(String inputFileName, Boolean sat) {
		
		DimacsCNF dimacsCNF = DimacsFileUtils.processDimacsFile(inputFileName) ;
		Boolean[] assignment = Solver.solve(inputFileName);
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
	*/
}
