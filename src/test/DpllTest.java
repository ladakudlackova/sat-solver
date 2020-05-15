package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import dpll.Solver;


public class DpllTest {

	private static final Path SAT_CNF_FOLDER 
		= Paths.get("src", "test", "data", "input", "task_2", "sat");
	
	private static final Path UNSAT_CNF_FOLDER 
	= Paths.get("src", "test", "data", "input", "task_2","unsat");
	
	@Test
	public void testSolver() {       
		
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
	

	
}
