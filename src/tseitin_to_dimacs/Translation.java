package tseitin_to_dimacs;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

import simple_nnf_tree.DerivationTree;
import tseitin.CNFDefinition.implicationType;
import utils.IOUtils;
import tseitin.Encoding;

public class Translation {

	private static final String NOT_NNF = "Input string is not nnf.";	
	private static boolean valid = true;	
	private static Encoding tseitinEncoding;

	public static void formula2cnf(boolean bothImplications){
		translateAndPrint(bothImplications, new InputStreamReader(System.in), System.out);
	}
	
	public static void formula2cnf(boolean bothImplications, String inputFileName){
		Reader r = IOUtils.createReader(inputFileName);
		translateAndPrint(bothImplications, r, System.out);
	}
	
	public static void formula2cnf(boolean bothImplications, String inputFileName, String outputFileName){
		Reader r = IOUtils.createReader(inputFileName);
		FileOutputStream fos = IOUtils.createOutputFile(outputFileName);
		translateAndPrint(bothImplications, r, fos);
	}
	
	public static DimacsCNF formula2dimacsCNF(boolean bothImplications, String inputFileName){
		Reader r = IOUtils.createReader(inputFileName);
		return translate(r, bothImplications);
	}
	
	
	private static void translateAndPrint(boolean bothImplications, Reader r, OutputStream os) {
		valid = (r!=null && os!=null);
		if (valid) {
			DimacsCNF dimacsCNF = translate(r, bothImplications);
			String cnf=dimacsCNF.toString();
			if (valid)
				IOUtils.print(os, cnf, valid);
		}
	}
	
	private static DimacsCNF translate(Reader r, boolean bothImplications) {
		
		implicationType implType = bothImplications? implicationType.EQUIVALENCE : implicationType.LEFT_TO_RIGHT;
		DerivationTree tree = new DerivationTree(r);
		valid=tree.validNNF();
		if (valid) {
			tseitinEncoding = new Encoding();
			tseitinEncoding.encode(tree, implType);
			return new DimacsCNF(
					tseitinEncoding.getClauses(), tree.getNNFVariables(), tseitinEncoding.getTseitinVariables());
		}
		System.out.println(NOT_NNF);
		return null;
	}
}