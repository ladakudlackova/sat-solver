package dpll;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
	private List<List<Assignment>> clausesX;
	
	private List<TseitinVariableToken> variables;
	private int derivationCount=0;
	private int decisionCount=0;
	
	// 
	public static void main(String[] args) {
		
		
		DimacsCNF dimacsCNF = createDimacsCNF(DATA_INPUT_FOLDER.getPath().toString());
		if (dimacsCNF!=null) {
			List<List<Assignment>> clauses =  (List<List<Assignment>>) dimacsCNF.getClauses();
			HashMap<TseitinVariableToken, List<List<Assignment>>> variableClausesEdges = dimacsCNF.createVariableClausesEdges();
		
			
			//	Dpll dpll = new Dpll(variableClausesEdges, clauses);
		//	dpll.solve(clauses);
		}
			
	}
	
	private Dpll(
			HashMap<TseitinVariableToken, List<List<Assignment>>> variableClausesEdges,
			List<List<Assignment>> clauses) {
		
		this.variableClausesEdges = variableClausesEdges;
		this.variables=new ArrayList<TseitinVariableToken>(variableClausesEdges.keySet());
		this.clausesX = clauses;
		
	}
	
	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT)) 
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;			
	}
	
	private HashMap<TseitinVariableToken, Boolean> solve(
			List<List<Assignment>> clauses){
		
		return solve(
				new ArrayList<List<Assignment>>(),
				new HashMap<TseitinVariableToken, Boolean>(),
				clauses
				);
	}
	
	
	private HashMap<TseitinVariableToken, Boolean> solve(
			ArrayList<List<Assignment>> unitClauses,
			HashMap<TseitinVariableToken, Boolean> assignment,
			List<List<Assignment>> clauses) {
		
		assignment = unitPropagation(unitClauses, assignment, clauses);
		if (clauses.isEmpty())
			return assignment;
		if (assignment==null)
			return null;
		TseitinVariableToken var = variables.remove(0);
		
		ArrayList<List<Assignment>> unitClauses0= new ArrayList<List<Assignment>>(unitClauses);
		ArrayList<List<Assignment>> clauses0= new ArrayList<List<Assignment>>(clauses);
		boolean valid = trySetValue(var, true, unitClauses, clauses);
		if (valid) {
			assignment = solve(unitClauses, assignment, clauses0);
			if (assignment!=null) {
				assignment.put(var, true);
				return assignment;
			}
		}
		
		valid = trySetValue(var, false, unitClauses0,  new ArrayList<List<Assignment>>(clauses));
		if (valid) {
			assignment = solve(unitClauses0, assignment, clauses0);
			if (assignment!=null) {
				assignment.put(var, false);
				return assignment;
			}
		}
		return null;
	}
	
	private HashMap<TseitinVariableToken, Boolean> unitPropagation(
			ArrayList<List<Assignment>> unitClauses,
			HashMap<TseitinVariableToken, Boolean> assignment,
			List<List<Assignment>> clauses ){
		
		while (true){
			if (unitClauses.isEmpty())
				return assignment;
			List<Assignment> unitClause = unitClauses.remove(0);
			clauses.remove(unitClause);
			Assignment unit= unitClause.get(0);
			assignment.put(unit.getVariable(), unit.getValue());
			TseitinVariableToken unitVar = unit.getVariable();
			boolean unitValue = unit.getValue();
			variables.remove(unitVar);
			for (List<Assignment> unitVarClause: variableClausesEdges.get(unit.getVariable())) {
				for (int i=0;i<unitVarClause.size();i++) {
					Assignment a=unitVarClause.get(i);
					if (a.getVariable().equals(unitVar)) {
						if (a.getValue() == unitValue)
							clauses.remove(unitVarClause);
						else {
							unitVarClause.remove(i);
							if (unitVarClause.size()==1)
								unitClauses.add(unitVarClause);
							else if (unitVarClause.size()==0)
								return null; // UNSAT
						}
						break;
						}
				}
			}
		}		
	}
	
	private boolean trySetValue(TseitinVariableToken var,
			boolean value,
			ArrayList<List<Assignment>> unitClauses,
			List<List<Assignment>> clauses ) {
		
		for (List<Assignment> clause : variableClausesEdges.get(var)) {
			for (int i=0;i<clause.size();i++) {
				Assignment a=clause.get(i);
				if (a.getVariable().equals(var)) {
					if (a.getValue()==value)
						clauses.remove(clause);
					else {
						clause.remove(i);
						if (clause.size()==1)
							unitClauses.add(clause);
						else if (clause.size()==0)
							return false; // UNSAT
					}
				}
			}
		}
		return true;
	}
	
	
}
