package dpll;

import java.util.ArrayList;
import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class Dpll {
	
	private Boolean[] assignment;
	private List<TseitinVariableToken> variables;
	private boolean unsat = false;
	private int decisionCount = 0;
	private int unitPropagationSteps = 0;  // Steps or vars?
	
	
	protected Dpll(ArrayList<TseitinVariableToken> variables, ArrayList<ArrayList<Assignment>>[] variableClausesEdges,
			ArrayList<ArrayList<Assignment>> clauses) {

		this.variables = variables;
		assignment=new Boolean[variables.size()+1];
	}
	
	
	protected Boolean[] solve(ArrayList<ArrayList<Assignment>> clauses,
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
		List<Integer> last = unitPropagation(state);	
		int derivedCount = last.size();
		if (unsat) {
			resetAssignment(last);
			return;
		}	
		unitPropagationSteps=unitPropagationSteps+derivedCount;
		if (state.clauses.isEmpty())
			return;
		
		TseitinVariableToken var = chooseVariable();
		if (var == null) 
			return;
		last.add(var.getIndex());
		decisionCount = decisionCount + 1;
		decideAndSolve(state, var, true);
		if (!unsat) 
			return;
		unsat=false;
		decideAndSolve(state, var, false);
		if (!unsat) 
			return;
		unitPropagationSteps=unitPropagationSteps-derivedCount;
		resetAssignment(last);	
	}
	
	private void decideAndSolve(State state, TseitinVariableToken var, boolean value) {
		assignment[var.getIndex()] = value;
		State stateT = new State(state);
		infer(new Assignment(var, value), stateT);
		if (!unsat) 
			solve(stateT);
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
			if (assignment[unitVar.getIndex()]!=null)
				continue;
			assignment[unitVar.getIndex()] = unitValue;
			last.add(unitVar.getIndex());
			infer(unit, s);
		}
		return last;
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

	private TseitinVariableToken chooseVariable() {
		for (TseitinVariableToken v : variables) 
			if (assignment[v.getIndex()] == null) 
				return v;
		return null;
	}
	
	private void resetAssignment(List<Integer> last) {
		for (Integer i : last)
			assignment[i] = null;
	}


	
	public int getDecisionCount() {
		return decisionCount;
	}
	
	public int getUnitPropagationSteps() {
		return unitPropagationSteps;
	}


}
