package dpll;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

//	TODO: 	clauses as object; solve;
//			total time;
//			organize project 


public class Dpll {

	private static final String DIMACS_EXT =".cnf";
	private static final String SMT_LIB =".sat";
	
	private static final File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "input","toy_5.sat").toFile();
	
	private HashMap<TseitinVariableToken, List<List<Assignment>>> variableClausesEdges;
	private List<List<Assignment>> clauses;
	private int derivationCount=0;
	private int decisionCount=0;
	
	// 
	public static void wip_run_test(String[] args) {
		
		
		DimacsCNF dimacsCNF = createDimacsCNF(DATA_INPUT_FOLDER.getPath().toString());
		if (dimacsCNF!=null) {
			List<List<Assignment>> clauses =  (List<List<Assignment>>) dimacsCNF.getClauses();
			HashMap<TseitinVariableToken, List<List<Assignment>>> variableClausesEdges = dimacsCNF.createVariableClausesEdges();
			Dpll dpll = new Dpll(variableClausesEdges, clauses);
		}
			
	}
	
	private Dpll(
			HashMap<TseitinVariableToken, List<List<Assignment>>> variableClausesEdges,
			List<List<Assignment>> clauses) {
		
		this.variableClausesEdges = variableClausesEdges;
		this.clauses = clauses;
		
	}
	
	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT)) 
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;			
	}
	
	
	
	private List<Assignment> solve(
			ArrayList<List<Assignment>> unitClauses,
			HashMap<TseitinVariableToken, List<Assignment>> assignment) {
		
		// TODO:
		// empty clauses 
		// unit propagation
		// nonsat
		// decide
		
		return null;
		
		
	}
	
	private List<Assignment> unitPropagation(
			ArrayList<List<Assignment>> unitClauses,
			List<Assignment> assignment) {
		
		while (true){
			if (unitClauses.isEmpty())
				return assignment;
			List<Assignment> unitClause = unitClauses.remove(0);
			clauses.remove(unitClause);
			Assignment unit= unitClause.get(0);
			assignment.add(unit);
			boolean unitValue = unit.getValue();
			TseitinVariableToken unitVar = unit.getVariable();
			for (List<Assignment> unitVarClause: variableClausesEdges.get(unit.getVariable())) {
													// TODO: method in Clause class
				Assignment varA;
				
				for (Assignment a:unitVarClause) {
					if (a.getVariable().equals(unitValue)) {
						// ...
					}
				}
			}
			return null;
		}
		
		
	}
	
	
}
