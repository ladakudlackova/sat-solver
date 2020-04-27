package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import dimacs.DimacsCNF;


public class DimacsFileUtils {

	private static final String INVALID_DIMACS_FILE	= "Invalid dimacs file.";
	
	public static DimacsCNF processDimacsFile(String inputFileName) {
		FileReader inputFReader = IOUtils.createReader(inputFileName);
		if (inputFReader!=null) {
			BufferedReader inputBReader = new BufferedReader(inputFReader);			
			return processDimacsReader(inputBReader);
		}
		return null;
	}
	
	public static List<Integer[]> processDimacsCnf(String clauses) {
		BufferedReader dimacsReader = new BufferedReader(new StringReader(clauses));
		return processClauses(dimacsReader);
	}
	
	private static DimacsCNF processDimacsReader(BufferedReader clausesReader) {	
		DimacsCNF dimacsCNF = createDimacsCNF(clausesReader);
		if (dimacsCNF==null)
			IOUtils.reportError(INVALID_DIMACS_FILE);
		IOUtils.closeReader(clausesReader);
		return dimacsCNF;
	}
	
	
	private static DimacsCNF createDimacsCNF(BufferedReader dimacsReader) {

		int varsCount =processHeader(dimacsReader);
		List<Integer[]> clauses = null;
		if (varsCount>-1)
			clauses = processClauses(dimacsReader);
		return new DimacsCNF(clauses, varsCount);
	}
	
	private static int processHeader(BufferedReader dimacsReader) {
		String line;
		try {
			while ((line = dimacsReader.readLine()) != null)
				if (!line.startsWith("c")) {
					if (line.startsWith(DimacsCNF.HEADER)) {
						String[] words = line.split("\\s+");
						if (words.length==4)
							return Integer.parseInt(words[2]);
						break;
					}
					else 
						break;
				}
		} catch (IOException e) {
		}
		return -1;
	}
	
	private static List<Integer[]> processClauses(BufferedReader dimacsReader) {
		
		List<Integer[]> clauses = new ArrayList<Integer[]>();
		String line;
		try {
			while ((line = dimacsReader.readLine()) != null) {
				if (line.equals("%"))
					break;
				Integer[]clause = processClause(line);
				clauses.add(clause);
			}
		} catch (IOException|NumberFormatException e) {
			return null;
		}
		return clauses;
	
	}
	
	private static Integer[] processClause(String line) {
		String[] vars = line.trim().split("\\s+");
		int len = vars.length - 1;
		Integer[] clause = new Integer[len];
		for (int i = 0; i < len; i++)
			clause[i] = Integer.parseInt(vars[i]);
		return clause;
	}
}
