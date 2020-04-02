package tseitin_to_dimacs;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import simple_nnf_tree.DerivationTree;
import tseitin.CNFDefinition.implicationType;
import tseitin.Encoding;

public class Translation {

	private static final String NOT_NNF = "Input string is not nnf.";	
	private static boolean valid = true;	

	public static void formula2cnf(boolean bothImplications){
		translateAndPrint(bothImplications, new InputStreamReader(System.in), System.out);
	}
	
	public static void formula2cnf(boolean bothImplications, String inputFileName){
		Reader r = IOUtils.getReader(inputFileName, valid);
		translateAndPrint(bothImplications, r, System.out);
	}
	
	public static void formula2cnf(boolean bothImplications, String inputFileName, String outputFileName){
		Reader r = IOUtils.getReader(inputFileName, valid);
		FileOutputStream fos = IOUtils.createOutputFile(outputFileName, valid);
		translateAndPrint(bothImplications, r, fos);
	}
	
	
	private static void translateAndPrint(boolean bothImplications, Reader r, OutputStream os) {
		if (valid) {
			String cnf = translate(r, bothImplications);
			if (valid)
				IOUtils.print(os, cnf, valid);
		}
	}
	
	private static String translate(Reader r, boolean bothImplications) {
		
		implicationType implType = bothImplications? implicationType.EQUIVALENCE : implicationType.LEFT_TO_RIGHT;
		DerivationTree tree = new DerivationTree(r);
		String dimacsCNF=null;
		valid=tree.validNNF();
		if (valid) {
			Encoding tseitinEncoding = new Encoding();
			tseitinEncoding.encode(tree, implType);
			dimacsCNF = DimacsCNF.getDimacsCNF(
					tseitinEncoding.getClauses(), tree.getNNFVariables(), tseitinEncoding.getTseitinVarCount());
		}
		else {
			System.out.println(NOT_NNF);
		}
		return dimacsCNF;
	}
}