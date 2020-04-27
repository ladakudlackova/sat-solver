package dpll;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dimacs.Clause;
import dimacs.Clauses;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class Dpll {
	
	private Boolean[] assignment;
	private Clauses clauses;
	private TseitinVariableToken[] variables;
	private boolean unsat = false;
	private int decisionCount = 0;
	private int unitPropagationSteps = 0;   
	
	
	protected Dpll(TseitinVariableToken[] variables, Clauses clauses) {

		this.variables = variables;
		this.clauses=clauses;
		this.clauses.createVariableClausesEdges(variables);
		assignment=new Boolean[variables.length];
	}
	
	protected Boolean[] solveXXX() {
		
		try {
		solve();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (unsat)
			return null;
		for (int i=0;i<assignment.length;i++)
			if (assignment[i]==null)
				assignment[i]=true;
		return assignment;
	}
	

	private void solve() {
		for (Boolean b:assignment)
			System.out.print(b+" ");
		System.out.println(clauses.getUnsatisfiedCount());
		unsat = false;	
		List<Integer> last = unitPropagation();	
		//int derivedCount = last.size();
		if (unsat) {
			resetAssignment(last);
			return;
		}	
		//unitPropagationSteps=unitPropagationSteps+derivedCount;
		if (clauses.getUnsatisfiedCount()==0)
			return;
		TseitinVariableToken var = chooseVariable();
		if (var == null) 
			return;
		last.add(var.getIndex());
		decisionCount = decisionCount + 1;
		decideAndSolve(var, true);
		if (!unsat) 
			return;
		unsat=false;
		clauses.setFailed(false);
		clauses.resetValue(var);
		decideAndSolve(var, false);
		if (!unsat) 
			return;
		//unitPropagationSteps=unitPropagationSteps-derivedCount;
		resetAssignment(last);	
	}
	
	private void decideAndSolve(TseitinVariableToken var, boolean value) {
		assignment[var.getIndex()] = value;
		infer(new Assignment(var, value));
		if (!unsat) 
			solve();
	}

	private List<Integer> unitPropagation() {

		List<Integer> last = new ArrayList<Integer>();
		Iterator<Clause> unitClauses;
		while (!unsat && clauses.getUnitClauses().hasNext()) {
			unitClauses=clauses.getUnitClauses();                // !!!
			Clause unitClause = unitClauses.next();
			unitClauses.remove();
			Assignment unit = unitClause.getUnitAssignment();
			TseitinVariableToken unitVar = unit.getVariable();
			boolean unitValue = unit.getValue();
			if (assignment[unitVar.getIndex()]!=null)
				continue;
			assignment[unitVar.getIndex()] = unitValue;
			last.add(unitVar.getIndex());
			infer(unit);
		}
		return last;
	}

	
	private void infer(Assignment unit) {
		
		clauses.setValue(unit);
		unsat=clauses.failed();
		
	}

	private TseitinVariableToken chooseVariable() {
		for (int i=1;i<variables.length;i++) {
			TseitinVariableToken var=variables[i];
			if (assignment[var.getIndex()] == null) 
				return var;
		}
		return null;
	}
	
	private void resetAssignment(List<Integer> last) {
		for (Integer i : last) {
			assignment[i] = null;
			
		}
		clauses.resetValues(last, variables);
	}


	
	public int getDecisionCount() {
		return decisionCount;
	}
	
	public int getUnitPropagationSteps() {
		return unitPropagationSteps;
	}


}
