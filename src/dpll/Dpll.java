package dpll;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

//	TODO: 	don't copy state (inferred list)
//			total time;
//			organize project 


public class Dpll {

	private static final String DIMACS_EXT =".cnf";
	private static final String SMT_LIB =".sat";
	
	
	
	private ArrayList<ArrayList<Assignment>>[] variableClausesEdges;
	private List<TseitinVariableToken> variables;
	private int derivationCount=0;
	private int decisionCount=0;
	
	// 
		public static void main(String[] args) {
			
			solve("src\\test\\data\\input\\and.sat");
		}
		
		public static Boolean[] solve(String inputFileName) {
			
			DimacsCNF dimacsCNF = createDimacsCNF(inputFileName);
			if (dimacsCNF!=null) {
				ArrayList<ArrayList<Assignment>> clauses =  dimacsCNF.getClauses();
				ArrayList<ArrayList<Assignment>>[] variableClausesEdges = dimacsCNF.createVariableClausesEdges();
				
				Dpll dpll = new Dpll(dimacsCNF.getVariables(), variableClausesEdges, clauses);
				Boolean[] assignment = dpll.solve(clauses);
				return assignment;
			}				
			return null;
		}
	
	private Dpll(
			ArrayList<TseitinVariableToken> variables,
			ArrayList<ArrayList<Assignment>>[] variableClausesEdges,
			ArrayList<ArrayList<Assignment>> clauses) {
		
		this.variableClausesEdges = variableClausesEdges;
		this.variables=variables;
		
	}
	
	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT)) 
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;			
	}
	
	private Boolean[] solve(
			ArrayList<ArrayList<Assignment>> clauses){
		
		ArrayList<List<Assignment>> unitClauses = new ArrayList<List<Assignment>>();
		for (List<Assignment> clause:clauses)
			if (clause.size()==1)
				unitClauses.add(clause);
		Boolean[] assignment = new Boolean[variables.size()+1];
		return solve( 
				new State(unitClauses, assignment, clauses));
				
	}
	
	
	private Boolean[] solve(State state) {
		
		Boolean[] assignment = unitPropagation(state);
		if (state.clauses.isEmpty())
			return assignment;
		if (assignment==null)
			return null;
		TseitinVariableToken var=null;
		for (TseitinVariableToken v:variables)   //!!
			if (assignment[v.getIndex()]==null) {
				var=v;
				assignment[var.getIndex()]=true;
				break;
			}
		if (var==null)
			return assignment;
		
		ArrayList<List<Assignment>> unitClauses0= new ArrayList<List<Assignment>>(state.unitClauses);
		ArrayList<ArrayList<Assignment>> clauses0= new ArrayList<ArrayList<Assignment>>(state.clauses);
		boolean valid = trySetValue(var, true, state.unitClauses, state.clauses);
		if (valid) {
			assignment = solve(new State(state.unitClauses, assignment, state.clauses));
			if (assignment!=null) {
				assignment[var.getIndex()]=true;
				return assignment;
			}
		}
		
		valid = trySetValue(var, false, unitClauses0,  new ArrayList<ArrayList<Assignment>>(clauses0));
		
		if (valid) {
			state.assignment[var.getIndex()]=false;
			assignment = solve(new State(unitClauses0, state.assignment, clauses0));
			if (assignment!=null) {
				//assignment[var.getIndex()]=true;
				return assignment;
			}
		}
		variables.add(var);
		return null;
	}
	
	
	private Boolean[] unitPropagation(
				State state) {	
		
		while (true){
			if (state.unitClauses.isEmpty())
				return state.assignment;
			List<Assignment> unitClause = state.unitClauses.remove(0);
			state.clauses.remove(unitClause);
			Assignment unit= unitClause.get(0);
			state.assignment[unit.getVariable().getIndex()]= unit.getValue();
			TseitinVariableToken unitVar = unit.getVariable();
			boolean unitValue = unit.getValue();
			variables.remove(unitVar);
			boolean valid=trySetValue(unitVar, unitValue, state.unitClauses, state.clauses);
			if (valid) {
				state.assignment[unitVar.getIndex()]=unitValue;
				return state.assignment;
			}
			else
				return null;
			
			
		}		
	}
	
	private boolean trySetValue(TseitinVariableToken var,
			boolean value,
			ArrayList<List<Assignment>> unitClauses,
			ArrayList<ArrayList<Assignment>> clauses ) {
		
		for (List<Assignment> clause : variableClausesEdges[var.getIndex()]) {
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
