package tseitin_to_dimacs;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Paths;

import simple_nnf_tree.DerivationTree;
import tseitin.Encoding;

public class Translation {

	private static final String NOT_NNF = "Input string is not nnf.";
	
	private static boolean valid = true;
	
	public static void main(String[] args) {
		String in  = Paths.get("inputFiles",  "toy_5.sat").toString();
		String out = Paths.get("outputFiles", "toy_5.dimacs").toString();
		formula2cnf(in,out);
	}	

	public static void formula2cnf(){
		translateAndPrint(new InputStreamReader(System.in), System.out);
	}
	
	public static void formula2cnf(String inputFileName){
		Reader r = IOUtils.getReader(inputFileName, valid);
		translateAndPrint(r, System.out);
	}
	
	public static void formula2cnf(String inputFileName, String outputFileName){
		Reader r = IOUtils.getReader(inputFileName, valid);
		FileOutputStream fos = IOUtils.createOutputFile(outputFileName, valid);
		translateAndPrint(r, fos);
	}
	
	
	private static void translateAndPrint(Reader r, OutputStream os) {
		if (valid) {
			String cnf = translate(r);
			if (valid)
				IOUtils.print(os, cnf, valid);
		}
	}
	
	private static String translate(Reader r) {
		
		DerivationTree tree = new DerivationTree(r);
		String dimacsCNF=null;
		valid=tree.validNNF();
		if (valid) {
			Encoding tseitinEncoding = new Encoding();
			tseitinEncoding.encode(tree);
			dimacsCNF = DimacsCNF.getDimacsCNF(tseitinEncoding.getClauses(), tree.getNNFVariables(), tseitinEncoding.getTseitinVarCount());
		}
		else {
			System.out.println(NOT_NNF);
		}
		return dimacsCNF;
	}
}