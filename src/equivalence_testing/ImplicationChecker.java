package equivalence_testing;

import java.io.BufferedReader;
import java.io.StringReader;

import dimacs.DimacsCNF;
import dimacs.DimacsFileUtils;
import dpll.Solver;
import simple_nnf_tree.Translation;

public class ImplicationChecker implements Runnable {
	private Thread t;
	private String threadName;

	DimacsCNF phi;
	DimacsCNF psi;
	Boolean[] result;

	String inputFileName0;
	String inputFileName1;

	public ImplicationChecker(String inputFileName0, String inputFileName1) {
		this.inputFileName0 = inputFileName0;
		this.inputFileName1 = inputFileName1;
	}

	public void run() {
		phi = DimacsFileUtils.processDimacsFile(inputFileName0, false);
		psi = DimacsFileUtils.processDimacsFile(inputFileName1, false);
		String nnf = Translation.translate2PhiAndNotPsi(phi, psi);
		System.out.println(nnf);
		//DimacsCNF dimacsCNF = tseitin_to_dimacs.Translation.formula2dimacsCNF(new StringReader(nnf), true);
		String cnf  = tseitin_to_dimacs.Translation.formula2cnfString(nnf);
		System.out.println(cnf);
		DimacsCNF dimacsCNF = DimacsFileUtils.createDimacsCNF(
				new BufferedReader(new StringReader(cnf)), true) ;//Translation.formula2cnf(false, fileEntry.getPath(), outputFileName);
		result = Solver.solve(dimacsCNF, true);
		if (result != null)
			System.out.println("SAT");
		else
			System.out.println("UNSAT");

	}

	public void start() {
		System.out.println("Starting ");
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

}
