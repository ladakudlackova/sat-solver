package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tseitin_to_dimacs.DimacsCNF;

//
public class DimacsFileUtils {

	private static final String INVALID_DIMACS_FILE	= "Invalid dimacs file.";
	
	public static List<Integer[]> processDimacsFile(String inputFileName) {

		Boolean valid = true;
		FileReader inputFReader = IOUtils.createReader(inputFileName, valid);
		if (!valid)
			return null;
		BufferedReader inputBReader = new BufferedReader(inputFReader);
		List<Integer[]> clauses = null;
		try {
			clauses = readClauses(inputBReader, valid);
		} catch (NumberFormatException | IOException e1) {
			IOUtils.reportError(INVALID_DIMACS_FILE, valid);
		}
		IOUtils.closeReader(inputBReader, valid);
		return clauses;
	}

	private static List<Integer[]> readClauses(BufferedReader dimacsReader, Boolean valid) throws IOException, NumberFormatException {

		String line;
		while ((line = dimacsReader.readLine()) != null)
			if (!line.startsWith(DimacsCNF.COMMENT)) {
				if (line.startsWith(DimacsCNF.HEADER))
					break;
				else {
					valid=false;
					return null;
				}
			}
		List<Integer[]> clauses = new ArrayList<Integer[]>();
		while ((line = dimacsReader.readLine()) != null) {
			String[] vars = line.split("\\s");
			int len = vars.length - 1;
			Integer[] varsNr = new Integer[len];
			for (int i = 0; i < len; i++)
				varsNr[i] = Integer.parseInt(vars[i]);
		}
		return clauses;
	}
}
