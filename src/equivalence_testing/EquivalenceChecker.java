package equivalence_testing;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Paths;
import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Solver;
import simple_nnf_tree.Translation;
import tseitin.TseitinVariableToken;

public class EquivalenceChecker implements Runnable {

	 private Thread t;
	 private String threadName;
	 String inputFileName0;
	 String inputFileName1;
	 
	public static void main(String[] args) {
		File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "output", "task_1").toFile();
		checkEquivalence(DATA_INPUT_FOLDER+"\\toy_50.cnf", DATA_INPUT_FOLDER+"\\toy_501.cnf");
    /*    EquivalenceChecker eq=new EquivalenceChecker(
        		DATA_INPUT_FOLDER+"\\toy_50.cnf", DATA_INPUT_FOLDER+"\\toy_501.cnf");
        eq.run();
        EquivalenceChecker eq2=new EquivalenceChecker(
        		DATA_INPUT_FOLDER+"\\toy_501.cnf", DATA_INPUT_FOLDER+"\\toy_50.cnf");
        eq2.run();
        */
	}
	
	public EquivalenceChecker(String inputFileName0, String inputFileName1) {
		this.inputFileName0=inputFileName0;
		this.inputFileName1=inputFileName1;
	}
	
	public static void checkEquivalence(String inputFileName0, String inputFileName1) {
		
	//	DimacsCNF psi = DimacsFileUtils.processDimacsFile(inputFileName0, false);
	//	DimacsCNF phi = DimacsFileUtils.processDimacsFile(inputFileName1, false); 
		ImplicationChecker c1 = new ImplicationChecker(inputFileName0, inputFileName1);
		c1.run();
		System.out.println(c1.result==null);
	//	c1.run();
	//	System.out.println(c1.result==null);
		ImplicationChecker c2 = new ImplicationChecker(inputFileName1, inputFileName0);
		c2.run();
		System.out.println(c2.result==null);
	//	DimacsCNF psi = c1.psi;
	//	DimacsCNF phi = c1.phi;
		//	DimacsCNF phi 
		
		boolean[] implications=new boolean[] {
				//checkImplication(psi, psi),
				//checkImplication(psi, psi),
				//checkImplication(phi, phi),
				//checkImplication(phi, phi),
				
				c1.result==null,
				c2.result==null,
		//		checkImplication(psi, psi)
				
		};
		System.out.println(implications[0]+" "+ implications[1]);
	}
	
	private static boolean checkImplication(DimacsCNF phi, DimacsCNF psi) {
		
		String nnf=Translation.translate2PhiAndNotPsi(phi, psi);
		System.out.println(nnf);
		DimacsCNF dimacsCNF = tseitin_to_dimacs.Translation.formula2dimacsCNF(new StringReader(nnf),true);
		//dimacsCNF.getClauses().
		Boolean[] result = Solver.solve(dimacsCNF, true);
		if (result!=null)
			System.out.println("SAT");
		else 
			System.out.println("UNSAT");
		return result==null;
	}

	@Override
	public void run() {
		checkEquivalence(inputFileName0, inputFileName1);
		
	}
	public void start () {
	      System.out.println("Starting " );
	      if (t == null) {
	         t = new Thread (this);
	         t.start ();
	      }
	   }
    
}
