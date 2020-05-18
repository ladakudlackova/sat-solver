package dpll;

import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public abstract class Clauses {
	
	protected int unsatisfiedCount = 0;
	
	protected boolean failed = false;	

	protected abstract void init(TseitinVariableToken[] variables);
	
	protected abstract Clause getFirstUnitClause();

	public abstract void addClause(Object[] intClause, TseitinVariableToken[] variables);
	
	public abstract void updateClauses(Assignment a);
	
	public abstract void resetValues(List<Integer> last, TseitinVariableToken[] variables);
	
	public abstract void resetValue(TseitinVariableToken var);

	public int getUnsatisfiedCount() {
		return unsatisfiedCount;
	}
	
	public void setValue(Assignment a) {
		
		TseitinVariableToken var=a.getVariable();
		boolean value=a.getValue();
		updateClauses(a);
		var.setValue(value);
	}

	public boolean failed() {
		return failed;
	}

	public void setFailed(boolean b) {
		failed = b;
	}

	

}

