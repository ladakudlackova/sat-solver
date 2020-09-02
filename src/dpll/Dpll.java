package dpll;

import java.util.ArrayList;
import java.util.List;

import clauses_base.ClauseBase;
import clauses_base.ClausesBase;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import watched_literals.ClauseWithWatches;
import watched_literals.ClausesWithWatches;

public class Dpll {

	private Boolean[] assignment;
	private ClausesBase clauses;
	private TseitinVariableToken[] variables;
	private boolean unsat = false;
	private int decisionCount = 0;
	private int unitPropagationSteps = 0;

	protected Dpll(TseitinVariableToken[] variables, ClausesBase clauses) {

		this.variables = variables;
		this.clauses = clauses;
		this.clauses.init(variables);
		assignment = new Boolean[variables.length];
	}
	
	protected Dpll(ClausesWithWatches clausesWithWatches, TseitinVariableToken[] variables) {

		this.variables = variables;
		this.clauses = clausesWithWatches;
		this.clauses.init(variables);
		assignment = new Boolean[variables.length];
		for (Assignment unit: clausesWithWatches.getAndRemoveUnitAssignment()) {
		
			assignment[unit.getVariable().getIndex()]=unit.getValue();
			clauses.setValue(unit);
		}
	}

	protected Boolean[] solveClauses() {
		
		solve();
		if (unsat)
			return null;
		for (int i = 0; i < assignment.length; i++)
			if (assignment[i] == null)
				assignment[i] = true;
		return assignment;
	}

	private void solve() {

		unsat = false;
		List<Integer> last = unitPropagation();
		if (unsat) {
			resetAssignment(last);
			return;
		}
		if (clauses.allSatisfied())
			return;
		TseitinVariableToken var = chooseVariable();
		if (var == null)
			return;
		last.add(var.getIndex());
		decisionCount++;
		decideAndSolve(var, true);		
		if (unsat) {		
			unsat = false;
			clauses.setFailed(false);
			clauses.resetValue(var);
			decideAndSolve(var, false);
			if (unsat)
				resetAssignment(last);
		}
	
	}

	private void decideAndSolve(TseitinVariableToken var, boolean value) {

		assignment[var.getIndex()] = value;
		infer(new Assignment(var, value));
		if (!unsat)
			solve();
	}

	private List<Integer> unitPropagation() {

		List<Integer> last = new ArrayList<Integer>();
		ClauseBase unitClause = clauses.getFirstUnitClause();
		while (!unsat && unitClause != null) {
			unitPropagationSteps++;
			Assignment unit = unitClause.getUnitAssignment();
			if (unit != null) {
				TseitinVariableToken unitVar = unit.getVariable();
				boolean unitValue = unit.getValue();
				assignment[unitVar.getIndex()] = unitValue;
				last.add(unitVar.getIndex());
				infer(unit);
			}
			unitClause = clauses.getFirstUnitClause();
		}
		return last;
	}

	private void infer(Assignment unit) {

		clauses.setValue(unit);
		unsat = clauses.failed();

	}

	private TseitinVariableToken chooseVariable() {
		
		for (int i = 1; i < variables.length; i++) {
			TseitinVariableToken var = variables[i];
			if (assignment[var.getIndex()] == null)
				return var;
		}
		return null;
	}

	private void resetAssignment(List<Integer> last) {
		for (Integer i : last)
			assignment[i] = null;
		clauses.resetValues(last, variables);
	}

	public int getDecisionCount() {
		return decisionCount;
	}

	public int getUnitPropagationSteps() {
		return unitPropagationSteps;
	}

}
