package dpll;

import java.util.ArrayList;
import java.util.List;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

//	TODO: 	equality on clauses; stack instead of recursion
//			total time;
//			organize project 

public class Dpll {

	private static final String DIMACS_EXT = ".cnf";
	private static final String SMT_LIB = ".sat";

	//private ArrayList<ArrayList<Assignment>>[] variableClausesEdges;
	private Boolean[] assignment;
	private List<TseitinVariableToken> variables;
	private boolean unsat = false;
	private int derivationCount = 0;
	private int decisionCount = 0;

	//
	public static void main(String[] args) {

		solve(args[0]);
	}

	public static Boolean[] solve(String inputFileName) {

		DimacsCNF dimacsCNF = createDimacsCNF(inputFileName);
		if (dimacsCNF != null) {
			ArrayList<ArrayList<Assignment>> clauses = dimacsCNF.getClauses();
			ArrayList<ArrayList<Assignment>>[] variableClausesEdges = dimacsCNF.createVariableClausesEdges();

			Dpll dpll = new Dpll(dimacsCNF.getVariables(), variableClausesEdges, clauses);
			return dpll.solve(clauses,variableClausesEdges);
		}
		return null;
	}

	private Dpll(ArrayList<TseitinVariableToken> variables, ArrayList<ArrayList<Assignment>>[] variableClausesEdges,
			ArrayList<ArrayList<Assignment>> clauses) {

		//this.variableClausesEdges = variableClausesEdges;
		this.variables = variables;
		assignment=new Boolean[variables.size()+1];
	}

	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT))
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;
	}

	private Boolean[] solve(ArrayList<ArrayList<Assignment>> clauses,
			ArrayList<ArrayList<Assignment>>[] variableClausesEdges) {
		solve(new State(clauses, new ArrayList<List<Assignment>>(), variableClausesEdges));
		if (unsat)
			return null;
		for (int i=0;i<assignment.length;i++)
			if (assignment[i]==null)
				assignment[i]=true;
		return assignment;
	}
	

	private void solve(State state) {
		
		unsat = false;
		for (List<Assignment> clause : state.clauses)
			if (clause.size() == 1)
				state.unitClauses.add(clause);
			else if (clause.size()==0)
				System.out.println();
		List<Integer> last = unitPropagation(state);
	

		
		if (state.clauses.isEmpty())
			return;
		if (unsat) {
			resetAssignment(last);
			return;
		}
		
		TseitinVariableToken var = null;
		for (TseitinVariableToken v : variables) // !!
			if (assignment[v.getIndex()] == null) {
				var = v;
				assignment[var.getIndex()] = true;
				break;
			}
		if (var == null) {
		
			return;
		}
		State stateT = new State(state.clauses, state.unitClauses, state.variableClausesEdges);
		State stateF = new State(state.clauses, state.unitClauses, state.variableClausesEdges);
		
		
		last.add(var.getIndex());
		
		infer(new Assignment(var, true), stateT);
		if (!unsat) 
			solve(stateT);
		if (!unsat) 
			return;
		
		unsat=false;
		assignment[var.getIndex()] = false;
		infer(new Assignment(var, false), stateF);
		if (!unsat) 
			solve(stateF);
		if (!unsat) 
			return;
		resetAssignment(last);	
	}

	private List<Integer> unitPropagation(State s) {

		List<Integer> last = new ArrayList<Integer>();
		while (!unsat) {
			if (s.unitClauses.isEmpty())
				break;
			List<Assignment> unitClause = s.unitClauses.remove(0);
			s.clauses.remove(unitClause);
			Assignment unit = unitClause.get(0);
			TseitinVariableToken unitVar = unit.getVariable();
			boolean unitValue = unit.getValue();
			assignment[unitVar.getIndex()] = unitValue;
			last.add(unitVar.getIndex());
			infer(unit, s);
		}
		return last;
	}

	private void resetAssignment(List<Integer> last) {
		for (Integer i : last)
			assignment[i] = null;
	}
	
	private void infer(Assignment unit, State s) {
		for (List<Assignment> clause : s.variableClausesEdges[unit.getVariable().getIndex()]) {
			for (int i = 0; i < clause.size(); i++) {
				Assignment a = clause.get(i);
				if (a.getVariable().equals(unit.getVariable())) {
					if (a.getValue() == unit.getValue())
						s.clauses.remove(clause);
					else {
						if (clause.size() == 1) {
							unsat = true;
							return; // UNSAT
						}
						clause.remove(i);
						if (clause.size() == 1) 
							s.unitClauses.add(clause);
					}
				}
			}
		}

	}


}
