package tseitin_to_dimacs;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import dimacs.DimacsCNF;
import simple_nnf_tree.DerivationTree;
import tseitin.CNFDefinition.implicationType;
import utils.IOUtils;
import tseitin.Encoding;

public class Translation {

	private static final String NOT_NNF = "Input string is not nnf.";
	private static boolean valid = true;
	private static Encoding tseitinEncoding;

	public static void main(String[] args) {
		
		ArrayList<String> args_withoutOption = new ArrayList<String>();
		Boolean bothImplications = false;
		for (String arg : args) {
			if (arg.toLowerCase().equals("true") || arg.equals("1"))
				bothImplications = true;
			else if (arg.toLowerCase().equals("false") || arg.equals("0"))
				bothImplications = false;
			else
				args_withoutOption.add(arg);
		}
		switch (args_withoutOption.size()) {
		case 0:
			formula2cnf(bothImplications);
			return;
		case 1:
			formula2cnf(bothImplications, args_withoutOption.get(0));
			return;
		case 2:
			formula2cnf(bothImplications, args_withoutOption.get(0), args_withoutOption.get(1));
			return;
		default:
			System.out.println("Invalid count of parameters.");
			return;
		}

	}

	public static void formula2cnf(boolean bothImplications) {
		
		translateAndPrint(bothImplications, new InputStreamReader(System.in), System.out);
	}

	public static void formula2cnf(boolean bothImplications, String inputFileName) {
		
		Reader r = IOUtils.createReader(inputFileName);
		translateAndPrint(bothImplications, r, System.out);
	}

	public static void formula2cnf(boolean bothImplications, String inputFileName, String outputFileName) {
		
		Reader r = IOUtils.createReader(inputFileName);
		FileOutputStream fos = IOUtils.createOutputFile(outputFileName);
		translateAndPrint(bothImplications, r, fos);
	}
	
	public static String formula2cnfString(String nnf) {
		
		Reader r = new StringReader(nnf);
		valid = (r != null);
		if (valid) {
			DimacsCNF dimacsCNF = translate(r, true, false);
			return dimacsCNF.toString();
		}
		return null;
	}
	

	public static DimacsCNF formula2dimacsCNF(boolean bothImplications, String inputFileName,
			boolean watchedLiterals) {
		Reader r = IOUtils.createReader(inputFileName);
		return translate(r, bothImplications, watchedLiterals);
	}
	
	public static DimacsCNF formula2dimacsCNF(StringReader nnf, boolean watchedLiterals) {
		return translate(nnf, true, watchedLiterals);
	}

	private static void translateAndPrint(boolean bothImplications, Reader r, OutputStream os) {
		valid = (r != null && os != null);
		if (valid) {
			DimacsCNF dimacsCNF = translate(r, bothImplications, false);
			String cnf = dimacsCNF.toString();
			if (valid)
				IOUtils.print(os, cnf, valid);
		}
	}

	private static DimacsCNF translate(Reader r, boolean bothImplications, boolean watchedLiterals) {

		implicationType implType = bothImplications ? implicationType.EQUIVALENCE : implicationType.LEFT_TO_RIGHT;
		DerivationTree tree = new DerivationTree(r);
		valid = tree.validNNF();
		if (valid) {
			tseitinEncoding = new Encoding();
			tseitinEncoding.encode(tree, implType);
			return new DimacsCNF(tseitinEncoding.getClauses(), tree.getNNFVariables(),
					tseitinEncoding.getTseitinVariables(), watchedLiterals);
		}
		System.out.println(NOT_NNF);
		return null;
	}
}